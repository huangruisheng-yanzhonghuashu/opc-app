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
import com.opc.core.domain.CoreActivityBanner;
import com.opc.core.service.ICoreActivityBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "活动Banner配置", description = "活动区Banner相关操作")
@RestController
@RequestMapping("/core/activityBanner")
public class CoreActivityBannerController extends BaseController
{
    @Autowired
    private ICoreActivityBannerService bannerService;

    @Operation(summary = "获取Banner列表", description = "分页查询活动Banner列表")
    @PreAuthorize("@ss.hasPermi('core:activityBanner:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreActivityBanner banner)
    {
        startPage();
        List<CoreActivityBanner> list = bannerService.selectActivityBannerList(banner);
        return getDataTable(list);
    }

    @Log(title = "活动Banner配置", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:activityBanner:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreActivityBanner banner)
    {
        List<CoreActivityBanner> list = bannerService.selectActivityBannerList(banner);
        ExcelUtil<CoreActivityBanner> util = new ExcelUtil<CoreActivityBanner>(CoreActivityBanner.class);
        util.exportExcel(response, list, "活动Banner数据");
    }

    @Operation(summary = "获取Banner详情", description = "根据BannerID获取详细信息")
    @Parameter(name = "id", description = "BannerID", required = true)
    @PreAuthorize("@ss.hasPermi('core:activityBanner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(bannerService.selectActivityBannerById(id));
    }

    @Operation(summary = "新增Banner", description = "新增活动Banner信息")
    @PreAuthorize("@ss.hasPermi('core:activityBanner:add')")
    @Log(title = "活动Banner配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreActivityBanner banner)
    {
        banner.setCreateBy(getUsername());
        return toAjax(bannerService.insertActivityBanner(banner));
    }

    @Operation(summary = "修改Banner", description = "修改活动Banner信息")
    @PreAuthorize("@ss.hasPermi('core:activityBanner:edit')")
    @Log(title = "活动Banner配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreActivityBanner banner)
    {
        banner.setUpdateBy(getUsername());
        return toAjax(bannerService.updateActivityBanner(banner));
    }

    @Operation(summary = "删除Banner", description = "删除活动Banner信息")
    @Parameter(name = "ids", description = "BannerID数组", required = true)
    @PreAuthorize("@ss.hasPermi('core:activityBanner:remove')")
    @Log(title = "活动Banner配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bannerService.deleteActivityBannerByIds(ids));
    }

    @Operation(summary = "修改Banner状态", description = "修改Banner启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:activityBanner:changeStatus')")
    @Log(title = "活动Banner配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreActivityBanner banner)
    {
        return toAjax(bannerService.changeStatus(banner.getId(), banner.getStatus()));
    }
}
