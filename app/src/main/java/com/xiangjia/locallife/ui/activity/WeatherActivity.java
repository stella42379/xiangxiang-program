package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;

/**
 * 天气预警Activity
 * 显示天气信息和预警
 */
public class WeatherActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextView currentTempText;
    private TextView weatherDescText;
    private TextView humidityText;
    private TextView windText;
    private TextView airQualityText;
    private ImageView weatherIcon;
    private ProgressBar progressBar;
    private RecyclerView forecastRecyclerView;
    private RecyclerView warningRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        
        initViews();
        setupToolbar();
        loadWeatherData();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        currentTempText = findViewById(R.id.current_temp_text);
        weatherDescText = findViewById(R.id.weather_desc_text);
        humidityText = findViewById(R.id.humidity_text);
        windText = findViewById(R.id.wind_text);
        airQualityText = findViewById(R.id.air_quality_text);
        weatherIcon = findViewById(R.id.weather_icon);
        progressBar = findViewById(R.id.progress_bar);
        forecastRecyclerView = findViewById(R.id.forecast_recycler_view);
        warningRecyclerView = findViewById(R.id.warning_recycler_view);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("天气预警");
        }
    }
    
    /**
     * 加载天气数据
     */
    private void loadWeatherData() {
        showLoading(true);
        
        // TODO: 从网络API加载天气数据
        // 模拟加载数据
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                    showLoading(false);
                    displayWeatherData();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 显示天气数据
     */
    private void displayWeatherData() {
        // TODO: 显示天气数据
        currentTempText.setText("25°C");
        weatherDescText.setText("晴");
        humidityText.setText("湿度: 65%");
        windText.setText("风力: 3级");
        airQualityText.setText("空气质量: 良好");
        
        // 加载天气预报
        loadWeatherForecast();
        
        // 加载天气预警
        loadWeatherWarning();
    }
    
    /**
     * 加载天气预报
     */
    private void loadWeatherForecast() {
        // TODO: 加载天气预报数据
    }
    
    /**
     * 加载天气预警
     */
    private void loadWeatherWarning() {
        // TODO: 加载天气预警数据
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
    
    /**
     * 刷新天气数据
     */
    private void refreshWeatherData() {
        loadWeatherData();
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
        // 刷新天气数据
        refreshWeatherData();
    }
}
