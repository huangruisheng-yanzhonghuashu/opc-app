package com.opc.core.domain;

import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

public class CoreMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "素材ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "标题")
    private String title;

    @Excel(name = "作者")
    private String author;

    @Excel(name = "总结")
    private String summary;

    @Excel(name = "正文")
    private String content;

    @Excel(name = "原链接")
    private String originalUrl;

    @Excel(name = "原ID")
    private String originalId;

    @Excel(name = "回复数", cellType = ColumnType.NUMERIC)
    private Long replyCount;

    @Excel(name = "点赞数", cellType = ColumnType.NUMERIC)
    private Long likeCount;

    @Excel(name = "不喜欢数", cellType = ColumnType.NUMERIC)
    private Long dislikeCount;

    @Excel(name = "查看数", cellType = ColumnType.NUMERIC)
    private Long viewCount;

    @Excel(name = "转发数", cellType = ColumnType.NUMERIC)
    private Long shareCount;

    @Excel(name = "评论数", cellType = ColumnType.NUMERIC)
    private Long commentCount;

    @Excel(name = "发布时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Instant publishTime;

    @Excel(name = "内容类型" , readConverterExp = "text/image/video")
    private String contentType;

    @Excel(name = "套餐分类", readConverterExp = "0=晨报,1=普通素材,2=VIP素材,3=超级VIP")
    private Integer packageType;

    /** 二级分类ID */
    private Long categoryId;

    @Excel(name = "状态", readConverterExp = "0=上线,1=下线")
    private String status;

    @Excel(name = "是否置顶", readConverterExp = "0=否,1=是")
    private String isTop;

    @Excel(name = "来源")
    private String source;

    @Excel(name = "封面图")
    private String coverImage;

    @Excel(name = "视频URL")
    private String videoUrl;

    @Excel(name = "上线时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.time.Instant onlineTime;

    @Excel(name = "下线时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.time.Instant offlineTime;

    @Excel(name = "置顶时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.time.Instant topTime;

    @Excel(name = "取消置顶时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.time.Instant untopTime;

    private List<Long> tagIds;

    private List<CoreTag> tags;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> tag2Ids;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CoreTag2> tags2;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl)
    {
        this.originalUrl = originalUrl;
    }

    public String getOriginalId()
    {
        return originalId;
    }

    public void setOriginalId(String originalId)
    {
        this.originalId = originalId;
    }

    public Long getReplyCount()
    {
        return replyCount;
    }

    public void setReplyCount(Long replyCount)
    {
        this.replyCount = replyCount;
    }

    public Long getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(Long likeCount)
    {
        this.likeCount = likeCount;
    }

    public Long getDislikeCount()
    {
        return dislikeCount;
    }

    public void setDislikeCount(Long dislikeCount)
    {
        this.dislikeCount = dislikeCount;
    }

    public Long getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(Long viewCount)
    {
        this.viewCount = viewCount;
    }

    public Long getShareCount()
    {
        return shareCount;
    }

    public void setShareCount(Long shareCount)
    {
        this.shareCount = shareCount;
    }

    public Long getCommentCount()
    {
        return commentCount;
    }

    public void setCommentCount(Long commentCount)
    {
        this.commentCount = commentCount;
    }

    public Instant getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Instant publishTime)
    {
        this.publishTime = publishTime;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public Integer getPackageType()
    {
        return packageType;
    }

    public void setPackageType(Integer packageType)
    {
        this.packageType = packageType;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIsTop()
    {
        return isTop;
    }

    public void setIsTop(String isTop)
    {
        this.isTop = isTop;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getCoverImage()
    {
        return coverImage;
    }

    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }

    public String getVideoUrl()
    {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public java.time.Instant getOnlineTime()
    {
        return onlineTime;
    }

    public void setOnlineTime(java.time.Instant onlineTime)
    {
        this.onlineTime = onlineTime;
    }

    public java.time.Instant getOfflineTime()
    {
        return offlineTime;
    }

    public void setOfflineTime(java.time.Instant offlineTime)
    {
        this.offlineTime = offlineTime;
    }

    public java.time.Instant getTopTime()
    {
        return topTime;
    }

    public void setTopTime(java.time.Instant topTime)
    {
        this.topTime = topTime;
    }

    public java.time.Instant getUntopTime()
    {
        return untopTime;
    }

    public void setUntopTime(java.time.Instant untopTime)
    {
        this.untopTime = untopTime;
    }

    public List<Long> getTagIds()
    {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds)
    {
        this.tagIds = tagIds;
    }

    public List<CoreTag> getTags()
    {
        return tags;
    }

    public void setTags(List<CoreTag> tags)
    {
        this.tags = tags;
    }

    public List<Long> getTag2Ids()
    {
        return tag2Ids;
    }

    public void setTag2Ids(List<Long> tag2Ids)
    {
        this.tag2Ids = tag2Ids;
    }

    public List<CoreTag2> getTags2()
    {
        return tags2;
    }

    public void setTags2(List<CoreTag2> tags2)
    {
        this.tags2 = tags2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("author", getAuthor())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("originalUrl", getOriginalUrl())
            .append("originalId", getOriginalId())
            .append("replyCount", getReplyCount())
            .append("likeCount", getLikeCount())
            .append("dislikeCount", getDislikeCount())
            .append("viewCount", getViewCount())
            .append("shareCount", getShareCount())
            .append("commentCount", getCommentCount())
            .append("publishTime", getPublishTime())
            .append("contentType", getContentType())
            .append("packageType", getPackageType())
            .append("categoryId", getCategoryId())
            .append("status", getStatus())
            .append("isTop", getIsTop())
            .append("source", getSource())
            .append("coverImage", getCoverImage())
            .append("videoUrl", getVideoUrl())
            .append("onlineTime", getOnlineTime())
            .append("offlineTime", getOfflineTime())
            .append("topTime", getTopTime())
            .append("untopTime", getUntopTime())
            .append("tagIds", getTagIds())
            .append("tags", getTags())
            .append("tag2Ids", getTag2Ids())
            .append("tags2", getTags2())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
