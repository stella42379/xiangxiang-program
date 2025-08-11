// 文件路径: app/src/main/java/com/xiangjia/locallife/network/NewsApiConfig.java
package com.xiangjia.locallife.network;

/**
 * 新闻API配置类
 * 包含所有API密钥和配置常量
 */
public class NewsApiConfig {
    
    // ============= API密钥配置 =============
    public static final String NEWS_API_KEY = "4ae67c64b53f46019b3f2b4d7d37299d";
    public static final String GUARDIAN_API_KEY = "dfd2220f-63dc-4407-88d0-7f09ea85dd68";
    public static final String GNEWS_TOKEN = "d90eca5b0b6180cb165248a454500cd9";
    
    // ============= API基础URL =============
    public static final String NEWS_API_BASE_URL = "https://newsapi.org/";
    public static final String GUARDIAN_BASE_URL = "https://content.guardianapis.com/";
    public static final String GNEWS_BASE_URL = "https://gnews.io/api/v4/";
    
    // ============= 澳洲新闻源配置 =============
    public static final String AU_NEWS_SOURCES = "abc-news-au,news-com-au,the-australian,australian-financial-review";
    
    // ============= 国际新闻源配置 =============
    public static final String INTERNATIONAL_SOURCES = "bbc-news,cnn,reuters,associated-press,bloomberg,the-guardian-uk";
    
    // ============= 网络配置 =============
    public static final int CONNECT_TIMEOUT = 10; // 秒
    public static final int READ_TIMEOUT = 15;     // 秒
    public static final int WRITE_TIMEOUT = 15;    // 秒
    
    // ============= 分页配置 =============
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // ============= 缓存配置 =============
    public static final long CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final int CACHE_MAX_AGE = 60 * 5; // 5分钟
    
    // ============= 澳洲媒体优先级 =============
    public static final String[] AUSTRALIA_PRIORITY_SOURCES = {
        "abc-news-au",           // ABC News - 最权威
        "news-com-au",           // News.com.au - 最流行  
        "the-australian",        // The Australian - 商业新闻
        "australian-financial-review" // AFR - 金融新闻
    };
    
    // ============= 国际媒体优先级 =============
    public static final String[] INTERNATIONAL_PRIORITY_SOURCES = {
        "bbc-news",              // BBC - 英国权威
        "cnn",                   // CNN - 美国主流
        "reuters",               // 路透社 - 国际通讯社
        "associated-press",      // 美联社 - 国际通讯社
        "bloomberg",             // 彭博社 - 财经
        "the-guardian-uk"        // 卫报 - 英国左翼
    };
    
    // ============= 新闻分类 =============
    public static final String CATEGORY_BUSINESS = "business";
    public static final String CATEGORY_ENTERTAINMENT = "entertainment";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_SCIENCE = "science";
    public static final String CATEGORY_SPORTS = "sports";
    public static final String CATEGORY_TECHNOLOGY = "technology";
    public static final String CATEGORY_GENERAL = "general";
    
    // ============= 国家代码 =============
    public static final String COUNTRY_AUSTRALIA = "au";
    public static final String COUNTRY_US = "us";
    public static final String COUNTRY_UK = "gb";
    public static final String COUNTRY_CANADA = "ca";
    
    // ============= 语言代码 =============
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_CHINESE = "zh";
    
    // ============= 排序方式 =============
    public static final String SORT_BY_PUBLISHED_AT = "publishedAt";
    public static final String SORT_BY_RELEVANCY = "relevancy";
    public static final String SORT_BY_POPULARITY = "popularity";
    
    // ============= Guardian特定配置 =============
    public static final String GUARDIAN_SECTION_AUSTRALIA = "australia-news";
    public static final String GUARDIAN_SECTION_WORLD = "world";
    public static final String GUARDIAN_SECTION_TECHNOLOGY = "technology";
    public static final String GUARDIAN_SECTION_BUSINESS = "business";
    
    public static final String GUARDIAN_ORDER_NEWEST = "newest";
    public static final String GUARDIAN_ORDER_OLDEST = "oldest";
    public static final String GUARDIAN_ORDER_RELEVANCE = "relevance";
    
    public static final String GUARDIAN_SHOW_FIELDS = "headline,thumbnail,short-url,body";
    
    // ============= 错误信息 =============
    public static final String ERROR_NETWORK = "网络连接失败，请检查网络设置";
    public static final String ERROR_API_LIMIT = "API调用次数已达上限，请稍后重试";
    public static final String ERROR_NO_DATA = "暂无新闻数据";
    public static final String ERROR_INVALID_KEY = "API密钥无效，请检查配置";
    
    // ============= 工具方法 =============
    
    /**
     * 获取完整的澳洲新闻源列表
     */
    public static String getAustralianSourcesString() {
        return String.join(",", AUSTRALIA_PRIORITY_SOURCES);
    }
    
    /**
     * 获取完整的国际新闻源列表
     */
    public static String getInternationalSourcesString() {
        return String.join(",", INTERNATIONAL_PRIORITY_SOURCES);
    }
    
    /**
     * 检查API密钥是否有效
     */
    public static boolean isValidApiKey(String apiKey) {
        return apiKey != null && !apiKey.trim().isEmpty() && apiKey.length() > 10;
    }
    
    /**
     * 获取错误信息
     */
    public static String getErrorMessage(int httpCode) {
        switch (httpCode) {
            case 401:
                return ERROR_INVALID_KEY;
            case 429:
                return ERROR_API_LIMIT;
            case 404:
                return ERROR_NO_DATA;
            default:
                return ERROR_NETWORK;
        }
    }
}