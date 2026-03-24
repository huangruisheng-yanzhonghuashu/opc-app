package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class CoreFeedbackTest
{
    @Test
    public void testCoreFeedbackGettersAndSetters()
    {
        CoreFeedback feedback = new CoreFeedback();

        feedback.setId(1L);
        feedback.setMemberId(100L);
        feedback.setMemberName("Test Member");
        feedback.setType("bug");
        feedback.setTitle("Bug Report");
        feedback.setContent("This is a bug description");
        feedback.setContact("contact@example.com");
        feedback.setStatus("0");
        feedback.setReply("Fixed");
        feedback.setReplyTime(Instant.parse("2024-01-01T10:00:00Z"));
        feedback.setReplyBy("admin");

        assertEquals(1L, feedback.getId());
        assertEquals(100L, feedback.getMemberId());
        assertEquals("Test Member", feedback.getMemberName());
        assertEquals("bug", feedback.getType());
        assertEquals("Bug Report", feedback.getTitle());
        assertEquals("This is a bug description", feedback.getContent());
        assertEquals("contact@example.com", feedback.getContact());
        assertEquals("0", feedback.getStatus());
        assertEquals("Fixed", feedback.getReply());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"), feedback.getReplyTime());
        assertEquals("admin", feedback.getReplyBy());
    }

    @Test
    public void testCoreFeedbackToString()
    {
        CoreFeedback feedback = new CoreFeedback();
        feedback.setId(1L);
        feedback.setTitle("Bug Report");

        String str = feedback.toString();
        assertNotNull(str);
        assertTrue(str.contains("Bug Report"));
    }

    @Test
    public void testCoreFeedbackDefaultValues()
    {
        CoreFeedback feedback = new CoreFeedback();

        assertNull(feedback.getId());
        assertNull(feedback.getMemberId());
        assertNull(feedback.getMemberName());
        assertNull(feedback.getType());
        assertNull(feedback.getTitle());
        assertNull(feedback.getContent());
        assertNull(feedback.getContact());
        assertNull(feedback.getStatus());
        assertNull(feedback.getReply());
        assertNull(feedback.getReplyTime());
        assertNull(feedback.getReplyBy());
    }

    @Test
    public void testCoreFeedbackStatusValues()
    {
        CoreFeedback feedback = new CoreFeedback();

        feedback.setStatus("0");
        assertEquals("0", feedback.getStatus());

        feedback.setStatus("1");
        assertEquals("1", feedback.getStatus());

        feedback.setStatus("2");
        assertEquals("2", feedback.getStatus());
    }

    @Test
    public void testCoreFeedbackTypeValues()
    {
        CoreFeedback feedback = new CoreFeedback();

        feedback.setType("bug");
        assertEquals("bug", feedback.getType());

        feedback.setType("feature");
        assertEquals("feature", feedback.getType());

        feedback.setType("other");
        assertEquals("other", feedback.getType());
    }
}
