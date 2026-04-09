package com.opc.mobile.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.opc.mobile.vo.CoreCommunityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreCommunity;
import com.opc.core.domain.CoreCommunityReview;
import com.opc.core.domain.CoreCommunityVisited;
import com.opc.core.domain.CoreCommunityWantToGo;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreCommunityService;
import com.opc.core.service.ICoreCommunityReviewService;
import com.opc.core.service.ICoreCommunityWantToGoService;
import com.opc.core.service.ICoreCommunityVisitedService;
import com.opc.core.service.MemberTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.mobile.dto.CommunityIdDTO;
import com.opc.mobile.dto.CommunityMemberDTO;
import com.opc.mobile.dto.CommunityQueryDTO;
import com.opc.mobile.dto.CommunityReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "会员社区", description = "移动端会员社区相关接口")
@RestController
@RequestMapping("/mobile/community")
@MemberLogin
public class MemberCommunityController extends BaseController
{
    @Autowired
    private ICoreCommunityService communityService;

    @Autowired
    private ICoreCommunityReviewService reviewService;

    @Autowired
    private ICoreCommunityWantToGoService wantToGoService;

    @Autowired
    private ICoreCommunityVisitedService visitedService;

    @Autowired
    private MemberTokenService memberTokenService;

    @Operation(summary = "社区列表", description = "查询全部社区列表，按省份分组，需要会员登录")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreCommunityVO.class)))
    @MemberLogin
    @PostMapping("/list")
    public AjaxResult list(HttpServletRequest request)
    {
        CoreCommunity community = new CoreCommunity();
        community.setStatus("0");
        List<CoreCommunity> list = communityService.selectCommunityList(community);

        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        Long memberId = loginUser.getMemberId();

        // 批量查询会员的关联状态（性能优化）
        List<Long> communityIds = list.stream()
                .map(CoreCommunity::getId)
                .collect(Collectors.toList());

        // 查询想去记录
        List<CoreCommunityWantToGo> wantToGoList = wantToGoService.selectByMemberAndCommunityIds(memberId, communityIds);
        Map<Long, CoreCommunityWantToGo> wantToGoMap = wantToGoList.stream()
                .collect(Collectors.toMap(CoreCommunityWantToGo::getCommunityId, w -> w, (w1, w2) -> w1));

        // 查询去过记录
        List<CoreCommunityVisited> visitedList = visitedService.selectByMemberAndCommunityIds(memberId, communityIds);
        Map<Long, CoreCommunityVisited> visitedMap = visitedList.stream()
                .collect(Collectors.toMap(CoreCommunityVisited::getCommunityId, v -> v, (v1, v2) -> v1));

        // 查询评价记录
        List<CoreCommunityReview> reviewList = reviewService.selectByMemberAndCommunityIds(memberId, communityIds);
        Map<Long, CoreCommunityReview> reviewMap = reviewList.stream()
                .collect(Collectors.toMap(CoreCommunityReview::getCommunityId, r -> r, (r1, r2) -> r1));

        // 按省份分组并转换为VO
        Map<String, List<CoreCommunityVO>> groupedByProvince = list.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getProvince() != null ? c.getProvince() : "其他",
                        Collectors.mapping(c -> {
                            CoreCommunityVO vo = new CoreCommunityVO();
                            BeanUtils.copyProperties(c, vo);
                            Long communityId = c.getId();

                            // 设置会员关联状态
                            vo.setWantToGo(wantToGoMap.containsKey(communityId));
                            vo.setVisited(visitedMap.containsKey(communityId));

                            CoreCommunityReview review = reviewMap.get(communityId);
                            if (review != null) {
                                vo.setReviewed(true);
                                vo.setMyRating(review.getRating());
                            } else {
                                vo.setReviewed(false);
                                vo.setMyRating(null);
                            }

                            return vo;
                        }, Collectors.toList())
                ));

        return AjaxResult.success(groupedByProvince);
    }

    @Operation(summary = "社区详情", description = "根据社区ID获取详细信息，需要会员登录")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreCommunityVO.class)))
    @MemberLogin
    @PostMapping("/detail")
    public AjaxResult getInfo(@RequestBody CommunityIdDTO dto, HttpServletRequest request)
    {
        Long id = dto.getId();
        CoreCommunity community = communityService.selectCommunityById(id);
        if (community == null)
        {
            return AjaxResult.error("社区不存在");
        }

        // 检查社区状态
        if (!"0".equals(community.getStatus()))
        {
            return AjaxResult.error("社区已下线");
        }

        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        Long memberId = loginUser.getMemberId();

        // 构建VO对象
        CoreCommunityVO vo = new CoreCommunityVO();
        BeanUtils.copyProperties(community, vo);

        // 设置会员关联状态（需要判断状态是否为0）
        CoreCommunityWantToGo wantToGoRecord = wantToGoService.selectByCommunityAndMember(id, memberId);
        vo.setWantToGo(wantToGoRecord != null && "0".equals(wantToGoRecord.getStatus()));

        CoreCommunityVisited visitedRecord = visitedService.selectByCommunityAndMember(id, memberId);
        vo.setVisited(visitedRecord != null && "0".equals(visitedRecord.getStatus()));

        // 查询会员评价
        CoreCommunityReview reviewParam = new CoreCommunityReview();
        reviewParam.setCommunityId(id);
        reviewParam.setMemberId(memberId);
        List<CoreCommunityReview> reviews = reviewService.selectReviewList(reviewParam);
        if (reviews != null && !reviews.isEmpty()) {
            vo.setReviewed(true);
            vo.setMyRating(reviews.get(0).getRating());
        } else {
            vo.setReviewed(false);
            vo.setMyRating(null);
        }

        return AjaxResult.success(vo);
    }

    /**
     * 想去/取消想去
     * 会员标记或取消想去某个社区，已标记则取消，未标记则标记
     *
     * @param dto 社区会员关联信息
     * @param request HTTP请求
     * @return 操作结果
     */
    @Operation(summary = "想去标记或取消", description = "会员标记或取消想去某个社区，已标记则取消，未标记则标记，需要会员登录")
    @MemberLogin
    @PostMapping("/want")
    public AjaxResult toggleWantToGo(@RequestBody CommunityMemberDTO dto, HttpServletRequest request)
    {
        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        Long memberId = loginUser.getMemberId();

        CoreCommunityWantToGo existing = wantToGoService.selectByCommunityAndMember(dto.getCommunityId(), memberId);
        boolean isMarked = existing != null && "0".equals(existing.getStatus());
        if (isMarked) {
            int result = wantToGoService.unmarkWantToGo(dto.getCommunityId(), memberId);
            return result > 0 ? success("取消想去成功") : error("取消想去失败");
        } else {
            int result = wantToGoService.markWantToGo(dto.getCommunityId(), memberId);
            return result > 0 ? success("标记想去成功") : error("标记想去失败");
        }
    }

    /**
     * 去过/取消去过
     * 会员标记或取消去过某个社区，已标记则取消，未标记则标记
     *
     * @param dto 社区会员关联信息
     * @param request HTTP请求
     * @return 操作结果
     */
    @Operation(summary = "去过标记或取消", description = "会员标记或取消去过某个社区，已标记则取消，未标记则标记，需要会员登录")
    @MemberLogin
    @PostMapping("/visited")
    public AjaxResult toggleVisited(@RequestBody CommunityMemberDTO dto, HttpServletRequest request)
    {
        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        Long memberId = loginUser.getMemberId();

        CoreCommunityVisited existing = visitedService.selectByCommunityAndMember(dto.getCommunityId(), memberId);
        boolean isMarked = existing != null && "0".equals(existing.getStatus());
        if (isMarked) {
            int result = visitedService.unmarkVisited(dto.getCommunityId(), memberId);
            return result > 0 ? success("取消去过成功") : error("取消去过失败");
        } else {
            int result = visitedService.markVisited(dto.getCommunityId(), memberId);
            return result > 0 ? success("标记去过成功") : error("标记去过失败");
        }
    }

    /**
     * 提交/修改评价
     * 会员对社区进行评价（1-5分），已评价则修改，未评价则新增
     *
     * @param dto 评价信息
     * @param request HTTP请求
     * @return 操作结果
     */
    @Operation(summary = "评价提交或修改", description = "会员对社区进行评价（1-5分），已评价则修改，未评价则新增，需要会员登录")
    @MemberLogin
    @PostMapping("/review")
    public AjaxResult saveReview(@RequestBody CommunityReviewDTO dto, HttpServletRequest request)
    {
        if (dto == null || dto.getCommunityId() == null)
        {
            return error("社区ID不能为空");
        }
        if (dto.getRating() == null)
        {
            return error("评分不能为空");
        }
        BigDecimal rating = dto.getRating();
        if (rating.compareTo(BigDecimal.ONE) < 0 || rating.compareTo(new BigDecimal("5")) > 0)
        {
            return error("评分必须在1-5分之间");
        }

        // 获取当前登录会员
        MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
        Long memberId = loginUser.getMemberId();

        // 查询是否已存在评价
        CoreCommunityReview queryParam = new CoreCommunityReview();
        queryParam.setCommunityId(dto.getCommunityId());
        queryParam.setMemberId(memberId);
        List<CoreCommunityReview> existingReviews = reviewService.selectReviewList(queryParam);

        CoreCommunityReview review = new CoreCommunityReview();
        BeanUtils.copyProperties(dto, review);
        review.setMemberId(memberId);

        if (existingReviews != null && !existingReviews.isEmpty()) {
            // 已存在评价，进行修改
            CoreCommunityReview existingReview = existingReviews.get(0);
            review.setId(existingReview.getId());
            review.setStatus(existingReview.getStatus());
            int result = reviewService.updateReview(review);
            return result > 0 ? success("修改评价成功") : error("修改评价失败");
        } else {
            // 未评价，新增
            review.setStatus("0");
            int result = reviewService.insertReview(review);
            return result > 0 ? success("提交评价成功") : error("提交评价失败");
        }
    }
}
