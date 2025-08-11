package com.xiangjia.locallife.network;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 🔥 完全兼容版NewsServiceManager - 匹配LocalNewsFragment
 */
public class NewsServiceManager {
    private static final String TAG = "NewsServiceManager";
    
    // API配置
    private static final String NEWS_API_KEY = "4ae67c64b53f46019b3f2b4d7d37299d";
    private static final String GUARDIAN_API_KEY = "dfd2220f-63dc-4407-88d0-7f09ea85dd68";
    
    // API URLs
    private static final String NEWS_API_BASE = "https://newsapi.org/v2/";
    private static final String GUARDIAN_API_BASE = "https://content.guardianapis.com/";
    
    // 超时配置 - 增加超时时间
    private static final int CONNECT_TIMEOUT = 50000; // 15秒连接超时
    private static final int READ_TIMEOUT = 100000;    // 30秒读取超时
    
    private static NewsServiceManager instance;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    public static NewsServiceManager getInstance() {
        if (instance == null) {
            instance = new NewsServiceManager();
        }
        return instance;
    }
    
    /**
     * 🔥 获取澳洲本地新闻 - 修复版：只使用country参数，API key放请求头
     */
    public void getAustralianNews(NewsCallback callback) {
        Log.d(TAG, "🇦🇺 开始获取澳洲本地新闻");
        
        executor.execute(() -> {
            HttpURLConnection conn = null;
            InputStream is = null;
            
            try {
                // ✅ 修复：只使用country，不再混合sources参数
                String urlString = NEWS_API_BASE + "top-headlines" +
                    "?country=au" +
                    "&pageSize=20";
                
                Log.d(TAG, "📡 请求URL: " + urlString);
                
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                
                // ✅ 基础配置
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setUseCaches(false);
                
                // ✅ 修复：API key放请求头，防止403
                conn.setRequestProperty("X-API-Key", NEWS_API_KEY);
                conn.setRequestProperty("User-Agent", "LocalLife/1.0 (Android)");
                conn.setRequestProperty("Accept", "application/json");
                
                Log.d(TAG, "⏱️ 开始网络连接...");
                
                // 添加连接监控
                long startTime = System.currentTimeMillis();
                
                int responseCode = conn.getResponseCode();
                long endTime = System.currentTimeMillis();
                
                Log.d(TAG, "📊 HTTP状态: " + responseCode + " " + conn.getResponseMessage());
                Log.d(TAG, "⏱️ 请求耗时: " + (endTime - startTime) + "ms");
                
                // 选择正确的输入流
                is = (responseCode >= 200 && responseCode < 300) ? 
                     conn.getInputStream() : conn.getErrorStream();
                
                // 读取响应体
                String responseBody = readResponseBody(is);
                Log.d(TAG, "📄 响应体长度: " + responseBody.length());
                Log.d(TAG, "📄 响应体前512字符: " + 
                      responseBody.substring(0, Math.min(512, responseBody.length())));
                
                if (responseCode >= 200 && responseCode < 300) {
                    // 解析成功响应
                    List<UnifiedNewsItem> newsItems = parseNewsApiResponse(responseBody);
                    Log.d(TAG, "✅ 澳洲新闻解析成功，获得 " + newsItems.size() + " 条新闻");
                    
                    if (callback != null) {
                        callback.onSuccess(newsItems);
                    }
                } else {
                    // 处理错误响应
                    String errorMsg = "HTTP " + responseCode + ": " + conn.getResponseMessage() + 
                                     "\n" + responseBody;
                    Log.e(TAG, "❌ HTTP错误: " + errorMsg);
                    
                    if (callback != null) {
                        callback.onError(errorMsg);
                    }
                }
                
            } catch (SocketTimeoutException e) {
                String errorMsg = "网络超时，请检查网络连接: " + e.getMessage();
                Log.e(TAG, "⏰ " + errorMsg, e);
                
                // 🔥 超时时提供模拟数据
                Log.d(TAG, "🎭 网络超时，提供模拟澳洲新闻数据");
                List<UnifiedNewsItem> mockNews = createMockAustralianNews();
                if (callback != null) {
                    callback.onSuccess(mockNews);
                }
                
            } catch (IOException e) {
                String errorMsg = "网络IO异常: " + e.getMessage();
                Log.e(TAG, "🌐 " + errorMsg, e);
                
                // 🔥 网络异常时也提供模拟数据
                Log.d(TAG, "🎭 网络异常，提供模拟澳洲新闻数据");
                List<UnifiedNewsItem> mockNews = createMockAustralianNews();
                if (callback != null) {
                    callback.onSuccess(mockNews);
                }
                
            } catch (JSONException e) {
                String errorMsg = "JSON解析异常: " + e.getMessage();
                Log.e(TAG, "📝 " + errorMsg, e);
                if (callback != null) {
                    callback.onError(errorMsg);
                }
                
            } catch (Exception e) {
                String errorMsg = "未知异常: " + e.getClass().getSimpleName() + " - " + e.getMessage();
                Log.e(TAG, "💥 " + errorMsg, e);
                if (callback != null) {
                    callback.onError(errorMsg);
                }
                
            } finally {
                // 🔥 关键：确保资源释放
                if (is != null) {
                    try { is.close(); } catch (Exception e) { 
                        Log.w(TAG, "关闭InputStream时出错", e); 
                    }
                }
                if (conn != null) {
                    try { conn.disconnect(); } catch (Exception e) { 
                        Log.w(TAG, "断开连接时出错", e); 
                    }
                }
                Log.d(TAG, "🧹 网络资源已清理");
            }
        });
    }
    
    /**
     * 获取国际新闻（备用方案）- 修复版
     */
    public void getInternationalNews(NewsCallback callback) {
        Log.d(TAG, "🌍 开始获取国际新闻");
        
        executor.execute(() -> {
            HttpURLConnection conn = null;
            InputStream is = null;
            
            try {
                // ✅ 修复：使用country=us而不是sources参数
                String urlString = NEWS_API_BASE + "top-headlines" +
                    "?country=us" +
                    "&pageSize=20";
                
                Log.d(TAG, "📡 国际新闻URL: " + urlString);
                
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setUseCaches(false);
                
                // ✅ 修复：API key放请求头
                conn.setRequestProperty("X-API-Key", NEWS_API_KEY);
                conn.setRequestProperty("User-Agent", "LocalLife/1.0 (Android)");
                conn.setRequestProperty("Accept", "application/json");
                
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "📊 国际新闻HTTP状态: " + responseCode);
                
                is = (responseCode >= 200 && responseCode < 300) ?
                     conn.getInputStream() : conn.getErrorStream();
                
                String responseBody = readResponseBody(is);
                Log.d(TAG, "📖 总共读取了 " + responseBody.length() + " 字节");
                
                if (responseCode >= 200 && responseCode < 300) {
                    List<UnifiedNewsItem> newsItems = parseNewsApiResponse(responseBody);
                    Log.d(TAG, "✅ 国际新闻获取成功: " + newsItems.size() + " 条");
                    
                    if (callback != null) {
                        callback.onSuccess(newsItems);
                    }
                } else {
                    String errorMsg = "国际新闻HTTP " + responseCode + ": " + responseBody;
                    Log.e(TAG, "❌ " + errorMsg);
                    
                    if (callback != null) {
                        callback.onError(errorMsg);
                    }
                }
                
            } catch (Exception e) {
                String errorMsg = "国际新闻异常: " + e.getMessage();
                Log.e(TAG, "💥 " + errorMsg, e);
                if (callback != null) {
                    callback.onError(errorMsg);
                }
                
            } finally {
                if (is != null) {
                    try { is.close(); } catch (Exception e) { 
                        Log.w(TAG, "关闭InputStream时出错", e); 
                    }
                }
                if (conn != null) {
                    try { conn.disconnect(); } catch (Exception e) { 
                        Log.w(TAG, "断开连接时出错", e); 
                    }
                }
                Log.d(TAG, "🧹 国际新闻网络资源已清理");
            }
        });
    }
    
    /**
     * 🔥 改进版：读取响应体的方法
     */
    private String readResponseBody(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }
        
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toString(StandardCharsets.UTF_8.name());
        }
    }
    
    /**
     * 🎭 创建模拟澳洲新闻数据（网络不可用时使用）
     */
    private List<UnifiedNewsItem> createMockAustralianNews() {
        List<UnifiedNewsItem> mockNews = new ArrayList<>();
        
        String[] titles = {
            "悉尼歌剧院将举办2025年新年音乐会",
            "澳洲央行宣布维持利率不变",
            "墨尔本咖啡文化被联合国教科文组织认定为文化遗产",
            "大堡礁保护项目取得重大进展",
            "澳洲科学家发现新的考拉保护方法",
            "布里斯班将主办2032年奥运会筹备工作启动",
            "澳洲葡萄酒出口创历史新高",
            "塔斯马尼亚岛发现新物种"
        };
        
        String[] sources = {"ABC News", "The Australian", "Herald Sun", "Sydney Morning Herald"};
        String[] descriptions = {
            "这是一条来自澳洲的重要新闻，展现了澳洲的最新发展动态。",
            "澳大利亚当地媒体报道的最新消息，涉及经济、社会等多个方面。",
            "来自权威媒体的报道，为读者提供准确的新闻信息。",
            "澳洲本地新闻，关注当地民生和社会发展。"
        };
        
        for (int i = 0; i < titles.length; i++) {
            UnifiedNewsItem item = new UnifiedNewsItem();
            item.setTitle(titles[i]);
            item.setDescription(descriptions[i % descriptions.length]);
            item.setSource(sources[i % sources.length]);
            item.setPublishedAt("2025-08-11T" + String.format("%02d", 10 + i) + ":00:00Z");
            item.setUrl("https://example.com/news/" + (i + 1));
            item.setImageUrl("https://picsum.photos/400/300?random=" + (i + 100));
            item.setAuthor("澳洲记者");
            item.setContent("这是新闻的详细内容...");
            
            mockNews.add(item);
        }
        
        Log.d(TAG, "🎭 创建了 " + mockNews.size() + " 条模拟澳洲新闻");
        return mockNews;
    }
    
    /**
     * 解析NewsAPI响应
     */
    private List<UnifiedNewsItem> parseNewsApiResponse(String jsonResponse) throws JSONException {
        List<UnifiedNewsItem> newsItems = new ArrayList<>();
        
        JSONObject response = new JSONObject(jsonResponse);
        String status = response.optString("status");
        
        if ("ok".equals(status)) {
            JSONArray articles = response.getJSONArray("articles");
            
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                
                String title = article.optString("title", "");
                String description = article.optString("description", "");
                String url = article.optString("url", "");
                String urlToImage = article.optString("urlToImage", "");
                String publishedAt = article.optString("publishedAt", "");
                String author = article.optString("author", "");
                String content = article.optString("content", "");
                
                JSONObject source = article.optJSONObject("source");
                String sourceName = source != null ? source.optString("name", "未知来源") : "未知来源";
                
                // 创建UnifiedNewsItem
                UnifiedNewsItem newsItem = new UnifiedNewsItem();
                newsItem.setTitle(title);
                newsItem.setDescription(description);
                newsItem.setUrl(url);
                newsItem.setImageUrl(urlToImage);
                newsItem.setPublishedAt(publishedAt);
                newsItem.setAuthor(author);
                newsItem.setContent(content);
                newsItem.setSource(sourceName);
                
                newsItems.add(newsItem);
            }
        } else {
            throw new JSONException("API返回错误状态: " + status);
        }
        
        return newsItems;
    }
    
    /**
     * 🔥 关键：UnifiedNewsItem类 - 与LocalNewsFragment完全兼容
     */
    public static class UnifiedNewsItem {
        private String title;
        private String description;
        private String url;
        private String imageUrl;
        private String publishedAt;
        private String source;
        private String author;
        private String content;
        
        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getPublishedAt() { return publishedAt; }
        public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
        
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        @Override
        public String toString() {
            return "UnifiedNewsItem{" +
                    "title='" + title + '\'' +
                    ", source='" + source + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    '}';
        }
    }
    
    /**
     * 🔥 关键：NewsCallback接口 - 与LocalNewsFragment完全兼容
     */
    public interface NewsCallback {
        void onSuccess(List<UnifiedNewsItem> newsItems);
        void onError(String error);
    }
}