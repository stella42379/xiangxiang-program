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

import java.util.List;

/**
 * 🎯 新闻轮播适配器
 */
public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    
    private List<UnifiedNewsItem> newsItems;
    private OnCarouselClickListener clickListener;
    
    public interface OnCarouselClickListener {
        void onCarouselClick(UnifiedNewsItem newsItem);
    }
    
    public CarouselAdapter(List<UnifiedNewsItem> newsItems, OnCarouselClickListener clickListener) {
        this.newsItems = newsItems;
        this.clickListener = clickListener;
    }
    
    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_carousel_news, parent, false);
        return new CarouselViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        UnifiedNewsItem newsItem = newsItems.get(position);
        Context context = holder.itemView.getContext();
        
        // 设置标题
        holder.titleText.setText(newsItem.getTitle() != null ? newsItem.getTitle() : "无标题");
        
        // 设置新闻源
        holder.sourceText.setText(newsItem.getSource() != null ? newsItem.getSource() : "未知来源");
        
        // 加载背景图片
        if (!TextUtils.isEmpty(newsItem.getImageUrl())) {
            Glide.with(context)
                .load(newsItem.getImageUrl())
                .apply(new RequestOptions()
                    .transform(new RoundedCorners(16))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image))
                .into(holder.backgroundImage);
        } else {
            holder.backgroundImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCarouselClick(newsItem);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return newsItems != null ? newsItems.size() : 0;
    }
    
    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        public ImageView backgroundImage;
        public TextView titleText;
        public TextView sourceText;
        
        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImage = itemView.findViewById(R.id.carousel_background);
            titleText = itemView.findViewById(R.id.carousel_title);
            sourceText = itemView.findViewById(R.id.carousel_source);
        }
    }
}