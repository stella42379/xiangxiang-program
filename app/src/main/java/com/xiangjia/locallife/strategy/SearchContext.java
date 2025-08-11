package com.xiangjia.locallife.strategy;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.search.SearchQuery;

import java.util.List;

/**
 * Strategy Pattern - 搜索上下文
 * 管理和切换不同的搜索策略
 */
public class SearchContext {
    
    private SearchStrategy strategy;
    
    /**
     * 构造函数，设置默认策略
     */
    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy != null ? strategy : new BasicSearchStrategy();
    }
    
    /**
     * 动态切换搜索策略
     */
    public void setStrategy(SearchStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
    }
    
    /**
     * 执行搜索
     */
    public List<ForumPost> executeSearch(SearchQuery query, List<ForumPost> posts) {
        if (strategy == null) {
            throw new IllegalStateException("搜索策略未设置");
        }
        return strategy.search(query, posts);
    }
    
    /**
     * 获取当前策略名称
     */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "未知策略";
    }
    
    /**
     * 获取当前策略描述
     */
    public String getCurrentStrategyDescription() {
        return strategy != null ? strategy.getStrategyDescription() : "无描述";
    }
    
    /**
     * 检查当前策略是否支持复杂查询
     */
    public boolean supportsComplexQueries() {
        return strategy != null && strategy.supportsComplexQueries();
    }
    
    /**
     * 获取当前策略
     */
    public SearchStrategy getCurrentStrategy() {
        return strategy;
    }
    
    /**
     * 检查是否为高级搜索策略
     */
    public boolean isAdvancedStrategy() {
        return strategy instanceof AdvancedSearchStrategy;
    }
    
    /**
     * 检查是否为基本搜索策略
     */
    public boolean isBasicStrategy() {
        return strategy instanceof BasicSearchStrategy;
    }
    
    /**
     * 获取策略类型信息
     */
    public String getStrategyInfo() {
        if (strategy == null) {
            return "无策略";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("策略名称: ").append(strategy.getStrategyName()).append("\n");
        info.append("策略描述: ").append(strategy.getStrategyDescription()).append("\n");
        info.append("支持复杂查询: ").append(strategy.supportsComplexQueries() ? "是" : "否");
        
        // 如果是高级策略，添加额外信息
        if (strategy instanceof AdvancedSearchStrategy) {
            AdvancedSearchStrategy advancedStrategy = (AdvancedSearchStrategy) strategy;
            info.append("\n").append("索引状态: ").append(advancedStrategy.getIndexStatistics());
            info.append("\n").append("索引平衡: ").append(advancedStrategy.isIndexBalanced() ? "是" : "否");
        }
        
        return info.toString();
    }
}