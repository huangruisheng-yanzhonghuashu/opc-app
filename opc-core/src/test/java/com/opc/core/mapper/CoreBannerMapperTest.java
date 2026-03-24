package com.opc.core.mapper;

import com.opc.core.domain.CoreBanner;
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
public class CoreBannerMapperTest
{
    @Mock
    private CoreBannerMapper bannerMapper;

    @Test
    public void testSelectBannerList()
    {
        CoreBanner banner = new CoreBanner();
        banner.setTitle("Test Banner");

        CoreBanner resultBanner = new CoreBanner();
        resultBanner.setId(1L);
        resultBanner.setTitle("Test Banner");

        when(bannerMapper.selectBannerList(any(CoreBanner.class))).thenReturn(Arrays.asList(resultBanner));

        List<CoreBanner> list = bannerMapper.selectBannerList(banner);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Banner", list.get(0).getTitle());
        verify(bannerMapper, times(1)).selectBannerList(banner);
    }

    @Test
    public void testSelectBannerById()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setTitle("Test Banner");

        when(bannerMapper.selectBannerById(1L)).thenReturn(banner);

        CoreBanner result = bannerMapper.selectBannerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Banner", result.getTitle());
        verify(bannerMapper, times(1)).selectBannerById(1L);
    }

    @Test
    public void testInsertBanner()
    {
        CoreBanner banner = new CoreBanner();
        banner.setTitle("New Banner");
        banner.setImageUrl("https://example.com/banner.jpg");

        when(bannerMapper.insertBanner(any(CoreBanner.class))).thenReturn(1);

        int result = bannerMapper.insertBanner(banner);

        assertEquals(1, result);
        verify(bannerMapper, times(1)).insertBanner(banner);
    }

    @Test
    public void testUpdateBanner()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setTitle("Updated Banner");

        when(bannerMapper.updateBanner(any(CoreBanner.class))).thenReturn(1);

        int result = bannerMapper.updateBanner(banner);

        assertEquals(1, result);
        verify(bannerMapper, times(1)).updateBanner(banner);
    }

    @Test
    public void testDeleteBannerById()
    {
        when(bannerMapper.deleteBannerById(1L)).thenReturn(1);

        int result = bannerMapper.deleteBannerById(1L);

        assertEquals(1, result);
        verify(bannerMapper, times(1)).deleteBannerById(1L);
    }

    @Test
    public void testDeleteBannerByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(bannerMapper.deleteBannerByIds(ids)).thenReturn(3);

        int result = bannerMapper.deleteBannerByIds(ids);

        assertEquals(3, result);
        verify(bannerMapper, times(1)).deleteBannerByIds(ids);
    }

    @Test
    public void testChangeStatus()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setStatus("1");

        when(bannerMapper.changeStatus(any(CoreBanner.class))).thenReturn(1);

        int result = bannerMapper.changeStatus(banner);

        assertEquals(1, result);
        verify(bannerMapper, times(1)).changeStatus(banner);
    }
}
