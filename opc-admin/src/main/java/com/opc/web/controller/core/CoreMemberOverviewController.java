package com.opc.web.controller.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.service.ICoreMemberService;

@RestController
@RequestMapping("/core/member/overview")
public class CoreMemberOverviewController extends BaseController
{
    @Autowired
    private ICoreMemberService memberService;

    @PreAuthorize("@ss.hasPermi('core:member:overview:query')")
    @GetMapping
    public AjaxResult getOverview(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDate == null || startDate.isEmpty())
        {
            startDate = LocalDate.now().minusMonths(1).format(formatter);
        }
        if (endDate == null || endDate.isEmpty())
        {
            endDate = LocalDate.now().format(formatter);
        }

        List<Map<String, Object>> overviewList = memberService.getMemberOverview(startDate, endDate);

        return success(overviewList);
    }
}
