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
import com.xiangjia.locallife.util.DateUtils;

import java.util.List;

/**
 * 论坛帖子列表适配器
 * 用于显示帖子列表
 */
public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.PostViewHolder> {
    
    private Context context;
    private List<ForumPost> postList;
    private OnPostClickListener onPostClickListener;
    
    public ForumPostAdapter(Context context, List<ForumPost> postList) {
        this.context = context;
        this.postList = postList;
    }
    
    /**
     * 帖子点击监听接口
     */
    public interface OnPostClickListener {
        void onPostClick(ForumPost post);
        void onLikeClick(ForumPost post);
        void onUserClick(ForumPost post);
    }
    
    public void setOnPostClickListener(OnPostClickListener listener) {
        this.onPostClickListener = listener;
    }
    
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post, parent, false);
        return new PostViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = postList.get(position);
        holder.bind(post);
    }
    
    @Override
    public int getItemCount() {
        return postList.size();
    }
    
    /**
     * 帖子ViewHolder
     */
    class PostViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivAvatar;
        private TextView tvAuthorName;
        private TextView tvPostTime;
        private ImageView ivPinned;
        private TextView tvCategory;
        private TextView tvTitle;
        private TextView tvContent;
        private ImageView ivPostImage;
        private TextView tvLikeCount;
        private TextView tvReplyCount;
        private TextView tvViewCount;
        private ImageView ivLike;
        private View layoutLike;
        private View layoutUser;
        
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvPostTime = itemView.findViewById(R.id.tv_post_time);
            ivPinned = itemView.findViewById(R.id.iv_pinned);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivPostImage = itemView.findViewById(R.id.iv_post_image);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvReplyCount = itemView.findViewById(R.id.tv_reply_count);
            tvViewCount = itemView.findViewById(R.id.tv_view_count);
            ivLike = itemView.findViewById(R.id.iv_like);
            layoutLike = itemView.findViewById(R.id.layout_like);
            layoutUser = itemView.findViewById(R.id.layout_user);
            
            // 设置点击监听
            itemView.setOnClickListener(v -> {
                if (onPostClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onPostClickListener.onPostClick(postList.get(position));
                    }
                }
            });
            
            layoutLike.setOnClickListener(v -> {
                if (onPostClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onPostClickListener.onLikeClick(postList.get(position));
                    }
                }
            });
            
            layoutUser.setOnClickListener(v -> {
                if (onPostClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onPostClickListener.onUserClick(postList.get(position));
                    }
                }
            });
        }
        
        public void bind(ForumPost post) {
            // 设置作者信息
            tvAuthorName.setText(post.getAuthorName());
            tvPostTime.setText(DateUtils.formatRelativeTime(post.getTimestamp()));
            
            // 设置头像 (简化处理，使用首字母)
            setAvatarText(post.getAuthorName());
            
            // 设置置顶标识
            ivPinned.setVisibility(post.isPinned() ? View.VISIBLE : View.GONE);
            
            // 设置分类
            tvCategory.setText(getCategoryDisplayName(post.getCategory()));
            setCategoryBackground(post.getCategory());
            
            // 设置帖子内容
            tvTitle.setText(post.getTitle());
            tvContent.setText(post.getContent());
            
            // 处理内容长度
            if (post.getContent().length() > 150) {
                tvContent.setText(post.getContent().substring(0, 150) + "...");
            }
            
            // 设置帖子图片
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                ivPostImage.setVisibility(View.VISIBLE);
                // TODO: 使用图片加载库加载图片
                // Glide.with(context).load(post.getImageUrl()).into(ivPostImage);
            } else {
                ivPostImage.setVisibility(View.GONE);
            }
            
            // 设置统计数据
            tvLikeCount.setText(String.valueOf(post.getLikeCount()));
            tvReplyCount.setText(String.valueOf(post.getReplyCount()));
            tvViewCount.setText(String.valueOf(post.getViewCount()));
            
            // 设置点赞状态
            // TODO: 根据用户是否已点赞设置图标状态
            // ivLike.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        }
        
        /**
         * 设置头像文字 (使用用户名首字母)
         */
        private void setAvatarText(String authorName) {
            if (authorName != null && !authorName.isEmpty()) {
                String initial = authorName.substring(0, 1).toUpperCase();
                // 这里应该设置到一个TextView上，简化处理
                // tvAvatarText.setText(initial);
            }
        }
        
        /**
         * 获取分类显示名称
         */
        private String getCategoryDisplayName(String category) {
            switch (category) {
                case "discussion":
                    return "讨论";
                case "friends":
                    return "交友";
                case "help":
                    return "求助";
                case "share":
                    return "分享";
                case "announcement":
                    return "公告";
                default:
                    return "其他";
            }
        }
        
        /**
         * 设置分类背景色
         */
        private void setCategoryBackground(String category) {
            int backgroundRes;
            switch (category) {
                case "discussion":
                    backgroundRes = R.drawable.bg_category_discussion;
                    break;
                case "friends":
                    backgroundRes = R.drawable.bg_category_friends;
                    break;
                case "help":
                    backgroundRes = R.drawable.bg_category_help;
                    break;
                case "share":
                    backgroundRes = R.drawable.bg_category_share;
                    break;
                case "announcement":
                    backgroundRes = R.drawable.bg_category_announcement;
                    break;
                default:
                    backgroundRes = R.drawable.bg_category_default;
                    break;
            }
            tvCategory.setBackgroundResource(backgroundRes);
        }
    }
    
    /**
     * 更新数据
     */
    public void updateData(List<ForumPost> newPostList) {
        this.postList.clear();
        this.postList.addAll(newPostList);
        notifyDataSetChanged();
    }
    
    /**
     * 添加新帖子到顶部
     */
    public void addNewPost(ForumPost post) {
        postList.add(0, post);
        notifyItemInserted(0);
    }
    
    /**
     * 移除帖子
     */
    public void removePost(int position) {
        if (position >= 0 && position < postList.size()) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }
}