package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 素材行为操作DTO
 *
 * @author opc
 */
@Schema(description = "素材行为操作请求")
public class MaterialActionDTO
{
    @Schema(description = "素材ID", required = true)
    private Long materialId;

    @Schema(description = "操作类型：true-执行操作(喜欢/不喜欢), false-取消操作", required = true)
    private Boolean isAction;

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Boolean getIsAction()
    {
        return isAction;
    }

    public void setIsAction(Boolean isAction)
    {
        this.isAction = isAction;
    }

    @Override
    public String toString()
    {
        return "MaterialActionDTO{" +
                "materialId=" + materialId +
                ", isAction=" + isAction +
                '}';
    }
}
