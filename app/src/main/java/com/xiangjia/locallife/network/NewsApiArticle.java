package com.xiangjia.locallife.network;

import com.google.gson.annotations.SerializedName;

/**
 * NewsAPI 文章模型
 */
public class NewsApiArticle {
    @SerializedName("source")
    private NewsSource source;
    
    @SerializedName("author")
    private String author;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("url")
    private String url;
    
    @SerializedName("urlToImage")
    private String urlToImage;
    
    @SerializedName("publishedAt")
    private String publishedAt;
    
    @SerializedName("content")
    private String content;
    
    // Getters and Setters
    public NewsSource getSource() { return source; }
    public void setSource(NewsSource source) { this.source = source; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getUrlToImage() { return urlToImage; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }
    
    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}