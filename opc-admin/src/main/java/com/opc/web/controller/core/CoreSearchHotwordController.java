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
import com.opc.core.domain.CoreSearchHotword;
import com.opc.core.service.ICoreSearchHotwordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "搜索热词配置", description = "搜索热词相关操作")
@RestController
@RequestMapping("/core/searchHotword")
public class CoreSearchHotwordController extends BaseController
{
    @Autowired
    private ICoreSearchHotwordService searchHotwordService;

    @Operation(summary = "获取搜索热词列表", description = "分页查询搜索热词列表")
    @PreAuthorize("@ss.hasPermi('core:searchHotword:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreSearchHotword searchHotword)
    {
        startPage();
        List<CoreSearchHotword> list = searchHotwordService.selectSearchHotwordList(searchHotword);
        return getDataTable(list);
    }

    @Log(title = "搜索热词配置", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:searchHotword:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreSearchHotword searchHotword)
    {
        List<CoreSearchHotword> list = searchHotwordService.selectSearchHotwordList(searchHotword);
        ExcelUtil<CoreSearchHotword> util = new ExcelUtil<CoreSearchHotword>(CoreSearchHotword.class);
        util.exportExcel(response, list, "搜索热词数据");
    }

    @Operation(summary = "获取搜索热词详情", description = "根据热词ID获取详细信息")
    @Parameter(name = "id", description = "热词ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:searchHotword:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(searchHotwordService.selectSearchHotwordById(id));
    }

    @Operation(summary = "新增搜索热词", description = "新增搜索热词信息")
    @PreAuthorize("@ss.hasPermi('core:searchHotword:add')")
    @Log(title = "搜索热词配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreSearchHotword searchHotword)
    {
        searchHotword.setCreateBy(getUsername());
        return toAjax(searchHotwordService.insertSearchHotword(searchHotword));
    }

    @Operation(summary = "修改搜索热词", description = "修改搜索热词信息")
    @PreAuthorize("@ss.hasPermi('core:searchHotword:edit')")
    @Log(title = "搜索热词配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreSearchHotword searchHotword)
    {
        searchHotword.setUpdateBy(getUsername());
        return toAjax(searchHotwordService.updateSearchHotword(searchHotword));
    }

    @Operation(summary = "删除搜索热词", description = "删除搜索热词信息")
    @Parameter(name = "id", description = "热词ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:searchHotword:remove')")
    @Log(title = "搜索热词配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(searchHotwordService.deleteSearchHotwordByIds(ids));
    }

    @Operation(summary = "修改搜索热词状态", description = "修改搜索热词启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:searchHotword:changeStatus')")
    @Log(title = "搜索热词配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreSearchHotword searchHotword)
    {
        return toAjax(searchHotwordService.changeStatus(searchHotword.getId(), searchHotword.getStatus()));
    }
}
