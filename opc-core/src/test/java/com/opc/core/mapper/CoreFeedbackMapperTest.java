package com.opc.core.mapper;

import com.opc.core.domain.CoreFeedback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreFeedbackMapperTest
{
    @Mock
    private CoreFeedbackMapper feedbackMapper;

    @Test
    public void testSelectFeedbackList()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setTitle("Test Feedback");

        CoreFeedback resultFeedback = new CoreFeedback();
        resultFeedback.setId(1L);
        resultFeedback.setTitle("Test Feedback");

        when(feedbackMapper.selectFeedbackList(any(CoreFeedback.class))).thenReturn(Arrays.asList(resultFeedback));

        List<CoreFeedback> list = feedbackMapper.selectFeedbackList(feedback);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Feedback", list.get(0).getTitle());
        verify(feedbackMapper, times(1)).selectFeedbackList(feedback);
    }

    @Test
    public void testSelectFeedbackById()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setTitle("Test Feedback");

        when(feedbackMapper.selectFeedbackById(1L)).thenReturn(feedback);

        CoreFeedback result = feedbackMapper.selectFeedbackById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Feedback", result.getTitle());
        verify(feedbackMapper, times(1)).selectFeedbackById(1L);
    }

    @Test
    public void testInsertFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setTitle("New Feedback");
        feedback.setContent("Feedback content");

        when(feedbackMapper.insertFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackMapper.insertFeedback(feedback);

        assertEquals(1, result);
        verify(feedbackMapper, times(1)).insertFeedback(feedback);
    }

    @Test
    public void testUpdateFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setTitle("Updated Feedback");

        when(feedbackMapper.updateFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackMapper.updateFeedback(feedback);

        assertEquals(1, result);
        verify(feedbackMapper, times(1)).updateFeedback(feedback);
    }

    @Test
    public void testReplyFeedback()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setReply("Reply content");
        feedback.setReplyBy("admin");

        when(feedbackMapper.replyFeedback(any(CoreFeedback.class))).thenReturn(1);

        int result = feedbackMapper.replyFeedback(feedback);

        assertEquals(1, result);
        verify(feedbackMapper, times(1)).replyFeedback(feedback);
    }

    @Test
    public void testDeleteFeedbackById()
    {
        when(feedbackMapper.deleteFeedbackById(1L)).thenReturn(1);

        int result = feedbackMapper.deleteFeedbackById(1L);

        assertEquals(1, result);
        verify(feedbackMapper, times(1)).deleteFeedbackById(1L);
    }

    @Test
    public void testDeleteFeedbackByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(feedbackMapper.deleteFeedbackByIds(ids)).thenReturn(3);

        int result = feedbackMapper.deleteFeedbackByIds(ids);

        assertEquals(3, result);
        verify(feedbackMapper, times(1)).deleteFeedbackByIds(ids);
    }
}
