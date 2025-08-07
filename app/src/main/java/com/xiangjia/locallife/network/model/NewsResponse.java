package com.xiangjia.locallife.network.model;

import java.util.List;

public class NewsResponse {
    private String status;
    private List<NewsItem> articles;
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<NewsItem> getArticles() { return articles; }
    public void setArticles(List<NewsItem> articles) { this.articles = articles; }
    
    public static class NewsItem {
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private String publishedAt;
        
        // Getters and setters
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
    }
}