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
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "素材管理", description = "素材相关操作")
@RestController
@RequestMapping("/core/material")
public class CoreMaterialController extends BaseController
{
    @Autowired
    private ICoreMaterialService materialService;

    @Operation(summary = "获取素材列表", description = "分页查询素材列表")
    @PreAuthorize("@ss.hasPermi('core:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreMaterial material)
    {
        startPage();
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    @Log(title = "素材管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:material:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreMaterial material)
    {
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        ExcelUtil<CoreMaterial> util = new ExcelUtil<CoreMaterial>(CoreMaterial.class);
        util.exportExcel(response, list, "素材数据");
    }

    @Operation(summary = "获取素材详情", description = "根据素材ID获取详细信息")
    @Parameter(name = "id", description = "素材ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(materialService.selectMaterialById(id));
    }

    @Operation(summary = "新增素材", description = "新增素材信息")
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "素材管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreMaterial material)
    {
        material.setCreateBy(getUsername());
        return toAjax(materialService.insertMaterial(material));
    }

    @Operation(summary = "修改素材", description = "修改素材信息")
    @PreAuthorize("@ss.hasPermi('core:material:edit')")
    @Log(title = "素材管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreMaterial material)
    {
        material.setUpdateBy(getUsername());
        return toAjax(materialService.updateMaterial(material));
    }

    @Operation(summary = "删除素材", description = "删除素材信息")
    @Parameter(name = "id", description = "素材ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:remove')")
    @Log(title = "素材管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialService.deleteMaterialByIds(ids));
    }

    @Operation(summary = "修改素材状态", description = "修改素材上下线状态")
    @PreAuthorize("@ss.hasPermi('core:material:changeStatus')")
    @Log(title = "素材管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreMaterial material)
    {
        return toAjax(materialService.changeStatus(material.getId(), material.getStatus()));
    }

    @Operation(summary = "修改素材置顶状态", description = "修改素材是否置顶")
    @PreAuthorize("@ss.hasPermi('core:material:changeTop')")
    @Log(title = "素材管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeTop")
    public AjaxResult changeTop(@RequestBody CoreMaterial material)
    {
        return toAjax(materialService.changeTop(material.getId(), material.getIsTop()));
    }
}
