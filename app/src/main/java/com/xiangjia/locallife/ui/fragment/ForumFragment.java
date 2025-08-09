package com.xiangjia.locallife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.activity.CreatePostActivity;
import com.xiangjia.locallife.ui.activity.PostDetailActivity;
import com.xiangjia.locallife.ui.activity.DataInitActivity;
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
 * 论坛Fragment
 * 在底部导航的第三个页面显示论坛功能
 */
public class ForumFragment extends Fragment {
    
    private static final String TAG = "ForumFragment";
    private static final int REQUEST_CREATE_POST = 100;
    
    // UI组件
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        // 初始化数据
        initData();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupCategoryChips();
        setupListeners();
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 检查数据并加载帖子
        checkDataAndLoad();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        if (getContext() != null) {
            AppDatabase database = AppDatabase.getInstance(getContext());
            forumPostDao = database.forumPostDao();
            executor = Executors.newFixedThreadPool(4);
            
            postList = new ArrayList<>();
            postAdapter = new ForumPostAdapter(getContext(), postList);
        }
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews(View view) {
        categoryChipGroup = view.findViewById(R.id.category_chip_group);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        fabCreatePost = view.findViewById(R.id.fab_create_post);
    }
    
    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postAdapter);
        
        // 设置帖子点击监听
        postAdapter.setOnPostClickListener(new ForumPostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(ForumPost post) {
                // 增加浏览数
                incrementViewCount(post);
                
                // 跳转到帖子详情页
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
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
                Toast.makeText(getContext(), "查看用户：" + post.getAuthorName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * 设置分类筛选芯片
     */
    private void setupCategoryChips() {
        String[] categories = {"全部", "讨论", "交友", "求助", "分享", "公告"};
        String[] categoryValues = {"all", "discussion", "friends", "help", "share", "announcement"};
        
        for (int i = 0; i < categories.length; i++) {
            Chip chip = new Chip(getContext());
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
            // 检查用户是否已登录
            if (!SharedPrefsUtil.isLoggedIn(getContext())) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CREATE_POST);
        });
    }
    
    /**
     * 检查数据并加载
     */
    private void checkDataAndLoad() {
        if (executor == null || getContext() == null) return;
        
        executor.execute(() -> {
            try {
                int userCount = forumPostDao.getTotalPostCount();
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (userCount == 0) {
                            // 没有数据，显示初始化提示
                            showInitDataPrompt();
                        } else {
                            // 有数据，直接加载
                            loadPosts();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "数据检查失败", Toast.LENGTH_SHORT).show();
                        loadPosts(); // 尝试加载
                    });
                }
            }
        });
    }
    
    /**
     * 显示数据初始化提示
     */
    private void showInitDataPrompt() {
        if (getContext() == null) return;
        
        // 简化版：直接跳转到数据初始化页面
        Intent intent = new Intent(getContext(), DataInitActivity.class);
        startActivity(intent);
    }
    
    /**
     * 加载帖子数据
     */
    private void loadPosts() {
        if (executor == null || swipeRefreshLayout == null) return;
        
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
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    postList.clear();
                    postList.addAll(finalPosts);
                    postAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    
                    if (finalPosts.isEmpty()) {
                        Toast.makeText(getContext(), "暂无帖子数据", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    
    /**
     * 增加帖子浏览数
     */
    private void incrementViewCount(ForumPost post) {
        if (executor == null) return;
        
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
        if (executor == null) return;
        
        executor.execute(() -> {
            try {
                // 这里简化处理，实际应该检查用户是否已点赞
                int newLikeCount = post.getLikeCount() + 1;
                long currentTime = System.currentTimeMillis();
                
                forumPostDao.updateLikeCount(post.getPostId(), newLikeCount, currentTime);
                post.setLikeCount(newLikeCount);
                post.setLastActivityTime(currentTime);
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        postAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                    });
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "点赞失败", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forum, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        
        if (searchView != null) {
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
        }
        
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadPosts();
                return true;
            case R.id.action_my_posts:
                Toast.makeText(getContext(), "我的帖子功能开发中", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_friends:
                Toast.makeText(getContext(), "好友列表功能开发中", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getContext(), "设置功能开发中", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_POST && resultCode == getActivity().RESULT_OK) {
            // 创建帖子成功，刷新列表
            loadPosts();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 每次回到页面时刷新数据
        if (postAdapter != null) {
            loadPosts();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}