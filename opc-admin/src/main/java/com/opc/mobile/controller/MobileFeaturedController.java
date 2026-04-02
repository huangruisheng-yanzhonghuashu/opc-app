package com.opc.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMaterialCategory;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMaterialCategoryService;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import com.opc.mobile.dto.CategoryQueryDTO;
import com.opc.mobile.dto.FeaturedMaterialQueryDTO;
import com.opc.mobile.dto.LatestMaterialQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端精选素材接口
 *
 * @author opc
 */
@Tag(name = "精选素材", description = "移动端精选素材相关接口")
@RestController
@RequestMapping("/mobile/featured")
@MemberLogin
public class MobileFeaturedController extends BaseController
{
    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreMaterialCategoryService categoryService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private MemberTokenService memberTokenService;

    /**
     * 查询素材二级分类列表
     *
     * @param queryDTO 查询参数
     * @return 分类列表
     */
    @Operation(summary = "获取素材二级分类列表", description = "根据套餐分类查询启用的素材二级分类列表")
    @Parameter(name = "queryDTO", description = "分类查询参数")
    @PostMapping("/category/list")
    public AjaxResult categoryList(@RequestBody CategoryQueryDTO queryDTO)
    {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setPackageType(queryDTO.getPackageType());
        // 只查询启用的分类
        category.setStatus("0");

        List<CoreMaterialCategory> list = categoryService.selectCoreMaterialCategoryList(category);
        return success(list);
    }

    /**
     * 根据二级分类查询最新期数的素材（最大的期数记录）
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 最新期数素材
     */
    @Operation(summary = "获取最新期数素材", description = "根据二级分类查询期数最大的素材记录")
    @Parameter(name = "queryDTO", description = "最新期数素材查询参数")
    @PostMapping("/material/latest")
    public AjaxResult getLatestMaterial(@RequestBody LatestMaterialQueryDTO queryDTO, HttpServletRequest request)
    {
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return AjaxResult.error("会员信息不存在");
        }

        // 查询最新期数的素材（不做package_type限制）
        CoreMaterial material = materialService.selectLatestMaterialByCategoryId(
                queryDTO.getCategoryId(),
                "0"
        );

        if (material == null)
        {
            return AjaxResult.error("暂无素材");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("material", material);

        return AjaxResult.success(result);
    }

    /**
     * 根据二级分类查询素材列表（排除期数最大的记录，按期数降序分页查询）
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 分页素材列表
     */
    @Operation(summary = "获取历史素材列表", description = "根据二级分类查询素材列表，排除最新期数，按期数降序分页")
    @Parameter(name = "queryDTO", description = "精选素材查询参数")
    @PostMapping("/material/list")
    public TableDataInfo getMaterialList(@RequestBody FeaturedMaterialQueryDTO queryDTO, HttpServletRequest request)
    {
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return getDataTable(List.of());
        }

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 按期数降序排序
        PageHelper.orderBy("issue_no desc");

        // 查询历史素材列表（不做package_type限制）
        List<CoreMaterial> list = materialService.selectMaterialListByCategoryIdExcludeLatest(
                queryDTO.getCategoryId(),
                "0"
        );
        return getDataTable(list);
    }
}
