package com.opc.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.constant.Constants;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.StringUtils;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.IMemberLoginService;
import com.opc.core.service.MemberTokenService;
import com.opc.mobile.dto.MemberLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OpenAPI/会员登录", description = "移动端会员邮箱密码登录相关接口")
@RestController
@RequestMapping("/mobile/member")
public class MemberLoginController
{
    @Autowired
    private IMemberLoginService memberLoginService;

    @Autowired
    private MemberTokenService memberTokenService;

    @Operation(summary = "会员登录", description = "会员使用邮箱和密码登录，成功返回JWT令牌")
    @Log(title = "会员登录", businessType = BusinessType.OTHER)
    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody MemberLoginDTO loginDTO)
    {
        String token = memberLoginService.login(loginDTO.getEmail(), loginDTO.getPassword());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @Operation(summary = "获取会员信息", description = "获取当前登录会员的详细信息")
    @GetMapping("/getMemberInfo")
    public AjaxResult getMemberInfo()
    {
        MemberLoginVO memberLoginVO = memberLoginService.getMemberLoginUser();
        if (StringUtils.isNull(memberLoginVO))
        {
            return AjaxResult.error("用户未登录");
        }
        return AjaxResult.success(memberLoginVO);
    }

    @Operation(summary = "会员登出", description = "退出当前会员登录")
    @Log(title = "会员登出", businessType = BusinessType.OTHER)
    @PostMapping("/logout")
    public AjaxResult logout()
    {
        String token = memberTokenService.getHeader();
        if (StringUtils.isNotEmpty(token))
        {
            memberLoginService.logout(token);
        }
        return AjaxResult.success();
    }
}