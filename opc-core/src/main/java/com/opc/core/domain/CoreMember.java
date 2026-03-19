package com.opc.core.domain;

import java.time.Instant;
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
    private Long id;

    @Excel(name = "会员名")
    @NotBlank(message = "会员名不能为空")
    @Size(min = 0, max = 50, message = "会员名长度不能超过50个字符")
    private String username;

    @Excel(name = "密码")
    @Size(min = 0, max = 100, message = "密码长度不能超过100个字符")
    private String password;

    @Excel(name = "会员昵称")
    @Size(min = 0, max = 50, message = "会员昵称长度不能超过50个字符")
    private String nickname;

    @Excel(name = "手机号")
    private String phoneNumber;

    @Excel(name = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Excel(name = "头像")
    private String avatar;

    @Excel(name = "最近活跃时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Instant lastActiveTime;

    @Excel(name = "当前购买套餐")
    @Size(min = 0, max = 100, message = "套餐名称长度不能超过100个字符")
    private String currentPackage;

    @Excel(name = "当前套餐等级", cellType = ColumnType.NUMERIC)
    private Integer currentPackageLevel;

    @Excel(name = "来源", readConverterExp = "email=邮箱,x=X,facebook=Facebook,apple=Apple,google=Google")
    @Size(min = 0, max = 50, message = "来源长度不能超过50个字符")
    private String source;

    @Excel(name = "来源ID")
    @Size(min = 0, max = 64, message = "来源ID长度不能超过64个字符")
    private String sourceId;

    @Excel(name = "Token")
    private String token;

    @Excel(name = "会员状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    @Excel(name = "注册时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Instant registerTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public Instant getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime(Instant lastActiveTime)
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

    public Integer getCurrentPackageLevel()
    {
        return currentPackageLevel;
    }

    public void setCurrentPackageLevel(Integer currentPackageLevel)
    {
        this.currentPackageLevel = currentPackageLevel;
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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Instant getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(Instant registerTime)
    {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("nickname", getNickname())
            .append("phoneNumber", getPhoneNumber())
            .append("email", getEmail())
            .append("avatar", getAvatar())
            .append("lastActiveTime", getLastActiveTime())
            .append("currentPackage", getCurrentPackage())
            .append("currentPackageLevel", getCurrentPackageLevel())
            .append("source", getSource())
            .append("sourceId", getSourceId())
            .append("token", getToken())
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
