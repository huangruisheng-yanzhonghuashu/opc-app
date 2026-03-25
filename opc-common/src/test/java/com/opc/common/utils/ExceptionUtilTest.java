package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionUtilTest
{
    @Test
    public void testGetExceptionMessage()
    {
        Exception exception = new RuntimeException("Test exception message");
        String message = ExceptionUtil.getExceptionMessage(exception);

        assertNotNull(message);
        assertTrue(message.contains("Test exception message"));
        assertTrue(message.contains("RuntimeException"));
    }

    @Test
    public void testGetExceptionMessageWithCause()
    {
        Exception cause = new IllegalArgumentException("Root cause");
        Exception exception = new RuntimeException("Wrapper", cause);
        String message = ExceptionUtil.getExceptionMessage(exception);

        assertNotNull(message);
        assertTrue(message.contains("Wrapper"));
        assertTrue(message.contains("Root cause"));
    }

    @Test
    public void testGetExceptionMessageWithStackTrace()
    {
        Exception exception = new RuntimeException("Test");
        String message = ExceptionUtil.getExceptionMessage(exception);

        assertNotNull(message);
        assertTrue(message.contains("at"));
    }

    @Test
    public void testGetRootErrorMessage()
    {
        Exception cause = new IllegalArgumentException("Root cause message");
        Exception exception = new RuntimeException("Wrapper", cause);
        String message = ExceptionUtil.getRootErrorMessage(exception);

        assertEquals("Root cause message", message);
    }

    @Test
    public void testGetRootErrorMessageNoCause()
    {
        Exception exception = new RuntimeException("Direct message");
        String message = ExceptionUtil.getRootErrorMessage(exception);

        assertEquals("Direct message", message);
    }

    @Test
    public void testGetRootErrorMessageNullException()
    {
        String message = ExceptionUtil.getRootErrorMessage(null);
        assertEquals("", message);
    }

    @Test
    public void testGetRootErrorMessageNullMessage()
    {
        Exception exception = new RuntimeException();
        String message = ExceptionUtil.getRootErrorMessage(exception);

        assertEquals("null", message);
    }

    @Test
    public void testGetRootErrorMessageEmptyMessage()
    {
        Exception exception = new RuntimeException("");
        String message = ExceptionUtil.getRootErrorMessage(exception);

        assertEquals("", message);
    }

    @Test
    public void testGetRootErrorMessageNestedCauses()
    {
        Exception root = new IllegalStateException("Deep root");
        Exception middle = new IllegalArgumentException("Middle", root);
        Exception wrapper = new RuntimeException("Wrapper", middle);
        String message = ExceptionUtil.getRootErrorMessage(wrapper);

        assertEquals("Deep root", message);
    }
}
