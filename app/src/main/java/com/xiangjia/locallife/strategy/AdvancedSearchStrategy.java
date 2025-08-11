package com.xiangjia.locallife.strategy;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.search.SearchQuery;
import com.xiangjia.locallife.datastructure.AVLTree;
import com.xiangjia.locallife.datastructure.PostSearchKey;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 高级搜索策略 - 使用AVL树优化搜索性能
 * Strategy Pattern实现
 */
public class AdvancedSearchStrategy implements SearchStrategy {
    
    // 不同排序类型的AVL树索引
    private AVLTree<PostSearchKey> timestampTree;
    private AVLTree<PostSearchKey> likeCountTree;
    private AVLTree<PostSearchKey> replyCountTree;
    private AVLTree<PostSearchKey> viewCountTree;
    
    // 帖子ID到帖子对象的映射
    private Map<String, ForumPost> postMap;
    
    // 索引是否已建立
    private boolean indexBuilt = false;
    
    public AdvancedSearchStrategy() {
        this.timestampTree = new AVLTree<>();
        this.likeCountTree = new AVLTree<>();
        this.replyCountTree = new AVLTree<>();
        this.viewCountTree = new AVLTree<>();
        this.postMap = new HashMap<>();
    }
    
    /**
     * 为帖子列表建立索引
     */
    public void buildIndex(List<ForumPost> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        
        // 清空现有索引
        clearIndex();
        
        // 建立新索引
        for (ForumPost post : posts) {
            if (post == null || post.getPostId() == null) continue;
            
            // 添加到映射表
            postMap.put(post.getPostId(), post);
            
            // 添加到各种排序树
            PostSearchKey timestampKey = new PostSearchKey(post, PostSearchKey.SortType.TIMESTAMP);
            PostSearchKey likeKey = new PostSearchKey(post, PostSearchKey.SortType.LIKE_COUNT);
            PostSearchKey replyKey = new PostSearchKey(post, PostSearchKey.SortType.REPLY_COUNT);
            PostSearchKey viewKey = new PostSearchKey(post, PostSearchKey.SortType.VIEW_COUNT);
            
            timestampTree.insert(timestampKey);
            likeCountTree.insert(likeKey);
            replyCountTree.insert(replyKey);
            viewCountTree.insert(viewKey);
        }
        
        indexBuilt = true;
    }
    
    /**
     * 清空索引
     */
    private void clearIndex() {
        timestampTree.clear();
        likeCountTree.clear();
        replyCountTree.clear();
        viewCountTree.clear();
        postMap.clear();
        indexBuilt = false;
    }
    
    @Override
    public List<ForumPost> search(SearchQuery query, List<ForumPost> posts) {
        if (query == null || posts == null) {
            return new ArrayList<>();
        }
        
        // 如果索引未建立，先建立索引
        if (!indexBuilt) {
            buildIndex(posts);
        }
        
        // 使用树结构进行优化搜索
        List<ForumPost> sortedPosts = getSortedPosts(PostSearchKey.SortType.TIMESTAMP);
        
        // 在排序后的结果中执行搜索
        BasicSearchStrategy basicStrategy = new BasicSearchStrategy();
        return basicStrategy.search(query, sortedPosts);
    }
    
    /**
     * 获取按指定类型排序的帖子列表
     */
    public List<ForumPost> getSortedPosts(PostSearchKey.SortType sortType) {
        if (!indexBuilt) {
            return new ArrayList<>();
        }
        
        AVLTree<PostSearchKey> tree = getTreeBySortType(sortType);
        List<PostSearchKey> keys = tree.inorderTraversal();
        
        List<ForumPost> sortedPosts = new ArrayList<>();
        for (PostSearchKey key : keys) {
            ForumPost post = postMap.get(key.getPostId());
            if (post != null) {
                sortedPosts.add(post);
            }
        }
        
        return sortedPosts;
    }
    
    /**
     * 范围查询 - 查找在指定范围内的帖子
     */
    public List<ForumPost> rangeQuery(PostSearchKey.SortType sortType, long minValue, long maxValue) {
        if (!indexBuilt) {
            return new ArrayList<>();
        }
        
        AVLTree<PostSearchKey> tree = getTreeBySortType(sortType);
        
        // 创建范围查询的键
        PostSearchKey minKey = createSearchKey("", minValue, sortType);
        PostSearchKey maxKey = createSearchKey("", maxValue, sortType);
        
        List<PostSearchKey> rangeKeys = tree.rangeQuery(minKey, maxKey);
        
        List<ForumPost> result = new ArrayList<>();
        for (PostSearchKey key : rangeKeys) {
            ForumPost post = postMap.get(key.getPostId());
            if (post != null) {
                result.add(post);
            }
        }
        
        return result;
    }
    
    /**
     * 快速查找热门帖子 (点赞数排序)
     */
    public List<ForumPost> getPopularPosts(int limit) {
        List<ForumPost> sortedByLikes = getSortedPosts(PostSearchKey.SortType.LIKE_COUNT);
        
        // 倒序获取最热门的帖子
        List<ForumPost> popular = new ArrayList<>();
        for (int i = sortedByLikes.size() - 1; i >= 0 && popular.size() < limit; i--) {
            popular.add(sortedByLikes.get(i));
        }
        
        return popular;
    }
    
    /**
     * 快速查找最新帖子 (时间排序)
     */
    public List<ForumPost> getLatestPosts(int limit) {
        List<ForumPost> sortedByTime = getSortedPosts(PostSearchKey.SortType.TIMESTAMP);
        
        // 倒序获取最新的帖子
        List<ForumPost> latest = new ArrayList<>();
        for (int i = sortedByTime.size() - 1; i >= 0 && latest.size() < limit; i--) {
            latest.add(sortedByTime.get(i));
        }
        
        return latest;
    }
    
    /**
     * 根据排序类型获取对应的树
     */
    private AVLTree<PostSearchKey> getTreeBySortType(PostSearchKey.SortType sortType) {
        switch (sortType) {
            case TIMESTAMP:
                return timestampTree;
            case LIKE_COUNT:
                return likeCountTree;
            case REPLY_COUNT:
                return replyCountTree;
            case VIEW_COUNT:
                return viewCountTree;
            default:
                return timestampTree;
        }
    }
    
    /**
     * 创建搜索键
     */
    private PostSearchKey createSearchKey(String postId, long value, PostSearchKey.SortType sortType) {
        switch (sortType) {
            case TIMESTAMP:
                return new PostSearchKey(postId, value, 0, 0, 0, sortType);
            case LIKE_COUNT:
                return new PostSearchKey(postId, 0, (int)value, 0, 0, sortType);
            case REPLY_COUNT:
                return new PostSearchKey(postId, 0, 0, (int)value, 0, sortType);
            case VIEW_COUNT:
                return new PostSearchKey(postId, 0, 0, 0, (int)value, sortType);
            default:
                return new PostSearchKey(postId, value, 0, 0, 0, PostSearchKey.SortType.TIMESTAMP);
        }
    }
    
    /**
     * 获取索引统计信息
     */
    public String getIndexStatistics() {
        if (!indexBuilt) {
            return "索引未建立";
        }
        
        return String.format("索引统计: 帖子数=%d, 时间树高度=%d, 点赞树高度=%d", 
                           postMap.size(), 
                           timestampTree.getTreeHeight(), 
                           likeCountTree.getTreeHeight());
    }
    
    /**
     * 检查索引是否平衡
     */
    public boolean isIndexBalanced() {
        if (!indexBuilt) return false;
        
        return timestampTree.isBalanced() && 
               likeCountTree.isBalanced() && 
               replyCountTree.isBalanced() && 
               viewCountTree.isBalanced();
    }
    
    @Override
    public String getStrategyName() {
        return "高级搜索";
    }
    
    @Override
    public String getStrategyDescription() {
        return "使用AVL树索引优化的高性能搜索策略，支持快速排序和范围查询";
    }
    
    @Override
    public boolean supportsComplexQueries() {
        return true;
    }
}