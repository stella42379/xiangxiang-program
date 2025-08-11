package com.xiangjia.locallife.service;

import android.util.Log;

import com.xiangjia.locallife.search.ForumSearchTokenizer;
import com.xiangjia.locallife.search.ForumSearchParser;
import com.xiangjia.locallife.search.SearchQuery;
import com.xiangjia.locallife.search.Token;
import com.xiangjia.locallife.strategy.SearchContext;
import com.xiangjia.locallife.strategy.BasicSearchStrategy;
import com.xiangjia.locallife.strategy.AdvancedSearchStrategy;
import com.xiangjia.locallife.strategy.SearchStrategy;
import com.xiangjia.locallife.model.ForumPost;

import java.util.List;
import java.util.ArrayList;

/**
 * Singleton Pattern - 搜索管理器（增强调试版本）
 * 统一管理所有搜索相关功能
 */
public class ForumSearchManager {
    private static final String TAG = "ForumSearchManager";
    private static volatile ForumSearchManager instance;
    
    private final ForumSearchTokenizer tokenizer;
    private final ForumSearchParser parser;
    private final SearchContext searchContext;
    
    // 搜索历史
    private final List<String> searchHistory;
    private static final int MAX_HISTORY_SIZE = 50;
    
    // 搜索统计
    private int totalSearches = 0;
    private long totalSearchTime = 0;
    
    private ForumSearchManager() {
        this.tokenizer = new ForumSearchTokenizer();
        this.parser = new ForumSearchParser();
        this.searchContext = new SearchContext(new BasicSearchStrategy());
        this.searchHistory = new ArrayList<>();
        
        Log.d(TAG, "ForumSearchManager initialized");
    }
    
    /**
     * 获取单例实例
     */
    public static ForumSearchManager getInstance() {
        if (instance == null) {
            synchronized (ForumSearchManager.class) {
                if (instance == null) {
                    instance = new ForumSearchManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * 执行搜索（增强调试版本）
     * @param queryString 搜索查询字符串
     * @param posts 帖子列表
     * @return 搜索结果
     */
    public SearchResult search(String queryString, List<ForumPost> posts) {
        long startTime = System.currentTimeMillis();
        
        Log.d(TAG, "=== 开始搜索 ===");
        Log.d(TAG, "查询字符串: '" + queryString + "'");
        Log.d(TAG, "帖子数量: " + (posts != null ? posts.size() : 0));
        
        try {
            // 1. 验证输入
            if (queryString == null || queryString.trim().isEmpty()) {
                Log.w(TAG, "搜索查询为空");
                return new SearchResult(false, new ArrayList<>(), 0, "搜索查询不能为空", null);
            }
            
            if (posts == null) {
                Log.w(TAG, "帖子列表为空");
                return new SearchResult(false, new ArrayList<>(), 0, "帖子数据不能为空", null);
            }
            
            // 记录一些帖子样本用于调试
            for (int i = 0; i < Math.min(3, posts.size()); i++) {
                ForumPost post = posts.get(i);
                Log.d(TAG, String.format("样本帖子%d: 标题='%s', 内容前20字='%s'", 
                    i, post.getTitle(), 
                    post.getContent() != null ? post.getContent().substring(0, Math.min(20, post.getContent().length())) : "null"));
            }
            
            // 2. 词法分析
            List<Token> tokens = tokenizer.tokenize(queryString);
            Log.d(TAG, "词法分析完成: " + tokens.size() + " 个tokens");
            for (Token token : tokens) {
                Log.d(TAG, "Token: " + token.getType() + " = '" + token.getValue() + "'");
            }
            
            // 3. 语法分析
            SearchQuery query = parser.parse(tokens);
            Log.d(TAG, "语法分析完成: " + query.getClass().getSimpleName());
            Log.d(TAG, "查询对象: " + query.toString());
            
            // 4. 执行搜索（使用增强的搜索逻辑）
            List<ForumPost> results = executeEnhancedSearch(query, posts);
            
            // 5. 记录搜索历史和统计
            addToSearchHistory(queryString);
            updateStatistics(startTime);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Log.d(TAG, String.format("搜索完成: query='%s', results=%d, time=%dms", 
                queryString, results.size(), duration));
            
            return new SearchResult(true, results, duration, null, query);
            
        } catch (ForumSearchParser.SearchParseException e) {
            Log.e(TAG, "搜索解析错误: " + e.getMessage());
            return new SearchResult(false, new ArrayList<>(), 0, "搜索语法错误: " + e.getMessage(), null);
        } catch (Exception e) {
            Log.e(TAG, "搜索错误", e);
            return new SearchResult(false, new ArrayList<>(), 0, "搜索失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 增强的搜索执行逻辑
     */
    private List<ForumPost> executeEnhancedSearch(SearchQuery query, List<ForumPost> posts) {
        Log.d(TAG, "开始执行增强搜索");
        
        // 如果是关键词查询，添加调试日志
        if (query instanceof SearchQuery.KeywordQuery) {
            SearchQuery.KeywordQuery keywordQuery = (SearchQuery.KeywordQuery) query;
            String keyword = keywordQuery.getKeyword().toLowerCase();
            Log.d(TAG, "关键词搜索: '" + keyword + "'");
            
            List<ForumPost> results = new ArrayList<>();
            int titleMatches = 0;
            int contentMatches = 0;
            
            for (ForumPost post : posts) {
                String title = post.getTitle() != null ? post.getTitle().toLowerCase() : "";
                String content = post.getContent() != null ? post.getContent().toLowerCase() : "";
                
                boolean titleMatch = title.contains(keyword);
                boolean contentMatch = content.contains(keyword);
                
                if (titleMatch || contentMatch) {
                    results.add(post);
                    if (titleMatch) titleMatches++;
                    if (contentMatch) contentMatches++;
                    
                    Log.d(TAG, String.format("匹配: 标题匹配=%s, 内容匹配=%s, 标题='%s'", 
                        titleMatch, contentMatch, post.getTitle()));
                }
            }
            
            Log.d(TAG, String.format("关键词搜索结果: 总匹配=%d, 标题匹配=%d, 内容匹配=%d", 
                results.size(), titleMatches, contentMatches));
            
            return results;
        }
        
        // 其他类型的查询使用原有逻辑
        return searchContext.executeSearch(query, posts);
    }
    
    /**
     * 切换搜索策略
     * @param useAdvanced 是否使用高级搜索
     */
    public void setAdvancedSearch(boolean useAdvanced) {
        if (useAdvanced) {
            searchContext.setStrategy(new AdvancedSearchStrategy());
            Log.d(TAG, "Switched to advanced search strategy");
        } else {
            searchContext.setStrategy(new BasicSearchStrategy());
            Log.d(TAG, "Switched to basic search strategy");
        }
    }
    
    /**
     * 设置自定义搜索策略（新增方法）
     */
    public void setStrategy(SearchStrategy strategy) {
        if (strategy != null) {
            searchContext.setStrategy(strategy);
            Log.d(TAG, "设置自定义搜索策略: " + strategy.getClass().getSimpleName());
        }
    }
    
    /**
     * 获取当前搜索策略名称
     */
    public String getCurrentStrategyName() {
        return searchContext.getCurrentStrategyName();
    }
    
    /**
     * 获取当前搜索策略描述
     */
    public String getCurrentStrategyDescription() {
        return searchContext.getCurrentStrategyDescription();
    }
    
    /**
     * 获取策略详细信息
     */
    public String getStrategyInfo() {
        return searchContext.getStrategyInfo();
    }
    
    /**
     * 添加到搜索历史
     */
    private void addToSearchHistory(String query) {
        if (query == null || query.trim().isEmpty()) return;
        
        String trimmedQuery = query.trim();
        
        // 移除重复项
        searchHistory.remove(trimmedQuery);
        
        // 添加到开头
        searchHistory.add(0, trimmedQuery);
        
        // 限制历史记录大小
        while (searchHistory.size() > MAX_HISTORY_SIZE) {
            searchHistory.remove(searchHistory.size() - 1);
        }
    }
    
    /**
     * 更新搜索统计
     */
    private void updateStatistics(long startTime) {
        totalSearches++;
        totalSearchTime += (System.currentTimeMillis() - startTime);
    }
    
    /**
     * 获取搜索历史
     */
    public List<String> getSearchHistory() {
        return new ArrayList<>(searchHistory);
    }
    
    /**
     * 获取搜索历史（限制数量）
     */
    public List<String> getSearchHistory(int limit) {
        List<String> history = new ArrayList<>(searchHistory);
        if (history.size() > limit) {
            return history.subList(0, limit);
        }
        return history;
    }
    
    /**
     * 清除搜索历史
     */
    public void clearSearchHistory() {
        searchHistory.clear();
        Log.d(TAG, "Search history cleared");
    }
    
    /**
     * 验证搜索查询语法
     */
    public boolean validateQuery(String queryString) {
        try {
            List<Token> tokens = tokenizer.tokenize(queryString);
            parser.parse(tokens);
            return true;
        } catch (Exception e) {
            Log.w(TAG, "查询验证失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取查询建议
     */
    public List<String> getQuerySuggestions(String partial) {
        List<String> suggestions = new ArrayList<>();
        
        if (partial == null || partial.trim().isEmpty()) {
            // 返回最近的搜索历史
            return getSearchHistory(5);
        }
        
        String lowerPartial = partial.toLowerCase();
        
        // 从历史记录中匹配
        for (String history : searchHistory) {
            if (history.toLowerCase().contains(lowerPartial) && suggestions.size() < 5) {
                suggestions.add(history);
            }
        }
        
        // 添加一些常用的搜索模式建议
        if (suggestions.size() < 5) {
            String[] commonPatterns = {
                "category:help " + partial,
                "category:discussion " + partial,
                "author:" + partial,
                "\"" + partial + "\"",
                partial + " AND category:share"
            };
            
            for (String pattern : commonPatterns) {
                if (suggestions.size() < 5) {
                    suggestions.add(pattern);
                }
            }
        }
        
        return suggestions;
    }
    
    /**
     * 获取搜索统计信息
     */
    public SearchStatistics getSearchStatistics() {
        long avgSearchTime = totalSearches > 0 ? totalSearchTime / totalSearches : 0;
        return new SearchStatistics(totalSearches, totalSearchTime, avgSearchTime, searchHistory.size());
    }
    
    /**
     * 重置搜索统计
     */
    public void resetStatistics() {
        totalSearches = 0;
        totalSearchTime = 0;
        Log.d(TAG, "Search statistics reset");
    }
    
    /**
     * 获取Token统计信息
     */
    public String getTokenStatistics(String queryString) {
        return tokenizer.getTokenStatistics(queryString);
    }
    
    /**
     * 检查是否使用高级搜索
     */
    public boolean isUsingAdvancedSearch() {
        return searchContext.isAdvancedStrategy();
    }
    
    /**
     * 搜索结果类
     */
    public static class SearchResult {
        private final boolean success;
        private final List<ForumPost> results;
        private final long searchTime;
        private final String errorMessage;
        private final SearchQuery query;
        
        public SearchResult(boolean success, List<ForumPost> results, long searchTime, String errorMessage, SearchQuery query) {
            this.success = success;
            this.results = results != null ? results : new ArrayList<>();
            this.searchTime = searchTime;
            this.errorMessage = errorMessage;
            this.query = query;
        }
        
        public boolean isSuccess() { 
            return success; 
        }
        
        public List<ForumPost> getResults() { 
            return results; 
        }
        
        public long getSearchTime() { 
            return searchTime; 
        }
        
        public String getErrorMessage() { 
            return errorMessage; 
        }
        
        public int getResultCount() { 
            return results.size(); 
        }
        
        public SearchQuery getQuery() { 
            return query; 
        }
        
        public boolean hasResults() {
            return !results.isEmpty();
        }
        
        public String getResultSummary() {
            if (!success) {
                return "搜索失败: " + errorMessage;
            }
            return String.format("找到 %d 条结果，耗时 %d ms", results.size(), searchTime);
        }
    }
    
    /**
     * 搜索统计信息类
     */
    public static class SearchStatistics {
        private final int totalSearches;
        private final long totalTime;
        private final long averageTime;
        private final int historySize;
        
        public SearchStatistics(int totalSearches, long totalTime, long averageTime, int historySize) {
            this.totalSearches = totalSearches;
            this.totalTime = totalTime;
            this.averageTime = averageTime;
            this.historySize = historySize;
        }
        
        public int getTotalSearches() { 
            return totalSearches; 
        }
        
        public long getTotalTime() { 
            return totalTime; 
        }
        
        public long getAverageTime() { 
            return averageTime; 
        }
        
        public int getHistorySize() { 
            return historySize; 
        }
        
        @Override
        public String toString() {
            return String.format("搜索统计: 总次数=%d, 总时间=%dms, 平均时间=%dms, 历史记录=%d条",
                    totalSearches, totalTime, averageTime, historySize);
        }
    }
}