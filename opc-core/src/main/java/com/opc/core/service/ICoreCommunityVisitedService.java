package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreCommunityVisited;

public interface ICoreCommunityVisitedService
{
    CoreCommunityVisited selectById(Long id);
    List<CoreCommunityVisited> selectList(CoreCommunityVisited record);
    List<CoreCommunityVisited> selectByCommunityId(Long communityId);
    List<CoreCommunityVisited> selectByMemberId(Long memberId);
    CoreCommunityVisited selectByCommunityAndMember(Long communityId, Long memberId);
    int insert(CoreCommunityVisited record);
    int update(CoreCommunityVisited record);
    int deleteById(Long id);
    int deleteByIds(Long[] ids);
    int cancel(Long id);
    int countByCommunityId(Long communityId);
    int updateCommunityVisitedCount(Long communityId);
    int markVisited(Long communityId, Long memberId);
    int unmarkVisited(Long communityId, Long memberId);

    /**
     * 根据会员ID和社区ID列表查询去过记录
     *
     * @param memberId 会员ID
     * @param communityIds 社区ID列表
     * @return 列表
     */
    List<CoreCommunityVisited> selectByMemberAndCommunityIds(Long memberId, List<Long> communityIds);
}
