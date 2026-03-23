package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 邮箱验证码发送请求DTO
 *
 * @author opc
 */
@Schema(description = "邮箱验证码发送请求")
public class EmailCodeRequestDTO {

    /**
     * 邮箱
     */
    @Schema(description = "邮箱地址", required = true, example = "zhangsan@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
