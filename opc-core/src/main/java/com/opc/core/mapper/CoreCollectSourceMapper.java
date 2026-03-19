package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreCollectSource;

public interface CoreCollectSourceMapper
{
    public List<CoreCollectSource> selectCollectSourceList(CoreCollectSource collectSource);

    public CoreCollectSource selectCollectSourceById(Long id);

    public int insertCollectSource(CoreCollectSource collectSource);

    public int updateCollectSource(CoreCollectSource collectSource);

    public int deleteCollectSourceById(Long id);

    public int deleteCollectSourceByIds(Long[] ids);

    public int changeStatus(CoreCollectSource collectSource);
}
