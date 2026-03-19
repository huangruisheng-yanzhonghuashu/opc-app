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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "采集信息源配置", description = "采集信息源相关操作")
@RestController
@RequestMapping("/core/collect")
public class CoreCollectSourceController extends BaseController
{
    @Autowired
    private ICoreCollectSourceService collectSourceService;

    @Operation(summary = "获取采集信息源列表", description = "分页查询采集信息源列表")
    @PreAuthorize("@ss.hasPermi('core:collect:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreCollectSource collectSource)
    {
        startPage();
        List<CoreCollectSource> list = collectSourceService.selectCollectSourceList(collectSource);
        return getDataTable(list);
    }

    @Log(title = "采集信息源配置", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:collect:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreCollectSource collectSource)
    {
        List<CoreCollectSource> list = collectSourceService.selectCollectSourceList(collectSource);
        ExcelUtil<CoreCollectSource> util = new ExcelUtil<CoreCollectSource>(CoreCollectSource.class);
        util.exportExcel(response, list, "采集信息源数据");
    }

    @Operation(summary = "获取采集信息源详情", description = "根据ID获取详细信息")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:collect:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(collectSourceService.selectCollectSourceById(id));
    }

    @Operation(summary = "新增采集信息源", description = "新增采集信息源配置")
    @PreAuthorize("@ss.hasPermi('core:collect:add')")
    @Log(title = "采集信息源配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreCollectSource collectSource)
    {
        collectSource.setCreateBy(getUsername());
        return toAjax(collectSourceService.insertCollectSource(collectSource));
    }

    @Operation(summary = "修改采集信息源", description = "修改采集信息源配置")
    @PreAuthorize("@ss.hasPermi('core:collect:edit')")
    @Log(title = "采集信息源配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreCollectSource collectSource)
    {
        collectSource.setUpdateBy(getUsername());
        return toAjax(collectSourceService.updateCollectSource(collectSource));
    }

    @Operation(summary = "删除采集信息源", description = "删除采集信息源配置")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:collect:remove')")
    @Log(title = "采集信息源配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(collectSourceService.deleteCollectSourceByIds(ids));
    }

    @Operation(summary = "修改采集信息源状态", description = "修改采集信息源启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:collect:changeStatus')")
    @Log(title = "采集信息源配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreCollectSource collectSource)
    {
        return toAjax(collectSourceService.changeStatus(collectSource.getId(), collectSource.getStatus()));
    }
}
