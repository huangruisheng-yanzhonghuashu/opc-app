package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 社区评价DTO
 * 用于会员提交或修改社区评价
 *
 * @author opc
 */
@Schema(description = "社区评价请求")
public class CommunityReviewDTO
{
    /** 社区ID */
    @Schema(description = "社区ID", required = true, example = "1")
    @NotNull(message = "社区ID不能为空")
    private Long communityId;

    /** 评分 */
    @Schema(description = "评分（1-5分）", required = true, example = "4")
    @NotNull(message = "评分不能为空")
    private BigDecimal rating;

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public BigDecimal getRating()
    {
        return rating;
    }

    public void setRating(BigDecimal rating)
    {
        this.rating = rating;
    }
}
