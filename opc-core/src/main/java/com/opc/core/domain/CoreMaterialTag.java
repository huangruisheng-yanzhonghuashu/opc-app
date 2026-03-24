package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.core.domain.BaseEntity;

public class CoreMaterialTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long materialId;

    private Long tagId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getTagId()
    {
        return tagId;
    }

    public void setTagId(Long tagId)
    {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialId", getMaterialId())
            .append("tagId", getTagId())
            .toString();
    }
}
