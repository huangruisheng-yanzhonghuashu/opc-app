package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "BannerID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "标题")
    private String title;

    @Excel(name = "图片URL")
    private String imageUrl;

    @Excel(name = "链接类型", readConverterExp = "1=文章ID,2=外部链接")
    private String linkType;

    @Excel(name = "链接值")
    private String linkValue;

    @Excel(name = "排序", cellType = ColumnType.NUMERIC)
    private Integer sortOrder;

    @Excel(name = "状态", readConverterExp = "0=启用,1=禁用")
    private String status;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("imageUrl", getImageUrl())
            .append("linkType", getLinkType())
            .append("linkValue", getLinkValue())
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
