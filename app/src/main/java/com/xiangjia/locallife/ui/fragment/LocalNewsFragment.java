package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.network.NewsServiceManager;
import com.xiangjia.locallife.network.NewsServiceManager.UnifiedNewsItem;
import com.xiangjia.locallife.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 最稳版本的新闻页面 - 按小承建议优化
 */
public class LocalNewsFragment extends Fragment {
    private static final String TAG = "LocalNewsFragment";
    
    // UI组件
    private RecyclerView newsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View loadingView;
    private View errorView;
    private TextView errorText;
    private TextView statusText;
    
    // 数据相关
    private NewsAdapter newsAdapter;
    private List<UnifiedNewsItem> newsList;
    private NewsServiceManager newsService;
    
    // 线程相关
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private boolean isLoading = false;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "LocalNewsFragment onCreateView");
        return inflater.inflate(R.layout.fragment_local_news, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "LocalNewsFragment onViewCreated");
        
        initViews(view);
        initNewsService();
        setupAdapter(); // 🔥 关键：先挂Adapter，避免 "No adapter attached"
        setupSwipeRefresh();
        loadNews(); // 然后才加载数据
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews(View view) {
        Log.d(TAG, "开始初始化视图组件");
        
        try {
            newsRecyclerView = view.findViewById(R.id.news_recycler_view);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            statusText = view.findViewById(R.id.status_text);
            
            // 使用statusText作为loading和error的显示
            loadingView = statusText;
            errorView = statusText;
            errorText = statusText;
            
            Log.d(TAG, "视图组件查找结果:");
            Log.d(TAG, "newsRecyclerView: " + (newsRecyclerView != null ? "✅找到" : "❌未找到"));
            Log.d(TAG, "swipeRefreshLayout: " + (swipeRefreshLayout != null ? "✅找到" : "❌未找到"));
            Log.d(TAG, "statusText: " + (statusText != null ? "✅找到" : "❌未找到"));
            
            // 初始化数据
            newsList = new ArrayList<>();
            
            Log.d(TAG, "视图组件初始化完成");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 视图组件初始化失败", e);
        }
    }
    
    /**
     * 初始化新闻服务
     */
    private void initNewsService() {
        try {
            newsService = NewsServiceManager.getInstance();
            Log.d(TAG, "✅ 新闻服务初始化成功");
        } catch (Exception e) {
            Log.e(TAG, "❌ 新闻服务初始化失败", e);
        }
    }
    
    /**
     * 🔥 关键：先设置Adapter，避免RecyclerView报错
     */
    private void setupAdapter() {
        try {
            if (newsRecyclerView != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                newsRecyclerView.setLayoutManager(layoutManager);
                
                // 创建适配器（即使数据为空也要先挂上）
                newsAdapter = new NewsAdapter(newsList, new NewsAdapter.OnNewsClickListener() {
                    @Override
                    public void onNewsClick(UnifiedNewsItem newsItem) {
                        navigateToNewsDetail(newsItem);
                    }
                    
                    @Override
                    public void onShareClick(UnifiedNewsItem newsItem) {
                        shareNews(newsItem);
                    }
                });
                
                // 🔥 关键：立即设置Adapter
                newsRecyclerView.setAdapter(newsAdapter);
                
                Log.d(TAG, "✅ NewsAdapter已设置，避免了 'No adapter attached' 错误");
            } else {
                Log.e(TAG, "❌ newsRecyclerView为null，无法设置Adapter");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置Adapter失败", e);
        }
    }
    
    /**
     * 设置下拉刷新
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
                loadNews();
            });
            
            Log.d(TAG, "✅ 下拉刷新设置完成");
        }
    }
    
    /**
     * 🔥 核心方法：加载新闻数据
     */
    private void loadNews() {
        Log.d(TAG, "🚀 开始加载新闻数据");
        
        if (isLoading) {
            Log.d(TAG, "⚠️ 正在加载中，跳过重复请求");
            return;
        }
        
        // 显示加载状态
        showLoading(true);
        
        // 🔥 关键：在后台线程执行网络请求
        ioExecutor.execute(() -> {
            Log.d(TAG, "📡 在后台线程开始网络请求");
            
            try {
                // 调用新闻服务
                newsService.getAustralianNews(new NewsServiceManager.NewsCallback() {
                    @Override
                    public void onSuccess(List<UnifiedNewsItem> news) {
                        Log.d(TAG, "✅ 澳洲新闻请求成功: " + (news != null ? news.size() : 0) + "条");
                        
                        // 🔥 关键：切换到主线程更新UI
                        mainHandler.post(() -> {
                            try {
                                showLoading(false);
                                
                                if (news != null && !news.isEmpty()) {
                                    hideError();
                                    updateNewsList(news);
                                    Log.d(TAG, "✅ UI更新完成，显示了 " + news.size() + " 条新闻");
                                } else {
                                    showError("没有获取到澳洲新闻，尝试加载国际新闻...");
                                    // 尝试加载国际新闻
                                    loadInternationalNewsAsFallback();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "❌ 处理成功回调时出错", e);
                                showError("处理新闻数据时出错: " + e.getMessage());
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ 澳洲新闻请求失败: " + error);
                        
                        // 🔥 关键：切换到主线程更新UI
                        mainHandler.post(() -> {
                            try {
                                showLoading(false);
                                showError("澳洲新闻加载失败，尝试国际新闻...");
                                // 尝试加载国际新闻作为备用
                                loadInternationalNewsAsFallback();
                            } catch (Exception e) {
                                Log.e(TAG, "❌ 处理错误回调时出错", e);
                            }
                        });
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "❌ 网络请求异常", e);
                
                // 🔥 关键：异常时也要在主线程更新UI
                mainHandler.post(() -> {
                    showLoading(false);
                    showError("网络请求异常: " + e.getMessage());
                });
            }
        });
    }
    
    /**
     * 加载国际新闻作为备用
     */
    private void loadInternationalNewsAsFallback() {
        Log.d(TAG, "🌍 尝试加载国际新闻作为备用");
        
        ioExecutor.execute(() -> {
            try {
                newsService.getInternationalNews(new NewsServiceManager.NewsCallback() {
                    @Override
                    public void onSuccess(List<UnifiedNewsItem> news) {
                        Log.d(TAG, "✅ 国际新闻请求成功: " + (news != null ? news.size() : 0) + "条");
                        
                        mainHandler.post(() -> {
                            try {
                                showLoading(false);
                                
                                if (news != null && !news.isEmpty()) {
                                    hideError();
                                    updateNewsList(news);
                                    Log.d(TAG, "✅ 国际新闻UI更新完成，显示了 " + news.size() + " 条新闻");
                                } else {
                                    showError("所有新闻源都没有数据");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "❌ 处理国际新闻成功回调时出错", e);
                                showError("处理国际新闻数据时出错: " + e.getMessage());
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ 国际新闻也请求失败: " + error);
                        
                        mainHandler.post(() -> {
                            showLoading(false);
                            showError("所有新闻源都加载失败: " + error);
                        });
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "❌ 国际新闻请求异常", e);
                
                mainHandler.post(() -> {
                    showLoading(false);
                    showError("国际新闻请求异常: " + e.getMessage());
                });
            }
        });
    }
    
    /**
     * 🔥 关键：更新新闻列表（在主线程中调用）
     */
    private void updateNewsList(List<UnifiedNewsItem> news) {
        try {
            if (newsAdapter != null && news != null) {
                newsList.clear();
                newsList.addAll(news);
                newsAdapter.notifyDataSetChanged();
                
                Log.d(TAG, "✅ 新闻列表更新完成，共 " + newsList.size() + " 条新闻");
                
                // 更新状态文字
                if (statusText != null) {
                    statusText.setText("共加载 " + newsList.size() + " 条新闻");
                    statusText.setVisibility(View.VISIBLE);
                }
            } else {
                Log.e(TAG, "❌ 无法更新新闻列表: adapter=" + (newsAdapter != null) + ", news=" + (news != null));
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 更新新闻列表时出错", e);
        }
    }
    
    /**
     * 显示/隐藏加载状态
     */
    private void showLoading(boolean show) {
        isLoading = show;
        
        // 更新下拉刷新状态
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(show);
        }
        
        // 更新状态文字
        if (statusText != null) {
            if (show) {
                statusText.setText("正在加载最新新闻...");
                statusText.setVisibility(View.VISIBLE);
            }
        }
        
        Log.d(TAG, show ? "🔄 显示加载状态" : "✅ 隐藏加载状态");
    }
    
    /**
     * 显示错误信息
     */
    private void showError(String message) {
        if (statusText != null) {
            statusText.setText("❌ " + message + "\n下拉刷新重试");
            statusText.setVisibility(View.VISIBLE);
        }
        
        Log.e(TAG, "❌ 显示错误: " + message);
    }
    
    /**
     * 隐藏错误信息
     */
    private void hideError() {
        // 错误信息会被成功状态覆盖，这里不需要特别处理
    }
    
    /**
     * 跳转到新闻详情
     */
    private void navigateToNewsDetail(UnifiedNewsItem newsItem) {
        try {
            Log.d(TAG, "📰 点击新闻: " + newsItem.getTitle());
            Toast.makeText(getContext(), "点击了: " + newsItem.getTitle(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "❌ 跳转新闻详情失败", e);
        }
    }
    
    /**
     * 分享新闻
     */
    private void shareNews(UnifiedNewsItem newsItem) {
        try {
            Log.d(TAG, "📤 分享新闻: " + newsItem.getTitle());
            Toast.makeText(getContext(), "分享: " + newsItem.getTitle(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "❌ 分享失败", e);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "LocalNewsFragment onResume");
        
        // 如果没有数据且不在加载中，重新加载
        if (newsList.isEmpty() && !isLoading) {
            Log.d(TAG, "🔄 Resume时重新加载数据");
            loadNews();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "LocalNewsFragment onDestroyView");
        
        // 清理资源
        if (newsRecyclerView != null) {
            newsRecyclerView.setAdapter(null);
        }
        
        // 关闭线程池
        if (ioExecutor != null && !ioExecutor.isShutdown()) {
            ioExecutor.shutdown();
        }
    }
}