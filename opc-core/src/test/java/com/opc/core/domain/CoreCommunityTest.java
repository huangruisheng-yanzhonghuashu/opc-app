package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CoreCommunityTest
{
    @Test
    public void testCoreCommunityGettersAndSetters()
    {
        CoreCommunity community = new CoreCommunity();

        community.setId(1L);
        community.setName("测试社区");
        community.setImage("https://example.com/community.jpg");
        community.setAddress("北京市朝阳区测试街道1号");
        community.setLongitude(new BigDecimal("116.397428"));
        community.setLatitude(new BigDecimal("39.90923"));
        community.setDetails("这是一个测试社区的详情描述");
        community.setWantToGoCount(100);
        community.setVisitedCount(50);
        community.setReviewCount(20);
        community.setRating(new BigDecimal("4.5"));
        community.setStatus("0");
        community.setSortOrder(1);

        assertEquals(Long.valueOf(1L), community.getId());
        assertEquals("测试社区", community.getName());
        assertEquals("https://example.com/community.jpg", community.getImage());
        assertEquals("北京市朝阳区测试街道1号", community.getAddress());
        assertEquals(new BigDecimal("116.397428"), community.getLongitude());
        assertEquals(new BigDecimal("39.90923"), community.getLatitude());
        assertEquals("这是一个测试社区的详情描述", community.getDetails());
        assertEquals(Integer.valueOf(100), community.getWantToGoCount());
        assertEquals(Integer.valueOf(50), community.getVisitedCount());
        assertEquals(Integer.valueOf(20), community.getReviewCount());
        assertEquals(new BigDecimal("4.5"), community.getRating());
        assertEquals("0", community.getStatus());
        assertEquals(Integer.valueOf(1), community.getSortOrder());
    }

    @Test
    public void testCoreCommunityToString()
    {
        CoreCommunity community = new CoreCommunity();
        community.setId(1L);
        community.setName("测试社区");
        community.setAddress("北京市朝阳区测试街道1号");
        community.setStatus("0");

        String str = community.toString();
        assertNotNull(str);
        assertTrue(str.contains("测试社区"));
        assertTrue(str.contains("北京市朝阳区测试街道1号"));
        assertTrue(str.contains("0"));
    }

    @Test
    public void testCoreCommunityDefaultValues()
    {
        CoreCommunity community = new CoreCommunity();

        assertNull(community.getId());
        assertNull(community.getName());
        assertNull(community.getImage());
        assertNull(community.getAddress());
        assertNull(community.getLongitude());
        assertNull(community.getLatitude());
        assertNull(community.getDetails());
        assertNull(community.getWantToGoCount());
        assertNull(community.getVisitedCount());
        assertNull(community.getReviewCount());
        assertNull(community.getRating());
        assertNull(community.getStatus());
        assertNull(community.getSortOrder());
    }

    @Test
    public void testCoreCommunityStatusValues()
    {
        CoreCommunity community = new CoreCommunity();

        community.setStatus("0");
        assertEquals("0", community.getStatus());

        community.setStatus("1");
        assertEquals("1", community.getStatus());
    }

    @Test
    public void testCoreCommunityRatingValues()
    {
        CoreCommunity community = new CoreCommunity();

        community.setRating(new BigDecimal("5.0"));
        assertEquals(new BigDecimal("5.0"), community.getRating());

        community.setRating(new BigDecimal("4.5"));
        assertEquals(new BigDecimal("4.5"), community.getRating());

        community.setRating(new BigDecimal("3.0"));
        assertEquals(new BigDecimal("3.0"), community.getRating());

        community.setRating(new BigDecimal("0.0"));
        assertEquals(new BigDecimal("0.0"), community.getRating());
    }

    @Test
    public void testCoreCommunityCountFields()
    {
        CoreCommunity community = new CoreCommunity();

        community.setWantToGoCount(0);
        assertEquals(Integer.valueOf(0), community.getWantToGoCount());

        community.setWantToGoCount(9999);
        assertEquals(Integer.valueOf(9999), community.getWantToGoCount());

        community.setVisitedCount(0);
        assertEquals(Integer.valueOf(0), community.getVisitedCount());

        community.setVisitedCount(9999);
        assertEquals(Integer.valueOf(9999), community.getVisitedCount());

        community.setReviewCount(0);
        assertEquals(Integer.valueOf(0), community.getReviewCount());

        community.setReviewCount(9999);
        assertEquals(Integer.valueOf(9999), community.getReviewCount());
    }

    @Test
    public void testCoreCommunitySortOrder()
    {
        CoreCommunity community = new CoreCommunity();

        community.setSortOrder(0);
        assertEquals(Integer.valueOf(0), community.getSortOrder());

        community.setSortOrder(1);
        assertEquals(Integer.valueOf(1), community.getSortOrder());

        community.setSortOrder(999);
        assertEquals(Integer.valueOf(999), community.getSortOrder());
    }

    @Test
    public void testCoreCommunityCoordinates()
    {
        CoreCommunity community = new CoreCommunity();

        community.setLongitude(new BigDecimal("116.397428"));
        community.setLatitude(new BigDecimal("39.90923"));
        assertEquals(new BigDecimal("116.397428"), community.getLongitude());
        assertEquals(new BigDecimal("39.90923"), community.getLatitude());

        community.setLongitude(new BigDecimal("-122.419416"));
        community.setLatitude(new BigDecimal("37.774929"));
        assertEquals(new BigDecimal("-122.419416"), community.getLongitude());
        assertEquals(new BigDecimal("37.774929"), community.getLatitude());
    }
}
