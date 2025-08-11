package com.xiangjia.locallife.strategy;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基本搜索策略 - 简单文本匹配
 * Strategy Pattern实现
 */
public class BasicSearchStrategy implements SearchStrategy {
    
    @Override
    public List<ForumPost> search(SearchQuery query, List<ForumPost> posts) {
        if (query == null || posts == null) {
            return new ArrayList<>();
        }
        return executeSearch(query, posts);
    }
    
    /**
     * 执行搜索逻辑
     */
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
        return new ArrayList<>();
    }
    
    /**
     * 关键词搜索
     */
    private List<ForumPost> searchByKeyword(SearchQuery.KeywordQuery query, List<ForumPost> posts) {
        String keyword = query.getKeyword().toLowerCase();
        return posts.stream()
            .filter(post -> {
                String title = post.getTitle() != null ? post.getTitle().toLowerCase() : "";
                String content = post.getContent() != null ? post.getContent().toLowerCase() : "";
                return title.contains(keyword) || content.contains(keyword);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 分类搜索
     */
    private List<ForumPost> searchByCategory(SearchQuery.CategoryQuery query, List<ForumPost> posts) {
        String category = query.getCategory();
        return posts.stream()
            .filter(post -> category.equals(post.getCategory()))
            .collect(Collectors.toList());
    }
    
    /**
     * 作者搜索
     */
    private List<ForumPost> searchByAuthor(SearchQuery.AuthorQuery query, List<ForumPost> posts) {
        String author = query.getAuthor();
        return posts.stream()
            .filter(post -> author.equals(post.getAuthorName()))
            .collect(Collectors.toList());
    }
    
    /**
     * 日期搜索
     */
    private List<ForumPost> searchByDate(SearchQuery.DateQuery query, List<ForumPost> posts) {
        String dateStr = query.getDate();
        try {
            // 简化处理：假设日期格式为 YYYY-MM-DD
            String[] dateParts = dateStr.split("-");
            if (dateParts.length == 3) {
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                
                return posts.stream()
                    .filter(post -> {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTimeInMillis(post.getTimestamp());
                        return cal.get(java.util.Calendar.YEAR) == year &&
                               cal.get(java.util.Calendar.MONTH) + 1 == month &&
                               cal.get(java.util.Calendar.DAY_OF_MONTH) == day;
                    })
                    .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            // 日期格式错误，返回空结果
        }
        return new ArrayList<>();
    }
    
    /**
     * 标签搜索
     */
    private List<ForumPost> searchByTag(SearchQuery.TagQuery query, List<ForumPost> posts) {
        String tag = query.getTag();
        return posts.stream()
            .filter(post -> {
                String tags = post.getTags();
                return tags != null && tags.contains(tag);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 二元操作搜索 (AND, OR)
     */
    private List<ForumPost> searchBinary(SearchQuery.BinaryQuery query, List<ForumPost> posts) {
        List<ForumPost> leftResults = executeSearch(query.getLeft(), posts);
        List<ForumPost> rightResults = executeSearch(query.getRight(), posts);
        
        if (query.getOperator() == SearchQuery.BinaryQuery.Operator.AND) {
            // AND操作：取交集
            return leftResults.stream()
                .filter(rightResults::contains)
                .collect(Collectors.toList());
        } else {
            // OR操作：取并集
            List<ForumPost> result = new ArrayList<>(leftResults);
            rightResults.stream()
                .filter(post -> !result.contains(post))
                .forEach(result::add);
            return result;
        }
    }
    
    /**
     * 否定搜索 (NOT)
     */
    private List<ForumPost> searchNot(SearchQuery.NotQuery query, List<ForumPost> posts) {
        List<ForumPost> excludeResults = executeSearch(query.getQuery(), posts);
        return posts.stream()
            .filter(post -> !excludeResults.contains(post))
            .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "基本搜索";
    }
    
    @Override
    public String getStrategyDescription() {
        return "基于简单文本匹配的搜索策略，支持关键词、分类、作者等搜索";
    }
    
    @Override
    public boolean supportsComplexQueries() {
        return true;
    }
}