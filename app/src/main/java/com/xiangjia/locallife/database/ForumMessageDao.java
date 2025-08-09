package com.xiangjia.locallife.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.xiangjia.locallife.model.ForumMessage;
import java.util.List;

/**
 * 论坛消息数据访问对象
 * 实现Task2设计要求中的MessageDAO类
 * 提供按时间排序的消息访问功能
 */
@Dao
public interface ForumMessageDao {
    
    /**
     * 插入新消息
     */
    @Insert
    void insert(ForumMessage message);
    
    /**
     * 更新消息
     */
    @Update
    void update(ForumMessage message);
    
    /**
     * 删除消息
     */
    @Delete
    void delete(ForumMessage message);
    
    /**
     * 根据ID获取消息
     */
    @Query("SELECT * FROM forum_messages WHERE messageId = :messageId AND status = 'active'")
    ForumMessage getMessageById(String messageId);
    
    /**
     * 获取帖子的所有消息，按时间排序 (实现SortedArray功能)
     */
    @Query("SELECT * FROM forum_messages WHERE postId = :postId AND status = 'active' ORDER BY timestamp ASC")
    List<ForumMessage> getMessagesByPost(String postId);
    
    /**
     * 获取用户的所有消息
     */
    @Query("SELECT * FROM forum_messages WHERE authorId = :authorId AND status = 'active' ORDER BY timestamp DESC")
    List<ForumMessage> getMessagesByAuthor(String authorId);
    
    /**
     * 获取某条消息的回复
     */
    @Query("SELECT * FROM forum_messages WHERE parentMessageId = :parentMessageId AND status = 'active' ORDER BY timestamp ASC")
    List<ForumMessage> getRepliesByMessage(String parentMessageId);
    
    /**
     * 获取帖子的消息数量
     */
    @Query("SELECT COUNT(*) FROM forum_messages WHERE postId = :postId AND status = 'active'")
    int getMessageCountByPost(String postId);
    
    /**
     * 获取用户的消息数量
     */
    @Query("SELECT COUNT(*) FROM forum_messages WHERE authorId = :authorId AND status = 'active'")
    int getMessageCountByAuthor(String authorId);
    
    /**
     * 获取最新消息
     */
    @Query("SELECT * FROM forum_messages WHERE status = 'active' ORDER BY timestamp DESC LIMIT :limit")
    List<ForumMessage> getLatestMessages(int limit);
    
    /**
     * 搜索消息内容
     */
    @Query("SELECT * FROM forum_messages WHERE status = 'active' AND content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    List<ForumMessage> searchMessages(String keyword);
    
    /**
     * 获取热门消息 (按点赞数排序)
     */
    @Query("SELECT * FROM forum_messages WHERE status = 'active' ORDER BY likeCount DESC, timestamp DESC LIMIT :limit")
    List<ForumMessage> getPopularMessages(int limit);
    
    /**
     * 更新消息的点赞数
     */
    @Query("UPDATE forum_messages SET likeCount = :likeCount WHERE messageId = :messageId")
    void updateLikeCount(String messageId, int likeCount);
    
    /**
     * 软删除消息 (将状态设置为deleted)
     */
    @Query("UPDATE forum_messages SET status = 'deleted' WHERE messageId = :messageId")
    void softDeleteMessage(String messageId);
    
    /**
     * 获取帖子的分页消息数据
     */
    @Query("SELECT * FROM forum_messages WHERE postId = :postId AND status = 'active' ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    List<ForumMessage> getMessagesPaginated(String postId, int limit, int offset);
    
    /**
     * 获取楼主回复的消息
     */
    @Query("SELECT * FROM forum_messages WHERE postId = :postId AND isAuthorReply = 1 AND status = 'active' ORDER BY timestamp ASC")
    List<ForumMessage> getAuthorRepliesByPost(String postId);
    
    /**
     * 获取最近的消息时间戳 (用于更新帖子的最后活动时间)
     */
    @Query("SELECT MAX(timestamp) FROM forum_messages WHERE postId = :postId AND status = 'active'")
    Long getLatestMessageTimestamp(String postId);
    
    /**
     * 批量插入消息
     */
    @Insert
    void insertMessages(List<ForumMessage> messages);
    
    /**
     * 删除帖子相关的所有消息
     */
    @Query("UPDATE forum_messages SET status = 'deleted' WHERE postId = :postId")
    void deleteMessagesByPost(String postId);
}