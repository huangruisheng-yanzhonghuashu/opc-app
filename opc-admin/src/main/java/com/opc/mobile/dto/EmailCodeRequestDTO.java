package com.opc.mobile.dto;

/**
 * 邮箱验证码发送请求DTO
 *
 * @author opc
 */
public class EmailCodeRequestDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
