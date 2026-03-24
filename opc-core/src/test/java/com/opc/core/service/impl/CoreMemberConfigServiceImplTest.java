package com.opc.core.service.impl;

import com.opc.core.domain.CoreMemberConfig;
import com.opc.core.mapper.CoreMemberConfigMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreMemberConfigServiceImplTest
{
    @Mock
    private CoreMemberConfigMapper configMapper;

    @InjectMocks
    private CoreMemberConfigServiceImpl configService;

    @Test
    public void testSelectConfigList()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("banner");

        CoreMemberConfig resultConfig = new CoreMemberConfig();
        resultConfig.setId(1L);
        resultConfig.setConfigType("banner");

        when(configMapper.selectConfigList(any(CoreMemberConfig.class))).thenReturn(Arrays.asList(resultConfig));

        List<CoreMemberConfig> list = configService.selectConfigList(config);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("banner", list.get(0).getConfigType());
    }

    @Test
    public void testSelectConfigById()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("banner");

        when(configMapper.selectConfigById(1L)).thenReturn(config);

        CoreMemberConfig result = configService.selectConfigById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("banner", result.getConfigType());
    }

    @Test
    public void testSelectConfigByType()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("vip_guide");

        when(configMapper.selectConfigByType("vip_guide")).thenReturn(Arrays.asList(config));

        List<CoreMemberConfig> list = configService.selectConfigByType("vip_guide");

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("vip_guide", list.get(0).getConfigType());
    }

    @Test
    public void testSaveConfig_Insert()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("banner");
        config.setImageUrl("https://example.com/banner.jpg");

        when(configMapper.insertConfig(any(CoreMemberConfig.class))).thenReturn(1);

        int result = configService.saveConfig(config);

        assertEquals(1, result);
        verify(configMapper).insertConfig(config);
    }

    @Test
    public void testSaveConfig_Update()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("banner");
        config.setUpdateBy("admin");

        when(configMapper.updateConfig(any(CoreMemberConfig.class))).thenReturn(1);

        int result = configService.saveConfig(config);

        assertEquals(1, result);
        verify(configMapper).updateConfig(config);
    }

    @Test
    public void testDeleteConfig()
    {
        when(configMapper.deleteConfigById(1L)).thenReturn(1);

        int result = configService.deleteConfig(1L);

        assertEquals(1, result);
    }
}
