package com.xiangjia.locallife.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.xiangjia.locallife.model.AIConversation;
import com.xiangjia.locallife.model.UserAddress;
import com.xiangjia.locallife.model.UserFeedback;

/**
 * 湘湘管家精简数据库
 * 只包含必要的3个表
 */
@Database(
    entities = {
        AIConversation.class,
        UserAddress.class,
        UserFeedback.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "xiangjia_database";
    private static volatile AppDatabase INSTANCE;
    
    // DAO接口
    public abstract AIConversationDao aiConversationDao();
    public abstract UserAddressDao userAddressDao();
    public abstract UserFeedbackDao userFeedbackDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
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
}