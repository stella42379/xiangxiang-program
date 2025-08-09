package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.ForumMessageAdapter;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.ForumMessage;
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.database.ForumMessageDao;
import com.xiangjia.locallife.database.UserDao;
import com.xiangjia.locallife.util.DateUtils;
import com.xiangjia.locallife.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 帖子详情页面
 * 显示帖子内容和回复列表，支持发表回复
 */
public class PostDetailActivity extends AppCompatActivity {
    
    private static final String TAG = "PostDetailActivity";
    
    // UI组件
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    
    // 帖子内容区域
    private MaterialCardView cardPostContent;
    private ImageView ivAuthorAvatar;
    private TextView tvAuthorName;
    private TextView tvPostTime;
    private TextView tvCategory;
    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivPostImage;
    private TextView tvLikeCount;
    private TextView tvReplyCount;
    private TextView tvViewCount;
    private MaterialButton btnLike;
    private MaterialButton btnReply;
    
    // 回复列表
    private RecyclerView recyclerViewMessages;
    private ForumMessageAdapter messageAdapter;
    
    // 回复输入区域
    private MaterialCardView cardReplyInput;
    private EditText etReplyContent;
    private MaterialButton btnSendReply;
    
    // 数据相关
    private ForumPost currentPost;
    private List<ForumMessage> messageList;
    private ForumPostDao forumPostDao;
    private ForumMessageDao forumMessageDao;
    private UserDao userDao;
    private ExecutorService executor;
    
    private String postId;
    private String currentUserId;
    private User currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        
        // 获取传入的帖子ID
        postId = getIntent().getStringExtra("postId");
        if (postId == null) {
            Toast.makeText(this, "帖子ID不能为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        initData();
        setupRecyclerView();
        setupListeners();
        loadPostDetail();
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        
        // 帖子内容
        cardPostContent = findViewById(R.id.card_post_content);
        ivAuthorAvatar = findViewById(R.id.iv_author_avatar);
        tvAuthorName = findViewById(R.id.tv_author_name);
        tvPostTime = findViewById(R.id.tv_post_time);
        tvCategory = findViewById(R.id.tv_category);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        ivPostImage = findViewById(R.id.iv_post_image);
        tvLikeCount = findViewById(R.id.tv_like_count);
        tvReplyCount = findViewById(R.id.tv_reply_count);
        tvViewCount = findViewById(R.id.tv_view_count);
        btnLike = findViewById(R.id.btn_like);
        btnReply = findViewById(R.id.btn_reply);
        
        // 回复列表
        recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        
        // 回复输入
        cardReplyInput = findViewById(R.id.card_reply_input);
        etReplyContent = findViewById(R.id.et_reply_content);
        btnSendReply = findViewById(R.id.btn_send_reply);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("帖子详情");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        
        messageList = new ArrayList<>();
        messageAdapter = new ForumMessageAdapter(this, messageList);
        
        // 获取当前用户信息
        currentUserId = SharedPrefsUtil.getUserId(this);
        loadCurrentUser();
    }
    
    /**
     * 加载当前用户信息
     */
    private void loadCurrentUser() {
        executor.execute(() -> {
            try {
                currentUser = userDao.getUserById(currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
        
        // 设置消息点击监听
        messageAdapter.setOnMessageClickListener(new ForumMessageAdapter.OnMessageClickListener() {
            @Override
            public void onMessageClick(ForumMessage message) {
                // 点击消息，可以实现回复功能
                showReplyToMessage(message);
            }
            
            @Override
            public void onLikeClick(ForumMessage message) {
                toggleMessageLike(message);
            }
            
            @Override
            public void onUserClick(ForumMessage message) {
                // 跳转到用户资料页
                Intent intent = new Intent(PostDetailActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", message.getAuthorId());
                startActivity(intent);
            }
        });
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadPostDetail();
            loadMessages();
        });
        
        // 点赞按钮
        btnLike.setOnClickListener(v -> togglePostLike());
        
        // 回复按钮
        btnReply.setOnClickListener(v -> showReplyInput());
        
        // 发送回复按钮
        btnSendReply.setOnClickListener(v -> sendReply());
        
        // 作者头像点击
        ivAuthorAvatar.setOnClickListener(v -> {
            if (currentPost != null) {
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("userId", currentPost.getAuthorId());
                startActivity(intent);
            }
        });
    }
    
    /**
     * 加载帖子详情
     */
    private void loadPostDetail() {
        executor.execute(() -> {
            try {
                currentPost = forumPostDao.getPostById(postId);
                
                if (currentPost != null) {
                    runOnUiThread(() -> {
                        displayPostContent();
                        loadMessages();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "帖子不存在或已被删除", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载帖子失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 显示帖子内容
     */
    private void displayPostContent() {
        if (currentPost == null) return;
        
        // 设置作者信息
        tvAuthorName.setText(currentPost.getAuthorName());
        tvPostTime.setText(DateUtils.formatRelativeTime(currentPost.getTimestamp()));
        
        // 设置分类
        tvCategory.setText(getCategoryDisplayName(currentPost.getCategory()));
        
        // 设置帖子内容
        tvTitle.setText(currentPost.getTitle());
        tvContent.setText(currentPost.getContent());
        
        // 设置图片
        if (currentPost.getImageUrl() != null && !currentPost.getImageUrl().isEmpty()) {
            ivPostImage.setVisibility(View.VISIBLE);
            // TODO: 使用图片加载库加载图片
        } else {
            ivPostImage.setVisibility(View.GONE);
        }
        
        // 设置统计数据
        tvLikeCount.setText(String.valueOf(currentPost.getLikeCount()));
        tvReplyCount.setText(String.valueOf(currentPost.getReplyCount()));
        tvViewCount.setText(String.valueOf(currentPost.getViewCount()));
        
        // TODO: 设置点赞状态
    }
    
    /**
     * 加载回复消息
     */
    private void loadMessages() {
        executor.execute(() -> {
            try {
                List<ForumMessage> messages = forumMessageDao.getMessagesByPost(postId);
                
                runOnUiThread(() -> {
                    messageList.clear();
                    messageList.addAll(messages);
                    messageAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(this, "加载回复失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 切换帖子点赞状态
     */
    private void togglePostLike() {
        if (currentPost == null || currentUser == null) return;
        
        executor.execute(() -> {
            try {
                // 简化处理，实际应该检查用户是否已点赞
                int newLikeCount = currentPost.getLikeCount() + 1;
                long currentTime = System.currentTimeMillis();
                
                forumPostDao.updateLikeCount(postId, newLikeCount, currentTime);
                currentPost.setLikeCount(newLikeCount);
                currentPost.setLastActivityTime(currentTime);
                
                runOnUiThread(() -> {
                    tvLikeCount.setText(String.valueOf(newLikeCount));
                    Toast.makeText(this, "点赞成功", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "点赞失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 显示回复输入框
     */
    private void showReplyInput() {
        cardReplyInput.setVisibility(View.VISIBLE);
        etReplyContent.requestFocus();
        // TODO: 弹出软键盘
    }
    
    /**
     * 发送回复
     */
    private void sendReply() {
        String content = etReplyContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (currentUser == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        
        executor.execute(() -> {
            try {
                // 创建新回复
                ForumMessage newMessage = new ForumMessage(
                    postId,
                    currentUser.getUserId(),
                    currentUser.getNickname(),
                    content
                );
                
                // 检查是否为楼主回复
                if (currentUser.getUserId().equals(currentPost.getAuthorId())) {
                    newMessage.setAuthorReply(true);
                }
                
                // 保存回复
                forumMessageDao.insert(newMessage);
                
                // 更新帖子回复数和最后活动时间
                int newReplyCount = currentPost.getReplyCount() + 1;
                long currentTime = System.currentTimeMillis();
                forumPostDao.updateReplyCount(postId, newReplyCount, currentTime);
                
                // 更新用户回复数
                currentUser.incrementMessageCount();
                userDao.updateMessageCount(currentUser.getUserId(), currentUser.getMessageCount());
                
                runOnUiThread(() -> {
                    // 清空输入框
                    etReplyContent.setText("");
                    cardReplyInput.setVisibility(View.GONE);
                    
                    // 添加新回复到列表
                    messageList.add(newMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    
                    // 滚动到最新回复
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    
                    // 更新回复数显示
                    tvReplyCount.setText(String.valueOf(newReplyCount));
                    
                    Toast.makeText(this, "回复成功", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "回复失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 回复特定消息
     */
    private void showReplyToMessage(ForumMessage message) {
        showReplyInput();
        etReplyContent.setText("@" + message.getAuthorName() + " ");
        etReplyContent.setSelection(etReplyContent.getText().length());
    }
    
    /**
     * 切换消息点赞状态
     */
    private void toggleMessageLike(ForumMessage message) {
        executor.execute(() -> {
            try {
                int newLikeCount = message.getLikeCount() + 1;
                forumMessageDao.updateLikeCount(message.getMessageId(), newLikeCount);
                message.setLikeCount(newLikeCount);
                
                runOnUiThread(() -> {
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "点赞成功", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "点赞失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 获取分类显示名称
     */
    private String getCategoryDisplayName(String category) {
        switch (category) {
            case "discussion":
                return "讨论";
            case "friends":
                return "交友";
            case "help":
                return "求助";
            case "share":
                return "分享";
            case "announcement":
                return "公告";
            default:
                return "其他";
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                sharePost();
                return true;
            case R.id.action_report:
                reportPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * 分享帖子
     */
    private void sharePost() {
        if (currentPost != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, 
                currentPost.getTitle() + "\n\n" + currentPost.getContent());
            startActivity(Intent.createChooser(shareIntent, "分享帖子"));
        }
    }
    
    /**
     * 举报帖子
     */
    private void reportPost() {
        // TODO: 实现举报功能
        Toast.makeText(this, "举报功能开发中", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}