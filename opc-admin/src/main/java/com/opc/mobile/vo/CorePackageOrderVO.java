package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;

/**
 * 套餐订单 VO
 *
 * @author opc
 */
@Schema(description = "套餐订单信息")
public class CorePackageOrderVO
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @Schema(description = "订单ID")
    private Long id;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 会员ID */
    @Schema(description = "会员ID")
    private Long memberId;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 第三方账号 */
    @Schema(description = "第三方账号")
    private String thirdPartyAccount;

    /** 昵称 */
    @Schema(description = "昵称")
    private String nickname;

    /** 套餐ID */
    @Schema(description = "套餐ID")
    private Long packageId;

    /** 套餐名称 */
    @Schema(description = "套餐名称")
    private String packageName;

    /** 套餐分类（1普通会员 2VIP会员 3超级VIP会员） */
    @Schema(description = "套餐分类（1普通会员 2VIP会员 3超级VIP会员）")
    private Integer packageType;

    /** 价格 */
    @Schema(description = "价格")
    private BigDecimal price;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /** 支付状态（0待支付 1已支付 2已取消） */
    @Schema(description = "支付状态（0待支付 1已支付 2已取消）")
    private String payStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Instant createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Instant updateTime;

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

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

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

    public String getThirdPartyAccount()
    {
        return thirdPartyAccount;
    }

    public void setThirdPartyAccount(String thirdPartyAccount)
    {
        this.thirdPartyAccount = thirdPartyAccount;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public Long getPackageId()
    {
        return packageId;
    }

    public void setPackageId(Long packageId)
    {
        this.packageId = packageId;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public Integer getPackageType()
    {
        return packageType;
    }

    public void setPackageType(Integer packageType)
    {
        this.packageType = packageType;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public LocalDateTime getPayTime()
    {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime)
    {
        this.payTime = payTime;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(String payStatus)
    {
        this.payStatus = payStatus;
    }

    public Instant getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Instant createTime)
    {
        this.createTime = createTime;
    }

    public Instant getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime)
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
