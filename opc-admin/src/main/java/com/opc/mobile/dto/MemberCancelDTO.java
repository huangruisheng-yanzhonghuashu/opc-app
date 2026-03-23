package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "会员注销DTO")
public class MemberCancelDTO
{
    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码，用于验证身份", required = true)
    private String password;

    @Schema(description = "注销原因")
    private String reason;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }
}
