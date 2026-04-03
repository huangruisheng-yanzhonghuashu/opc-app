package com.opc.common.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    public void testGetNowDate() {
        Date now = DateUtils.getNowDate();
        assertNotNull(now);
        assertTrue(now.getTime() <= System.currentTimeMillis());
    }

    @Test
    public void testGetDate() {
        String date = DateUtils.getDate();
        assertNotNull(date);
        assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetTime() {
        String time = DateUtils.getTime();
        assertNotNull(time);
        assertTrue(time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testDateTimeNow() {
        String dateTime = DateUtils.dateTimeNow();
        assertNotNull(dateTime);
        assertEquals(14, dateTime.length());
        assertTrue(dateTime.matches("\\d{14}"));
    }

    @Test
    public void testDateTimeNowWithFormat() {
        String dateTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        assertNotNull(dateTime);
        assertTrue(dateTime.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testDateTime() {
        Date date = new Date(1609459200000L); // 2021-01-01 00:00:00
        String result = DateUtils.dateTime(date);
        assertEquals("2021-01-01", result);
    }

    @Test
    public void testParseDateToStr() {
        Date date = new Date(1609459200000L);
        String result = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
        assertNotNull(result);
        assertTrue(result.contains("2021"));
    }

    @Test
    public void testDateTimeWithFormat() {
        Date result = DateUtils.dateTime(DateUtils.YYYY_MM_DD, "2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testDatePath() {
        String path = DateUtils.datePath();
        assertNotNull(path);
        assertTrue(path.matches("\\d{4}/\\d{2}/\\d{2}"));
    }

    @Test
    public void testDateTimeShort() {
        String date = DateUtils.dateTime();
        assertNotNull(date);
        assertEquals(8, date.length());
        assertTrue(date.matches("\\d{8}"));
    }

    @Test
    public void testParseDateWithNull() {
        Date result = DateUtils.parseDate(null);
        assertNull(result);
    }

    @Test
    public void testParseDateWithValidString() {
        Date result = DateUtils.parseDate("2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testParseDateWithInvalidString() {
        Date result = DateUtils.parseDate("invalid");
        assertNull(result);
    }

    @Test
    public void testGetServerStartDate() {
        Date startDate = DateUtils.getServerStartDate();
        assertNotNull(startDate);
        assertTrue(startDate.getTime() <= System.currentTimeMillis());
    }

    @Test
    public void testDifferentDaysByMillisecond() {
        Date date1 = new Date(1609459200000L); // 2021-01-01
        Date date2 = new Date(1609545600000L); // 2021-01-02
        int days = DateUtils.differentDaysByMillisecond(date1, date2);
        assertEquals(1, days);
    }

    @Test
    public void testDifferentDaysByMillisecondSameDay() {
        Date date = new Date();
        int days = DateUtils.differentDaysByMillisecond(date, date);
        assertEquals(0, days);
    }

    @Test
    public void testTimeDistance() {
        Date start = new Date(1609459200000L);
        Date end = new Date(1609459200000L + 86400000L + 3600000L + 60000L); // 1 day + 1 hour + 1 minute
        String distance = DateUtils.timeDistance(end, start);
        assertNotNull(distance);
        assertTrue(distance.contains("天"));
        assertTrue(distance.contains("小时"));
        assertTrue(distance.contains("分钟"));
    }

    @Test
    public void testToDateFromLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
        Date date = DateUtils.toDate(localDateTime);
        assertNotNull(date);
    }

    @Test
    public void testToDateFromLocalDate() {
        LocalDate localDate = LocalDate.of(2021, 1, 1);
        Date date = DateUtils.toDate(localDate);
        assertNotNull(date);
    }
}
