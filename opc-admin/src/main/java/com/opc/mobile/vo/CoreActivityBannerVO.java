package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 活动Banner VO
 *
 * @author opc
 */
@Schema(description = "活动Banner信息")
public class CoreActivityBannerVO
{
    private static final long serialVersionUID = 1L;

    /** BannerID */
    @Schema(description = "BannerID")
    private Long id;

    /** Banner名称 */
    @Schema(description = "Banner名称")
    private String bannerName;

    /** 图片URL */
    @Schema(description = "图片URL")
    private String imageUrl;

    /** 活动ID */
    @Schema(description = "活动ID（点击跳转）")
    private Long activityId;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortOrder;

    /** 状态（0启用 1禁用） */
    @Schema(description = "状态（0启用 1禁用）")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Instant createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getBannerName()
    {
        return bannerName;
    }

    public void setBannerName(String bannerName)
    {
        this.bannerName = bannerName;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Long getActivityId()
    {
        return activityId;
    }

    public void setActivityId(Long activityId)
    {
        this.activityId = activityId;
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

    public Instant getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Instant createTime)
    {
        this.createTime = createTime;
    }
}
