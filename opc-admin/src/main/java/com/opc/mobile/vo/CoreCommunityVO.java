package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 社区详情VO
 * 包含社区基本信息及当前会员的关联状态
 *
 * @author opc
 */
@Schema(description = "社区详情")
public class CoreCommunityVO
{
    private static final long serialVersionUID = 1L;

    /** 社区ID */
    @Schema(description = "社区ID")
    private Long id;

    /** 社区名 */
    @Schema(description = "社区名")
    private String name;

    /** 社区图片 */
    @Schema(description = "社区图片")
    private String image;

    /** 社区地址 */
    @Schema(description = "社区地址")
    private String address;

    /** 省份 */
    @Schema(description = "省份")
    private String province;

    /** 经度 */
    @Schema(description = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Schema(description = "纬度")
    private BigDecimal latitude;

    /** 相关详情 */
    @Schema(description = "相关详情")
    private String details;

    /** 想去数 */
    @Schema(description = "想去数")
    private Integer wantToGoCount;

    /** 已去过数 */
    @Schema(description = "已去过数")
    private Integer visitedCount;

    /** 评价数 */
    @Schema(description = "评价数")
    private Integer reviewCount;

    /** 评价星级 */
    @Schema(description = "评价星级")
    private BigDecimal rating;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortOrder;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 是否想去 */
    @Schema(description = "当前会员是否已标记想去")
    private Boolean wantToGo;

    /** 是否去过 */
    @Schema(description = "当前会员是否已标记去过")
    private Boolean visited;

    /** 是否评价 */
    @Schema(description = "当前会员是否已评价")
    private Boolean reviewed;

    /** 我的评分 */
    @Schema(description = "当前会员的评分")
    private BigDecimal myRating;

    /** 去过时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "去过时间")
    private LocalDateTime visitTime;

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

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
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

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
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

    public Boolean getWantToGo()
    {
        return wantToGo;
    }

    public void setWantToGo(Boolean wantToGo)
    {
        this.wantToGo = wantToGo;
    }

    public Boolean getVisited()
    {
        return visited;
    }

    public void setVisited(Boolean visited)
    {
        this.visited = visited;
    }

    public Boolean getReviewed()
    {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed)
    {
        this.reviewed = reviewed;
    }

    public BigDecimal getMyRating()
    {
        return myRating;
    }

    public void setMyRating(BigDecimal myRating)
    {
        this.myRating = myRating;
    }

    public LocalDateTime getVisitTime()
    {
        return visitTime;
    }

    public void setVisitTime(LocalDateTime visitTime)
    {
        this.visitTime = visitTime;
    }
}
