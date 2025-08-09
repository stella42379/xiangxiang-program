package com.xiangjia.locallife.model;

/**
 * 用户信息数据模型
 * 对应小程序的userInfo结构
 */
public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private String userId;
    private String openId;
    private boolean isAuthenticated;
    private String phoneNumber;
    private String email;
    private long lastLoginTime;
    private String userType; // 普通用户、VIP用户等
    
    public UserInfo() {
        // 默认构造函数
        this.nickName = "点击获取微信昵称";
        this.avatarUrl = "";
        this.userId = "138****5678";
        this.isAuthenticated = false;
        this.userType = "普通用户";
    }
    
    public UserInfo(String nickName, String avatarUrl, String userId) {
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        this.isAuthenticated = true;
        this.userType = "已认证用户";
        this.lastLoginTime = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getOpenId() {
        return openId;
    }
    
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public long getLastLoginTime() {
        return lastLoginTime;
    }
    
    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    /**
     * 获取显示用的用户状态
     */
    public String getDisplayStatus() {
        if (isAuthenticated) {
            return "已认证用户";
        } else {
            return "未认证用户";
        }
    }
    
    /**
     * 获取脱敏的手机号
     */
    public String getMaskedPhoneNumber() {
        if (phoneNumber != null && phoneNumber.length() >= 11) {
            return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
        }
        return userId; // 如果没有手机号，返回用户ID
    }
    
    @Override
    public String toString() {
        return "UserInfo{" +
                "nickName='" + nickName + '\'' +
                ", userId='" + userId + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", userType='" + userType + '\'' +
                '}';
    }
}