package com.opc.mobile.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 素材媒体文件 VO
 *
 * @author opc
 */
@Schema(description = "素材媒体文件信息")
public class CoreMaterialMediaVO
{
    private static final long serialVersionUID = 1L;

    /** 媒体ID */
    @Schema(description = "媒体ID")
    private Long id;

    /** 素材ID */
    @Schema(description = "素材ID")
    private Long materialId;

    /** 媒体类型（image/video） */
    @Schema(description = "媒体类型（image/video）")
    private String mediaType;

    /** 文件的URL */
    @Schema(description = "文件的URL")
    private String fileUrl;

    /** 排序号 */
    @Schema(description = "排序号")
    private Integer sortOrder;

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

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }
}
