package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.ForumPostAdapter;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 论坛主界面Activity
 * 实现社区讨论和交友功能
 */
public class ForumActivity extends AppCompatActivity {
    
    private static final String TAG = "ForumActivity";
    
    // UI组件
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ChipGroup categoryChipGroup;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fabCreatePost;
    private SearchView searchView;
    
    // 数据相关
    private ForumPostAdapter postAdapter;
    private List<ForumPost> postList;
    private ForumPostDao forumPostDao;
    private ExecutorService executor;
    
    // 当前状态
    private String currentCategory = "all";
    private String currentSort = "activity"; // activity, latest, popular
    private String currentSearchQuery = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        
        initViews();
        initData();
        setupRecyclerView();
        setupTabs();
        setupListeners();
        loadPosts();
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        categoryChipGroup = findViewById(R.id.category_chip_group);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycler_view_posts);
        fabCreatePost = findViewById(R.id.fab_create_post);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("社区论坛");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        AppDatabase database = AppDatabase.getDatabase(this);
        forumPostDao = database.forumPostDao();
        executor = Executors.newFixedThreadPool(4);
        
        postList = new ArrayList<>();
        postAdapter = new ForumPostAdapter(this, postList);
    }
    
    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postAdapter);
        
        // 设置帖子点击监听
        postAdapter.setOnPostClickListener(new ForumPostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(ForumPost post) {
                // 增加浏览数
                incrementViewCount(post);
                
                // 跳转到帖子详情页
                Intent intent = new Intent(ForumActivity.this, PostDetailActivity.class);
                intent.putExtra("postId", post.getPostId());
                startActivity(intent);
            }
            
            @Override
            public void onLikeClick(ForumPost post) {
                toggleLike(post);
            }
            
            @Override
            public void onUserClick(ForumPost post) {
                // 跳转到用户资料页
                Intent intent = new Intent(ForumActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", post.getAuthorId());
                startActivity(intent);
            }
        });
    }
    
    /**
     * 设置标签页
     */
    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("最新活动"));
        tabLayout.addTab(tabLayout.newTab().setText("最新发布"));
        tabLayout.addTab(tabLayout.newTab().setText("热门帖子"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentSort = "activity";
                        break;
                    case 1:
                        currentSort = "latest";
                        break;
                    case 2:
                        currentSort = "popular";
                        break;
                }
                loadPosts();
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        // 设置分类筛选
        setupCategoryChips();
    }
    
    /**
     * 设置分类筛选芯片
     */
    private void setupCategoryChips() {
        String[] categories = {"全部", "讨论", "交友", "求助", "分享", "公告"};
        String[] categoryValues = {"all", "discussion", "friends", "help", "share", "announcement"};
        
        for (int i = 0; i < categories.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(categories[i]);
            chip.setCheckable(true);
            chip.setTag(categoryValues[i]);
            
            if (i == 0) {
                chip.setChecked(true);
            }
            
            chip.setOnCheckedChangeListener((view, isChecked) -> {
                if (isChecked) {
                    // 取消其他chip的选中状态
                    for (int j = 0; j < categoryChipGroup.getChildCount(); j++) {
                        Chip otherChip = (Chip) categoryChipGroup.getChildAt(j);
                        if (otherChip != view) {
                            otherChip.setChecked(false);
                        }
                    }
                    currentCategory = (String) view.getTag();
                    loadPosts();
                }
            });
            
            categoryChipGroup.addView(chip);
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadPosts();
        });
        
        // 创建帖子按钮
        fabCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(ForumActivity.this, CreatePostActivity.class);
            startActivityForResult(intent, 100);
        });
    }
    
    /**
     * 加载帖子数据
     */
    private void loadPosts() {
        swipeRefreshLayout.setRefreshing(true);
        
        executor.execute(() -> {
            List<ForumPost> posts = new ArrayList<>();
            
            try {
                if (!currentSearchQuery.isEmpty()) {
                    // 搜索模式
                    posts = forumPostDao.searchPosts(currentSearchQuery);
                } else if ("all".equals(currentCategory)) {
                    // 所有分类
                    switch (currentSort) {
                        case "latest":
                            posts = forumPostDao.getLatestPosts(50);
                            break;
                        case "popular":
                            posts = forumPostDao.getPopularPosts(50);
                            break;
                        default:
                            posts = forumPostDao.getAllPostsSortedByActivity();
                            break;
                    }
                } else {
                    // 特定分类
                    posts = forumPostDao.getPostsByCategory(currentCategory);
                }
                
                // 添加置顶帖子到顶部
                if (currentSort.equals("activity") && currentSearchQuery.isEmpty()) {
                    List<ForumPost> pinnedPosts = forumPostDao.getPinnedPosts();
                    if (!pinnedPosts.isEmpty()) {
                        posts.addAll(0, pinnedPosts);
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            final List<ForumPost> finalPosts = posts;
            
            runOnUiThread(() -> {
                postList.clear();
                postList.addAll(finalPosts);
                postAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                
                if (finalPosts.isEmpty()) {
                    Toast.makeText(ForumActivity.this, "暂无帖子数据", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    /**
     * 增加帖子浏览数
     */
    private void incrementViewCount(ForumPost post) {
        executor.execute(() -> {
            try {
                int newViewCount = post.getViewCount() + 1;
                forumPostDao.updateViewCount(post.getPostId(), newViewCount);
                post.setViewCount(newViewCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * 切换点赞状态
     */
    private void toggleLike(ForumPost post) {
        // TODO: 实现用户点赞逻辑，需要用户点赞记录表
        executor.execute(() -> {
            try {
                // 这里简化处理，实际应该检查用户是否已点赞
                int newLikeCount = post.getLikeCount() + 1;
                long currentTime = System.currentTimeMillis();
                
                forumPostDao.updateLikeCount(post.getPostId(), newLikeCount, currentTime);
                post.setLikeCount(newLikeCount);
                post.setLastActivityTime(currentTime);
                
                runOnUiThread(() -> {
                    postAdapter.notifyDataSetChanged();
                    Toast.makeText(ForumActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(ForumActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_forum, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                loadPosts();
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    currentSearchQuery = "";
                    loadPosts();
                }
                return true;
            }
        });
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                loadPosts();
                return true;
            case R.id.action_my_posts:
                // 查看我的帖子
                Intent intent = new Intent(this, MyPostsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_friends:
                // 好友列表
                Intent friendsIntent = new Intent(this, FriendsActivity.class);
                startActivity(friendsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // 创建帖子成功，刷新列表
            loadPosts();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}