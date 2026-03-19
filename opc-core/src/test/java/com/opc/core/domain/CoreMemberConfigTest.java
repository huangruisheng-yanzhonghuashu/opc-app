package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreMemberConfigTest
{
    @Test
    public void testCoreMemberConfigGettersAndSetters()
    {
        CoreMemberConfig config = new CoreMemberConfig();

        config.setId(1L);
        config.setConfigType("banner");
        config.setImageUrl("https://example.com/banner.jpg");
        config.setArticleLink("article_123");
        config.setRichContent("<p>Rich content</p>");
        config.setStatus("0");

        assertEquals(1L, config.getId());
        assertEquals("banner", config.getConfigType());
        assertEquals("https://example.com/banner.jpg", config.getImageUrl());
        assertEquals("article_123", config.getArticleLink());
        assertEquals("<p>Rich content</p>", config.getRichContent());
        assertEquals("0", config.getStatus());
    }

    @Test
    public void testCoreMemberConfigToString()
    {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setId(1L);
        config.setConfigType("banner");

        String str = config.toString();
        assertNotNull(str);
        assertTrue(str.contains("banner"));
    }

    @Test
    public void testCoreMemberConfigDefaultValues()
    {
        CoreMemberConfig config = new CoreMemberConfig();

        assertNull(config.getId());
        assertNull(config.getConfigType());
        assertNull(config.getImageUrl());
    }

    @Test
    public void testCoreMemberConfigStatusValues()
    {
        CoreMemberConfig config = new CoreMemberConfig();

        config.setStatus("0");
        assertEquals("0", config.getStatus());

        config.setStatus("1");
        assertEquals("1", config.getStatus());
    }

    @Test
    public void testCoreMemberConfigTypeValues()
    {
        CoreMemberConfig config = new CoreMemberConfig();

        config.setConfigType("banner");
        assertEquals("banner", config.getConfigType());

        config.setConfigType("vip_guide");
        assertEquals("vip_guide", config.getConfigType());
    }
}
