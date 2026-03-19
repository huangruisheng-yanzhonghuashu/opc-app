package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest
{
    @Test
    public void testGetNowDate()
    {
        Date now = DateUtils.getNowDate();
        assertNotNull(now);
        assertTrue(now.getTime() > 0);
    }

    @Test
    public void testGetDate()
    {
        String date = DateUtils.getDate();
        assertNotNull(date);
        assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetTime()
    {
        String time = DateUtils.getTime();
        assertNotNull(time);
        assertTrue(time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testDateTimeNow()
    {
        String dateTime = DateUtils.dateTimeNow();
        assertNotNull(dateTime);
        assertEquals(14, dateTime.length());
    }

    @Test
    public void testDateTimeNowWithFormat()
    {
        String dateTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        assertNotNull(dateTime);
        assertTrue(dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testParseDateToStr()
    {
        Date date = new Date();
        String result = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, date);
        assertNotNull(result);
    }

    @Test
    public void testDateTimeWithDate()
    {
        Date date = new Date();
        String result = DateUtils.dateTime(date);
        assertNotNull(result);
    }

    @Test
    public void testDatePath()
    {
        String path = DateUtils.datePath();
        assertNotNull(path);
        assertTrue(path.matches("\\d{4}/\\d{2}/\\d{2}"));
    }

    @Test
    public void testDateTime()
    {
        String dateTime = DateUtils.dateTime();
        assertNotNull(dateTime);
        assertEquals(8, dateTime.length());
    }

    @Test
    public void testParseDate()
    {
        Date date = DateUtils.parseDate("2024-01-01");
        assertNotNull(date);

        Date dateTime = DateUtils.parseDate("2024-01-01 10:00:00");
        assertNotNull(dateTime);
    }

    @Test
    public void testParseDateWithNull()
    {
        Date date = DateUtils.parseDate(null);
        assertNull(date);
    }

    @Test
    public void testDifferentDaysByMillisecond()
    {
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 7 * 24 * 60 * 60 * 1000L);
        int days = DateUtils.differentDaysByMillisecond(date1, date2);
        assertEquals(7, days);
    }
}
