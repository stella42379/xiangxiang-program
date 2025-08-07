package com.xiangjia.locallife.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表适配器
 * 用于显示新闻列表
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    
    private List<NewsItem> newsList;
    private OnItemClickListener onItemClickListener;
    
    public NewsAdapter() {
        this.newsList = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem news = newsList.get(position);
        holder.bind(news);
    }
    
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    
    /**
     * 设置数据
     */
    public void setData(List<NewsItem> newsList) {
        this.newsList.clear();
        this.newsList.addAll(newsList);
        notifyDataSetChanged();
    }
    
    /**
     * 添加数据
     */
    public void addData(NewsItem news) {
        this.newsList.add(news);
        notifyItemInserted(this.newsList.size() - 1);
    }
    
    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    /**
     * 点击监听器接口
     */
    public interface OnItemClickListener {
        void onItemClick(NewsItem news);
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView newsImage;
        private TextView newsTitle;
        private TextView newsSummary;
        private TextView newsSource;
        private TextView newsTime;
        private TextView newsCategory;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsSummary = itemView.findViewById(R.id.news_summary);
            newsSource = itemView.findViewById(R.id.news_source);
            newsTime = itemView.findViewById(R.id.news_time);
            newsCategory = itemView.findViewById(R.id.news_category);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(newsList.get(position));
                    }
                }
            });
        }
        
        public void bind(NewsItem news) {
            newsTitle.setText(news.getTitle());
            newsSummary.setText(news.getSummary());
            newsSource.setText(news.getSource());
            newsTime.setText(formatTime(news.getPublishTime()));
            newsCategory.setText(news.getCategory());
            
            // TODO: 加载新闻图片
            loadNewsImage(news.getImageUrl());
        }
        
        /**
         * 加载新闻图片
         */
        private void loadNewsImage(String imageUrl) {
            // TODO: 使用图片加载库加载图片
            // 例如：Glide, Picasso, Coil等
        }
        
        /**
         * 格式化时间
         */
        private String formatTime(String publishTime) {
            // TODO: 格式化时间显示
            return publishTime;
        }
    }
}
