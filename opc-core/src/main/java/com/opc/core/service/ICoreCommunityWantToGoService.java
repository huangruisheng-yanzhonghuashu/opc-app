package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreCommunityWantToGo;

public interface ICoreCommunityWantToGoService
{
    CoreCommunityWantToGo selectById(Long id);
    List<CoreCommunityWantToGo> selectList(CoreCommunityWantToGo record);
    List<CoreCommunityWantToGo> selectByCommunityId(Long communityId);
    List<CoreCommunityWantToGo> selectByMemberId(Long memberId);
    CoreCommunityWantToGo selectByCommunityAndMember(Long communityId, Long memberId);
    int insert(CoreCommunityWantToGo record);
    int update(CoreCommunityWantToGo record);
    int deleteById(Long id);
    int deleteByIds(Long[] ids);
    int cancel(Long id);
    int countByCommunityId(Long communityId);
    int updateCommunityWantToGoCount(Long communityId);
    int markWantToGo(Long communityId, Long memberId);
    int unmarkWantToGo(Long communityId, Long memberId);

    /**
     * 根据会员ID和社区ID列表查询想去记录
     *
     * @param memberId 会员ID
     * @param communityIds 社区ID列表
     * @return 列表
     */
    List<CoreCommunityWantToGo> selectByMemberAndCommunityIds(Long memberId, List<Long> communityIds);
}
