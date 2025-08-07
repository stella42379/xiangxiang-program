package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.NewsAdapter;

/**
 * 本地新闻Fragment
 * 显示本地新闻列表
 */
public class LocalNewsFragment extends Fragment {
    
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView newsRecyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private NewsAdapter newsAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_news, container, false);
        
        initViews(view);
        initRecyclerView();
        setupSwipeRefresh();
        loadNewsData();
        
        return view;
    }
    
    /**
     * 初始化视图
     */
    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        newsRecyclerView = view.findViewById(R.id.news_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyText = view.findViewById(R.id.empty_text);
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsAdapter);
        
        // 设置点击监听
        newsAdapter.setOnItemClickListener(newsItem -> {
            // 跳转到新闻详情页面
            navigateToNewsDetail(newsItem);
        });
    }
    
    /**
     * 设置下拉刷新
     */
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 刷新新闻数据
            refreshNewsData();
        });
    }
    
    /**
     * 加载新闻数据
     */
    private void loadNewsData() {
        showLoading(true);
        
        // TODO: 从网络或本地数据库加载新闻数据
        // 模拟加载数据
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                getActivity().runOnUiThread(() -> {
                    showLoading(false);
                    // 显示模拟数据
                    showNewsData();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 刷新新闻数据
     */
    private void refreshNewsData() {
        // TODO: 刷新新闻数据
        swipeRefreshLayout.setRefreshing(false);
    }
    
    /**
     * 显示新闻数据
     */
    private void showNewsData() {
        // TODO: 显示新闻数据
        if (newsAdapter.getItemCount() == 0) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
        }
    }
    
    /**
     * 显示加载状态
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            newsRecyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            newsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 显示空视图
     */
    private void showEmptyView(boolean show) {
        if (show) {
            emptyText.setVisibility(View.VISIBLE);
            newsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            newsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 跳转到新闻详情页面
     */
    private void navigateToNewsDetail(Object newsItem) {
        // TODO: 实现跳转到新闻详情页面
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 刷新数据
        if (newsAdapter.getItemCount() == 0) {
            loadNewsData();
        }
    }
}
