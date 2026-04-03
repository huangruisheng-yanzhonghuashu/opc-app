package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * 素材VO
 *
 * @author opc
 */
@Schema(description = "素材信息")
public class CoreMaterialVO
{
    private static final long serialVersionUID = 1L;

    /** 素材ID */
    @Schema(description = "素材ID")
    private Long id;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 作者 */
    @Schema(description = "作者")
    private String author;

    /** 总结 */
    @Schema(description = "总结")
    private String summary;

    /** 正文 */
    @Schema(description = "正文")
    private String content;

    /** 原链接 */
    @Schema(description = "原链接")
    private String originalUrl;

    /** 原ID */
    @Schema(description = "原ID")
    private String originalId;

    /** 回复数 */
    @Schema(description = "回复数")
    private Long replyCount;

    /** 点赞数 */
    @Schema(description = "点赞数")
    private Long likeCount;

    /** 不喜欢数 */
    @Schema(description = "不喜欢数")
    private Long dislikeCount;

    /** 查看数 */
    @Schema(description = "查看数")
    private Long viewCount;

    /** 转发数 */
    @Schema(description = "转发数")
    private Long shareCount;

    /** 评论数 */
    @Schema(description = "评论数")
    private Long commentCount;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布时间")
    private Instant publishTime;

    /** 内容类型 */
    @Schema(description = "内容类型")
    private String contentType;

    /** 套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP） */
    @Schema(description = "套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP）")
    private Integer packageType;

    /** 二级分类ID */
    @Schema(description = "二级分类ID")
    private Long categoryId;

    /** 状态（0上线 1下线） */
    @Schema(description = "状态（0上线 1下线）")
    private String status;

    /** 是否置顶（0否 1是） */
    @Schema(description = "是否置顶（0否 1是）")
    private String isTop;

    /** 来源 */
    @Schema(description = "来源")
    private String source;

    /** 素材类型（post帖子 article文章） */
    @Schema(description = "素材类型（post帖子 article文章）")
    private String materialType;

    /** 期数 */
    @Schema(description = "期数")
    private Integer issueNo;

    /** 封面图 */
    @Schema(description = "封面图")
    private String coverImage;

    /** 视频URL */
    @Schema(description = "视频URL")
    private String videoUrl;

    /** 上线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上线时间")
    private Instant onlineTime;

    /** 下线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下线时间")
    private Instant offlineTime;

    /** 置顶时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "置顶时间")
    private Instant topTime;

    /** 取消置顶时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "取消置顶时间")
    private Instant untopTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Instant createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Instant updateTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 标签列表 */
    @Schema(description = "标签列表")
    private List<CoreTagVO> tags;

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

    public String getMaterialType()
    {
        return materialType;
    }

    public void setMaterialType(String materialType)
    {
        this.materialType = materialType;
    }

    public Integer getIssueNo()
    {
        return issueNo;
    }

    public void setIssueNo(Integer issueNo)
    {
        this.issueNo = issueNo;
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

    public Instant getOnlineTime()
    {
        return onlineTime;
    }

    public void setOnlineTime(Instant onlineTime)
    {
        this.onlineTime = onlineTime;
    }

    public Instant getOfflineTime()
    {
        return offlineTime;
    }

    public void setOfflineTime(Instant offlineTime)
    {
        this.offlineTime = offlineTime;
    }

    public Instant getTopTime()
    {
        return topTime;
    }

    public void setTopTime(Instant topTime)
    {
        this.topTime = topTime;
    }

    public Instant getUntopTime()
    {
        return untopTime;
    }

    public void setUntopTime(Instant untopTime)
    {
        this.untopTime = untopTime;
    }

    public Instant getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Instant createTime)
    {
        this.createTime = createTime;
    }

    public Instant getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public List<CoreTagVO> getTags()
    {
        return tags;
    }

    public void setTags(List<CoreTagVO> tags)
    {
        this.tags = tags;
    }
}
