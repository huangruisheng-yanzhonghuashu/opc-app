package com.opc.core.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    @Excel(name = "手机号")
    private String phoneNumber;

    @Excel(name = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Excel(name = "会员名")
    @NotBlank(message = "会员名不能为空")
    @Size(min = 0, max = 50, message = "会员名长度不能超过50个字符")
    private String username;

    @Excel(name = "会员昵称")
    @Size(min = 0, max = 50, message = "会员昵称长度不能超过50个字符")
    private String nickname;

    @Excel(name = "最近活跃时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String lastActiveTime;

    @Excel(name = "当前购买套餐")
    @Size(min = 0, max = 100, message = "套餐名称长度不能超过100个字符")
    private String currentPackage;

    @Excel(name = "一级用户数", cellType = ColumnType.NUMERIC)
    private Integer level1Users;

    @Excel(name = "二级用户数", cellType = ColumnType.NUMERIC)
    private Integer level2Users;

    @Excel(name = "三级用户数", cellType = ColumnType.NUMERIC)
    private Integer level3Users;

    @Excel(name = "来源")
    @Size(min = 0, max = 50, message = "来源长度不能超过50个字符")
    private String source;

    @Excel(name = "来源ID")
    @Size(min = 0, max = 64, message = "来源ID长度不能超过64个字符")
    private String sourceId;

    @Excel(name = "会员状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    @Excel(name = "注册时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String registerTime;

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime)
    {
        this.lastActiveTime = lastActiveTime;
    }

    public String getCurrentPackage()
    {
        return currentPackage;
    }

    public void setCurrentPackage(String currentPackage)
    {
        this.currentPackage = currentPackage;
    }

    public Integer getLevel1Users()
    {
        return level1Users;
    }

    public void setLevel1Users(Integer level1Users)
    {
        this.level1Users = level1Users;
    }

    public Integer getLevel2Users()
    {
        return level2Users;
    }

    public void setLevel2Users(Integer level2Users)
    {
        this.level2Users = level2Users;
    }

    public Integer getLevel3Users()
    {
        return level3Users;
    }

    public void setLevel3Users(Integer level3Users)
    {
        this.level3Users = level3Users;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("phoneNumber", getPhoneNumber())
            .append("email", getEmail())
            .append("username", getUsername())
            .append("nickname", getNickname())
            .append("lastActiveTime", getLastActiveTime())
            .append("currentPackage", getCurrentPackage())
            .append("level1Users", getLevel1Users())
            .append("level2Users", getLevel2Users())
            .append("level3Users", getLevel3Users())
            .append("source", getSource())
            .append("sourceId", getSourceId())
            .append("status", getStatus())
            .append("registerTime", getRegisterTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
