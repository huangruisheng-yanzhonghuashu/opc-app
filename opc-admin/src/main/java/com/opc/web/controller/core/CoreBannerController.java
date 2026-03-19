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
import com.opc.core.domain.CoreBanner;
import com.opc.core.service.ICoreBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "资讯页banner配置", description = "Banner相关操作")
@RestController
@RequestMapping("/core/banner")
public class CoreBannerController extends BaseController
{
    @Autowired
    private ICoreBannerService bannerService;

    @Operation(summary = "获取Banner列表", description = "分页查询Banner列表")
    @PreAuthorize("@ss.hasPermi('core:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreBanner banner)
    {
        startPage();
        List<CoreBanner> list = bannerService.selectBannerList(banner);
        return getDataTable(list);
    }

    @Log(title = "资讯页banner配置", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:banner:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreBanner banner)
    {
        List<CoreBanner> list = bannerService.selectBannerList(banner);
        ExcelUtil<CoreBanner> util = new ExcelUtil<CoreBanner>(CoreBanner.class);
        util.exportExcel(response, list, "Banner数据");
    }

    @Operation(summary = "获取Banner详情", description = "根据BannerID获取详细信息")
    @Parameter(name = "id", description = "BannerID", required = true)
    @PreAuthorize("@ss.hasPermi('core:banner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(bannerService.selectBannerById(id));
    }

    @Operation(summary = "新增Banner", description = "新增Banner信息")
    @PreAuthorize("@ss.hasPermi('core:banner:add')")
    @Log(title = "资讯页banner配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreBanner banner)
    {
        banner.setCreateBy(getUsername());
        return toAjax(bannerService.insertBanner(banner));
    }

    @Operation(summary = "修改Banner", description = "修改Banner信息")
    @PreAuthorize("@ss.hasPermi('core:banner:edit')")
    @Log(title = "资讯页banner配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreBanner banner)
    {
        banner.setUpdateBy(getUsername());
        return toAjax(bannerService.updateBanner(banner));
    }

    @Operation(summary = "删除Banner", description = "删除Banner信息")
    @Parameter(name = "id", description = "BannerID", required = true)
    @PreAuthorize("@ss.hasPermi('core:banner:remove')")
    @Log(title = "资讯页banner配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bannerService.deleteBannerByIds(ids));
    }

    @Operation(summary = "修改Banner状态", description = "修改Banner启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:banner:changeStatus')")
    @Log(title = "资讯页banner配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreBanner banner)
    {
        return toAjax(bannerService.changeStatus(banner.getId(), banner.getStatus()));
    }
}
