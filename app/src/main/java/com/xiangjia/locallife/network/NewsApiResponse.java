package com.xiangjia.locallife.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * NewsAPI 响应模型
 */
public class NewsApiResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("totalResults")
    private int totalResults;
    
    @SerializedName("articles")
    private List<NewsApiArticle> articles;
    
    @SerializedName("code")
    private String code;
    
    @SerializedName("message")
    private String message;
    
    public boolean isSuccessful() { 
        return "ok".equals(status); 
    }
    
    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
    
    public List<NewsApiArticle> getArticles() { return articles; }
    public void setArticles(List<NewsApiArticle> articles) { this.articles = articles; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}