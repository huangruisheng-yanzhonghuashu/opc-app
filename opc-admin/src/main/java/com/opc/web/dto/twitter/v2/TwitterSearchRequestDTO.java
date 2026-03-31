package com.opc.web.dto.twitter.v2;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Twitter API v2 搜索请求 DTO
 * <p>
 * 封装 Twitter API v2 搜索接口的请求参数
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Schema(description = "Twitter API v2 搜索请求参数")
public class TwitterSearchRequestDTO {

    @Schema(description = "搜索查询语句", required = true, example = "Java programming")
    private String query;

    @Schema(description = "返回结果数量（10-100，默认 10）", example = "10")
    private Integer maxResults = 10;

    @Schema(description = "分页令牌", example = "b26v89c19zqg8o3fosbs7l4ecm0k8v0mxv7nucx1d0e9p")
    private String nextToken;

    @Schema(description = "开始时间（ISO 8601格式）", example = "2024-01-01T00:00:00Z")
    private String startTime;

    @Schema(description = "结束时间（ISO 8601格式）", example = "2024-01-31T23:59:59Z")
    private String endTime;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
