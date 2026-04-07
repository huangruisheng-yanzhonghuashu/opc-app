package com.opc.web.controller.core;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.poi.ExcelUtil;
import com.opc.core.domain.CoreCollectSource;
import com.opc.core.service.ICoreCollectSourceService;
import com.opc.web.enums.SourceType;
import com.opc.web.service.core.CollectSourceFetchService;
import com.opc.web.service.twitter.v2.TwitterApiV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "采集信息源", description = "采集信息源相关操作")
@RestController
@RequestMapping("/core/collect")
public class CoreCollectSourceController extends BaseController {
    @Autowired
    private ICoreCollectSourceService collectSourceService;

    @Autowired
    private CollectSourceFetchService collectSourceFetchService;

    @Autowired
    private TwitterApiV2Service twitterApiV2Service;

    @Operation(summary = "获取采集信息源列表", description = "分页查询采集信息源列表")
    @PreAuthorize("@ss.hasPermi('core:collect:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreCollectSource collectSource) {
        startPage();
        List<CoreCollectSource> list = collectSourceService.selectCollectSourceList(collectSource);
        return getDataTable(list);
    }

    @Log(title = "采集信息源配置", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:collect:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreCollectSource collectSource) {
        List<CoreCollectSource> list = collectSourceService.selectCollectSourceList(collectSource);
        ExcelUtil<CoreCollectSource> util = new ExcelUtil<CoreCollectSource>(CoreCollectSource.class);
        util.exportExcel(response, list, "采集信息源数据");
    }

    @Operation(summary = "获取采集信息源详情", description = "根据ID获取详细信息")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:collect:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(collectSourceService.selectCollectSourceById(id));
    }

    @Operation(summary = "新增采集信息源", description = "新增采集信息源配置")
    @PreAuthorize("@ss.hasPermi('core:collect:add')")
    @Log(title = "采集信息源配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreCollectSource collectSource) {
        collectSource.setCreateBy(getUsername());
        return toAjax(collectSourceService.insertCollectSource(collectSource));
    }

    @Operation(summary = "修改采集信息源", description = "修改采集信息源配置")
    @PreAuthorize("@ss.hasPermi('core:collect:edit')")
    @Log(title = "采集信息源配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreCollectSource collectSource) {
        collectSource.setUpdateBy(getUsername());
        return toAjax(collectSourceService.updateCollectSource(collectSource));
    }

    @Operation(summary = "删除采集信息源", description = "删除采集信息源配置")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:collect:remove')")
    @Log(title = "采集信息源配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(collectSourceService.deleteCollectSourceByIds(ids));
    }

    @Operation(summary = "修改采集信息源状态", description = "修改采集信息源启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:collect:changeStatus')")
    @Log(title = "采集信息源配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreCollectSource collectSource) {
        return toAjax(collectSourceService.changeStatus(collectSource.getId(), collectSource.getStatus()));
    }

    @Operation(summary = "获取来源类型列表", description = "获取所有支持的来源类型，用于前端下拉选择")
    @PreAuthorize("@ss.hasPermi('core:collect:list')")
    @GetMapping("/sourceTypes")
    public AjaxResult getSourceTypes() {
        List<Map<String, String>> list = Arrays.stream(SourceType.values())
                .map(type -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("value", type.getValue());
                    map.put("label", type.getLabel());
                    return map;
                })
                .collect(Collectors.toList());
        return AjaxResult.success(list);
    }

    @Operation(summary = "获取数据", description = "根据来源类型调用对应接口获取数据（异步执行）")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:collect:query')")
    @Log(title = "采集信息源获取数据", businessType = BusinessType.OTHER)
    @PostMapping("/fetch/{id}")
    public AjaxResult fetchData(@PathVariable Long id) {
        CoreCollectSource collectSource = collectSourceService.selectCollectSourceById(id);
        if (collectSource == null) {
            return AjaxResult.error("采集信息源不存在");
        }

        String sourceType = collectSource.getSourceType();
        String sourceUrl = collectSource.getSourceUrl();
        String keyword = collectSource.getKeyword();

        if (sourceType == null || sourceType.isEmpty()) {
            return AjaxResult.error("来源类型不能为空");
        }

        // 根据来源类型调用不同服务方法
        SourceType type = SourceType.fromValue(sourceType);
        if (type == null) {
            return AjaxResult.error("该来源类型暂且不支持：" + sourceType);
        }

        // 异步执行数据获取任务
        collectSourceFetchService.fetchDataAsync(sourceType, sourceUrl, keyword);

        // 立即返回提示信息
        AjaxResult result = AjaxResult.success("数据正在后台同步中，稍后查看");
        result.put("sourceType", sourceType);
        result.put("keyword", keyword);
        return result;
    }

    /**
     * 处理 Twitter 数据获取
     */
    private AjaxResult handleTwitterFetch(String sourceUrl, String keyword) {
        String TWITTER_API_V2_SEARCH_RECENT = "https://api.x.com/2/tweets/search/recent";

        if (TWITTER_API_V2_SEARCH_RECENT.equals(sourceUrl)) {
            // API v2 搜索端点
            return collectSourceFetchService.fetchTwitterDataByApiV2(keyword);
        } else if (isTwitterUserUrl(sourceUrl)) {
            // Twitter 用户主页，提取用户名调用 fetchTwitterDataByUserName
            String userName = extractUserNameFromUrl(sourceUrl);
            if (userName != null && !userName.isEmpty()) {
                return collectSourceFetchService.fetchTwitterDataByUserName(userName);
            } else {
                return AjaxResult.error("无法从 URL 提取用户名: " + sourceUrl);
            }
        } else {
            // 其他情况使用 opencli
            return collectSourceFetchService.fetchTwitterData(keyword);
        }
    }

    /**
     * 处理 YouTube 数据获取
     */
    private AjaxResult handleYoutubeFetch(String sourceUrl, String keyword) {
        // 如果是 YouTube 视频链接，直接下载该视频
        //if (sourceUrl != null && (sourceUrl.contains("youtube.com") || sourceUrl.contains("youtu.be"))) {
            //return collectSourceFetchService.fetchYoutubeVideoByUrl(sourceUrl);
        //}
        // 否则使用关键词搜索
        return collectSourceFetchService.fetchYoutubeData(keyword);
    }

    /**
     * 判断是否是 Twitter 用户主页 URL (https://x.com/用户名)
     *
     * @param sourceUrl 信息源链接
     * @return true=是用户主页URL
     */
    private boolean isTwitterUserUrl(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            return false;
        }

        // 使用正则匹配 https://x.com/用户名
        // 用户名规则：1-15位，只能包含字母、数字、下划线
        String twitterUserPattern = "^https://x\\.com/[a-zA-Z0-9_]{1,15}$";
        return sourceUrl.matches(twitterUserPattern);
    }

    /**
     * 从 Twitter 用户主页 URL 中提取用户名
     *
     * @param sourceUrl Twitter 用户主页 URL，如 https://x.com/elonmusk
     * @return 用户名，如 elonmusk；提取失败返回 null
     */
    private String extractUserNameFromUrl(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            return null;
        }

        // 使用正则匹配并捕获用户名
        String twitterUserPattern = "^https://x\\.com/([a-zA-Z0-9_]{1,15})$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(twitterUserPattern);
        java.util.regex.Matcher matcher = pattern.matcher(sourceUrl);

        if (matcher.matches()) {
            return matcher.group(1); // 返回捕获的用户名
        }

        return null;
    }

}
