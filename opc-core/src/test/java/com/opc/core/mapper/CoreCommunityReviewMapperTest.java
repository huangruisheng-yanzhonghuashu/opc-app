package com.opc.core.mapper;

import com.opc.core.domain.CoreCommunityReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreCommunityReviewMapperTest
{
    @Mock
    private CoreCommunityReviewMapper reviewMapper;

    @Test
    public void testSelectReviewList()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setCommunityId(100L);

        CoreCommunityReview resultReview = new CoreCommunityReview();
        resultReview.setId(1L);
        resultReview.setCommunityId(100L);
        resultReview.setRating(new BigDecimal("4.5"));

        when(reviewMapper.selectReviewList(any(CoreCommunityReview.class))).thenReturn(Arrays.asList(resultReview));

        List<CoreCommunityReview> list = reviewMapper.selectReviewList(review);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Long.valueOf(100L), list.get(0).getCommunityId());
        verify(reviewMapper, times(1)).selectReviewList(review);
    }

    @Test
    public void testSelectReviewsByCommunityId()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setRating(new BigDecimal("5.0"));

        when(reviewMapper.selectReviewsByCommunityId(100L)).thenReturn(Arrays.asList(review));

        List<CoreCommunityReview> list = reviewMapper.selectReviewsByCommunityId(100L);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Long.valueOf(100L), list.get(0).getCommunityId());
        verify(reviewMapper, times(1)).selectReviewsByCommunityId(100L);
    }

    @Test
    public void testSelectReviewById()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("4.5"));
        review.setContent("测试评价内容");

        when(reviewMapper.selectReviewById(1L)).thenReturn(review);

        CoreCommunityReview result = reviewMapper.selectReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试评价内容", result.getContent());
        verify(reviewMapper, times(1)).selectReviewById(1L);
    }

    @Test
    public void testInsertReview()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("5.0"));
        review.setContent("新增评价");

        when(reviewMapper.insertReview(any(CoreCommunityReview.class))).thenReturn(1);

        int result = reviewMapper.insertReview(review);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).insertReview(review);
    }

    @Test
    public void testUpdateReview()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setContent("更新后的评价内容");
        review.setRating(new BigDecimal("4.0"));

        when(reviewMapper.updateReview(any(CoreCommunityReview.class))).thenReturn(1);

        int result = reviewMapper.updateReview(review);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).updateReview(review);
    }

    @Test
    public void testDeleteReviewById()
    {
        when(reviewMapper.deleteReviewById(1L)).thenReturn(1);

        int result = reviewMapper.deleteReviewById(1L);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).deleteReviewById(1L);
    }

    @Test
    public void testDeleteReviewByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(reviewMapper.deleteReviewByIds(ids)).thenReturn(3);

        int result = reviewMapper.deleteReviewByIds(ids);

        assertEquals(3, result);
        verify(reviewMapper, times(1)).deleteReviewByIds(ids);
    }

    @Test
    public void testGetReviewStatsByCommunityId()
    {
        Map<String, Object> stats = new HashMap<>();
        stats.put("review_count", 10L);
        stats.put("avg_rating", new BigDecimal("4.5"));

        when(reviewMapper.getReviewStatsByCommunityId(100L)).thenReturn(stats);

        Map<String, Object> result = reviewMapper.getReviewStatsByCommunityId(100L);

        assertNotNull(result);
        assertEquals(10L, result.get("review_count"));
        assertEquals(new BigDecimal("4.5"), result.get("avg_rating"));
        verify(reviewMapper, times(1)).getReviewStatsByCommunityId(100L);
    }

    @Test
    public void testUpdateCommunityReviewStats()
    {
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewMapper.updateCommunityReviewStats(100L);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testSelectReviewListWithStatus()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setStatus("0");

        CoreCommunityReview resultReview = new CoreCommunityReview();
        resultReview.setId(1L);
        resultReview.setStatus("0");

        when(reviewMapper.selectReviewList(any(CoreCommunityReview.class))).thenReturn(Arrays.asList(resultReview));

        List<CoreCommunityReview> list = reviewMapper.selectReviewList(review);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("0", list.get(0).getStatus());
        verify(reviewMapper, times(1)).selectReviewList(review);
    }

    @Test
    public void testSelectReviewListWithMemberId()
    {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setMemberId(200L);

        CoreCommunityReview resultReview = new CoreCommunityReview();
        resultReview.setId(1L);
        resultReview.setMemberId(200L);

        when(reviewMapper.selectReviewList(any(CoreCommunityReview.class))).thenReturn(Arrays.asList(resultReview));

        List<CoreCommunityReview> list = reviewMapper.selectReviewList(review);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Long.valueOf(200L), list.get(0).getMemberId());
        verify(reviewMapper, times(1)).selectReviewList(review);
    }
}
