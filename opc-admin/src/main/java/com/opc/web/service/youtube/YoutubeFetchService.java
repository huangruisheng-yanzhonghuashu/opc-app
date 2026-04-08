package com.opc.web.service.youtube;

import com.fasterxml.jackson.databind.JsonNode;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.http.HttpUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialMediaService;
import com.opc.web.service.common.AbstractCollectFetchService;
import com.opc.web.service.common.opecli.OpenCliCommandBuilder;
import com.opc.web.service.translate.TranslateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.opc.web.service.common.opecli.OpenCliConstants.*;

/**
 * YouTube 数据获取服务
 * <p>
 * 封装 YouTube 的数据获取、视频下载和导入逻辑
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class YoutubeFetchService extends AbstractCollectFetchService {

    @Autowired
    private ICoreMaterialMediaService materialMediaService;

    /**
     * 搜索 YouTube 视频并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchYoutubeData(String keyword) {
        try {
            String sourceType = SOURCE_YOUTUBE;

            // 执行 opencli 命令获取 YouTube 搜索数据
            String jsonResult = executeOpenCliYoutubeCommand(keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 YouTube 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseYoutubeJson(jsonResult, sourceType);
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
            log.error("YouTube 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 下载封面图并上传到文件服务器（支持代理）
     */
    private String downloadAndUploadThumbnail(String thumbnailUrl, String videoId) {
        HttpURLConnection connection = null;
        try {
            // 构建下载目录
            String downloadPath = buildDownloadPath(videoId);
            String thumbnailFileName = videoId + "_thumbnail.jpg";
            File thumbnailFile = new File(downloadPath, thumbnailFileName);

            // 下载封面图
            log.info("下载封面图: {} -> {}", thumbnailUrl, thumbnailFile.getAbsolutePath());

            URL url = new URL(thumbnailUrl);

            // 获取代理配置
            Proxy proxy = getHttpProxy();
            if (proxy != null) {
                log.info("使用代理下载封面图: {}", proxy);
                connection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("封面图下载失败，HTTP状态码: {}", responseCode);
                return null;
            }

            // 读取图片数据
            byte[] imageData;
            try (InputStream in = connection.getInputStream()) {
                imageData = in.readAllBytes();
            }

            if (imageData == null || imageData.length == 0) {
                log.error("封面图下载失败，数据为空: {}", thumbnailUrl);
                return null;
            }

            // 保存到本地
            java.nio.file.Files.write(thumbnailFile.toPath(), imageData);
            log.info("封面图下载成功: {}, 大小: {} 字节", thumbnailFile.getAbsolutePath(), imageData.length);

            // 上传到文件服务器
            String uploadedUrl = HttpUtils.uploadToFileServer(thumbnailFile, thumbnailFileName);
            if (uploadedUrl != null) {
                log.info("封面上传成功: {}", uploadedUrl);
                return uploadedUrl;
            } else {
                log.error("封面上传失败: {}", thumbnailFileName);
            }
        } catch (Exception e) {
            log.error("下载或上传封面图失败: {}", thumbnailUrl, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * 获取 HTTP 代理配置
     */
    private Proxy getHttpProxy() {
        if (openCliProperties == null) {
            return null;
        }

        String proxyUrl = openCliProperties.getHttpProxyUrl();
        if (proxyUrl == null || proxyUrl.isEmpty()) {
            return null;
        }

        try {
            // 解析代理地址，格式如 http://127.0.0.1:7890
            URL url = new URL(proxyUrl);
            String host = url.getHost();
            int port = url.getPort() > 0 ? url.getPort() : 7890;

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            log.debug("使用 HTTP 代理: {}:{}", host, port);
            return proxy;
        } catch (Exception e) {
            log.warn("解析 HTTP 代理地址失败: {}", proxyUrl, e);
            return null;
        }
    }

    /**
     * 执行 opencli youtube search 命令
     */
    private String executeOpenCliYoutubeCommand(String keyword) throws Exception {
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(MODULE_YOUTUBE)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
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

                // 使用 youtube video 命令获取视频详情（包括高清封面）
                String originalUrl = material.getOriginalUrl();
                if (originalUrl != null && !originalUrl.isEmpty()) {
                    CoreMaterial videoDetail = fetchVideoDetailByUrl(originalUrl, originalId, material.getSource());
                    if (videoDetail != null) {
                        // 更新素材信息（标题、作者、封面等）
                        if (videoDetail.getOriginalTitle() != null) {
                            material.setOriginalTitle(videoDetail.getOriginalTitle());
                            material.setTitle(videoDetail.getTitle());
                        }
                        if (videoDetail.getAuthor() != null) {
                            material.setAuthor(videoDetail.getAuthor());
                        }
                        if (videoDetail.getCoverImage() != null) {
                            material.setCoverImage(videoDetail.getCoverImage());
                        }
                    }
                }

                // 下载并上传封面图（优先处理）
                String thumbnailUrl = material.getCoverImage();
                if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                    String uploadedCoverUrl = downloadAndUploadThumbnail(thumbnailUrl, originalId);
                    if (uploadedCoverUrl != null) {
                        material.setCoverImage(uploadedCoverUrl);
                        log.info("素材 {} 封面上传成功: {}", originalId, uploadedCoverUrl);
                    }
                }

                // 下载视频并上传
                List<String> uploadedUrls = new ArrayList<>();
                if (originalUrl != null && !originalUrl.isEmpty()) {
                    uploadedUrls = downloadAndUploadVideo(originalUrl, originalId);
                    if (!uploadedUrls.isEmpty()) {
                        log.info("素材 {} 上传了 {} 个文件到文件服务器", originalId, uploadedUrls.size());
                        material.setVideoUrl(uploadedUrls.get(0));
                    }
                }

                // 设置内容类型并保存素材
                material.setContentType(CONTENT_TYPE_VIDEO);
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
     * 使用 opencli youtube video 命令获取视频详情
     */
    private CoreMaterial fetchVideoDetailByUrl(String videoUrl, String videoId, String sourceType) {
        try {
            // 执行 opencli youtube video 命令获取视频详情
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                    .withModule(MODULE_YOUTUBE)
                    .withSubCommand("video")
                    .withArg(videoUrl)
                    .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

            String jsonResult = executeCommand(builder);
            log.info("获取到视频详情: {}", jsonResult);

            // 解析返回的 field-value 格式数据
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            if (rootNode.isArray() && isFieldValueFormat(rootNode)) {
                JsonNode convertedNode = convertFieldValueToObject(rootNode);
                CoreMaterial material = convertYoutubeToMaterial(convertedNode, sourceType);
                if (material != null) {
                    // 确保 videoId 和 originalUrl 设置正确
                    material.setOriginalId(videoId);
                    material.setOriginalUrl(videoUrl);
                    return material;
                }
            }
        } catch (Exception e) {
            log.warn("获取视频详情失败: {}", videoUrl, e);
        }
        return null;
    }

    /**
     * 使用 yt-dlp 下载视频并上传到文件服务器
     */
    private List<String> downloadAndUploadVideo(String videoUrl, String videoId) {
        List<String> uploadedUrls = new ArrayList<>();
        try {
            String downloadPath = buildDownloadPath(videoId);
            log.info("使用 yt-dlp 下载视频, URL: {}, 下载路径: {}", videoUrl, downloadPath);

            OpenCliCommandBuilder builder = OpenCliCommandBuilder
                    .buildYtDlpDownloadWithConfigProxy(videoUrl, downloadPath, openCliProperties);

            String result = executeCommand(builder);
            log.info("yt-dlp 下载命令执行成功, URL: {}, 结果: {}", videoUrl, result);

            // 上传下载目录中的文件到文件服务器
            File downloadDir = new File(downloadPath);
            if (downloadDir.exists() && downloadDir.isDirectory()) {
                File[] files = downloadDir.listFiles();
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
                    log.warn("下载目录下没有文件: {}", downloadPath);
                }
            } else {
                log.warn("下载目录不存在: {}", downloadPath);
            }

        } catch (Exception e) {
            log.error("下载视频失败, URL: {}", videoUrl, e);
        }
        return uploadedUrls;
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
    private String buildDownloadPath(String videoId) {
        String currentDir = System.getProperty("user.dir");
        String downloadDir = currentDir + "/youtube_downloads/" + videoId + "/";

        java.io.File dir = new java.io.File(downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return downloadDir;
    }

    /**
     * 解析 YouTube JSON 数据
     * 支持两种格式：
     * 1. search 命令返回的普通对象数组：[{"title": "...", "thumbnail": "..."}]
     * 2. video 命令返回的 field-value 数组：[{"field": "thumbnail", "value": "..."}]
     */
    private List<CoreMaterial> parseYoutubeJson(String youtubeJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(youtubeJson);

        if (rootNode.isArray()) {
            // 判断是否是 video 命令返回的 field-value 格式
            if (isFieldValueFormat(rootNode)) {
                // 转换为普通对象格式
                JsonNode convertedNode = convertFieldValueToObject(rootNode);
                CoreMaterial material = convertYoutubeToMaterial(convertedNode, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            } else {
                // search 命令返回的普通对象数组
                for (JsonNode node : rootNode) {
                    CoreMaterial material = convertYoutubeToMaterial(node, sourceType);
                    if (material != null) {
                        materials.add(material);
                    }
                }
            }
        } else {
            CoreMaterial material = convertYoutubeToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 判断是否是 video 命令返回的 field-value 格式
     */
    private boolean isFieldValueFormat(JsonNode arrayNode) {
        if (arrayNode.size() == 0) {
            return false;
        }
        JsonNode firstNode = arrayNode.get(0);
        return firstNode.has("field") && firstNode.has("value");
    }

    /**
     * 将 field-value 数组转换为普通对象
     * [{"field": "thumbnail", "value": "..."}] -> {"thumbnail": "..."}
     */
    private JsonNode convertFieldValueToObject(JsonNode fieldValueArray) {
        Map<String, String> map = new HashMap<>();
        for (JsonNode node : fieldValueArray) {
            String field = getTextValue(node, "field");
            String value = getTextValue(node, "value");
            if (field != null && value != null) {
                map.put(field, value);
            }
        }
        return objectMapper.valueToTree(map);
    }

    /**
     * 将 YouTube JSON 节点转换为 CoreMaterial
     */
    private CoreMaterial convertYoutubeToMaterial(JsonNode node, String sourceType) {
        if (node == null) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置原标题和翻译后的标题
        String originalTitle = getTextValue(node, "title");
        material.setOriginalTitle(originalTitle != null ? originalTitle : "YouTube Video");
        String translatedTitle = TranslateUtils.autoTranslateToChinese(originalTitle != null ? originalTitle : "YouTube Video");
        material.setTitle(translatedTitle);

        // 设置作者（频道）
        material.setAuthor(getTextValue(node, "channel"));

        // 设置原始 URL
        String url = getTextValue(node, "url");
        material.setOriginalUrl(url);

        // 从 URL 提取视频 ID 作为 originalId
        String videoId = extractVideoId(url);
        material.setOriginalId(videoId != null ? videoId : url);

        // 设置封面图片
        String thumbnail = getTextValue(node, "thumbnail");
        if (thumbnail != null && !thumbnail.isEmpty()) {
            material.setCoverImage(thumbnail);
        }

        // 设置内容描述（originalContent存储原始描述，content存储翻译后的内容）
/*        String originalContent = buildContentDescription(node);
        material.setOriginalContent(originalContent);
        String translatedContent = TranslateUtils.autoTranslateToChinese(originalContent);
        material.setContent(translatedContent);*/

        // 设置观看数（移除 "次观看" 等字符，只保留数字）
       /* String viewsStr = getTextValue(node, "views");
        if (viewsStr != null) {
            try {
                String numericViews = viewsStr.replaceAll("[^0-9]", "");
                if (!numericViews.isEmpty()) {
                    material.setViewCount(Long.parseLong(numericViews));
                }
            } catch (NumberFormatException e) {
                log.warn("解析观看数失败: {}", viewsStr);
            }
        }*/

        // 设置其他字段
        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        material.setContentType(CONTENT_TYPE_VIDEO);
        material.setSource(sourceType != null ? sourceType : SOURCE_YOUTUBE);

        return material;
    }

    /**
     * 构建内容描述
     */
    private String buildContentDescription(JsonNode node) {
        StringBuilder content = new StringBuilder();

        // 添加标题
        String title = getTextValue(node, "title");
        if (title != null && !title.isEmpty()) {
            content.append("标题: ").append(title).append("\n");
        }

        // 添加频道
        String channel = getTextValue(node, "channel");
        if (channel != null && !channel.isEmpty()) {
            content.append("频道: ").append(channel).append("\n");
        }

        // 添加描述
        String description = getTextValue(node, "description");
        if (description != null && !description.isEmpty()) {
            content.append("描述: ").append(description).append("\n");
        }

        // 添加观看数
        String views = getTextValue(node, "views");
        if (views != null && !views.isEmpty()) {
            content.append("观看数: ").append(views).append("\n");
        }

        // 添加时长
        String duration = getTextValue(node, "duration");
        if (duration != null && !duration.isEmpty()) {
            content.append("时长: ").append(duration).append("\n");
        }

        return content.toString().trim();
    }

    /**
     * 从 YouTube URL 中提取视频 ID
     */
    private String extractVideoId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            // 处理 https://www.youtube.com/watch?v=VIDEO_ID
            if (url.contains("v=")) {
                String[] parts = url.split("v=");
                if (parts.length > 1) {
                    String id = parts[1];
                    // 处理可能存在的其他参数
                    int ampersandIndex = id.indexOf("&");
                    if (ampersandIndex > 0) {
                        id = id.substring(0, ampersandIndex);
                    }
                    return id;
                }
            }
            // 处理 https://youtu.be/VIDEO_ID
            if (url.contains("youtu.be/")) {
                String[] parts = url.split("youtu.be/");
                if (parts.length > 1) {
                    String id = parts[1];
                    // 处理可能存在的其他参数
                    int questionIndex = id.indexOf("?");
                    if (questionIndex > 0) {
                        id = id.substring(0, questionIndex);
                    }
                    return id;
                }
            }
        } catch (Exception e) {
            log.warn("提取 YouTube 视频 ID 失败: {}", url);
        }
        return null;
    }
}
