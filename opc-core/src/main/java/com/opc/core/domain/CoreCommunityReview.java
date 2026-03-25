package com.opc.core.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CoreCommunityReview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "评价ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "社区ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "社区ID不能为空")
    private Long communityId;

    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Excel(name = "评价星级")
    @NotNull(message = "评价星级不能为空")
    private BigDecimal rating;

    @Excel(name = "评价内容")
    private String content;

    @Excel(name = "评价图片")
    private String images;

    @Excel(name = "状态", readConverterExp = "0=正常,1=隐藏,2=删除")
    private String status;

    @Excel(name = "点赞数")
    private Integer likeCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public BigDecimal getRating()
    {
        return rating;
    }

    public void setRating(BigDecimal rating)
    {
        this.rating = rating;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImages()
    {
        return images;
    }

    public void setImages(String images)
    {
        this.images = images;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount)
    {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("communityId", getCommunityId())
            .append("memberId", getMemberId())
            .append("rating", getRating())
            .append("content", getContent())
            .append("images", getImages())
            .append("status", getStatus())
            .append("likeCount", getLikeCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
