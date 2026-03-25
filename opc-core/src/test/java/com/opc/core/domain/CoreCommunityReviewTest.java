package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CoreCommunityReviewTest
{
    @Test
    public void testCoreCommunityReviewGettersAndSetters()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        review.setId(1L);
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("4.5"));
        review.setContent("这是一个很好的社区，环境优美！");
        review.setImages("[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]");
        review.setStatus("0");
        review.setLikeCount(50);

        assertEquals(Long.valueOf(1L), review.getId());
        assertEquals(Long.valueOf(100L), review.getCommunityId());
        assertEquals(Long.valueOf(200L), review.getMemberId());
        assertEquals(new BigDecimal("4.5"), review.getRating());
        assertEquals("这是一个很好的社区，环境优美！", review.getContent());
        assertEquals("[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]", review.getImages());
        assertEquals("0", review.getStatus());
        assertEquals(Integer.valueOf(50), review.getLikeCount());
    }

    @Test
    public void testCoreCommunityReviewToString()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("4.5"));
        review.setStatus("0");

        String str = review.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("4.5"));
        assertTrue(str.contains("0"));
    }

    @Test
    public void testCoreCommunityReviewDefaultValues()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        assertNull(review.getId());
        assertNull(review.getCommunityId());
        assertNull(review.getMemberId());
        assertNull(review.getRating());
        assertNull(review.getContent());
        assertNull(review.getImages());
        assertNull(review.getStatus());
        assertNull(review.getLikeCount());
    }

    @Test
    public void testCoreCommunityReviewStatusValues()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        review.setStatus("0");
        assertEquals("0", review.getStatus());

        review.setStatus("1");
        assertEquals("1", review.getStatus());

        review.setStatus("2");
        assertEquals("2", review.getStatus());
    }

    @Test
    public void testCoreCommunityReviewRatingValues()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        review.setRating(new BigDecimal("5.0"));
        assertEquals(new BigDecimal("5.0"), review.getRating());

        review.setRating(new BigDecimal("4.5"));
        assertEquals(new BigDecimal("4.5"), review.getRating());

        review.setRating(new BigDecimal("3.0"));
        assertEquals(new BigDecimal("3.0"), review.getRating());

        review.setRating(new BigDecimal("0.0"));
        assertEquals(new BigDecimal("0.0"), review.getRating());
    }

    @Test
    public void testCoreCommunityReviewLikeCount()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        review.setLikeCount(0);
        assertEquals(Integer.valueOf(0), review.getLikeCount());

        review.setLikeCount(100);
        assertEquals(Integer.valueOf(100), review.getLikeCount());

        review.setLikeCount(9999);
        assertEquals(Integer.valueOf(9999), review.getLikeCount());
    }

    @Test
    public void testCoreCommunityReviewContent()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        review.setContent("社区环境很好，交通便利！");
        assertEquals("社区环境很好，交通便利！", review.getContent());

        review.setContent("");
        assertEquals("", review.getContent());

        review.setContent(null);
        assertNull(review.getContent());
    }

    @Test
    public void testCoreCommunityReviewImages()
    {
        CoreCommunityReview review = new CoreCommunityReview();

        String imagesJson = "[\"https://example.com/1.jpg\", \"https://example.com/2.jpg\"]";
        review.setImages(imagesJson);
        assertEquals(imagesJson, review.getImages());

        review.setImages("");
        assertEquals("", review.getImages());

        review.setImages(null);
        assertNull(review.getImages());
    }
}
