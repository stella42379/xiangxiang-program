package com.xiangjia.locallife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

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
import com.xiangjia.locallife.model.User;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.util.SharedPrefsUtil;

// 新增的高级功能导入
import com.xiangjia.locallife.service.ForumDataStream;
import com.xiangjia.locallife.service.ForumSearchManager;
import com.xiangjia.locallife.factory.ForumPostFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 论坛Fragment - 集成高级功能
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
    
    // 高级功能
    private ForumDataStream dataStream;
    private ForumSearchManager searchManager;
    
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
            
            // 初始化高级功能
            initAdvancedFeatures();
        }
    }
    
    /**
     * 初始化高级功能
     */
    private void initAdvancedFeatures() {
        try {
            // 1. 初始化数据流 (Observer Pattern + Singleton Pattern)
            dataStream = ForumDataStream.getInstance(AppDatabase.getInstance(getContext()));
            dataStream.addObserver(this::onDataStreamUpdated);
            dataStream.startDataStream();
            
            // 2. 初始化搜索管理器 (Singleton Pattern + Strategy Pattern)
            searchManager = ForumSearchManager.getInstance();
            searchManager.setAdvancedSearch(true); // 使用高级搜索
            
            Log.d(TAG, "高级功能初始化完成");
            Log.d(TAG, "当前搜索策略: " + searchManager.getCurrentStrategyName());
            
        } catch (Exception e) {
            Log.e(TAG, "高级功能初始化失败", e);
            Toast.makeText(getContext(), "高级功能初始化失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 数据流更新回调 (Observer Pattern 实现)
     */
    private void onDataStreamUpdated(ForumDataStream.DataStreamEvent event) {
        if (getContext() == null) return;
        
        switch (event.getType()) {
            case POSTS_UPDATED:
                @SuppressWarnings("unchecked")
                List<ForumPost> updatedPosts = (List<ForumPost>) event.getData();
                updatePostList(updatedPosts);
                Log.d(TAG, "数据流更新: " + event.getMessage());
                break;
                
            case MESSAGES_UPDATED:
                // 消息更新，可以在这里处理消息相关的UI更新
                Log.d(TAG, "消息更新: " + event.getMessage());
                break;
                
            case ERROR:
                Toast.makeText(getContext(), "数据更新失败: " + event.getMessage(), Toast.LENGTH_SHORT).show();
                break;
                
            case STREAM_STOPPED:
                Log.d(TAG, "数据流已停止");
                break;
        }
    }
    
    /**
     * 更新帖子列表
     */
    private void updatePostList(List<ForumPost> updatedPosts) {
        if (updatedPosts != null && !updatedPosts.isEmpty()) {
            // 智能更新：只添加新帖子，避免重复
            List<String> existingIds = new ArrayList<>();
            for (ForumPost post : postList) {
                existingIds.add(post.getPostId());
            }
            
            int newPostsCount = 0;
            for (ForumPost post : updatedPosts) {
                if (!existingIds.contains(post.getPostId())) {
                    postList.add(0, post); // 添加到顶部
                    newPostsCount++;
                }
            }
            
            if (newPostsCount > 0) {
                postAdapter.notifyDataSetChanged();
                // 显示更新提示
                String message = "发现 " + newPostsCount + " 条新帖子";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews(View view) {
        categoryChipGroup = view.findViewById(R.id.chipGroup);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        fabCreatePost = view.findViewById(R.id.fab_create_post);

        TextView headerTitle = view.findViewById(R.id.tvHeaderTitle);
        TextView headerSub = view.findViewById(R.id.tvHeaderSub);
        if (headerTitle != null) {
            headerTitle.setText("社区论坛");
        }
        if (headerSub != null) {
            headerSub.setText("Community Forum");
        }
    }
    
    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        
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
            // 手动触发数据流更新
            if (dataStream != null) {
                dataStream.forceUpdate();
            }
            loadPosts();
        });
        
        // 创建帖子按钮 - 使用 Factory Pattern
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
                int postCount = forumPostDao.getTotalPostCount();
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (postCount == 0) {
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
     * 加载帖子数据 - 修改避免全是置顶帖
     */
    private void loadPosts() {
        if (executor == null || swipeRefreshLayout == null) return;
        
        swipeRefreshLayout.setRefreshing(true);
        
        executor.execute(() -> {
            List<ForumPost> posts = new ArrayList<>();
            
            try {
                if (!currentSearchQuery.isEmpty()) {
                    // 搜索模式 - 使用高级搜索
                    performAdvancedSearch(currentSearchQuery);
                    return;
                } else if ("all".equals(currentCategory)) {
                    // 获取普通帖子
                    List<ForumPost> normalPosts = forumPostDao.getAllPostsSortedByActivity();
                    posts.addAll(normalPosts);
                    
                    // 只在按活动时间排序时显示置顶帖，并限制数量
                    if ("activity".equals(currentSort)) {
                        List<ForumPost> pinnedPosts = forumPostDao.getPinnedPosts();
                        // 限制置顶帖数量为3个
                        if (pinnedPosts.size() > 3) {
                            pinnedPosts = pinnedPosts.subList(0, 3);
                        }
                        // 置顶帖放在最前面
                        posts.addAll(0, pinnedPosts);
                    }
                } else {
                    // 特定分类
                    posts = forumPostDao.getPostsByCategory(currentCategory);
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
     * 执行高级搜索 - 使用新的搜索管理器
     */
    private void performAdvancedSearch(String query) {
        if (searchManager == null) {
            Toast.makeText(getContext(), "搜索功能未初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        
        executor.execute(() -> {
            try {
                // 获取所有帖子
                List<ForumPost> allPosts = forumPostDao.getAllPostsSortedByActivity();
                
                // 执行搜索 (使用 Strategy Pattern)
                ForumSearchManager.SearchResult result = searchManager.search(query, allPosts);
                
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    
                    if (result.isSuccess()) {
                        // 更新搜索结果
                        postList.clear();
                        postList.addAll(result.getResults());
                        postAdapter.notifyDataSetChanged();
                        
                        // 显示搜索结果统计
                        String message = result.getResultSummary();
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        
                        Log.d(TAG, "搜索完成: " + message);
                        Log.d(TAG, "使用策略: " + searchManager.getCurrentStrategyName());
                    } else {
                        Toast.makeText(getContext(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "搜索出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    
    /**
     * 切换搜索策略（可选，用于测试）
     */
    public void toggleSearchStrategy() {
        if (searchManager != null) {
            boolean isAdvanced = searchManager.isUsingAdvancedSearch();
            searchManager.setAdvancedSearch(!isAdvanced);
            
            String newStrategy = searchManager.getCurrentStrategyName();
            Toast.makeText(getContext(), "切换到: " + newStrategy, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "搜索策略切换到: " + newStrategy);
        }
    }
    
    /**
     * 创建演示帖子的方法（可选，用于测试 Factory Pattern）
     */
    private void createDemoPosts() {
        executor.execute(() -> {
            try {
                // 获取当前用户（简化处理）
                User currentUser = new User("demo_user", "demo@example.com", "password");
                currentUser.setNickname("演示用户");
                
                // 使用 Factory Pattern 创建不同类型的帖子
                ForumPost helpPost = ForumPostFactory.createTemplatePost(ForumPostFactory.PostType.HELP, currentUser);
                ForumPost sharePost = ForumPostFactory.createTemplatePost(ForumPostFactory.PostType.SHARE, currentUser);
                ForumPost friendsPost = ForumPostFactory.createTemplatePost(ForumPostFactory.PostType.FRIENDS, currentUser);
                
                // 保存到数据库
                forumPostDao.insert(helpPost);
                forumPostDao.insert(sharePost);
                forumPostDao.insert(friendsPost);
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "演示帖子创建完成", Toast.LENGTH_SHORT).show();
                    loadPosts(); // 刷新列表
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "创建演示帖子失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forum, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            
            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        currentSearchQuery = query;
                        performAdvancedSearch(query);
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
        
        // 停止数据流
        if (dataStream != null) {
            dataStream.removeObserver(this::onDataStreamUpdated);
            dataStream.stopDataStream();
        }
        
        // 清理线程池
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        
        Log.d(TAG, "ForumFragment destroyed, 资源已清理");
    }
}