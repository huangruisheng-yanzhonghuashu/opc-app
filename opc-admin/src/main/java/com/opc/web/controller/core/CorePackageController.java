package com.opc.web.controller.core;

import java.util.List;
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
import com.opc.core.domain.CorePackage;
import com.opc.core.service.ICorePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "套餐管理", description = "套餐配置相关操作")
@RestController
@RequestMapping("/core/package")
public class CorePackageController extends BaseController
{
    @Autowired
    private ICorePackageService packageService;

    @Operation(summary = "获取套餐列表", description = "分页查询套餐列表")
    @PreAuthorize("@ss.hasPermi('core:package:list')")
    @GetMapping("/list")
    public TableDataInfo list(CorePackage pkg)
    {
        startPage();
        List<CorePackage> list = packageService.selectPackageList(pkg);
        return getDataTable(list);
    }

    @Operation(summary = "获取套餐详情", description = "根据套餐ID获取详细信息")
    @Parameter(name = "id", description = "套餐ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:package:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(packageService.selectPackageById(id));
    }

    @Operation(summary = "新增套餐", description = "新增套餐信息")
    @PreAuthorize("@ss.hasPermi('core:package:add')")
    @Log(title = "套餐管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CorePackage pkg)
    {
        pkg.setCreateBy(getUsername());
        return toAjax(packageService.insertPackage(pkg));
    }

    @Operation(summary = "修改套餐", description = "修改套餐信息")
    @PreAuthorize("@ss.hasPermi('core:package:edit')")
    @Log(title = "套餐管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CorePackage pkg)
    {
        pkg.setUpdateBy(getUsername());
        return toAjax(packageService.updatePackage(pkg));
    }

    @Operation(summary = "删除套餐", description = "根据套餐ID删除套餐")
    @Parameter(name = "id", description = "套餐ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:package:remove')")
    @Log(title = "套餐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(packageService.deletePackageById(id));
    }

    @Operation(summary = "批量删除套餐", description = "批量删除套餐")
    @Parameter(name = "ids", description = "套餐ID数组", required = true)
    @PreAuthorize("@ss.hasPermi('core:package:remove')")
    @Log(title = "套餐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids)
    {
        return toAjax(packageService.deletePackageByIds(ids));
    }
}
