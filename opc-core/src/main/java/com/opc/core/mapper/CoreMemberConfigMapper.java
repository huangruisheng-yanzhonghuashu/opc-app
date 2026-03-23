package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreMemberConfig;

public interface CoreMemberConfigMapper
{
    public List<CoreMemberConfig> selectConfigList(CoreMemberConfig config);

    public CoreMemberConfig selectConfigById(Long id);

    public List<CoreMemberConfig> selectConfigByType(String configType);

    public int deleteConfigById(Long id);

    public int insertConfig(CoreMemberConfig config);

    public int updateConfig(CoreMemberConfig config);
}
