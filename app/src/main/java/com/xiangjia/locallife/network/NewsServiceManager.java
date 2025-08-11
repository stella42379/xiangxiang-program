package com.xiangjia.locallife.network;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Proxy;

/**
 * 🔥 OkHttp版NewsServiceManager - 解决跨境网络和Cloudflare问题
 */
public class NewsServiceManager {
    private static final String TAG = "NewsServiceManager";
    
    // API配置
    private static final String NEWS_API_KEY = "4ae67c64b53f46019b3f2b4d7d37299d";
    private static final String NEWS_API_BASE = "https://newsapi.org/";
    
    private static NewsServiceManager instance;
    private final OkHttpClient httpClient;
    private Call currentCall; // 用于取消正在进行的请求
    
    private NewsServiceManager() {
        // 🔥 关键：配置OkHttp客户端，解决跨境连接问题
        httpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)        // 5秒连接超时
            .readTimeout(8, TimeUnit.SECONDS)           // 8秒读取超时
            .writeTimeout(8, TimeUnit.SECONDS)          // 8秒写入超时
            .retryOnConnectionFailure(true)             // 连接失败时重试
            .protocols(Collections.singletonList(Protocol.HTTP_1_1)) // 强制HTTP/1.1，避免HTTP/2兼容问题
            // 🔥 关键：自定义DNS解析，避免系统DNS慢/黑洞问题
            .dns(hostname -> {
                Log.d(TAG, "🌐 开始DNS解析: " + hostname);
                long dnsStart = System.currentTimeMillis();
                
                try {
                    // 🔥 方法1：手动指定IP，绕过DNS解析
                    if ("newsapi.org".equals(hostname)) {
                        Log.d(TAG, "🎯 使用硬编码IP绕过DNS");
                        // NewsAPI的实际IP地址（可能需要更新）
                        List<InetAddress> result = new ArrayList<>();
                        try {
                            // 添加多个可能的IP
                            result.add(InetAddress.getByName("104.18.27.173"));  // Cloudflare IP
                            result.add(InetAddress.getByName("104.18.26.173"));  // Cloudflare IP备用
                            result.add(InetAddress.getByName("172.67.139.99"));  // 另一个Cloudflare IP
                            
                            long dnsEnd = System.currentTimeMillis();
                            Log.d(TAG, "✅ 硬编码IP解析完成，耗时: " + (dnsEnd - dnsStart) + "ms");
                            return result;
                        } catch (Exception e) {
                            Log.e(TAG, "❌ 硬编码IP解析失败，回退到系统DNS", e);
                        }
                    }
                    
                    // 🔥 方法2：系统DNS解析（只使用IPv4）
                    List<InetAddress> all = Dns.SYSTEM.lookup(hostname);
                    List<InetAddress> ipv4Only = new ArrayList<>();
                    for (InetAddress addr : all) {
                        if (addr instanceof Inet4Address) {
                            ipv4Only.add(addr);
                        }
                    }
                    
                    List<InetAddress> result = ipv4Only.isEmpty() ? all : ipv4Only;
                    long dnsEnd = System.currentTimeMillis();
                    
                    Log.d(TAG, "✅ DNS解析完成: " + hostname);
                    Log.d(TAG, "📊 解析耗时: " + (dnsEnd - dnsStart) + "ms");
                    Log.d(TAG, "📊 获得IP数量: " + result.size());
                    for (InetAddress addr : result) {
                        Log.d(TAG, "📍 IP地址: " + addr.getHostAddress());
                    }
                    
                    return result;
                    
                } catch (Exception e) {
                    long dnsEnd = System.currentTimeMillis();
                    Log.e(TAG, "❌ DNS解析失败: " + hostname + "，耗时: " + (dnsEnd - dnsStart) + "ms", e);
                    
                    // 最后的降级方案：使用公共DNS
                    try {
                        Log.d(TAG, "🆘 尝试降级方案：直接返回Cloudflare通用IP");
                        List<InetAddress> fallback = new ArrayList<>();
                        fallback.add(InetAddress.getByName("104.18.27.173"));
                        return fallback;
                    } catch (Exception ex) {
                        Log.e(TAG, "💥 所有DNS方案都失败了", ex);
                        throw new RuntimeException("DNS解析完全失败", e);
                    }
                }
            })
            // 🔥 添加详细的事件监听器，诊断卡点
            .eventListener(new okhttp3.EventListener() {
                @Override
                public void callStart(okhttp3.Call call) {
                    Log.d(TAG, "📞 OkHttp开始调用: " + call.request().url());
                }
                
                @Override
                public void dnsStart(okhttp3.Call call, String domainName) {
                    Log.d(TAG, "🌐 DNS查询开始: " + domainName);
                }
                
                @Override
                public void dnsEnd(okhttp3.Call call, String domainName, List<InetAddress> inetAddressList) {
                    Log.d(TAG, "✅ DNS查询完成: " + domainName + ", IP数量: " + inetAddressList.size());
                }
                
    
                public void connectStart(okhttp3.Call call, InetSocketAddress inetSocketAddress, java.net.Proxy proxy) {
                    Log.d(TAG, "🔌 TCP连接开始: " + inetSocketAddress);
                }
                
        
                public void connectEnd(okhttp3.Call call, InetSocketAddress inetSocketAddress, java.net.Proxy proxy, okhttp3.Protocol protocol) {
                    Log.d(TAG, "✅ TCP连接完成: " + inetSocketAddress + ", 协议: " + protocol);
                }
                

                public void tlsHandshakeStart(okhttp3.Call call) {
                    Log.d(TAG, "🔐 TLS握手开始");
                }
                

                public void tlsHandshakeEnd(okhttp3.Call call, okhttp3.Handshake handshake) {
                    Log.d(TAG, "✅ TLS握手完成: " + handshake.cipherSuite());
                }
                
                @Override
                public void requestHeadersStart(okhttp3.Call call) {
                    Log.d(TAG, "📤 发送请求头");
                }
                
                @Override
                public void requestHeadersEnd(okhttp3.Call call, okhttp3.Request request) {
                    Log.d(TAG, "✅ 请求头发送完成");
                }
                
                @Override
                public void responseHeadersStart(okhttp3.Call call) {
                    Log.d(TAG, "📥 接收响应头");
                }
                
                @Override
                public void responseHeadersEnd(okhttp3.Call call, okhttp3.Response response) {
                    Log.d(TAG, "✅ 响应头接收完成: " + response.code());
                }
                
                @Override
                public void callEnd(okhttp3.Call call) {
                    Log.d(TAG, "✅ OkHttp调用完成");
                }
                
                @Override
                public void callFailed(okhttp3.Call call, IOException ioe) {
                    Log.e(TAG, "❌ OkHttp调用失败: " + ioe.getMessage(), ioe);
                }
                
                @Override
                public void canceled(okhttp3.Call call) {
                    Log.w(TAG, "🛑 OkHttp调用被取消");
                }
            })
            .build();
        
        Log.d(TAG, "✅ OkHttp客户端初始化完成 (HTTP/1.1 + IPv4优先 + 硬编码DNS + 详细监控)");
    }
    
    public static NewsServiceManager getInstance() {
        if (instance == null) {
            instance = new NewsServiceManager();
        }
        return instance;
    }
    
    /**
     * 🔥 网络诊断：测试基础连通性
     */
    public void testNetworkConnectivity(NewsCallback callback) {
        Log.d(TAG, "🩺 开始网络连通性测试");
        
        // 测试多个目标，找出网络问题
        String[] testUrls = {
            "https://httpbin.org/status/200",  // 测试基础HTTP连通性
            "https://www.google.com",          // 测试国际网络
            "https://newsapi.org",             // 测试目标域名
        };
        
        for (String testUrl : testUrls) {
            Request request = new Request.Builder()
                .url(testUrl)
                .header("User-Agent", "LocalLife/1.0 Network Test")
                .build();
            
            Log.d(TAG, "🔍 测试连接: " + testUrl);
            
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "❌ 连接失败 " + testUrl + ": " + e.getMessage(), e);
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "✅ 连接成功 " + testUrl + ": " + response.code());
                    response.close();
                }
            });
        }
    }
    public void getGlobalTopNews(NewsCallback callback) {
        Log.d(TAG, "🌍 开始获取全球热门新闻 (OkHttp版本)");
        
        // 取消之前的请求
        cancelCurrentRequest();
        
        try {
            // 构建请求URL - API key放在query参数中
            String url = NEWS_API_BASE + "v2/top-headlines" +
                "?country=us" +
                "&pageSize=20" +
                "&apiKey=" + NEWS_API_KEY;
            
            Log.d(TAG, "📡 请求URL: " + url.replace(NEWS_API_KEY, "***"));
            
            // 构建请求 - 🔥 关键：不手动设置Accept-Encoding，让OkHttp自动处理GZIP
            Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", 
                    "Mozilla/5.0 (Linux; Android 14; Pixel 5) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36")
                .header("Accept", "application/json")
                .header("Connection", "close")
                .header("X-API-Key", NEWS_API_KEY) // 冗余保险
                // 🔥 关键：移除Accept-Encoding，让OkHttp自动处理
                .build();
            
            Log.d(TAG, "📋 请求构建完成，开始异步执行...");
            
            // 异步执行请求
            currentCall = httpClient.newCall(request);
            currentCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "❌ 请求失败: " + e.getMessage(), e);
                    
                    if (call.isCanceled()) {
                        Log.d(TAG, "🛑 请求已被取消");
                        return;
                    }
                    
                    String errorMsg;
                    if (e instanceof java.net.SocketTimeoutException) {
                        errorMsg = "网络超时，请检查网络连接";
                    } else if (e instanceof java.net.ConnectException) {
                        errorMsg = "连接失败: " + e.getMessage();
                    } else if (e instanceof java.net.UnknownHostException) {
                        errorMsg = "域名解析失败，请检查网络";
                    } else {
                        errorMsg = "网络异常: " + e.getMessage();
                    }
                    
                    if (callback != null) {
                        callback.onError(errorMsg);
                    }
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    long startTime = System.currentTimeMillis();
                    Log.d(TAG, "📊 HTTP状态: " + response.code() + " " + response.message());
                    Log.d(TAG, "📦 Content-Type: " + response.header("Content-Type"));
                    Log.d(TAG, "🗜️ Content-Encoding: " + response.header("Content-Encoding"));
                    Log.d(TAG, "📏 Content-Length: " + response.header("Content-Length"));
                    
                    if (call.isCanceled()) {
                        response.close();
                        Log.d(TAG, "🛑 请求已被取消");
                        return;
                    }
                    
                    try {
                        // 🔥 关键：OkHttp会自动处理GZIP解压缩
                        if (response.body() == null) {
                            throw new IOException("响应体为空");
                        }
                        
                        String responseBody = response.body().string();
                        long parseTime = System.currentTimeMillis();
                        
                        Log.d(TAG, "📄 响应体长度: " + responseBody.length());
                        Log.d(TAG, "⏱️ 读取响应耗时: " + (parseTime - startTime) + "ms");
                        Log.d(TAG, "📄 响应体前256字符: " + 
                              responseBody.substring(0, Math.min(256, responseBody.length())));
                        
                        // 🔥 检查响应体是否是有效的JSON
                        if (responseBody.trim().isEmpty()) {
                            throw new IOException("响应体为空");
                        }
                        
                        if (!responseBody.trim().startsWith("{") && !responseBody.trim().startsWith("[")) {
                            Log.e(TAG, "❌ 响应体不是有效JSON，前100字符: " + 
                                  responseBody.substring(0, Math.min(100, responseBody.length())));
                            throw new IOException("响应体不是有效的JSON格式");
                        }
                        
                        if (response.isSuccessful()) {
                            // 解析成功响应
                            List<UnifiedNewsItem> newsItems = parseNewsApiResponse(responseBody);
                            Log.d(TAG, "✅ 全球新闻解析成功，获得 " + newsItems.size() + " 条新闻");
                            
                            if (callback != null) {
                                callback.onSuccess(newsItems);
                            }
                        } else {
                            // 处理错误响应
                            String errorMsg = "HTTP " + response.code() + ": " + response.message() + 
                                             "\n" + responseBody;
                            Log.e(TAG, "❌ HTTP错误: " + errorMsg);
                            
                            if (callback != null) {
                                callback.onError(errorMsg);
                            }
                        }
                        
                    } catch (JSONException e) {
                        String errorMsg = "JSON解析异常: " + e.getMessage();
                        Log.e(TAG, "📝 " + errorMsg, e);
                        if (callback != null) {
                            callback.onError(errorMsg);
                        }
                        
                    } catch (Exception e) {
                        String errorMsg = "响应处理异常: " + e.getMessage();
                        Log.e(TAG, "💥 " + errorMsg, e);
                        if (callback != null) {
                            callback.onError(errorMsg);
                        }
                        
                    } finally {
                        response.close();
                        Log.d(TAG, "✅ 响应资源已释放");
                    }
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "💥 构建请求时出错", e);
            if (callback != null) {
                callback.onError("构建请求失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 🔥 获取国际新闻 - 备用方案
     */
    public void getInternationalNews(NewsCallback callback) {
        Log.d(TAG, "🌍 开始获取国际新闻 (备用方案)");
        // 直接调用全球新闻方法
        getGlobalTopNews(callback);
    }
    
    /**
     * 🔥 获取澳洲新闻 - 备用方案
     */
    public void getAustralianNews(NewsCallback callback) {
        Log.d(TAG, "🇦🇺 开始获取澳洲新闻");
        
        // 取消之前的请求
        cancelCurrentRequest();
        
        try {
            // 构建澳洲新闻请求URL
            String url = NEWS_API_BASE + "v2/top-headlines" +
                "?country=au" +
                "&pageSize=20" +
                "&apiKey=" + NEWS_API_KEY;
            
            Log.d(TAG, "📡 澳洲新闻URL: " + url.replace(NEWS_API_KEY, "***"));
            
            // 构建请求（配置与全球新闻相同）- 🔥 关键：不手动设置Accept-Encoding
            Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", 
                    "Mozilla/5.0 (Linux; Android 14; Pixel 5) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36")
                .header("Accept", "application/json")
                .header("Connection", "close")
                .header("X-API-Key", NEWS_API_KEY)
                .build();
            
            // 异步执行请求（逻辑与全球新闻相同，此处简化）
            currentCall = httpClient.newCall(request);
            currentCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "❌ 澳洲新闻请求失败: " + e.getMessage(), e);
                    if (callback != null && !call.isCanceled()) {
                        callback.onError("澳洲新闻获取失败: " + e.getMessage());
                    }
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "📊 澳洲新闻HTTP状态: " + response.code());
                    
                    try {
                        String responseBody = response.body().string();
                        if (response.isSuccessful()) {
                            List<UnifiedNewsItem> newsItems = parseNewsApiResponse(responseBody);
                            Log.d(TAG, "✅ 澳洲新闻解析成功，获得 " + newsItems.size() + " 条新闻");
                            if (callback != null) {
                                callback.onSuccess(newsItems);
                            }
                        } else {
                            String errorMsg = "澳洲新闻HTTP " + response.code() + ": " + responseBody;
                            Log.e(TAG, "❌ " + errorMsg);
                            if (callback != null) {
                                callback.onError(errorMsg);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "❌ 澳洲新闻处理异常", e);
                        if (callback != null) {
                            callback.onError("澳洲新闻处理失败: " + e.getMessage());
                        }
                    } finally {
                        response.close();
                    }
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "💥 澳洲新闻请求构建失败", e);
            if (callback != null) {
                callback.onError("澳洲新闻请求构建失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 🛑 取消当前请求
     */
    public void cancelCurrentRequest() {
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
            Log.d(TAG, "🛑 已取消当前请求");
        }
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