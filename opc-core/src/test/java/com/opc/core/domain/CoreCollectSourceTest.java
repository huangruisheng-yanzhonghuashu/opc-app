package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreCollectSourceTest
{
    @Test
    public void testCoreCollectSourceGettersAndSetters()
    {
        CoreCollectSource source = new CoreCollectSource();

        source.setId(1L);
        source.setKeyword("Twitter Trends");
        source.setSourceUrl("https://twitter.com/trends");
        source.setSourceType("twitter");
        source.setStatus("0");

        assertEquals(1L, source.getId());
        assertEquals("Twitter Trends", source.getKeyword());
        assertEquals("https://twitter.com/trends", source.getSourceUrl());
        assertEquals("twitter", source.getSourceType());
        assertEquals("0", source.getStatus());
    }

    @Test
    public void testCoreCollectSourceToString()
    {
        CoreCollectSource source = new CoreCollectSource();
        source.setId(1L);
        source.setKeyword("Twitter Trends");

        String str = source.toString();
        assertNotNull(str);
        assertTrue(str.contains("Twitter Trends"));
    }

    @Test
    public void testCoreCollectSourceDefaultValues()
    {
        CoreCollectSource source = new CoreCollectSource();

        assertNull(source.getId());
        assertNull(source.getKeyword());
        assertNull(source.getSourceUrl());
    }

    @Test
    public void testCoreCollectSourceStatusValues()
    {
        CoreCollectSource source = new CoreCollectSource();

        source.setStatus("0");
        assertEquals("0", source.getStatus());

        source.setStatus("1");
        assertEquals("1", source.getStatus());
    }
}
