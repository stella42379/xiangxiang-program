package com.xiangjia.locallife.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.xiangjia.locallife.model.ForumPost;
import java.util.List;

/**
 * 论坛帖子数据访问对象
 * 实现Task2设计要求中的PostDAO类
 * 提供按时间排序的帖子访问功能
 */
@Dao
public interface ForumPostDao {
    
    /**
     * 插入新帖子
     */
    @Insert
    void insert(ForumPost post);
    
    /**
     * 更新帖子
     */
    @Update
    void update(ForumPost post);
    
    /**
     * 删除帖子
     */
    @Delete
    void delete(ForumPost post);
    
    /**
     * 根据ID获取帖子
     */
    @Query("SELECT * FROM forum_posts WHERE postId = :postId AND status = 'active'")
    ForumPost getPostById(String postId);
    
    /**
     * 获取所有活跃帖子，按最后活动时间排序 (实现SortedArray功能)
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' ORDER BY lastActivityTime DESC")
    List<ForumPost> getAllPostsSortedByActivity();
    
    /**
     * 获取置顶帖子，按最后活动时间排序
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' AND isPinned = 1 ORDER BY lastActivityTime DESC")
    List<ForumPost> getPinnedPosts();
    
    /**
     * 根据分类获取帖子，按最后活动时间排序
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' AND category = :category ORDER BY lastActivityTime DESC")
    List<ForumPost> getPostsByCategory(String category);
    
    /**
     * 根据作者ID获取帖子
     */
    @Query("SELECT * FROM forum_posts WHERE authorId = :authorId AND status = 'active' ORDER BY timestamp DESC")
    List<ForumPost> getPostsByAuthor(String authorId);
    
    /**
     * 搜索帖子 (标题或内容包含关键词)
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY lastActivityTime DESC")
    List<ForumPost> searchPosts(String keyword);
    
    /**
     * 获取热门帖子 (按点赞数排序)
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' ORDER BY likeCount DESC, lastActivityTime DESC LIMIT :limit")
    List<ForumPost> getPopularPosts(int limit);
    
    /**
     * 获取最新帖子
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' ORDER BY timestamp DESC LIMIT :limit")
    List<ForumPost> getLatestPosts(int limit);
    
    /**
     * 获取用户的帖子数量
     */
    @Query("SELECT COUNT(*) FROM forum_posts WHERE authorId = :authorId AND status = 'active'")
    int getPostCountByAuthor(String authorId);
    
    /**
     * 更新帖子的点赞数
     */
    @Query("UPDATE forum_posts SET likeCount = :likeCount, lastActivityTime = :lastActivityTime WHERE postId = :postId")
    void updateLikeCount(String postId, int likeCount, long lastActivityTime);
    
    /**
     * 更新帖子的回复数
     */
    @Query("UPDATE forum_posts SET replyCount = :replyCount, lastActivityTime = :lastActivityTime WHERE postId = :postId")
    void updateReplyCount(String postId, int replyCount, long lastActivityTime);
    
    /**
     * 更新帖子的浏览数
     */
    @Query("UPDATE forum_posts SET viewCount = :viewCount WHERE postId = :postId")
    void updateViewCount(String postId, int viewCount);
    
    /**
     * 软删除帖子 (将状态设置为deleted)
     */
    @Query("UPDATE forum_posts SET status = 'deleted' WHERE postId = :postId")
    void softDeletePost(String postId);
    
    /**
     * 置顶/取消置顶帖子
     */
    @Query("UPDATE forum_posts SET isPinned = :isPinned WHERE postId = :postId")
    void updatePinnedStatus(String postId, boolean isPinned);
    
    /**
     * 获取所有分类
     */
    @Query("SELECT DISTINCT category FROM forum_posts WHERE status = 'active'")
    List<String> getAllCategories();
    
    /**
     * 获取分页帖子数据
     */
    @Query("SELECT * FROM forum_posts WHERE status = 'active' ORDER BY lastActivityTime DESC LIMIT :limit OFFSET :offset")
    List<ForumPost> getPostsPaginated(int limit, int offset);
    
    /**
     * 获取总帖子数
     */
    @Query("SELECT COUNT(*) FROM forum_posts WHERE status = 'active'")
    int getTotalPostCount();
}