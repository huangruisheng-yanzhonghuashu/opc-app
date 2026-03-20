package com.opc.core.domain;

import java.time.Instant;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

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

    @Excel(name = "查看数", cellType = ColumnType.NUMERIC)
    private Long viewCount;

    @Excel(name = "转发数", cellType = ColumnType.NUMERIC)
    private Long shareCount;

    @Excel(name = "评论数", cellType = ColumnType.NUMERIC)
    private Long commentCount;

    @Excel(name = "发布时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Instant publishTime;

    @Excel(name = "查看权限", cellType = ColumnType.NUMERIC)
    private Integer viewPermission;

    @Excel(name = "内容类型")
    private String contentType;

    @Excel(name = "分类")
    private String category;

    @Excel(name = "状态", readConverterExp = "0=上线,1=下线")
    private String status;

    @Excel(name = "是否置顶", readConverterExp = "0=否,1=是")
    private String isTop;

    @Excel(name = "来源")
    private String source;

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

    public Integer getViewPermission()
    {
        return viewPermission;
    }

    public void setViewPermission(Integer viewPermission)
    {
        this.viewPermission = viewPermission;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
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
            .append("viewCount", getViewCount())
            .append("shareCount", getShareCount())
            .append("commentCount", getCommentCount())
            .append("publishTime", getPublishTime())
            .append("viewPermission", getViewPermission())
            .append("contentType", getContentType())
            .append("category", getCategory())
            .append("status", getStatus())
            .append("isTop", getIsTop())
            .append("source", getSource())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
