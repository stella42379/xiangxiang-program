package com.xiangjia.locallife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.activity.CreatePostActivity;
import com.xiangjia.locallife.ui.activity.PostDetailActivity;
import com.xiangjia.locallife.ui.activity.DataInitActivity;
import com.xiangjia.locallife.ui.activity.SearchActivity;
import com.xiangjia.locallife.ui.adapter.ForumPostAdapter;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.util.SharedPrefsUtil;

// 高级功能导入
import com.xiangjia.locallife.service.ForumDataStream;
import com.xiangjia.locallife.service.ForumSearchManager;
import com.xiangjia.locallife.factory.ForumPostFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 🎯 毛玻璃风格论坛Fragment - 仿新闻页面设计
 */
public class ForumFragment extends Fragment {
    
    private static final String TAG = "ForumFragment";
    private static final int REQUEST_CREATE_POST = 100;
    
    // 🎯 UI组件 - 毛玻璃风格
    private ImageView backgroundImage;
    private ChipGroup categoryChipGroup;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private FloatingActionButton fabCreatePost;
    private TextView statusText;
    private Spinner sortSpinner;
    private ImageView btnSearch;
    private ImageView btnRefresh;
    
    // 🎯 空状态显示
    private CardView cardEmptyState;
    private ImageView ivEmptyIcon;
    private TextView tvEmptyTitle;
    private TextView tvEmptyMessage;
    
    // 数据相关
    private ForumPostAdapter postAdapter;
    private List<ForumPost> postList;
    private ForumPostDao forumPostDao;
    private ExecutorService executor;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    // 高级功能
    private ForumDataStream dataStream;
    private ForumSearchManager searchManager;
    
    // 当前状态
    private String currentCategory = "all";
    private String currentSort = "activity"; // activity, latest, popular
    private boolean isLoading = false;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "🎯 ForumFragment onCreate - 毛玻璃风格");
        setHasOptionsMenu(true);
        
        // 初始化数据
        initData();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "🎯 ForumFragment onCreateView");
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "🎯 ForumFragment onViewCreated");
        
        initViews(view);
        setupRecyclerView();
        setupCategoryChips();
        setupSortSpinner();
        setupSwipeRefresh();
        setupListeners();
        
        // 检查数据并加载帖子
        checkDataAndLoad();
    }
    
    /**
     * 🎯 初始化毛玻璃风格视图组件
     */
    private void initViews(View view) {
        Log.d(TAG, "🎯 开始初始化毛玻璃风格视图组件");
        
        try {
            // 基础组件
            backgroundImage = view.findViewById(R.id.iv_background);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            nestedScrollView = view.findViewById(R.id.nested_scroll);
            recyclerView = view.findViewById(R.id.recycler_view_posts);
            fabCreatePost = view.findViewById(R.id.fab_create_post);
            statusText = view.findViewById(R.id.status_text);
            
            // 功能按钮
            btnSearch = view.findViewById(R.id.btn_search);
            btnRefresh = view.findViewById(R.id.btn_refresh);
            sortSpinner = view.findViewById(R.id.spinner_sort);
            
            // 分类筛选
            categoryChipGroup = view.findViewById(R.id.category_chip_group);
            
            // 空状态组件
            cardEmptyState = view.findViewById(R.id.card_empty_state);
            ivEmptyIcon = view.findViewById(R.id.iv_empty_icon);
            tvEmptyTitle = view.findViewById(R.id.tv_empty_title);
            tvEmptyMessage = view.findViewById(R.id.tv_empty_message);
            
            Log.d(TAG, "🎯 毛玻璃风格视图组件初始化完成");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 视图组件初始化失败", e);
        }
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
            
            // 初始化高级功能
            initAdvancedFeatures();
        }
    }
    
    /**
     * 初始化高级功能
     */
    private void initAdvancedFeatures() {
        try {
            // 检查高级功能类是否存在，如果不存在就跳过
            if (isClassAvailable("com.xiangjia.locallife.service.ForumDataStream") &&
                isClassAvailable("com.xiangjia.locallife.service.ForumSearchManager")) {
                
                // 1. 初始化数据流
                dataStream = ForumDataStream.getInstance(AppDatabase.getInstance(getContext()));
                dataStream.addObserver(this::onDataStreamUpdated);
                dataStream.startDataStream();
                
                // 2. 初始化搜索管理器
                searchManager = ForumSearchManager.getInstance();
                searchManager.setAdvancedSearch(true);
                
                Log.d(TAG, "🎯 高级功能初始化完成");
            } else {
                Log.d(TAG, "🎯 高级功能类不存在，跳过初始化");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 高级功能初始化失败，继续使用基础功能", e);
        }
    }
    
    /**
     * 检查类是否存在
     */
    private boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    /**
     * 🎯 设置RecyclerView - 适配毛玻璃风格
     */
    private void setupRecyclerView() {
        try {
            if (recyclerView != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                
                // 🎯 关键：禁用嵌套滚动，避免冲突
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setHasFixedSize(false);
                
                // 设置适配器
                recyclerView.setAdapter(postAdapter);
                
                // 设置帖子点击监听
                postAdapter.setOnPostClickListener(new ForumPostAdapter.OnPostClickListener() {
                    @Override
                    public void onPostClick(ForumPost post) {
                        incrementViewCount(post);
                        
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
                        Toast.makeText(getContext(), "查看用户：" + post.getAuthorName(), Toast.LENGTH_SHORT).show();
                    }
                });
                
                Log.d(TAG, "🎯 RecyclerView设置完成");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置RecyclerView失败", e);
        }
    }
    
    /**
     * 🎯 设置分类筛选芯片 - 毛玻璃风格
     */
    private void setupCategoryChips() {
        try {
            if (categoryChipGroup == null) return;
            
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
                        // 更新其他chip样式
                        updateChipStyles((Chip) view);  // 强制转换为Chip
                        currentCategory = (String) view.getTag();
                        loadPosts();
                    }
                });
                
                categoryChipGroup.addView(chip);
            }
            
            Log.d(TAG, "🎯 分类芯片设置完成");
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置分类芯片失败", e);
        }
    }
    
    /**
     * 🎯 更新芯片样式
     */
    private void updateChipStyles(Chip selectedChip) {
        if (categoryChipGroup == null) return;
        
        for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) categoryChipGroup.getChildAt(i);
            if (chip != selectedChip) {
                chip.setChecked(false);
            }
        }
    }
    
    /**
     * 🎯 设置排序下拉框
     */
    private void setupSortSpinner() {
        try {
            if (sortSpinner == null) return;
            
            String[] sortOptions = {"最新活动", "最新发布", "最多点赞"};
            String[] sortValues = {"activity", "latest", "popular"};
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, sortOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortSpinner.setAdapter(adapter);
            
            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentSort = sortValues[position];
                    loadPosts();
                }
                
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置排序下拉框失败", e);
        }
    }
    
    /**
     * 🎯 设置下拉刷新 - 适配NestedScrollView
     */
    private void setupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(
                0xFF2d8cf0,  // 蓝色
                0xFF87CEEB,  // 天蓝色
                0xFFFFB6C1   // 粉色
            );
            
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Log.d(TAG, "🔄 用户下拉刷新");
                if (dataStream != null) {
                    dataStream.forceUpdate();
                }
                loadPosts();
            });
            
            // 🎯 关键：让SwipeRefreshLayout正确判断NestedScrollView是否能继续下拉
            if (nestedScrollView != null) {
                swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> {
                    return nestedScrollView.getScrollY() > 0;
                });
            }
            
            Log.d(TAG, "🎯 下拉刷新设置完成");
        }
    }
    
    /**
     * 🎯 设置监听器
     */
    private void setupListeners() {
        // 搜索按钮
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            });
        }
        
        // 刷新按钮
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> {
                if (dataStream != null) {
                    dataStream.forceUpdate();
                }
                loadPosts();
            });
        }
        
        // 创建帖子按钮
        if (fabCreatePost != null) {
            fabCreatePost.setOnClickListener(v -> {
                if (!SharedPrefsUtil.isLoggedIn(getContext())) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Intent intent = new Intent(getContext(), CreatePostActivity.class);
                startActivityForResult(intent, REQUEST_CREATE_POST);
            });
        }
    }
    
    /**
     * 数据流更新回调
     */
    private void onDataStreamUpdated(ForumDataStream.DataStreamEvent event) {
        if (getContext() == null) return;
        
        switch (event.getType()) {
            case POSTS_UPDATED:
                @SuppressWarnings("unchecked")
                List<ForumPost> updatedPosts = (List<ForumPost>) event.getData();
                updatePostList(updatedPosts);
                Log.d(TAG, "🎯 数据流更新: " + event.getMessage());
                break;
                
            case MESSAGES_UPDATED:
                Log.d(TAG, "🎯 消息更新: " + event.getMessage());
                break;
                
            case ERROR:
                showError("数据更新失败: " + event.getMessage());
                break;
                
            case STREAM_STOPPED:
                Log.d(TAG, "🎯 数据流已停止");
                break;
        }
    }
    
    /**
     * 🎯 检查数据并加载
     */
    private void checkDataAndLoad() {
        if (executor == null || getContext() == null) return;
        
        executor.execute(() -> {
            try {
                int postCount = forumPostDao.getTotalPostCount();
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (postCount == 0) {
                            showInitDataPrompt();
                        } else {
                            loadPosts();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "数据检查失败", Toast.LENGTH_SHORT).show();
                        loadPosts();
                    });
                }
            }
        });
    }
    
    /**
     * 🎯 加载帖子数据
     */
    private void loadPosts() {
        if (executor == null || swipeRefreshLayout == null) return;
        
        showLoading(true);
        
        executor.execute(() -> {
            List<ForumPost> posts = new ArrayList<>();
            
            try {
                if ("all".equals(currentCategory)) {
                    switch (currentSort) {
                        case "latest":
                            // 使用现有方法按时间排序
                            posts = forumPostDao.getAllPostsSortedByActivity();
                            break;
                        case "popular":
                            // 使用现有方法，可以根据点赞数排序（如果有的话）
                            posts = forumPostDao.getAllPostsSortedByActivity();
                            break;
                        default: // activity
                            List<ForumPost> normalPosts = forumPostDao.getAllPostsSortedByActivity();
                            posts.addAll(normalPosts);
                            
                            // 添加少量置顶帖
                            List<ForumPost> pinnedPosts = forumPostDao.getPinnedPosts();
                            if (pinnedPosts.size() > 3) {
                                pinnedPosts = pinnedPosts.subList(0, 3);
                            }
                            posts.addAll(0, pinnedPosts);
                            break;
                    }
                } else {
                    posts = forumPostDao.getPostsByCategory(currentCategory);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            final List<ForumPost> finalPosts = posts;
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    showLoading(false);
                    updatePostList(finalPosts);
                    
                    if (finalPosts.isEmpty()) {
                        showEmptyState();
                    } else {
                        hideEmptyState();
                        // 移除轮播功能调用
                    }
                });
            }
        });
    }
    
    /**
     * 🎯 更新帖子列表
     */
    private void updatePostList(List<ForumPost> posts) {
        try {
            if (postAdapter != null && posts != null) {
                postList.clear();
                postList.addAll(posts);
                postAdapter.notifyDataSetChanged();
                
                // 🎯 确保RecyclerView重新测量
                if (recyclerView != null) {
                    recyclerView.invalidate();
                    recyclerView.requestLayout();
                }
                
                // 更新状态文字
                if (statusText != null) {
                    statusText.setText("共加载 " + postList.size() + " 条帖子");
                    statusText.setVisibility(View.VISIBLE);
                }
                
                Log.d(TAG, "🎯 帖子列表更新完成，共 " + postList.size() + " 条帖子");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 更新帖子列表失败", e);
        }
    }
    
    // 🎯 移除了热门帖子轮播相关的所有方法
    // setupHotPostsCarousel() 和 setupCarouselIndicators() 已删除
    
    /**
     * dp转px工具方法
     */
    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * 🎯 显示/隐藏加载状态
     */
    private void showLoading(boolean show) {
        isLoading = show;
        
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(show);
        }
        
        if (statusText != null) {
            if (show) {
                statusText.setText("正在加载最新帖子...");
                statusText.setVisibility(View.VISIBLE);
            }
        }
        
        Log.d(TAG, show ? "🔄 显示加载状态" : "✅ 隐藏加载状态");
    }
    
    /**
     * 🎯 显示错误信息
     */
    private void showError(String message) {
        if (statusText != null) {
            statusText.setText("❌ " + message + "\n下拉刷新重试");
            statusText.setVisibility(View.VISIBLE);
        }
        
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "❌ 显示错误: " + message);
    }
    
    /**
     * 🎯 显示空状态
     */
    private void showEmptyState() {
        if (cardEmptyState != null) {
            cardEmptyState.setVisibility(View.VISIBLE);
        }
        
        if (tvEmptyTitle != null && tvEmptyMessage != null) {
            if ("all".equals(currentCategory)) {
                tvEmptyTitle.setText("暂无帖子");
                tvEmptyMessage.setText("成为第一个发帖的人吧！");
            } else {
                tvEmptyTitle.setText("该分类暂无帖子");
                tvEmptyMessage.setText("试试其他分类或发布新帖子");
            }
        }
    }
    
    /**
     * 🎯 隐藏空状态
     */
    private void hideEmptyState() {
        if (cardEmptyState != null) {
            cardEmptyState.setVisibility(View.GONE);
        }
    }
    
    /**
     * 显示数据初始化提示
     */
    private void showInitDataPrompt() {
        if (getContext() == null) return;
        
        Intent intent = new Intent(getContext(), DataInitActivity.class);
        startActivity(intent);
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
        // 简化菜单，主要功能移到UI中
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_POST && resultCode == getActivity().RESULT_OK) {
            loadPosts();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "🎯 ForumFragment onDestroyView");
        
        // 停止数据流
        if (dataStream != null) {
            try {
                dataStream.removeObserver(this::onDataStreamUpdated);
                dataStream.stopDataStream();
            } catch (Exception e) {
                Log.e(TAG, "停止数据流失败", e);
            }
        }
        
        // 清理资源
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        
        // 关闭线程池
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        
        Log.d(TAG, "🎯 ForumFragment destroyed, 资源已清理");
    }
}