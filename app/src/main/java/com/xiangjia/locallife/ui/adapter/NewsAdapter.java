package com.xiangjia.locallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xiangjia.locallife.R;
import com.xiangjia.locallife.network.NewsServiceManager.UnifiedNewsItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 🔥 完全兼容版NewsAdapter - 匹配LocalNewsFragment的UnifiedNewsItem
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    
    private List<UnifiedNewsItem> newsList;
    private OnNewsClickListener clickListener;
    private Context context;
    
    // 时间格式化
    private SimpleDateFormat inputFormat;
    private SimpleDateFormat outputFormat;
    
    /**
     * 🔥 关键：接口定义必须与LocalNewsFragment匹配
     */
    public interface OnNewsClickListener {
        void onNewsClick(UnifiedNewsItem newsItem);
        void onShareClick(UnifiedNewsItem newsItem);
    }
    
    public NewsAdapter(List<UnifiedNewsItem> newsList, OnNewsClickListener clickListener) {
        this.newsList = newsList;
        this.clickListener = clickListener;
        
        // 初始化时间格式化器
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        outputFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney")); // 澳洲时区
    }
    
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        UnifiedNewsItem newsItem = newsList.get(position);
        
        // 设置标题
        holder.titleText.setText(newsItem.getTitle() != null ? newsItem.getTitle() : "无标题");
        
        // 设置描述
        if (!TextUtils.isEmpty(newsItem.getDescription())) {
            holder.descriptionText.setText(newsItem.getDescription());
            holder.descriptionText.setVisibility(View.VISIBLE);
        } else {
            holder.descriptionText.setVisibility(View.GONE);
        }
        
        // 设置新闻源
        holder.sourceText.setText(newsItem.getSource() != null ? newsItem.getSource() : "未知来源");
        
        // 设置时间
        holder.timeText.setText(formatTime(newsItem.getPublishedAt()));
        
        // 加载图片
        loadNewsImage(holder.newsImage, newsItem.getImageUrl());
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onNewsClick(newsItem);
            }
        });
        
        // 分享按钮点击事件（如果有分享按钮的话）
        if (holder.shareButton != null) {
            holder.shareButton.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onShareClick(newsItem);
                }
            });
        }
    }
    
    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }
    
    /**
     * 格式化时间显示
     */
    private String formatTime(String publishedAt) {
        if (TextUtils.isEmpty(publishedAt)) {
            return "未知时间";
        }
        
        try {
            // 尝试解析ISO 8601格式的时间
            Date date = inputFormat.parse(publishedAt);
            if (date != null) {
                // 计算时间差
                long timeDiff = System.currentTimeMillis() - date.getTime();
                long minutes = timeDiff / (1000 * 60);
                long hours = timeDiff / (1000 * 60 * 60);
                long days = timeDiff / (1000 * 60 * 60 * 24);
                
                // 返回相对时间
                if (minutes < 60) {
                    return minutes <= 1 ? "刚刚" : minutes + "分钟前";
                } else if (hours < 24) {
                    return hours + "小时前";
                } else if (days < 7) {
                    return days + "天前";
                } else {
                    // 超过一周显示具体日期
                    return outputFormat.format(date);
                }
            }
        } catch (ParseException e) {
            // 解析失败，尝试其他格式或直接显示原始时间
            if (publishedAt.length() >= 10) {
                return publishedAt.substring(0, 10); // 显示日期部分
            }
        }
        
        return "时间未知";
    }
    
    /**
     * 🔥 修复：加载新闻图片，处理空URL和错误
     */
    private void loadNewsImage(ImageView imageView, String imageUrl) {
        if (context == null || imageView == null) {
            return;
        }
        
        // 🔥 调试：打印图片加载状态
        android.util.Log.d("NewsAdapter", "加载图片: " + (TextUtils.isEmpty(imageUrl) ? "URL为空" : imageUrl));
        
        if (!TextUtils.isEmpty(imageUrl)) {
            // 检查是否是有效的图片URL
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // 显示图片View
                imageView.setVisibility(View.VISIBLE);
                
                // 使用Glide加载图片，带圆角和错误处理
                Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                        .transform(new RoundedCorners(16))
                        .placeholder(android.R.drawable.ic_menu_gallery) // 系统占位图
                        .error(android.R.drawable.ic_menu_report_image))  // 系统错误图
                    .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e, 
                                                  Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, 
                                                  boolean isFirstResource) {
                            android.util.Log.e("NewsAdapter", "图片加载失败: " + imageUrl, e);
                            return false;
                        }
                        
                        @Override
                        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, 
                                                     com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, 
                                                     com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            android.util.Log.d("NewsAdapter", "图片加载成功: " + imageUrl);
                            return false;
                        }
                    })
                    .into(imageView);
            } else {
                // URL格式不正确，隐藏图片
                android.util.Log.w("NewsAdapter", "无效的图片URL: " + imageUrl);
                imageView.setVisibility(View.GONE);
            }
        } else {
            // 没有图片URL，隐藏图片View节省空间
            android.util.Log.d("NewsAdapter", "图片URL为空，隐藏图片");
            imageView.setVisibility(View.GONE);
        }
    }
    
    /**
     * ViewHolder类
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView descriptionText;
        public TextView sourceText;
        public TextView timeText;
        public ImageView newsImage;
        public ImageView shareButton; // 可选的分享按钮
        
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // 🔥 关键：这些ID必须与你的item_news.xml布局匹配
            titleText = itemView.findViewById(R.id.news_title);
            descriptionText = itemView.findViewById(R.id.news_description);
            sourceText = itemView.findViewById(R.id.news_source);
            timeText = itemView.findViewById(R.id.news_time);
            newsImage = itemView.findViewById(R.id.news_image);
            
            // 可选的分享按钮（如果你的布局里有的话）
            try {
                shareButton = itemView.findViewById(R.id.btn_share);
            } catch (Exception e) {
                // 如果没有分享按钮就忽略
                shareButton = null;
            }
            
            // 如果找不到某些视图，创建一个隐藏的TextView防止空指针
            if (descriptionText == null) {
                descriptionText = new TextView(itemView.getContext());
                descriptionText.setVisibility(View.GONE);
            }
        }
    }
}