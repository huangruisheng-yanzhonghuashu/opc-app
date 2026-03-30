package com.opc.web.dto.twitter.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * Twitter API v2 搜索响应 DTO
 * <p>
 * 对应 Twitter API v2 /tweets/search/recent 接口的响应结构
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Schema(description = "Twitter API v2 搜索响应")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterSearchResponseDTO {

    @Schema(description = "推文数据列表")
    private List<TweetDTO> data;

    @Schema(description = "元数据信息（分页等）")
    private Meta meta;

    @Schema(description = "扩展数据（如作者信息等）")
    private Map<String, Object> includes;

    @Schema(description = "错误信息")
    private List<Error> errors;

    /**
     * 元数据内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        @Schema(description = "结果总数", example = "10")
        @JsonProperty("result_count")
        private Integer resultCount;

        @Schema(description = "下一页令牌", example = "b26v89c19zqg8o3fosbs7l4ecm0k8v0mxv7nucx1d0e9p")
        @JsonProperty("next_token")
        private String nextToken;

        @Schema(description = "上一页令牌", example = "77qpmmws2z9p8v0mxv7nucx1d0e9pb26v89c19zqg8o3")
        @JsonProperty("previous_token")
        private String previousToken;

        @Schema(description = "最新推文ID", example = "1234567890123456789")
        @JsonProperty("newest_id")
        private String newestId;

        @Schema(description = "最旧推文ID", example = "1234567890123456700")
        @JsonProperty("oldest_id")
        private String oldestId;

        public Integer getResultCount() {
            return resultCount;
        }

        public void setResultCount(Integer resultCount) {
            this.resultCount = resultCount;
        }

        public String getNextToken() {
            return nextToken;
        }

        public void setNextToken(String nextToken) {
            this.nextToken = nextToken;
        }

        public String getPreviousToken() {
            return previousToken;
        }

        public void setPreviousToken(String previousToken) {
            this.previousToken = previousToken;
        }

        public String getNewestId() {
            return newestId;
        }

        public void setNewestId(String newestId) {
            this.newestId = newestId;
        }

        public String getOldestId() {
            return oldestId;
        }

        public void setOldestId(String oldestId) {
            this.oldestId = oldestId;
        }
    }

    /**
     * 错误信息内部类
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {

        @Schema(description = "错误标题", example = "Invalid Request")
        private String title;

        @Schema(description = "错误类型", example = "https://api.twitter.com/2/problems/invalid-request")
        private String type;

        @Schema(description = "错误详情", example = "One or more parameters to your request was invalid.")
        private String detail;

        @Schema(description = "HTTP 状态码", example = "400")
        private Integer status;

        @Schema(description = "错误参数")
        private Map<String, Object> parameters;

        @Schema(description = "错误消息", example = "Invalid query")
        private String message;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public List<TweetDTO> getData() {
        return data;
    }

    public void setData(List<TweetDTO> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Map<String, Object> getIncludes() {
        return includes;
    }

    public void setIncludes(Map<String, Object> includes) {
        this.includes = includes;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
