package com.opc.web.service.twitter.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.utils.http.HttpUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMaterialMedia;
import com.opc.core.service.ICoreMaterialMediaService;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.config.opencli.OpenCliProperties;
import com.opc.web.config.twitter.v2.TwitterApiV2Properties;
import com.opc.web.dto.twitter.v2.TweetDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import kong.unirest.Config;
import kong.unirest.HttpResponse;
import kong.unirest.Proxy;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Twitter API v2 服务实现类
 * <p>
 * 使用 Unirest 实现 Twitter API v2 相关操作，包括搜索推文等功能
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class TwitterApiV2ServiceImpl implements TwitterApiV2Service {

    private static final Logger log = LoggerFactory.getLogger(TwitterApiV2ServiceImpl.class);

    private static final String SEARCH_RECENT_ENDPOINT = "/tweets/search/recent";

    @Autowired
    private TwitterApiV2Properties twitterApiV2Properties;

    @Autowired
    private OpenCliProperties openCliProperties;

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreMaterialMediaService materialMediaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        // 配置 Unirest
        Config config = Unirest.config()
                .connectTimeout(twitterApiV2Properties.getTimeout())
                .socketTimeout(twitterApiV2Properties.getTimeout())
                .concurrency(100, 20);

        // 设置代理（使用 opencli.httpProxy 配置，用于 HTTP API 请求）
        if (openCliProperties != null) {
            String proxyUrl = openCliProperties.getHttpProxyUrl();
            if (proxyUrl != null && !proxyUrl.isEmpty()) {
                try {
                    java.net.URL url = new java.net.URL(proxyUrl);
                    String host = url.getHost();
                    int port = url.getPort() > 0 ? url.getPort() : 7890;
                    Proxy proxy = new Proxy(host, port);
                    config.proxy(proxy);
                    log.info("Unirest HTTP 代理已启用: {}:{}", host, port);
                } catch (Exception e) {
                    log.warn("解析 HTTP 代理地址失败: {}", proxyUrl, e);
                }
            }
        }

        log.info("Unirest 初始化完成");
    }

    @PreDestroy
    public void shutdown() {
        Unirest.shutDown();
        log.info("Unirest 已关闭");
    }

    @Override
    public TwitterSearchResponseDTO searchRecentTweets(TwitterSearchRequestDTO request) {
        try {
            // 校验并修正 maxResults 参数（Twitter API 要求 10-100）
            if (request.getMaxResults() == null || request.getMaxResults() < 10) {
                log.warn("maxResults 参数值 [{}] 小于 Twitter API 最小值 10，已修正为 10", request.getMaxResults());
                request.setMaxResults(10);
            } else if (request.getMaxResults() > 100) {
                log.warn("maxResults 参数值 [{}] 大于 Twitter API 最大值 100，已修正为 100", request.getMaxResults());
                request.setMaxResults(100);
            }

            // 构建基础 URL
            String url = twitterApiV2Properties.getBaseUrl() + SEARCH_RECENT_ENDPOINT;
            log.info("调用 Twitter API v2 搜索接口: {}", url);

            // 构建请求 - 使用 queryString 方式构建参数
            kong.unirest.GetRequest getRequest = Unirest.get(url)
                    .queryString("query", request.getQuery())
                    .queryString("max_results", request.getMaxResults())
                    .queryString("sort_order", "relevancy")
                    .queryString("tweet.fields", "created_at,author_id,public_metrics,text,attachments,entities,lang")
                    .queryString("expansions", "attachments.media_keys,author_id")
                    .queryString("media.fields", "media_key,type,url,preview_image_url,alt_text,duration_ms,width,height")
                    .header("Authorization", "Bearer " + twitterApiV2Properties.getBearerToken());

            // 添加可选参数
            if (request.getNextToken() != null && !request.getNextToken().isEmpty()) {
                getRequest.queryString("next_token", request.getNextToken());
            }
            if (request.getStartTime() != null && !request.getStartTime().isEmpty()) {
                getRequest.queryString("start_time", request.getStartTime());
            }
            if (request.getEndTime() != null && !request.getEndTime().isEmpty()) {
                getRequest.queryString("end_time", request.getEndTime());
            }

            // 如果配置了 HTTP 代理，在请求级别也设置代理（确保使用代理）
            if (openCliProperties != null) {
                String proxyUrl = openCliProperties.getHttpProxyUrl();
                if (proxyUrl != null && !proxyUrl.isEmpty()) {
                    try {
                        java.net.URL proxyAddress = new java.net.URL(proxyUrl);
                        String host = proxyAddress.getHost();
                        int port = proxyAddress.getPort() > 0 ? proxyAddress.getPort() : 7890;
                        getRequest.proxy(host, port);
                        log.debug("请求已设置 HTTP 代理: {}:{}", host, port);
                    } catch (Exception e) {
                        log.warn("解析 HTTP 代理地址失败: {}", proxyUrl, e);
                    }
                }
            }

            HttpResponse<String> response = getRequest.asString();

            if (response.getStatus() == 200) {
                TwitterSearchResponseDTO result = objectMapper.readValue(
                        response.getBody(),
                        TwitterSearchResponseDTO.class
                );
                log.info("Twitter API 搜索成功，获取 {} 条推文",
                        result.getMeta() != null ? result.getMeta().getResultCount() : 0);

                // 提取推文信息并转换为素材
                List<TweetInfo> tweetInfoList = extractTweetInfo(result);

                // 打印每个推文的媒体类型和URL，并下载媒体文件
                for (TweetInfo info : tweetInfoList) {
                    log.info("推文ID: {}", info.getId());
                    log.info("推文URL: {}", info.getUrl());

                    // 获取媒体列表
                    List<TwitterSearchResponseDTO.Media> mediaList = info.getMediaList();

                    // 初始化URL映射
                    Map<String, String> urlMapping = new HashMap<>();

                    // 遍历媒体并下载
                    for (TwitterSearchResponseDTO.Media media : mediaList) {
                        log.info("  媒体类型: {}", media.getType());        // photo / video / animated_gif
                        log.info("  媒体URL: {}", media.getUrl());           // 图片直接URL
                        log.info("  预览图: {}", media.getPreviewImageUrl()); // 视频封面
                        log.info("  宽度: {}", media.getWidth());
                        log.info("  高度: {}", media.getHeight());

                        // 下载媒体文件
                        String mediaUrl = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
                        if (mediaUrl != null) {
                            String fileName = generateFileName(media);
                            String fileUrl = downloadMediaAndUpload(mediaUrl, fileName);
                            if (fileUrl != null) {
                                urlMapping.put(mediaUrl, fileUrl);
                                log.info("  已保存到: {}", fileUrl);
                            }
                        }
                    }

                    // 设置URL映射到TweetInfo
                    info.setMediaUrlToFileServicePath(urlMapping);
                    log.info("---");
                }

                List<CoreMaterial> materials = convertToMaterials(tweetInfoList);

                // 保存到数据库
                int savedCount = 0;
                for (int i = 0; i < materials.size(); i++) {
                    CoreMaterial material = materials.get(i);
                    TweetInfo tweetInfo = tweetInfoList.get(i);
                    try {
                        // 检查是否已存在（根据 originalId 去重）
                        CoreMaterial existMaterial = materialService.selectMaterialByOriginalId(material.getOriginalId());
                        if (existMaterial == null) {
                            materialService.insertMaterial(material);
                            savedCount++;
                            log.info("素材已保存到数据库，originalId: {}", material.getOriginalId());

                            // 保存媒体文件信息到 core_material_media
                            saveMaterialMedias(material.getId(), tweetInfo);
                        } else {
                            log.info("素材已存在，跳过保存，originalId: {}", material.getOriginalId());
                        }
                    } catch (Exception e) {
                        log.error("保存素材到数据库失败，originalId: {}", material.getOriginalId(), e);
                    }
                }
                log.info("共保存 {} 条素材到数据库", savedCount);

                return result;
            } else {
                log.error("Twitter API 返回非成功状态码: {}，响应: {}", response.getStatus(), response.getBody());
                throw new RuntimeException("Twitter API 返回非成功状态码: " + response.getStatus());
            }

        } catch (IOException e) {
            log.error("解析 Twitter API 响应失败", e);
            throw new RuntimeException("解析 Twitter API 响应失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("调用 Twitter API v2 搜索接口失败", e);
            throw new RuntimeException("调用 Twitter API v2 搜索接口失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String buildTweetUrl(String tweetId, String authorUsername) {
        if (authorUsername != null && !authorUsername.isEmpty()) {
            return String.format("https://x.com/%s/status/%s", authorUsername, tweetId);
        }
        return String.format("https://x.com/i/status/%s", tweetId);
    }

    /**
     * 从响应中获取推文 URL 和对应的媒体信息
     *
     * @param response Twitter API 响应
     * @return 推文信息列表，包含 URL 和媒体
     */
    public List<TweetInfo> extractTweetInfo(TwitterSearchResponseDTO response) {
        List<TweetInfo> result = new ArrayList<>();

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return result;
        }

        // 构建用户ID到用户名的映射
        Map<String, String> userIdToUsername = new HashMap<>();
        if (response.getIncludes() != null && response.getIncludes().getUsers() != null) {
            for (TwitterSearchResponseDTO.User user : response.getIncludes().getUsers()) {
                userIdToUsername.put(user.getId(), user.getUsername());
            }
        }

        // 构建媒体密钥到媒体信息的映射
        Map<String, TwitterSearchResponseDTO.Media> mediaKeyToMedia = new HashMap<>();
        if (response.getIncludes() != null && response.getIncludes().getMedia() != null) {
            for (TwitterSearchResponseDTO.Media media : response.getIncludes().getMedia()) {
                mediaKeyToMedia.put(media.getMediaKey(), media);
            }
        }

        // 遍历推文数据
        for (TweetDTO tweet : response.getData()) {
            TweetInfo info = new TweetInfo();
            info.setId(tweet.getId());
            info.setText(tweet.getText());
            info.setCreatedAt(tweet.getCreatedAt());

            // 构建推文 URL
            String authorUsername = userIdToUsername.get(tweet.getAuthorId());
            info.setUrl(buildTweetUrl(tweet.getId(), authorUsername));
            info.setAuthorUsername(authorUsername);

            // 获取媒体列表
            List<TwitterSearchResponseDTO.Media> mediaList = new ArrayList<>();
            if (tweet.getAttachments() != null && tweet.getAttachments().getMediaKeys() != null) {
                for (String mediaKey : tweet.getAttachments().getMediaKeys()) {
                    TwitterSearchResponseDTO.Media media = mediaKeyToMedia.get(mediaKey);
                    if (media != null) {
                        mediaList.add(media);
                    }
                }
            }
            info.setMediaList(mediaList);

            result.add(info);
        }

        return result;
    }

    /**
     * 推文信息封装类
     */
    public static class TweetInfo {
        private String id;
        private String text;
        private String url;
        private String createdAt;
        private String authorUsername;
        private List<TwitterSearchResponseDTO.Media> mediaList;
        /**
         * 媒体URL到文件服务器路径的映射
         */
        private Map<String, String> mediaUrlToFileServicePath;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAuthorUsername() {
            return authorUsername;
        }

        public void setAuthorUsername(String authorUsername) {
            this.authorUsername = authorUsername;
        }

        public List<TwitterSearchResponseDTO.Media> getMediaList() {
            return mediaList;
        }

        public void setMediaList(List<TwitterSearchResponseDTO.Media> mediaList) {
            this.mediaList = mediaList;
        }

        public Map<String, String> getMediaUrlToFileServicePath() {
            return mediaUrlToFileServicePath;
        }

        public void setMediaUrlToFileServicePath(Map<String, String> mediaUrlToFileServicePath) {
            this.mediaUrlToFileServicePath = mediaUrlToFileServicePath;
        }

        /**
         * 获取媒体 URL 列表（只包含图片/视频的直接 URL）
         */
        public List<String> getMediaUrls() {
            if (mediaList == null) {
                return new ArrayList<>();
            }
            return mediaList.stream()
                    .map(media -> media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl())
                    .filter(url -> url != null)
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "TweetInfo{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", authorUsername='" + authorUsername + '\'' +
                    ", mediaCount=" + (mediaList != null ? mediaList.size() : 0) +
                    '}';
        }
    }

    /**
     * 将推文信息列表转换为素材列表
     *
     * @param tweetInfoList 推文信息列表
     * @return 素材列表
     */
    public List<CoreMaterial> convertToMaterials(List<TweetInfo> tweetInfoList) {
        List<CoreMaterial> materials = new ArrayList<>();

        for (TweetInfo info : tweetInfoList) {
            CoreMaterial material = new CoreMaterial();

            // 设置原ID
            material.setOriginalId(info.getId());

            // 设置内容 - 移除推文中的 t.co URL，在末尾添加图片/视频HTML
            String content = info.getText();
            if (content != null) {
          /*      // 移除 t.co 短链接（通常是推文末尾的媒体链接）
                content = content.replaceAll("\\s*https?://t\\.co/\\w+\\s*$", "");

                // 如果有媒体文件，在末尾添加 HTML 标签
                if (info.getMediaUrlToFileServicePath() != null && !info.getMediaUrlToFileServicePath().isEmpty()) {
                    StringBuilder mediaHtml = new StringBuilder();
                    for (Map.Entry<String, String> entry : info.getMediaUrlToFileServicePath().entrySet()) {
                        String localPath = entry.getValue();
                        // 获取媒体信息
                        TwitterSearchResponseDTO.Media media = getMediaByUrl(entry.getKey(), info.getMediaList());
                        String mediaType = media != null ? media.getType() : "photo";


                        if ("video".equals(mediaType) || "animated_gif".equals(mediaType)) {
                            // 视频使用 video 标签
                            mediaHtml.append("<video src=\"").append(localPath).append("\" controls style=\"max-width:100%;\"></video><br>");
                        } else {
                            // 图片使用 img 标签，添加宽高属性
                            Integer width = media != null ? media.getWidth() : null;
                            Integer height = media != null ? media.getHeight() : null;
                            mediaHtml.append("<img src=\"").append(localPath).append("\"");
                            if (width != null) {
                                mediaHtml.append(" width=\"").append(width).append("\"");
                            }
                            if (height != null) {
                                mediaHtml.append(" height=\"").append(height).append("\"");
                            }
                            mediaHtml.append(" style=\"max-width:100%;\" /><br>");
                        }
                    }
                    content = content + "<br>" + mediaHtml.toString();
                }*/
            }
            material.setContent(content);

            // 设置标题：取 text 的前100位
            if (info.getText() != null && !info.getText().isEmpty()) {
                String title = info.getText().length() > 100 ? info.getText().substring(0, 100) : info.getText();
                material.setTitle(title);
            }

            // 设置作者
            material.setAuthor(info.getAuthorUsername());

            // 设置原链接
            material.setOriginalUrl(info.getUrl());

            // 设置来源为 Twitter
            material.setSource("twitter");

            // 设置内容类型
            if (info.getMediaList() != null && !info.getMediaList().isEmpty()) {
                String mediaType = info.getMediaList().get(0).getType();
                // 获取第一张媒体的文件服务器路径
                String firstMediaUrl = info.getMediaUrls().get(0);
                String fileServicePath = info.getMediaUrlToFileServicePath() != null
                        ? info.getMediaUrlToFileServicePath().get(firstMediaUrl)
                        : null;

                if ("video".equals(mediaType) || "animated_gif".equals(mediaType)) {
                    material.setContentType("video");
                    material.setVideoUrl(fileServicePath != null ? fileServicePath : firstMediaUrl);
                } else {
                    material.setContentType("image");
                }
            } else {
                material.setContentType("image");
            }

            // 设置默认状态为下线
            material.setStatus("1");

            // 设置默认套餐类型为普通会员
            material.setPackageType(1);

            materials.add(material);
        }

        return materials;
    }

    /**
     * 根据原始URL获取媒体类型
     */
    private String getMediaTypeByUrl(String url, List<TwitterSearchResponseDTO.Media> mediaList) {
        TwitterSearchResponseDTO.Media media = getMediaByUrl(url, mediaList);
        return media != null ? media.getType() : "photo";
    }

    /**
     * 根据原始URL获取媒体对象
     */
    private TwitterSearchResponseDTO.Media getMediaByUrl(String url, List<TwitterSearchResponseDTO.Media> mediaList) {
        if (mediaList == null) {
            return null;
        }
        for (TwitterSearchResponseDTO.Media media : mediaList) {
            String mediaUrl = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
            if (url.equals(mediaUrl)) {
                return media;
            }
        }
        return null;
    }

    /**
     * 生成文件名
     */
    private String generateFileName(TwitterSearchResponseDTO.Media media) {
        String extension = getExtension(media);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 根据媒体类型获取扩展名
     */
    private String getExtension(TwitterSearchResponseDTO.Media media) {
        String url = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
        if (url == null) {
            return "jpg";
        }

        // 从URL中提取扩展名
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            return "jpg";
        } else if (url.contains(".png")) {
            return "png";
        } else if (url.contains(".gif")) {
            return "gif";
        } else if (url.contains(".mp4")) {
            return "mp4";
        }

        // 根据类型默认
        if ("video".equals(media.getType())) {
            return "mp4";
        } else if ("animated_gif".equals(media.getType())) {
            return "gif";
        }
        return "jpg";
    }

    /**
     * 下载媒体文件并上传到文件服务器
     *
     * @return 文件服务器返回的URL
     */
    private String downloadMediaAndUpload(String mediaUrl, String fileName) {
        File tempFile = null;
        try {
            // 下载文件到临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            tempFile = new File(tempDir, fileName);

            // 下载文件（使用 HTTP 代理）
            URL url = new URL(mediaUrl);
            InputStream in;
            String proxyUrl = openCliProperties != null ? openCliProperties.getHttpProxyUrl() : null;
            if (proxyUrl != null && !proxyUrl.isEmpty()) {
                // 使用 HTTP 代理
                try {
                    java.net.URL proxyAddress = new java.net.URL(proxyUrl);
                    String host = proxyAddress.getHost();
                    int port = proxyAddress.getPort() > 0 ? proxyAddress.getPort() : 7890;
                    java.net.Proxy proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP,
                            new InetSocketAddress(host, port));
                    in = url.openConnection(proxy).getInputStream();
                    log.debug("使用 HTTP 代理下载媒体文件: {}:{}", host, port);
                } catch (Exception e) {
                    log.warn("解析 HTTP 代理地址失败，直接连接: {}", proxyUrl, e);
                    in = url.openStream();
                }
            } else {
                // 直接连接
                in = url.openStream();
            }

            try (ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

            // 使用通用方法上传到文件服务器
            String fileUrl = HttpUtils.uploadToFileServer(tempFile, fileName);
            if (fileUrl != null) {
                log.info("文件上传成功: {}", fileUrl);
            } else {
                log.error("文件上传到文件服务器失败");
            }
            return fileUrl;

        } catch (Exception e) {
            log.error("下载或上传媒体文件失败: {}", mediaUrl, e);
            return null;
        } finally {
            // 清理临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * 保存素材媒体文件信息到 core_material_media
     *
     * @param materialId 素材ID
     * @param tweetInfo  推文信息
     */
    private void saveMaterialMedias(Long materialId, TweetInfo tweetInfo) {
        if (tweetInfo.getMediaList() == null || tweetInfo.getMediaList().isEmpty()) {
            return;
        }

        Map<String, String> urlMapping = tweetInfo.getMediaUrlToFileServicePath();
        if (urlMapping == null || urlMapping.isEmpty()) {
            return;
        }

        int sortOrder = 0;
        for (TwitterSearchResponseDTO.Media media : tweetInfo.getMediaList()) {
            String originalUrl = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
            if (originalUrl == null) {
                continue;
            }

            String fileUrl = urlMapping.get(originalUrl);
            if (fileUrl == null) {
                continue;
            }

            String mediaType = "photo".equals(media.getType()) ? "image" :
                    ("video".equals(media.getType()) || "animated_gif".equals(media.getType())) ? "video" : "image";

            try {
                materialMediaService.saveMaterialMedia(
                        materialId,
                        mediaType,
                        fileUrl,
                        sortOrder++
                );
                log.info("媒体文件已保存到 core_material_media, materialId: {}, type: {}", materialId, mediaType);
            } catch (Exception e) {
                log.error("保存媒体文件信息失败, materialId: {}, url: {}", materialId, originalUrl, e);
            }
        }
    }
}
