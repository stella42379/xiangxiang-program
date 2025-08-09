package com.xiangjia.locallife.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.UUID;

/**
 * 论坛消息模型类
 * 实现Task2设计要求中的Message类
 */
@Entity(tableName = "forum_messages")
public class ForumMessage {
    @PrimaryKey
    @NonNull
    private String messageId;        // 使用UUID作为唯一标识符
    
    private String postId;           // 所属帖子ID
    private String authorId;         // 作者用户ID
    private String authorName;       // 作者用户名
    private String content;          // 消息内容
    private long timestamp;          // 创建时间戳
    private int likeCount;           // 点赞数
    private String status;           // 消息状态 (active, deleted, hidden)
    private String parentMessageId; // 父消息ID (用于回复功能)
    private String imageUrl;         // 图片URL (可选)
    private boolean isAuthorReply;   // 是否为楼主回复
    
    // Room使用的无参构造函数
    public ForumMessage() {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.likeCount = 0;
        this.status = "active";
        this.isAuthorReply = false;
    }
    
    // 应用使用的带参构造函数
    @Ignore
    public ForumMessage(String postId, String authorId, String authorName, String content) {
        this();
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
    }
    
    // Getter和Setter方法
    @NonNull
    public String getMessageId() { return messageId; }
    public void setMessageId(@NonNull String messageId) { this.messageId = messageId; }
    
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getParentMessageId() { return parentMessageId; }
    public void setParentMessageId(String parentMessageId) { this.parentMessageId = parentMessageId; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isAuthorReply() { return isAuthorReply; }
    public void setAuthorReply(boolean authorReply) { isAuthorReply = authorReply; }
    
    /**
     * 增加点赞数
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    /**
     * 减少点赞数
     */
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
    
    /**
     * 检查是否为回复消息
     */
    public boolean isReply() {
        return parentMessageId != null && !parentMessageId.isEmpty();
    }
}