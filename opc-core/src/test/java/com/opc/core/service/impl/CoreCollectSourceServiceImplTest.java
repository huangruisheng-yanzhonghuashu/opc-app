package com.opc.core.service.impl;

import com.opc.core.domain.CoreCollectSource;
import com.opc.core.mapper.CoreCollectSourceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreCollectSourceServiceImplTest
{
    @Mock
    private CoreCollectSourceMapper collectSourceMapper;

    @InjectMocks
    private CoreCollectSourceServiceImpl collectSourceService;

    @Test
    public void testSelectCollectSourceList()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setKeyword("Twitter");

        CoreCollectSource resultSource = new CoreCollectSource();
        resultSource.setId(1L);
        resultSource.setKeyword("Twitter");

        when(collectSourceMapper.selectCollectSourceList(any(CoreCollectSource.class))).thenReturn(Arrays.asList(resultSource));

        List<CoreCollectSource> list = collectSourceService.selectCollectSourceList(source);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Twitter", list.get(0).getKeyword());
    }

    @Test
    public void testSelectCollectSourceById()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setKeyword("Twitter");

        when(collectSourceMapper.selectCollectSourceById(1L)).thenReturn(source);

        CoreCollectSource result = collectSourceService.selectCollectSourceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Twitter", result.getKeyword());
    }

    @Test
    public void testInsertCollectSource()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setKeyword("New Source");
        source.setSourceUrl("https://example.com");

        when(collectSourceMapper.insertCollectSource(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceService.insertCollectSource(source);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateCollectSource()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setKeyword("Updated Source");

        when(collectSourceMapper.updateCollectSource(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceService.updateCollectSource(source);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteCollectSourceById()
    {
        when(collectSourceMapper.deleteCollectSourceById(1L)).thenReturn(1);

        int result = collectSourceService.deleteCollectSourceById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteCollectSourceByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(collectSourceMapper.deleteCollectSourceByIds(ids)).thenReturn(3);

        int result = collectSourceService.deleteCollectSourceByIds(ids);

        assertEquals(3, result);
    }

    @Test
    public void testChangeStatus()
    {
        when(collectSourceMapper.changeStatus(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceService.changeStatus(1L, "1");

        assertEquals(1, result);
        verify(collectSourceMapper).changeStatus(argThat(source ->
            source.getId().equals(1L) && source.getStatus().equals("1")
        ));
    }
}
