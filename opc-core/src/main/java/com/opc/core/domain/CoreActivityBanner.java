package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreActivityBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "BannerID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "Banner名称")
    private String bannerName;

    @Excel(name = "图片URL")
    private String imageUrl;

    @Excel(name = "活动ID", cellType = ColumnType.NUMERIC)
    private Long activityId;

    @Excel(name = "排序", cellType = ColumnType.NUMERIC)
    private Integer sortOrder;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bannerName", getBannerName())
            .append("imageUrl", getImageUrl())
            .append("activityId", getActivityId())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
