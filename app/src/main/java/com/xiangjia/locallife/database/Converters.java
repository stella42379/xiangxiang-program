package com.xiangjia.locallife.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Room数据库类型转换器
 * 用于处理复杂数据类型的转换
 */
public class Converters {
    
    private static final Gson gson = new Gson();
    
    /**
     * Date转Long
     */
    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
    
    /**
     * Long转Date
     */
    @TypeConverter
    public static Date longToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
    
    /**
     * List<String>转JSON字符串
     */
    @TypeConverter
    public static String listToString(List<String> list) {
        return list == null ? null : gson.toJson(list);
    }
    
    /**
     * JSON字符串转List<String>
     */
    @TypeConverter
    public static List<String> stringToList(String json) {
        if (json == null) {
            return null;
        }
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    
    /**
     * Object转JSON字符串
     */
    @TypeConverter
    public static String objectToString(Object object) {
        return object == null ? null : gson.toJson(object);
    }
    
    /**
     * JSON字符串转Object
     */
    @TypeConverter
    public static <T> T stringToObject(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }
}
