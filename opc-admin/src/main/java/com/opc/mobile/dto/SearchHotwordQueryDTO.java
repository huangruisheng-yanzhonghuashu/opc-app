package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 搜索热词查询DTO
 *
 * @author opc
 */
@Schema(description = "搜索热词查询参数")
public class SearchHotwordQueryDTO {

    /** 热词关键字（模糊查询） */
    @Schema(description = "热词关键字，支持模糊查询", example = "热门")
    private String keyword;

    /** 页码 */
    @Schema(description = "页码，默认为1", example = "1")
    private Integer pageNum = 1;

    /** 每页大小 */
    @Schema(description = "每页大小，默认为10", example = "10")
    private Integer pageSize = 10;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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
