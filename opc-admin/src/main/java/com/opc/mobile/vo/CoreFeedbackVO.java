package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 意见反馈 VO
 *
 * @author opc
 */
@Schema(description = "意见反馈信息")
public class CoreFeedbackVO
{
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    @Schema(description = "反馈ID")
    private Long id;

    /** 会员ID */
    @Schema(description = "会员ID")
    private Long memberId;

    /** 会员名称 */
    @Schema(description = "会员名称")
    private String memberName;

    /** 反馈类型（bug功能异常 feature功能建议 other其他） */
    @Schema(description = "反馈类型（bug功能异常 feature功能建议 other其他）")
    private String type;

    /** 反馈标题 */
    @Schema(description = "反馈标题")
    private String title;

    /** 反馈内容 */
    @Schema(description = "反馈内容")
    private String content;

    /** 联系方式 */
    @Schema(description = "联系方式")
    private String contact;

    /** 处理状态（0待处理 1处理中 2已处理） */
    @Schema(description = "处理状态（0待处理 1处理中 2已处理）")
    private String status;

    /** 回复内容 */
    @Schema(description = "回复内容")
    private String reply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "回复时间")
    private Date replyTime;

    /** 回复人 */
    @Schema(description = "回复人")
    private String replyBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public Date getReplyTime()
    {
        return replyTime;
    }

    public void setReplyTime(Date replyTime)
    {
        this.replyTime = replyTime;
    }

    public String getReplyBy()
    {
        return replyBy;
    }

    public void setReplyBy(String replyBy)
    {
        this.replyBy = replyBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
