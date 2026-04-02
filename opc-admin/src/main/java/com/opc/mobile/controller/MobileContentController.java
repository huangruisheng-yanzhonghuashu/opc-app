package com.opc.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import com.opc.common.annotation.MemberLogin;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.HtmlWrapperUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import com.opc.mobile.dto.MaterialActionDTO;
import com.opc.mobile.dto.MaterialIdDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param dto 素材ID
     * @param request HTTP请求
     * @return 素材详情
     */
    @Operation(summary = "获取内容详情", description = "根据内容ID获取详情，需要校验会员套餐权限，同时增加查看数")
    @MemberLogin
    @PostMapping("/material/detail")
    public AjaxResult getMaterialDetail(@RequestBody MaterialIdDTO dto, HttpServletRequest request)
    {
        Long id = dto.getId();
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

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
        if (!checkPackagePermission(member.getPackageType(), material.getPackageType()))
        {
            return AjaxResult.error("当前套餐无权限查看该内容");
        }

        // 增加查看数
        materialService.incrementViewCount(id);

        // 包装content为完整HTML，适配移动端WebView显示（仅文章类型需要转换）
        if ("article".equals(material.getMaterialType())) {
            String wrappedContent = HtmlWrapperUtils.wrapContent(material.getContent());
            material.setContent(wrappedContent);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("material", material);

        return AjaxResult.success(result);
    }

    /**
     * 点赞/取消点赞
     *
     * @param dto 素材ID和操作类型
     * @param request HTTP请求
     * @return 操作结果
     */
    @Operation(summary = "喜欢/取消喜欢", description = "对素材进行喜欢或取消喜欢操作（幂等接口）")
    @MemberLogin
    @PostMapping("/material/like")
    public AjaxResult likeMaterial(@RequestBody MaterialActionDTO dto, HttpServletRequest request)
    {
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        boolean success = materialService.likeMaterial(dto.getMaterialId(), loginUser.getMemberId(), dto.getIsAction());
        // 幂等接口：只有素材不存在才返回失败，其他情况都返回成功
        return success ? AjaxResult.success() : AjaxResult.error("素材不存在");
    }

    /**
     * 不喜欢/取消不喜欢
     *
     * @param dto 素材ID和操作类型
     * @param request HTTP请求
     * @return 操作结果
     */
    @Operation(summary = "不喜欢/取消不喜欢", description = "对素材进行不喜欢或取消不喜欢操作（幂等接口）")
    @MemberLogin
    @PostMapping("/material/dislike")
    public AjaxResult dislikeMaterial(@RequestBody MaterialActionDTO dto, HttpServletRequest request)
    {
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        boolean success = materialService.dislikeMaterial(dto.getMaterialId(), loginUser.getMemberId(), dto.getIsAction());
        // 幂等接口：只有素材不存在才返回失败，其他情况都返回成功
        return success ? AjaxResult.success() : AjaxResult.error("素材不存在");
    }
}
