package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "标签ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "标签名称")
    private String tagName;

    @Excel(name = "标签颜色")
    private String tagColor;

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

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public String getTagColor()
    {
        return tagColor;
    }

    public void setTagColor(String tagColor)
    {
        this.tagColor = tagColor;
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
            .append("tagName", getTagName())
            .append("tagColor", getTagColor())
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
