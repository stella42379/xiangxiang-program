package com.xiangjia.locallife.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 提供日期时间处理功能
 */
public class DateUtils {
    
    private static final String TAG = "DateUtils";
    
    // 常用日期格式
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD = "MM-dd";
    
    /**
     * 获取当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * 获取当前日期字符串
     */
    public static String getCurrentDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
    
    /**
     * 格式化时间戳
     */
    public static String formatTimestamp(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * 解析日期字符串
     */
    public static Date parseDateString(String dateString, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取相对时间描述
     */
    public static String getRelativeTimeString(long timestamp) {
        long currentTime = getCurrentTimestamp();
        long diff = currentTime - timestamp;
        
        // 转换为秒
        long seconds = diff / 1000;
        
        if (seconds < 60) {
            return "刚刚";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else if (seconds < 2592000) {
            return (seconds / 86400) + "天前";
        } else if (seconds < 31536000) {
            return (seconds / 2592000) + "个月前";
        } else {
            return (seconds / 31536000) + "年前";
        }
    }
    
    /**
     * 获取友好的时间显示
     */
    public static String getFriendlyTimeString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        
        Calendar now = Calendar.getInstance();
        
        // 如果是今天
        if (isSameDay(calendar, now)) {
            return formatTimestamp(timestamp, FORMAT_HH_MM);
        }
        
        // 如果是昨天
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(calendar, yesterday)) {
            return "昨天 " + formatTimestamp(timestamp, FORMAT_HH_MM);
        }
        
        // 如果是本周
        if (isSameWeek(calendar, now)) {
            return getWeekDayString(calendar.get(Calendar.DAY_OF_WEEK)) + " " + formatTimestamp(timestamp, FORMAT_HH_MM);
        }
        
        // 其他情况显示完整日期
        return formatTimestamp(timestamp, FORMAT_MM_DD_HH_MM);
    }
    
    /**
     * 判断是否是同一天
     */
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 判断是否是同一周
     */
    private static boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 获取星期字符串
     */
    private static String getWeekDayString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "周日";
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            default:
                return "";
        }
    }
    
    /**
     * 获取指定日期的开始时间戳
     */
    public static long getDayStartTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    /**
     * 获取指定日期的结束时间戳
     */
    public static long getDayEndTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
    
    /**
     * 获取两个时间戳之间的天数差
     */
    public static int getDaysBetween(long timestamp1, long timestamp2) {
        long diff = Math.abs(timestamp1 - timestamp2);
        return (int) (diff / (24 * 60 * 60 * 1000));
    }
    
    /**
     * 判断是否是今天
     */
    public static boolean isToday(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Calendar now = Calendar.getInstance();
        return isSameDay(calendar, now);
    }
    
    /**
     * 判断是否是昨天
     */
    public static boolean isYesterday(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return isSameDay(calendar, yesterday);
    }
}
