package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.ForumPostAdapter;
import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.database.AppDatabase;
import com.xiangjia.locallife.database.ForumPostDao;
import com.xiangjia.locallife.service.ForumSearchManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 论坛搜索页面 - 修复编译错误版本
 */
public class SearchActivity extends AppCompatActivity {
    
    private static final String TAG = "SearchActivity";
    
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ForumPostAdapter searchAdapter;
    private List<ForumPost> searchResults;
    
    private ForumPostDao forumPostDao;
    private ForumSearchManager searchManager;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        initViews();
        initData();
        setupSearchView();
        setupRecyclerView();
        
        // 自动显示键盘
        showKeyboard();
    }
    
    private void initViews() {
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view_search);
        
        // 设置ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("高级搜索");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initData() {
        searchResults = new ArrayList<>();
        searchAdapter = new ForumPostAdapter(this, searchResults);
        
        // 初始化数据库和搜索管理器
        forumPostDao = AppDatabase.getDatabase(this).forumPostDao();
        searchManager = ForumSearchManager.getInstance();
        
        // 使用基本搜索策略
        searchManager.setAdvancedSearch(false);
        
        executor = Executors.newCachedThreadPool();
    }
    
    private void setupSearchView() {
        // 设置SearchView配置
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("输入搜索关键词...");
        searchView.setSubmitButtonEnabled(true);
        
        // 强制显示搜索框
        searchView.setIconified(false);
        searchView.clearFocus();
        
        // 设置监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "搜索提交: " + query);
                performAdvancedSearch(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "搜索文本变化: " + newText);
                
                // 实时搜索建议
                if (!TextUtils.isEmpty(newText) && newText.length() >= 1) {
                    List<String> suggestions = searchManager.getQuerySuggestions(newText);
                    Log.d(TAG, "搜索建议: " + suggestions);
                    
                    // 如果输入的是单个字符，也尝试搜索
                    if (newText.length() >= 1) {
                        performAdvancedSearch(newText);
                    }
                }
                
                return true;
            }
        });
        
        // 设置焦点变化监听
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "搜索框焦点变化: " + hasFocus);
                if (hasFocus) {
                    showKeyboard();
                }
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchAdapter);
        
        // 设置点击监听
        searchAdapter.setOnPostClickListener(new ForumPostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(ForumPost post) {
                Intent intent = new Intent(SearchActivity.this, PostDetailActivity.class);
                intent.putExtra("postId", post.getPostId());
                startActivity(intent);
                finish();
            }
            
            @Override
            public void onLikeClick(ForumPost post) {
                Toast.makeText(SearchActivity.this, "点赞: " + post.getTitle(), Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onUserClick(ForumPost post) {
                Toast.makeText(SearchActivity.this, "查看用户: " + post.getAuthorName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * 显示软键盘
     */
    private void showKeyboard() {
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }
    
    /**
     * 执行高级搜索 - 增强调试版本
     */
    private void performAdvancedSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            searchResults.clear();
            searchAdapter.notifyDataSetChanged();
            return;
        }
        
        Log.d(TAG, "开始执行搜索: '" + query + "'");
        
        // 验证查询语法
        if (!searchManager.validateQuery(query)) {
            Log.w(TAG, "搜索语法错误: " + query);
            Toast.makeText(this, "搜索语法错误，请检查查询格式", Toast.LENGTH_LONG).show();
            return;
        }
        
        executor.execute(() -> {
            try {
                // 获取所有帖子
                List<ForumPost> allPosts = forumPostDao.getAllPostsSortedByActivity();
                Log.d(TAG, "从数据库获取帖子数量: " + allPosts.size());
                
                // 如果没有帖子，提示用户
                if (allPosts.isEmpty()) {
                    runOnUiThread(() -> {
                        Toast.makeText(SearchActivity.this, "数据库中没有帖子数据，请先初始化数据", Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                
                // 记录前几个帖子的信息用于调试
                for (int i = 0; i < Math.min(3, allPosts.size()); i++) {
                    ForumPost post = allPosts.get(i);
                    Log.d(TAG, String.format("帖子%d: 标题='%s', 内容='%s', 分类='%s'", 
                        i, post.getTitle(), 
                        post.getContent() != null ? post.getContent().substring(0, Math.min(20, post.getContent().length())) + "..." : "null",
                        post.getCategory()));
                }
                
                // 执行搜索
                ForumSearchManager.SearchResult result = searchManager.search(query, allPosts);
                
                Log.d(TAG, String.format("搜索完成: 成功=%s, 结果数量=%d, 耗时=%dms", 
                    result.isSuccess(), result.getResultCount(), result.getSearchTime()));
                
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        searchResults.clear();
                        searchResults.addAll(result.getResults());
                        searchAdapter.notifyDataSetChanged();
                        
                        String message = String.format("找到 %d 条结果", result.getResultCount());
                        Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        
                        if (result.getResultCount() == 0) {
                            // 显示更详细的调试信息
                            showDebugInfo(query, allPosts);
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, 
                            "搜索失败: " + result.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "搜索异常", e);
                runOnUiThread(() -> {
                    Toast.makeText(SearchActivity.this, "搜索出现异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * 显示调试信息
     */
    private void showDebugInfo(String query, List<ForumPost> allPosts) {
        // 检查是否有包含查询字符的帖子
        int partialMatches = 0;
        for (ForumPost post : allPosts) {
            String title = post.getTitle() != null ? post.getTitle().toLowerCase() : "";
            String content = post.getContent() != null ? post.getContent().toLowerCase() : "";
            if (title.contains(query.toLowerCase()) || content.contains(query.toLowerCase())) {
                partialMatches++;
                Log.d(TAG, "潜在匹配帖子: " + post.getTitle());
            }
        }
        
        String debugMessage = String.format("调试信息:\n• 查询: '%s'\n• 总帖子数: %d\n• 潜在匹配: %d\n\n建议尝试:\n• 英文字符: a, s, t\n• 中文关键词: 讨论, 社区\n• 分类搜索: category:discussion",
            query, allPosts.size(), partialMatches);
            
        Toast.makeText(this, debugMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, debugMessage);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 简化菜单，避免资源不存在的错误
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}