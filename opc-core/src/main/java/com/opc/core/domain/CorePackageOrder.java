package com.opc.core.domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CorePackageOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "订单ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "订单号")
    private String orderNo;

    private Long memberId;

    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "第三方账号")
    private String thirdPartyAccount;

    @Excel(name = "昵称")
    private String nickname;

    private Long packageId;

    @Excel(name = "套餐名称")
    private String packageName;

    @Excel(name = "套餐分类", readConverterExp = "normal=普通会员,vip=VIP会员,svip=超级VIP会员")
    private String packageType;

    @Excel(name = "价格", cellType = ColumnType.NUMERIC)
    private BigDecimal price;

    @Excel(name = "支付时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @Excel(name = "支付状态", readConverterExp = "0=待支付,1=已支付,2=已取消")
    private String payStatus;

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

    public String getPackageType()
    {
        return packageType;
    }

    public void setPackageType(String packageType)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("memberId", getMemberId())
            .append("email", getEmail())
            .append("thirdPartyAccount", getThirdPartyAccount())
            .append("nickname", getNickname())
            .append("packageId", getPackageId())
            .append("packageName", getPackageName())
            .append("packageType", getPackageType())
            .append("price", getPrice())
            .append("payTime", getPayTime())
            .append("payStatus", getPayStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
