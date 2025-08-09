package com.xiangjia.locallife.ui.adapter;

import android.content.Context;
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
import com.xiangjia.locallife.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsCarouselAdapter extends RecyclerView.Adapter<NewsCarouselAdapter.CarouselViewHolder> {
    
    private Context context;
    private List<NewsItem> featuredNews = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(NewsItem newsItem);
    }
    
    public NewsCarouselAdapter(Context context) {
        this.context = context;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setFeaturedNews(List<NewsItem> news) {
        this.featuredNews = news;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_carousel, parent, false);
        return new CarouselViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        NewsItem news = featuredNews.get(position);
        holder.bind(news);
    }
    
    @Override
    public int getItemCount() {
        return featuredNews.size();
    }
    
    class CarouselViewHolder extends RecyclerView.ViewHolder {
        private ImageView carouselImage;
        private TextView carouselTitle;
        private TextView carouselSource;
        private View carouselOverlay;
        
        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselImage = itemView.findViewById(R.id.carousel_image);
            carouselTitle = itemView.findViewById(R.id.carousel_title);
            carouselSource = itemView.findViewById(R.id.carousel_source);
            carouselOverlay = itemView.findViewById(R.id.carousel_overlay);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(featuredNews.get(position));
                    }
                }
            });
        }
        
        public void bind(NewsItem news) {
            carouselTitle.setText(news.getTitle());
            carouselSource.setText(news.getSource());
            
            // 使用Glide加载图片
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.placeholder_news)
                    .error(R.drawable.placeholder_news);
            
            Glide.with(context)
                    .load(news.getThumbnail())
                    .apply(requestOptions)
                    .into(carouselImage);
        }
    }
}