package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.xiangjia.locallife.R;

/**
 * 用户资料页面（简化版本）
 */
public class UserProfileActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String userId = getIntent().getStringExtra("userId");
        
        // 简化实现，显示Toast
        Toast.makeText(this, "用户资料页面开发中\n用户ID: " + userId, Toast.LENGTH_LONG).show();
        
        // 返回上一页
        finish();
    }
}