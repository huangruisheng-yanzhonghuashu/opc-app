package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 套餐 VO
 *
 * @author opc
 */
@Schema(description = "套餐信息")
public class CorePackageVO
{
    private static final long serialVersionUID = 1L;

    /** 套餐ID */
    @Schema(description = "套餐ID")
    private Long id;

    /** 套餐名称 */
    @Schema(description = "套餐名称")
    private String packageName;

    /** 套餐价格 */
    @Schema(description = "套餐价格")
    private BigDecimal packagePrice;

    /** 套餐分类（1普通会员 2VIP会员 3超级VIP会员） */
    @Schema(description = "套餐分类（1普通会员 2VIP会员 3超级VIP会员）")
    private Integer packageType;

    /** 套餐描述 */
    @Schema(description = "套餐描述")
    private String description;

    /** 套餐图片 */
    @Schema(description = "套餐图片")
    private String imageUrl;

    /** 状态（0上架 1下架） */
    @Schema(description = "状态（0上架 1下架）")
    private String status;

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

    public Integer getPackageType()
    {
        return packageType;
    }

    public void setPackageType(Integer packageType)
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

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
