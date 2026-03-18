package com.opc.web.controller.core;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.opc.core.domain.CoreMember;
import com.opc.core.service.ICoreMemberService;

@RestController
@RequestMapping("/core/member")
public class CoreMemberController extends BaseController
{
    @Autowired
    private ICoreMemberService memberService;

    @PreAuthorize("@ss.hasPermi('core:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreMember member)
    {
        startPage();
        List<CoreMember> list = memberService.selectMemberList(member);
        return getDataTable(list);
    }

    @Log(title = "会员管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:member:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreMember member)
    {
        List<CoreMember> list = memberService.selectMemberList(member);
        ExcelUtil<CoreMember> util = new ExcelUtil<CoreMember>(CoreMember.class);
        util.exportExcel(response, list, "会员数据");
    }

    @PreAuthorize("@ss.hasPermi('core:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable Long memberId)
    {
        return success(memberService.selectMemberById(memberId));
    }

    @PreAuthorize("@ss.hasPermi('core:member:add')")
    @Log(title = "会员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CoreMember member)
    {
        if (!memberService.checkMemberNameUnique(member))
        {
            return error("新增会员'" + member.getUsername() + "'失败，会员名已存在");
        }
        else if (!memberService.checkPhoneUnique(member))
        {
            return error("新增会员'" + member.getUsername() + "'失败，手机号已存在");
        }
        else if (!memberService.checkEmailUnique(member))
        {
            return error("新增会员'" + member.getUsername() + "'失败，邮箱已存在");
        }
        member.setCreateBy(getUsername());
        return toAjax(memberService.insertMember(member));
    }

    @PreAuthorize("@ss.hasPermi('core:member:edit')")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CoreMember member)
    {
        if (!memberService.checkMemberNameUnique(member))
        {
            return error("修改会员'" + member.getUsername() + "'失败，会员名已存在");
        }
        else if (!memberService.checkPhoneUnique(member))
        {
            return error("修改会员'" + member.getUsername() + "'失败，手机号已存在");
        }
        else if (!memberService.checkEmailUnique(member))
        {
            return error("修改会员'" + member.getUsername() + "'失败，邮箱已存在");
        }
        member.setUpdateBy(getUsername());
        return toAjax(memberService.updateMember(member));
    }

    @PreAuthorize("@ss.hasPermi('core:member:remove')")
    @Log(title = "会员管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(memberService.deleteMemberByIds(memberIds));
    }
}
