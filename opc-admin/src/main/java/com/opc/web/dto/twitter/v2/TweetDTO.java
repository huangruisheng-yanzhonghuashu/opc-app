package com.opc.web.dto.twitter.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Twitter 推文数据 DTO
 * <p>
 * 对应 Twitter API v2 返回的推文数据结构
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Schema(description = "Twitter 推文数据")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetDTO {

    @Schema(description = "推文ID", example = "1234567890123456789")
    private String id;

    @Schema(description = "推文内容", example = "Hello World!")
    private String text;

    @Schema(description = "作者ID", example = "9876543210987654321")
    @JsonProperty("author_id")
    private String authorId;

    @Schema(description = "创建时间", example = "2024-01-15T10:30:00.000Z")
    @JsonProperty("created_at")
    private String createdAt;

    @Schema(description = "公开指标数据（点赞、转发、回复、引用数）")
    @JsonProperty("public_metrics")
    private PublicMetrics publicMetrics;

    @Schema(description = "来源", example = "Twitter Web App")
    private String source;

    @Schema(description = "语言", example = "en")
    private String lang;

    @Schema(description = "是否可能敏感", example = "false")
    @JsonProperty("possibly_sensitive")
    private Boolean possiblySensitive;

    @Schema(description = "回复设置", example = "everyone")
    @JsonProperty("reply_settings")
    private String replySettings;

    @Schema(description = "对话ID", example = "1234567890123456789")
    @JsonProperty("conversation_id")
    private String conversationId;

    @Schema(description = "附件（媒体引用）")
    private Attachments attachments;

    @Schema(description = "实体信息（URL、话题标签等）")
    private Entities entities;

    /**
     * 附件内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachments {

        @Schema(description = "媒体密钥列表")
        @JsonProperty("media_keys")
        private List<String> mediaKeys;

        @Schema(description = "投票ID列表")
        @JsonProperty("poll_ids")
        private List<String> pollIds;

        public List<String> getMediaKeys() {
            return mediaKeys;
        }

        public void setMediaKeys(List<String> mediaKeys) {
            this.mediaKeys = mediaKeys;
        }

        public List<String> getPollIds() {
            return pollIds;
        }

        public void setPollIds(List<String> pollIds) {
            this.pollIds = pollIds;
        }
    }

    /**
     * 实体信息内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entities {

        @Schema(description = "URL列表")
        private List<UrlEntity> urls;

        @Schema(description = "话题标签列表")
        private List<HashtagEntity> hashtags;

        @Schema(description = "提及用户列表")
        private List<MentionEntity> mentions;

        @Schema(description = "媒体列表")
        private List<MediaEntity> media;

        public List<UrlEntity> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlEntity> urls) {
            this.urls = urls;
        }

        public List<HashtagEntity> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<HashtagEntity> hashtags) {
            this.hashtags = hashtags;
        }

        public List<MentionEntity> getMentions() {
            return mentions;
        }

        public void setMentions(List<MentionEntity> mentions) {
            this.mentions = mentions;
        }

        public List<MediaEntity> getMedia() {
            return media;
        }

        public void setMedia(List<MediaEntity> media) {
            this.media = media;
        }
    }

    /**
     * URL实体内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UrlEntity {

        @Schema(description = "起始位置")
        private Integer start;

        @Schema(description = "结束位置")
        private Integer end;

        @Schema(description = "URL")
        private String url;

        @Schema(description = "扩展URL")
        @JsonProperty("expanded_url")
        private String expandedUrl;

        @Schema(description = "显示URL")
        @JsonProperty("display_url")
        private String displayUrl;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "描述")
        private String description;

        @Schema(description = "图片")
        private List<String> images;

        @Schema(description = "状态码")
        private Integer status;

        @Schema(description = "是否展开")
        @JsonProperty("unwound_url")
        private String unwoundUrl;

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getExpandedUrl() {
            return expandedUrl;
        }

        public void setExpandedUrl(String expandedUrl) {
            this.expandedUrl = expandedUrl;
        }

        public String getDisplayUrl() {
            return displayUrl;
        }

        public void setDisplayUrl(String displayUrl) {
            this.displayUrl = displayUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getUnwoundUrl() {
            return unwoundUrl;
        }

        public void setUnwoundUrl(String unwoundUrl) {
            this.unwoundUrl = unwoundUrl;
        }
    }

    /**
     * 话题标签实体内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HashtagEntity {

        @Schema(description = "起始位置")
        private Integer start;

        @Schema(description = "结束位置")
        private Integer end;

        @Schema(description = "标签文本")
        private String tag;

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    /**
     * 提及用户实体内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MentionEntity {

        @Schema(description = "起始位置")
        private Integer start;

        @Schema(description = "结束位置")
        private Integer end;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "用户ID")
        private String id;

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 媒体实体内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MediaEntity {

        @Schema(description = "媒体密钥")
        @JsonProperty("media_key")
        private String mediaKey;

        @Schema(description = "类型")
        private String type;

        @Schema(description = "起始位置")
        private Integer start;

        @Schema(description = "结束位置")
        private Integer end;

        @Schema(description = "URL")
        private String url;

        @Schema(description = "预览图片URL")
        @JsonProperty("preview_image_url")
        private String previewImageUrl;

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

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
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
     * 公开指标内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicMetrics {

        @Schema(description = "转发数", example = "10")
        @JsonProperty("retweet_count")
        private Integer retweetCount;

        @Schema(description = "回复数", example = "5")
        @JsonProperty("reply_count")
        private Integer replyCount;

        @Schema(description = "点赞数", example = "100")
        @JsonProperty("like_count")
        private Integer likeCount;

        @Schema(description = "引用数", example = "2")
        @JsonProperty("quote_count")
        private Integer quoteCount;

        @Schema(description = "书签数", example = "3")
        @JsonProperty("bookmark_count")
        private Integer bookmarkCount;

        @Schema(description = "展示数", example = "1000")
        @JsonProperty("impression_count")
        private Integer impressionCount;

        public Integer getRetweetCount() {
            return retweetCount;
        }

        public void setRetweetCount(Integer retweetCount) {
            this.retweetCount = retweetCount;
        }

        public Integer getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(Integer replyCount) {
            this.replyCount = replyCount;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getQuoteCount() {
            return quoteCount;
        }

        public void setQuoteCount(Integer quoteCount) {
            this.quoteCount = quoteCount;
        }

        public Integer getBookmarkCount() {
            return bookmarkCount;
        }

        public void setBookmarkCount(Integer bookmarkCount) {
            this.bookmarkCount = bookmarkCount;
        }

        public Integer getImpressionCount() {
            return impressionCount;
        }

        public void setImpressionCount(Integer impressionCount) {
            this.impressionCount = impressionCount;
        }
    }

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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PublicMetrics getPublicMetrics() {
        return publicMetrics;
    }

    public void setPublicMetrics(PublicMetrics publicMetrics) {
        this.publicMetrics = publicMetrics;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getPossiblySensitive() {
        return possiblySensitive;
    }

    public void setPossiblySensitive(Boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }

    public String getReplySettings() {
        return replySettings;
    }

    public void setReplySettings(String replySettings) {
        this.replySettings = replySettings;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }
}
