package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreCommunityWantToGo;

public interface CoreCommunityWantToGoMapper
{
    /**
     * 查询记录
     *
     * @param id 记录ID
     * @return 记录
     */
    public CoreCommunityWantToGo selectById(Long id);

    /**
     * 查询列表
     *
     * @param record 记录
     * @return 列表
     */
    public List<CoreCommunityWantToGo> selectList(CoreCommunityWantToGo record);

    /**
     * 根据社区ID查询
     *
     * @param communityId 社区ID
     * @return 列表
     */
    public List<CoreCommunityWantToGo> selectByCommunityId(Long communityId);

    /**
     * 根据会员ID查询
     *
     * @param memberId 会员ID
     * @return 列表
     */
    public List<CoreCommunityWantToGo> selectByMemberId(Long memberId);

    /**
     * 查询会员对社区的记录
     *
     * @param communityId 社区ID
     * @param memberId 会员ID
     * @return 记录
     */
    public CoreCommunityWantToGo selectByCommunityAndMember(Long communityId, Long memberId);

    /**
     * 新增
     *
     * @param record 记录
     * @return 结果
     */
    public int insert(CoreCommunityWantToGo record);

    /**
     * 修改
     *
     * @param record 记录
     * @return 结果
     */
    public int update(CoreCommunityWantToGo record);

    /**
     * 删除
     *
     * @param id 记录ID
     * @return 结果
     */
    public int deleteById(Long id);

    /**
     * 批量删除
     *
     * @param ids 记录ID数组
     * @return 结果
     */
    public int deleteByIds(Long[] ids);

    /**
     * 取消
     *
     * @param id 记录ID
     * @return 结果
     */
    public int cancel(Long id);

    /**
     * 统计数量
     *
     * @param communityId 社区ID
     * @return 数量
     */
    public int countByCommunityId(Long communityId);

    /**
     * 更新社区想去数
     *
     * @param communityId 社区ID
     * @return 结果
     */
    public int updateCommunityWantToGoCount(Long communityId);

    /**
     * 根据会员ID和社区ID列表查询想去记录
     *
     * @param memberId 会员ID
     * @param communityIds 社区ID列表
     * @return 列表
     */
    public List<CoreCommunityWantToGo> selectByMemberAndCommunityIds(Long memberId, List<Long> communityIds);
}
