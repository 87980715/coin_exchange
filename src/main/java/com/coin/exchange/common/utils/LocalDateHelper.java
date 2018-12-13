/**
 * @(#) LocalDateHelper.java
 *
 * Copyright (c) 2017, Credan(上海)-版权所有
 */
package com.coin.exchange.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * java8日期工具类
 *
 * @author storys.zhang@aliyun.com
 *
 * Created at 2017/12/14 by Storys.Zhang in credan_soa_risk
 */
public final class LocalDateHelper {

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
    /**
     * 默认使用系统当前时区
     */
    private static final ZoneId ZONE = ZoneId.systemDefault();

    public static Date getCurrentTime(){
        return new Date();
    }

    /**
     * 根据传入的时间格式返回系统当前的时间
     *
     * @param timeFormat TimeFormat
     * @return
     */
    public static String getCurrentTime(TimeFormat timeFormat) {
        if (null == timeFormat) {
            timeFormat = TimeFormat.LONG_DATE_PATTERN_LINE;
        }
        DateTimeFormatter dateTimeFormatter = timeFormat.formatter;
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }

    /**
     * 将Date转换成LocalDateTime
     *
     * @param d date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime;
    }


    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return date
     */
    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将相应格式yyyy-MM-dd yyyy-MM-dd HH:mm:ss 时间字符串转换成Date
     *
     * @param time string
     * @param timeFormat string
     * @return date
     */
    public static Date stringToDate(String time, TimeFormat timeFormat) {
        switch (timeFormat){
            case LONG_DATE_PATTERN_LINE:
                return localDateTimeToDate(LocalDateTime.parse(time, timeFormat.formatter));
            case SHORT_DATE_PATTERN_LINE:
                return localDateToDate(LocalDate.parse(time, timeFormat.formatter));
            default:
                break;
        }
        return null;
    }

    /**
     * 根据ChronoUnit枚举计算两个时间差，日期类型对应枚举
     * 注:注意时间格式，避免cu选择不当的类型出现的异常
     *
     * @param cu chronoUnit.enum.key
     * @param d1 date
     * @param d2 date
     * @return
     */
    public static long dateDiff(ChronoUnit cu, Date d1, Date d2) {
        return cu.between(dateToLocalDateTime(d1), dateToLocalDateTime(d2));
    }

    /**
     * 根据ChronoUnit枚举计算两个时间差，日期类型对应枚举
     * 注:注意时间格式，避免cu选择不当的类型出现的异常
     * s2
     *
     * @param cu chronoUnit.enum.key
     * @param s1 string
     * @param s2 string
     * @return
     */
    public static long dateDiff(ChronoUnit cu, String s1, String s2,
        TimeFormat timeFormat) {
        LocalDateTime l1 = dateToLocalDateTime(stringToDate(s1, timeFormat));
        LocalDateTime l2 = dateToLocalDateTime(stringToDate(s2, timeFormat));
        return cu.between(l1, l2);
    }

    /**
     * 比较s1 < s2
     * @param s1
     * @param s2
     * @param timeFormat
     * @return
     */
    public static boolean dateCompare(String s1, String s2, TimeFormat timeFormat){
        return dateDiff(ChronoUnit.SECONDS, s1, s2, timeFormat) > 0;
    }

    /**
     * 比较 d1 < d2
     * @param d1
     * @param d2
     * @return
     */
    public static boolean dateCompare(Date d1, Date d2){
        return dateDiff(ChronoUnit.SECONDS, d1, d2) > 0;
    }

    /**
     * 增加
     * @param d 时间
     * @param l 增加的值
     * @param timeUnit 时间单位
     * @return
     */
    public static Date add(Date d, long l,TimeUnit timeUnit){
        return localDateTimeToDate(timeUnit.plus(dateToLocalDateTime(d), l));
    }

    /**
     * 减法
     * @param d 时间
     * @param l 减去的值
     * @param timeUnit 时间单位
     * @return
     */
    public static Date minus(Date d, long l,TimeUnit timeUnit){
        return localDateTimeToDate(timeUnit.minus(dateToLocalDateTime(d), l));
    }

    public enum TimeUnit{
        /**
         * 月
         */
        MONTH {
            @Override
            public LocalDateTime plus(LocalDateTime d, long l) {
                return d.plusMonths(l);
            }
            @Override
            public LocalDateTime minus(LocalDateTime d, long l) {
                return d.minusMonths(l);
            }
        },
        /**
         * 日
         */
        DAY {
            @Override
            public LocalDateTime plus(LocalDateTime d, long l) {
                return d.plusDays(l);
            }

            @Override
            public LocalDateTime minus(LocalDateTime d, long l) {
                return d.minusDays(l);
            }
        },
        /**
         * 时
         */
        HOUR {
            @Override
            public LocalDateTime plus(LocalDateTime d, long l) {
                return d.plusHours(l);
            }
            @Override
            public LocalDateTime minus(LocalDateTime d, long l) {
                return d.minusHours(l);
            }
        },
        /**
         * 分
         */
        MINUTE {
            @Override
            public LocalDateTime plus(LocalDateTime d, long l) {
                return d.plusMinutes(l);
            }
            @Override
            public LocalDateTime minus(LocalDateTime d, long l) {
                return d.minusMinutes(l);
            }
        },
        /**
         * 秒
         */
        SECOND {
            @Override
            public LocalDateTime plus(LocalDateTime d, long l) {
                return d.plusSeconds(l);
            }
            @Override
            public LocalDateTime minus(LocalDateTime d, long l) {
                return d.minusSeconds(l);
            }
        },
        ;

        public abstract LocalDateTime plus(LocalDateTime d, long l);
        public abstract LocalDateTime minus(LocalDateTime d, long l);
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {
        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),
        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }
}
