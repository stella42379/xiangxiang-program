package com.xiangjia.locallife.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.xiangjia.locallife.model.User;
import java.util.List;

/**
 * 用户数据访问对象
 * 实现Task2设计要求中的UserDAO类
 */
@Dao
public interface UserDao {
    
    /**
     * 插入新用户
     */
    @Insert
    void insert(User user);
    
    /**
     * 更新用户信息
     */
    @Update
    void update(User user);
    
    /**
     * 删除用户
     */
    @Delete
    void delete(User user);
    
    /**
     * 根据用户ID获取用户
     */
    @Query("SELECT * FROM users WHERE userId = :userId AND status = 'active'")
    User getUserById(String userId);
    
    /**
     * 根据邮箱获取用户 (用于登录)
     */
    @Query("SELECT * FROM users WHERE email = :email AND status = 'active'")
    User getUserByEmail(String email);
    
    /**
     * 根据用户名获取用户
     */
    @Query("SELECT * FROM users WHERE username = :username AND status = 'active'")
    User getUserByUsername(String username);
    
    /**
     * 用户登录验证
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND status = 'active'")
    User loginUser(String email, String password);
    
    /**
     * 检查邮箱是否已存在
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
    
    /**
     * 检查用户名是否已存在
     */
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);
    
    /**
     * 获取所有活跃用户
     */
    @Query("SELECT * FROM users WHERE status = 'active' ORDER BY joinDate DESC")
    List<User> getAllActiveUsers();
    
    /**
     * 获取在线用户
     */
    @Query("SELECT * FROM users WHERE status = 'active' AND isOnline = 1 ORDER BY lastLoginTime DESC")
    List<User> getOnlineUsers();
    
    /**
     * 获取新注册用户
     */
    @Query("SELECT * FROM users WHERE status = 'active' ORDER BY joinDate DESC LIMIT :limit")
    List<User> getNewUsers(int limit);
    
    /**
     * 搜索用户 (用户名或昵称包含关键词)
     */
    @Query("SELECT * FROM users WHERE status = 'active' AND (username LIKE '%' || :keyword || '%' OR nickname LIKE '%' || :keyword || '%') ORDER BY lastLoginTime DESC")
    List<User> searchUsers(String keyword);
    
    /**
     * 获取活跃用户 (按发帖数排序)
     */
    @Query("SELECT * FROM users WHERE status = 'active' ORDER BY postCount DESC, messageCount DESC LIMIT :limit")
    List<User> getActiveUsers(int limit);
    
    /**
     * 更新用户最后登录时间
     */
    @Query("UPDATE users SET lastLoginTime = :lastLoginTime, isOnline = 1 WHERE userId = :userId")
    void updateLastLoginTime(String userId, long lastLoginTime);
    
    /**
     * 更新用户在线状态
     */
    @Query("UPDATE users SET isOnline = :isOnline WHERE userId = :userId")
    void updateOnlineStatus(String userId, boolean isOnline);
    
    /**
     * 更新用户发帖数
     */
    @Query("UPDATE users SET postCount = :postCount WHERE userId = :userId")
    void updatePostCount(String userId, int postCount);
    
    /**
     * 更新用户回复数
     */
    @Query("UPDATE users SET messageCount = :messageCount WHERE userId = :userId")
    void updateMessageCount(String userId, int messageCount);
    
    /**
     * 更新用户好友数
     */
    @Query("UPDATE users SET friendCount = :friendCount WHERE userId = :userId")
    void updateFriendCount(String userId, int friendCount);
    
    /**
     * 软删除用户 (将状态设置为deleted)
     */
    @Query("UPDATE users SET status = 'deleted' WHERE userId = :userId")
    void softDeleteUser(String userId);
    
    /**
     * 更新用户角色
     */
    @Query("UPDATE users SET userRole = :userRole WHERE userId = :userId")
    void updateUserRole(String userId, String userRole);
    
    /**
     * 更新用户密码
     */
    @Query("UPDATE users SET password = :password WHERE userId = :userId")
    void updatePassword(String userId, String password);
    
    /**
     * 获取管理员用户
     */
    @Query("SELECT * FROM users WHERE userRole = 'admin' AND status = 'active'")
    List<User> getAdminUsers();
    
    /**
     * 获取版主用户
     */
    @Query("SELECT * FROM users WHERE userRole = 'moderator' AND status = 'active'")
    List<User> getModeratorUsers();
    
    /**
     * 获取用户总数
     */
    @Query("SELECT COUNT(*) FROM users WHERE status = 'active'")
    int getTotalUserCount();
    
    /**
     * 批量插入用户 (用于数据初始化)
     */
    @Insert
    void insertUsers(List<User> users);
}