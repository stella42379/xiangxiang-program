package com.xiangjia.locallife.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.UUID;

/**
 * 用户模型类
 * 实现 Task2 设计要求中的 User 类
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String userId;           // 使用 UUID 作为唯一标识符

    private String username;         // 用户名
    private String email;            // 邮箱 (用于登录)
    private String password;         // 密码 (加密存储)
    private String nickname;         // 昵称
    private String avatarUrl;        // 头像 URL
    private String bio;              // 个人简介
    private String location;         // 所在地
    private long joinDate;           // 注册时间
    private long lastLoginTime;      // 最后登录时间
    private int postCount;           // 发帖数
    private int messageCount;        // 回复数
    private int friendCount;         // 好友数
    private String status;           // 用户状态 (active, banned, deleted)
    private String userRole;         // 用户角色 (user, moderator, admin)
    private boolean isOnline;        // 是否在线
    private String preferences;      // 用户偏好设置 (JSON 格式)

    /**
     * Room 使用的无参构造函数
     */
    public User() {
        this.userId = UUID.randomUUID().toString();
        this.joinDate = System.currentTimeMillis();
        this.lastLoginTime = this.joinDate;
        this.postCount = 0;
        this.messageCount = 0;
        this.friendCount = 0;
        this.status = "active";
        this.userRole = "user";
        this.isOnline = false;
    }

    /**
     * 应用使用的带参构造函数（忽略给 Room）
     */
    @Ignore
    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = username; // 默认昵称为用户名
    }

    // Getter 和 Setter 方法
    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getJoinDate() { return joinDate; }
    public void setJoinDate(long joinDate) { this.joinDate = joinDate; }

    public long getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(long lastLoginTime) { this.lastLoginTime = lastLoginTime; }

    public int getPostCount() { return postCount; }
    public void setPostCount(int postCount) { this.postCount = postCount; }

    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }

    public int getFriendCount() { return friendCount; }
    public void setFriendCount(int friendCount) { this.friendCount = friendCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    // 业务方法
    public void incrementPostCount() { this.postCount++; }
    public void incrementMessageCount() { this.messageCount++; }
    public void incrementFriendCount() { this.friendCount++; }
    public void decrementFriendCount() {
        if (this.friendCount > 0) {
            this.friendCount--;
        }
    }
    public void updateLastLoginTime() { this.lastLoginTime = System.currentTimeMillis(); }
    public boolean isAdmin() { return "admin".equals(this.userRole); }
    public boolean isModerator() { return "moderator".equals(this.userRole) || isAdmin(); }
}
