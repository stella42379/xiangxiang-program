package com.xiangjia.locallife;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
 * 优化后的登录Activity - 修复Lambda变量捕获问题
 * 参考小程序UI设计，集成数据库功能
 */
public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    
    // UI组件
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton wechatLoginButton;
    private MaterialButton registerButton;
    private MaterialButton skipButton;
    private MaterialCheckBox agreementCheckBox;
    
    // 数据库相关
    private AppDatabase database;
    private UserDao userDao;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "LoginActivity onCreate");
        
        // 检查是否已经登录
        if (isUserLoggedIn()) {
            Log.d(TAG, "User already logged in, navigating to MainActivity");
            navigateToMainActivity();
            return;
        }
        
        // 初始化数据库
        initDatabase();
        
        // 设置内容视图
        setContentView(R.layout.activity_login);
        
        // 初始化视图
        initViews();
        
        // 设置监听器
        setupListeners();
        
        Log.d(TAG, "LoginActivity initialized successfully");
    }
    
    /**
     * 检查用户是否已登录
     */
    private boolean isUserLoggedIn() {
        try {
            return SharedPrefsUtil.isLoggedIn(this);
        } catch (Exception e) {
            Log.w(TAG, "检查登录状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 初始化数据库
     */
    private void initDatabase() {
        try {
            database = AppDatabase.getInstance(this);
            userDao = database.userDao();
            executor = Executors.newSingleThreadExecutor();
        } catch (Exception e) {
            Log.e(TAG, "数据库初始化失败: " + e.getMessage());
        }
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        usernameLayout = findViewById(R.id.til_username);
        passwordLayout = findViewById(R.id.til_password);
        usernameEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        wechatLoginButton = findViewById(R.id.btn_wechat_login);
        registerButton = findViewById(R.id.tv_register);
        skipButton = findViewById(R.id.tv_skip_login);
        agreementCheckBox = findViewById(R.id.cb_agreement);
        
        // 检查必要组件
        if (usernameEditText == null || passwordEditText == null || loginButton == null) {
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
     * 设置监听器
     */
    private void setupListeners() {
        // 登录按钮
        loginButton.setOnClickListener(v -> handleLogin());
        
        // 微信登录按钮
        if (wechatLoginButton != null) {
            wechatLoginButton.setOnClickListener(v -> handleWeChatLogin());
        }
        
        // 注册按钮
        if (registerButton != null) {
            registerButton.setOnClickListener(v -> navigateToRegister());
        }
        
        // 跳过登录按钮
        if (skipButton != null) {
            skipButton.setOnClickListener(v -> handleSkipLogin());
        }
    }
    
    /**
     * 处理登录逻辑 - 修复Lambda变量捕获问题
     */
    private void handleLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // 输入验证
        if (!validateInput(username, password)) {
            return;
        }
        
        // 检查用户协议
        if (agreementCheckBox != null && !agreementCheckBox.isChecked()) {
            showToast("请先同意用户协议");
            return;
        }
        
        // 显示加载状态
        loginButton.setEnabled(false);
        loginButton.setText("登录中...");
        
        // 🔧 修复方案：在lambda外部创建final变量
        final String finalUsername = username;
        final String finalPassword = password;
        
        // 异步执行登录
        if (executor != null && userDao != null) {
            executor.execute(() -> {
                User foundUser = null;
                try {
                    // 加密密码
                    String hashedPassword = hashPassword(finalPassword);
                    
                    // 尝试邮箱登录
                    foundUser = userDao.loginUser(finalUsername, hashedPassword);
                    
                    // 如果邮箱登录失败，尝试用户名登录
                    if (foundUser == null) {
                        User userByUsername = userDao.getUserByUsername(finalUsername);
                        if (userByUsername != null && userByUsername.getPassword().equals(hashedPassword)) {
                            foundUser = userByUsername;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Login query failed", e);
                }
                
                // 🔧 修复方案：创建final副本用于lambda捕获
                final User finalUser = foundUser;
                
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("登录");
                    
                    if (finalUser != null) {
                        // 登录成功
                        handleLoginSuccess(finalUser);
                    } else {
                        // 登录失败
                        showToast("用户名或密码错误");
                    }
                });
            });
        } else {
            // 数据库未初始化的fallback
            loginButton.setEnabled(true);
            loginButton.setText("登录");
            showToast("数据库未初始化，请重试");
        }
    }
    
    /**
     * 处理登录成功
     */
    private void handleLoginSuccess(User user) {
        try {
            // 更新用户最后登录时间
            if (executor != null && userDao != null) {
                executor.execute(() -> {
                    try {
                        userDao.updateLastLoginTime(user.getUserId(), System.currentTimeMillis());
                        userDao.updateOnlineStatus(user.getUserId(), true);
                    } catch (Exception e) {
                        Log.e(TAG, "更新用户状态失败", e);
                    }
                });
            }
            
            // 保存登录状态到SharedPreferences
            SharedPrefsUtil.saveUserInfo(
                this,
                user.getUserId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getUserRole()
            );
            
            showToast("登录成功，欢迎回来!");
            
            // 跳转到主页
            navigateToMainActivity();
        } catch (Exception e) {
            Log.e(TAG, "处理登录成功失败", e);
            showToast("登录成功，但保存状态失败");
        }
    }
    
    /**
     * 处理微信登录
     */
    private void handleWeChatLogin() {
        if (agreementCheckBox != null && !agreementCheckBox.isChecked()) {
            showToast("请先同意用户协议");
            return;
        }
        
        // 模拟微信登录流程
        showToast("微信登录功能开发中...");
        
        // TODO: 集成微信SDK
        // 1. 调用微信登录API
        // 2. 获取微信用户信息
        // 3. 创建或查找对应的用户账户
        // 4. 执行登录流程
    }
    
    /**
     * 跳转到注册页面
     */
    private void navigateToRegister() {
        try {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "跳转注册页面失败", e);
            showToast("注册页面暂不可用");
        }
    }
    
    /**
     * 处理跳过登录
     */
    private void handleSkipLogin() {
        // 创建游客用户
        createGuestUser();
    }
    
    /**
     * 创建游客用户 - 修复Lambda变量捕获问题
     */
    private void createGuestUser() {
        if (executor != null && userDao != null) {
            executor.execute(() -> {
                User guestUser = null;
                try {
                    // 创建游客账户
                    guestUser = new User();
                    guestUser.setUsername("游客" + System.currentTimeMillis());
                    guestUser.setNickname("游客用户");
                    guestUser.setEmail("guest@example.com");
                    guestUser.setPassword(hashPassword("guest123"));
                    guestUser.setUserRole("guest");
                    
                    userDao.insert(guestUser);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to create guest user", e);
                }
                
                // 🔧 修复方案：创建final副本用于lambda捕获
                final User finalGuestUser = guestUser;
                
                runOnUiThread(() -> {
                    if (finalGuestUser != null) {
                        try {
                            // 保存游客登录状态
                            SharedPrefsUtil.saveUserInfo(
                                this,
                                finalGuestUser.getUserId(),
                                finalGuestUser.getUsername(),
                                finalGuestUser.getNickname(),
                                finalGuestUser.getEmail(),
                                finalGuestUser.getAvatarUrl(),
                                finalGuestUser.getUserRole()
                            );
                            
                            showToast("以游客身份进入");
                            navigateToMainActivity();
                        } catch (Exception e) {
                            Log.e(TAG, "保存游客状态失败", e);
                            showToast("游客登录失败");
                        }
                    } else {
                        showToast("游客登录失败");
                    }
                });
            });
        } else {
            showToast("数据库未初始化，无法创建游客账户");
        }
    }
    
    /**
     * 输入验证
     */
    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            if (usernameLayout != null) {
                usernameLayout.setError("请输入用户名或邮箱");
            }
            if (usernameEditText != null) {
                usernameEditText.requestFocus();
            }
            return false;
        } else {
            if (usernameLayout != null) {
                usernameLayout.setError(null);
            }
        }
        
        if (TextUtils.isEmpty(password)) {
            if (passwordLayout != null) {
                passwordLayout.setError("请输入密码");
            }
            if (passwordEditText != null) {
                passwordEditText.requestFocus();
            }
            return false;
        } else {
            if (passwordLayout != null) {
                passwordLayout.setError(null);
            }
        }
        
        if (password.length() < 6) {
            if (passwordLayout != null) {
                passwordLayout.setError("密码至少6个字符");
            }
            if (passwordEditText != null) {
                passwordEditText.requestFocus();
            }
            return false;
        } else {
            if (passwordLayout != null) {
                passwordLayout.setError(null);
            }
        }
        
        return true;
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
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "跳转主界面失败", e);
            showToast("跳转失败，请重试");
        }
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