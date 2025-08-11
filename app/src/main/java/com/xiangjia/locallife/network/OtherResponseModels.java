package com.xiangjia.locallife.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 其他API响应模型类
 */

// NewsSourcesResponse
class NewsSourcesResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("sources")
    private List<NewsSource> sources;
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<NewsSource> getSources() { return sources; }
    public void setSources(List<NewsSource> sources) { this.sources = sources; }
}

// GuardianSectionsResponse  
class GuardianSectionsResponse {
    @SerializedName("response")
    private GuardianSectionsWrapper response;
    
    public GuardianSectionsWrapper getResponse() { return response; }
    public void setResponse(GuardianSectionsWrapper response) { this.response = response; }
}

class GuardianSectionsWrapper {
    @SerializedName("status")
    private String status;
    
    @SerializedName("results")
    private List<GuardianSection> results;
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<GuardianSection> getResults() { return results; }
    public void setResults(List<GuardianSection> results) { this.results = results; }
}

class GuardianSection {
    @SerializedName("id")
    private String id;
    
    @SerializedName("webTitle")
    private String webTitle;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getWebTitle() { return webTitle; }
    public void setWebTitle(String webTitle) { this.webTitle = webTitle; }
}

// GNews 响应模型
class GNewsResponse {
    @SerializedName("totalArticles")
    private int totalArticles;
    
    @SerializedName("articles")
    private List<GNewsArticle> articles;
    
    public int getTotalArticles() { return totalArticles; }
    public void setTotalArticles(int totalArticles) { this.totalArticles = totalArticles; }
    
    public List<GNewsArticle> getArticles() { return articles; }
    public void setArticles(List<GNewsArticle> articles) { this.articles = articles; }
}

class GNewsArticle {
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("url")
    private String url;
    
    @SerializedName("image")
    private String image;
    
    @SerializedName("publishedAt")
    private String publishedAt;
    
    @SerializedName("source")
    private GNewsSource source;
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
    
    public GNewsSource getSource() { return source; }
    public void setSource(GNewsSource source) { this.source = source; }
}

class GNewsSource {
    @SerializedName("name")
    private String name;
    
    @SerializedName("url")
    private String url;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}