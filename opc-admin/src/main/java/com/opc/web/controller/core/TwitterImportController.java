package com.opc.web.controller.core;

import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.web.service.core.CollectSourceFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Twitter 数据导入控制器
 * <p>
 * 通过调用 opencli 命令行工具，实现 Twitter 数据的搜索和导入功能。
 * 支持将 Twitter 推文数据解析并保存到 CoreMaterial 素材表中。
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>Twitter 推文搜索与导入 - 执行 opencli twitter search 命令</li>
 *   <li>自动翻译 - 使用 TranslateUtils 将推文内容翻译为中文</li>
 *   <li>素材表管理 - 将数据保存到 CoreMaterial 表</li>
 * </ul>
 *
 * <h3>API 端点：</h3>
 * <ul>
 *   <li>POST /core/twitter/search?keyword=xxx - 搜索并导入推文</li>
 * </ul>
 *
 * @author opc
 * @since 3.9.1
 * @see com.opc.core.domain.CoreMaterial
 * @see com.opc.core.service.ICoreMaterialService
 */
@Tag(name = "Twitter数据导入", description = "Twitter推文搜索和导入")
@RestController
@RequestMapping("/core/twitter")
public class TwitterImportController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TwitterImportController.class);

    @Autowired
    private CollectSourceFetchService collectSourceFetchService;

    /**
     * 搜索 Twitter 并导入到素材表
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    @Operation(summary = "搜索并导入推文", description = "根据关键词搜索Twitter推文并导入到素材表")
    @Parameter(name = "keyword", description = "搜索关键词", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Twitter搜索导入", businessType = BusinessType.IMPORT)
    @PostMapping("/search")
    public AjaxResult searchAndImport(@RequestParam String keyword) {
        log.info("Twitter 搜索导入请求，关键词: {}", keyword);
        return collectSourceFetchService.fetchTwitterData(keyword);
    }

}
