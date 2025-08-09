package com.xiangjia.locallife.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.UUID;

/**
 * 论坛帖子模型类
 * 实现Task2设计要求中的Post类
 */
@Entity(tableName = "forum_posts")
public class ForumPost {
    @PrimaryKey
    @NonNull
    private String postId;           // 使用UUID作为唯一标识符
    
    private String authorId;         // 作者用户ID
    private String authorName;       // 作者用户名
    private String title;            // 帖子标题
    private String content;          // 帖子内容
    private String category;         // 帖子分类 (discussion, friends, help, etc.)
    private long timestamp;          // 创建时间戳
    private long lastActivityTime;   // 最后活动时间(用于排序)
    private int likeCount;           // 点赞数
    private int replyCount;          // 回复数
    private int viewCount;           // 浏览数
    private String status;           // 帖子状态 (active, deleted, hidden)
    private String tags;             // 标签 (JSON格式存储)
    private boolean isPinned;        // 是否置顶
    private String imageUrl;         // 图片URL (可选)
    
    // Room使用的无参构造函数
    public ForumPost() {
        this.postId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.lastActivityTime = this.timestamp;
        this.likeCount = 0;
        this.replyCount = 0;
        this.viewCount = 0;
        this.status = "active";
        this.isPinned = false;
    }
    
    // 应用使用的带参构造函数
    @Ignore
    public ForumPost(String authorId, String authorName, String title, String content, String category) {
        this();
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.category = category;
    }
    
    // Getter和Setter方法
    @NonNull
    public String getPostId() { return postId; }
    public void setPostId(@NonNull String postId) { this.postId = postId; }
    
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public long getLastActivityTime() { return lastActivityTime; }
    public void setLastActivityTime(long lastActivityTime) { this.lastActivityTime = lastActivityTime; }
    
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    
    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }
    
    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    /**
     * 增加点赞数
     */
    public void incrementLikeCount() {
        this.likeCount++;
        updateLastActivityTime();
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
     * 增加回复数
     */
    public void incrementReplyCount() {
        this.replyCount++;
        updateLastActivityTime();
    }
    
    /**
     * 增加浏览数
     */
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    /**
     * 更新最后活动时间
     */
    public void updateLastActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }
}