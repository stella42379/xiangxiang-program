package com.xiangjia.locallife.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xiangjia.locallife.model.UserAddress;

import java.util.List;

/**
 * 用户地址DAO接口
 * 定义用户地址相关的数据库操作
 */
@Dao
public interface UserAddressDao {
    
    /**
     * 插入用户地址
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserAddress address);
    
    /**
     * 批量插入用户地址
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserAddress> addresses);
    
    /**
     * 更新用户地址
     */
    @Update
    void update(UserAddress address);
    
    /**
     * 删除用户地址
     */
    @Delete
    void delete(UserAddress address);
    
    /**
     * 根据ID删除用户地址
     */
    @Query("DELETE FROM user_addresses WHERE id = :id")
    void deleteById(int id);
    
    /**
     * 删除用户的所有地址
     */
    @Query("DELETE FROM user_addresses WHERE userId = :userId")
    void deleteByUserId(String userId);
    
    /**
     * 根据ID获取用户地址
     */
    @Query("SELECT * FROM user_addresses WHERE id = :id")
    UserAddress getById(int id);
    
    /**
     * 获取用户的所有地址
     */
    @Query("SELECT * FROM user_addresses WHERE userId = :userId ORDER BY isDefault DESC, createTime DESC")
    LiveData<List<UserAddress>> getByUserId(String userId);
    
    /**
     * 获取用户的默认地址
     */
    @Query("SELECT * FROM user_addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    UserAddress getDefaultAddress(String userId);
    
   
    
    /**
     * 获取所有用户地址
     */
    @Query("SELECT * FROM user_addresses ORDER BY createTime DESC")
    LiveData<List<UserAddress>> getAll();
    
    /**
     * 设置默认地址
     */
    @Query("UPDATE user_addresses SET isDefault = 0 WHERE userId = :userId")
    void clearDefaultAddress(String userId);
    
    /**
     * 设置指定地址为默认地址
     */
    @Query("UPDATE user_addresses SET isDefault = 1 WHERE id = :id")
    void setDefaultAddress(int id);
    
    /**
     * 获取用户地址总数
     */
    @Query("SELECT COUNT(*) FROM user_addresses")
    int getCount();
    
    /**
     * 获取用户的地址总数
     */
    @Query("SELECT COUNT(*) FROM user_addresses WHERE userId = :userId")
    int getCountByUserId(String userId);
    
    /**
     * 检查用户是否有默认地址
     */
    @Query("SELECT COUNT(*) FROM user_addresses WHERE userId = :userId AND isDefault = 1")
    int hasDefaultAddress(String userId);
}
