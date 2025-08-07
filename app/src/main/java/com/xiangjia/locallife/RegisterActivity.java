package com.xiangjia.locallife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * 注册Activity
 * 用户注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    
    private static final String TAG = "RegisterActivity";
    
    private Toolbar toolbar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneEditText;
    private Button registerButton;
    private TextView loginTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Log.d(TAG, "RegisterActivity onCreate");
        
        initViews();
        setupToolbar();
        setupListeners();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        usernameEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        phoneEditText = findViewById(R.id.et_phone);
        registerButton = findViewById(R.id.btn_register);
        loginTextView = findViewById(R.id.tv_login);
        
        // 检查必要的视图是否存在
        if (usernameEditText == null || passwordEditText == null || registerButton == null) {
            Log.e(TAG, "Critical views not found in layout");
            showToast("页面加载异常");
            finish();
        }
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("用户注册");
            }
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        if (registerButton != null) {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleRegister();
                }
            });
        }
        
        if (loginTextView != null) {
            loginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 返回登录页面
                    finish();
                }
            });
        }
    }
    
    /**
     * 处理注册逻辑
     */
    private void handleRegister() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText != null ? 
                confirmPasswordEditText.getText().toString().trim() : password;
        String phone = phoneEditText != null ? 
                phoneEditText.getText().toString().trim() : "";
        
        // 输入验证
        if (!validateInput(username, password, confirmPassword, phone)) {
            return;
        }
        
        // 执行注册
        if (performRegister(username, password, phone)) {
            showToast("注册成功");
            // 自动登录并跳转到主页
            saveLoginState(username);
            navigateToMainActivity();
        } else {
            showToast("注册失败，请重试");
        }
    }
    
    /**
     * 验证输入信息
     */
    private boolean validateInput(String username, String password, String confirmPassword, String phone) {
        if (TextUtils.isEmpty(username)) {
            showToast("请输入用户名");
            return false;
        }
        
        if (username.length() < 3) {
            showToast("用户名至少3个字符");
            return false;
        }
        
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return false;
        }
        
        if (password.length() < 6) {
            showToast("密码至少6个字符");
            return false;
        }
        
        if (confirmPasswordEditText != null && !password.equals(confirmPassword)) {
            showToast("两次输入的密码不一致");
            return false;
        }
        
        if (phoneEditText != null && !TextUtils.isEmpty(phone)) {
            if (phone.length() != 11 || !phone.startsWith("1")) {
                showToast("请输入正确的手机号码");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 执行注册
     * 临时实现，实际应该调用后端API
     */
    private boolean performRegister(String username, String password, String phone) {
        // 简单的本地验证，实际项目中需要调用API
        try {
            // TODO: 调用注册API
            // 这里可以添加网络请求逻辑
            
            // 模拟网络延迟
            Thread.sleep(500);
            
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Registration failed", e);
            return false;
        }
    }
    
    /**
     * 保存登录状态
     */
    private void saveLoginState(String username) {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("is_logged_in", true)
                .putString("username", username)
                .apply();
    }
    
    /**
     * 跳转到主页
     */
    private void navigateToMainActivity() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Failed to navigate to MainActivity", e);
            showToast("启动失败，请重试");
        }
    }
    
    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RegisterActivity onResume");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "RegisterActivity onPause");
    }
}