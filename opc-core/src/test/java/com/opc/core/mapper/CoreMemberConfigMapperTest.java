package com.opc.core.mapper;

import com.opc.core.domain.CoreMemberConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreMemberConfigMapperTest
{
    @Mock
    private CoreMemberConfigMapper configMapper;

    @Test
    public void testSelectConfigList()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("banner");

        CoreMemberConfig resultConfig = new CoreMemberConfig();
        resultConfig.setId(1L);
        resultConfig.setConfigType("banner");

        when(configMapper.selectConfigList(any(CoreMemberConfig.class))).thenReturn(Arrays.asList(resultConfig));

        List<CoreMemberConfig> list = configMapper.selectConfigList(config);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("banner", list.get(0).getConfigType());
        verify(configMapper, times(1)).selectConfigList(config);
    }

    @Test
    public void testSelectConfigById()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("banner");

        when(configMapper.selectConfigById(1L)).thenReturn(config);

        CoreMemberConfig result = configMapper.selectConfigById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("banner", result.getConfigType());
        verify(configMapper, times(1)).selectConfigById(1L);
    }

    @Test
    public void testSelectConfigByType()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("vip_guide");

        when(configMapper.selectConfigByType("vip_guide")).thenReturn(Arrays.asList(config));

        List<CoreMemberConfig> list = configMapper.selectConfigByType("vip_guide");

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("vip_guide", list.get(0).getConfigType());
        verify(configMapper, times(1)).selectConfigByType("vip_guide");
    }

    @Test
    public void testInsertConfig()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("banner");
        config.setImageUrl("https://example.com/banner.jpg");

        when(configMapper.insertConfig(any(CoreMemberConfig.class))).thenReturn(1);

        int result = configMapper.insertConfig(config);

        assertEquals(1, result);
        verify(configMapper, times(1)).insertConfig(config);
    }

    @Test
    public void testUpdateConfig()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("banner");

        when(configMapper.updateConfig(any(CoreMemberConfig.class))).thenReturn(1);

        int result = configMapper.updateConfig(config);

        assertEquals(1, result);
        verify(configMapper, times(1)).updateConfig(config);
    }

    @Test
    public void testDeleteConfigById()
    {
        when(configMapper.deleteConfigById(1L)).thenReturn(1);

        int result = configMapper.deleteConfigById(1L);

        assertEquals(1, result);
        verify(configMapper, times(1)).deleteConfigById(1L);
    }
}
