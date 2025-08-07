package com.xiangjia.locallife;

import android.app.Application;
import android.content.Context;

/**
 * 香家生活应用入口类
 * 负责应用级别的初始化和配置
 */
public class XiangJiaApplication extends Application {
    
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        // 初始化应用配置
        initApplication();
    }
    
    /**
     * 初始化应用配置
     */
    private void initApplication() {
        // 初始化数据库
        // 初始化网络配置
        // 初始化定位服务
        // 初始化语音服务
    }
    
    /**
     * 获取应用上下文
     */
    public static Context getAppContext() {
        return context;
    }
}
