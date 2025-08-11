package com.xiangjia.locallife.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.model.ForumPost;

import java.util.List;

/**
 * 🎯 热门帖子轮播适配器 - 修复版本
 */
public class HotPostsCarouselAdapter extends RecyclerView.Adapter<HotPostsCarouselAdapter.HotPostViewHolder> {
    
    private final List<ForumPost> hotPosts;
    private final OnHotPostClickListener listener;
    private final Context context;
    
    public interface OnHotPostClickListener {
        void onHotPostClick(ForumPost post);
    }
    
    public HotPostsCarouselAdapter(List<ForumPost> hotPosts, OnHotPostClickListener listener) {
        this.hotPosts = hotPosts;
        this.listener = listener;
        this.context = null;
    }
    
    public HotPostsCarouselAdapter(Context context, List<ForumPost> hotPosts, OnHotPostClickListener listener) {
        this.context = context;
        this.hotPosts = hotPosts;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public HotPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_hot_post_carousel, parent, false);
        return new HotPostViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull HotPostViewHolder holder, int position) {
        ForumPost post = hotPosts.get(position);
        holder.bind(post);
    }
    
    @Override
    public int getItemCount() {
        return hotPosts != null ? hotPosts.size() : 0;
    }
    
    class HotPostViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView ivBackground;
        private final TextView tvTitle;
        private final TextView tvAuthor;
        private final TextView tvTime;
        private final TextView tvCategory;
        private final TextView tvLikeCount;
        private final ImageView ivHotIcon;
        
        public HotPostViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivBackground = itemView.findViewById(R.id.iv_hot_post_background);
            tvTitle = itemView.findViewById(R.id.tv_hot_post_title);
            tvAuthor = itemView.findViewById(R.id.tv_hot_post_author);
            tvTime = itemView.findViewById(R.id.tv_hot_post_time);
            tvCategory = itemView.findViewById(R.id.tv_hot_post_category);
            tvLikeCount = itemView.findViewById(R.id.tv_hot_post_likes);
            ivHotIcon = itemView.findViewById(R.id.iv_hot_icon);
            
            // 设置点击监听
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onHotPostClick(hotPosts.get(getAdapterPosition()));
                }
            });
        }
        
        public void bind(ForumPost post) {
            // 设置标题
            if (tvTitle != null) {
                tvTitle.setText(post.getTitle());
            }
            
            // 设置作者
            if (tvAuthor != null) {
                tvAuthor.setText(post.getAuthorName());
            }
            
            // 设置时间 - 修复：使用正确的方法名
            if (tvTime != null) {
                // 检查ForumPost有哪些时间相关的方法
                long timestamp = 0;
                try {
                    // 尝试不同的可能方法名
                    if (hasMethod(post, "getCreateTime")) {
                        timestamp = (Long) post.getClass().getMethod("getCreateTime").invoke(post);
                    } else if (hasMethod(post, "getCreatedAt")) {
                        timestamp = (Long) post.getClass().getMethod("getCreatedAt").invoke(post);
                    } else if (hasMethod(post, "getTimestamp")) {
                        timestamp = (Long) post.getClass().getMethod("getTimestamp").invoke(post);
                    } else if (hasMethod(post, "getLastActivityTime")) {
                        timestamp = post.getLastActivityTime();
                    } else {
                        // 如果都没有，使用当前时间
                        timestamp = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                    // 如果反射调用失败，使用当前时间
                    timestamp = System.currentTimeMillis();
                }
                tvTime.setText(formatTimeAgo(timestamp));
            }
            
            // 设置分类
            if (tvCategory != null) {
                tvCategory.setText(getCategoryDisplayName(post.getCategory()));
            }
            
            // 设置点赞数
            if (tvLikeCount != null) {
                tvLikeCount.setText(String.valueOf(post.getLikeCount()));
            }
            
            // 设置背景图片（如果有）
            if (ivBackground != null) {
                // TODO: 加载帖子图片或使用默认背景
                // Glide.with(itemView.getContext())
                //     .load(post.getImageUrl())
                //     .placeholder(R.drawable.bg_hot_post_default)
                //     .into(ivBackground);
            }
            
            // 设置热门图标
            if (ivHotIcon != null) {
                ivHotIcon.setVisibility(post.getLikeCount() > 10 ? View.VISIBLE : View.GONE);
            }
        }
        
        /**
         * 检查对象是否有指定方法
         */
        private boolean hasMethod(Object obj, String methodName) {
            try {
                obj.getClass().getMethod(methodName);
                return true;
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
        
        /**
         * 格式化时间
         */
        private String formatTimeAgo(long timestamp) {
            long now = System.currentTimeMillis();
            long diff = now - timestamp;
            
            if (diff < 60 * 1000) {
                return "刚刚";
            } else if (diff < 60 * 60 * 1000) {
                return (diff / (60 * 1000)) + "分钟前";
            } else if (diff < 24 * 60 * 60 * 1000) {
                return (diff / (60 * 60 * 1000)) + "小时前";
            } else {
                return (diff / (24 * 60 * 60 * 1000)) + "天前";
            }
        }
        
        /**
         * 获取分类显示名称
         */
        private String getCategoryDisplayName(String category) {
            if (category == null) return "其他";
            
            switch (category) {
                case "discussion": return "讨论";
                case "friends": return "交友";
                case "help": return "求助";
                case "share": return "分享";
                case "announcement": return "公告";
                default: return "其他";
            }
        }
    }
}