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
import com.opc.core.domain.CoreFeedback;
import com.opc.core.service.ICoreFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "意见反馈管理", description = "意见反馈相关操作")
@RestController
@RequestMapping("/core/feedback")
public class CoreFeedbackController extends BaseController
{
    @Autowired
    private ICoreFeedbackService feedbackService;

    @Operation(summary = "获取意见反馈列表", description = "分页查询意见反馈列表")
    @PreAuthorize("@ss.hasPermi('core:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreFeedback feedback)
    {
        startPage();
        List<CoreFeedback> list = feedbackService.selectFeedbackList(feedback);
        return getDataTable(list);
    }

    @Log(title = "意见反馈管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:feedback:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreFeedback feedback)
    {
        List<CoreFeedback> list = feedbackService.selectFeedbackList(feedback);
        ExcelUtil<CoreFeedback> util = new ExcelUtil<CoreFeedback>(CoreFeedback.class);
        util.exportExcel(response, list, "意见反馈数据");
    }

    @Operation(summary = "获取意见反馈详情", description = "根据反馈ID获取详细信息")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:feedback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(feedbackService.selectFeedbackById(id));
    }

    @Operation(summary = "新增意见反馈", description = "新增意见反馈信息")
    @PreAuthorize("@ss.hasPermi('core:feedback:add')")
    @Log(title = "意见反馈管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreFeedback feedback)
    {
        feedback.setCreateBy(getUsername());
        return toAjax(feedbackService.insertFeedback(feedback));
    }

    @Operation(summary = "修改意见反馈", description = "修改意见反馈信息")
    @PreAuthorize("@ss.hasPermi('core:feedback:edit')")
    @Log(title = "意见反馈管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreFeedback feedback)
    {
        feedback.setUpdateBy(getUsername());
        return toAjax(feedbackService.updateFeedback(feedback));
    }

    @Operation(summary = "回复意见反馈", description = "对意见反馈进行回复")
    @PreAuthorize("@ss.hasPermi('core:feedback:reply')")
    @Log(title = "意见反馈管理", businessType = BusinessType.UPDATE)
    @PutMapping("/reply")
    public AjaxResult reply(@RequestBody CoreFeedback feedback)
    {
        feedback.setReplyBy(getUsername());
        return toAjax(feedbackService.replyFeedback(feedback));
    }

    @Operation(summary = "删除意见反馈", description = "根据ID删除意见反馈")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:feedback:remove')")
    @Log(title = "意见反馈管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(feedbackService.deleteFeedbackById(id));
    }

    @Operation(summary = "批量删除意见反馈", description = "根据ID数组批量删除意见反馈")
    @PreAuthorize("@ss.hasPermi('core:feedback:remove')")
    @Log(title = "意见反馈管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public AjaxResult removeBatch(@PathVariable Long[] ids)
    {
        return toAjax(feedbackService.deleteFeedbackByIds(ids));
    }
}
