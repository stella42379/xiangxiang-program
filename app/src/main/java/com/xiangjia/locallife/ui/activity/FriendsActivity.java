package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.xiangjia.locallife.R;

/**
 * 好友列表页面（简化版本）
 */
public class FriendsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 简化实现，显示Toast
        Toast.makeText(this, "好友列表页面开发中", Toast.LENGTH_LONG).show();
        
        // 返回上一页
        finish();
    }
}