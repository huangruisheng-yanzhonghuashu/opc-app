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
import com.opc.core.domain.CoreTag;
import com.opc.core.service.ICoreMaterialTagService;
import com.opc.core.service.ICoreTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "标签库管理", description = "素材标签相关操作")
@RestController
@RequestMapping("/core/tag")
public class CoreTagController extends BaseController
{
    @Autowired
    private ICoreTagService tagService;

    @Autowired
    private ICoreMaterialTagService materialTagService;

    @Operation(summary = "获取标签列表", description = "分页查询标签列表")
    @PreAuthorize("@ss.hasPermi('core:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreTag tag)
    {
        startPage();
        List<CoreTag> list = tagService.selectTagList(tag);
        return getDataTable(list);
    }

    @Operation(summary = "获取所有有效标签", description = "获取所有状态正常的标签")
    @PreAuthorize("@ss.hasPermi('core:tag:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<CoreTag> list = tagService.selectAllActiveTags();
        return success(list);
    }

    @Log(title = "标签库管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:tag:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreTag tag)
    {
        List<CoreTag> list = tagService.selectTagList(tag);
        ExcelUtil<CoreTag> util = new ExcelUtil<CoreTag>(CoreTag.class);
        util.exportExcel(response, list, "标签数据");
    }

    @Operation(summary = "获取标签详情", description = "根据标签ID获取详细信息")
    @Parameter(name = "id", description = "标签ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(tagService.selectTagById(id));
    }

    @Operation(summary = "新增标签", description = "新增标签信息")
    @PreAuthorize("@ss.hasPermi('core:tag:add')")
    @Log(title = "标签库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreTag tag)
    {
        // 检查标签名是否已存在
        CoreTag existingTag = tagService.selectTagByName(tag.getTagName());
        if (existingTag != null)
        {
            return error("标签名称已存在");
        }
        tag.setCreateBy(getUsername());
        return toAjax(tagService.insertTag(tag));
    }

    @Operation(summary = "修改标签", description = "修改标签信息")
    @PreAuthorize("@ss.hasPermi('core:tag:edit')")
    @Log(title = "标签库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreTag tag)
    {
        // 检查标签名是否已存在（排除当前标签）
        CoreTag existingTag = tagService.selectTagByName(tag.getTagName());
        if (existingTag != null && !existingTag.getId().equals(tag.getId()))
        {
            return error("标签名称已存在");
        }
        tag.setUpdateBy(getUsername());
        return toAjax(tagService.updateTag(tag));
    }

    @Operation(summary = "删除标签", description = "删除标签信息")
    @Parameter(name = "id", description = "标签ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:tag:remove')")
    @Log(title = "标签库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 删除标签前，先删除关联的素材标签关系
        for (Long id : ids)
        {
            materialTagService.deleteMaterialTagByTagId(id);
        }
        return toAjax(tagService.deleteTagByIds(ids));
    }
}
