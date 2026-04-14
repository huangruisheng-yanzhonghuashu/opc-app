package com.opc.web.controller.core;

import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.poi.ExcelUtil;
import com.opc.core.domain.CorePublishConfig;
import com.opc.core.service.ICorePublishConfigService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 发布配置Controller
 *
 * @author opc
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/core/publishConfig")
public class CorePublishConfigController extends BaseController {

    @Autowired
    private ICorePublishConfigService corePublishConfigService;

    /**
     * 查询发布配置列表
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(CorePublishConfig corePublishConfig) {
        startPage();
        List<CorePublishConfig> list = corePublishConfigService.selectCorePublishConfigList(corePublishConfig);
        return getDataTable(list);
    }

    /**
     * 导出发布配置列表
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:query')")
    @Log(title = "发布配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CorePublishConfig corePublishConfig) {
        List<CorePublishConfig> list = corePublishConfigService.selectCorePublishConfigList(corePublishConfig);
        ExcelUtil<CorePublishConfig> util = new ExcelUtil<>(CorePublishConfig.class);
        util.exportExcel(response, list, "发布配置数据");
    }

    /**
     * 获取发布配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(corePublishConfigService.selectCorePublishConfigById(id));
    }

    /**
     * 新增发布配置
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:save')")
    @Log(title = "发布配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CorePublishConfig corePublishConfig) {
        return toAjax(corePublishConfigService.insertCorePublishConfig(corePublishConfig));
    }

    /**
     * 修改发布配置
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:save')")
    @Log(title = "发布配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CorePublishConfig corePublishConfig) {
        return toAjax(corePublishConfigService.updateCorePublishConfig(corePublishConfig));
    }

    /**
     * 删除发布配置
     */
    @PreAuthorize("@ss.hasPermi('core:publishConfig:delete')")
    @Log(title = "发布配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(corePublishConfigService.deleteCorePublishConfigByIds(ids));
    }
}
