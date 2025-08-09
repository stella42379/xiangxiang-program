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
 * 新闻适配器 - 增强版本（支持详情页跳转）
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
        Log.d(TAG, "设置点击监听器");
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
            Log.d(TAG, "添加" + news.size() + "条新闻");
        }
    }
    
    public void clearNews() {
        int itemCount = this.newsList.size();
        this.newsList.clear();
        notifyItemRangeRemoved(0, itemCount);
        Log.d(TAG, "清空新闻列表");
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
        
        // 添加点击效果
        itemLayout.setBackground(createSelectableBackground());
        
        // 标题
        TextView titleView = new TextView(context);
        titleView.setTag("title");
        titleView.setTextSize(16);
        titleView.setTextColor(Color.parseColor("#333333"));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setPadding(0, 0, 0, dp(8));
        titleView.setMaxLines(2);
        titleView.setEllipsize(android.text.TextUtils.TruncateAt.END);
        
        // 信息栏
        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        infoLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // 来源图标和文本容器
        LinearLayout sourceContainer = new LinearLayout(context);
        sourceContainer.setOrientation(LinearLayout.HORIZONTAL);
        sourceContainer.setGravity(android.view.Gravity.CENTER_VERTICAL);
        sourceContainer.setLayoutParams(new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));
        
        // 来源图标
        TextView sourceIcon = new TextView(context);
        sourceIcon.setText("📰");
        sourceIcon.setTextSize(12);
        sourceIcon.setPadding(0, 0, dp(6), 0);
        
        TextView sourceView = new TextView(context);
        sourceView.setTag("source");
        sourceView.setTextSize(12);
        sourceView.setTextColor(Color.parseColor("#666666"));
        
        sourceContainer.addView(sourceIcon);
        sourceContainer.addView(sourceView);
        
        TextView timeView = new TextView(context);
        timeView.setTag("time");
        timeView.setTextSize(12);
        timeView.setTextColor(Color.parseColor("#999999"));
        
        infoLayout.addView(sourceContainer);
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
    
    /**
     * 创建可选择背景（点击效果）
     */
    private android.graphics.drawable.Drawable createSelectableBackground() {
        android.graphics.drawable.StateListDrawable stateListDrawable = new android.graphics.drawable.StateListDrawable();
        
        // 按下状态
        android.graphics.drawable.GradientDrawable pressedDrawable = new android.graphics.drawable.GradientDrawable();
        pressedDrawable.setColor(Color.parseColor("#F5F5F5"));
        pressedDrawable.setCornerRadius(dp(8));
        
        // 正常状态
        android.graphics.drawable.GradientDrawable normalDrawable = new android.graphics.drawable.GradientDrawable();
        normalDrawable.setColor(Color.WHITE);
        normalDrawable.setCornerRadius(dp(8));
        
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        
        return stateListDrawable;
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
                        NewsItem clickedNews = newsList.get(position);
                        Log.d(TAG, "新闻被点击: " + clickedNews.getTitle());
                        onItemClickListener.onItemClick(clickedNews);
                    }
                }
            });
            
            // 设置长按监听
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && position < newsList.size()) {
                    NewsItem longClickedNews = newsList.get(position);
                    Log.d(TAG, "新闻被长按: " + longClickedNews.getTitle());
                    showNewsOptions(longClickedNews);
                    return true;
                }
                return false;
            });
        }
        
        public void bind(NewsItem news) {
            try {
                if (news != null) {
                    if (titleView != null) {
                        String title = news.getTitle() != null ? news.getTitle() : "无标题";
                        titleView.setText(title);
                    }
                    
                    if (sourceView != null) {
                        String source = news.getSource() != null ? news.getSource() : "未知来源";
                        sourceView.setText(source);
                    }
                    
                    if (timeView != null) {
                        String time = news.getTime() != null ? news.getTime() : "刚刚";
                        timeView.setText(time);
                    }
                    
                    // 根据新闻分类设置不同的视觉效果
                    updateItemAppearance(news);
                }
            } catch (Exception e) {
                Log.e(TAG, "绑定新闻数据失败", e);
            }
        }
        
        /**
         * 根据新闻类型更新外观
         */
        private void updateItemAppearance(NewsItem news) {
            try {
                if (news.getCategory() != null) {
                    // 根据分类设置不同颜色的左边框
                    String category = news.getCategory();
                    int accentColor = getAccentColorByCategory(category);
                    
                    // 创建带有左边框的背景
                    android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
                    drawable.setColor(Color.WHITE);
                    drawable.setCornerRadius(dp(8));
                    drawable.setStroke(dp(3), accentColor, 0, 0);
                    
                    itemLayout.setBackground(drawable);
                }
                
                // 如果是重要新闻，标题加粗
                if (news.getTitle() != null && titleView != null) {
                    if (news.getTitle().contains("重要") || news.getTitle().contains("紧急") || news.getTitle().contains("最新")) {
                        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
                        titleView.setTextColor(Color.parseColor("#D32F2F"));
                    }
                }
                
            } catch (Exception e) {
                Log.w(TAG, "更新外观失败", e);
            }
        }
        
        /**
         * 根据分类获取强调色
         */
        private int getAccentColorByCategory(String category) {
            switch (category) {
                case "technology":
                    return Color.parseColor("#2196F3"); // 蓝色
                case "traffic":
                    return Color.parseColor("#FF9800"); // 橙色
                case "environment":
                    return Color.parseColor("#4CAF50"); // 绿色
                case "medical":
                    return Color.parseColor("#F44336"); // 红色
                case "safety":
                    return Color.parseColor("#9C27B0"); // 紫色
                default:
                    return Color.parseColor("#607D8B"); // 默认灰蓝色
            }
        }
        
        /**
         * 显示新闻操作选项
         */
        private void showNewsOptions(NewsItem news) {
            // 这里可以实现长按显示操作菜单的功能
            // 比如：收藏、分享、举报等
            Log.d(TAG, "显示新闻操作选项: " + news.getTitle());
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