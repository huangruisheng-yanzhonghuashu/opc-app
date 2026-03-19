package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreCollectSource;
import com.opc.core.mapper.CoreCollectSourceMapper;
import com.opc.core.service.ICoreCollectSourceService;

@Service
public class CoreCollectSourceServiceImpl implements ICoreCollectSourceService
{
    @Autowired
    private CoreCollectSourceMapper collectSourceMapper;

    @Override
    public List<CoreCollectSource> selectCollectSourceList(CoreCollectSource collectSource)
    {
        return collectSourceMapper.selectCollectSourceList(collectSource);
    }

    @Override
    public CoreCollectSource selectCollectSourceById(Long id)
    {
        return collectSourceMapper.selectCollectSourceById(id);
    }

    @Override
    public int insertCollectSource(CoreCollectSource collectSource)
    {
        return collectSourceMapper.insertCollectSource(collectSource);
    }

    @Override
    public int updateCollectSource(CoreCollectSource collectSource)
    {
        return collectSourceMapper.updateCollectSource(collectSource);
    }

    @Override
    public int deleteCollectSourceById(Long id)
    {
        return collectSourceMapper.deleteCollectSourceById(id);
    }

    @Override
    public int deleteCollectSourceByIds(Long[] ids)
    {
        return collectSourceMapper.deleteCollectSourceByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreCollectSource collectSource = new CoreCollectSource();
        collectSource.setId(id);
        collectSource.setStatus(status);
        return collectSourceMapper.changeStatus(collectSource);
    }
}
