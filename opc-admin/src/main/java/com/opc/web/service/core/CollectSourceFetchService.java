package com.opc.web.service.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.http.HttpUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialMediaService;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.config.opencli.OpenCliProperties;
import com.opc.web.controller.common.OpenCliCommandBuilder;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import com.opc.web.service.twitter.v2.TwitterApiV2Service;
import com.opc.common.utils.ShortUrlResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.opc.web.controller.common.OpenCliConstants.*;

/**
 * 采集信息源数据获取服务
 * <p>
 * 封装 Twitter 和 Reddit 的数据获取和导入逻辑，供多个控制器复用
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class CollectSourceFetchService {

    private static final Logger log = LoggerFactory.getLogger(CollectSourceFetchService.class);

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreMaterialMediaService materialMediaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OpenCliProperties openCliProperties;

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
            ImportResult result = importMaterials(materials);

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
     * <p>
     * 执行命令: opencli twitter search "from:userName" --limit 10 -f json
     * </p>
     *
     * @param userName Twitter 用户名
     * @return 导入结果
     */
    public AjaxResult fetchTwitterDataByUserName(String userName) {
        try {
            String sourceType = SOURCE_TWITTER;

            // 构建 from:username 查询格式
            String query = "from:" + userName;

            // 执行 opencli 命令获取 Twitter 数据
            String jsonResult = executeOpenCliCommand(MODULE_TWITTER, SUBCOMMAND_SEARCH, query);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Twitter 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseTwitterJson(jsonResult, sourceType);
            ImportResult result = importMaterials(materials);

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
     * 获取 Reddit 数据并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchRedditData(String keyword) {
        try {
            String sourceType = SOURCE_REDDIT;

            // 执行 opencli reddit search 命令获取数据
            String jsonResult = executeOpenCliRedditCommand(keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Reddit 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseRedditJson(jsonResult, sourceType);
            ImportResult result = importMaterials(materials);

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("keyword", keyword);
            ajaxResult.put("sourceType", sourceType);
            ajaxResult.put("total", result.total);
            ajaxResult.put("successCount", result.successCount);
            ajaxResult.put("failCount", result.failCount);
            return ajaxResult;

        } catch (Exception e) {
            log.error("Reddit 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入素材列表
     */
    private ImportResult importMaterials(List<CoreMaterial> materials) {
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

                // 解析短链接
                List<String> mediaUrls = extractUrls(material.getContent());
                for (String mediaUrl : mediaUrls) {
                    String resolvedUrl = resolveShortUrl(mediaUrl);
                    log.debug("解析短链接: {} -> {}", mediaUrl, resolvedUrl);
                }

                // 执行带代理的下载命令并上传文件，获取内容类型
                String contentType = CONTENT_TYPE_TEXT;
                List<String> uploadedUrls = new ArrayList<>();
                String originalUrl = material.getOriginalUrl();
                if (originalUrl != null && !originalUrl.isEmpty()) {
                    uploadedUrls = downloadWithProxyAndUpload(originalUrl, material.getOriginalId());
                    if (!uploadedUrls.isEmpty()) {
                        log.info("素材 {} 上传了 {} 个文件到文件服务器", material.getOriginalId(), uploadedUrls.size());
                        contentType = determineContentType(uploadedUrls);
                    }
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
     * 执行带代理的下载命令（使用 opencli twitter download）
     *
     * @param originalUrl 原始URL（Twitter推文链接）
     * @param originalId  素材原始ID
     * @return 上传后的文件URL列表
     */
    private List<String> downloadWithProxyAndUpload(String originalUrl, String originalId) {
        List<String> uploadedUrls = new ArrayList<>();
        try {
            // 构建下载目录路径
            String downloadPath = buildDownloadPath(originalId);
            log.info("执行带代理的下载命令, URL: {}, 下载路径: {}", originalUrl, downloadPath);

            // 使用配置中的代理执行下载命令
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                    .withModule(MODULE_TWITTER)
                    .withSubCommand(SUBCOMMAND_DOWNLOAD)
                    .withOption(PARAM_TWEET_URL, originalUrl)
                    .withOption(PARAM_OUTPUT, downloadPath);

            // 应用配置中的代理设置
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
            // 下载失败不影响素材导入，只记录日志
        }
        return uploadedUrls;
    }

    /**
     * 使用 yt-dlp 执行带代理的下载命令
     *
     * @param originalUrl 原始URL（视频链接，支持 Twitter/X、YouTube 等）
     * @param originalId  素材原始ID
     */
    private void downloadWithYtDlp(String originalUrl, String originalId) {
        try {
            // 构建下载目录路径
            String downloadPath = buildDownloadPath(originalId);
            log.info("使用 yt-dlp 执行下载命令, URL: {}, 下载路径: {}", originalUrl, downloadPath);

            // 使用 yt-dlp 下载命令（自动应用配置代理）
            OpenCliCommandBuilder builder = OpenCliCommandBuilder
                    .buildYtDlpDownloadWithConfigProxy(originalUrl, downloadPath, openCliProperties);

            String result = executeCommand(builder);
            log.info("yt-dlp 下载命令执行成功, URL: {}, 下载路径: {}, 结果: {}", originalUrl, downloadPath, result);

        } catch (Exception e) {
            log.error("yt-dlp 下载命令执行失败, URL: {}", originalUrl, e);
            // 下载失败不影响素材导入，只记录日志
        }
    }

    /**
     * 保存素材媒体文件信息
     *
     * @param materialId  素材ID
     * @param uploadedUrls 上传后的文件URL列表
     */
    private void saveMaterialMediaFiles(Long materialId, List<String> uploadedUrls) {
        if (materialId == null || uploadedUrls == null || uploadedUrls.isEmpty()) {
            return;
        }
        try {
            int sortOrder = 1;
            for (String fileUrl : uploadedUrls) {
                // 根据文件扩展名判断媒体类型
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
     *
     * @param uploadedUrls 上传后的文件URL列表
     * @return 内容类型（video/image/text）
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
        // 有视频=video，有图片无视频=image，无媒体=text
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
     *
     * @param fileUrl 文件URL
     * @return 媒体类型（image/video）
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
     *
     * @param originalId 素材原始ID
     * @return 下载目录完整路径
     */
    private String buildDownloadPath(String originalId) {
        // 使用应用当前目录 + twitter_downloads/originalId 子目录
        String currentDir = System.getProperty("user.dir");
        String downloadDir = currentDir + "/twitter_downloads/" + originalId + "/";

        // 确保目录存在
        java.io.File dir = new java.io.File(downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return downloadDir;
    }

    /**
     * 执行 opencli 命令获取数据
     */
    private String executeOpenCliCommand(String module, String subCommand, String keyword) throws Exception {
        // 使用命令构建器构建命令（传入配置以获取可执行路径）
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(module)
                .withSubCommand(subCommand)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
    }

    /**
     * 执行 opencli 命令获取数据（带代理）
     */
    private String executeOpenCliCommandWithProxy(String module, String subCommand, String keyword, String proxyUrl) throws Exception {
        // 使用命令构建器构建命令，添加代理支持
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(module)
                .withSubCommand(subCommand)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
                .withProxy(proxyUrl);

        return executeCommand(builder);
    }

    /**
     * 执行 opencli reddit search 命令
     */
    private String executeOpenCliRedditCommand(String keyword) throws Exception {
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_SORT, SORT_HOT)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
    }

    /**
     * 执行命令并返回输出（自动应用配置中的代理设置）
     */
    private String executeCommand(OpenCliCommandBuilder builder) throws Exception {
        ProcessBuilder processBuilder = builder.createProcessBuilder(openCliProperties);

        // 获取实际应用的代理设置（builder 中的优先级高于配置）
        String effectiveProxy = builder.getProxyUrl();
        if (effectiveProxy == null || effectiveProxy.isEmpty()) {
            if (openCliProperties != null && openCliProperties.getProxy() != null && openCliProperties.getProxy().isEnabled()) {
                effectiveProxy = openCliProperties.getProxyUrl();
            }
        }

        // 打印实际设置的环境变量（用于调试）
        String proxyDebug = (effectiveProxy != null && !effectiveProxy.isEmpty()) ? effectiveProxy : "未启用";
        if (effectiveProxy != null && !effectiveProxy.isEmpty()) {
            Map<String, String> env = processBuilder.environment();
            String httpProxy = env.get("HTTP_PROXY");
            String httpsProxy = env.get("HTTPS_PROXY");
            log.debug("环境变量 HTTP_PROXY={}, HTTPS_PROXY={}", httpProxy, httpsProxy);
        }

        log.info("执行命令: {} (OS: {}, 代理: {})",
                builder.toFullCommandString(),
                builder.isWindows() ? "Windows" : "Unix",
                proxyDebug);

        // 调试：打印完整的命令列表
        if (log.isDebugEnabled()) {
            log.debug("ProcessBuilder 命令: {}", processBuilder.command());
        }

        Process process = processBuilder.start();

        // 读取命令输出
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("命令执行失败，退出码: {}", exitCode);
            throw new RuntimeException("opencli 命令执行失败，退出码: " + exitCode);
        }

        String result = output.toString().trim();
        log.info("获取到 {} 字节数据", result.length());
        return result;
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
     * 解析 Reddit JSON 数据
     */
    private List<CoreMaterial> parseRedditJson(String redditJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(redditJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertRedditToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            CoreMaterial material = convertRedditToMaterial(rootNode, sourceType);
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

        // 设置内容
        String originalText = getTextValue(node, "text");
        //String text = convertNewLineToBr(originalText);
        //material.setContent(text);
        material.setContent(originalText);

        // 设置标题：取 text 的前100位
        if (originalText != null && !originalText.isEmpty()) {
            String title = originalText.length() > 100 ? originalText.substring(0, 100) : originalText;
            material.setTitle(title);
        }

        material.setAuthor(getTextValue(node, "author"));
        material.setOriginalUrl(getTextValue(node, "url"));

        // 设置点赞数
        if (node.has("likes") && !node.get("likes").isNull()) {
            //material.setLikeCount(node.get("likes").asLong());
        }

        // 设置查看数
        if (node.has("views") && !node.get("views").isNull()) {
           /* String viewsStr = node.get("views").asText().replaceAll(",", "");
            try {
                material.setViewCount(Long.parseLong(viewsStr));
            } catch (NumberFormatException e) {
                material.setViewCount(0L);
            }*/
        }

        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        //material.setContentType(CONTENT_TYPE_TEXT);
        material.setSource(sourceType != null ? sourceType : SOURCE_TWITTER);

        return material;
    }

    /**
     * 将 Reddit JSON 节点转换为 CoreMaterial
     */
    private CoreMaterial convertRedditToMaterial(JsonNode node, String sourceType) {
        if (node == null) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置标题
        String title = getTextValue(node, "title");
        material.setTitle(title != null ? title : "Reddit 内容");

        material.setAuthor(getTextValue(node, "author"));
        material.setOriginalUrl(getTextValue(node, "url"));

        // 使用 url 作为 originalId
        String url = getTextValue(node, "url");
        if (url != null) {
            String originalId = extractRedditPostId(url);
            material.setOriginalId(originalId != null ? originalId : url);

            // 获取详细内容
            String detailText = fetchRedditDetailContent(url);
            String content = detailText != null && !detailText.isEmpty() ? detailText : (title != null ? title : "");
            // 将换行符转换为 HTML <br> 标签
            material.setContent(convertNewLineToBr(content));
        } else {
            material.setContent(convertNewLineToBr(title != null ? title : ""));
        }

        // 设置点赞数
        if (node.has("score") && !node.get("score").isNull()) {
            material.setLikeCount(node.get("score").asLong());
        }

        // 设置评论数
        if (node.has("comments") && !node.get("comments").isNull()) {
            material.setCommentCount(node.get("comments").asLong());
        }

        material.setViewCount(0L);
        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        material.setContentType(CONTENT_TYPE_TEXT);
        material.setSource(sourceType != null ? sourceType : SOURCE_REDDIT);

        return material;
    }

    /**
     * 获取 Reddit 帖子详细内容
     */
    private String fetchRedditDetailContent(String postUrl) {
        try {
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                    .withModule(MODULE_REDDIT)
                    .withSubCommand(SUBCOMMAND_READ)
                    .withArg(postUrl)
                    .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

            String result = executeCommand(builder);
            JsonNode rootNode = objectMapper.readTree(result);

            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstNode = rootNode.get(0);
                if (firstNode.has("text") && !firstNode.get("text").isNull()) {
                    String text = firstNode.get("text").asText();
                    log.info("获取到 Reddit 帖子内容: {} 字符, URL: {}", text.length(), postUrl);
                    return text;
                }
            }

            log.warn("Reddit 详情返回数据格式异常");
            return null;

        } catch (Exception e) {
            log.error("获取 Reddit 详情失败, URL: {}", postUrl, e);
            return null;
        }
    }

    /**
     * 从 Reddit URL 中提取帖子ID
     */
    private String extractRedditPostId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            String[] parts = url.split("/");
            for (int i = 0; i < parts.length; i++) {
                if ("comments".equals(parts[i]) && i + 1 < parts.length) {
                    return parts[i + 1];
                }
            }
        } catch (Exception e) {
            log.warn("提取 Reddit 帖子ID失败: {}", url);
        }
        return null;
    }

    /**
     * 安全获取文本值
     */
    private String getTextValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
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

    /**
     * 将换行符转换为 HTML <br> 标签
     */
    private String convertNewLineToBr(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
    }

    /**
     * 导入结果内部类
     */
    private static class ImportResult {
        final int total;
        final int successCount;
        final int failCount;
        final int skipCount;

        ImportResult(int total, int successCount, int failCount, int skipCount) {
            this.total = total;
            this.successCount = successCount;
            this.failCount = failCount;
            this.skipCount = skipCount;
        }
    }
}
