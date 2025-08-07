package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xiangjia.locallife.R;

/**
 * 故障报修Activity
 * 提供报修功能
 */
public class RepairActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private Spinner repairTypeSpinner;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText addressEditText;
    private EditText contactPhoneEditText;
    private Spinner prioritySpinner;
    private Button submitButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        
        initViews();
        setupToolbar();
        setupListeners();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        repairTypeSpinner = findViewById(R.id.repair_type_spinner);
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        contactPhoneEditText = findViewById(R.id.contact_phone_edit_text);
        prioritySpinner = findViewById(R.id.priority_spinner);
        submitButton = findViewById(R.id.submit_button);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("故障报修");
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        submitButton.setOnClickListener(v -> {
            submitRepairOrder();
        });
    }
    
    /**
     * 提交报修订单
     */
    private void submitRepairOrder() {
        // 获取表单数据
        String repairType = repairTypeSpinner.getSelectedItem().toString();
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String contactPhone = contactPhoneEditText.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();
        
        // 验证表单数据
        if (!validateForm(title, description, address, contactPhone)) {
            return;
        }
        
        // 创建报修订单
        createRepairOrder(repairType, title, description, address, contactPhone, priority);
    }
    
    /**
     * 验证表单数据
     */
    private boolean validateForm(String title, String description, String address, String contactPhone) {
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入报修标题", Toast.LENGTH_SHORT).show();
            titleEditText.requestFocus();
            return false;
        }
        
        if (description.isEmpty()) {
            Toast.makeText(this, "请输入故障描述", Toast.LENGTH_SHORT).show();
            descriptionEditText.requestFocus();
            return false;
        }
        
        if (address.isEmpty()) {
            Toast.makeText(this, "请输入报修地址", Toast.LENGTH_SHORT).show();
            addressEditText.requestFocus();
            return false;
        }
        
        if (contactPhone.isEmpty()) {
            Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
            contactPhoneEditText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * 创建报修订单
     */
    private void createRepairOrder(String repairType, String title, String description, 
                                  String address, String contactPhone, String priority) {
        // TODO: 创建报修订单并保存到数据库
        // 模拟创建订单
        Toast.makeText(this, "报修订单提交成功", Toast.LENGTH_SHORT).show();
        
        // 返回上一页
        finish();
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
