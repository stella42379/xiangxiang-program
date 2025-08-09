package com.xiangjia.locallife.model;

/**
 * 通知项数据模型
 * 对应小程序的notifications结构
 */
public class NotificationItem {
    private int id;
    private String icon;
    private String iconClass;
    private String title;
    private String desc;
    private String time;
    private boolean isNew;
    private String type; // 通知类型：system, service, news等
    private String actionUrl; // 点击跳转的URL或页面
    private long timestamp; // 时间戳
    
    public NotificationItem() {
        // 默认构造函数
    }
    
    public NotificationItem(int id, String icon, String iconClass, String title, 
                          String desc, String time, boolean isNew) {
        this.id = id;
        this.icon = icon;
        this.iconClass = iconClass;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.isNew = isNew;
        this.timestamp = System.currentTimeMillis();
    }
    
    public NotificationItem(String icon, String iconClass, String title, 
                          String desc, String time, boolean isNew) {
        this.icon = icon;
        this.iconClass = iconClass;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.isNew = isNew;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public boolean isNew() {
        return isNew;
    }
    
    public void setNew(boolean aNew) {
        isNew = aNew;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 获取图标颜色（根据iconClass）
     */
    public int getIconColor() {
        switch (iconClass) {
            case "icon-red":
                return android.graphics.Color.parseColor("#EF4444");
            case "icon-green":
                return android.graphics.Color.parseColor("#10B981");
            case "icon-blue":
                return android.graphics.Color.parseColor("#3B82F6");
            case "icon-yellow":
                return android.graphics.Color.parseColor("#F59E0B");
            case "icon-purple":
                return android.graphics.Color.parseColor("#8B5CF6");
            default:
                return android.graphics.Color.parseColor("#6B7280");
        }
    }
    
    /**
     * 创建系统通知
     */
    public static NotificationItem createSystemNotification(String title, String desc) {
        return new NotificationItem("🔔", "icon-red", title, desc, "刚刚", true);
    }
    
    /**
     * 创建服务通知
     */
    public static NotificationItem createServiceNotification(String title, String desc) {
        return new NotificationItem("✅", "icon-green", title, desc, "刚刚", true);
    }
    
    /**
     * 创建新闻通知
     */
    public static NotificationItem createNewsNotification(String title, String desc) {
        return new NotificationItem("📰", "icon-blue", title, desc, "刚刚", true);
    }
    
    @Override
    public String toString() {
        return "NotificationItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", isNew=" + isNew +
                '}';
    }
}