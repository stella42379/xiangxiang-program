package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.network.ChatGPTService;
import com.xiangjia.locallife.network.APICallback;
import com.xiangjia.locallife.ui.adapter.ChatAdapter;
import com.xiangjia.locallife.database.ChatDatabase;
import com.xiangjia.locallife.database.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

/**
 * Dify AI助手Fragment - 接入ChatGPT API
 * 提供AI对话功能，支持消息持久化和错误恢复
 */
public class DifyFragment extends Fragment {
    
    private static final String TAG = "DifyFragment";
    
    // UI组件
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    
    // 服务和数据
    private ChatGPTService chatGPTService;
    private ChatDatabase chatDatabase;
    private String conversationId;
    private Handler mainHandler;
    
    // 状态管理
    private boolean isRequestPending = false;
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dify, container, false);
        
        initServices();
        initViews(view);
        initRecyclerView();
        setupListeners();
        loadChatHistory();
        
        return view;
    }
    
    /**
     * 初始化服务
     */
    private void initServices() {
        chatGPTService = new ChatGPTService();
        chatDatabase = ChatDatabase.getInstance(getContext());
        mainHandler = new Handler(Looper.getMainLooper());
        conversationId = generateConversationId();
        
        Log.d(TAG, "服务初始化完成，会话ID: " + conversationId);
    }
    
    /**
     * 初始化视图
     */
    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_button);
        progressBar = view.findViewById(R.id.progress_bar);
        
        // 初始状态下禁用发送按钮
        sendButton.setEnabled(false);
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        chatAdapter = new ChatAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true); // 从底部开始布局
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
        
        // 监听数据变化自动滚动到底部
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                scrollToBottom();
            }
        });
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 发送按钮点击监听
        sendButton.setOnClickListener(v -> sendMessage());
        
        // 输入框文本变化监听
        messageEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 根据输入内容启用/禁用发送按钮
                boolean hasText = !TextUtils.isEmpty(s.toString().trim());
                sendButton.setEnabled(hasText && !isRequestPending);
            }
            
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        
        // 输入框回车监听
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }
    
    /**
     * 发送消息 - 核心方法，包含完整的错误处理
     */
    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        
        // 输入验证
        if (TextUtils.isEmpty(message)) {
            showToast("请输入消息内容");
            return;
        }
        
        if (message.length() > 2000) {
            showToast("消息内容过长，请控制在2000字以内");
            return;
        }
        
        if (isRequestPending) {
            showToast("上一条消息正在处理中，请稍等");
            return;
        }
        
        // 清空输入框并设置UI状态
        messageEditText.setText("");
        setUIState(true);
        
        // 添加用户消息到界面和数据库
        ChatMessage userMessage = createUserMessage(message);
        addMessageToUI(userMessage);
        saveChatMessage(userMessage);
        
        // 发送到ChatGPT API
        sendMessageToGPT(message);
    }
    
    /**
     * 发送消息到ChatGPT API
     */
    private void sendMessageToGPT(String message) {
        Log.d(TAG, "发送消息到ChatGPT: " + message);
        
        chatGPTService.sendMessage(message, conversationId, new APICallback<String>() {
            @Override
            public void onStart() {
                Log.d(TAG, "开始请求ChatGPT API");
                mainHandler.post(() -> showAIThinking(true));
            }
            
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "ChatGPT响应成功: " + response);
                retryCount = 0; // 重置重试次数
                
                mainHandler.post(() -> {
                    setUIState(false);
                    if (!TextUtils.isEmpty(response)) {
                        ChatMessage aiMessage = createAIMessage(response);
                        addMessageToUI(aiMessage);
                        saveChatMessage(aiMessage);
                    } else {
                        handleEmptyResponse();
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "ChatGPT请求失败: " + errorMessage);
                mainHandler.post(() -> handleAPIError(errorMessage, message));
            }
            
            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "ChatGPT网络失败: " + errorMessage);
                mainHandler.post(() -> handleNetworkFailure(errorMessage, message));
            }
            
            @Override
            public void onNetworkError(String errorMessage) {
                Log.e(TAG, "ChatGPT网络错误: " + errorMessage);
                mainHandler.post(() -> handleNetworkError(errorMessage, message));
            }
            
            @Override
            public void onFinish() {
                Log.d(TAG, "ChatGPT请求结束");
                mainHandler.post(() -> showAIThinking(false));
            }
            
            @Override
            public void onProgress(int progress) {
                // ChatGPT API通常不提供进度回调
            }
        });
    }
    
    /**
     * 处理API错误 - 借鉴新闻界面的错误处理策略
     */
    private void handleAPIError(String errorMessage, String originalMessage) {
        setUIState(false);
        
        String userFriendlyMessage;
        boolean canRetry = false;
        
        if (errorMessage.contains("401") || errorMessage.contains("Unauthorized")) {
            userFriendlyMessage = "API密钥无效，请检查配置";
        } else if (errorMessage.contains("429") || errorMessage.contains("rate limit")) {
            userFriendlyMessage = "请求过于频繁，请稍后再试";
            canRetry = true;
        } else if (errorMessage.contains("500") || errorMessage.contains("502") || errorMessage.contains("503")) {
            userFriendlyMessage = "服务器暂时不可用，请稍后再试";
            canRetry = true;
        } else if (errorMessage.contains("timeout")) {
            userFriendlyMessage = "请求超时，请检查网络连接";
            canRetry = true;
        } else {
            userFriendlyMessage = "AI服务暂时不可用：" + errorMessage;
            canRetry = true;
        }
        
        if (canRetry && retryCount < MAX_RETRY_COUNT) {
            showRetryOption(userFriendlyMessage, originalMessage);
        } else {
            showErrorMessage(userFriendlyMessage);
        }
    }
    
    /**
     * 处理网络失败
     */
    private void handleNetworkFailure(String errorMessage, String originalMessage) {
        setUIState(false);
        
        if (retryCount < MAX_RETRY_COUNT) {
            showRetryOption("网络连接失败，是否重试？", originalMessage);
        } else {
            showErrorMessage("网络连接失败，请检查网络设置后重试");
        }
    }
    
    /**
     * 处理网络错误
     */
    private void handleNetworkError(String errorMessage, String originalMessage) {
        setUIState(false);
        
        String message = "网络错误";
        if (errorMessage.contains("No network")) {
            message = "无网络连接，请检查网络设置";
        } else if (errorMessage.contains("DNS")) {
            message = "DNS解析失败，请检查网络";
        } else if (errorMessage.contains("SSL") || errorMessage.contains("certificate")) {
            message = "安全连接失败，请检查系统时间";
        }
        
        showErrorMessage(message);
    }
    
    /**
     * 处理空响应
     */
    private void handleEmptyResponse() {
        ChatMessage aiMessage = createAIMessage("抱歉，我现在无法回答您的问题，请稍后再试。");
        addMessageToUI(aiMessage);
        saveChatMessage(aiMessage);
    }
    
    /**
     * 显示重试选项
     */
    private void showRetryOption(String message, String originalMessage) {
        ChatMessage errorMessage = createAIMessage(message + " [点击重试]");
        addMessageToUI(errorMessage);
        
        // 设置重试点击事件
        chatAdapter.setOnRetryClickListener(() -> {
            retryCount++;
            removeLastMessage(); // 移除错误消息
            sendMessageToGPT(originalMessage);
        });
    }
    
    /**
     * 显示错误消息
     */
    private void showErrorMessage(String message) {
        ChatMessage errorMessage = createAIMessage("❌ " + message);
        addMessageToUI(errorMessage);
        saveChatMessage(errorMessage);
    }
    
    /**
     * 创建用户消息
     */
    private ChatMessage createUserMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.id = UUID.randomUUID().toString();
        message.content = content;
        message.isFromUser = true;
        message.timestamp = System.currentTimeMillis();
        message.conversationId = conversationId;
        return message;
    }
    
    /**
     * 创建AI消息
     */
    private ChatMessage createAIMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.id = UUID.randomUUID().toString();
        message.content = content;
        message.isFromUser = false;
        message.timestamp = System.currentTimeMillis();
        message.conversationId = conversationId;
        return message;
    }
    
    /**
     * 添加消息到UI
     */
    private void addMessageToUI(ChatMessage message) {
        if (message.isFromUser) {
            chatAdapter.addUserMessage(message.content);
        } else {
            chatAdapter.addAIReply(message.content);
        }
    }
    
    /**
     * 移除最后一条消息（用于重试时移除错误消息）
     */
    private void removeLastMessage() {
        chatAdapter.removeLastMessage();
    }
    
    /**
     * 设置UI状态
     */
    private void setUIState(boolean isLoading) {
        isRequestPending = isLoading;
        sendButton.setEnabled(!isLoading && !TextUtils.isEmpty(messageEditText.getText().toString().trim()));
        messageEditText.setEnabled(!isLoading);
    }
    
    /**
     * 显示AI思考状态
     */
    private void showAIThinking(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * 滚动到底部
     */
    private void scrollToBottom() {
        if (chatAdapter.getItemCount() > 0) {
            chatRecyclerView.post(() -> {
                chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            });
        }
    }
    
    /**
     * 加载聊天历史
     */
    private void loadChatHistory() {
        new Thread(() -> {
            try {
                List<ChatMessage> history = chatDatabase.chatMessageDao()
                    .getMessagesByConversation(conversationId);
                
                mainHandler.post(() -> {
                    if (history != null && !history.isEmpty()) {
                        chatAdapter.setData(history);
                        scrollToBottom();
                        Log.d(TAG, "加载聊天历史: " + history.size() + " 条消息");
                    } else {
                        // 显示欢迎消息
                        showWelcomeMessage();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "加载聊天历史失败", e);
                mainHandler.post(this::showWelcomeMessage);
            }
        }).start();
    }
    
    /**
     * 显示欢迎消息
     */
    private void showWelcomeMessage() {
        ChatMessage welcomeMessage = createAIMessage("您好！我是湘湘AI助手，有什么可以帮助您的吗？");
        addMessageToUI(welcomeMessage);
        saveChatMessage(welcomeMessage);
    }
    
    /**
     * 保存聊天记录到数据库
     */
    private void saveChatMessage(ChatMessage message) {
        new Thread(() -> {
            try {
                chatDatabase.chatMessageDao().insert(message);
                Log.d(TAG, "保存消息成功: " + message.content);
            } catch (Exception e) {
                Log.e(TAG, "保存消息失败", e);
            }
        }).start();
    }
    
    /**
     * 生成会话ID
     */
    private String generateConversationId() {
        return "conv_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 清理聊天记录
     */
    public void clearChatHistory() {
        new Thread(() -> {
            try {
                chatDatabase.chatMessageDao().deleteByConversation(conversationId);
                mainHandler.post(() -> {
                    chatAdapter.clear();
                    showWelcomeMessage();
                    showToast("聊天记录已清空");
                });
            } catch (Exception e) {
                Log.e(TAG, "清空聊天记录失败", e);
                mainHandler.post(() -> showToast("清空失败，请重试"));
            }
        }).start();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 重新加载聊天历史，确保数据同步
        loadChatHistory();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        // 暂停时保存当前状态
        if (isRequestPending) {
            // 如果有正在进行的请求，可以选择取消或等待
            Log.d(TAG, "Fragment暂停，有正在进行的请求");
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清理资源
        if (chatGPTService != null) {
            chatGPTService.cancelPendingRequests();
        }
        mainHandler = null;
    }
}