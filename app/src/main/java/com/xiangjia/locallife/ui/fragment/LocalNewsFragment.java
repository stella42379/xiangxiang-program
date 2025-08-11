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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.network.NewsServiceManager;
import com.xiangjia.locallife.network.NewsServiceManager.UnifiedNewsItem;
import com.xiangjia.locallife.adapter.NewsAdapter;
import com.xiangjia.locallife.adapter.CarouselAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.TextUtils;

/**
 * 🔥 修复版新闻页面 - 解决嵌套滚动问题
 */
public class LocalNewsFragment extends Fragment {
    private static final String TAG = "LocalNewsFragment";
    
    // 🔥 修正看门狗超时 - 真正的10秒，不是80秒！
    private static final int WATCHDOG_TIMEOUT_MS = 10000; // 10秒
    
    // UI组件
    private RecyclerView newsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private TextView statusText;
    
    // 数据相关
    private NewsAdapter newsAdapter;
    private List<UnifiedNewsItem> newsList;
    private NewsServiceManager newsService;
    
    // 线程相关
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private boolean isLoading = false;
    private Runnable watchdogRunnable; // 网络看门狗
    
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
            nestedScrollView = view.findViewById(R.id.nested_scroll);
            statusText = view.findViewById(R.id.status_text);
            
            Log.d(TAG, "视图组件查找结果:");
            Log.d(TAG, "newsRecyclerView: " + (newsRecyclerView != null ? "✅找到" : "❌未找到"));
            Log.d(TAG, "swipeRefreshLayout: " + (swipeRefreshLayout != null ? "✅找到" : "❌未找到"));
            Log.d(TAG, "nestedScrollView: " + (nestedScrollView != null ? "✅找到" : "❌未找到"));
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
     * 🔥 关键：正确设置Adapter和嵌套滚动
     */
    private void setupAdapter() {
        try {
            if (newsRecyclerView != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                newsRecyclerView.setLayoutManager(layoutManager);
                
                // 🔥 关键：禁用嵌套滚动，避免冲突
                newsRecyclerView.setNestedScrollingEnabled(false);
                newsRecyclerView.setHasFixedSize(false); // 高度可变
                
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
                
                Log.d(TAG, "✅ NewsAdapter已设置，嵌套滚动已禁用");
            } else {
                Log.e(TAG, "❌ newsRecyclerView为null，无法设置Adapter");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置Adapter失败", e);
        }
    }
    
    /**
     * 🔥 修复：正确设置下拉刷新，处理NestedScrollView
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
            
            // 🔥 关键：让SwipeRefreshLayout正确判断NestedScrollView是否能继续下拉
            if (nestedScrollView != null) {
                swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> {
                    return nestedScrollView.getScrollY() > 0;
                });
            }
            
            Log.d(TAG, "✅ 下拉刷新设置完成，已适配NestedScrollView");
        }
    }
    
    /**
     * 🔥 核心方法：直接加载全球热门新闻（最快最全）
     */
    private void loadNews() {
        Log.d(TAG, "🚀 开始加载全球热门新闻");
        
        if (isLoading) {
            Log.d(TAG, "⚠️ 正在加载中，跳过重复请求");
            return;
        }
        
        // 显示加载状态
        showLoading(true);
        
        // 🔥 启动网络看门狗
        startNetworkWatchdog();
        
        // 🔥 新增：先做网络诊断（不影响主要流程）
        if (newsService != null) {
            newsService.testNetworkConnectivity(null);
        }
        
        // 🔥 直接获取全球热门新闻（无country限制）
        ioExecutor.execute(() -> {
            Log.d(TAG, "🌍 在后台线程开始获取全球热门新闻");
            
            try {
                // 获取全球热门新闻
                newsService.getGlobalTopNews(new NewsServiceManager.NewsCallback() {
                    @Override
                    public void onSuccess(List<UnifiedNewsItem> news) {
                        Log.d(TAG, "✅ 全球新闻请求成功: " + (news != null ? news.size() : 0) + "条");
                        
                        // 🔥 关键：切换到主线程更新UI
                        mainHandler.post(() -> {
                            try {
                                stopNetworkWatchdog(); // 停止看门狗
                                showLoading(false);
                                
                                if (news != null && !news.isEmpty()) {
                                    hideError();
                                    updateNewsList(news);
                                    Log.d(TAG, "✅ UI更新完成，显示了 " + news.size() + " 条全球新闻");
                                } else {
                                    showError("暂无新闻数据，请下拉刷新重试");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "❌ 处理成功回调时出错", e);
                                showError("处理新闻数据时出错: " + e.getMessage());
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ 全球新闻请求失败: " + error);
                        
                        // 🔥 关键：切换到主线程更新UI
                        mainHandler.post(() -> {
                            try {
                                stopNetworkWatchdog(); // 停止看门狗
                                showLoading(false);
                                
                                // 🔥 分析错误类型，给出更具体的提示
                                String userFriendlyError;
                                if (error.contains("DNS") || error.contains("UnknownHostException")) {
                                    userFriendlyError = "网络DNS解析失败，请检查网络连接";
                                } else if (error.contains("timeout") || error.contains("超时")) {
                                    userFriendlyError = "网络连接超时，请检查网络状况";
                                } else if (error.contains("ConnectException")) {
                                    userFriendlyError = "无法连接到服务器，请检查网络";
                                } else {
                                    userFriendlyError = "新闻加载失败: " + error;
                                }
                                
                                showError(userFriendlyError);
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
                    stopNetworkWatchdog();
                    showLoading(false);
                    showError("网络请求异常: " + e.getMessage());
                });
            }
        });
    }
    
    /**
     * 🔥 网络看门狗：防止请求卡住，支持取消OkHttp请求
     */
    private void startNetworkWatchdog() {
        stopNetworkWatchdog(); // 先停止之前的
        
        watchdogRunnable = () -> {
            if (isLoading) {
                Log.w(TAG, "🐕 网络看门狗触发：请求超时，取消当前请求");
                
                // 🔥 关键：取消OkHttp请求
                if (newsService != null) {
                    newsService.cancelCurrentRequest();
                }
                
                showLoading(false);
                showError("网络响应超时，请下拉重试");
            }
        };
        
        mainHandler.postDelayed(watchdogRunnable, WATCHDOG_TIMEOUT_MS);
        Log.d(TAG, "🐕 网络看门狗已启动，" + WATCHDOG_TIMEOUT_MS + "ms后触发");
    }
    
    /**
     * 停止网络看门狗
     */
    private void stopNetworkWatchdog() {
        if (watchdogRunnable != null) {
            mainHandler.removeCallbacks(watchdogRunnable);
            watchdogRunnable = null;
            Log.d(TAG, "🐕 网络看门狗已停止");
        }
    }
    
    /**
     * 加载澳洲新闻作为备用（如果国际新闻失败）
     */
    private void loadAustralianNewsAsFallback() {
        Log.d(TAG, "🇦🇺 尝试加载澳洲新闻作为备用");
        
        ioExecutor.execute(() -> {
            try {
                newsService.getAustralianNews(new NewsServiceManager.NewsCallback() {
                    @Override
                    public void onSuccess(List<UnifiedNewsItem> news) {
                        Log.d(TAG, "✅ 澳洲新闻请求成功: " + (news != null ? news.size() : 0) + "条");
                        
                        mainHandler.post(() -> {
                            try {
                                showLoading(false);
                                
                                if (news != null && !news.isEmpty()) {
                                    hideError();
                                    updateNewsList(news);
                                    Log.d(TAG, "✅ 澳洲新闻UI更新完成，显示了 " + news.size() + " 条新闻");
                                } else {
                                    showError("所有新闻源都没有数据");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "❌ 处理澳洲新闻成功回调时出错", e);
                                showError("处理澳洲新闻数据时出错: " + e.getMessage());
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ 澳洲新闻也请求失败: " + error);
                        
                        mainHandler.post(() -> {
                            showLoading(false);
                            showError("所有新闻源都加载失败，请检查网络连接");
                        });
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "❌ 澳洲新闻请求异常", e);
                
                mainHandler.post(() -> {
                    showLoading(false);
                    showError("澳洲新闻请求异常: " + e.getMessage());
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
                
                // 🔥 强制刷新适配器
                newsAdapter.notifyDataSetChanged();
                
                // 🔥 确保RecyclerView重新测量
                if (newsRecyclerView != null) {
                    newsRecyclerView.invalidate();
                    newsRecyclerView.requestLayout();
                }
                
                // 🔥 新增：设置轮播数据（取前3条作为头条推荐）
                setupCarouselData(news);
                
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
     * 🎯 设置轮播数据
     */
    private void setupCarouselData(List<UnifiedNewsItem> news) {
        try {
            // 查找轮播ViewPager
            androidx.viewpager2.widget.ViewPager2 carouselViewPager = 
                getView().findViewById(R.id.carousel_viewpager);
            
            if (carouselViewPager != null && news != null && !news.isEmpty()) {
                // 取前3条新闻作为轮播内容
                List<UnifiedNewsItem> carouselNews = news.subList(0, Math.min(3, news.size()));
                
                // 创建轮播适配器
                CarouselAdapter carouselAdapter = new CarouselAdapter(carouselNews, this::navigateToNewsDetail);
                carouselViewPager.setAdapter(carouselAdapter);
                
                // 设置轮播指示器
                setupCarouselIndicators(carouselNews.size());
                
                Log.d(TAG, "✅ 轮播数据设置完成，共 " + carouselNews.size() + " 条");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置轮播数据失败", e);
        }
    }
    
    /**
     * 🎯 设置轮播指示器
     */
    private void setupCarouselIndicators(int count) {
        try {
            android.widget.LinearLayout indicatorLayout = 
                getView().findViewById(R.id.carousel_indicators);
            
            if (indicatorLayout != null) {
                indicatorLayout.removeAllViews();
                
                for (int i = 0; i < count; i++) {
                    View dot = new View(getContext());
                    android.widget.LinearLayout.LayoutParams params = 
                        new android.widget.LinearLayout.LayoutParams(
                            dpToPx(6), dpToPx(6));
                    params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
                    dot.setLayoutParams(params);
                    
                    // 设置圆形背景
                    android.graphics.drawable.GradientDrawable drawable = 
                        new android.graphics.drawable.GradientDrawable();
                    drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                    drawable.setColor(i == 0 ? 0xFFFFFFFF : 0x40FFFFFF);
                    dot.setBackground(drawable);
                    
                    indicatorLayout.addView(dot);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置轮播指示器失败", e);
        }
    }
    
    /**
     * dp转px工具方法
     */
    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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
     * 🔥 修复：跳转到新闻详情页 - 使用现有的NewsDetailActivity
     */
    private void navigateToNewsDetail(UnifiedNewsItem newsItem) {
        try {
            Log.d(TAG, "📰 点击新闻: " + newsItem.getTitle());
            Log.d(TAG, "📰 新闻URL: " + newsItem.getUrl());
            
            if (getContext() == null) {
                Log.e(TAG, "❌ Context为空，无法跳转");
                return;
            }
            
            // 🔥 使用现有的NewsDetailActivity
            android.content.Intent intent = new android.content.Intent(getContext(), 
                com.xiangjia.locallife.ui.activity.NewsDetailActivity.class);
            
            // 按照现有Activity的Intent参数传递数据
            intent.putExtra("news_title", newsItem.getTitle());
            intent.putExtra("news_source", newsItem.getSource());
            intent.putExtra("news_url", newsItem.getUrl());
            intent.putExtra("news_thumbnail", newsItem.getImageUrl());
            intent.putExtra("news_time", newsItem.getPublishedAt());
            intent.putExtra("news_category", "热点新闻"); // 默认分类
            
            try {
                startActivity(intent);
                Log.d(TAG, "✅ 成功跳转到新闻详情页");
            } catch (android.content.ActivityNotFoundException e) {
                Log.e(TAG, "❌ 未找到NewsDetailActivity，使用浏览器打开");
                openInBrowser(newsItem.getUrl());
            }
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 跳转新闻详情失败", e);
            // 降级方案：直接用浏览器打开
            openInBrowser(newsItem.getUrl());
        }
    }
    
    /**
     * 🔥 降级方案：用浏览器打开新闻
     */
    private void openInBrowser(String url) {
        try {
            if (TextUtils.isEmpty(url) || getContext() == null) {
                Toast.makeText(getContext(), "新闻链接无效", Toast.LENGTH_SHORT).show();
                return;
            }
            
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(url));
            
            // 检查是否有应用可以处理这个Intent
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
                Log.d(TAG, "✅ 用浏览器打开新闻: " + url);
            } else {
                Toast.makeText(getContext(), "无法打开链接，请安装浏览器应用", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "❌ 没有应用可以打开URL: " + url);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 用浏览器打开失败", e);
            Toast.makeText(getContext(), "打开链接失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 分享新闻
     */
    private void shareNews(UnifiedNewsItem newsItem) {
        try {
            Log.d(TAG, "📤 分享新闻: " + newsItem.getTitle());
            Toast.makeText(getContext(), "分享: " + newsItem.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: 实现分享功能
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
        
        // 停止网络看门狗
        stopNetworkWatchdog();
        
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