package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreMemberConfig;
import com.opc.core.mapper.CoreMemberConfigMapper;
import com.opc.core.service.ICoreMemberConfigService;

@Service
public class CoreMemberConfigServiceImpl implements ICoreMemberConfigService
{
    @Autowired
    private CoreMemberConfigMapper configMapper;

    @Override
    public List<CoreMemberConfig> selectConfigList(CoreMemberConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    @Override
    public CoreMemberConfig selectConfigById(Long id)
    {
        return configMapper.selectConfigById(id);
    }

    @Override
    public List<CoreMemberConfig> selectConfigByType(String configType)
    {
        return configMapper.selectConfigByType(configType);
    }

    @Override
    public int saveConfig(CoreMemberConfig config)
    {
        if (config.getId() != null) {
            config.setUpdateBy(config.getUpdateBy());
            return configMapper.updateConfig(config);
        } else {
            return configMapper.insertConfig(config);
        }
    }

    @Override
    public int deleteConfig(Long id)
    {
        return configMapper.deleteConfigById(id);
    }
}
