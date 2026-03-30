package com.opc.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.service.core.CollectSourceFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Reddit 数据导入控制器
 * <p>
 * 通过调用 opencli 命令行工具，实现 Reddit 帖子的搜索和导入功能。
 * 支持将 Reddit 帖子数据解析并保存到 CoreMaterial 素材表中，同时获取详细内容。
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>Reddit 帖子搜索与导入 - 执行 opencli reddit search 命令</li>
 *   <li>帖子详情获取 - 执行 opencli reddit read 命令获取正文内容</li>
 *   <li>自动翻译 - 使用 TranslateUtils 将帖子内容翻译为中文</li>
 *   <li>素材表管理 - 将数据保存到 CoreMaterial 表</li>
 * </ul>
 *
 * <h3>API 端点：</h3>
 * <ul>
 *   <li>POST /core/reddit/search?keyword=xxx - 搜索并导入 Reddit 帖子</li>
 * </ul>
 *
 * <h3>数据处理流程：</h3>
 * <ol>
 *   <li>执行 reddit search 获取帖子列表</li>
 *   <li>对每个帖子执行 reddit read 获取详细内容</li>
 *   <li>翻译内容并保存到 CoreMaterial 表</li>
 * </ol>
 *
 * @author opc
 * @since 3.9.1
 * @see com.opc.core.domain.CoreMaterial
 * @see com.opc.core.service.ICoreMaterialService
 */
@Tag(name = "Reddit数据导入", description = "Reddit帖子搜索和导入")
@RestController
@RequestMapping("/core/reddit")
public class RedditImportController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RedditImportController.class);

    @Autowired
    private CollectSourceFetchService collectSourceFetchService;

    /**
     * 搜索 Reddit 并导入到素材表
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    @Operation(summary = "搜索并导入Reddit帖子", description = "根据关键词搜索Reddit帖子并导入到素材表")
    @Parameter(name = "keyword", description = "搜索关键词", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Reddit搜索导入", businessType = BusinessType.IMPORT)
    @PostMapping("/search")
    public AjaxResult searchRedditAndImport(@RequestParam String keyword) {
        log.info("Reddit 搜索导入请求，关键词: {}", keyword);
        return collectSourceFetchService.fetchRedditData(keyword);
    }

}
