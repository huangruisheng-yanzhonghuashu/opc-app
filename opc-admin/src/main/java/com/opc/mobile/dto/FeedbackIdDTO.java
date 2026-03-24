package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 反馈ID请求DTO
 */
@Schema(description = "反馈ID请求")
public class FeedbackIdDTO
{
    @Schema(description = "反馈ID", required = true, example = "1")
    @NotNull(message = "反馈ID不能为空")
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
