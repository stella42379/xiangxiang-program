package com.xiangjia.locallife.ui.fragment;

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

import com.xiangjia.locallife.model.NewsItem;
import com.xiangjia.locallife.ui.adapter.NewsAdapter;
import com.xiangjia.locallife.utils.NewsDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地新闻Fragment - 安全简化版本
 */
public class LocalNewsFragment extends Fragment {
    
    private static final String TAG = "LocalNewsFragment";
    
    private RecyclerView newsRecyclerView;
    private TextView statusText;
    private NewsAdapter newsAdapter;
    
    private List<NewsItem> newsList = new ArrayList<>();
    private boolean isLoading = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "LocalNewsFragment onCreateView");
        
        try {
            // 创建安全的布局，不依赖复杂的XML
            LinearLayout mainLayout = createSafeLayout();
            
            initViews(mainLayout);
            initRecyclerView();
            loadNewsData();
            
            Log.d(TAG, "LocalNewsFragment 创建成功");
            return mainLayout;
            
        } catch (Exception e) {
            Log.e(TAG, "LocalNewsFragment 创建失败", e);
            return createErrorView();
        }
    }
    
    /**
     * 创建安全的布局
     */
    private LinearLayout createSafeLayout() {
        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
        mainLayout.setPadding(dp(16), dp(20), dp(16), dp(16));
        
        // 标题
        TextView titleView = new TextView(getContext());
        titleView.setText("今日时讯");
        titleView.setTextSize(24);
        titleView.setTextColor(Color.parseColor("#2d8cf0"));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setPadding(0, 0, 0, dp(16));
        
        // 状态文本
        statusText = new TextView(getContext());
        statusText.setText("正在加载新闻...");
        statusText.setTextSize(14);
        statusText.setTextColor(Color.parseColor("#666666"));
        statusText.setPadding(0, 0, 0, dp(16));
        
        // RecyclerView
        newsRecyclerView = new RecyclerView(getContext());
        LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        newsRecyclerView.setLayoutParams(recyclerParams);
        newsRecyclerView.setBackgroundColor(Color.WHITE);
        
        mainLayout.addView(titleView);
        mainLayout.addView(statusText);
        mainLayout.addView(newsRecyclerView);
        
        return mainLayout;
    }
    
    /**
     * 创建错误视图
     */
    private View createErrorView() {
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
        // 视图已在createSafeLayout中初始化
        Log.d(TAG, "视图初始化完成");
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
     * 加载新闻数据
     */
    private void loadNewsData() {
        if (isLoading) return;
        
        isLoading = true;
        statusText.setText("正在加载新闻...");
        
        // 在后台线程生成数据
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // 使用数据生成器生成新闻数据
                List<NewsItem> generatedNews = NewsDataGenerator.generateNewsData();
                
                newsList.clear();
                newsList.addAll(generatedNews);
                
                // 更新UI
                updateNewsListData();
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
     * 跳转到新闻详情页面
     */
    private void navigateToNewsDetail(NewsItem newsItem) {
        try {
            Log.d(TAG, "点击新闻: " + newsItem.getTitle());
            // TODO: 实现跳转到新闻详情页面
            if (statusText != null) {
                statusText.setText("已点击: " + newsItem.getTitle());
            }
        } catch (Exception e) {
            Log.e(TAG, "跳转新闻详情失败", e);
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