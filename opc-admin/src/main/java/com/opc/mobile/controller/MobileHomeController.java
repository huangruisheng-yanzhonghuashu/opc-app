package com.opc.mobile.controller;

import java.util.List;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreBanner;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.CoreTag;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreBannerService;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.ICoreTagService;
import com.opc.core.service.MemberTokenService;
import com.opc.mobile.dto.BannerQueryDTO;
import com.opc.mobile.dto.MaterialByTagQueryDTO;
import com.opc.mobile.dto.MorningReportQueryDTO;
import com.opc.mobile.dto.NormalMaterialQueryDTO;
import com.opc.mobile.dto.TopMaterialQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 移动端首页接口
 *
 * @author opc
 */
@Tag(name = "会员首页", description = "移动端首页相关接口")
@RestController
@RequestMapping("/mobile/home")
@MemberLogin
public class MobileHomeController extends BaseController
{
    @Autowired
    private ICoreBannerService bannerService;

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreTagService tagService;

    @Autowired
    private MemberTokenService memberTokenService;

    @Autowired
    private ICoreMemberService memberService;

    /**
     * 分页查询Banner列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取Banner列表", description = "分页查询Banner列表，按sortOrder升序排序")
    @Parameter(name = "queryDTO", description = "Banner查询参数")
    @PostMapping("/banner/list")
    public TableDataInfo bannerList(@RequestBody BannerQueryDTO queryDTO)
    {
        CoreBanner banner = new CoreBanner();

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CoreBanner> list = bannerService.selectBannerList(banner);
        return getDataTable(list);
    }

    /**
     * 分页查询置顶素材列表
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 分页数据
     */
    @Operation(summary = "获取置顶内容列表", description = "查询置顶素材列表，按发布时间倒序排序")
    @Parameter(name = "queryDTO", description = "置顶素材查询参数")
    @PostMapping("/material/top/list")
    public TableDataInfo topMaterialList(@RequestBody TopMaterialQueryDTO queryDTO, HttpServletRequest request)
    {
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return getDataTable(List.of());
        }

        CoreMaterial material = new CoreMaterial();
        // 只查询置顶的素材
        material.setIsTop("1");
        // 只查询上线的素材
        material.setStatus("0");
        // 根据会员套餐类型限制素材packageType
        material.getParams().put("maxPackageType", member.getPackageType());

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 处理排序
        String orderBy = queryDTO.getOrderByColumn() + " " + (queryDTO.getIsAsc() ? "asc" : "desc");
        PageHelper.orderBy(orderBy);
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }


    /**
     * 分页查询普通素材（非置顶）列表
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 分页数据
     */
    @Operation(summary = "获取非置顶内容列表", description = "查询非置顶素材列表，按发布时间倒序排序")
    @Parameter(name = "queryDTO", description = "普通素材查询参数")
    @PostMapping("/material/normal/list")
    public TableDataInfo normalMaterialList(@RequestBody NormalMaterialQueryDTO queryDTO, HttpServletRequest request)
    {
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return getDataTable(List.of());
        }

        CoreMaterial material = new CoreMaterial();
        // 只查询非置顶的素材
        material.setIsTop("0");
        // 只查询上线的素材
        material.setStatus("0");
        // 根据会员套餐类型限制素材packageType
        material.getParams().put("maxPackageType", member.getPackageType());

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 处理排序
        String orderBy = queryDTO.getOrderByColumn() + " " + (queryDTO.getIsAsc() ? "asc" : "desc");
        PageHelper.orderBy(orderBy);
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    @Operation(summary = "获取标签列表", description = "查询启用的标签列表，按sortOrder升序排序")
    @GetMapping("/tag/list")
    public AjaxResult tagList()
    {
        CoreTag tag = new CoreTag();
        // 只查询启用的标签
        tag.setStatus("0");

        List<CoreTag> list = tagService.selectTagList(tag);
        return success(list);
    }

    /**
     * 根据标签查询素材列表
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 分页数据
     */
    @Operation(summary = "根据标签查询内容列表", description = "根据标签ID查询素材列表，按发布时间倒序排序")
    @PostMapping("/material/byTag/list")
    public TableDataInfo materialListByTag(@RequestBody MaterialByTagQueryDTO queryDTO, HttpServletRequest request)
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
        // 处理排序
        String orderBy = queryDTO.getOrderByColumn() + " " + (queryDTO.getIsAsc() ? "asc" : "desc");
        PageHelper.orderBy(orderBy);
        List<CoreMaterial> list = materialService.selectMaterialListByTagId(queryDTO.getTagId(), "0", member.getPackageType());
        return getDataTable(list);
    }

    /**
     * 分页查询晨报素材列表
     *
     * @param queryDTO 查询参数
     * @param request HTTP请求
     * @return 分页数据
     */
    @Operation(summary = "获取晨报内容列表", description = "查询当天创建的晨报素材列表，按发布时间倒序排序")
    @Parameter(name = "queryDTO", description = "晨报素材查询参数")
    @PostMapping("/material/morningReport/list")
    public TableDataInfo morningReportList(@RequestBody MorningReportQueryDTO queryDTO, HttpServletRequest request)
    {
        // 获取当前登录会员（已由拦截器验证）
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);

        // 查询会员信息
        CoreMember member = memberService.selectMemberById(loginUser.getMemberId());
        if (member == null)
        {
            return getDataTable(List.of());
        }

        CoreMaterial material = new CoreMaterial();
        // 只查询上线的素材
        material.setStatus("0");
        // 根据会员套餐类型限制素材packageType
        material.getParams().put("maxPackageType", member.getPackageType());
        // 设置查询当天创建的数据
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        material.getParams().put("beginCreateTime", today + " 00:00:00");
        material.getParams().put("endCreateTime", today + " 23:59:59");

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 处理排序
        String orderBy = queryDTO.getOrderByColumn() + " " + (queryDTO.getIsAsc() ? "asc" : "desc");
        PageHelper.orderBy(orderBy);
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

}
