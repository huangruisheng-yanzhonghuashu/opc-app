package com.opc.mobile.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 订单ID请求DTO
 */
public class OrderIdDTO
{
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
