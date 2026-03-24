package com.opc.core.service.impl;

import com.opc.core.domain.CoreFeedback;
import com.opc.core.mapper.CoreFeedbackMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreFeedbackServiceImplTest
{
    @Mock
    private CoreFeedbackMapper feedbackMapper;

    @InjectMocks
    private CoreFeedbackServiceImpl feedbackService;

    @Test
    public void testSelectFeedbackList()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setTitle("Test Feedback");

        CoreFeedback resultFeedback = new CoreFeedback();
        resultFeedback.setId(1L);
        resultFeedback.setTitle("Test Feedback");

        when(feedbackMapper.selectFeedbackList(any(CoreFeedback.class))).thenReturn(Arrays.asList(resultFeedback));

        List<CoreFeedback> list = feedbackService.selectFeedbackList(feedback);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Feedback", list.get(0).getTitle());
    }

    @Test
    public void testSelectFeedbackById()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setTitle("Test Feedback");

        when(feedbackMapper.selectFeedbackById(1L)).thenReturn(feedback);

        CoreFeedback result = feedbackService.selectFeedbackById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Feedback", result.getTitle());
    }

    @Test
    public void testInsertFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setTitle("New Feedback");
        feedback.setContent("Feedback content");

        when(feedbackMapper.insertFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackService.insertFeedback(feedback);

        assertEquals(1, result);
        assertEquals("0", feedback.getStatus());
    }

    @Test
    public void testUpdateFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setTitle("Updated Feedback");

        when(feedbackMapper.updateFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackService.updateFeedback(feedback);

        assertEquals(1, result);
    }

    @Test
    public void testReplyFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setReply("Reply content");
        feedback.setReplyBy("admin");

        when(feedbackMapper.replyFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackService.replyFeedback(feedback);

        assertEquals(1, result);
        assertNotNull(feedback.getReplyTime());
    }

    @Test
    public void testDeleteFeedbackById()
    {
        when(feedbackMapper.deleteFeedbackById(1L)).thenReturn(1);

        int result = feedbackService.deleteFeedbackById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteFeedbackByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(feedbackMapper.deleteFeedbackByIds(ids)).thenReturn(3);

        int result = feedbackService.deleteFeedbackByIds(ids);

        assertEquals(3, result);
    }
}
