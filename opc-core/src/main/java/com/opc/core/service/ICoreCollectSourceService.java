package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreCollectSource;

public interface ICoreCollectSourceService
{
    public List<CoreCollectSource> selectCollectSourceList(CoreCollectSource collectSource);

    public CoreCollectSource selectCollectSourceById(Long id);

    public int insertCollectSource(CoreCollectSource collectSource);

    public int updateCollectSource(CoreCollectSource collectSource);

    public int deleteCollectSourceById(Long id);

    public int deleteCollectSourceByIds(Long[] ids);

    public int changeStatus(Long id, String status);
}
