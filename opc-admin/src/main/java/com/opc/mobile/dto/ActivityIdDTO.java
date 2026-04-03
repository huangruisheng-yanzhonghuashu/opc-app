package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 活动ID DTO
 *
 * @author opc
 */
@Schema(description = "活动ID参数")
public class ActivityIdDTO {

    /** 活动ID */
    @NotNull(message = "活动ID不能为空")
    @Schema(description = "活动ID", example = "1", required = true)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
