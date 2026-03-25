package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 社区会员关联DTO
 * 用于标记想去/去过等操作
 *
 * @author opc
 */
@Schema(description = "社区会员关联请求")
public class CommunityMemberDTO
{
    /** 社区ID */
    @Schema(description = "社区ID", required = true, example = "1")
    @NotNull(message = "社区ID不能为空")
    private Long communityId;

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }
}
