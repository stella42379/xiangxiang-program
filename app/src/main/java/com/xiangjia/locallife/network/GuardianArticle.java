package com.xiangjia.locallife.network;

import com.google.gson.annotations.SerializedName;

/**
 * Guardian 文章模型
 */
public class GuardianArticle {
    @SerializedName("id")
    private String id;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("sectionId")
    private String sectionId;
    
    @SerializedName("sectionName")
    private String sectionName;
    
    @SerializedName("webPublicationDate")
    private String webPublicationDate;
    
    @SerializedName("webTitle")
    private String webTitle;
    
    @SerializedName("webUrl")
    private String webUrl;
    
    @SerializedName("apiUrl")
    private String apiUrl;
    
    @SerializedName("fields")
    private GuardianFields fields;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getSectionId() { return sectionId; }
    public void setSectionId(String sectionId) { this.sectionId = sectionId; }
    
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    
    public String getWebPublicationDate() { return webPublicationDate; }
    public void setWebPublicationDate(String webPublicationDate) { this.webPublicationDate = webPublicationDate; }
    
    public String getWebTitle() { return webTitle; }
    public void setWebTitle(String webTitle) { this.webTitle = webTitle; }
    
    public String getWebUrl() { return webUrl; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }
    
    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
    
    public GuardianFields getFields() { return fields; }
    public void setFields(GuardianFields fields) { this.fields = fields; }
}

class GuardianFields {
    @SerializedName("headline")
    private String headline;
    
    @SerializedName("thumbnail")
    private String thumbnail;
    
    @SerializedName("shortUrl")
    private String shortUrl;
    
    @SerializedName("body")
    private String body;
    
    // Getters and Setters
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    
    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}