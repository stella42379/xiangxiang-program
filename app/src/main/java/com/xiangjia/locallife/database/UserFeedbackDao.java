package com.xiangjia.locallife.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xiangjia.locallife.model.UserFeedback;

import java.util.List;

@Dao
public interface UserFeedbackDao {
    
    @Insert
    long insertFeedback(UserFeedback feedback);
    
    @Query("SELECT * FROM user_feedback WHERE userId = :userId ORDER BY createTime DESC")
    List<UserFeedback> getFeedbackByUser(String userId);
    
    @Query("SELECT COUNT(*) FROM user_feedback WHERE userId = :userId")
    int getFeedbackCount(String userId);
}