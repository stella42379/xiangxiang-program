package com.xiangjia.locallife.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xiangjia.locallife.model.AIConversation;

import java.util.List;

/**
 * AI对话DAO接口
 * 定义AI对话相关的数据库操作
 */
@Dao
public interface AIConversationDao {
    
    /**
     * 插入对话记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AIConversation conversation);
    
    /**
     * 批量插入对话记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AIConversation> conversations);
    
    /**
     * 更新对话记录
     */
    @Update
    void update(AIConversation conversation);
    
    /**
     * 删除对话记录
     */
    @Delete
    void delete(AIConversation conversation);
    
    /**
     * 根据ID删除对话记录
     */
    @Query("DELETE FROM ai_conversations WHERE id = :id")
    void deleteById(int id);
    
    /**
     * 删除用户的所有对话记录
     */
    @Query("DELETE FROM ai_conversations WHERE userId = :userId")
    void deleteByUserId(String userId);
    
    /**
     * 根据ID获取对话记录
     */
    @Query("SELECT * FROM ai_conversations WHERE id = :id")
    AIConversation getById(int id);
    
    /**
     * 获取用户的所有对话记录
     */
    @Query("SELECT * FROM ai_conversations WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<AIConversation>> getByUserId(String userId);
    
    /**
     * 获取用户最近的对话记录
     */
    @Query("SELECT * FROM ai_conversations WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    List<AIConversation> getRecentByUserId(String userId, int limit);
    
    /**
     * 获取所有对话记录
     */
    @Query("SELECT * FROM ai_conversations ORDER BY timestamp DESC")
    LiveData<List<AIConversation>> getAll();
    
    /**
     * 获取对话记录总数
     */
    @Query("SELECT COUNT(*) FROM ai_conversations")
    int getCount();
    
    /**
     * 获取用户的对话记录总数
     */
    @Query("SELECT COUNT(*) FROM ai_conversations WHERE userId = :userId")
    int getCountByUserId(String userId);
}
