package com.opc.quartz.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class CronUtilsTest
{
    @Test
    public void testIsValid_ValidCron()
    {
        assertTrue(CronUtils.isValid("0 0 12 * * ?"));
        assertTrue(CronUtils.isValid("0 15 10 ? * *"));
        assertTrue(CronUtils.isValid("0 0/5 14 * * ?"));
        assertTrue(CronUtils.isValid("0 15 10 ? * MON-FRI"));
    }

    @Test
    public void testIsValid_InvalidCron()
    {
        assertFalse(CronUtils.isValid("invalid"));
        assertFalse(CronUtils.isValid(""));
        assertFalse(CronUtils.isValid("0 0"));
        assertFalse(CronUtils.isValid("* * * * * * * *"));
    }

    @Test
    public void testGetInvalidMessage_ValidCron()
    {
        String message = CronUtils.getInvalidMessage("0 0 12 * * ?");
        assertNull(message);
    }

    @Test
    public void testGetInvalidMessage_InvalidCron()
    {
        String message = CronUtils.getInvalidMessage("invalid");
        assertNotNull(message);
    }

    @Test
    public void testGetInvalidMessage_EmptyCron()
    {
        String message = CronUtils.getInvalidMessage("");
        assertNotNull(message);
    }

    @Test
    public void testGetNextExecution_ValidCron()
    {
        Date nextExecution = CronUtils.getNextExecution("0 0 12 * * ?");
        assertNotNull(nextExecution);
        assertTrue(nextExecution.after(new Date()));
    }

    @Test
    public void testGetNextExecution_EveryMinute()
    {
        Date nextExecution = CronUtils.getNextExecution("0 * * * * ?");
        assertNotNull(nextExecution);
    }

    @Test
    public void testGetNextExecution_EverySecond()
    {
        Date nextExecution = CronUtils.getNextExecution("* * * * * ?");
        assertNotNull(nextExecution);
    }

    @Test
    public void testGetNextExecution_InvalidCron()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            CronUtils.getNextExecution("invalid");
        });
    }

    @Test
    public void testGetNextExecution_EmptyCron()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            CronUtils.getNextExecution("");
        });
    }

    @Test
    public void testCronExpressions()
    {
        assertTrue(CronUtils.isValid("0 0 0 * * ?"));
        assertTrue(CronUtils.isValid("0 0 12 * * ?"));
        assertTrue(CronUtils.isValid("0 0 23 ? * *"));
        assertTrue(CronUtils.isValid("0 0 1 1 * ?"));
        assertTrue(CronUtils.isValid("0 0 0 ? * MON"));
    }

    @Test
    public void testCronWithRanges()
    {
        assertTrue(CronUtils.isValid("0 0 9-17 * * ?"));
        assertTrue(CronUtils.isValid("0 0/30 9-17 * * ?"));
    }

    @Test
    public void testCronWithLists()
    {
        assertTrue(CronUtils.isValid("0 0 9,12,17 * * ?"));
        assertTrue(CronUtils.isValid("0 0 0 ? * MON,WED,FRI"));
    }

    @Test
    public void testCronWithStep()
    {
        assertTrue(CronUtils.isValid("0 0/5 * * * ?"));
        assertTrue(CronUtils.isValid("0 0 0 1/2 * ?"));
    }

    @Test
    public void testCronWithLastDay()
    {
        assertTrue(CronUtils.isValid("0 0 0 L * ?"));
    }

    @Test
    public void testCronWithWeekday()
    {
        assertTrue(CronUtils.isValid("0 0 0 1W * ?"));
    }

    @Test
    public void testCronWithHash()
    {
        assertTrue(CronUtils.isValid("0 0 0 ? * 1#1"));
    }

    @Test
    public void testGetNextExecutionReturnsFutureDate()
    {
        Date now = new Date();
        Date nextExecution = CronUtils.getNextExecution("0 0 12 * * ?");

        assertNotNull(nextExecution);
        assertTrue(nextExecution.after(now) || nextExecution.equals(now));
    }

    @Test
    public void testGetNextExecutionMultipleTimes()
    {
        String cron = "0 0/5 * * * ?";

        Date execution1 = CronUtils.getNextExecution(cron);
        Date execution2 = CronUtils.getNextExecution(cron);

        assertNotNull(execution1);
        assertNotNull(execution2);
    }
}
