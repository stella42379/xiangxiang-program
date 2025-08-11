package com.xiangjia.locallife.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.xiangjia.locallife.database.entity.ChatMessage;

import java.util.List;

@Dao
public interface ChatMessageDao {
    
    @Insert
    void insert(ChatMessage message);
    
    @Insert
    void insertAll(List<ChatMessage> messages);
    
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    List<ChatMessage> getMessagesByConversation(String conversationId);
    
    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC LIMIT 1")
    ChatMessage getLastMessage();
    
    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    void deleteByConversation(String conversationId);
    
    @Query("DELETE FROM chat_messages WHERE timestamp < :timestamp")
    void deleteOldMessages(long timestamp);
    
    @Query("SELECT COUNT(*) FROM chat_messages WHERE conversationId = :conversationId")
    int getMessageCount(String conversationId);
    
    @Query("SELECT DISTINCT conversationId FROM chat_messages ORDER BY timestamp DESC")
    List<String> getAllConversationIds();
    
    @Delete
    void delete(ChatMessage message);
    
    @Query("DELETE FROM chat_messages")
    void deleteAll();
}