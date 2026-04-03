package com.opc.web.controller.core;

import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.service.twitter.v2.TwitterApiV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Twitter API v2 控制器
 * <p>
 * 提供 Twitter API v2 搜索和导入功能
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Tag(name = "Twitter API v2", description = "Twitter API v2 搜索和导入")
@RestController
@RequestMapping("/core/twitter/api-v2")
public class TwitterApiV2Controller extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TwitterApiV2Controller.class);

    @Autowired
    private TwitterApiV2Service twitterApiV2Service;

    /**
     * 搜索并导入推文到素材表
     *
     * @param query        搜索关键词
     * @param maxResults   返回结果数量
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 导入结果
     */
    @Operation(summary = "搜索并导入推文", description = "搜索 Twitter 推文并导入到素材表")
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Twitter API v2 导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult searchAndImport(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String query,
            @Parameter(description = "返回结果数量（10-100，默认 10）") @RequestParam(required = false, defaultValue = "10") Integer maxResults,
            @Parameter(description = "开始时间（yyyy-MM-dd格式）") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间（yyyy-MM-dd格式）") @RequestParam(required = false) String endTime) {

        try {
            // 构建请求参数
            TwitterSearchRequestDTO request = new TwitterSearchRequestDTO();
            request.setQuery(query);
            request.setMaxResults(maxResults);
            // 将 yyyy-MM-dd 格式转换为 ISO 8601 格式
            request.setStartTime(convertToIso8601(startTime, true));
            request.setEndTime(convertToIso8601(endTime, false));
            // 调用服务搜索推文
            twitterApiV2Service.searchRecentTweets(request);

            return AjaxResult.success("导入完成");

        } catch (Exception e) {
            log.error("Twitter API v2 导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }


    /**
     * 将 yyyy-MM-dd 格式转换为 ISO 8601 格式
     *
     * @param dateStr 日期字符串（yyyy-MM-dd格式）
     * @param isStart true=开始时间(00:00:00)，false=结束时间(23:59:59)
     * @return ISO 8601 格式字符串
     */
    private String convertToIso8601(String dateStr, boolean isStart) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            if (isStart) {
                return date.atStartOfDay(ZoneOffset.UTC).toInstant().toString();
            } else {
                return date.atTime(23, 59, 59).atOffset(ZoneOffset.UTC).toInstant().toString();
            }
        } catch (Exception e) {
            log.warn("日期格式转换失败: {}", dateStr);
            return null;
        }
    }
}
