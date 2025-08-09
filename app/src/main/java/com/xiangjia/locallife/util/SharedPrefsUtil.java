package com.xiangjia.locallife.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * 用于存储用户登录状态和应用设置
 */
public class SharedPrefsUtil {
    
    private static final String PREF_NAME = "LocalLifePrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AVATAR_URL = "avatar_url";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_LAST_LOGIN_TIME = "last_login_time";
    
    // 应用设置相关
    private static final String KEY_THEME_MODE = "theme_mode";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    
    /**
     * 获取SharedPreferences实例
     */
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * 保存用户登录信息
     */
    public static void saveUserInfo(Context context, String userId, String username, 
                                   String nickname, String email, String avatarUrl, String userRole) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NICKNAME, nickname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AVATAR_URL, avatarUrl);
        editor.putString(KEY_USER_ROLE, userRole);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_LAST_LOGIN_TIME, System.currentTimeMillis());
        editor.apply();
    }
    
    /**
     * 获取用户ID
     */
    public static String getUserId(Context context) {
        return getPrefs(context).getString(KEY_USER_ID, null);
    }
    
    /**
     * 获取用户名
     */
    public static String getUsername(Context context) {
        return getPrefs(context).getString(KEY_USERNAME, null);
    }
    
    /**
     * 获取昵称
     */
    public static String getNickname(Context context) {
        return getPrefs(context).getString(KEY_NICKNAME, null);
    }
    
    /**
     * 获取邮箱
     */
    public static String getEmail(Context context) {
        return getPrefs(context).getString(KEY_EMAIL, null);
    }
    
    /**
     * 获取头像URL
     */
    public static String getAvatarUrl(Context context) {
        return getPrefs(context).getString(KEY_AVATAR_URL, null);
    }
    
    /**
     * 获取用户角色
     */
    public static String getUserRole(Context context) {
        return getPrefs(context).getString(KEY_USER_ROLE, "user");
    }
    
    /**
     * 检查是否已登录
     */
    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * 获取最后登录时间
     */
    public static long getLastLoginTime(Context context) {
        return getPrefs(context).getLong(KEY_LAST_LOGIN_TIME, 0);
    }
    
    /**
     * 更新用户信息
     */
    public static void updateUserInfo(Context context, String nickname, String avatarUrl) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        if (nickname != null) {
            editor.putString(KEY_NICKNAME, nickname);
        }
        if (avatarUrl != null) {
            editor.putString(KEY_AVATAR_URL, avatarUrl);
        }
        editor.apply();
    }
    
    /**
     * 清除用户登录信息
     */
    public static void clearUserInfo(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_NICKNAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_AVATAR_URL);
        editor.remove(KEY_USER_ROLE);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
    
    /**
     * 保存主题模式
     */
    public static void setThemeMode(Context context, String themeMode) {
        getPrefs(context).edit().putString(KEY_THEME_MODE, themeMode).apply();
    }
    
    /**
     * 获取主题模式
     */
    public static String getThemeMode(Context context) {
        return getPrefs(context).getString(KEY_THEME_MODE, "auto");
    }
    
    /**
     * 设置通知开关
     */
    public static void setNotificationEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }
    
    /**
     * 获取通知开关状态
     */
    public static boolean isNotificationEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }
    
    /**
     * 设置声音开关
     */
    public static void setSoundEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }
    
    /**
     * 获取声音开关状态
     */
    public static boolean isSoundEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SOUND_ENABLED, true);
    }
    
    /**
     * 设置震动开关
     */
    public static void setVibrationEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }
    
    /**
     * 获取震动开关状态
     */
    public static boolean isVibrationEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_VIBRATION_ENABLED, true);
    }
    
    /**
     * 保存字符串值
     */
    public static void putString(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).apply();
    }
    
    /**
     * 获取字符串值
     */
    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }
    
    /**
     * 保存布尔值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).apply();
    }
    
    /**
     * 获取布尔值
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }
    
    /**
     * 保存整数值
     */
    public static void putInt(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).apply();
    }
    
    /**
     * 获取整数值
     */
    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }
    
    /**
     * 保存长整数值
     */
    public static void putLong(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).apply();
    }
    
    /**
     * 获取长整数值
     */
    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }
    
    /**
     * 移除指定键的值
     */
    public static void remove(Context context, String key) {
        getPrefs(context).edit().remove(key).apply();
    }
    
    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        getPrefs(context).edit().clear().apply();
    }
    
    /**
     * 检查是否包含指定键
     */
    public static boolean contains(Context context, String key) {
        return getPrefs(context).contains(key);
    }
}