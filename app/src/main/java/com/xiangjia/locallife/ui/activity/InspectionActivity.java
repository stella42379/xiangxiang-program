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
import com.xiangjia.locallife.ui.adapter.CheckItemAdapter;

/**
 * 日常检查Activity
 * 提供日常检查功能
 */
public class InspectionActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextView inspectionStatusText;
    private TextView lastInspectionText;
    private Button startInspectionButton;
    private Button viewHistoryButton;
    private ProgressBar progressBar;
    private RecyclerView checkItemRecyclerView;
    private CheckItemAdapter checkItemAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        
        initViews();
        setupToolbar();
        initRecyclerView();
        setupListeners();
        loadInspectionData();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        inspectionStatusText = findViewById(R.id.inspection_status_text);
        lastInspectionText = findViewById(R.id.last_inspection_text);
        startInspectionButton = findViewById(R.id.start_inspection_button);
        viewHistoryButton = findViewById(R.id.view_history_button);
        progressBar = findViewById(R.id.progress_bar);
        checkItemRecyclerView = findViewById(R.id.check_item_recycler_view);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("日常检查");
        }
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        checkItemAdapter = new CheckItemAdapter();
        checkItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkItemRecyclerView.setAdapter(checkItemAdapter);
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        startInspectionButton.setOnClickListener(v -> {
            startInspection();
        });
        
        viewHistoryButton.setOnClickListener(v -> {
            viewInspectionHistory();
        });
    }
    
    /**
     * 加载检查数据
     */
    private void loadInspectionData() {
        showLoading(true);
        
        // TODO: 从数据库加载检查数据
        // 模拟加载数据
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                    showLoading(false);
                    displayInspectionData();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 显示检查数据
     */
    private void displayInspectionData() {
        // TODO: 显示检查数据
        inspectionStatusText.setText("检查状态: 待检查");
        lastInspectionText.setText("上次检查: 2024-01-01");
        
        // 加载检查项目列表
        loadCheckItems();
    }
    
    /**
     * 加载检查项目列表
     */
    private void loadCheckItems() {
        // TODO: 加载检查项目列表
    }
    
    /**
     * 开始检查
     */
    private void startInspection() {
        // TODO: 实现开始检查功能
        Toast.makeText(this, "开始日常检查...", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 查看检查历史
     */
    private void viewInspectionHistory() {
        // TODO: 实现查看检查历史功能
        Toast.makeText(this, "查看检查历史...", Toast.LENGTH_SHORT).show();
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
    
    @Override
    public void onResume() {
        super.onResume();
        // 刷新检查数据
        loadInspectionData();
    }
}
