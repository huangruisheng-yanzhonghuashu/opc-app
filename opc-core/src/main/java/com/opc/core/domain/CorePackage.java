package com.opc.core.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CorePackage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "套餐ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "套餐名称")
    private String packageName;

    @Excel(name = "套餐价格", cellType = ColumnType.NUMERIC)
    private BigDecimal packagePrice;

    @Excel(name = "套餐分类", readConverterExp = "normal=普通会员,vip=VIP会员,svip=超级VIP会员")
    private String packageType;

    private String description;

    @Excel(name = "状态", readConverterExp = "0=上架,1=下架")
    private String status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public BigDecimal getPackagePrice()
    {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice)
    {
        this.packagePrice = packagePrice;
    }

    public String getPackageType()
    {
        return packageType;
    }

    public void setPackageType(String packageType)
    {
        this.packageType = packageType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("packageName", getPackageName())
            .append("packagePrice", getPackagePrice())
            .append("packageType", getPackageType())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
