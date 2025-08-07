package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.ChatAdapter;

/**
 * Dify AI助手Fragment
 * 提供AI对话功能
 */
public class DifyFragment extends Fragment {
    
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dify, container, false);
        
        initViews(view);
        initRecyclerView();
        setupListeners();
        loadChatHistory();
        
        return view;
    }
    
    /**
     * 初始化视图
     */
    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_button);
        progressBar = view.findViewById(R.id.progress_bar);
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 发送按钮点击监听
        sendButton.setOnClickListener(v -> {
            sendMessage();
        });
        
        // 输入框回车监听
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }
    
    /**
     * 发送消息
     */
    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // 清空输入框
        messageEditText.setText("");
        
        // 添加用户消息到聊天列表
        addUserMessage(message);
        
        // 显示AI正在输入状态
        showAIThinking(true);
        
        // 发送消息到AI服务
        sendMessageToAI(message);
    }
    
    /**
     * 添加用户消息
     */
    private void addUserMessage(String message) {
        // TODO: 添加用户消息到聊天列表
        chatAdapter.addUserMessage(message);
        scrollToBottom();
    }
    
    /**
     * 添加AI回复
     */
    private void addAIReply(String reply) {
        // TODO: 添加AI回复到聊天列表
        chatAdapter.addAIReply(reply);
        scrollToBottom();
    }
    
    /**
     * 滚动到底部
     */
    private void scrollToBottom() {
        chatRecyclerView.post(() -> {
            if (chatAdapter.getItemCount() > 0) {
                chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }
    
    /**
     * 显示AI思考状态
     */
    private void showAIThinking(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            sendButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            sendButton.setEnabled(true);
        }
    }
    
    /**
     * 发送消息到AI服务
     */
    private void sendMessageToAI(String message) {
        // TODO: 调用Dify API发送消息
        // 模拟AI回复
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                getActivity().runOnUiThread(() -> {
                    showAIThinking(false);
                    addAIReply("这是AI的回复：" + message);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 加载聊天历史
     */
    private void loadChatHistory() {
        // TODO: 从本地数据库加载聊天历史
    }
    
    /**
     * 保存聊天记录
     */
    private void saveChatRecord(String message, String reply) {
        // TODO: 保存聊天记录到本地数据库
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 刷新聊天历史
        loadChatHistory();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        // 保存聊天记录
        // TODO: 保存当前聊天记录
    }
}
