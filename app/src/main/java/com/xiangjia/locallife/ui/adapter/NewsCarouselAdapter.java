package com.xiangjia.locallife.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xiangjia.locallife.R;
import com.xiangjia.locallife.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻轮播适配器 - 完整功能版本
 */
public class NewsCarouselAdapter extends RecyclerView.Adapter<NewsCarouselAdapter.CarouselViewHolder> {
    
    private static final String TAG = "NewsCarouselAdapter";
    
    private Context context;
    private List<NewsItem> featuredNews = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(NewsItem newsItem);
    }
    
    public NewsCarouselAdapter(Context context) {
        this.context = context;
        Log.d(TAG, "NewsCarouselAdapter 创建");
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setFeaturedNews(List<NewsItem> news) {
        this.featuredNews = news != null ? news : new ArrayList<>();
        notifyDataSetChanged();
        Log.d(TAG, "设置轮播新闻，共" + this.featuredNews.size() + "条");
    }
    
    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 检查是否有XML布局文件
        View view = null;
        try {
            view = LayoutInflater.from(context).inflate(R.layout.item_news_carousel, parent, false);
        } catch (Exception e) {
            Log.w(TAG, "无法加载XML布局，使用代码创建", e);
            view = createCarouselItemView(parent);
        }
        return new CarouselViewHolder(view);
    }
    
    /**
     * 代码创建轮播项视图（备用方案）
     */
    private View createCarouselItemView(ViewGroup parent) {
        // 主容器
        CardView cardView = new CardView(context);
        RecyclerView.LayoutParams cardParams = new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dp(12));
        cardView.setCardElevation(0);
        
        // 内容容器
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new CardView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        
        // 背景图片
        ImageView carouselImage = new ImageView(context);
        carouselImage.setId(View.generateViewId());
        carouselImage.setLayoutParams(new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        carouselImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        carouselImage.setTag("carousel_image");
        
        // 渐变遮罩
        View overlay = new View(context);
        overlay.setId(View.generateViewId());
        overlay.setLayoutParams(new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        overlay.setBackground(createGradientDrawable());
        overlay.setTag("carousel_overlay");
        
        // 内容区域
        LinearLayout contentLayout = new LinearLayout(context);
        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        contentParams.gravity = android.view.Gravity.BOTTOM;
        contentLayout.setLayoutParams(contentParams);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(dp(16), dp(16), dp(16), dp(16));
        
        // 标题
        TextView titleView = new TextView(context);
        titleView.setId(View.generateViewId());
        titleView.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        titleView.setText("新闻标题");
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setMaxLines(2);
        titleView.setEllipsize(android.text.TextUtils.TruncateAt.END);
        titleView.setTag("carousel_title");
        
        // 底部信息布局
        LinearLayout metaLayout = new LinearLayout(context);
        LinearLayout.LayoutParams metaParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        metaParams.topMargin = dp(4);
        metaLayout.setLayoutParams(metaParams);
        metaLayout.setOrientation(LinearLayout.HORIZONTAL);
        metaLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // 来源布局
        LinearLayout sourceLayout = new LinearLayout(context);
        sourceLayout.setLayoutParams(new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));
        sourceLayout.setOrientation(LinearLayout.HORIZONTAL);
        sourceLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // 来源图标
        TextView sourceIcon = new TextView(context);
        sourceIcon.setText("📰");
        sourceIcon.setTextSize(10);
        sourceIcon.setPadding(0, 0, dp(4), 0);
        
        // 来源文本
        TextView sourceView = new TextView(context);
        sourceView.setId(View.generateViewId());
        sourceView.setText("新闻来源");
        sourceView.setTextColor(Color.WHITE);
        sourceView.setTextSize(12);
        sourceView.setAlpha(0.9f);
        sourceView.setTag("carousel_source");
        
        sourceLayout.addView(sourceIcon);
        sourceLayout.addView(sourceView);
        
        // "Read more"文本
        TextView readMoreView = new TextView(context);
        readMoreView.setText("Read more ›");
        readMoreView.setTextColor(Color.WHITE);
        readMoreView.setTextSize(12);
        readMoreView.setAlpha(0.9f);
        
        metaLayout.addView(sourceLayout);
        metaLayout.addView(readMoreView);
        
        contentLayout.addView(titleView);
        contentLayout.addView(metaLayout);
        
        frameLayout.addView(carouselImage);
        frameLayout.addView(overlay);
        frameLayout.addView(contentLayout);
        cardView.addView(frameLayout);
        
        return cardView;
    }
    
    /**
     * 创建渐变背景
     */
    private android.graphics.drawable.GradientDrawable createGradientDrawable() {
        android.graphics.drawable.GradientDrawable gradient = new android.graphics.drawable.GradientDrawable(
            android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{
                Color.parseColor("#00000000"),
                Color.parseColor("#B3000000")
            }
        );
        return gradient;
    }
    
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        if (position < featuredNews.size()) {
            NewsItem news = featuredNews.get(position);
            holder.bind(news);
        }
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
            
            // 尝试通过ID查找视图
            try {
                carouselImage = itemView.findViewById(R.id.carousel_image);
                carouselTitle = itemView.findViewById(R.id.carousel_title);
                carouselSource = itemView.findViewById(R.id.carousel_source);
                carouselOverlay = itemView.findViewById(R.id.carousel_overlay);
            } catch (Exception e) {
                Log.w(TAG, "通过ID查找视图失败，使用Tag查找", e);
            }
            
            // 如果通过ID查找失败，使用Tag查找
            if (carouselImage == null) {
                carouselImage = itemView.findViewWithTag("carousel_image");
            }
            if (carouselTitle == null) {
                carouselTitle = itemView.findViewWithTag("carousel_title");
            }
            if (carouselSource == null) {
                carouselSource = itemView.findViewWithTag("carousel_source");
            }
            if (carouselOverlay == null) {
                carouselOverlay = itemView.findViewWithTag("carousel_overlay");
            }
            
            // 设置点击监听
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < featuredNews.size()) {
                        onItemClickListener.onItemClick(featuredNews.get(position));
                    }
                }
            });
        }
        
        public void bind(NewsItem news) {
            try {
                if (news != null) {
                    // 设置标题
                    if (carouselTitle != null) {
                        carouselTitle.setText(news.getTitle() != null ? news.getTitle() : "无标题");
                    }
                    
                    // 设置来源
                    if (carouselSource != null) {
                        carouselSource.setText(news.getSource() != null ? news.getSource() : "未知来源");
                    }
                    
                    // 加载图片
                    if (carouselImage != null && news.getThumbnail() != null) {
                        try {
                            RequestOptions requestOptions = new RequestOptions()
                                    .transform(new RoundedCorners(dp(12)))
                                    .placeholder(generatePlaceholderImage(news.getTitle()))
                                    .error(generatePlaceholderImage(news.getTitle()));
                            
                            Glide.with(context)
                                    .load(news.getThumbnail())
                                    .apply(requestOptions)
                                    .into(carouselImage);
                        } catch (Exception e) {
                            Log.w(TAG, "图片加载失败，使用占位图", e);
                            carouselImage.setImageDrawable(generatePlaceholderDrawable(news.getTitle()));
                        }
                    } else if (carouselImage != null) {
                        carouselImage.setImageDrawable(generatePlaceholderDrawable(news.getTitle()));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "绑定轮播数据失败", e);
            }
        }
    }
    
    /**
     * 生成占位图片资源ID
     */
    private int generatePlaceholderImage(String title) {
        // 尝试使用预定义的占位图
        try {
            return R.drawable.placeholder_news;
        } catch (Exception e) {
            return android.R.drawable.ic_menu_gallery;
        }
    }
    
    /**
     * 生成占位图片Drawable
     */
    private android.graphics.drawable.Drawable generatePlaceholderDrawable(String title) {
        // 创建一个简单的颜色占位图
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        
        // 根据标题生成不同颜色
        int colorIndex = title != null ? Math.abs(title.hashCode()) % 6 : 0;
        String[] colors = {
            "#4CAF50", "#2196F3", "#FF9800", "#9C27B0", "#F44336", "#607D8B"
        };
        
        drawable.setColor(Color.parseColor(colors[colorIndex]));
        drawable.setCornerRadius(dp(12));
        
        return drawable;
    }
    
    /**
     * dp转px工具方法
     */
    private int dp(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}