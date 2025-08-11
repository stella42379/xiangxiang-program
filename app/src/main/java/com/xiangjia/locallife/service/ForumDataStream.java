package com.xiangjia.locallife.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.database.ForumMessageDao;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.ForumMessage;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 论坛数据流管理器 - Observer Pattern实现
 * 负责定期更新数据并通知观察者
 */
public class ForumDataStream {
    private static final String TAG = "ForumDataStream";
    private static final long UPDATE_INTERVAL = 30; // 30秒更新一次
    
    private static ForumDataStream instance;
    private final ScheduledExecutorService scheduler;
    private final Handler mainHandler;
    private final ForumPostDao postDao;
    private final ForumMessageDao messageDao;
    
    // Observer Pattern - 观察者列表
    private final java.util.Set<DataStreamObserver> observers = new java.util.HashSet<>();
    
    private boolean isRunning = false;
    
    private ForumDataStream(AppDatabase database) {
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.postDao = database.forumPostDao();
        this.messageDao = database.forumMessageDao();
    }
    
    /**
     * Singleton Pattern - 获取实例
     */
    public static synchronized ForumDataStream getInstance(AppDatabase database) {
        if (instance == null) {
            instance = new ForumDataStream(database);
        }
        return instance;
    }
    
    /**
     * Observer Pattern - 添加观察者
     */
    public void addObserver(DataStreamObserver observer) {
        synchronized (observers) {
            observers.add(observer);
            Log.d(TAG, "添加观察者，当前观察者数量: " + observers.size());
        }
    }
    
    /**
     * Observer Pattern - 移除观察者
     */
    public void removeObserver(DataStreamObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
            Log.d(TAG, "移除观察者，当前观察者数量: " + observers.size());
        }
    }
    
    /**
     * 通知所有观察者
     */
    private void notifyObservers(DataStreamEvent event) {
        mainHandler.post(() -> {
            synchronized (observers) {
                for (DataStreamObserver observer : observers) {
                    try {
                        observer.onDataUpdated(event);
                    } catch (Exception e) {
                        Log.e(TAG, "通知观察者失败", e);
                    }
                }
            }
        });
    }
    
    /**
     * 开始数据流
     */
    public void startDataStream() {
        if (isRunning) {
            Log.d(TAG, "数据流已在运行中");
            return;
        }
        
        isRunning = true;
        Log.d(TAG, "启动数据流，更新间隔: " + UPDATE_INTERVAL + "秒");
        
        // 立即更新一次
        updateAllData();
        
        // 定期更新
        scheduler.scheduleAtFixedRate(this::updateAllData, 
            UPDATE_INTERVAL, UPDATE_INTERVAL, TimeUnit.SECONDS);
    }
    
    /**
     * 停止数据流
     */
    public void stopDataStream() {
        if (!isRunning) return;
        
        isRunning = false;
        scheduler.shutdown();
        Log.d(TAG, "数据流已停止");
        
        // 通知观察者数据流已停止
        notifyObservers(new DataStreamEvent(
            DataStreamEvent.Type.STREAM_STOPPED,
            null,
            "数据流已停止"
        ));
    }
    
    /**
     * 更新所有数据
     */
    private void updateAllData() {
        Log.d(TAG, "开始更新数据流数据");
        
        // 异步更新帖子数据
        scheduler.execute(this::updatePosts);
        
        // 异步更新消息数据
        scheduler.execute(this::updateMessages);
    }
    
    /**
     * 更新帖子数据
     */
    private void updatePosts() {
        try {
            List<ForumPost> latestPosts = postDao.getLatestPosts(50);
            
            DataStreamEvent event = new DataStreamEvent(
                DataStreamEvent.Type.POSTS_UPDATED,
                latestPosts,
                "帖子数据已更新，共" + latestPosts.size() + "条"
            );
            
            notifyObservers(event);
            Log.d(TAG, "帖子数据更新完成: " + latestPosts.size() + "条");
            
        } catch (Exception e) {
            Log.e(TAG, "更新帖子数据失败", e);
            notifyObservers(new DataStreamEvent(
                DataStreamEvent.Type.ERROR,
                null,
                "帖子数据更新失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 更新消息数据
     */
    private void updateMessages() {
        try {
            List<ForumMessage> latestMessages = messageDao.getLatestMessages(100);
            
            DataStreamEvent event = new DataStreamEvent(
                DataStreamEvent.Type.MESSAGES_UPDATED,
                latestMessages,
                "消息数据已更新，共" + latestMessages.size() + "条"
            );
            
            notifyObservers(event);
            Log.d(TAG, "消息数据更新完成: " + latestMessages.size() + "条");
            
        } catch (Exception e) {
            Log.e(TAG, "更新消息数据失败", e);
            notifyObservers(new DataStreamEvent(
                DataStreamEvent.Type.ERROR,
                null,
                "消息数据更新失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 手动触发数据更新
     */
    public void forceUpdate() {
        Log.d(TAG, "手动触发数据更新");
        scheduler.execute(this::updateAllData);
    }
    
    /**
     * 检查是否正在运行
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * 获取观察者数量
     */
    public int getObserverCount() {
        synchronized (observers) {
            return observers.size();
        }
    }
    
    /**
     * Observer Pattern - 观察者接口
     */
    public interface DataStreamObserver {
        void onDataUpdated(DataStreamEvent event);
    }
    
    /**
     * 数据流事件
     */
    public static class DataStreamEvent {
        public enum Type {
            POSTS_UPDATED,      // 帖子更新
            MESSAGES_UPDATED,   // 消息更新
            STREAM_STOPPED,     // 数据流停止
            ERROR               // 错误
        }
        
        private final Type type;
        private final Object data;
        private final String message;
        private final long timestamp;
        
        public DataStreamEvent(Type type, Object data, String message) {
            this.type = type;
            this.data = data;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public Type getType() { 
            return type; 
        }
        
        public Object getData() { 
            return data; 
        }
        
        public String getMessage() { 
            return message; 
        }
        
        public long getTimestamp() { 
            return timestamp; 
        }
        
        @Override
        public String toString() {
            return String.format("DataStreamEvent{type=%s, message='%s', time=%d}", 
                               type, message, timestamp);
        }
    }
}