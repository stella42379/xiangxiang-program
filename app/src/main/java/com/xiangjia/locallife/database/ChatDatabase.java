package com.xiangjia.locallife.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xiangjia.locallife.database.dao.ChatMessageDao;
import com.xiangjia.locallife.database.entity.ChatMessage;

@Database(
    entities = {ChatMessage.class},
    version = 1,
    exportSchema = false
)
public abstract class ChatDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "chat_database";
    private static volatile ChatDatabase INSTANCE;
    
    public abstract ChatMessageDao chatMessageDao();
    
    public static ChatDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ChatDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ChatDatabase.class,
                            DATABASE_NAME
                    )
                    .addCallback(roomCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            // 数据库创建时的初始化操作
        }
    };
    
    // 如果需要数据库迁移，可以在这里添加Migration
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 数据库迁移逻辑
        }
    };
}