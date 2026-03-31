package com.opc.web.dto.twitter.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * Twitter API v2 搜索响应 DTO
 * <p>
 * 对应 Twitter API v2 /tweets/search/recent 接口的响应结构
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Schema(description = "Twitter API v2 搜索响应")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterSearchResponseDTO {

    @Schema(description = "推文数据列表")
    private List<TweetDTO> data;

    @Schema(description = "元数据信息（分页等）")
    private Meta meta;

    @Schema(description = "扩展数据（如作者信息、媒体等）")
    private Includes includes;

    @Schema(description = "错误信息")
    private List<Error> errors;

    /**
     * 元数据内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        @Schema(description = "结果总数", example = "10")
        @JsonProperty("result_count")
        private Integer resultCount;

        @Schema(description = "下一页令牌", example = "b26v89c19zqg8o3fosbs7l4ecm0k8v0mxv7nucx1d0e9p")
        @JsonProperty("next_token")
        private String nextToken;

        @Schema(description = "上一页令牌", example = "77qpmmws2z9p8v0mxv7nucx1d0e9pb26v89c19zqg8o3")
        @JsonProperty("previous_token")
        private String previousToken;

        @Schema(description = "最新推文ID", example = "1234567890123456789")
        @JsonProperty("newest_id")
        private String newestId;

        @Schema(description = "最旧推文ID", example = "1234567890123456700")
        @JsonProperty("oldest_id")
        private String oldestId;

        public Integer getResultCount() {
            return resultCount;
        }

        public void setResultCount(Integer resultCount) {
            this.resultCount = resultCount;
        }

        public String getNextToken() {
            return nextToken;
        }

        public void setNextToken(String nextToken) {
            this.nextToken = nextToken;
        }

        public String getPreviousToken() {
            return previousToken;
        }

        public void setPreviousToken(String previousToken) {
            this.previousToken = previousToken;
        }

        public String getNewestId() {
            return newestId;
        }

        public void setNewestId(String newestId) {
            this.newestId = newestId;
        }

        public String getOldestId() {
            return oldestId;
        }

        public void setOldestId(String oldestId) {
            this.oldestId = oldestId;
        }
    }

    /**
     * 扩展数据内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Includes {

        @Schema(description = "用户列表")
        private List<User> users;

        @Schema(description = "媒体列表")
        private List<Media> media;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<Media> getMedia() {
            return media;
        }

        public void setMedia(List<Media> media) {
            this.media = media;
        }
    }

    /**
     * 用户信息内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {

        @Schema(description = "用户ID", example = "1234567890123456789")
        private String id;

        @Schema(description = "用户名", example = "twitter")
        private String username;

        @Schema(description = "显示名称", example = "Twitter")
        private String name;

        @Schema(description = "头像URL")
        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @Schema(description = "认证状态")
        private Boolean verified;

        @Schema(description = "描述")
        private String description;

        @Schema(description = "公开指标")
        @JsonProperty("public_metrics")
        private UserPublicMetrics publicMetrics;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public UserPublicMetrics getPublicMetrics() {
            return publicMetrics;
        }

        public void setPublicMetrics(UserPublicMetrics publicMetrics) {
            this.publicMetrics = publicMetrics;
        }
    }

    /**
     * 用户公开指标内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserPublicMetrics {

        @Schema(description = "关注者数")
        @JsonProperty("followers_count")
        private Integer followersCount;

        @Schema(description = "关注数")
        @JsonProperty("following_count")
        private Integer followingCount;

        @Schema(description = "推文数")
        @JsonProperty("tweet_count")
        private Integer tweetCount;

        @Schema(description = "列表数")
        @JsonProperty("listed_count")
        private Integer listedCount;

        @Schema(description = "喜欢数")
        @JsonProperty("like_count")
        private Integer likeCount;

        public Integer getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(Integer followersCount) {
            this.followersCount = followersCount;
        }

        public Integer getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(Integer followingCount) {
            this.followingCount = followingCount;
        }

        public Integer getTweetCount() {
            return tweetCount;
        }

        public void setTweetCount(Integer tweetCount) {
            this.tweetCount = tweetCount;
        }

        public Integer getListedCount() {
            return listedCount;
        }

        public void setListedCount(Integer listedCount) {
            this.listedCount = listedCount;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }
    }

    /**
     * 媒体信息内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Media {

        @Schema(description = "媒体密钥")
        @JsonProperty("media_key")
        private String mediaKey;

        @Schema(description = "媒体类型", example = "photo")
        private String type;

        @Schema(description = "图片/视频URL")
        private String url;

        @Schema(description = "预览图片URL（视频类型）")
        @JsonProperty("preview_image_url")
        private String previewImageUrl;

        @Schema(description = "替代文本")
        @JsonProperty("alt_text")
        private String altText;

        @Schema(description = "时长（毫秒，视频类型）")
        @JsonProperty("duration_ms")
        private Integer durationMs;

        @Schema(description = "宽度")
        private Integer width;

        @Schema(description = "高度")
        private Integer height;

        public String getMediaKey() {
            return mediaKey;
        }

        public void setMediaKey(String mediaKey) {
            this.mediaKey = mediaKey;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPreviewImageUrl() {
            return previewImageUrl;
        }

        public void setPreviewImageUrl(String previewImageUrl) {
            this.previewImageUrl = previewImageUrl;
        }

        public String getAltText() {
            return altText;
        }

        public void setAltText(String altText) {
            this.altText = altText;
        }

        public Integer getDurationMs() {
            return durationMs;
        }

        public void setDurationMs(Integer durationMs) {
            this.durationMs = durationMs;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

    /**
     * 错误信息内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {

        @Schema(description = "错误标题", example = "Invalid Request")
        private String title;

        @Schema(description = "错误类型", example = "https://api.twitter.com/2/problems/invalid-request")
        private String type;

        @Schema(description = "错误详情", example = "One or more parameters to your request was invalid.")
        private String detail;

        @Schema(description = "HTTP 状态码", example = "400")
        private Integer status;

        @Schema(description = "错误参数")
        private Map<String, Object> parameters;

        @Schema(description = "错误消息", example = "Invalid query")
        private String message;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public List<TweetDTO> getData() {
        return data;
    }

    public void setData(List<TweetDTO> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Includes getIncludes() {
        return includes;
    }

    public void setIncludes(Includes includes) {
        this.includes = includes;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
