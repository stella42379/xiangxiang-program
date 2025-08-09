package com.xiangjia.locallife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xiangjia.locallife.model.UserInfo;

/**
 * 用户管理工具类
 * 对应小程序的用户数据管理功能
 */
public class UserManager {
    
    private static final String TAG = "UserManager";
    private static final String PREFS_NAME = "xiangjia_user_prefs";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_LAST_LOGIN_TIME = "last_login_time";
    
    private static UserManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    private UserInfo currentUserInfo;
    
    private UserManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadUserInfo();
    }
    
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }
    
    /**
     * 保存用户信息
     */
    public void saveUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            Log.w(TAG, "尝试保存空的用户信息");
            return;
        }
        
        try {
            String userJson = gson.toJson(userInfo);
            prefs.edit()
                .putString(KEY_USER_INFO, userJson)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putLong(KEY_LAST_LOGIN_TIME, System.currentTimeMillis())
                .apply();
            
            this.currentUserInfo = userInfo;
            Log.d(TAG, "用户信息已保存: " + userInfo.getNickName());
        } catch (Exception e) {
            Log.e(TAG, "保存用户信息失败", e);
        }
    }
    
    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        try {
            String userJson = prefs.getString(KEY_USER_INFO, null);
            if (userJson != null) {
                currentUserInfo = gson.fromJson(userJson, UserInfo.class);
                Log.d(TAG, "用户信息已加载: " + currentUserInfo.getNickName());
            } else {
                // 创建默认用户信息
                currentUserInfo = new UserInfo();
                Log.d(TAG, "创建默认用户信息");
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "解析用户信息失败，使用默认信息", e);
            currentUserInfo = new UserInfo();
        }
    }
    
    /**
     * 获取当前用户信息
     */
    public UserInfo getCurrentUserInfo() {
        if (currentUserInfo == null) {
            loadUserInfo();
        }
        return currentUserInfo;
    }
    
    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && currentUserInfo != null;
    }
    
    /**
     * 更新用户昵称
     */
    public void updateNickName(String nickName) {
        if (currentUserInfo != null) {
            currentUserInfo.setNickName(nickName);
            saveUserInfo(currentUserInfo);
        }
    }
    
    /**
     * 更新用户头像
     */
    public void updateAvatar(String avatarUrl) {
        if (currentUserInfo != null) {
            currentUserInfo.setAvatarUrl(avatarUrl);
            saveUserInfo(currentUserInfo);
        }
    }
    
    /**
     * 更新用户手机号
     */
    public void updatePhoneNumber(String phoneNumber) {
        if (currentUserInfo != null) {
            currentUserInfo.setPhoneNumber(phoneNumber);
            currentUserInfo.setUserId(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7));
            saveUserInfo(currentUserInfo);
        }
    }
    
    /**
     * 设置用户认证状态
     */
    public void setAuthenticated(boolean authenticated) {
        if (currentUserInfo != null) {
            currentUserInfo.setAuthenticated(authenticated);
            currentUserInfo.setUserType(authenticated ? "已认证用户" : "未认证用户");
            saveUserInfo(currentUserInfo);
        }
    }
    
    /**
     * 用户登录
     */
    public void login(String nickName, String avatarUrl, String userId, String openId) {
        UserInfo userInfo = new UserInfo(nickName, avatarUrl, userId);
        userInfo.setOpenId(openId);
        userInfo.setAuthenticated(true);
        userInfo.setLastLoginTime(System.currentTimeMillis());
        
        saveUserInfo(userInfo);
        Log.d(TAG, "用户登录成功: " + nickName);
    }
    
    /**
     * 用户登出
     */
    public void logout() {
        prefs.edit()
            .remove(KEY_USER_INFO)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply();
        
        currentUserInfo = new UserInfo(); // 重置为默认用户信息
        Log.d(TAG, "用户已登出");
    }
    
    /**
     * 获取最后登录时间
     */
    public long getLastLoginTime() {
        return prefs.getLong(KEY_LAST_LOGIN_TIME, 0);
    }
    
    /**
     * 清除所有用户数据
     */
    public void clearAllData() {
        prefs.edit().clear().apply();
        currentUserInfo = new UserInfo();
        Log.d(TAG, "所有用户数据已清除");
    }
    
    /**
     * 模拟微信授权获取用户信息
     * 对应小程序的getUserProfile功能
     */
    public void requestUserProfile(UserProfileCallback callback) {
        // 模拟异步获取用户信息的过程
        new Thread(() -> {
            try {
                Thread.sleep(500); // 模拟网络请求延迟
                
                // 模拟获取到的用户信息
                String nickName = "湘湘用户" + System.currentTimeMillis() % 1000;
                String avatarUrl = ""; // 实际项目中这里是头像URL
                String userId = "186****" + (1000 + System.currentTimeMillis() % 9000);
                
                // 更新用户信息
                login(nickName, avatarUrl, userId, "mock_openid_" + System.currentTimeMillis());
                
                // 回调成功
                if (callback != null) {
                    callback.onSuccess(currentUserInfo);
                }
                
            } catch (InterruptedException e) {
                Log.e(TAG, "获取用户信息被中断", e);
                if (callback != null) {
                    callback.onFailure("获取用户信息失败");
                }
            }
        }).start();
    }
    
    /**
     * 用户信息获取回调接口
     */
    public interface UserProfileCallback {
        void onSuccess(UserInfo userInfo);
        void onFailure(String error);
    }
}