package com.xiangjia.locallife.datastructure;

import com.xiangjia.locallife.model.ForumPost;

/**
 * 帖子搜索键 - 用于AVL树中快速查找帖子
 * 支持按时间戳、点赞数、回复数等排序
 */
public class PostSearchKey implements Comparable<PostSearchKey> {
    
    public enum SortType {
        TIMESTAMP,      // 按时间排序
        LIKE_COUNT,     // 按点赞数排序
        REPLY_COUNT,    // 按回复数排序
        VIEW_COUNT      // 按浏览数排序
    }
    
    private final String postId;
    private final long timestamp;
    private final int likeCount;
    private final int replyCount;
    private final int viewCount;
    private final SortType sortType;
    
    /**
     * 从ForumPost创建搜索键
     */
    public PostSearchKey(ForumPost post, SortType sortType) {
        this.postId = post.getPostId();
        this.timestamp = post.getTimestamp();
        this.likeCount = post.getLikeCount();
        this.replyCount = post.getReplyCount();
        this.viewCount = post.getViewCount();
        this.sortType = sortType;
    }
    
    /**
     * 手动创建搜索键
     */
    public PostSearchKey(String postId, long timestamp, int likeCount, int replyCount, int viewCount, SortType sortType) {
        this.postId = postId;
        this.timestamp = timestamp;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.viewCount = viewCount;
        this.sortType = sortType;
    }
    
    @Override
    public int compareTo(PostSearchKey other) {
        if (this.sortType != other.sortType) {
            throw new IllegalArgumentException("Cannot compare PostSearchKeys with different sort types");
        }
        
        int result;
        switch (sortType) {
            case TIMESTAMP:
                result = Long.compare(this.timestamp, other.timestamp);
                break;
            case LIKE_COUNT:
                result = Integer.compare(this.likeCount, other.likeCount);
                break;
            case REPLY_COUNT:
                result = Integer.compare(this.replyCount, other.replyCount);
                break;
            case VIEW_COUNT:
                result = Integer.compare(this.viewCount, other.viewCount);
                break;
            default:
                result = this.postId.compareTo(other.postId);
        }
        
        // 如果主要排序字段相等，用postId作为tie-breaker
        if (result == 0) {
            result = this.postId.compareTo(other.postId);
        }
        
        return result;
    }
    
    // Getters
    public String getPostId() { 
        return postId; 
    }
    
    public long getTimestamp() { 
        return timestamp; 
    }
    
    public int getLikeCount() { 
        return likeCount; 
    }
    
    public int getReplyCount() { 
        return replyCount; 
    }
    
    public int getViewCount() { 
        return viewCount; 
    }
    
    public SortType getSortType() { 
        return sortType; 
    }
    
    /**
     * 获取排序值
     */
    public long getSortValue() {
        switch (sortType) {
            case TIMESTAMP:
                return timestamp;
            case LIKE_COUNT:
                return likeCount;
            case REPLY_COUNT:
                return replyCount;
            case VIEW_COUNT:
                return viewCount;
            default:
                return timestamp;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostSearchKey that = (PostSearchKey) obj;
        return postId.equals(that.postId);
    }
    
    @Override
    public int hashCode() {
        return postId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("PostSearchKey{id='%s', sort=%s, value=%d}", 
                           postId, sortType, getSortValue());
    }
    
    /**
     * 创建时间排序键
     */
    public static PostSearchKey byTimestamp(ForumPost post) {
        return new PostSearchKey(post, SortType.TIMESTAMP);
    }
    
    /**
     * 创建点赞数排序键
     */
    public static PostSearchKey byLikeCount(ForumPost post) {
        return new PostSearchKey(post, SortType.LIKE_COUNT);
    }
    
    /**
     * 创建回复数排序键
     */
    public static PostSearchKey byReplyCount(ForumPost post) {
        return new PostSearchKey(post, SortType.REPLY_COUNT);
    }
    
    /**
     * 创建浏览数排序键
     */
    public static PostSearchKey byViewCount(ForumPost post) {
        return new PostSearchKey(post, SortType.VIEW_COUNT);
    }
}