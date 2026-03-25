package com.opc.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreCommunityVisited;
import com.opc.core.mapper.CoreCommunityVisitedMapper;
import com.opc.core.service.ICoreCommunityVisitedService;

@Service
public class CoreCommunityVisitedServiceImpl implements ICoreCommunityVisitedService
{
    @Autowired
    private CoreCommunityVisitedMapper mapper;

    @Override
    public CoreCommunityVisited selectById(Long id)
    {
        return mapper.selectById(id);
    }

    @Override
    public List<CoreCommunityVisited> selectList(CoreCommunityVisited record)
    {
        return mapper.selectList(record);
    }

    @Override
    public List<CoreCommunityVisited> selectByCommunityId(Long communityId)
    {
        return mapper.selectByCommunityId(communityId);
    }

    @Override
    public List<CoreCommunityVisited> selectByMemberId(Long memberId)
    {
        return mapper.selectByMemberId(memberId);
    }

    @Override
    public CoreCommunityVisited selectByCommunityAndMember(Long communityId, Long memberId)
    {
        return mapper.selectByCommunityAndMember(communityId, memberId);
    }

    @Override
    @Transactional
    public int insert(CoreCommunityVisited record)
    {
        int result = mapper.insert(record);
        if (result > 0 && record.getCommunityId() != null)
        {
            mapper.updateCommunityVisitedCount(record.getCommunityId());
        }
        return result;
    }

    @Override
    @Transactional
    public int update(CoreCommunityVisited record)
    {
        int result = mapper.update(record);
        if (result > 0 && record.getCommunityId() != null)
        {
            mapper.updateCommunityVisitedCount(record.getCommunityId());
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteById(Long id)
    {
        CoreCommunityVisited record = mapper.selectById(id);
        Long communityId = record != null ? record.getCommunityId() : null;
        int result = mapper.deleteById(id);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityVisitedCount(communityId);
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteByIds(Long[] ids)
    {
        Long communityId = null;
        if (ids != null && ids.length > 0)
        {
            CoreCommunityVisited record = mapper.selectById(ids[0]);
            if (record != null)
            {
                communityId = record.getCommunityId();
            }
        }
        int result = mapper.deleteByIds(ids);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityVisitedCount(communityId);
        }
        return result;
    }

    @Override
    @Transactional
    public int cancel(Long id)
    {
        CoreCommunityVisited record = mapper.selectById(id);
        Long communityId = record != null ? record.getCommunityId() : null;
        int result = mapper.cancel(id);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityVisitedCount(communityId);
        }
        return result;
    }

    @Override
    public int countByCommunityId(Long communityId)
    {
        return mapper.countByCommunityId(communityId);
    }

    @Override
    public int updateCommunityVisitedCount(Long communityId)
    {
        return mapper.updateCommunityVisitedCount(communityId);
    }

    @Override
    @Transactional
    public int markVisited(Long communityId, Long memberId)
    {
        CoreCommunityVisited existing = mapper.selectByCommunityAndMember(communityId, memberId);
        if (existing != null)
        {
            if ("0".equals(existing.getStatus()))
            {
                return 1;
            }
            existing.setStatus("0");
            existing.setVisitTime(LocalDateTime.now());
            return update(existing);
        }
        CoreCommunityVisited record = new CoreCommunityVisited();
        record.setCommunityId(communityId);
        record.setMemberId(memberId);
        record.setVisitTime(LocalDateTime.now());
        record.setStatus("0");
        return insert(record);
    }

    @Override
    @Transactional
    public int unmarkVisited(Long communityId, Long memberId)
    {
        CoreCommunityVisited existing = mapper.selectByCommunityAndMember(communityId, memberId);
        if (existing == null || "1".equals(existing.getStatus()))
        {
            return 1;
        }
        return cancel(existing.getId());
    }
}
