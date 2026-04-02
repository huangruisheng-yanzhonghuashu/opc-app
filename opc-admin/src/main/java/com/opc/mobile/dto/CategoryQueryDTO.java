package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 素材二级分类查询DTO
 *
 * @author opc
 */
@Schema(description = "素材二级分类查询参数")
public class CategoryQueryDTO {

    /** 套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP） */
    @Schema(description = "套餐分类，0=晨报, 1=普通素材, 2=VIP素材, 3=超级VIP，默认2", example = "2")
    private Integer packageType = 2;

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }
}
