package com.opc.web.service.twitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.http.HttpUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialMediaService;
import com.opc.web.service.common.opecli.OpenCliCommandBuilder;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import com.opc.web.service.common.AbstractCollectFetchService;
import com.opc.web.service.twitter.v2.TwitterApiV2Service;
import com.opc.web.service.translate.TranslateUtils;
import com.opc.common.utils.ShortUrlResolver;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.opc.web.service.common.opecli.OpenCliConstants.*;

/**
 * Twitter 数据获取服务
 * <p>
 * 封装 Twitter 的数据获取和导入逻辑
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class TwitterFetchService extends AbstractCollectFetchService {

    @Autowired
    private ICoreMaterialMediaService materialMediaService;

    @Autowired
    private TwitterApiV2Service twitterApiV2Service;

    /**
     * 使用 Twitter API v2 获取数据
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchTwitterDataByApiV2(String keyword) {
        try {
            TwitterSearchRequestDTO request = new TwitterSearchRequestDTO();
            request.setQuery(keyword);
            request.setMaxResults(10);

            TwitterSearchResponseDTO response = twitterApiV2Service.searchRecentTweets(request);

            int resultCount = (response.getMeta() != null) ? response.getMeta().getResultCount() : 0;

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("keyword", keyword);
            ajaxResult.put("sourceType", SOURCE_TWITTER);
            ajaxResult.put("apiVersion", "v2");
            ajaxResult.put("resultCount", resultCount);
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取 Twitter 数据并导入（使用 opencli）
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchTwitterData(String keyword) {
        try {
            String sourceType = SOURCE_TWITTER;

            // 执行 opencli 命令获取 Twitter 数据
            String jsonResult = executeOpenCliCommand(MODULE_TWITTER, SUBCOMMAND_SEARCH, keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Twitter 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseTwitterJson(jsonResult, sourceType);
            ImportResult result = importMaterialsWithMedia(materials);

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("keyword", keyword);
            ajaxResult.put("sourceType", sourceType);
            ajaxResult.put("apiVersion", "opencli");
            ajaxResult.put("total", result.total);
            ajaxResult.put("successCount", result.successCount);
            ajaxResult.put("failCount", result.failCount);
            return ajaxResult;

        } catch (Exception e) {
            log.error("Twitter 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名获取 Twitter 数据并导入（使用 opencli）
     *
     * @param userName Twitter 用户名
     * @return 导入结果
     */
    public AjaxResult fetchTwitterDataByUserName(String userName) {
        try {
            String sourceType = SOURCE_TWITTER;
            String query = "from:" + userName;

            String jsonResult = executeOpenCliCommand(MODULE_TWITTER, SUBCOMMAND_SEARCH, query);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Twitter 数据");
            }

            List<CoreMaterial> materials = parseTwitterJson(jsonResult, sourceType);
            ImportResult result = importMaterialsWithMedia(materials);

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("userName", userName);
            ajaxResult.put("sourceType", sourceType);
            ajaxResult.put("apiVersion", "opencli");
            ajaxResult.put("query", query);
            ajaxResult.put("total", result.total);
            ajaxResult.put("successCount", result.successCount);
            ajaxResult.put("failCount", result.failCount);
            return ajaxResult;

        } catch (Exception e) {
            log.error("Twitter 根据用户名搜索导入失败，用户名: {}", userName, e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入素材列表（带媒体处理）
     */
    private ImportResult importMaterialsWithMedia(List<CoreMaterial> materials) {
        int successCount = 0;
        int failCount = 0;
        int skipCount = 0;

        for (CoreMaterial material : materials) {
            String originalId = material.getOriginalId();
            try {
                // 根据 originId 判断数据是否已存在
                if (originalId != null && !originalId.isEmpty()) {
                    CoreMaterial existingMaterial = materialService.selectMaterialByOriginalId(originalId);
                    if (existingMaterial != null) {
                        log.info("素材已存在，跳过导入, originalId: {}", originalId);
                        skipCount++;
                        continue;
                    }
                }

                // 解析短链接，检测是否是文章URL
                String originalUrl = material.getOriginalUrl();
                boolean isArticle = false;
                List<String> mediaUrls = extractUrls(material.getContent());
                //没法解析文章或图和视频的短链接
                /*for (String mediaUrl : mediaUrls) {
                    String resolvedUrl = resolveShortUrl(mediaUrl);
                    log.debug("解析短链接: {} -> {}", mediaUrl, resolvedUrl);
                    String newResolvedUrl = resolveShortUrl(resolvedUrl);
                    log.debug("解析短链接: {} -> {}", newResolvedUrl, resolvedUrl);

                    // 检测是否是 Twitter/X 文章 URL
                    if (isTwitterArticleUrl(newResolvedUrl)) {
                        log.info("检测到 Twitter 文章 URL: {}", newResolvedUrl);
                        // 获取文章并转换为 HTML
                        String articleHtml = fetchTwitterArticleAsHtml(newResolvedUrl);
                        if (articleHtml != null && !articleHtml.isEmpty()) {
                            material.setContent(articleHtml);
                            material.setOriginalContent(articleHtml);
                            material.setMaterialType("article");
                            isArticle = true;
                        }
                    }
                }*/

                // 执行带代理的下载命令并上传文件，获取内容类型
                String contentType = CONTENT_TYPE_TEXT;
                List<String> uploadedUrls = new ArrayList<>();
                if (!isArticle && originalUrl != null && !originalUrl.isEmpty() && !mediaUrls.isEmpty()) {
                    uploadedUrls = downloadWithProxyAndUpload(originalUrl, material.getOriginalId());
                    if (!uploadedUrls.isEmpty()) {
                        log.info("素材 {} 上传了 {} 个文件到文件服务器", material.getOriginalId(), uploadedUrls.size());
                        contentType = determineContentType(uploadedUrls);
                    }
                } else if (isArticle) {
                    contentType = "article";
                }

                // 设置内容类型并保存素材
                material.setContentType(contentType);
                materialService.insertMaterial(material);
                Long materialId = material.getId();

                // 保存素材媒体文件信息
                if (!uploadedUrls.isEmpty()) {
                    saveMaterialMediaFiles(materialId, uploadedUrls);
                }

                successCount++;
            } catch (Exception e) {
                log.error("导入素材失败, originalId: {}", originalId, e);
                failCount++;
            }
        }

        log.info("导入完成: 总计={}, 成功={}, 跳过={}, 失败={}", materials.size(), successCount, skipCount, failCount);
        return new ImportResult(materials.size(), successCount, failCount, skipCount);
    }

    /**
     * 判断是否是 Twitter/X 文章 URL
     * 例如: https://x.com/thedankoe/article/2010042119121957316
     */
    private boolean isTwitterArticleUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return url.matches("https?://(x\\.com|twitter\\.com)/[^/]+/article/\\d+");
    }

    /**
     * 获取 Twitter 文章内容并转换为 HTML
     * 执行命令: opencli twitter article <articleId> -f md
     *
     * @param articleUrl 文章URL
     * @return HTML 格式的内容
     */
    private String fetchTwitterArticleAsHtml(String articleUrl) {
        try {
            // 从 URL 中提取文章 ID
            String articleId = extractArticleId(articleUrl);
            if (articleId == null || articleId.isEmpty()) {
                log.error("无法从 URL 中提取文章 ID: {}", articleUrl);
                return null;
            }

            // 构建命令: opencli twitter article <articleId> -f md
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                    .withModule(MODULE_TWITTER)
                    .withSubCommand(SUBCOMMAND_ARTICLE)
                    .withArg(articleId)
                    .withOption(OPTION_FORMAT_JSON, "md");

            builder.applyProxyFromConfig(openCliProperties);

            String markdownContent = executeCommand(builder);
            if (markdownContent == null || markdownContent.isEmpty()) {
                log.error("获取 Twitter 文章失败: {}", articleUrl);
                return null;
            }

            // 将 Markdown 转换为 HTML
            String htmlContent = markdownToHtml(markdownContent);
            log.info("成功获取并转换 Twitter 文章: {}, 内容长度: {}", articleUrl, htmlContent.length());
            return htmlContent;

        } catch (Exception e) {
            log.error("获取 Twitter 文章失败, URL: {}", articleUrl, e);
            return null;
        }
    }

    /**
     * 从文章 URL 中提取文章 ID
     * 例如: https://x.com/thedankoe/article/2010042119121957316 -> 2010042119121957316
     */
    private String extractArticleId(String articleUrl) {
        if (articleUrl == null || articleUrl.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("/article/(\\d+)");
        Matcher matcher = pattern.matcher(articleUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 将 Markdown 转换为 HTML
     */
    private String markdownToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdown));
    }

    /**
     * 执行 opencli 命令获取数据
     */
    private String executeOpenCliCommand(String module, String subCommand, String keyword) throws Exception {
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(module)
                .withSubCommand(subCommand)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
    }

    /**
     * 执行带代理的下载命令（使用 opencli twitter download）
     */
    private List<String> downloadWithProxyAndUpload(String originalUrl, String originalId) {
        List<String> uploadedUrls = new ArrayList<>();
        try {
            String downloadPath = buildDownloadPath(originalId);
            log.info("执行带代理的下载命令, URL: {}, 下载路径: {}", originalUrl, downloadPath);

            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                    .withModule(MODULE_TWITTER)
                    .withSubCommand(SUBCOMMAND_DOWNLOAD)
                    .withOption(PARAM_TWEET_URL, originalUrl)
                    .withOption(PARAM_OUTPUT, downloadPath);

            builder.applyProxyFromConfig(openCliProperties);

            String result = executeCommand(builder);
            log.info("下载命令执行成功, URL: {}, 下载路径: {}, 结果: {}", originalUrl, downloadPath, result);

            // 上传下载目录下tweets文件夹中的文件到文件服务器
            String tweetsPath = downloadPath + "tweets/";
            File tweetsDir = new File(tweetsPath);
            if (tweetsDir.exists() && tweetsDir.isDirectory()) {
                File[] files = tweetsDir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String url = HttpUtils.uploadToFileServer(file, file.getName());
                            if (url != null) {
                                uploadedUrls.add(url);
                                log.info("文件上传成功: {}, URL: {}", file.getName(), url);
                            } else {
                                log.error("文件上传失败: {}", file.getName());
                            }
                        }
                    }
                } else {
                    log.warn("tweets目录下没有文件: {}", tweetsPath);
                }
            } else {
                log.warn("tweets目录不存在: {}", tweetsPath);
            }

        } catch (Exception e) {
            log.error("下载命令执行失败, URL: {}", originalUrl, e);
        }
        return uploadedUrls;
    }

    /**
     * 使用 yt-dlp 执行带代理的下载命令
     */
    public void downloadWithYtDlp(String originalUrl, String originalId) {
        try {
            String downloadPath = buildDownloadPath(originalId);
            log.info("使用 yt-dlp 执行下载命令, URL: {}, 下载路径: {}", originalUrl, downloadPath);

            OpenCliCommandBuilder builder = OpenCliCommandBuilder
                    .buildYtDlpDownloadWithConfigProxy(originalUrl, downloadPath, openCliProperties);

            String result = executeCommand(builder);
            log.info("yt-dlp 下载命令执行成功, URL: {}, 下载路径: {}, 结果: {}", originalUrl, downloadPath, result);

        } catch (Exception e) {
            log.error("yt-dlp 下载命令执行失败, URL: {}", originalUrl, e);
        }
    }

    /**
     * 保存素材媒体文件信息
     */
    private void saveMaterialMediaFiles(Long materialId, List<String> uploadedUrls) {
        if (materialId == null || uploadedUrls == null || uploadedUrls.isEmpty()) {
            return;
        }
        try {
            int sortOrder = 1;
            for (String fileUrl : uploadedUrls) {
                String mediaType = determineMediaType(fileUrl);
                materialMediaService.saveMaterialMedia(materialId, mediaType, fileUrl, sortOrder++);
                log.info("保存素材媒体文件成功, materialId: {}, mediaType: {}, url: {}", materialId, mediaType, fileUrl);
            }
        } catch (Exception e) {
            log.error("保存素材媒体文件失败, materialId: {}", materialId, e);
        }
    }

    /**
     * 根据文件URL列表判断内容类型
     */
    private String determineContentType(List<String> uploadedUrls) {
        if (uploadedUrls == null || uploadedUrls.isEmpty()) {
            return CONTENT_TYPE_TEXT;
        }
        boolean hasVideo = false;
        boolean hasImage = false;
        for (String fileUrl : uploadedUrls) {
            String mediaType = determineMediaType(fileUrl);
            if ("video".equals(mediaType)) {
                hasVideo = true;
            } else {
                hasImage = true;
            }
        }
        if (hasVideo) {
            return CONTENT_TYPE_VIDEO;
        } else if (hasImage) {
            return CONTENT_TYPE_IMAGE;
        } else {
            return CONTENT_TYPE_TEXT;
        }
    }

    /**
     * 根据文件URL判断媒体类型
     */
    private String determineMediaType(String fileUrl) {
        if (fileUrl == null) {
            return "image";
        }
        String lowerUrl = fileUrl.toLowerCase();
        if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".avi") || lowerUrl.endsWith(".mov")
                || lowerUrl.endsWith(".wmv") || lowerUrl.endsWith(".flv") || lowerUrl.endsWith(".mkv")
                || lowerUrl.endsWith(".webm") || lowerUrl.contains("/video/")) {
            return "video";
        }
        return "image";
    }

    /**
     * 构建下载目录路径
     */
    private String buildDownloadPath(String originalId) {
        String currentDir = System.getProperty("user.dir");
        String downloadDir = currentDir + "/twitter_downloads/" + originalId + "/";

        java.io.File dir = new java.io.File(downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return downloadDir;
    }

    /**
     * 解析 Twitter JSON 数据
     */
    private List<CoreMaterial> parseTwitterJson(String twitterJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(twitterJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertTwitterToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            CoreMaterial material = convertTwitterToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 将 Twitter JSON 节点转换为 CoreMaterial
     */
    private CoreMaterial convertTwitterToMaterial(JsonNode node, String sourceType) {
        if (node == null || !node.has("id")) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();
        material.setOriginalId(getTextValue(node, "id"));

        String originalText = getTextValue(node, "text");
        material.setOriginalContent(originalText);
        // 翻译为中文后保存到 content
        String translatedText = TranslateUtils.autoTranslateToChinese(originalText);
        material.setContent(translatedText);

        // 设置原标题和翻译后的标题
        if (originalText != null && !originalText.isEmpty()) {
            String originalTitle = originalText.length() > 100 ? originalText.substring(0, 100) : originalText;
            material.setOriginalTitle(originalTitle);
            // 翻译标题
            String translatedTitle = TranslateUtils.autoTranslateToChinese(originalTitle);
            material.setTitle(translatedTitle);
        }

        material.setAuthor(getTextValue(node, "author"));
        material.setOriginalUrl(getTextValue(node, "url"));
        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        material.setSource(sourceType != null ? sourceType : SOURCE_TWITTER);

        return material;
    }

    /**
     * 从文本中提取所有 URL
     */
    private List<String> extractUrls(String text) {
        List<String> urls = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return urls;
        }
        Pattern pattern = Pattern.compile("https?://[^\\s<\"'<>]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }

    /**
     * 解析短链接为完整 URL
     */
    private String resolveShortUrl(String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            return shortUrl;
        }
        try {
            return ShortUrlResolver.expandShortUrl(shortUrl);
        } catch (Exception e) {
            log.debug("解析短链接失败: {}, 错误: {}", shortUrl, e.getMessage());
            return shortUrl;
        }
    }
}
