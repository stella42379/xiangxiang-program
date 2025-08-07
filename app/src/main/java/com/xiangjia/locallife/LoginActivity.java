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

/**
 * 登录Activity
 * 用户登录入口页面
 */
public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private TextView skipLoginTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Log.d(TAG, "LoginActivity onCreate");
        
        initViews();
        setupListeners();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        usernameEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        registerTextView = findViewById(R.id.tv_register);
        skipLoginTextView = findViewById(R.id.tv_skip_login);
        
        // 检查视图是否正确加载
        if (usernameEditText == null || passwordEditText == null || loginButton == null) {
            Log.e(TAG, "Failed to initialize views - check layout file");
            // 提供备用方案，直接跳转到MainActivity
            navigateToMainActivity();
            return;
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleLogin();
                }
            });
        }
        
        if (registerTextView != null) {
            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToRegister();
                }
            });
        }
        
        if (skipLoginTextView != null) {
            skipLoginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳过登录，直接进入主页
                    navigateToMainActivity();
                }
            });
        }
    }
    
    /**
     * 处理登录逻辑
     */
    private void handleLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // 输入验证
        if (TextUtils.isEmpty(username)) {
            showToast("请输入用户名");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        
        // 简单的登录验证（实际项目中应该调用API）
        if (isValidLogin(username, password)) {
            showToast("登录成功");
            // 保存登录状态
            saveLoginState(username);
            // 跳转到主页
            navigateToMainActivity();
        } else {
            showToast("用户名或密码错误");
        }
    }
    
    /**
     * 验证登录信息
     * 临时实现，实际应该调用后端API
     */
    private boolean isValidLogin(String username, String password) {
        // 简单验证逻辑，实际项目中需要调用API
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && password.length() >= 6;
    }
    
    /**
     * 保存登录状态
     */
    private void saveLoginState(String username) {
        // TODO: 使用SharedPreferences保存登录状态
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
     * 跳转到注册页面
     */
    private void navigateToRegister() {
        try {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to navigate to RegisterActivity", e);
            showToast("暂时无法打开注册页面");
        }
    }
    
    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "LoginActivity onResume");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "LoginActivity onPause");
    }
}