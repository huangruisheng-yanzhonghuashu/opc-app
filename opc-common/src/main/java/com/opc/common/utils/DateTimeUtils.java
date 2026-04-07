package com.opc.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 * <p>
 * 统一使用北京时间（Asia/Shanghai）处理日期时间
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
public class DateTimeUtils {

    /** 北京时区 */
    public static final ZoneId BEIJING_ZONE = ZoneId.of("Asia/Shanghai");

    /** 标准日期时间格式 */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式 */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /** 时间格式 */
    public static final String TIME_PATTERN = "HH:mm:ss";

    /** 带毫秒的日期时间格式 */
    public static final String DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获取当前北京时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(BEIJING_ZONE);
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前北京时间字符串
     *
     * @return yyyy-MM-dd HH:mm:ss 格式字符串
     */
    public static String nowStr() {
        return format(now(), DEFAULT_PATTERN);
    }

    /**
     * 格式化日期时间
     *
     * @param dateTime LocalDateTime
     * @param pattern  格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    /**
     * 格式化日期时间（使用默认格式）
     *
     * @param dateTime LocalDateTime
     * @return yyyy-MM-dd HH:mm:ss 格式字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_PATTERN);
    }

    /**
     * 解析日期时间字符串
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     格式
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期时间字符串（使用默认格式）
     *
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, DEFAULT_PATTERN);
    }

    /**
     * Instant 转换为北京时间的 LocalDateTime
     *
     * @param instant Instant
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, BEIJING_ZONE);
    }

    /**
     * Date 转换为 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date.toInstant());
    }

    /**
     * LocalDateTime 转换为 Date
     *
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(BEIJING_ZONE).toInstant());
    }

    /**
     * 获取当前日期字符串
     *
     * @return yyyy-MM-dd 格式字符串
     */
    public static String todayStr() {
        return format(now(), DATE_PATTERN);
    }

    /**
     * 获取当前时间字符串
     *
     * @return HH:mm:ss 格式字符串
     */
    public static String currentTimeStr() {
        return format(now(), TIME_PATTERN);
    }
}
