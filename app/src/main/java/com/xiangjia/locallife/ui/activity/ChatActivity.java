package com.xiangjia.locallife.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.adapter.ChatAdapter;

/**
 * 聊天页面Activity
 * 提供完整的聊天功能
 */
public class ChatActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton voiceButton;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        initViews();
        setupToolbar();
        initRecyclerView();
        setupListeners();
        loadChatHistory();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        voiceButton = findViewById(R.id.voice_button);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("社区论坛");
        }
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        
        // 语音按钮点击监听
        voiceButton.setOnClickListener(v -> {
            startVoiceInput();
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
        chatAdapter.addUserMessage(message);
        scrollToBottom();
    }
    
    /**
     * 添加AI回复
     */
    private void addAIReply(String reply) {
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
            progressBar.setVisibility(ProgressBar.VISIBLE);
            sendButton.setEnabled(false);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
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
                runOnUiThread(() -> {
                    showAIThinking(false);
                    addAIReply("这是AI的回复：" + message);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 开始语音输入
     */
    private void startVoiceInput() {
        // TODO: 实现语音输入功能
        Toast.makeText(this, "语音输入功能开发中", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // 保存聊天记录
        saveCurrentChat();
        super.onBackPressed();
    }
    
    /**
     * 保存当前聊天记录
     */
    private void saveCurrentChat() {
        // TODO: 保存当前聊天记录
    }
}
