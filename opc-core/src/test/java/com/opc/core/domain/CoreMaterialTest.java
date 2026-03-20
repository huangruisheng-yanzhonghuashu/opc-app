package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class CoreMaterialTest
{
    @Test
    public void testCoreMaterialGettersAndSetters()
    {
        CoreMaterial material = new CoreMaterial();

        material.setId(1L);
        material.setTitle("Test Article");
        material.setAuthor("John Doe");
        material.setSummary("This is a summary");
        material.setContent("Full content here");
        material.setOriginalUrl("https://original.com/article");
        material.setOriginalId("orig_123");
        material.setReplyCount(10L);
        material.setLikeCount(100L);
        material.setViewCount(1000L);
        material.setShareCount(50L);
        material.setCommentCount(20L);
        material.setPublishTime(Instant.parse("2024-01-01T10:00:00Z"));
        material.setViewPermission(1);
        material.setContentType("article");
        material.setCategory("tech");
        material.setStatus("0");
        material.setIsTop("1");
        material.setSource("twitter");

        assertEquals(1L, material.getId());
        assertEquals("Test Article", material.getTitle());
        assertEquals("John Doe", material.getAuthor());
        assertEquals("This is a summary", material.getSummary());
        assertEquals("Full content here", material.getContent());
        assertEquals("https://original.com/article", material.getOriginalUrl());
        assertEquals("orig_123", material.getOriginalId());
        assertEquals(Long.valueOf(10), material.getReplyCount());
        assertEquals(Long.valueOf(100), material.getLikeCount());
        assertEquals(Long.valueOf(1000), material.getViewCount());
        assertEquals(Long.valueOf(50), material.getShareCount());
        assertEquals(Long.valueOf(20), material.getCommentCount());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"), material.getPublishTime());
        assertEquals(Integer.valueOf(1), material.getViewPermission());
        assertEquals("article", material.getContentType());
        assertEquals("tech", material.getCategory());
        assertEquals("0", material.getStatus());
        assertEquals("1", material.getIsTop());
        assertEquals("twitter", material.getSource());
    }

    @Test
    public void testCoreMaterialToString()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Test Article");

        String str = material.toString();
        assertNotNull(str);
        assertTrue(str.contains("Test Article"));
    }

    @Test
    public void testCoreMaterialDefaultValues()
    {
        CoreMaterial material = new CoreMaterial();

        assertNull(material.getId());
        assertNull(material.getTitle());
        assertNull(material.getAuthor());
        assertNull(material.getReplyCount());
    }

    @Test
    public void testCoreMaterialStatusValues()
    {
        CoreMaterial material = new CoreMaterial();

        material.setStatus("0");
        assertEquals("0", material.getStatus());

        material.setStatus("1");
        assertEquals("1", material.getStatus());
    }

    @Test
    public void testCoreMaterialIsTopValues()
    {
        CoreMaterial material = new CoreMaterial();

        material.setIsTop("0");
        assertEquals("0", material.getIsTop());

        material.setIsTop("1");
        assertEquals("1", material.getIsTop());
    }

    @Test
    public void testCoreMaterialViewPermissionValues()
    {
        CoreMaterial material = new CoreMaterial();

        material.setViewPermission(0);
        assertEquals(Integer.valueOf(0), material.getViewPermission());

        material.setViewPermission(1);
        assertEquals(Integer.valueOf(1), material.getViewPermission());
    }
}
