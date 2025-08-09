package com.xiangjia.locallife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.UserDao;
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.util.SharedPrefsUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 优化后的注册Activity
 * 集成数据库功能，完整的用户注册流程
 */
public class RegisterActivity extends AppCompatActivity {
    
    private static final String TAG = "RegisterActivity";
    
    // UI组件
    private Toolbar toolbar;
    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputLayout phoneLayout;
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private TextInputEditText phoneEditText;
    private MaterialButton registerButton;
    private MaterialCheckBox agreementCheckBox;
    private MaterialButton loginButton;
    
    // 数据库相关
    private AppDatabase database;
    private UserDao userDao;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Log.d(TAG, "RegisterActivity onCreate");
        
        // 初始化数据库
        initDatabase();
        
        // 初始化视图
        initViews();
        
        // 设置工具栏
        setupToolbar();
        
        // 设置监听器
        setupListeners();
    }
    
    /**
     * 初始化数据库
     */
    private void initDatabase() {
        database = AppDatabase.getInstance(this);
        userDao = database.userDao();
        executor = Executors.newSingleThreadExecutor();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        usernameLayout = findViewById(R.id.til_username);
        emailLayout = findViewById(R.id.til_email);
        passwordLayout = findViewById(R.id.til_password);
        confirmPasswordLayout = findViewById(R.id.til_confirm_password);
        phoneLayout = findViewById(R.id.til_phone);
        usernameEditText = findViewById(R.id.et_username);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        phoneEditText = findViewById(R.id.et_phone);
        registerButton = findViewById(R.id.btn_register);
        agreementCheckBox = findViewById(R.id.cb_agreement);
        loginButton = findViewById(R.id.btn_login);
        
        // 检查必要的视图是否存在
        if (usernameEditText == null || emailEditText == null || 
            passwordEditText == null || registerButton == null) {
            Log.e(TAG, "Critical views not found in layout");
            showToast("页面加载异常");
            finish();
            return;
        }
        
        // 设置默认状态
        if (agreementCheckBox != null) {
            agreementCheckBox.setChecked(true);
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
            
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        if (registerButton != null) {
            registerButton.setOnClickListener(v -> handleRegister());
        }
        
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> finish());
        }
    }
    
    /**
     * 处理注册逻辑
     */
    private void handleRegister() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText != null ? 
                confirmPasswordEditText.getText().toString().trim() : password;
        String phone = phoneEditText != null ? 
                phoneEditText.getText().toString().trim() : "";
        
        // 输入验证
        if (!validateInput(username, email, password, confirmPassword, phone)) {
            return;
        }
        
        // 检查用户协议
        if (agreementCheckBox != null && !agreementCheckBox.isChecked()) {
            showToast("请先同意用户协议");
            return;
        }
        
        // 显示加载状态
        registerButton.setEnabled(false);
        registerButton.setText("注册中...");
        
        // 异步执行注册
        executor.execute(() -> {
            try {
                // 检查用户名和邮箱是否已存在
                if (userDao.checkUsernameExists(username) > 0) {
                    runOnUiThread(() -> {
                        resetRegisterButton();
                        usernameLayout.setError("用户名已存在");
                        showToast("用户名已存在，请更换");
                    });
                    return;
                }
                
                if (userDao.checkEmailExists(email) > 0) {
                    runOnUiThread(() -> {
                        resetRegisterButton();
                        emailLayout.setError("邮箱已注册");
                        showToast("邮箱已注册，请直接登录");
                    });
                    return;
                }
                
                // 创建新用户
                User newUser = createNewUser(username, email, password, phone);
                userDao.insert(newUser);
                
                runOnUiThread(() -> {
                    resetRegisterButton();
                    showToast("注册成功，正在为您登录...");
                    
                    // 自动登录
                    handleAutoLogin(newUser);
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Registration failed", e);
                runOnUiThread(() -> {
                    resetRegisterButton();
                    showToast("注册失败，请重试");
                });
            }
        });
    }
    
    /**
     * 创建新用户对象
     */
    private User createNewUser(String username, String email, String password, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashPassword(password));
        user.setNickname(username); // 默认昵称为用户名
        user.setLocation(phone.isEmpty() ? "未设置" : "中国");
        user.setBio("这个人很懒，什么都没留下");
        
        return user;
    }
    
    /**
     * 处理自动登录
     */
    private void handleAutoLogin(User user) {
        // 保存登录状态
        SharedPrefsUtil.saveUserInfo(
            this,
            user.getUserId(),
            user.getUsername(),
            user.getNickname(),
            user.getEmail(),
            user.getAvatarUrl(),
            user.getUserRole()
        );
        
        // 更新用户登录时间
        executor.execute(() -> {
            userDao.updateLastLoginTime(user.getUserId(), System.currentTimeMillis());
            userDao.updateOnlineStatus(user.getUserId(), true);
        });
        
        // 跳转到主界面
        navigateToMainActivity();
    }
    
    /**
     * 验证输入信息
     */
    private boolean validateInput(String username, String email, String password, 
                                String confirmPassword, String phone) {
        boolean isValid = true;
        
        // 清除之前的错误信息
        clearErrors();
        
        // 验证用户名
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("请输入用户名");
            if (isValid) usernameEditText.requestFocus();
            isValid = false;
        } else if (username.length() < 3) {
            usernameLayout.setError("用户名至少3个字符");
            if (isValid) usernameEditText.requestFocus();
            isValid = false;
        } else if (username.length() > 20) {
            usernameLayout.setError("用户名不能超过20个字符");
            if (isValid) usernameEditText.requestFocus();
            isValid = false;
        } else if (!username.matches("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")) {
            usernameLayout.setError("用户名只能包含字母、数字、下划线和中文");
            if (isValid) usernameEditText.requestFocus();
            isValid = false;
        }
        
        // 验证邮箱
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("请输入邮箱");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("请输入有效的邮箱地址");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        }
        
        // 验证密码
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("请输入密码");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("密码至少6个字符");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() > 20) {
            passwordLayout.setError("密码不能超过20个字符");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        }
        
        // 验证确认密码
        if (confirmPasswordEditText != null) {
            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordLayout.setError("请确认密码");
                if (isValid) confirmPasswordEditText.requestFocus();
                isValid = false;
            } else if (!password.equals(confirmPassword)) {
                confirmPasswordLayout.setError("两次输入的密码不一致");
                if (isValid) confirmPasswordEditText.requestFocus();
                isValid = false;
            }
        }
        
        // 验证手机号（可选）
        if (phoneEditText != null && !TextUtils.isEmpty(phone)) {
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                phoneLayout.setError("请输入有效的手机号");
                if (isValid) phoneEditText.requestFocus();
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    /**
     * 清除错误信息
     */
    private void clearErrors() {
        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        if (confirmPasswordLayout != null) {
            confirmPasswordLayout.setError(null);
        }
        if (phoneLayout != null) {
            phoneLayout.setError(null);
        }
    }
    
    /**
     * 重置注册按钮状态
     */
    private void resetRegisterButton() {
        registerButton.setEnabled(true);
        registerButton.setText("立即注册");
    }
    
    /**
     * 密码加密
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Password hashing failed", e);
            return password; // 如果加密失败，返回原密码（不推荐在生产环境中使用）
        }
    }
    
    /**
     * 跳转到主界面
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}