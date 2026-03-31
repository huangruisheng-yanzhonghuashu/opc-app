package com.opc.web.controller.core;

import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.dto.twitter.v2.TweetDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import com.opc.web.service.twitter.v2.TwitterApiV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.opc.web.controller.core.OpenCliConstants.*;

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

    @Autowired
    private ICoreMaterialService materialService;

    /**
     * 搜索最近推文
     *
     * @param query        搜索关键词
     * @param maxResults   返回结果数量（10-100，默认 10）
     * @param nextToken    分页令牌
     * @param tweetFields  返回的推文字段
     * @param expansions   扩展数据
     * @param startTime    开始时间（ISO 8601格式）
     * @param endTime      结束时间（ISO 8601格式）
     * @return 搜索结果
     */
    @Operation(summary = "搜索最近推文", description = "根据关键词搜索 Twitter 最近 7 天的推文")
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Twitter API v2 搜索", businessType = BusinessType.OTHER)
    @GetMapping("/search")
    public AjaxResult search(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String query,
            @Parameter(description = "返回结果数量（10-100，默认 10）") @RequestParam(required = false, defaultValue = "10") Integer maxResults,
            @Parameter(description = "分页令牌") @RequestParam(required = false) String nextToken,
            @Parameter(description = "返回的推文字段，逗号分隔") @RequestParam(required = false) String tweetFields,
            @Parameter(description = "扩展数据") @RequestParam(required = false) String expansions,
            @Parameter(description = "开始时间（ISO 8601格式）") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间（ISO 8601格式）") @RequestParam(required = false) String endTime) {

        try {
            // 构建请求参数
            TwitterSearchRequestDTO request = new TwitterSearchRequestDTO();
            request.setQuery(query);
            request.setMaxResults(maxResults);
            request.setNextToken(nextToken);
            request.setStartTime(startTime);
            request.setEndTime(endTime);

            // 调用服务
            TwitterSearchResponseDTO response = twitterApiV2Service.searchRecentTweets(request);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tweets", response.getData());
            result.put("meta", response.getMeta());
            result.put("includes", response.getIncludes());

            return AjaxResult.success("搜索成功", result);

        } catch (Exception e) {
            log.error("Twitter API v2 搜索失败", e);
            return AjaxResult.error("搜索失败: " + e.getMessage());
        }
    }

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
            @Parameter(description = "开始时间（ISO 8601格式）") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间（ISO 8601格式）") @RequestParam(required = false) String endTime) {

        try {
            // 构建请求参数
            TwitterSearchRequestDTO request = new TwitterSearchRequestDTO();
            request.setQuery(query);
            request.setMaxResults(maxResults);
            request.setStartTime(startTime);
            request.setEndTime(endTime);

            // 调用服务搜索推文
            TwitterSearchResponseDTO response = twitterApiV2Service.searchRecentTweets(request);

            if (response.getData() == null || response.getData().isEmpty()) {
                return AjaxResult.success("未找到匹配的推文", new HashMap<String, Object>() {{
                    put("total", 0);
                    put("successCount", 0);
                    put("failCount", 0);
                }});
            }

            // 导入推文到素材表
            List<CoreMaterial> materials = convertToMaterials(response.getData());
            int successCount = 0;
            int failCount = 0;

            for (CoreMaterial material : materials) {
                try {
                    materialService.insertMaterial(material);
                    successCount++;
                } catch (Exception e) {
                    log.error("导入素材失败, originalId: {}", material.getOriginalId(), e);
                    failCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("keyword", query);
            result.put("total", materials.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("meta", response.getMeta());

            return AjaxResult.success("导入完成", result);

        } catch (Exception e) {
            log.error("Twitter API v2 导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 将推文数据转换为素材对象
     *
     * @param tweets 推文列表
     * @return 素材列表
     */
    private List<CoreMaterial> convertToMaterials(List<TweetDTO> tweets) {
        List<CoreMaterial> materials = new ArrayList<>();

        for (TweetDTO tweet : tweets) {
            CoreMaterial material = new CoreMaterial();

            // 设置原ID
            material.setOriginalId(tweet.getId());

            // 设置内容
            String text = tweet.getText();
            material.setContent(convertNewLineToBr(text));

            // 设置标题（取前50字符）
            String title = text != null && !text.isEmpty()
                    ? (text.length() > 50 ? text.substring(0, 50) + "..." : text)
                    : "Twitter 内容";
            material.setTitle(title);

            // 设置作者
            material.setAuthor(tweet.getAuthorId());

            // 设置原链接
            material.setOriginalUrl(twitterApiV2Service.buildTweetUrl(tweet.getId(), null));

            // 设置点赞数
            if (tweet.getPublicMetrics() != null) {
                material.setLikeCount(tweet.getPublicMetrics().getLikeCount() != null
                        ? tweet.getPublicMetrics().getLikeCount().longValue() : 0L);
                material.setViewCount(tweet.getPublicMetrics().getImpressionCount() != null
                        ? tweet.getPublicMetrics().getImpressionCount().longValue() : 0L);
            }

            // 设置套餐类型为普通会员 (1)
            material.setPackageType(PACKAGE_TYPE_NORMAL);

            // 设置状态为下线 (1)
            material.setStatus(STATUS_OFFLINE);

            // 设置内容类型为文本
            material.setContentType(CONTENT_TYPE_TEXT);

            // 设置来源
            material.setSource(SOURCE_TWITTER);

            // 设置发布时间
            if (tweet.getCreatedAt() != null) {
                try {
                    Instant instant = Instant.parse(tweet.getCreatedAt());
                    material.setPublishTime(instant);
                } catch (Exception e) {
                    material.setPublishTime(Instant.now());
                }
            } else {
                material.setPublishTime(Instant.now());
            }

            materials.add(material);
        }

        return materials;
    }

    /**
     * 将换行符转换为 HTML <br> 标签
     */
    private String convertNewLineToBr(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
    }
}
