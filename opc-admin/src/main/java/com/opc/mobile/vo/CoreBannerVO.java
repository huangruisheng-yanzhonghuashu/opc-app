package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * Banner VO
 *
 * @author opc
 */
@Schema(description = "Banner信息")
public class CoreBannerVO
{
    private static final long serialVersionUID = 1L;

    /** BannerID */
    @Schema(description = "BannerID")
    private Long id;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 图片URL */
    @Schema(description = "图片URL")
    private String imageUrl;

    /** 链接目标类型（1帖子 2文章） */
    @Schema(description = "链接目标类型（1帖子 2文章）")
    private String linkTargetType;

    /** 链接类型（1内部ID 2外部链接） */
    @Schema(description = "链接类型（1内部ID 2外部链接）")
    private String linkType;

    /** 链接值 */
    @Schema(description = "链接值")
    private String linkValue;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortOrder;

    /** 状态（0启用 1禁用） */
    @Schema(description = "状态（0启用 1禁用）")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

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

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getLinkTargetType()
    {
        return linkTargetType;
    }

    public void setLinkTargetType(String linkTargetType)
    {
        this.linkTargetType = linkTargetType;
    }

    public String getLinkType()
    {
        return linkType;
    }

    public void setLinkType(String linkType)
    {
        this.linkType = linkType;
    }

    public String getLinkValue()
    {
        return linkValue;
    }

    public void setLinkValue(String linkValue)
    {
        this.linkValue = linkValue;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
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
}
