package com.xiangjia.locallife.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_addresses")
public class UserAddress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;           // 联系人姓名
    private String campus;         // 校区/区域
    private String address;        // 详细地址
    private String phone;          // 联系电话
    private boolean isDefault;     // 是否默认地址
    private long createTime;       // 创建时间
    private String userId;         // 用户ID
    
    // 构造函数
    public UserAddress() {}
    
    @Ignore
    public UserAddress(String name, String campus, String address, String phone, boolean isDefault, String userId) {
        this.name = name;
        this.campus = campus;
        this.address = address;
        this.phone = phone;
        this.isDefault = isDefault;
        this.userId = userId;
        this.createTime = System.currentTimeMillis();
    }
    
    // 所有getter和setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getFullAddress() {
        return campus + " " + address;
    }
}