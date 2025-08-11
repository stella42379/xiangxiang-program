package com.xiangjia.locallife.ui.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.database.entity.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天消息适配器 - 完善版
 * 支持用户消息、AI回复、错误处理和重试功能
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;
    private static final int VIEW_TYPE_ERROR = 3;
    
    private List<ChatItem> messageList;
    private OnItemClickListener onItemClickListener;
    private OnRetryClickListener onRetryClickListener;
    
    public ChatAdapter() {
        this.messageList = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        
        switch (viewType) {
            case VIEW_TYPE_USER:
                View userView = inflater.inflate(R.layout.item_chat_user, parent, false);
                return new UserMessageViewHolder(userView);
            case VIEW_TYPE_AI:
                View aiView = inflater.inflate(R.layout.item_chat_ai, parent, false);
                return new AIMessageViewHolder(aiView);
            case VIEW_TYPE_ERROR:
                View errorView = inflater.inflate(R.layout.item_chat_error, parent, false);
                return new ErrorMessageViewHolder(errorView);
            default:
                View defaultView = inflater.inflate(R.layout.item_chat_ai, parent, false);
                return new AIMessageViewHolder(defaultView);
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatItem item = messageList.get(position);
        
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(item);
        } else if (holder instanceof AIMessageViewHolder) {
            ((AIMessageViewHolder) holder).bind(item);
        } else if (holder instanceof ErrorMessageViewHolder) {
            ((ErrorMessageViewHolder) holder).bind(item);
        }
    }
    
    @Override
    public int getItemViewType(int position) {
        ChatItem item = messageList.get(position);
        if (item.isError) {
            return VIEW_TYPE_ERROR;
        } else if (item.isFromUser) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_AI;
        }
    }
    
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    
    /**
     * 添加用户消息
     */
    public void addUserMessage(String message) {
        ChatItem item = new ChatItem();
        item.content = message;
        item.isFromUser = true;
        item.timestamp = System.currentTimeMillis();
        item.isError = false;
        
        messageList.add(item);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 添加AI回复
     */
    public void addAIReply(String reply) {
        ChatItem item = new ChatItem();
        item.content = reply;
        item.isFromUser = false;
        item.timestamp = System.currentTimeMillis();
        item.isError = false;
        
        messageList.add(item);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 添加错误消息
     */
    public void addErrorMessage(String errorMessage) {
        ChatItem item = new ChatItem();
        item.content = errorMessage;
        item.isFromUser = false;
        item.timestamp = System.currentTimeMillis();
        item.isError = true;
        
        messageList.add(item);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 移除最后一条消息（用于重试）
     */
    public void removeLastMessage() {
        if (!messageList.isEmpty()) {
            int lastIndex = messageList.size() - 1;
            messageList.remove(lastIndex);
            notifyItemRemoved(lastIndex);
        }
    }
    
    /**
     * 设置数据
     */
    public void setData(List<ChatMessage> messageList) {
        this.messageList.clear();
        for (ChatMessage message : messageList) {
            ChatItem item = new ChatItem();
            item.content = message.content;
            item.isFromUser = message.isFromUser;
            item.timestamp = message.timestamp;
            item.isError = false;
            this.messageList.add(item);
        }
        notifyDataSetChanged();
    }
    
    /**
     * 清空数据
     */
    public void clear() {
        messageList.clear();
        notifyDataSetChanged();
    }
    
    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    /**
     * 设置重试点击监听器
     */
    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.onRetryClickListener = listener;
    }
    
    /**
     * 聊天项目数据类
     */
    public static class ChatItem {
        public String content;
        public boolean isFromUser;
        public long timestamp;
        public boolean isError;
    }
    
    /**
     * 用户消息ViewHolder
     */
    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        
        private TextView messageText;
        private TextView timeText;
        private ImageView userAvatar;
        
        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            messageText = itemView.findViewById(R.id.user_message_text);
            timeText = itemView.findViewById(R.id.user_message_time);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(messageList.get(position));
                    }
                }
            });
        }
        
        public void bind(ChatItem item) {
            messageText.setText(item.content);
            
            // 格式化时间
            String timeStr = DateFormat.format("HH:mm", new Date(item.timestamp)).toString();
            timeText.setText(timeStr);
            
            // 设置头像
            userAvatar.setImageResource(R.drawable.ic_user_avatar);
        }
    }
    
    /**
     * AI消息ViewHolder
     */
    class AIMessageViewHolder extends RecyclerView.ViewHolder {
        
        private TextView messageText;
        private TextView timeText;
        private ImageView aiAvatar;
        
        public AIMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            messageText = itemView.findViewById(R.id.ai_message_text);
            timeText = itemView.findViewById(R.id.ai_message_time);
            aiAvatar = itemView.findViewById(R.id.ai_avatar);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(messageList.get(position));
                    }
                }
            });
        }
        
        public void bind(ChatItem item) {
            messageText.setText(item.content);
            
            // 格式化时间
            String timeStr = DateFormat.format("HH:mm", new Date(item.timestamp)).toString();
            timeText.setText(timeStr);
            
            // 设置AI头像
            aiAvatar.setImageResource(R.drawable.ic_ai_avatar);
        }
    }
    
    /**
     * 错误消息ViewHolder
     */
    class ErrorMessageViewHolder extends RecyclerView.ViewHolder {
        
        private TextView errorText;
        private TextView retryButton;
        private ImageView errorIcon;
        
        public ErrorMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            errorText = itemView.findViewById(R.id.error_message_text);
            retryButton = itemView.findViewById(R.id.retry_button);
            errorIcon = itemView.findViewById(R.id.error_icon);
            
            retryButton.setOnClickListener(v -> {
                if (onRetryClickListener != null) {
                    onRetryClickListener.onRetryClick();
                }
            });
        }
        
        public void bind(ChatItem item) {
            errorText.setText(item.content);
            errorIcon.setImageResource(R.drawable.ic_error);
            
            // 如果消息包含"重试"，显示重试按钮
            if (item.content.contains("重试") || item.content.contains("点击重试")) {
                retryButton.setVisibility(View.VISIBLE);
            } else {
                retryButton.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * 点击监听器接口
     */
    public interface OnItemClickListener {
        void onItemClick(ChatItem item);
    }
    
    /**
     * 重试点击监听器接口
     */
    public interface OnRetryClickListener {
        void onRetryClick();
    }
}