package com.xiangjia.locallife.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Guardian API 响应模型
 */
public class GuardianResponse {
    @SerializedName("response")
    private GuardianResponseWrapper response;
    
    public GuardianResponseWrapper getResponse() { return response; }
    public void setResponse(GuardianResponseWrapper response) { this.response = response; }
}

class GuardianResponseWrapper {
    @SerializedName("status")
    private String status;
    
    @SerializedName("total")
    private int total;
    
    @SerializedName("pages")
    private int pages;
    
    @SerializedName("currentPage")
    private int currentPage;
    
    @SerializedName("results")
    private List<GuardianArticle> results;
    
    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    
    public List<GuardianArticle> getResults() { return results; }
    public void setResults(List<GuardianArticle> results) { this.results = results; }
}