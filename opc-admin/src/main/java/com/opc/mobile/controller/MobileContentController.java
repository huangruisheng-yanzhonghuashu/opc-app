package com.opc.mobile.controller;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端内容接口
 *
 * @author opc
 */
@Tag(name = "内容管理", description = "移动端素材内容查看接口")
@RestController
@RequestMapping("/mobile/content")
public class MobileContentController extends BaseController
{
    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private MemberTokenService memberTokenService;

    /**
     * 获取素材详情
     *
     * @param id 素材ID
     * @param request HTTP请求
     * @return 素材详情
     */
    @Operation(summary = "获取内容详情", description = "根据内容ID获取详情，需要校验会员套餐权限")
    @Parameter(name = "id", description = "素材ID", required = true)
    @GetMapping("/material/{id}")
    public AjaxResult getMaterialDetail(@PathVariable Long id, HttpServletRequest request)
    {
        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        if (loginUser == null)
        {
            return AjaxResult.error("请先登录");
        }

        // 查询素材信息
        CoreMaterial material = materialService.selectMaterialById(id);
        if (material == null)
        {
            return AjaxResult.error("素材不存在");
        }

        // 检查素材状态
        if (!"0".equals(material.getStatus()))
        {
            return AjaxResult.error("素材已下线");
        }

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return AjaxResult.error("会员信息不存在");
        }

        // 校验套餐权限（调用父类方法）
        if (!checkPackagePermission(member.getPackageType(), material.getCategory()))
        {
            return AjaxResult.error("当前套餐无权限查看该内容");
        }

        return AjaxResult.success(material);
    }
}
