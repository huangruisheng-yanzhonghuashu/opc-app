package com.opc.web.dto.twitter.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

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
}
