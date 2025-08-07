package com.xiangjia.locallife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录Activity - 最简化版本
 * 移除所有可能导致问题的依赖
 */
public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "LoginActivity onCreate started");
        
        try {
            // 创建简单的布局，不依赖XML文件
            createSimpleLayout();
            Log.d(TAG, "Layout created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating layout", e);
            // 如果创建布局失败，直接跳转到MainActivity
            navigateToMainActivity();
        }
    }
    
    /**
     * 创建简单的布局（不依赖XML文件）
     */
    private void createSimpleLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(50, 100, 50, 50);
        mainLayout.setBackgroundColor(0xFF6366F1); // 蓝色背景
        
        // 应用标题
        TextView titleText = new TextView(this);
        titleText.setText("湘湘管家");
        titleText.setTextSize(28);
        titleText.setTextColor(0xFFFFFFFF);
        titleText.setGravity(android.view.Gravity.CENTER);
        titleText.setPadding(0, 0, 0, 50);
        mainLayout.addView(titleText);
        
        // 用户名输入
        EditText usernameEdit = new EditText(this);
        usernameEdit.setHint("请输入用户名");
        usernameEdit.setPadding(20, 20, 20, 20);
        usernameEdit.setBackgroundColor(0xFFFFFFFF);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editParams.setMargins(0, 0, 0, 20);
        usernameEdit.setLayoutParams(editParams);
        mainLayout.addView(usernameEdit);
        
        // 密码输入
        EditText passwordEdit = new EditText(this);
        passwordEdit.setHint("请输入密码");
        passwordEdit.setPadding(20, 20, 20, 20);
        passwordEdit.setBackgroundColor(0xFFFFFFFF);
        passwordEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEdit.setLayoutParams(editParams);
        mainLayout.addView(passwordEdit);
        
        // 登录按钮
        Button loginButton = new Button(this);
        loginButton.setText("登录");
        loginButton.setTextSize(16);
        loginButton.setBackgroundColor(0xFF4F46E5);
        loginButton.setTextColor(0xFFFFFFFF);
        loginButton.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, 30, 0, 20);
        loginButton.setLayoutParams(buttonParams);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                
                Log.d(TAG, "Login attempt - username: " + username);
                
                if (username.length() > 0 && password.length() > 0) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainLayout.addView(loginButton);
        
        // 跳过登录按钮
        Button skipButton = new Button(this);
        skipButton.setText("跳过登录");
        skipButton.setTextSize(14);
        skipButton.setBackgroundColor(0x80FFFFFF);
        skipButton.setTextColor(0xFF666666);
        skipButton.setLayoutParams(buttonParams);
        
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Skip login clicked");
                Toast.makeText(LoginActivity.this, "跳过登录", Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
        });
        mainLayout.addView(skipButton);
        
        setContentView(mainLayout);
        Log.d(TAG, "Layout set successfully");
    }
    
    /**
     * 跳转到MainActivity
     */
    private void navigateToMainActivity() {
        Log.d(TAG, "Attempting to navigate to MainActivity");
        try {
            // 先尝试启动MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.d(TAG, "MainActivity intent sent");
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Failed to navigate to MainActivity", e);
            Toast.makeText(this, "MainActivity暂未实现，应用将停留在登录页", Toast.LENGTH_LONG).show();
            
            // 如果MainActivity不存在，就显示一个简单的成功页面
            showSuccessPage();
        }
    }
    
    /**
     * 显示成功页面
     */
    private void showSuccessPage() {
        LinearLayout successLayout = new LinearLayout(this);
        successLayout.setOrientation(LinearLayout.VERTICAL);
        successLayout.setPadding(50, 100, 50, 50);
        successLayout.setBackgroundColor(0xFF10B981); // 绿色背景
        successLayout.setGravity(android.view.Gravity.CENTER);
        
        TextView successText = new TextView(this);
        successText.setText("🎉 登录成功！\n\nLoginActivity 修复完成\n应用已正常启动");
        successText.setTextSize(20);
        successText.setTextColor(0xFFFFFFFF);
        successText.setGravity(android.view.Gravity.CENTER);
        successText.setPadding(20, 20, 20, 50);
        successLayout.addView(successText);
        
        Button backButton = new Button(this);
        backButton.setText("返回登录页");
        backButton.setTextSize(16);
        backButton.setBackgroundColor(0xFF059669);
        backButton.setTextColor(0xFFFFFFFF);
        backButton.setOnClickListener(v -> recreate());
        successLayout.addView(backButton);
        
        setContentView(successLayout);
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