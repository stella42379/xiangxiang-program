package com.xiangjia.locallife.strategy;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.search.SearchQuery;

import java.util.List;

/**
 * Strategy Pattern - 搜索策略接口
 * 定义不同的搜索算法实现
 */
public interface SearchStrategy {
    
    /**
     * 执行搜索
     * @param query 搜索查询
     * @param posts 帖子列表
     * @return 搜索结果
     */
    List<ForumPost> search(SearchQuery query, List<ForumPost> posts);
    
    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
    
    /**
     * 获取策略描述
     * @return 策略描述
     */
    default String getStrategyDescription() {
        return "搜索策略";
    }
    
    /**
     * 是否支持复杂查询
     * @return 是否支持
     */
    default boolean supportsComplexQueries() {
        return true;
    }
}