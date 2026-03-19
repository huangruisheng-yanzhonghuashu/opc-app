package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreBannerTest
{
    @Test
    public void testCoreBannerGettersAndSetters()
    {
        CoreBanner banner = new CoreBanner();

        banner.setId(1L);
        banner.setTitle("Banner Title");
        banner.setImageUrl("https://example.com/banner.jpg");
        banner.setLinkType("1");
        banner.setLinkValue("article_123");
        banner.setSortOrder(1);
        banner.setStatus("0");

        assertEquals(1L, banner.getId());
        assertEquals("Banner Title", banner.getTitle());
        assertEquals("https://example.com/banner.jpg", banner.getImageUrl());
        assertEquals("1", banner.getLinkType());
        assertEquals("article_123", banner.getLinkValue());
        assertEquals(Integer.valueOf(1), banner.getSortOrder());
        assertEquals("0", banner.getStatus());
    }

    @Test
    public void testCoreBannerToString()
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(1L);
        banner.setTitle("Banner Title");

        String str = banner.toString();
        assertNotNull(str);
        assertTrue(str.contains("Banner Title"));
    }

    @Test
    public void testCoreBannerDefaultValues()
    {
        CoreBanner banner = new CoreBanner();

        assertNull(banner.getId());
        assertNull(banner.getTitle());
        assertNull(banner.getImageUrl());
        assertNull(banner.getSortOrder());
    }

    @Test
    public void testCoreBannerStatusValues()
    {
        CoreBanner banner = new CoreBanner();

        banner.setStatus("0");
        assertEquals("0", banner.getStatus());

        banner.setStatus("1");
        assertEquals("1", banner.getStatus());
    }

    @Test
    public void testCoreBannerLinkTypeValues()
    {
        CoreBanner banner = new CoreBanner();

        banner.setLinkType("1");
        assertEquals("1", banner.getLinkType());

        banner.setLinkType("2");
        assertEquals("2", banner.getLinkType());
    }
}
