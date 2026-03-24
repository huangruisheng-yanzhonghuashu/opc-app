package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 订单ID请求DTO
 */
@Schema(description = "订单ID请求")
public class OrderIdDTO
{
    @Schema(description = "订单ID", required = true, example = "1")
    @NotNull(message = "订单ID不能为空")
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
