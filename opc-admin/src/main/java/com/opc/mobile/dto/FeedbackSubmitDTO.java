package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "意见反馈提交DTO", description = "移动端会员提交意见反馈的请求参数")
public class FeedbackSubmitDTO
{
    @Schema(name = "memberId", description = "会员ID（可选，已登录用户可传）", example = "1")
    private Long memberId;

    @Schema(name = "type", description = "反馈类型：bug-功能异常, feature-功能建议, other-其他", example = "bug", required = true)
    @NotBlank(message = "反馈类型不能为空")
    private String type;

    @Schema(name = "title", description = "反馈标题", example = "登录功能异常", required = true)
    @NotBlank(message = "反馈标题不能为空")
    @Size(max = 200, message = "反馈标题长度不能超过200个字符")
    private String title;

    @Schema(name = "content", description = "反馈内容", example = "在使用邮箱登录时，页面出现错误提示...", required = true)
    @NotBlank(message = "反馈内容不能为空")
    private String content;

    @Schema(name = "contact", description = "联系方式（邮箱或手机号，可选）", example = "user@example.com")
    @Size(max = 100, message = "联系方式长度不能超过100个字符")
    private String contact;

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }
}
