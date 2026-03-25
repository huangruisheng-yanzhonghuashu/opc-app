package com.opc.core.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CoreCommunity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "社区ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "社区名")
    @NotBlank(message = "社区名不能为空")
    @Size(min = 0, max = 100, message = "社区名长度不能超过100个字符")
    private String name;

    @Excel(name = "社区图片")
    private String image;

    @Excel(name = "社区地址")
    @Size(min = 0, max = 255, message = "社区地址长度不能超过255个字符")
    private String address;

    @Excel(name = "经度")
    private BigDecimal longitude;

    @Excel(name = "纬度")
    private BigDecimal latitude;

    @Excel(name = "相关详情")
    private String details;

    @Excel(name = "想去数")
    private Integer wantToGoCount;

    @Excel(name = "已去过数")
    private Integer visitedCount;

    @Excel(name = "评价数")
    private Integer reviewCount;

    @Excel(name = "评价星级")
    private BigDecimal rating;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @Excel(name = "排序")
    private Integer sortOrder;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public Integer getWantToGoCount()
    {
        return wantToGoCount;
    }

    public void setWantToGoCount(Integer wantToGoCount)
    {
        this.wantToGoCount = wantToGoCount;
    }

    public Integer getVisitedCount()
    {
        return visitedCount;
    }

    public void setVisitedCount(Integer visitedCount)
    {
        this.visitedCount = visitedCount;
    }

    public Integer getReviewCount()
    {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount)
    {
        this.reviewCount = reviewCount;
    }

    public BigDecimal getRating()
    {
        return rating;
    }

    public void setRating(BigDecimal rating)
    {
        this.rating = rating;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("image", getImage())
            .append("address", getAddress())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("details", getDetails())
            .append("wantToGoCount", getWantToGoCount())
            .append("visitedCount", getVisitedCount())
            .append("reviewCount", getReviewCount())
            .append("rating", getRating())
            .append("status", getStatus())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
