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
}
