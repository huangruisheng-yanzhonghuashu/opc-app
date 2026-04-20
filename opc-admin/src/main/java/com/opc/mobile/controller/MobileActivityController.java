package com.opc.mobile.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreActivity;
import com.opc.core.domain.CoreActivityBanner;
import com.opc.core.service.ICoreActivityBannerService;
import com.opc.core.service.ICoreActivityService;
import com.opc.common.utils.HtmlWrapperUtils;
import com.opc.mobile.dto.ActivityBannerQueryDTO;
import com.opc.mobile.dto.ActivityIdDTO;
import com.opc.mobile.dto.ActivityQueryDTO;
import com.opc.mobile.vo.CoreActivityBannerVO;
import com.opc.mobile.vo.CoreActivityVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端活动接口
 *
 * @author opc
 */
@Tag(name = "会员活动", description = "移动端活动相关接口")
@RestController
@RequestMapping("/mobile/activity")
public class MobileActivityController extends BaseController
{
    @Autowired
    private ICoreActivityService activityService;

    @Autowired
    private ICoreActivityBannerService activityBannerService;

    /**
     * 分页查询活动Banner列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取活动Banner列表", description = "分页查询活动Banner列表，按sortOrder升序排序，只返回正常状态的Banner")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreActivityBannerVO.class)))
    @Parameter(name = "queryDTO", description = "活动Banner查询参数")
    @PostMapping("/banner/list")
    public TableDataInfo bannerList(@RequestBody ActivityBannerQueryDTO queryDTO)
    {
        CoreActivityBanner banner = new CoreActivityBanner();
        // 只查询正常状态的Banner
        banner.setStatus("0");

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 按sortOrder升序排序
        PageHelper.orderBy("sort_order asc");
        List<CoreActivityBanner> list = activityBannerService.selectActivityBannerList(banner);

        // 转换为VO列表
        List<CoreActivityBannerVO> voList = list.stream().map(b -> {
            CoreActivityBannerVO vo = new CoreActivityBannerVO();
            BeanUtils.copyProperties(b, vo);
            return vo;
        }).collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 分页查询活动列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取活动列表", description = "分页查询活动列表，按活动时间倒序排序，只返回正常状态的活动")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreActivityVO.class)))
    @Parameter(name = "queryDTO", description = "活动查询参数")
    @PostMapping("/list")
    public TableDataInfo activityList(@RequestBody ActivityQueryDTO queryDTO)
    {
        CoreActivity activity = new CoreActivity();
        // 只查询正常状态的活动
        activity.setStatus("0");

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 按活动时间倒序排序
        PageHelper.orderBy("activity_time desc");
        List<CoreActivity> list = activityService.selectActivityList(activity);

        // 转换为VO列表
        List<CoreActivityVO> voList = list.stream().map(a -> {
            CoreActivityVO vo = new CoreActivityVO();
            BeanUtils.copyProperties(a, vo);
            return vo;
        }).collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 获取活动详情
     *
     * @param dto 活动ID
     * @return 活动详情
     */
    @Operation(summary = "获取活动详情", description = "根据活动ID获取详细信息，活动详情字段使用HtmlWrapperUtils.wrapContent包装")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreActivityVO.class)))
    @PostMapping("/detail")
    public AjaxResult activityDetail(@RequestBody ActivityIdDTO dto)
    {
        Long id = dto.getId();
        CoreActivity activity = activityService.selectActivityById(id);
        if (activity == null)
        {
            return error("活动不存在");
        }

        // 检查活动状态
        if (!"0".equals(activity.getStatus()))
        {
            return error("活动已停用");
        }

        // 转换为VO
        CoreActivityVO vo = new CoreActivityVO();
        BeanUtils.copyProperties(activity, vo);

        // 使用HtmlWrapperUtils包装活动详情字段
        String wrappedDetail = HtmlWrapperUtils.wrapContent(activity.getActivityDetail());
        vo.setActivityDetail(wrappedDetail);

        return success(vo);
    }
}
