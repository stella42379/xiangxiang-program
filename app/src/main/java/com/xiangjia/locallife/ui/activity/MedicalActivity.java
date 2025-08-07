package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xiangjia.locallife.R;

/**
 * 紧急送医Activity
 * 提供紧急医疗救助功能
 */
public class MedicalActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private Button emergencyCallButton;
    private Button nearestHospitalButton;
    private Button medicalRecordButton;
    private Button ambulanceButton;
    private TextView emergencyNumberText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        
        initViews();
        setupToolbar();
        setupListeners();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        emergencyCallButton = findViewById(R.id.emergency_call_button);
        nearestHospitalButton = findViewById(R.id.nearest_hospital_button);
        medicalRecordButton = findViewById(R.id.medical_record_button);
        ambulanceButton = findViewById(R.id.ambulance_button);
        emergencyNumberText = findViewById(R.id.emergency_number_text);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("紧急送医");
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 紧急呼叫按钮
        emergencyCallButton.setOnClickListener(v -> {
            showEmergencyCallDialog();
        });
        
        // 最近医院按钮
        nearestHospitalButton.setOnClickListener(v -> {
            navigateToNearestHospital();
        });
        
        // 医疗记录按钮
        medicalRecordButton.setOnClickListener(v -> {
            showMedicalRecord();
        });
        
        // 救护车按钮
        ambulanceButton.setOnClickListener(v -> {
            callAmbulance();
        });
    }
    
    /**
     * 显示紧急呼叫对话框
     */
    private void showEmergencyCallDialog() {
        new AlertDialog.Builder(this)
            .setTitle("紧急呼叫")
            .setMessage("确定要拨打紧急电话吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                callEmergency();
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    /**
     * 拨打紧急电话
     */
    private void callEmergency() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:120"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法拨打电话", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 导航到最近医院
     */
    private void navigateToNearestHospital() {
        // TODO: 实现导航到最近医院功能
        Toast.makeText(this, "正在查找最近医院...", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 显示医疗记录
     */
    private void showMedicalRecord() {
        // TODO: 显示用户医疗记录
        Toast.makeText(this, "医疗记录功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 呼叫救护车
     */
    private void callAmbulance() {
        new AlertDialog.Builder(this)
            .setTitle("呼叫救护车")
            .setMessage("确定要呼叫救护车吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                // TODO: 实现呼叫救护车功能
                Toast.makeText(this, "正在呼叫救护车...", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
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
