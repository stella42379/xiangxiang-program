package com.xiangjia.locallife.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "chat_messages")
public class ChatMessage {
    
    @PrimaryKey
    @NonNull
    public String id;
    
    public String content;
    public boolean isFromUser;
    public long timestamp;
    public String conversationId;
    
    public ChatMessage() {}
    
    public ChatMessage(String id, String content, boolean isFromUser, long timestamp, String conversationId) {
        this.id = id;
        this.content = content;
        this.isFromUser = isFromUser;
        this.timestamp = timestamp;
        this.conversationId = conversationId;
    }
}