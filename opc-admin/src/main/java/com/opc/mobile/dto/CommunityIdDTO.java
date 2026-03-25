package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 社区ID请求DTO
 */
@Schema(description = "社区ID请求")
public class CommunityIdDTO
{
    @Schema(description = "社区ID", required = true, example = "1")
    @NotNull(message = "社区ID不能为空")
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
