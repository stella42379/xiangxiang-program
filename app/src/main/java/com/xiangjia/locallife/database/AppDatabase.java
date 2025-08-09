package com.xiangjia.locallife.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// 原有的模型
import com.xiangjia.locallife.model.AIConversation;
import com.xiangjia.locallife.model.UserAddress;
import com.xiangjia.locallife.model.UserFeedback;

// 新增的论坛相关模型
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.ForumMessage;
import com.xiangjia.locallife.model.User;

/**
 * 湘湘管家数据库 - 新增论坛功能
 * 包含原有的3个表 + 新增的3个论坛表
 */
@Database(
    entities = {
        // 原有的实体类
        AIConversation.class,
        UserAddress.class,
        UserFeedback.class,
        // 新增的论坛相关实体类
        ForumPost.class,
        ForumMessage.class,
        User.class
    },
    version = 2, // 版本号从1升级到2
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "xiangjia_database";
    private static volatile AppDatabase INSTANCE;
    
    // 原有的DAO接口
    public abstract AIConversationDao aiConversationDao();
    public abstract UserAddressDao userAddressDao();
    public abstract UserFeedbackDao userFeedbackDao();
    
    // 新增的论坛相关DAO接口
    public abstract ForumPostDao forumPostDao();
    public abstract ForumMessageDao forumMessageDao();
    public abstract UserDao userDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration() // 简化迁移处理
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    public static AppDatabase getDatabase(Context context) {
        return getInstance(context); // 如果你已有 getInstance
    }
    
}