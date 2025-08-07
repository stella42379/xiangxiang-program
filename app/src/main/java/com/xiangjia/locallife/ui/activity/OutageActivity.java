package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;

/**
 * 停水停电Activity
 * 提供停水停电信息查询和报修功能
 */
public class OutageActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextView statusText;
    private TextView estimatedTimeText;
    private TextView affectedAreaText;
    private Button reportButton;
    private Button queryButton;
    private ProgressBar progressBar;
    private RecyclerView outageListRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outage);
        
        initViews();
        setupToolbar();
        setupListeners();
        loadOutageInfo();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        statusText = findViewById(R.id.status_text);
        estimatedTimeText = findViewById(R.id.estimated_time_text);
        affectedAreaText = findViewById(R.id.affected_area_text);
        reportButton = findViewById(R.id.report_button);
        queryButton = findViewById(R.id.query_button);
        progressBar = findViewById(R.id.progress_bar);
        outageListRecyclerView = findViewById(R.id.outage_list_recycler_view);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("停水停电");
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        reportButton.setOnClickListener(v -> {
            reportOutage();
        });
        
        queryButton.setOnClickListener(v -> {
            queryOutageStatus();
        });
    }
    
    /**
     * 加载停水停电信息
     */
    private void loadOutageInfo() {
        showLoading(true);
        
        // TODO: 从网络API加载停水停电信息
        // 模拟加载数据
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                    showLoading(false);
                    displayOutageInfo();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 显示停水停电信息
     */
    private void displayOutageInfo() {
        // TODO: 显示停水停电信息
        statusText.setText("状态: 正常");
        estimatedTimeText.setText("预计恢复时间: 无");
        affectedAreaText.setText("影响区域: 无");
    }
    
    /**
     * 报修停水停电
     */
    private void reportOutage() {
        // TODO: 实现报修功能
        Toast.makeText(this, "报修功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 查询停水停电状态
     */
    private void queryOutageStatus() {
        // TODO: 实现查询功能
        Toast.makeText(this, "正在查询停水停电状态...", Toast.LENGTH_SHORT).show();
        loadOutageInfo();
    }
    
    /**
     * 显示加载状态
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
