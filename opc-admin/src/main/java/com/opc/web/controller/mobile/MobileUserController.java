package com.opc.web.controller.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.domain.entity.SysUser;
import com.opc.common.utils.SecurityUtils;
import com.opc.system.service.ISysUserService;

/**
 * 移动端用户接口
 * 
 * @author opc
 */
@RestController
@RequestMapping("/mobile/user")
public class MobileUserController extends MobileBaseController
{
    @Autowired
    private ISysUserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/profile")
    public AjaxResult getUserProfile()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        return AjaxResult.success(user);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/info/{userId}")
    public AjaxResult getUserInfo(Long userId)
    {
        SysUser user = userService.selectUserById(userId);
        return AjaxResult.success(user);
    }
}
