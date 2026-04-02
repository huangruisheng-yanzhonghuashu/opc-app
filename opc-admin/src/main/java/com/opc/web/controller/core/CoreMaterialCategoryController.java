package com.opc.web.controller.core;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.poi.ExcelUtil;
import com.opc.core.domain.CoreMaterialCategory;
import com.opc.core.service.ICoreMaterialCategoryService;

/**
 * 素材二级分类Controller
 *
 * @author opc
 */
@RestController
@RequestMapping("/core/materialCategory")
public class CoreMaterialCategoryController extends BaseController
{
    @Autowired
    private ICoreMaterialCategoryService coreMaterialCategoryService;

    /**
     * 查询素材二级分类列表
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:list')")
    @GetMapping("/list")
    public AjaxResult list(CoreMaterialCategory coreMaterialCategory)
    {
        List<CoreMaterialCategory> list = coreMaterialCategoryService.selectCoreMaterialCategoryList(coreMaterialCategory);
        return success(list);
    }

    /**
     * 根据套餐分类查询素材二级分类列表
     */
    @GetMapping("/listByPackageType/{packageType}")
    public AjaxResult listByPackageType(@PathVariable("packageType") Integer packageType)
    {
        List<CoreMaterialCategory> list = coreMaterialCategoryService.selectCoreMaterialCategoryByPackageType(packageType);
        return success(list);
    }

    /**
     * 查询所有启用的素材二级分类
     */
    @GetMapping("/listAllActive")
    public AjaxResult listAllActive()
    {
        List<CoreMaterialCategory> list = coreMaterialCategoryService.selectAllActiveCategory();
        return success(list);
    }

    /**
     * 导出素材二级分类列表
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:export')")
    @Log(title = "素材二级分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreMaterialCategory coreMaterialCategory)
    {
        List<CoreMaterialCategory> list = coreMaterialCategoryService.selectCoreMaterialCategoryList(coreMaterialCategory);
        ExcelUtil<CoreMaterialCategory> util = new ExcelUtil<CoreMaterialCategory>(CoreMaterialCategory.class);
        util.exportExcel(response, list, "素材二级分类数据");
    }

    /**
     * 获取素材二级分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(coreMaterialCategoryService.selectCoreMaterialCategoryById(id));
    }

    /**
     * 新增素材二级分类
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:add')")
    @Log(title = "素材二级分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CoreMaterialCategory coreMaterialCategory)
    {
        return toAjax(coreMaterialCategoryService.insertCoreMaterialCategory(coreMaterialCategory));
    }

    /**
     * 修改素材二级分类
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:edit')")
    @Log(title = "素材二级分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CoreMaterialCategory coreMaterialCategory)
    {
        return toAjax(coreMaterialCategoryService.updateCoreMaterialCategory(coreMaterialCategory));
    }

    /**
     * 删除素材二级分类
     */
    @PreAuthorize("@ss.hasPermi('core:materialCategory:remove')")
    @Log(title = "素材二级分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(coreMaterialCategoryService.deleteCoreMaterialCategoryByIds(ids));
    }
}
