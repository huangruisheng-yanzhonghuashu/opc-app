package com.opc.core.service.impl;

import com.opc.core.domain.CoreCommunityReview;
import com.opc.core.mapper.CoreCommunityReviewMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
public class CoreCommunityReviewServiceImplTest {

    @Mock
    private CoreCommunityReviewMapper reviewMapper;

    @InjectMocks
    private CoreCommunityReviewServiceImpl reviewService;

    @Test
    public void testSelectReviewById() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("4.5"));
        review.setContent("测试评价");

        when(reviewMapper.selectReviewById(1L)).thenReturn(review);

        CoreCommunityReview result = reviewService.selectReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试评价", result.getContent());
    }

    @Test
    public void testSelectReviewList() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setCommunityId(100L);

        CoreCommunityReview resultReview = new CoreCommunityReview();
        resultReview.setId(1L);
        resultReview.setCommunityId(100L);
        resultReview.setRating(new BigDecimal("4.5"));

        when(reviewMapper.selectReviewList(any(CoreCommunityReview.class))).thenReturn(Arrays.asList(resultReview));

        List<CoreCommunityReview> list = reviewService.selectReviewList(review);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Long.valueOf(100L), list.get(0).getCommunityId());
    }

    @Test
    public void testSelectReviewsByCommunityId() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setRating(new BigDecimal("5.0"));

        when(reviewMapper.selectReviewsByCommunityId(100L)).thenReturn(Arrays.asList(review));

        List<CoreCommunityReview> list = reviewService.selectReviewsByCommunityId(100L);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Long.valueOf(100L), list.get(0).getCommunityId());
    }

    @Test
    public void testInsertReview() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setCommunityId(100L);
        review.setMemberId(200L);
        review.setRating(new BigDecimal("5.0"));
        review.setContent("新增评价");

        when(reviewMapper.insertReview(any(CoreCommunityReview.class))).thenReturn(1);
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.insertReview(review);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).insertReview(review);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testUpdateReview() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setContent("更新评价");
        review.setRating(new BigDecimal("4.0"));

        when(reviewMapper.updateReview(any(CoreCommunityReview.class))).thenReturn(1);
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.updateReview(review);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).updateReview(review);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testDeleteReviewById() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);

        when(reviewMapper.selectReviewById(1L)).thenReturn(review);
        when(reviewMapper.deleteReviewById(1L)).thenReturn(1);
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.deleteReviewById(1L);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).deleteReviewById(1L);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testDeleteReviewByIds() {
        Long[] ids = {1L, 2L, 3L};

        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);

        when(reviewMapper.selectReviewById(1L)).thenReturn(review);
        when(reviewMapper.deleteReviewByIds(ids)).thenReturn(3);
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.deleteReviewByIds(ids);

        assertEquals(3, result);
        verify(reviewMapper, times(1)).deleteReviewByIds(ids);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testGetReviewStatsByCommunityId() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("review_count", 10L);
        stats.put("avg_rating", new BigDecimal("4.5"));

        when(reviewMapper.getReviewStatsByCommunityId(100L)).thenReturn(stats);

        Map<String, Object> result = reviewService.getReviewStatsByCommunityId(100L);

        assertNotNull(result);
        assertEquals(10L, result.get("review_count"));
        assertEquals(new BigDecimal("4.5"), result.get("avg_rating"));
    }

    @Test
    public void testUpdateCommunityReviewStats() {
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.updateCommunityReviewStats(100L);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testAuditReview() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setId(1L);
        review.setCommunityId(100L);
        review.setStatus("0");

        when(reviewMapper.selectReviewById(1L)).thenReturn(review);
        when(reviewMapper.updateReview(any(CoreCommunityReview.class))).thenReturn(1);
        when(reviewMapper.updateCommunityReviewStats(100L)).thenReturn(1);

        int result = reviewService.auditReview(1L, "1");

        assertEquals(1, result);
        verify(reviewMapper, times(1)).updateReview(any(CoreCommunityReview.class));
        verify(reviewMapper, times(1)).updateCommunityReviewStats(100L);
    }

    @Test
    public void testInsertReviewWithoutCommunityId() {
        CoreCommunityReview review = new CoreCommunityReview();
        review.setMemberId(200L);
        review.setRating(new BigDecimal("5.0"));
        review.setContent("新增评价");

        when(reviewMapper.insertReview(any(CoreCommunityReview.class))).thenReturn(1);

        int result = reviewService.insertReview(review);

        assertEquals(1, result);
        verify(reviewMapper, times(1)).insertReview(review);
        verify(reviewMapper, never()).updateCommunityReviewStats(any());
    }
}
