package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 最新期数素材查询DTO
 *
 * @author opc
 */
@Schema(description = "最新期数素材查询参数")
public class LatestMaterialQueryDTO {

    /** 二级分类ID */
    @Schema(description = "二级分类ID", example = "1", required = true)
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
