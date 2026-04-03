package com.opc.core.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

public class CoreActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "活动ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "活动名称")
    private String activityName;

    @Excel(name = "活动海报")
    private String posterUrl;

    @Excel(name = "组织者名称")
    private String organizerName;

    @Excel(name = "组织者头像")
    private String organizerAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "活动时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date activityTime;

    @Excel(name = "省份")
    private String province;

    @Excel(name = "城市")
    private String city;

    @Excel(name = "详细地址")
    private String address;

    @Excel(name = "总人数", cellType = ColumnType.NUMERIC)
    private Integer totalCapacity;

    @Excel(name = "已报名人数", cellType = ColumnType.NUMERIC)
    private Integer registeredCount;

    @Excel(name = "报名费用", cellType = ColumnType.NUMERIC)
    private BigDecimal registrationFee;

    private String activityDetail;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getActivityName()
    {
        return activityName;
    }

    public void setActivityName(String activityName)
    {
        this.activityName = activityName;
    }

    public String getPosterUrl()
    {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl)
    {
        this.posterUrl = posterUrl;
    }

    public String getOrganizerName()
    {
        return organizerName;
    }

    public void setOrganizerName(String organizerName)
    {
        this.organizerName = organizerName;
    }

    public String getOrganizerAvatar()
    {
        return organizerAvatar;
    }

    public void setOrganizerAvatar(String organizerAvatar)
    {
        this.organizerAvatar = organizerAvatar;
    }

    public Date getActivityTime()
    {
        return activityTime;
    }

    public void setActivityTime(Date activityTime)
    {
        this.activityTime = activityTime;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getTotalCapacity()
    {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity)
    {
        this.totalCapacity = totalCapacity;
    }

    public Integer getRegisteredCount()
    {
        return registeredCount;
    }

    public void setRegisteredCount(Integer registeredCount)
    {
        this.registeredCount = registeredCount;
    }

    public BigDecimal getRegistrationFee()
    {
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee)
    {
        this.registrationFee = registrationFee;
    }

    public String getActivityDetail()
    {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail)
    {
        this.activityDetail = activityDetail;
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
            .append("activityName", getActivityName())
            .append("posterUrl", getPosterUrl())
            .append("organizerName", getOrganizerName())
            .append("organizerAvatar", getOrganizerAvatar())
            .append("activityTime", getActivityTime())
            .append("province", getProvince())
            .append("city", getCity())
            .append("address", getAddress())
            .append("totalCapacity", getTotalCapacity())
            .append("registeredCount", getRegisteredCount())
            .append("registrationFee", getRegistrationFee())
            .append("activityDetail", getActivityDetail())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
