package com.xiangjia.locallife.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xiangjia.locallife.model.ServiceOrder;

import java.util.List;

/**
 * 服务订单DAO接口
 * 定义服务订单相关的数据库操作
 */
@Dao
public interface ServiceOrderDao {
    
    /**
     * 插入服务订单
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServiceOrder order);
    
    /**
     * 批量插入服务订单
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ServiceOrder> orders);
    
    /**
     * 更新服务订单
     */
    @Update
    void update(ServiceOrder order);
    
    /**
     * 删除服务订单
     */
    @Delete
    void delete(ServiceOrder order);
    
    /**
     * 根据ID删除服务订单
     */
    @Query("DELETE FROM service_orders WHERE id = :id")
    void deleteById(int id);
    
    /**
     * 删除用户的所有服务订单
     */
    @Query("DELETE FROM service_orders WHERE userId = :userId")
    void deleteByUserId(String userId);
    
    /**
     * 根据ID获取服务订单
     */
    @Query("SELECT * FROM service_orders WHERE id = :id")
    ServiceOrder getById(int id);
    
    /**
     * 获取用户的所有服务订单
     */
    @Query("SELECT * FROM service_orders WHERE userId = :userId ORDER BY createTime DESC")
    LiveData<List<ServiceOrder>> getByUserId(String userId);
    
    /**
     * 根据订单类型获取服务订单
     */
    @Query("SELECT * FROM service_orders WHERE userId = :userId AND orderType = :orderType ORDER BY createTime DESC")
    LiveData<List<ServiceOrder>> getByUserIdAndType(String userId, String orderType);
    
    /**
     * 根据状态获取服务订单
     */
    @Query("SELECT * FROM service_orders WHERE userId = :userId AND status = :status ORDER BY createTime DESC")
    LiveData<List<ServiceOrder>> getByUserIdAndStatus(String userId, String status);
    
    /**
     * 获取所有服务订单
     */
    @Query("SELECT * FROM service_orders ORDER BY createTime DESC")
    LiveData<List<ServiceOrder>> getAll();
    
    /**
     * 获取待处理的服务订单
     */
    @Query("SELECT * FROM service_orders WHERE status = 'pending' ORDER BY createTime ASC")
    LiveData<List<ServiceOrder>> getPendingOrders();
    
    /**
     * 获取紧急服务订单
     */
    @Query("SELECT * FROM service_orders WHERE priority = 'urgent' ORDER BY createTime ASC")
    LiveData<List<ServiceOrder>> getUrgentOrders();
    
    /**
     * 获取服务订单总数
     */
    @Query("SELECT COUNT(*) FROM service_orders")
    int getCount();
    
    /**
     * 获取用户的订单总数
     */
    @Query("SELECT COUNT(*) FROM service_orders WHERE userId = :userId")
    int getCountByUserId(String userId);
    
    /**
     * 获取待处理订单数量
     */
    @Query("SELECT COUNT(*) FROM service_orders WHERE status = 'pending'")
    int getPendingCount();
}
