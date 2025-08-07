package com.xiangjia.locallife.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息适配器
 * 用于显示聊天消息列表
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;
    
    private List<ChatMessage> messageList;
    private OnItemClickListener onItemClickListener;
    
    public ChatAdapter() {
        this.messageList = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_ai, parent, false);
            return new AIMessageViewHolder(view);
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof AIMessageViewHolder) {
            ((AIMessageViewHolder) holder).bind(message);
        }
    }
    
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.isFromUser() ? VIEW_TYPE_USER : VIEW_TYPE_AI;
    }
    
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    
    /**
     * 添加用户消息
     */
    public void addUserMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, System.currentTimeMillis());
        messageList.add(chatMessage);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 添加AI回复
     */
    public void addAIReply(String reply) {
        ChatMessage chatMessage = new ChatMessage(reply, false, System.currentTimeMillis());
        messageList.add(chatMessage);
        notifyItemInserted(messageList.size() - 1);
    }
    
    /**
     * 设置数据
     */
    public void setData(List<ChatMessage> messageList) {
        this.messageList.clear();
        this.messageList.addAll(messageList);
        notifyDataSetChanged();
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
        void onItemClick(ChatMessage message);
    }
    
    /**
     * 聊天消息数据类
     */
    public static class ChatMessage {
        private String content;
        private boolean isFromUser;
        private long timestamp;
        
        public ChatMessage(String content, boolean isFromUser, long timestamp) {
            this.content = content;
            this.isFromUser = isFromUser;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getContent() { return content; }
        public boolean isFromUser() { return isFromUser; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * 用户消息ViewHolder
     */
    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        
        private TextView messageText;
        private TextView timeText;
        
        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            messageText = itemView.findViewById(R.id.message_text);
            timeText = itemView.findViewById(R.id.time_text);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(messageList.get(position));
                    }
                }
            });
        }
        
        public void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            timeText.setText(formatTime(message.getTimestamp()));
        }
        
        private String formatTime(long timestamp) {
            // TODO: 格式化时间显示
            return "12:00";
        }
    }
    
    /**
     * AI消息ViewHolder
     */
    class AIMessageViewHolder extends RecyclerView.ViewHolder {
        
        private TextView messageText;
        private TextView timeText;
        
        public AIMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            
            messageText = itemView.findViewById(R.id.message_text);
            timeText = itemView.findViewById(R.id.time_text);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(messageList.get(position));
                    }
                }
            });
        }
        
        public void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            timeText.setText(formatTime(message.getTimestamp()));
        }
        
        private String formatTime(long timestamp) {
            // TODO: 格式化时间显示
            return "12:00";
        }
    }
}
