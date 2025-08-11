package com.xiangjia.locallife.strategy;

import android.util.Log;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调试版本的基本搜索策略 - 添加详细日志
 */
public class DebugBasicSearchStrategy implements SearchStrategy {
    
    private static final String TAG = "DebugBasicSearch";
    
    @Override
    public List<ForumPost> search(SearchQuery query, List<ForumPost> posts) {
        Log.d(TAG, "=== 开始搜索调试 ===");
        Log.d(TAG, "查询对象: " + (query != null ? query.getClass().getSimpleName() : "null"));
        Log.d(TAG, "帖子数量: " + (posts != null ? posts.size() : 0));
        
        if (query == null || posts == null) {
            Log.w(TAG, "查询对象或帖子列表为空");
            return new ArrayList<>();
        }
        
        // 记录前5个帖子的信息用于调试
        for (int i = 0; i < Math.min(5, posts.size()); i++) {
            ForumPost post = posts.get(i);
            Log.d(TAG, String.format("帖子%d: 标题='%s', 内容='%s', 分类='%s'", 
                i, post.getTitle(), 
                post.getContent() != null ? post.getContent().substring(0, Math.min(20, post.getContent().length())) + "..." : "null",
                post.getCategory()));
        }
        
        return executeSearch(query, posts);
    }
    
    private List<ForumPost> executeSearch(SearchQuery query, List<ForumPost> posts) {
        if (query instanceof SearchQuery.KeywordQuery) {
            return searchByKeyword((SearchQuery.KeywordQuery) query, posts);
        } else if (query instanceof SearchQuery.CategoryQuery) {
            return searchByCategory((SearchQuery.CategoryQuery) query, posts);
        } else if (query instanceof SearchQuery.AuthorQuery) {
            return searchByAuthor((SearchQuery.AuthorQuery) query, posts);
        } else if (query instanceof SearchQuery.DateQuery) {
            return searchByDate((SearchQuery.DateQuery) query, posts);
        } else if (query instanceof SearchQuery.TagQuery) {
            return searchByTag((SearchQuery.TagQuery) query, posts);
        } else if (query instanceof SearchQuery.BinaryQuery) {
            return searchBinary((SearchQuery.BinaryQuery) query, posts);
        } else if (query instanceof SearchQuery.NotQuery) {
            return searchNot((SearchQuery.NotQuery) query, posts);
        }
        Log.w(TAG, "未知的查询类型: " + query.getClass().getSimpleName());
        return new ArrayList<>();
    }
    
    /**
     * 关键词搜索 - 增强调试版本
     */
    private List<ForumPost> searchByKeyword(SearchQuery.KeywordQuery query, List<ForumPost> posts) {
        String keyword = query.getKeyword().toLowerCase();
        Log.d(TAG, "关键词搜索: '" + keyword + "'");
        
        List<ForumPost> results = new ArrayList<>();
        int matchCount = 0;
        
        for (ForumPost post : posts) {
            String title = post.getTitle() != null ? post.getTitle().toLowerCase() : "";
            String content = post.getContent() != null ? post.getContent().toLowerCase() : "";
            
            boolean titleMatch = title.contains(keyword);
            boolean contentMatch = content.contains(keyword);
            
            if (titleMatch || contentMatch) {
                results.add(post);
                matchCount++;
                Log.d(TAG, String.format("匹配帖子: 标题匹配=%s, 内容匹配=%s, 标题='%s'", 
                    titleMatch, contentMatch, post.getTitle()));
            }
        }
        
        Log.d(TAG, String.format("关键词搜索完成: 关键词='%s', 匹配数量=%d/%d", 
            keyword, matchCount, posts.size()));
        
        return results;
    }
    
    /**
     * 分类搜索 - 增强调试版本
     */
    private List<ForumPost> searchByCategory(SearchQuery.CategoryQuery query, List<ForumPost> posts) {
        String category = query.getCategory();
        Log.d(TAG, "分类搜索: '" + category + "'");
        
        // 先统计所有分类
        List<String> allCategories = posts.stream()
            .map(ForumPost::getCategory)
            .distinct()
            .collect(Collectors.toList());
        Log.d(TAG, "数据库中的所有分类: " + allCategories);
        
        List<ForumPost> results = posts.stream()
            .filter(post -> category.equals(post.getCategory()))
            .collect(Collectors.toList());
            
        Log.d(TAG, String.format("分类搜索完成: 分类='%s', 匹配数量=%d/%d", 
            category, results.size(), posts.size()));
            
        return results;
    }
    
    /**
     * 作者搜索 - 增强调试版本
     */
    private List<ForumPost> searchByAuthor(SearchQuery.AuthorQuery query, List<ForumPost> posts) {
        String author = query.getAuthor();
        Log.d(TAG, "作者搜索: '" + author + "'");
        
        // 先统计所有作者
        List<String> allAuthors = posts.stream()
            .map(ForumPost::getAuthorName)
            .distinct()
            .collect(Collectors.toList());
        Log.d(TAG, "数据库中的所有作者: " + allAuthors.subList(0, Math.min(5, allAuthors.size())));
        
        List<ForumPost> results = posts.stream()
            .filter(post -> author.equals(post.getAuthorName()))
            .collect(Collectors.toList());
            
        Log.d(TAG, String.format("作者搜索完成: 作者='%s', 匹配数量=%d/%d", 
            author, results.size(), posts.size()));
            
        return results;
    }
    
    /**
     * 日期搜索
     */
    private List<ForumPost> searchByDate(SearchQuery.DateQuery query, List<ForumPost> posts) {
        String dateStr = query.getDate();
        Log.d(TAG, "日期搜索: '" + dateStr + "'");
        
        try {
            String[] dateParts = dateStr.split("-");
            if (dateParts.length == 3) {
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                
                List<ForumPost> results = posts.stream()
                    .filter(post -> {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTimeInMillis(post.getTimestamp());
                        return cal.get(java.util.Calendar.YEAR) == year &&
                               cal.get(java.util.Calendar.MONTH) + 1 == month &&
                               cal.get(java.util.Calendar.DAY_OF_MONTH) == day;
                    })
                    .collect(Collectors.toList());
                    
                Log.d(TAG, String.format("日期搜索完成: 日期='%s', 匹配数量=%d/%d", 
                    dateStr, results.size(), posts.size()));
                    
                return results;
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "日期格式错误: " + dateStr, e);
        }
        return new ArrayList<>();
    }
    
    /**
     * 标签搜索
     */
    private List<ForumPost> searchByTag(SearchQuery.TagQuery query, List<ForumPost> posts) {
        String tag = query.getTag();
        Log.d(TAG, "标签搜索: '" + tag + "'");
        
        List<ForumPost> results = posts.stream()
            .filter(post -> {
                String tags = post.getTags();
                return tags != null && tags.contains(tag);
            })
            .collect(Collectors.toList());
            
        Log.d(TAG, String.format("标签搜索完成: 标签='%s', 匹配数量=%d/%d", 
            tag, results.size(), posts.size()));
            
        return results;
    }
    
    /**
     * 二元操作搜索 (AND, OR)
     */
    private List<ForumPost> searchBinary(SearchQuery.BinaryQuery query, List<ForumPost> posts) {
        Log.d(TAG, "二元操作搜索: " + query.getOperator());
        
        List<ForumPost> leftResults = executeSearch(query.getLeft(), posts);
        List<ForumPost> rightResults = executeSearch(query.getRight(), posts);
        
        Log.d(TAG, String.format("左侧结果: %d, 右侧结果: %d", leftResults.size(), rightResults.size()));
        
        if (query.getOperator() == SearchQuery.BinaryQuery.Operator.AND) {
            List<ForumPost> results = leftResults.stream()
                .filter(rightResults::contains)
                .collect(Collectors.toList());
            Log.d(TAG, "AND操作结果: " + results.size());
            return results;
        } else {
            List<ForumPost> result = new ArrayList<>(leftResults);
            rightResults.stream()
                .filter(post -> !result.contains(post))
                .forEach(result::add);
            Log.d(TAG, "OR操作结果: " + result.size());
            return result;
        }
    }
    
    /**
     * 否定搜索 (NOT)
     */
    private List<ForumPost> searchNot(SearchQuery.NotQuery query, List<ForumPost> posts) {
        Log.d(TAG, "否定搜索");
        
        List<ForumPost> excludeResults = executeSearch(query.getQuery(), posts);
        List<ForumPost> results = posts.stream()
            .filter(post -> !excludeResults.contains(post))
            .collect(Collectors.toList());
            
        Log.d(TAG, String.format("否定搜索完成: 排除=%d, 结果=%d/%d", 
            excludeResults.size(), results.size(), posts.size()));
            
        return results;
    }
    
    @Override
    public String getStrategyName() {
        return "调试基本搜索";
    }
    
    @Override
    public String getStrategyDescription() {
        return "增强调试日志的基本搜索策略";
    }
    
    @Override
    public boolean supportsComplexQueries() {
        return true;
    }
}