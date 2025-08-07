package com.xiangjia.locallife.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 新闻条目模型
 * 用于存储新闻数据
 */
@Entity(tableName = "news_items")
public class NewsItem {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String content;
    private String summary;
    private String author;
    private String source;
    private String category;
    private String imageUrl;
    private String publishTime;
    private long createTime;
    private String url;
    private int viewCount;
    private boolean isRead;
    private String tags;
    private String location;
    
    public NewsItem() {}
    
    public NewsItem(String title, String content, String summary, String author, 
                   String source, String category, String publishTime) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.author = author;
        this.source = source;
        this.category = category;
        this.publishTime = publishTime;
        this.createTime = System.currentTimeMillis();
        this.viewCount = 0;
        this.isRead = false;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
