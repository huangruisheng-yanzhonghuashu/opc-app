package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreMemberConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "配置ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "配置类型", readConverterExp = "banner=会员页Banner图,vip_guide=VIP引导图片")
    private String configType;

    @Excel(name = "图片URL")
    private String imageUrl;

    @Excel(name = "文章链接/id")
    private String articleLink;

    private String richContent;

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

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getArticleLink()
    {
        return articleLink;
    }

    public void setArticleLink(String articleLink)
    {
        this.articleLink = articleLink;
    }

    public String getRichContent()
    {
        return richContent;
    }

    public void setRichContent(String richContent)
    {
        this.richContent = richContent;
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
            .append("configType", getConfigType())
            .append("imageUrl", getImageUrl())
            .append("articleLink", getArticleLink())
            .append("richContent", getRichContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
