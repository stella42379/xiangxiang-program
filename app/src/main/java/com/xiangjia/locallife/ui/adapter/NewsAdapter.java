package com.xiangjia.locallife.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻适配器 - 安全简化版本
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    
    private static final String TAG = "NewsAdapter";
    
    private Context context;
    private List<NewsItem> newsList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(NewsItem newsItem);
    }
    
    public NewsAdapter(Context context) {
        this.context = context;
        Log.d(TAG, "NewsAdapter 创建");
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setNewsList(List<NewsItem> news) {
        this.newsList = news != null ? news : new ArrayList<>();
        notifyDataSetChanged();
        Log.d(TAG, "设置新闻列表，共" + this.newsList.size() + "条");
    }
    
    public void addNews(List<NewsItem> news) {
        if (news != null) {
            int startPosition = this.newsList.size();
            this.newsList.addAll(news);
            notifyItemRangeInserted(startPosition, news.size());
        }
    }
    
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout itemView = createNewsItemView();
        return new NewsViewHolder(itemView);
    }
    
    /**
     * 创建新闻项目视图
     */
    private LinearLayout createNewsItemView() {
        LinearLayout itemLayout = new LinearLayout(context);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(dp(16), dp(12), dp(16), dp(12));
        itemLayout.setBackgroundColor(Color.WHITE);
        
        // 设置布局参数
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, dp(8));
        itemLayout.setLayoutParams(layoutParams);
        
        // 标题
        TextView titleView = new TextView(context);
        titleView.setTag("title");
        titleView.setTextSize(16);
        titleView.setTextColor(Color.parseColor("#333333"));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setPadding(0, 0, 0, dp(8));
        
        // 信息栏
        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView sourceView = new TextView(context);
        sourceView.setTag("source");
        sourceView.setTextSize(12);
        sourceView.setTextColor(Color.parseColor("#666666"));
        
        TextView timeView = new TextView(context);
        timeView.setTag("time");
        timeView.setTextSize(12);
        timeView.setTextColor(Color.parseColor("#999999"));
        timeView.setPadding(dp(16), 0, 0, 0);
        
        infoLayout.addView(sourceView);
        infoLayout.addView(timeView);
        
        itemLayout.addView(titleView);
        itemLayout.addView(infoLayout);
        
        // 分割线
        android.view.View divider = new android.view.View(context);
        divider.setBackgroundColor(Color.parseColor("#EEEEEE"));
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(1)
        );
        dividerParams.setMargins(0, dp(8), 0, 0);
        divider.setLayoutParams(dividerParams);
        itemLayout.addView(divider);
        
        return itemLayout;
    }
    
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if (position < newsList.size()) {
            NewsItem news = newsList.get(position);
            holder.bind(news);
        }
    }
    
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    
    class NewsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemLayout;
        private TextView titleView;
        private TextView sourceView;
        private TextView timeView;
        
        public NewsViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);
            this.itemLayout = itemView;
            
            // 查找子视图
            titleView = itemView.findViewWithTag("title");
            sourceView = itemView.findViewWithTag("source");
            timeView = itemView.findViewWithTag("time");
            
            // 设置点击监听
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < newsList.size()) {
                        onItemClickListener.onItemClick(newsList.get(position));
                    }
                }
            });
        }
        
        public void bind(NewsItem news) {
            try {
                if (news != null) {
                    if (titleView != null) {
                        titleView.setText(news.getTitle() != null ? news.getTitle() : "无标题");
                    }
                    
                    if (sourceView != null) {
                        sourceView.setText(news.getSource() != null ? news.getSource() : "未知来源");
                    }
                    
                    if (timeView != null) {
                        timeView.setText(news.getTime() != null ? news.getTime() : "刚刚");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "绑定新闻数据失败", e);
            }
        }
    }
    
    /**
     * dp转px工具方法
     */
    private int dp(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}