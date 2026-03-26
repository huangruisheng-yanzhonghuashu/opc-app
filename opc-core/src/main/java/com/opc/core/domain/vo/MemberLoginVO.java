package com.opc.core.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Set;

@Schema(description = "会员登录信息")
public class MemberLoginVO
{
    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "会员名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phoneNumber;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "状态：0=正常,1=禁用,2=已注销")
    private String status;

    @Schema(description = "当前购买套餐名称")
    private String currentPackage;

    @Schema(description = "套餐分类：1=普通会员,2=VIP会员,3=超级VIP会员")
    private Integer packageType;

    @Schema(description = "来源：email=邮箱,x=X,facebook=Facebook,apple=Apple,google=Google")
    private String source;

    @Schema(description = "登录时间")
    private Instant loginTime;

    @Schema(description = "过期时间")
    private Instant expireTime;

    @Schema(description = "登录IP")
    private String ipaddr;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录令牌")
    private String token;

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
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

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentPackage()
    {
        return currentPackage;
    }

    public void setCurrentPackage(String currentPackage)
    {
        this.currentPackage = currentPackage;
    }

    public Integer getPackageType()
    {
        return packageType;
    }

    public void setPackageType(Integer packageType)
    {
        this.packageType = packageType;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public Instant getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Instant loginTime)
    {
        this.loginTime = loginTime;
    }

    public Instant getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Instant expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}