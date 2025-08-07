package com.xiangjia.locallife.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 服务订单模型
 * 用于管理各种服务订单（维修、送医、检查等）
 */
@Entity(tableName = "service_orders")
public class ServiceOrder {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String userId;
    private String orderType; // "repair", "medical", "inspection", "emergency"
    private String title;
    private String description;
    private String address;
    private String contactPhone;
    private String status; // "pending", "processing", "completed", "cancelled"
    private long createTime;
    private long updateTime;
    private String priority; // "low", "medium", "high", "urgent"
    private String assignedTo;
    private String notes;
    
    public ServiceOrder() {}
    
    public ServiceOrder(String userId, String orderType, String title, String description, 
                       String address, String contactPhone, String priority) {
        this.userId = userId;
        this.orderType = orderType;
        this.title = title;
        this.description = description;
        this.address = address;
        this.contactPhone = contactPhone;
        this.priority = priority;
        this.status = "pending";
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getOrderType() {
        return orderType;
    }
    
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
