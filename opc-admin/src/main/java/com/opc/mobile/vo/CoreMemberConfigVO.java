package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 会员配置 VO
 *
 * @author opc
 */
@Schema(description = "会员配置信息")
public class CoreMemberConfigVO
{
    private static final long serialVersionUID = 1L;

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long id;

    /** 配置类型（banner会员页Banner图 vip_guideVIP引导图片） */
    @Schema(description = "配置类型（banner会员页Banner图 vip_guideVIP引导图片）")
    private String configType;

    /** 图片URL */
    @Schema(description = "图片URL")
    private String imageUrl;

    /** 链接类型（article文章ID link外部链接） */
    @Schema(description = "链接类型（article文章ID link外部链接）")
    private String linkType;

    /** 文章链接/id */
    @Schema(description = "文章链接/id")
    private String articleLink;

    /** 富文本内容 */
    @Schema(description = "富文本内容")
    private String richContent;

    /** 状态（0启用 1禁用） */
    @Schema(description = "状态（0启用 1禁用）")
    private String status;

    /** 排序号 */
    @Schema(description = "排序号")
    private Integer sortOrder;

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

    public String getLinkType()
    {
        return linkType;
    }

    public void setLinkType(String linkType)
    {
        this.linkType = linkType;
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

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
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
