package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 普通素材（非置顶）查询DTO
 *
 * @author opc
 */
@Schema(description = "普通素材查询参数")
public class NormalMaterialQueryDTO {

    /** 页码 */
    @Schema(description = "页码，默认为1", example = "1")
    private Integer pageNum = 1;

    /** 每页大小 */
    @Schema(description = "每页大小，默认为10", example = "10")
    private Integer pageSize = 10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
