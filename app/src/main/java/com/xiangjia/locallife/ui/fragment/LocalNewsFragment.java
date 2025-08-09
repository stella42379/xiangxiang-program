package com.xiangjia.locallife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.xiangjia.locallife.model.NewsItem;
import com.xiangjia.locallife.ui.adapter.NewsAdapter;
import com.xiangjia.locallife.ui.adapter.NewsCarouselAdapter;
import com.xiangjia.locallife.ui.activity.NewsDetailActivity;
import com.xiangjia.locallife.utils.NewsDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地新闻Fragment - 完整功能版本
 */
public class LocalNewsFragment extends Fragment {
    
    private static final String TAG = "LocalNewsFragment";
    
    // 主要视图组件
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView newsRecyclerView;
    private ViewPager2 carouselViewPager;
    private TextView statusText;
    
    // 适配器
    private NewsAdapter newsAdapter;
    private NewsCarouselAdapter carouselAdapter;
    
    // 数据
    private List<NewsItem> newsList = new ArrayList<>();
    private List<NewsItem> featuredNewsList = new ArrayList<>();
    private boolean isLoading = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "LocalNewsFragment onCreateView");
        
        try {
            // 创建主布局
            LinearLayout mainLayout = createMainLayout();
            
            initViews(mainLayout);
            initCarousel();
            initRecyclerView();
            loadNewsData();
            
            Log.d(TAG, "LocalNewsFragment 创建成功");
            return mainLayout;
            
        } catch (Exception e) {
            Log.e(TAG, "LocalNewsFragment 创建失败", e);
            return createErrorLayout();
        }
    }
    
    /**
     * 创建主布局
     */
    private LinearLayout createMainLayout() {
        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
        
        // 创建标题区域
        LinearLayout titleLayout = createTitleLayout();
        mainLayout.addView(titleLayout);
        
        // 创建轮播区域
        LinearLayout carouselContainer = createCarouselContainer();
        mainLayout.addView(carouselContainer);
        
        // 创建下拉刷新容器
        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
        LinearLayout.LayoutParams refreshParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        );
        swipeRefreshLayout.setLayoutParams(refreshParams);
        
        // 创建新闻列表容器
        LinearLayout newsContainer = createNewsListContainer();
        swipeRefreshLayout.addView(newsContainer);
        mainLayout.addView(swipeRefreshLayout);
        
        return mainLayout;
    }
    
    /**
     * 创建标题布局
     */
    private LinearLayout createTitleLayout() {
        LinearLayout titleLayout = new LinearLayout(getContext());
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setPadding(dp(16), dp(16), dp(16), dp(8));
        titleLayout.setBackgroundColor(Color.WHITE);
        
        TextView mainTitle = new TextView(getContext());
        mainTitle.setText("今日时讯");
        mainTitle.setTextSize(32);
        mainTitle.setTextColor(Color.parseColor("#333333"));
        mainTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView subTitle = new TextView(getContext());
        subTitle.setText("Local News");
        subTitle.setTextSize(16);
        subTitle.setTextColor(Color.parseColor("#666666"));
        subTitle.setPadding(0, dp(8), 0, 0);
        
        titleLayout.addView(mainTitle);
        titleLayout.addView(subTitle);
        
        return titleLayout;
    }
    
    /**
     * 创建轮播容器
     */
    private LinearLayout createCarouselContainer() {
        LinearLayout carouselContainer = new LinearLayout(getContext());
        carouselContainer.setOrientation(LinearLayout.VERTICAL);
        carouselContainer.setBackgroundColor(Color.WHITE);
        carouselContainer.setPadding(dp(16), 0, dp(16), dp(16));
        
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(220)
        );
        carouselContainer.setLayoutParams(containerParams);
        
        // 创建轮播ViewPager
        carouselViewPager = new ViewPager2(getContext());
        LinearLayout.LayoutParams vpParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(200)
        );
        carouselViewPager.setLayoutParams(vpParams);
        
        carouselContainer.addView(carouselViewPager);
        
        return carouselContainer;
    }
    
    /**
     * 创建新闻列表容器
     */
    private LinearLayout createNewsListContainer() {
        LinearLayout newsContainer = new LinearLayout(getContext());
        newsContainer.setOrientation(LinearLayout.VERTICAL);
        newsContainer.setBackgroundColor(Color.WHITE);
        newsContainer.setPadding(dp(16), dp(16), dp(16), 0);
        
        // 列表标题
        TextView listTitle = new TextView(getContext());
        listTitle.setText("最新资讯");
        listTitle.setTextSize(18);
        listTitle.setTextColor(Color.parseColor("#333333"));
        listTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        listTitle.setPadding(0, 0, 0, dp(16));
        
        // 状态文本
        statusText = new TextView(getContext());
        statusText.setText("正在加载新闻...");
        statusText.setTextSize(14);
        statusText.setTextColor(Color.parseColor("#666666"));
        statusText.setPadding(0, 0, 0, dp(8));
        
        // RecyclerView
        newsRecyclerView = new RecyclerView(getContext());
        LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newsRecyclerView.setLayoutParams(recyclerParams);
        
        newsContainer.addView(listTitle);
        newsContainer.addView(statusText);
        newsContainer.addView(newsRecyclerView);
        
        return newsContainer;
    }
    
    /**
     * 创建错误布局
     */
    private LinearLayout createErrorLayout() {
        LinearLayout errorLayout = new LinearLayout(getContext());
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(android.view.Gravity.CENTER);
        errorLayout.setBackgroundColor(Color.WHITE);
        errorLayout.setPadding(dp(40), dp(80), dp(40), dp(40));
        
        TextView errorText = new TextView(getContext());
        errorText.setText("新闻页面加载失败\n请稍后重试");
        errorText.setTextSize(16);
        errorText.setTextColor(Color.parseColor("#EF4444"));
        errorText.setGravity(android.view.Gravity.CENTER);
        
        errorLayout.addView(errorText);
        return errorLayout;
    }
    
    /**
     * 初始化视图
     */
    private void initViews(View view) {
        Log.d(TAG, "视图初始化完成");
    }
    
    /**
     * 初始化轮播图
     */
    private void initCarousel() {
        try {
            carouselAdapter = new NewsCarouselAdapter(getContext());
            carouselViewPager.setAdapter(carouselAdapter);
            
            // 设置轮播图点击监听
            carouselAdapter.setOnItemClickListener(this::navigateToNewsDetail);
            
            // 设置自动轮播
            carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.d(TAG, "轮播切换到位置: " + position);
                }
            });
            
            Log.d(TAG, "轮播图初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "轮播图初始化失败", e);
        }
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        try {
            newsAdapter = new NewsAdapter(getContext());
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            newsRecyclerView.setAdapter(newsAdapter);
            
            // 设置点击监听
            newsAdapter.setOnItemClickListener(this::navigateToNewsDetail);
            
            Log.d(TAG, "RecyclerView 初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "RecyclerView 初始化失败", e);
            statusText.setText("列表初始化失败");
        }
    }
    
    /**
     * 设置下拉刷新
     */
    private void setupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Log.d(TAG, "用户下拉刷新");
                refreshNewsData();
            });
            
            // 设置刷新动画颜色
            swipeRefreshLayout.setColorSchemeColors(
                Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#FF9800")
            );
        }
    }
    
    /**
     * 刷新新闻数据
     */
    private void refreshNewsData() {
        if (isLoading) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        
        isLoading = true;
        statusText.setText("正在刷新新闻...");
        
        // 模拟网络请求延迟
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // 重新生成新闻数据
                List<NewsItem> generatedNews = NewsDataGenerator.generateNewsData();
                
                newsList.clear();
                newsList.addAll(generatedNews);
                
                // 更新轮播图数据（取前5条作为头条）
                featuredNewsList.clear();
                featuredNewsList.addAll(newsList.subList(0, Math.min(5, newsList.size())));
                
                // 更新UI
                updateNewsListData();
                updateCarouselData();
                
                statusText.setText("共 " + newsList.size() + " 条新闻（已更新）");
                
                Log.d(TAG, "新闻数据刷新完成，共" + newsList.size() + "条");
                
            } catch (Exception e) {
                Log.e(TAG, "新闻数据刷新失败", e);
                statusText.setText("刷新失败，请稍后重试");
            } finally {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
    
    /**
     * 加载新闻数据
     */
    private void loadNewsData() {
        if (isLoading) return;
        
        isLoading = true;
        statusText.setText("正在加载新闻...");
        
        // 设置下拉刷新
        setupSwipeRefresh();
        
        // 在后台线程生成数据
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // 使用数据生成器生成新闻数据
                List<NewsItem> generatedNews = NewsDataGenerator.generateNewsData();
                
                newsList.clear();
                newsList.addAll(generatedNews);
                
                // 准备头条新闻数据（前5条）
                featuredNewsList.clear();
                featuredNewsList.addAll(newsList.subList(0, Math.min(5, newsList.size())));
                
                // 更新UI
                updateNewsListData();
                updateCarouselData();
                statusText.setText("共 " + newsList.size() + " 条新闻");
                
                Log.d(TAG, "新闻数据加载完成，共" + newsList.size() + "条");
                
            } catch (Exception e) {
                Log.e(TAG, "新闻数据加载失败", e);
                statusText.setText("加载失败，请稍后重试");
            } finally {
                isLoading = false;
            }
        }, 1000);
    }
    
    /**
     * 更新新闻列表数据
     */
    private void updateNewsListData() {
        if (newsAdapter != null) {
            newsAdapter.setNewsList(newsList);
        }
    }
    
    /**
     * 更新轮播图数据
     */
    private void updateCarouselData() {
        if (carouselAdapter != null) {
            carouselAdapter.setFeaturedNews(featuredNewsList);
        }
    }
    
    /**
     * 跳转到新闻详情页面
     */
    private void navigateToNewsDetail(NewsItem newsItem) {
        try {
            Log.d(TAG, "点击新闻: " + newsItem.getTitle());
            
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra("news_title", newsItem.getTitle());
            intent.putExtra("news_source", newsItem.getSource());
            intent.putExtra("news_url", newsItem.getUrl());
            intent.putExtra("news_thumbnail", newsItem.getThumbnail());
            intent.putExtra("news_time", newsItem.getTime());
            intent.putExtra("news_category", newsItem.getCategory());
            
            startActivity(intent);
            
            // 更新状态显示
            if (statusText != null) {
                statusText.setText("已打开: " + newsItem.getTitle());
                
                // 2秒后恢复原状态
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (statusText != null) {
                        statusText.setText("共 " + newsList.size() + " 条新闻");
                    }
                }, 2000);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "跳转新闻详情失败", e);
            if (statusText != null) {
                statusText.setText("跳转失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * dp转px工具方法
     */
    private int dp(int dp) {
        if (getContext() == null) return dp;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "LocalNewsFragment onResume");
        
        // 如果没有数据，重新加载
        if (newsList.isEmpty() && !isLoading) {
            loadNewsData();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "LocalNewsFragment onDestroyView");
    }
}