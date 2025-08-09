package com.xiangjia.locallife.util;

import android.content.Context;
import android.content.Intent;
import com.xiangjia.locallife.ui.activity.DataInitActivity;
import com.xiangjia.locallife.ui.activity.ForumActivity;
import com.xiangjia.locallife.database.AppDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 论坛启动器
 * 用于检查数据状态并启动相应页面
 */
public class ForumLauncher {
    
    /**
     * 启动论坛应用
     * 会检查数据状态，如果没有数据则跳转到初始化页面，否则直接进入论坛
     */
    public static void launch(Context context) {
        // 检查是否有现有数据
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                AppDatabase database = AppDatabase.getInstance(context);
                int userCount = database.userDao().getTotalUserCount();
                int postCount = database.forumPostDao().getTotalPostCount();
                
                Intent intent;
                if (userCount > 0 || postCount > 0) {
                    // 有数据，直接进入论坛
                    intent = new Intent(context, ForumActivity.class);
                } else {
                    // 无数据，进入初始化页面
                    intent = new Intent(context, DataInitActivity.class);
                }
                
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                
            } catch (Exception e) {
                e.printStackTrace();
                // 出错时默认进入初始化页面
                Intent intent = new Intent(context, DataInitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } finally {
                executor.shutdown();
            }
        });
    }
    
    /**
     * 直接启动论坛主页（不检查数据）
     */
    public static void launchForum(Context context) {
        Intent intent = new Intent(context, ForumActivity.class);
        context.startActivity(intent);
    }
    
    /**
     * 启动数据初始化页面
     */
    public static void launchDataInit(Context context) {
        Intent intent = new Intent(context, DataInitActivity.class);
        context.startActivity(intent);
    }
}