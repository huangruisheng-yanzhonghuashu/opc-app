package com.opc.system.service.impl;

import com.opc.common.constant.UserConstants;
import com.opc.common.core.redis.RedisCache;
import com.opc.system.domain.SysConfig;
import com.opc.system.mapper.SysConfigMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SysConfigServiceImplTest {

    @Mock
    private SysConfigMapper configMapper;

    @Mock
    private RedisCache redisCache;

    @InjectMocks
    private SysConfigServiceImpl configService;

    @Test
    public void testSelectConfigById() {
        SysConfig config = new SysConfig();
        config.setConfigId(1L);
        config.setConfigKey("sys.test.key");
        config.setConfigValue("test_value");

        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(config);

        SysConfig result = configService.selectConfigById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getConfigId());
        assertEquals("sys.test.key", result.getConfigKey());
    }

    @Test
    public void testSelectConfigByKeyFromCache() {
        when(redisCache.getCacheObject("sys_config:sys.test.key")).thenReturn("cached_value");

        String result = configService.selectConfigByKey("sys.test.key");

        assertEquals("cached_value", result);
        verify(configMapper, never()).selectConfig(any());
    }

    @Test
    public void testSelectConfigByKeyFromDb() {
        when(redisCache.getCacheObject(anyString())).thenReturn(null);

        SysConfig config = new SysConfig();
        config.setConfigKey("sys.test.key");
        config.setConfigValue("db_value");

        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(config);

        String result = configService.selectConfigByKey("sys.test.key");

        assertEquals("db_value", result);
        verify(redisCache).setCacheObject(anyString(), eq("db_value"));
    }

    @Test
    public void testSelectConfigByKeyNotFound() {
        when(redisCache.getCacheObject(anyString())).thenReturn(null);
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(null);

        String result = configService.selectConfigByKey("nonexistent.key");

        assertEquals("", result);
    }

    @Test
    public void testSelectCaptchaEnabledTrue() {
        when(redisCache.getCacheObject(anyString())).thenReturn("true");

        boolean result = configService.selectCaptchaEnabled();

        assertTrue(result);
    }

    @Test
    public void testSelectCaptchaEnabledFalse() {
        when(redisCache.getCacheObject(anyString())).thenReturn("false");

        boolean result = configService.selectCaptchaEnabled();

        assertFalse(result);
    }

    @Test
    public void testSelectCaptchaEnabledDefault() {
        when(redisCache.getCacheObject(anyString())).thenReturn(null);
        when(configMapper.selectConfig(any(SysConfig.class))).thenReturn(null);

        boolean result = configService.selectCaptchaEnabled();

        assertTrue(result);
    }

    @Test
    public void testSelectConfigList() {
        SysConfig config = new SysConfig();
        config.setConfigKey("sys");

        SysConfig resultConfig = new SysConfig();
        resultConfig.setConfigId(1L);
        resultConfig.setConfigKey("sys.test");

        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(Arrays.asList(resultConfig));

        List<SysConfig> list = configService.selectConfigList(config);

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testInsertConfig() {
        SysConfig config = new SysConfig();
        config.setConfigKey("sys.new.key");
        config.setConfigValue("new_value");

        when(configMapper.insertConfig(any(SysConfig.class))).thenReturn(1);

        int result = configService.insertConfig(config);

        assertEquals(1, result);
        verify(redisCache).setCacheObject(anyString(), eq("new_value"));
    }

    @Test
    public void testUpdateConfig() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(1L);
        existingConfig.setConfigKey("sys.old.key");
        existingConfig.setConfigValue("old_value");

        SysConfig newConfig = new SysConfig();
        newConfig.setConfigId(1L);
        newConfig.setConfigKey("sys.new.key");
        newConfig.setConfigValue("new_value");

        when(configMapper.selectConfigById(1L)).thenReturn(existingConfig);
        when(configMapper.updateConfig(any(SysConfig.class))).thenReturn(1);

        int result = configService.updateConfig(newConfig);

        assertEquals(1, result);
        verify(redisCache).deleteObject(anyString());
        verify(redisCache).setCacheObject(anyString(), eq("new_value"));
    }

    @Test
    public void testCheckConfigKeyUnique() {
        when(configMapper.checkConfigKeyUnique("sys.unique.key")).thenReturn(null);

        SysConfig config = new SysConfig();
        config.setConfigKey("sys.unique.key");

        boolean result = configService.checkConfigKeyUnique(config);

        assertEquals(UserConstants.UNIQUE, result);
    }

    @Test
    public void testCheckConfigKeyNotUnique() {
        SysConfig existingConfig = new SysConfig();
        existingConfig.setConfigId(2L);
        existingConfig.setConfigKey("sys.existing.key");

        when(configMapper.checkConfigKeyUnique("sys.existing.key")).thenReturn(existingConfig);

        SysConfig config = new SysConfig();
        config.setConfigId(1L);
        config.setConfigKey("sys.existing.key");

        boolean result = configService.checkConfigKeyUnique(config);

        assertEquals(UserConstants.NOT_UNIQUE, result);
    }

    @Test
    public void testLoadingConfigCache() {
        SysConfig config1 = new SysConfig();
        config1.setConfigKey("key1");
        config1.setConfigValue("value1");

        SysConfig config2 = new SysConfig();
        config2.setConfigKey("key2");
        config2.setConfigValue("value2");

        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(Arrays.asList(config1, config2));

        configService.loadingConfigCache();

        verify(redisCache, times(2)).setCacheObject(anyString(), anyString());
    }

    @Test
    public void testClearConfigCache() {
        when(redisCache.keys(anyString())).thenReturn(Arrays.asList("key1", "key2"));

        configService.clearConfigCache();

        verify(redisCache).deleteObject(anyList());
    }

    @Test
    public void testResetConfigCache() {
        when(redisCache.keys(anyString())).thenReturn(Arrays.asList());
        when(configMapper.selectConfigList(any(SysConfig.class))).thenReturn(Arrays.asList());

        configService.resetConfigCache();

        verify(redisCache).keys(anyString());
        verify(configMapper).selectConfigList(any(SysConfig.class));
    }
}
