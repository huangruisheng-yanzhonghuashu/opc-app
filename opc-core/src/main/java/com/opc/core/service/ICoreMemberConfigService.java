package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMemberConfig;

public interface ICoreMemberConfigService
{
    public List<CoreMemberConfig> selectConfigList(CoreMemberConfig config);

    public CoreMemberConfig selectConfigById(Long id);

    public CoreMemberConfig selectConfigByType(String configType);

    public int saveConfig(CoreMemberConfig config);
}
