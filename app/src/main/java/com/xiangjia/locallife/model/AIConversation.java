package com.xiangjia.locallife.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ai_conversations")
public class AIConversation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String sessionId;      // 会话ID
    private String message;        // 用户消息
    private String response;       // AI回复
    private String messageType;    // 消息类型：user/ai
    private long timestamp;        // 时间戳
    private String userId;         // 用户ID
    
    // 构造函数和Getter/Setter方法
    public AIConversation() {}
    
    @Ignore
    public AIConversation(String sessionId, String message, String response, String messageType, long timestamp, String userId) {
        this.sessionId = sessionId;
        this.message = message;
        this.response = response;
        this.messageType = messageType;
        this.timestamp = timestamp;
        this.userId = userId;
    }
    
    // 所有getter和setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}