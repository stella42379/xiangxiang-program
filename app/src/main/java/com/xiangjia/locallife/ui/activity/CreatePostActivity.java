package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.database.UserDao;
import com.xiangjia.locallife.util.SharedPrefsUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建帖子页面
 * 支持创建讨论帖、交友帖等不同类型的帖子
 */
public class CreatePostActivity extends AppCompatActivity {
    
    private static final String TAG = "CreatePostActivity";
    private static final int REQUEST_IMAGE_PICK = 1001;
    
    // UI组件
    private Toolbar toolbar;
    private Spinner spinnerCategory;
    private TextInputLayout tilTitle;
    private TextInputEditText etTitle;
    private TextInputLayout tilContent;
    private TextInputEditText etContent;
    private MaterialCardView cardImagePreview;
    private ImageView ivImagePreview;
    private MaterialButton btnRemoveImage;
    private MaterialButton btnAddImage;
    private MaterialButton btnPublish;
    
    // 数据相关
    private ForumPostDao forumPostDao;
    private UserDao userDao;
    private ExecutorService executor;
    
    private String currentUserId;
    private User currentUser;
    private String selectedImagePath;
    
    // 分类数据
    private String[] categoryNames = {"选择分类", "讨论", "交友", "求助", "分享"};
    private String[] categoryValues = {"", "discussion", "friends", "help", "share"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        
        initViews();
        initData();
        setupCategorySpinner();
        setupListeners();
        loadCurrentUser();
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        spinnerCategory = findViewById(R.id.spinner_category);
        tilTitle = findViewById(R.id.til_title);
        etTitle = findViewById(R.id.et_title);
        tilContent = findViewById(R.id.til_content);
        etContent = findViewById(R.id.et_content);
        cardImagePreview = findViewById(R.id.card_image_preview);
        ivImagePreview = findViewById(R.id.iv_image_preview);
        btnRemoveImage = findViewById(R.id.btn_remove_image);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnPublish = findViewById(R.id.btn_publish);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("发表帖子");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        AppDatabase database = AppDatabase.getDatabase(this);
        forumPostDao = database.forumPostDao();
        userDao = database.userDao();
        executor = Executors.newFixedThreadPool(2);
        
        currentUserId = SharedPrefsUtil.getUserId(this);
        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    
    /**
     * 设置分类选择器
     */
    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 添加图片按钮
        btnAddImage.setOnClickListener(v -> selectImage());
        
        // 移除图片按钮
        btnRemoveImage.setOnClickListener(v -> removeImage());
        
        // 发布按钮
        btnPublish.setOnClickListener(v -> publishPost());
    }
    
    /**
     * 加载当前用户信息
     */
    private void loadCurrentUser() {
        executor.execute(() -> {
            try {
                currentUser = userDao.getUserById(currentUserId);
                if (currentUser == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "用户信息获取失败", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "用户信息加载失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 选择图片
     */
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_IMAGE_PICK);
    }
    
    /**
     * 移除图片
     */
    private void removeImage() {
        selectedImagePath = null;
        cardImagePreview.setVisibility(View.GONE);
        btnAddImage.setText("添加图片");
    }

    
    
    /**
     * 发布帖子
     */
   /**
 

/**
 * 发布帖子方法
 */
private void publishPost() {
    String title = etTitle.getText().toString().trim();
    String content = etContent.getText().toString().trim();
    int categoryIndex = spinnerCategory.getSelectedItemPosition();
    
    if (title.isEmpty()) {
        tilTitle.setError("请输入帖子标题");
        return;
    }
    
    if (content.isEmpty()) {
        tilContent.setError("请输入帖子内容");
        return;
    }
    
    if (categoryIndex == 0) {
        Toast.makeText(this, "请选择帖子分类", Toast.LENGTH_SHORT).show();
        return;
    }
    
    String category = categoryValues[categoryIndex];
    
    executor.execute(() -> {
        try {
            // 创建新帖子
            ForumPost newPost = new ForumPost(
                currentUser.getUserId(),
                currentUser.getNickname(),
                title,
                content,
                category
            );
            
            if (selectedImagePath != null) {
                newPost.setImageUrl(selectedImagePath);
            }
            
            // 保存到数据库
            forumPostDao.insert(newPost);
            
            // 更新用户发帖数
            currentUser.incrementPostCount();
            userDao.updatePostCount(currentUser.getUserId(), currentUser.getPostCount());
            
            runOnUiThread(() -> {
                Toast.makeText(this, "发帖成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(this, "发帖失败", Toast.LENGTH_SHORT).show();
            });
        }
    });
}
    
    /**
     * 验证输入内容
     */
    private boolean validateInput() {
        // 检查标题
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            tilTitle.setError("请输入帖子标题");
            etTitle.requestFocus();
            return false;
        }
        if (title.length() < 5) {
            tilTitle.setError("标题至少需要5个字符");
            etTitle.requestFocus();
            return false;
        }
        if (title.length() > 100) {
            tilTitle.setError("标题不能超过100个字符");
            etTitle.requestFocus();
            return false;
        }
        tilTitle.setError(null);
        
        // 检查内容
        String content = etContent.getText().toString().trim();
        if (content.isEmpty()) {
            tilContent.setError("请输入帖子内容");
            etContent.requestFocus();
            return false;
        }
        if (content.length() < 10) {
            tilContent.setError("内容至少需要10个字符");
            etContent.requestFocus();
            return false;
        }
        if (content.length() > 5000) {
            tilContent.setError("内容不能超过5000个字符");
            etContent.requestFocus();
            return false;
        }
        tilContent.setError(null);
        
        // 检查分类
        if (spinnerCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "请选择帖子分类", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // 处理选择的图片
            selectedImagePath = data.getData().toString();
            
            // 显示图片预览
            cardImagePreview.setVisibility(View.VISIBLE);
            // TODO: 使用图片加载库显示预览
            // Glide.with(this).load(selectedImagePath).into(ivImagePreview);
            
            btnAddImage.setText("更换图片");
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 检查是否有未保存的内容
                if (hasUnsavedContent()) {
                    // TODO: 显示确认对话框
                }
                finish();
                return true;
            case R.id.action_draft:
                saveDraft();
                return true;
            case R.id.action_preview:
                previewPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * 检查是否有未保存的内容
     */
    private boolean hasUnsavedContent() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        return !title.isEmpty() || !content.isEmpty() || selectedImagePath != null;
    }
    
    /**
     * 保存草稿
     */
    private void saveDraft() {
        // TODO: 实现草稿保存功能
        Toast.makeText(this, "草稿保存功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 预览帖子
     */
    private void previewPost() {
        if (!validateInput()) {
            return;
        }
        
        // TODO: 实现帖子预览功能
        Toast.makeText(this, "预览功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}