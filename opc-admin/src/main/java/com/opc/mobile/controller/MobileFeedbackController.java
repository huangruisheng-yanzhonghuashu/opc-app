package com.opc.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreFeedback;
import com.opc.core.domain.CoreMember;
import com.opc.core.service.ICoreFeedbackService;
import com.opc.core.service.ICoreMemberService;
import com.opc.mobile.dto.FeedbackSubmitDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 移动端意见反馈接口
 */
@Tag(name = "移动端意见反馈", description = "移动端会员意见反馈相关接口")
@RestController
@RequestMapping("/mobile/feedback")
public class MobileFeedbackController extends BaseController
{
    @Autowired
    private ICoreFeedbackService feedbackService;

    @Autowired
    private ICoreMemberService memberService;

    @Operation(summary = "提交意见反馈", description = "移动端会员提交意见反馈")
    @Log(title = "移动端意见反馈", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody FeedbackSubmitDTO submitDTO)
    {
        CoreFeedback feedback = new CoreFeedback();
        
        // 如果有会员ID，关联会员信息
        if (submitDTO.getMemberId() != null) {
            CoreMember member = memberService.selectMemberById(submitDTO.getMemberId());
            if (member != null) {
                feedback.setMemberId(member.getId());
                feedback.setMemberName(member.getNickname() != null ? member.getNickname() : member.getUsername());
            }
        }
        
        feedback.setType(submitDTO.getType());
        feedback.setTitle(submitDTO.getTitle());
        feedback.setContent(submitDTO.getContent());
        feedback.setContact(submitDTO.getContact());
        
        return toAjax(feedbackService.insertFeedback(feedback));
    }

    @Operation(summary = "获取反馈列表", description = "根据会员ID获取该会员的反馈列表")
    @Parameter(name = "memberId", description = "会员ID", required = true)
    @GetMapping("/list/{memberId}")
    public TableDataInfo list(@PathVariable Long memberId)
    {
        startPage();
        CoreFeedback feedback = new CoreFeedback();
        feedback.setMemberId(memberId);
        List<CoreFeedback> list = feedbackService.selectFeedbackList(feedback);
        return getDataTable(list);
    }

    @Operation(summary = "获取反馈详情", description = "根据反馈ID获取详细信息")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(feedbackService.selectFeedbackById(id));
    }
}
