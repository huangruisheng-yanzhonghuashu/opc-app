package com.opc.core.mapper;

import java.util.List;
import java.util.Map;
import com.opc.core.domain.CoreCommunityReview;

public interface CoreCommunityReviewMapper
{
    /**
     * 查询评价信息
     *
     * @param id 评价ID
     * @return 评价信息
     */
    public CoreCommunityReview selectReviewById(Long id);

    /**
     * 查询评价列表
     *
     * @param review 评价信息
     * @return 评价集合
     */
    public List<CoreCommunityReview> selectReviewList(CoreCommunityReview review);

    /**
     * 根据社区ID查询评价列表
     *
     * @param communityId 社区ID
     * @return 评价集合
     */
    public List<CoreCommunityReview> selectReviewsByCommunityId(Long communityId);

    /**
     * 新增评价
     *
     * @param review 评价信息
     * @return 结果
     */
    public int insertReview(CoreCommunityReview review);

    /**
     * 修改评价
     *
     * @param review 评价信息
     * @return 结果
     */
    public int updateReview(CoreCommunityReview review);

    /**
     * 删除评价
     *
     * @param id 评价ID
     * @return 结果
     */
    public int deleteReviewById(Long id);

    /**
     * 批量删除评价
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteReviewByIds(Long[] ids);

    /**
     * 根据社区ID统计评价数量和平均星级
     *
     * @param communityId 社区ID
     * @return 统计结果
     */
    public Map<String, Object> getReviewStatsByCommunityId(Long communityId);

    /**
     * 更新社区评价统计
     *
     * @param communityId 社区ID
     * @return 结果
     */
    public int updateCommunityReviewStats(Long communityId);
}
