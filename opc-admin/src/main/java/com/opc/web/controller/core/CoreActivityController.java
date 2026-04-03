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
import com.opc.core.domain.CoreActivity;
import com.opc.core.service.ICoreActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "活动管理", description = "活动相关操作")
@RestController
@RequestMapping("/core/activity")
public class CoreActivityController extends BaseController
{
    @Autowired
    private ICoreActivityService activityService;

    @Operation(summary = "获取活动列表", description = "分页查询活动列表")
    @PreAuthorize("@ss.hasPermi('core:activity:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreActivity activity)
    {
        startPage();
        List<CoreActivity> list = activityService.selectActivityList(activity);
        return getDataTable(list);
    }

    @Log(title = "活动管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:activity:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreActivity activity)
    {
        List<CoreActivity> list = activityService.selectActivityList(activity);
        ExcelUtil<CoreActivity> util = new ExcelUtil<CoreActivity>(CoreActivity.class);
        util.exportExcel(response, list, "活动数据");
    }

    @Operation(summary = "获取活动详情", description = "根据活动ID获取详细信息")
    @Parameter(name = "id", description = "活动ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:activity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(activityService.selectActivityById(id));
    }

    @Operation(summary = "新增活动", description = "新增活动信息")
    @PreAuthorize("@ss.hasPermi('core:activity:add')")
    @Log(title = "活动管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreActivity activity)
    {
        activity.setCreateBy(getUsername());
        return toAjax(activityService.insertActivity(activity));
    }

    @Operation(summary = "修改活动", description = "修改活动信息")
    @PreAuthorize("@ss.hasPermi('core:activity:edit')")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreActivity activity)
    {
        activity.setUpdateBy(getUsername());
        return toAjax(activityService.updateActivity(activity));
    }

    @Operation(summary = "删除活动", description = "删除活动信息")
    @Parameter(name = "ids", description = "活动ID数组", required = true)
    @PreAuthorize("@ss.hasPermi('core:activity:remove')")
    @Log(title = "活动管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(activityService.deleteActivityByIds(ids));
    }

    @Operation(summary = "修改活动状态", description = "修改活动启用/禁用状态")
    @PreAuthorize("@ss.hasPermi('core:activity:changeStatus')")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CoreActivity activity)
    {
        return toAjax(activityService.changeStatus(activity.getId(), activity.getStatus()));
    }
}
