package com.opc.core.mapper;

import com.opc.core.domain.CoreCollectSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreCollectSourceMapperTest
{
    @Mock
    private CoreCollectSourceMapper collectSourceMapper;

    @Test
    public void testSelectCollectSourceList()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setKeyword("Twitter");

        CoreCollectSource resultSource = new CoreCollectSource();
        resultSource.setId(1L);
        resultSource.setKeyword("Twitter");

        when(collectSourceMapper.selectCollectSourceList(any(CoreCollectSource.class))).thenReturn(Arrays.asList(resultSource));

        List<CoreCollectSource> list = collectSourceMapper.selectCollectSourceList(source);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Twitter", list.get(0).getKeyword());
        verify(collectSourceMapper, times(1)).selectCollectSourceList(source);
    }

    @Test
    public void testSelectCollectSourceById()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setKeyword("Twitter");

        when(collectSourceMapper.selectCollectSourceById(1L)).thenReturn(source);

        CoreCollectSource result = collectSourceMapper.selectCollectSourceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Twitter", result.getKeyword());
        verify(collectSourceMapper, times(1)).selectCollectSourceById(1L);
    }

    @Test
    public void testInsertCollectSource()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setKeyword("New Source");
        source.setSourceUrl("https://example.com");

        when(collectSourceMapper.insertCollectSource(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceMapper.insertCollectSource(source);

        assertEquals(1, result);
        verify(collectSourceMapper, times(1)).insertCollectSource(source);
    }

    @Test
    public void testUpdateCollectSource()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setKeyword("Updated Source");

        when(collectSourceMapper.updateCollectSource(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceMapper.updateCollectSource(source);

        assertEquals(1, result);
        verify(collectSourceMapper, times(1)).updateCollectSource(source);
    }

    @Test
    public void testDeleteCollectSourceById()
    {
        when(collectSourceMapper.deleteCollectSourceById(1L)).thenReturn(1);

        int result = collectSourceMapper.deleteCollectSourceById(1L);

        assertEquals(1, result);
        verify(collectSourceMapper, times(1)).deleteCollectSourceById(1L);
    }

    @Test
    public void testDeleteCollectSourceByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(collectSourceMapper.deleteCollectSourceByIds(ids)).thenReturn(3);

        int result = collectSourceMapper.deleteCollectSourceByIds(ids);

        assertEquals(3, result);
        verify(collectSourceMapper, times(1)).deleteCollectSourceByIds(ids);
    }

    @Test
    public void testChangeStatus()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setStatus("1");

        when(collectSourceMapper.changeStatus(any(CoreCollectSource.class))).thenReturn(1);

        int result = collectSourceMapper.changeStatus(source);

        assertEquals(1, result);
        verify(collectSourceMapper, times(1)).changeStatus(source);
    }
}
