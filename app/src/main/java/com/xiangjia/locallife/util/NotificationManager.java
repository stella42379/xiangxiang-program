package com.xiangjia.locallife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiangjia.locallife.model.NotificationItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通知管理工具类
 * 对应小程序的通知数据管理功能
 */
public class NotificationManager {
    
    private static final String TAG = "NotificationManager";
    private static final String PREFS_NAME = "xiangjia_notification_prefs";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_LAST_UPDATE = "last_update";
    
    private static NotificationManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    private List<NotificationItem> notifications;
    
    private NotificationManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadNotifications();
    }
    
    public static synchronized NotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationManager(context);
        }
        return instance;
    }
    
    /**
     * 加载通知数据
     */
    private void loadNotifications() {
        try {
            String notificationsJson = prefs.getString(KEY_NOTIFICATIONS, null);
            if (notificationsJson != null) {
                Type listType = new TypeToken<List<NotificationItem>>(){}.getType();
                notifications = gson.fromJson(notificationsJson, listType);
                Log.d(TAG, "通知数据已加载，共 " + notifications.size() + " 条");
            } else {
                // 创建默认通知数据
                createDefaultNotifications();
                saveNotifications();
            }
        } catch (Exception e) {
            Log.e(TAG, "加载通知数据失败，使用默认数据", e);
            createDefaultNotifications();
        }
    }
    
    /**
     * 创建默认通知数据 - 对应小程序的初始通知
     */
    private void createDefaultNotifications() {
        notifications = new ArrayList<>();
        
        // 系统维护通知
        NotificationItem systemNotice = new NotificationItem(
            1, "🔔", "icon-red", "系统维护通知",
            "系统将于今晚23:00-02:00进行维护升级，届时部分功能可能暂时无法使用",
            "2小时前", true
        );
        systemNotice.setType("system");
        
        // 服务完成通知
        NotificationItem serviceComplete = new NotificationItem(
            2, "✅", "icon-green", "服务完成通知",
            "您的故障报修服务已完成，感谢您的使用！如有问题请及时反馈",
            "1天前", false
        );
        serviceComplete.setType("service");
        
        // 新功能上线通知
        NotificationItem newFeature = new NotificationItem(
            3, "📰", "icon-blue", "新功能上线",
            "湘民服务新增AI智能客服功能，24小时为您提供专业服务",
            "3天前", false
        );
        newFeature.setType("news");
        
        notifications.add(systemNotice);
        notifications.add(serviceComplete);
        notifications.add(newFeature);
        
        Log.d(TAG, "默认通知数据已创建");
    }
    
    /**
     * 保存通知数据
     */
    private void saveNotifications() {
        try {
            String notificationsJson = gson.toJson(notifications);
            prefs.edit()
                .putString(KEY_NOTIFICATIONS, notificationsJson)
                .putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
                .apply();
            Log.d(TAG, "通知数据已保存");
        } catch (Exception e) {
            Log.e(TAG, "保存通知数据失败", e);
        }
    }
    
    /**
     * 获取所有通知
     */
    public List<NotificationItem> getAllNotifications() {
        if (notifications == null) {
            loadNotifications();
        }
        return new ArrayList<>(notifications);
    }
    
    /**
     * 获取新通知数量
     */
    public int getNewNotificationCount() {
        if (notifications == null) {
            loadNotifications();
        }
        
        int count = 0;
        for (NotificationItem item : notifications) {
            if (item.isNew()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 添加新通知
     */
    public void addNotification(NotificationItem notification) {
        if (notifications == null) {
            loadNotifications();
        }
        
        // 设置ID
        int maxId = 0;
        for (NotificationItem item : notifications) {
            maxId = Math.max(maxId, item.getId());
        }
        notification.setId(maxId + 1);
        
        // 添加到列表开头
        notifications.add(0, notification);
        
        // 限制通知数量（最多保留50条）
        if (notifications.size() > 50) {
            notifications = notifications.subList(0, 50);
        }
        
        saveNotifications();
        Log.d(TAG, "新通知已添加: " + notification.getTitle());
    }
    
    /**
     * 标记通知为已读
     */
    public void markAsRead(int notificationId) {
        if (notifications == null) {
            loadNotifications();
        }
        
        for (NotificationItem item : notifications) {
            if (item.getId() == notificationId) {
                item.setNew(false);
                saveNotifications();
                Log.d(TAG, "通知已标记为已读: " + item.getTitle());
                break;
            }
        }
    }
    
    /**
     * 标记所有通知为已读
     */
    public void markAllAsRead() {
        if (notifications == null) {
            loadNotifications();
        }
        
        for (NotificationItem item : notifications) {
            item.setNew(false);
        }
        saveNotifications();
        Log.d(TAG, "所有通知已标记为已读");
    }
    
    /**
     * 删除通知
     */
    public void deleteNotification(int notificationId) {
        if (notifications == null) {
            loadNotifications();
        }
        
        notifications.removeIf(item -> item.getId() == notificationId);
        saveNotifications();
        Log.d(TAG, "通知已删除: " + notificationId);
    }
    
    /**
     * 清空所有通知
     */
    public void clearAllNotifications() {
        if (notifications != null) {
            notifications.clear();
            saveNotifications();
            Log.d(TAG, "所有通知已清空");
        }
    }
    
    /**
     * 刷新通知数据 - 模拟从服务器获取
     */
    public void refreshNotifications(NotificationRefreshCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 模拟网络请求延迟
                
                // 模拟可能收到新通知
                if (Math.random() < 0.3) { // 30%概率收到新通知
                    NotificationItem newNotification = createRandomNotification();
                    addNotification(newNotification);
                    
                    if (callback != null) {
                        callback.onSuccess(true); // 有新通知
                    }
                } else {
                    if (callback != null) {
                        callback.onSuccess(false); // 无新通知
                    }
                }
                
            } catch (InterruptedException e) {
                Log.e(TAG, "刷新通知被中断", e);
                if (callback != null) {
                    callback.onFailure("刷新失败");
                }
            }
        }).start();
    }
    
    /**
     * 创建随机通知（用于测试）
     */
    private NotificationItem createRandomNotification() {
        String[] titles = {
            "系统升级完成", "新功能发布", "服务提醒", "活动通知", "安全提醒"
        };
        String[] icons = {"🔔", "✅", "📰", "🎉", "⚠️"};
        String[] classes = {"icon-red", "icon-green", "icon-blue", "icon-yellow", "icon-purple"};
        
        int index = (int) (Math.random() * titles.length);
        
        return new NotificationItem(
            0, // ID会在添加时自动设置
            icons[index],
            classes[index],
            titles[index],
            "这是一条测试通知消息，用于演示通知功能的正常工作。",
            "刚刚",
            true
        );
    }
    
    /**
     * 获取最后更新时间
     */
    public long getLastUpdateTime() {
        return prefs.getLong(KEY_LAST_UPDATE, 0);
    }
    
    /**
     * 通知刷新回调接口
     */
    public interface NotificationRefreshCallback {
        void onSuccess(boolean hasNewNotification);
        void onFailure(String error);
    }
}