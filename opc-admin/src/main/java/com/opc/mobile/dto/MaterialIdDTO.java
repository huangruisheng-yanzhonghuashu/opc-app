package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 素材ID请求DTO
 */
@Schema(description = "素材ID请求")
public class MaterialIdDTO
{
    @Schema(description = "素材ID", required = true, example = "1")
    @NotNull(message = "素材ID不能为空")
    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
