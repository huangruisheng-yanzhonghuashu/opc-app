package com.opc.mobile.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 反馈ID请求DTO
 */
public class FeedbackIdDTO
{
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
