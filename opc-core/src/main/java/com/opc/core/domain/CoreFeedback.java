package com.opc.core.domain;

import java.time.Instant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "反馈ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    @Excel(name = "会员名称")
    private String memberName;

    @Excel(name = "反馈类型", readConverterExp = "bug=功能异常,feature=功能建议,other=其他")
    @NotBlank(message = "反馈类型不能为空")
    private String type;

    @Excel(name = "反馈标题")
    @NotBlank(message = "反馈标题不能为空")
    @Size(min = 0, max = 200, message = "反馈标题长度不能超过200个字符")
    private String title;

    @Excel(name = "反馈内容")
    @NotBlank(message = "反馈内容不能为空")
    private String content;

    @Excel(name = "联系方式")
    @Size(min = 0, max = 100, message = "联系方式长度不能超过100个字符")
    private String contact;

    @Excel(name = "处理状态", readConverterExp = "0=待处理,1=处理中,2=已处理")
    private String status;

    @Excel(name = "回复内容")
    private String reply;

    @Excel(name = "回复时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Instant replyTime;

    @Excel(name = "回复人")
    private String replyBy;

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

    public Instant getReplyTime()
    {
        return replyTime;
    }

    public void setReplyTime(Instant replyTime)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("memberId", getMemberId())
            .append("memberName", getMemberName())
            .append("type", getType())
            .append("title", getTitle())
            .append("content", getContent())
            .append("contact", getContact())
            .append("status", getStatus())
            .append("reply", getReply())
            .append("replyTime", getReplyTime())
            .append("replyBy", getReplyBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
