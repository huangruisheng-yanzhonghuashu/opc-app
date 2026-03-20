package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "会员信息修改DTO")
public class MemberUpdateDTO
{
    @Schema(description = "会员昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phoneNumber;

    @Schema(description = "头像URL")
    private String avatar;

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
}
