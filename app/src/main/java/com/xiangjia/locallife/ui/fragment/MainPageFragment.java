package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.ChipGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.xiangjia.locallife.R;

/**
 * 湘湘管家主页Fragment - 使用现有XML布局，修复ID引用问题
 */
public class MainPageFragment extends Fragment {
    
    private static final String TAG = "MainPageFragment";
    
    // UI组件 - 使用现有XML中的ID
    private EditText etChatInput;
    private Button btnSend;
    private TextView tvHeaderTitle;
    private TextView tvHeaderSub;
    private TextView tvManagerGreeting;
    private CardView layoutChatArea;
    private RecyclerView recyclerMaintenance;
    private RecyclerView recyclerEmergency;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "创建湘湘管家主页 - 使用现有XML布局");
        
        // 🎯 使用现有的XML布局文件
        View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        
        // 初始化UI组件
        initViews(rootView);
        
        // 设置监听器
        setupListeners();
        
        // 设置初始数据
        setupInitialData();
        
        return rootView;
    }
    
    /**
     * 初始化UI组件 - 使用现有XML中的实际ID
     */
    private void initViews(View rootView) {
        try {
            // 欢迎文本
            tvHeaderTitle = rootView.findViewById(R.id.tvHeaderTitle);
            tvHeaderSub = rootView.findViewById(R.id.tvHeaderSub);
            
            // 聊天相关
            etChatInput = rootView.findViewById(R.id.et_chat_input);
            btnSend = rootView.findViewById(R.id.btn_send);
            tvManagerGreeting = rootView.findViewById(R.id.tv_manager_greeting);

            // 隐藏通用头部中的分类ChipGroup
            ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);
            if (chipGroup != null) {
                chipGroup.setVisibility(View.GONE);
            }
            
            // 聊天区域卡片
            layoutChatArea = rootView.findViewById(R.id.layout_chat_area);
            
            // 维修和紧急服务列表
            recyclerMaintenance = rootView.findViewById(R.id.recycler_maintenance);
            recyclerEmergency = rootView.findViewById(R.id.recycler_emergency);

            if (recyclerMaintenance != null) {
                recyclerMaintenance.setNestedScrollingEnabled(false);
            }
            if (recyclerEmergency != null) {
                recyclerEmergency.setNestedScrollingEnabled(false);
            }
            
            Log.d(TAG, "UI组件初始化成功");
        } catch (Exception e) {
            Log.e(TAG, "UI组件初始化失败", e);
        }
    }
    
    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 发送按钮点击
        if (btnSend != null) {
            btnSend.setOnClickListener(v -> onSendClick());
        }
        
        // 聊天区域点击
        if (layoutChatArea != null) {
            layoutChatArea.setOnClickListener(v -> goToChat());
        }
        
        // 聊天输入框回车发送
        if (etChatInput != null) {
            etChatInput.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                    onSendClick();
                    return true;
                }
                return false;
            });
        }
    }
    
    /**
     * 设置初始数据
     */
    private void setupInitialData() {
        // 设置欢迎文本
        if (tvHeaderTitle != null) {
            tvHeaderTitle.setText("欢迎回来！");
        }
        if (tvHeaderSub != null) {
            tvHeaderSub.setText("Welcome Back!");
        }
        if (tvManagerGreeting != null) {
            tvManagerGreeting.setText("请问有什么需要帮助的吗？");
        }
        
        // 设置输入框提示
        if (etChatInput != null) {
            etChatInput.setHint("在这儿输入你的第一句话");
        }
        
        // 初始化服务列表
        setupServiceLists();
    }
    
    /**
     * 设置服务列表
     */
    private void setupServiceLists() {
        // TODO: 设置维修服务和紧急服务的RecyclerView
        // 这里可以后续添加适配器和数据
        if (recyclerMaintenance != null) {
            Log.d(TAG, "维修服务列表已准备");
        }
        if (recyclerEmergency != null) {
            Log.d(TAG, "紧急服务列表已准备");
        }
    }
    
    /**
     * 发送消息
     */
    private void onSendClick() {
        if (etChatInput != null) {
            String message = etChatInput.getText().toString().trim();
            if (!message.isEmpty()) {
                Log.d(TAG, "发送消息: " + message);
                Toast.makeText(getContext(), "消息已发送: " + message, Toast.LENGTH_SHORT).show();
                etChatInput.setText("");
                
                // 跳转到聊天页面
                goToChat();
            } else {
                Toast.makeText(getContext(), "请输入消息", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 跳转到聊天页面
     */
    private void goToChat() {
        try {
            Toast.makeText(getContext(), "即将跳转到聊天页面", Toast.LENGTH_SHORT).show();
            // TODO: 启动ChatActivity
            // Intent intent = new Intent(getActivity(), ChatActivity.class);
            // startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "跳转到聊天页面失败", e);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MainPageFragment onResume");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "MainPageFragment onPause");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "MainPageFragment onDestroyView");
        
        // 清理引用
        etChatInput = null;
        btnSend = null;
        tvHeaderTitle = null;
        tvHeaderSub = null;
        tvManagerGreeting = null;
        layoutChatArea = null;
        recyclerMaintenance = null;
        recyclerEmergency = null;
    }
}