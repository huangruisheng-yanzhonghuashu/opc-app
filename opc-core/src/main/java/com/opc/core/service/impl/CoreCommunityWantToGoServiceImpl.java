package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreCommunityWantToGo;
import com.opc.core.mapper.CoreCommunityWantToGoMapper;
import com.opc.core.service.ICoreCommunityWantToGoService;

@Service
public class CoreCommunityWantToGoServiceImpl implements ICoreCommunityWantToGoService
{
    @Autowired
    private CoreCommunityWantToGoMapper mapper;

    @Override
    public CoreCommunityWantToGo selectById(Long id)
    {
        return mapper.selectById(id);
    }

    @Override
    public List<CoreCommunityWantToGo> selectList(CoreCommunityWantToGo record)
    {
        return mapper.selectList(record);
    }

    @Override
    public List<CoreCommunityWantToGo> selectByCommunityId(Long communityId)
    {
        return mapper.selectByCommunityId(communityId);
    }

    @Override
    public List<CoreCommunityWantToGo> selectByMemberId(Long memberId)
    {
        return mapper.selectByMemberId(memberId);
    }

    @Override
    public CoreCommunityWantToGo selectByCommunityAndMember(Long communityId, Long memberId)
    {
        return mapper.selectByCommunityAndMember(communityId, memberId);
    }

    @Override
    @Transactional
    public int insert(CoreCommunityWantToGo record)
    {
        int result = mapper.insert(record);
        if (result > 0 && record.getCommunityId() != null)
        {
            mapper.updateCommunityWantToGoCount(record.getCommunityId());
        }
        return result;
    }

    @Override
    @Transactional
    public int update(CoreCommunityWantToGo record)
    {
        int result = mapper.update(record);
        if (result > 0 && record.getCommunityId() != null)
        {
            mapper.updateCommunityWantToGoCount(record.getCommunityId());
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteById(Long id)
    {
        CoreCommunityWantToGo record = mapper.selectById(id);
        Long communityId = record != null ? record.getCommunityId() : null;
        int result = mapper.deleteById(id);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityWantToGoCount(communityId);
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
            CoreCommunityWantToGo record = mapper.selectById(ids[0]);
            if (record != null)
            {
                communityId = record.getCommunityId();
            }
        }
        int result = mapper.deleteByIds(ids);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityWantToGoCount(communityId);
        }
        return result;
    }

    @Override
    @Transactional
    public int cancel(Long id)
    {
        CoreCommunityWantToGo record = mapper.selectById(id);
        Long communityId = record != null ? record.getCommunityId() : null;
        int result = mapper.cancel(id);
        if (result > 0 && communityId != null)
        {
            mapper.updateCommunityWantToGoCount(communityId);
        }
        return result;
    }

    @Override
    public int countByCommunityId(Long communityId)
    {
        return mapper.countByCommunityId(communityId);
    }

    @Override
    public int updateCommunityWantToGoCount(Long communityId)
    {
        return mapper.updateCommunityWantToGoCount(communityId);
    }

    @Override
    @Transactional
    public int markWantToGo(Long communityId, Long memberId)
    {
        CoreCommunityWantToGo existing = mapper.selectByCommunityAndMember(communityId, memberId);
        if (existing != null)
        {
            if ("0".equals(existing.getStatus()))
            {
                return 1;
            }
            existing.setStatus("0");
            return update(existing);
        }
        CoreCommunityWantToGo record = new CoreCommunityWantToGo();
        record.setCommunityId(communityId);
        record.setMemberId(memberId);
        record.setStatus("0");
        return insert(record);
    }

    @Override
    @Transactional
    public int unmarkWantToGo(Long communityId, Long memberId)
    {
        CoreCommunityWantToGo existing = mapper.selectByCommunityAndMember(communityId, memberId);
        if (existing == null || "1".equals(existing.getStatus()))
        {
            return 1;
        }
        return cancel(existing.getId());
    }
}
