package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 活动 VO
 *
 * @author opc
 */
@Schema(description = "活动信息")
public class CoreActivityVO
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @Schema(description = "活动ID")
    private Long id;

    /** 活动名称 */
    @Schema(description = "活动名称")
    private String activityName;

    /** 活动海报URL */
    @Schema(description = "活动海报URL")
    private String posterUrl;

    /** 活动组织者名称 */
    @Schema(description = "活动组织者名称")
    private String organizerName;

    /** 活动组织者头像URL */
    @Schema(description = "活动组织者头像URL")
    private String organizerAvatar;

    /** 活动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "活动时间")
    private Instant activityTime;

    /** 省份 */
    @Schema(description = "省份")
    private String province;

    /** 城市 */
    @Schema(description = "城市")
    private String city;

    /** 详细地址 */
    @Schema(description = "详细地址")
    private String address;

    /** 活动总人数 */
    @Schema(description = "活动总人数")
    private Integer totalCapacity;

    /** 已报名人数 */
    @Schema(description = "已报名人数")
    private Integer registeredCount;

    /** 活动报名费用 */
    @Schema(description = "活动报名费用")
    private BigDecimal registrationFee;

    /** 活动详情 */
    @Schema(description = "活动详情")
    private String activityDetail;

    /** 状态（0正常 1停用） */
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Instant createTime;

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

    public Instant getActivityTime()
    {
        return activityTime;
    }

    public void setActivityTime(Instant activityTime)
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

    public Instant getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Instant createTime)
    {
        this.createTime = createTime;
    }
}
