package com.xiangjia.locallife.model;

/**
 * 新闻条目模型 - 统一版本
 */
public class NewsItem {
    private int id;
    private String title;
    private String source;
    private String url;
    private String thumbnail;
    private String time;
    private String category;
    private String content;
    private boolean isCollected;

    // 默认构造函数
    public NewsItem() {}

    // 主要构造函数 - 与 NewsDataGenerator 兼容
    public NewsItem(int id, String title, String source, String url, String thumbnail, String time, String category) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.url = url;
        this.thumbnail = thumbnail;
        this.time = time;
        this.category = category;
        this.isCollected = false;
    }

    // 简化构造函数
    public NewsItem(String title, String source, String time) {
        this.title = title;
        this.source = source;
        this.time = time;
        this.isCollected = false;
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

    public String getSource() { 
        return source; 
    }
    
    public void setSource(String source) { 
        this.source = source; 
    }

    public String getUrl() { 
        return url; 
    }
    
    public void setUrl(String url) { 
        this.url = url; 
    }

    public String getThumbnail() { 
        return thumbnail; 
    }
    
    public void setThumbnail(String thumbnail) { 
        this.thumbnail = thumbnail; 
    }

    public String getTime() { 
        return time; 
    }
    
    public void setTime(String time) { 
        this.time = time; 
    }

    public String getCategory() { 
        return category; 
    }
    
    public void setCategory(String category) { 
        this.category = category; 
    }

    public String getContent() { 
        return content; 
    }
    
    public void setContent(String content) { 
        this.content = content; 
    }

    public boolean isCollected() { 
        return isCollected; 
    }
    
    public void setCollected(boolean collected) { 
        isCollected = collected; 
    }
    
    // 添加兼容方法，方便其他代码使用
    public String getImageUrl() {
        return thumbnail;
    }
    
    public void setImageUrl(String imageUrl) {
        this.thumbnail = imageUrl;
    }
    
    public String getPublishTime() {
        return time;
    }
    
    public void setPublishTime(String publishTime) {
        this.time = publishTime;
    }
}