package com.xiangjia.locallife.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_feedback")
public class UserFeedback {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String content;        // 反馈内容
    private String type;           // 反馈类型
    private String contact;        // 联系方式
    private long createTime;       // 创建时间
    private String userId;         // 用户ID
    private String status;         // 处理状态：pending/replied/closed
    
    // 构造函数
    public UserFeedback() {}
    
    @Ignore
    public UserFeedback(String content, String type, String contact, String userId) {
        this.content = content;
        this.type = type;
        this.contact = contact;
        this.userId = userId;
        this.createTime = System.currentTimeMillis();
        this.status = "pending";
    }
    
    // 所有getter和setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}