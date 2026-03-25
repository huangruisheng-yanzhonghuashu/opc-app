package com.opc.core.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreCommunityReview;
import com.opc.core.mapper.CoreCommunityReviewMapper;
import com.opc.core.service.ICoreCommunityReviewService;

@Service
public class CoreCommunityReviewServiceImpl implements ICoreCommunityReviewService
{
    @Autowired
    private CoreCommunityReviewMapper reviewMapper;

    /**
     * 查询评价信息
     *
     * @param id 评价ID
     * @return 评价信息
     */
    @Override
    public CoreCommunityReview selectReviewById(Long id)
    {
        return reviewMapper.selectReviewById(id);
    }

    /**
     * 查询评价列表
     *
     * @param review 评价信息
     * @return 评价集合
     */
    @Override
    public List<CoreCommunityReview> selectReviewList(CoreCommunityReview review)
    {
        return reviewMapper.selectReviewList(review);
    }

    /**
     * 根据社区ID查询评价列表
     *
     * @param communityId 社区ID
     * @return 评价集合
     */
    @Override
    public List<CoreCommunityReview> selectReviewsByCommunityId(Long communityId)
    {
        return reviewMapper.selectReviewsByCommunityId(communityId);
    }

    /**
     * 新增评价
     *
     * @param review 评价信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertReview(CoreCommunityReview review)
    {
        int result = reviewMapper.insertReview(review);
        // 更新社区评价统计
        if (result > 0 && review.getCommunityId() != null)
        {
            reviewMapper.updateCommunityReviewStats(review.getCommunityId());
        }
        return result;
    }

    /**
     * 修改评价
     *
     * @param review 评价信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateReview(CoreCommunityReview review)
    {
        int result = reviewMapper.updateReview(review);
        // 更新社区评价统计
        if (result > 0 && review.getCommunityId() != null)
        {
            reviewMapper.updateCommunityReviewStats(review.getCommunityId());
        }
        return result;
    }

    /**
     * 批量删除评价
     *
     * @param ids 需要删除的评价ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteReviewByIds(Long[] ids)
    {
        // 先获取这些评价对应的社区ID
        Long communityId = null;
        if (ids != null && ids.length > 0)
        {
            CoreCommunityReview review = reviewMapper.selectReviewById(ids[0]);
            if (review != null)
            {
                communityId = review.getCommunityId();
            }
        }
        
        int result = reviewMapper.deleteReviewByIds(ids);
        // 更新社区评价统计
        if (result > 0 && communityId != null)
        {
            reviewMapper.updateCommunityReviewStats(communityId);
        }
        return result;
    }

    /**
     * 删除评价信息
     *
     * @param id 评价ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteReviewById(Long id)
    {
        // 先获取评价对应的社区ID
        CoreCommunityReview review = reviewMapper.selectReviewById(id);
        Long communityId = review != null ? review.getCommunityId() : null;
        
        int result = reviewMapper.deleteReviewById(id);
        // 更新社区评价统计
        if (result > 0 && communityId != null)
        {
            reviewMapper.updateCommunityReviewStats(communityId);
        }
        return result;
    }

    /**
     * 根据社区ID统计评价数量和平均星级
     *
     * @param communityId 社区ID
     * @return 统计结果
     */
    @Override
    public Map<String, Object> getReviewStatsByCommunityId(Long communityId)
    {
        return reviewMapper.getReviewStatsByCommunityId(communityId);
    }

    /**
     * 更新社区评价统计
     *
     * @param communityId 社区ID
     * @return 结果
     */
    @Override
    public int updateCommunityReviewStats(Long communityId)
    {
        return reviewMapper.updateCommunityReviewStats(communityId);
    }

    /**
     * 审核评价
     *
     * @param id 评价ID
     * @param status 状态（0正常 1隐藏 2删除）
     * @return 结果
     */
    @Override
    @Transactional
    public int auditReview(Long id, String status)
    {
        // 先获取评价对应的社区ID
        CoreCommunityReview review = reviewMapper.selectReviewById(id);
        Long communityId = review != null ? review.getCommunityId() : null;
        
        CoreCommunityReview updateReview = new CoreCommunityReview();
        updateReview.setId(id);
        updateReview.setStatus(status);
        
        int result = reviewMapper.updateReview(updateReview);
        // 更新社区评价统计
        if (result > 0 && communityId != null)
        {
            reviewMapper.updateCommunityReviewStats(communityId);
        }
        return result;
    }
}
