package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.xiangjia.locallife.R;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.database.ForumMessageDao;
import com.xiangjia.locallife.database.UserDao;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.ForumMessage;
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.util.ForumDataGenerator;
import com.xiangjia.locallife.util.SharedPrefsUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据初始化页面
 * 用于初始化论坛测试数据，满足课程要求的2500+条数据记录
 */
public class DataInitActivity extends AppCompatActivity {
    
    private static final String TAG = "DataInitActivity";
    
    // UI组件
    private TextView tvStatus;
    private ProgressBar progressBar;
    private MaterialButton btnInitData;
    private MaterialButton btnEnterForum;
    private TextView tvDataSummary;
    
    // 数据相关
    private ForumPostDao forumPostDao;
    private ForumMessageDao forumMessageDao;
    private UserDao userDao;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_init);
        
        initViews();
        initData();
        setupListeners();
        checkExistingData();
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        tvStatus = findViewById(R.id.tv_status);
        progressBar = findViewById(R.id.progress_bar);
        btnInitData = findViewById(R.id.btn_init_data);
        btnEnterForum = findViewById(R.id.btn_enter_forum);
        tvDataSummary = findViewById(R.id.tv_data_summary);
        
        // 初始状态
        progressBar.setVisibility(ProgressBar.GONE);
        btnEnterForum.setEnabled(false);
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        AppDatabase database = AppDatabase.getDatabase(this);
        forumPostDao = database.forumPostDao();
        forumMessageDao = database.forumMessageDao();
        userDao = database.userDao();
        executor = Executors.newFixedThreadPool(4);
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        btnInitData.setOnClickListener(v -> initializeForumData());
        btnEnterForum.setOnClickListener(v -> enterForum());
    }
    
    /**
     * 检查现有数据
     */
    private void checkExistingData() {
        executor.execute(() -> {
            try {
                int userCount = userDao.getTotalUserCount();
                int postCount = forumPostDao.getTotalPostCount();
                int messageCount = forumMessageDao.getMessageCountByAuthor(""); // 获取总消息数的简化方法
                
                runOnUiThread(() -> {
                    if (userCount > 0 || postCount > 0) {
                        tvStatus.setText("检测到现有数据");
                        tvDataSummary.setText(String.format(
                            "当前数据:\n用户: %d\n帖子: %d\n消息: %d\n总计: %d", 
                            userCount, postCount, messageCount, userCount + postCount + messageCount
                        ));
                        btnEnterForum.setEnabled(true);
                        btnInitData.setText("重新初始化数据");
                    } else {
                        tvStatus.setText("暂无数据，请初始化");
                        tvDataSummary.setText("点击下方按钮开始初始化论坛数据");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tvStatus.setText("数据检查失败");
                    Toast.makeText(this, "数据库访问失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 初始化论坛数据
     */
    private void initializeForumData() {
        btnInitData.setEnabled(false);
        btnEnterForum.setEnabled(false);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        tvStatus.setText("正在生成数据...");
        
        executor.execute(() -> {
            try {
                // 生成完整的论坛数据
                runOnUiThread(() -> tvStatus.setText("正在生成用户数据..."));
                ForumDataGenerator.ForumDataSet dataSet = ForumDataGenerator.generateCompleteForumData();
                
                List<User> users = dataSet.getUsers();
                List<ForumPost> posts = dataSet.getPosts();
                List<ForumMessage> messages = dataSet.getMessages();
                
                // 插入用户数据
                runOnUiThread(() -> {
                    tvStatus.setText("正在保存用户数据...");
                    progressBar.setProgress(25);
                });
                
                for (User user : users) {
                    userDao.insert(user);
                }
                
                // 插入帖子数据
                runOnUiThread(() -> {
                    tvStatus.setText("正在保存帖子数据...");
                    progressBar.setProgress(50);
                });
                
                for (ForumPost post : posts) {
                    forumPostDao.insert(post);
                }
                
                // 插入消息数据
                runOnUiThread(() -> {
                    tvStatus.setText("正在保存消息数据...");
                    progressBar.setProgress(75);
                });
                
                // 批量插入消息以提高性能
                forumMessageDao.insertMessages(messages);
                
                // 设置默认登录用户
                User defaultUser = users.get(0); // 使用第一个测试用户
                SharedPrefsUtil.saveUserInfo(
                    this,
                    defaultUser.getUserId(),
                    defaultUser.getUsername(),
                    defaultUser.getNickname(),
                    defaultUser.getEmail(),
                    defaultUser.getAvatarUrl(),
                    defaultUser.getUserRole()
                );
                
                runOnUiThread(() -> {
                    progressBar.setProgress(100);
                    tvStatus.setText("数据初始化完成！");
                    
                    String summary = String.format(
                        "数据初始化成功!\n\n%s\n\n已为您自动登录测试账户：%s",
                        dataSet.getDataSummary(),
                        defaultUser.getNickname()
                    );
                    tvDataSummary.setText(summary);
                    
                    btnInitData.setEnabled(true);
                    btnInitData.setText("重新初始化数据");
                    btnEnterForum.setEnabled(true);
                    progressBar.setVisibility(ProgressBar.GONE);
                    
                    Toast.makeText(this, "数据初始化成功！", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tvStatus.setText("数据初始化失败");
                    btnInitData.setEnabled(true);
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "数据初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * 进入论坛
     */
    private void enterForum() {
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
        finish(); // 结束初始化页面
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}