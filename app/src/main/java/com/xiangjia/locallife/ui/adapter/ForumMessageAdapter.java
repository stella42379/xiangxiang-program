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
import com.xiangjia.locallife.model.ForumMessage;
import com.xiangjia.locallife.util.DateUtils;

import java.util.List;

/**
 * 论坛消息列表适配器
 * 用于显示帖子回复列表
 */
public class ForumMessageAdapter extends RecyclerView.Adapter<ForumMessageAdapter.MessageViewHolder> {
    
    private Context context;
    private List<ForumMessage> messageList;
    private OnMessageClickListener onMessageClickListener;
    
    public ForumMessageAdapter(Context context, List<ForumMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }
    
    /**
     * 消息点击监听接口
     */
    public interface OnMessageClickListener {
        void onMessageClick(ForumMessage message);
        void onLikeClick(ForumMessage message);
        void onUserClick(ForumMessage message);
    }
    
    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.onMessageClickListener = listener;
    }
    
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_message, parent, false);
        return new MessageViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ForumMessage message = messageList.get(position);
        holder.bind(message, position + 1); // 楼层从1开始
    }
    
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    
    /**
     * 消息ViewHolder
     */
    class MessageViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivAvatar;
        private TextView tvAuthorName;
        private TextView tvFloorNumber;
        private TextView tvMessageTime;
        private TextView tvAuthorTag;
        private TextView tvContent;
        private ImageView ivMessageImage;
        private TextView tvLikeCount;
        private ImageView ivLike;
        private View layoutLike;
        private View layoutUser;
        private View layoutMessage;
        
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvFloorNumber = itemView.findViewById(R.id.tv_floor_number);
            tvMessageTime = itemView.findViewById(R.id.tv_message_time);
            tvAuthorTag = itemView.findViewById(R.id.tv_author_tag);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivMessageImage = itemView.findViewById(R.id.iv_message_image);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            ivLike = itemView.findViewById(R.id.iv_like);
            layoutLike = itemView.findViewById(R.id.layout_like);
            layoutUser = itemView.findViewById(R.id.layout_user);
            layoutMessage = itemView.findViewById(R.id.layout_message);
            
            // 设置点击监听
            layoutMessage.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onMessageClickListener.onMessageClick(messageList.get(position));
                    }
                }
            });
            
            layoutLike.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onMessageClickListener.onLikeClick(messageList.get(position));
                    }
                }
            });
            
            layoutUser.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onMessageClickListener.onUserClick(messageList.get(position));
                    }
                }
            });
        }
        
        public void bind(ForumMessage message, int floorNumber) {
            // 设置作者信息
            tvAuthorName.setText(message.getAuthorName());
            tvFloorNumber.setText(floorNumber + "楼");
            tvMessageTime.setText(DateUtils.formatRelativeTime(message.getTimestamp()));
            
            // 设置头像 (简化处理，使用首字母)
            setAvatarText(message.getAuthorName());
            
            // 设置楼主标识
            if (message.isAuthorReply()) {
                tvAuthorTag.setVisibility(View.VISIBLE);
                tvAuthorTag.setText("楼主");
                tvAuthorTag.setBackgroundResource(R.drawable.bg_author_tag);
            } else {
                tvAuthorTag.setVisibility(View.GONE);
            }
            
            // 设置消息内容
            tvContent.setText(message.getContent());
            
            // 设置消息图片
            if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
                ivMessageImage.setVisibility(View.VISIBLE);
                // TODO: 使用图片加载库加载图片
                // Glide.with(context).load(message.getImageUrl()).into(ivMessageImage);
            } else {
                ivMessageImage.setVisibility(View.GONE);
            }
            
            // 设置点赞数
            tvLikeCount.setText(String.valueOf(message.getLikeCount()));
            
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
                // 实际项目中可以使用CircleImageView或者自定义View
            }
        }
    }
    
    /**
     * 更新数据
     */
    public void updateData(List<ForumMessage> newMessageList) {
        this.messageList.clear();
        this.messageList.addAll(newMessageList);
        notifyDataSetChanged();
    }
    
    /**
     * 添加新消息到底部
     */
    public void addNewMessage(ForumMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 移除消息
     */
    public void removeMessage(int position) {
        if (position >= 0 && position < messageList.size()) {
            messageList.remove(position);
            notifyItemRemoved(position);
            // 更新后续楼层号
            notifyItemRangeChanged(position, messageList.size() - position);
        }
    }
}