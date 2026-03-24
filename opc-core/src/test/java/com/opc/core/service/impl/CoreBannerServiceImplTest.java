package com.opc.core.service.impl;

import com.opc.core.domain.CoreBanner;
import com.opc.core.mapper.CoreBannerMapper;
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
public class CoreBannerServiceImplTest
{
    @Mock
    private CoreBannerMapper bannerMapper;

    @InjectMocks
    private CoreBannerServiceImpl bannerService;

    @Test
    public void testSelectBannerList()
    {
        CoreBanner banner = new CoreBanner();
        banner.setTitle("Test Banner");

        CoreBanner resultBanner = new CoreBanner();
        resultBanner.setId(1L);
        resultBanner.setTitle("Test Banner");

        when(bannerMapper.selectBannerList(any(CoreBanner.class))).thenReturn(Arrays.asList(resultBanner));

        List<CoreBanner> list = bannerService.selectBannerList(banner);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Banner", list.get(0).getTitle());
    }

    @Test
    public void testSelectBannerById()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setTitle("Test Banner");

        when(bannerMapper.selectBannerById(1L)).thenReturn(banner);

        CoreBanner result = bannerService.selectBannerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Banner", result.getTitle());
    }

    @Test
    public void testInsertBanner()
    {
        CoreBanner banner = new CoreBanner();
        banner.setTitle("New Banner");
        banner.setImageUrl("https://example.com/banner.jpg");

        when(bannerMapper.insertBanner(any(CoreBanner.class))).thenReturn(1);

        int result = bannerService.insertBanner(banner);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateBanner()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setTitle("Updated Banner");

        when(bannerMapper.updateBanner(any(CoreBanner.class))).thenReturn(1);

        int result = bannerService.updateBanner(banner);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteBannerById()
    {
        when(bannerMapper.deleteBannerById(1L)).thenReturn(1);

        int result = bannerService.deleteBannerById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteBannerByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(bannerMapper.deleteBannerByIds(ids)).thenReturn(3);

        int result = bannerService.deleteBannerByIds(ids);

        assertEquals(3, result);
    }

    @Test
    public void testChangeStatus()
    {
        when(bannerMapper.changeStatus(any(CoreBanner.class))).thenReturn(1);

        int result = bannerService.changeStatus(1L, "1");

        assertEquals(1, result);
        verify(bannerMapper).changeStatus(argThat(banner ->
            banner.getId().equals(1L) && banner.getStatus().equals("1")
        ));
    }
}
