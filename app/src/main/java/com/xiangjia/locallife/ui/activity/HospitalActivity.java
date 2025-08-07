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
 * 医院导航Activity
 * 提供医院导航功能
 */
public class HospitalActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextView currentLocationText;
    private Button locateButton;
    private Button searchButton;
    private ProgressBar progressBar;
    private RecyclerView hospitalListRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        
        initViews();
        setupToolbar();
        setupListeners();
        loadHospitalData();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        currentLocationText = findViewById(R.id.current_location_text);
        locateButton = findViewById(R.id.locate_button);
        searchButton = findViewById(R.id.search_button);
        progressBar = findViewById(R.id.progress_bar);
        hospitalListRecyclerView = findViewById(R.id.hospital_list_recycler_view);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("医院导航");
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        locateButton.setOnClickListener(v -> {
            getCurrentLocation();
        });
        
        searchButton.setOnClickListener(v -> {
            searchHospitals();
        });
    }
    
    /**
     * 加载医院数据
     */
    private void loadHospitalData() {
        showLoading(true);
        
        // TODO: 从网络API加载医院数据
        // 模拟加载数据
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                    showLoading(false);
                    displayHospitalData();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 显示医院数据
     */
    private void displayHospitalData() {
        // TODO: 显示医院数据
        currentLocationText.setText("当前位置: 正在定位...");
    }
    
    /**
     * 获取当前位置
     */
    private void getCurrentLocation() {
        // TODO: 实现获取当前位置功能
        Toast.makeText(this, "正在获取当前位置...", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 搜索医院
     */
    private void searchHospitals() {
        // TODO: 实现搜索医院功能
        Toast.makeText(this, "搜索医院功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 导航到医院
     */
    private void navigateToHospital(String hospitalName, double latitude, double longitude) {
        // TODO: 实现导航到医院功能
        Toast.makeText(this, "正在导航到" + hospitalName, Toast.LENGTH_SHORT).show();
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
        // 刷新医院数据
        loadHospitalData();
    }
}
