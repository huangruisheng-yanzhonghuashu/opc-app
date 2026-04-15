package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 移动端用户注册DTO
 *
 * @author opc
 */
@Schema(description = "移动端用户注册请求")
public class MobileRegisterDTO {

    @Schema(description = "用户名", required = true, example = "zhangsan")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String username;

    @Schema(description = "用户密码", required = true, example = "Password123!")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8到20个字符之间")
    @Pattern(regexp = "^(?:(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*\\d)|(?=.*[a-z])(?=.*[^a-zA-Z0-9])|(?=.*[A-Z])(?=.*\\d)|(?=.*[A-Z])(?=.*[^a-zA-Z0-9])|(?=.*\\d)(?=.*[^a-zA-Z0-9])).{8,20}$",
             message = "密码必须包含小写字母、大写字母、数字、特殊字符中的至少两种")
    private String password;

    @Schema(description = "邮箱地址", required = true, example = "zhangsan@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "邮箱验证码", required = true, example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "激活码（邀请码）", required = false, example = "ACQPZFHJP9A3MIGVFM")
    private String inviteCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
