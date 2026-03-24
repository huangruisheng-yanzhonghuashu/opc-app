package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 晨报素材查询DTO
 *
 * @author opc
 */
@Schema(description = "晨报素材查询参数")
public class MorningReportQueryDTO {

    /** 页码 */
    @Schema(description = "页码，默认为1", example = "1")
    private Integer pageNum = 1;

    /** 每页大小 */
    @Schema(description = "每页大小，默认为10", example = "10")
    private Integer pageSize = 10;

    /** 排序字段 */
    @Schema(description = "排序字段，可选值：create_time(创建时间),  view_count(查看数)，默认create_time", example = "create_time")
    private String orderByColumn = "create_time";

    /** 是否升序 */
    @Schema(description = "是否升序，true=升序，false=降序，默认false", example = "false")
    private Boolean isAsc = false;

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

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public Boolean getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(Boolean isAsc) {
        this.isAsc = isAsc;
    }
}
