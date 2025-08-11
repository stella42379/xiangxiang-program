package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.xiangjia.locallife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 湘湘管家主页Fragment - 毛玻璃风格设计（与新闻、论坛页面保持一致）
 */
public class MainPageFragment extends Fragment {
    
    private static final String TAG = "MainPageFragment";
    
    // 🎯 UI组件 - 毛玻璃风格
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private TextView statusText;
    
    // 聊天相关
    private CardView layoutChatArea;
    private EditText etChatInput;
    private ImageButton btnSend;
    private TextView tvWelcome;
    private TextView tvWelcomeSub;
    private TextView tvManagerGreeting;
    
    // 功能卡片
    private CardView cardDailyInspection;
    private CardView cardEmergencyMedical;
    private CardView cardTips;
    
    // 🆕 湘湘管家卡片 - 用于跳转到DifyFragment
    private CardView cardXiangjiaBotChat;
    
    // 服务列表
    private RecyclerView recyclerMaintenance;
    private RecyclerView recyclerEmergency;
    
    // 数据相关
    private List<ServiceItem> maintenanceServices;
    private List<ServiceItem> emergencyServices;
    private MaintenanceAdapter maintenanceAdapter;
    private EmergencyAdapter emergencyAdapter;
    
    // 线程处理
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "🎯 MainPageFragment onCreateView - 毛玻璃风格");
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "🎯 MainPageFragment onViewCreated");
        
        initViews(view);
        setupSwipeRefresh();
        setupChatArea();
        setupFunctionCards();
        setupServiceLists();
        setupInitialData();
    }
    
    /**
     * 🎯 初始化毛玻璃风格视图组件
     */
    private void initViews(View view) {
        Log.d(TAG, "🎯 开始初始化毛玻璃风格视图组件");
        
        try {
            // 基础组件
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            nestedScrollView = view.findViewById(R.id.nested_scroll);
            statusText = view.findViewById(R.id.status_text);
            
            // 欢迎文本
            tvWelcome = view.findViewById(R.id.tv_welcome);
            tvWelcomeSub = view.findViewById(R.id.tv_welcome_sub);
            
            // 聊天区域
            layoutChatArea = view.findViewById(R.id.layout_chat_area);
            etChatInput = view.findViewById(R.id.et_chat_input);
            btnSend = view.findViewById(R.id.btn_send);
            tvManagerGreeting = view.findViewById(R.id.tv_manager_greeting);
            
            // 🆕 湘湘管家聊天卡片 - 使用实际存在的布局ID
            cardXiangjiaBotChat = view.findViewById(R.id.layout_chat_area);
            
            // 功能卡片
            cardDailyInspection = view.findViewById(R.id.card_daily_inspection);
            cardEmergencyMedical = view.findViewById(R.id.card_emergency_medical);
            cardTips = view.findViewById(R.id.card_tips);
            
            // 服务列表
            recyclerMaintenance = view.findViewById(R.id.recycler_maintenance);
            recyclerEmergency = view.findViewById(R.id.recycler_emergency);
            
            Log.d(TAG, "🎯 毛玻璃风格视图组件初始化完成");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 视图组件初始化失败", e);
        }
    }
    
    /**
     * 🎯 设置下拉刷新 - 适配NestedScrollView
     */
    private void setupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(
                0xFF2d8cf0,  // 蓝色
                0xFF87CEEB,  // 天蓝色
                0xFFFFB6C1   // 粉色
            );
            
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Log.d(TAG, "🔄 用户下拉刷新");
                refreshData();
            });
            
            // 🎯 关键：让SwipeRefreshLayout正确判断NestedScrollView是否能继续下拉
            if (nestedScrollView != null) {
                swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> {
                    return nestedScrollView.getScrollY() > 0;
                });
            }
            
            Log.d(TAG, "🎯 下拉刷新设置完成");
        }
    }
    
    /**
     * 🎯 设置聊天区域 - 🚀 核心功能：点击跳转到DifyFragment
     */
    private void setupChatArea() {
        try {
            // 🎯 湘湘管家聊天卡片点击 - 跳转到DifyFragment（作为新页面）
            if (cardXiangjiaBotChat != null) {
                cardXiangjiaBotChat.setOnClickListener(v -> {
                    Log.d(TAG, "🤖 点击湘湘管家卡片，准备打开DifyFragment新页面");
                    navigateToDifyFragment();
                });
                Log.d(TAG, "✅ 湘湘管家卡片点击监听器已设置");
            } else {
                Log.w(TAG, "⚠️ 湘湘管家卡片未找到，请检查布局文件中的ID");
            }
            
            // 设置聊天区域点击监听 - 跳转到DifyFragment
            if (layoutChatArea != null) {
                layoutChatArea.setOnClickListener(v -> {
                    Log.d(TAG, "💬 点击聊天区域，打开DifyFragment");
                    navigateToDifyFragment();
                });
            }
            
            // 设置发送按钮点击监听 - 跳转到DifyFragment
            if (btnSend != null) {
                btnSend.setOnClickListener(v -> {
                    Log.d(TAG, "📤 点击发送按钮，打开DifyFragment进行对话");
                    navigateToDifyFragment();
                });
            }
            
            // 设置输入框回车发送 - 跳转到DifyFragment
            if (etChatInput != null) {
                etChatInput.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                        Log.d(TAG, "⌨️ 输入框回车，打开DifyFragment");
                        navigateToDifyFragment();
                        return true;
                    }
                    return false;
                });
            }
            
            Log.d(TAG, "🎯 聊天区域设置完成 - 支持跳转到DifyFragment");
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置聊天区域失败", e);
        }
    }
    
    /**
     * 🎯 核心方法：跳转到DifyFragment（作为新页面打开，不影响底部导航）
     */
    private void navigateToDifyFragment() {
        try {
            Log.d(TAG, "🤖 准备打开湘湘管家AI助手页面");
            
            // 创建DifyFragment实例
            DifyFragment difyFragment = new DifyFragment();
            
            // 使用Fragment事务，将DifyFragment覆盖在当前页面上
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction()
                    .add(android.R.id.content, difyFragment) // 添加到根容器
                    .addToBackStack("DifyFragment") // 添加到回退栈
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,  // 进入动画
                        android.R.anim.slide_out_right, // 退出动画
                        android.R.anim.slide_in_left,   // 回退进入动画
                        android.R.anim.slide_out_right  // 回退退出动画
                    )
                    .commit();
                    
                Log.d(TAG, "✅ DifyFragment已作为新页面打开");
                Toast.makeText(getContext(), "🤖 湘湘管家AI助手", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 备用方案：使用Activity方式打开（如果有DifyActivity的话）
            if (getContext() != null) {
                try {
                    // 尝试启动DifyActivity（如果存在）
                    android.content.Intent intent = new android.content.Intent();
                    intent.setClassName(getContext(), "com.xiangjia.locallife.ui.activity.DifyActivity");
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    
                    Log.d(TAG, "✅ 使用Activity方式打开DifyFragment");
                    Toast.makeText(getContext(), "🤖 正在打开AI助手...", Toast.LENGTH_SHORT).show();
                    return;
                    
                } catch (Exception activityException) {
                    Log.w(TAG, "DifyActivity不存在，继续使用Fragment方式", activityException);
                }
            }
            
            Log.w(TAG, "⚠️ 无法获取Fragment管理器，跳转失败");
            Toast.makeText(getContext(), "❌ 无法打开AI助手页面", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 打开DifyFragment失败", e);
            Toast.makeText(getContext(), "❌ 打开失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 🎯 设置功能卡片 - 移除按钮处理
     */
    private void setupFunctionCards() {
        try {
            // 日常检查卡片 - 只保留整体点击
            if (cardDailyInspection != null) {
                cardDailyInspection.setOnClickListener(v -> startDailyInspection());
            }
            
            // 紧急送医卡片 - 只保留整体点击
            if (cardEmergencyMedical != null) {
                cardEmergencyMedical.setOnClickListener(v -> startEmergencyCall());
            }
            
            // 温馨提示卡片点击
            if (cardTips != null) {
                cardTips.setOnClickListener(v -> showTipsDetail());
            }
            
            Log.d(TAG, "🎯 功能卡片设置完成 - 移除了按钮处理");
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置功能卡片失败", e);
        }
    }
    
    /**
     * 🎯 设置服务列表 - 使用网格布局
     */
    private void setupServiceLists() {
        try {
            // 初始化数据
            maintenanceServices = new ArrayList<>();
            emergencyServices = new ArrayList<>();
            
            // 设置维修服务RecyclerView - 使用网格布局
            if (recyclerMaintenance != null) {
                androidx.recyclerview.widget.GridLayoutManager gridLayoutManager = 
                    new androidx.recyclerview.widget.GridLayoutManager(getContext(), 4); // 4列网格
                recyclerMaintenance.setLayoutManager(gridLayoutManager);
                recyclerMaintenance.setNestedScrollingEnabled(false);
                
                maintenanceAdapter = new MaintenanceAdapter(getContext(), maintenanceServices);
                recyclerMaintenance.setAdapter(maintenanceAdapter);
            }
            
            // 设置紧急服务RecyclerView - 使用网格布局
            if (recyclerEmergency != null) {
                androidx.recyclerview.widget.GridLayoutManager gridLayoutManager = 
                    new androidx.recyclerview.widget.GridLayoutManager(getContext(), 4); // 4列网格
                recyclerEmergency.setLayoutManager(gridLayoutManager);
                recyclerEmergency.setNestedScrollingEnabled(false);
                
                emergencyAdapter = new EmergencyAdapter(getContext(), emergencyServices);
                recyclerEmergency.setAdapter(emergencyAdapter);
            }
            
            Log.d(TAG, "🎯 服务网格布局设置完成");
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置服务列表失败", e);
        }
    }
    
    /**
     * 🎯 设置初始数据
     */
    private void setupInitialData() {
        try {
            // 设置欢迎文本
            if (tvWelcome != null) {
                tvWelcome.setText("欢迎回来！");
            }
            if (tvWelcomeSub != null) {
                tvWelcomeSub.setText("Welcome Back!");
            }
            if (tvManagerGreeting != null) {
                tvManagerGreeting.setText("我是你的湘湘橘子头管家！");
            }
            
            // 设置输入框提示
            if (etChatInput != null) {
                etChatInput.setHint("在这儿输入你的第一句话");
            }
            
            // 设置状态文本
            if (statusText != null) {
                statusText.setText("湘湘管家为您服务...");
            }
            
            // 加载服务数据
            loadServiceData();
            
            Log.d(TAG, "🎯 初始数据设置完成");
        } catch (Exception e) {
            Log.e(TAG, "❌ 设置初始数据失败", e);
        }
    }
    
    /**
     * 🎯 加载服务数据 - 使用系统图标，删除感叹号按钮
     */
    private void loadServiceData() {
        try {
            // 加载维修服务数据 - 使用系统图标，4个服务
            maintenanceServices.clear();
            maintenanceServices.add(new ServiceItem("水电维修", android.R.drawable.ic_menu_edit, "专业水电维修服务", this::openMaintenanceService));
            maintenanceServices.add(new ServiceItem("家电维修", android.R.drawable.ic_menu_preferences, "家用电器维修", this::openMaintenanceService));
            maintenanceServices.add(new ServiceItem("门锁维修", android.R.drawable.ic_lock_lock, "门锁安装维修", this::openMaintenanceService));
            maintenanceServices.add(new ServiceItem("清洁服务", android.R.drawable.ic_menu_delete, "专业清洁服务", this::openMaintenanceService));
            
            // 加载紧急服务数据 - 使用系统图标，删除感叹号，只保留3个服务
            emergencyServices.clear();
            emergencyServices.add(new ServiceItem("紧急维修", android.R.drawable.ic_menu_agenda, "24小时紧急维修", this::openEmergencyService));
            emergencyServices.add(new ServiceItem("医疗急救", android.R.drawable.ic_menu_call, "医疗急救服务", this::openEmergencyService));
            emergencyServices.add(new ServiceItem("火警报警", android.R.drawable.ic_dialog_info, "火警报警服务", this::openEmergencyService));
            // 删除了感叹号按钮 (安全报警)
            
            // 通知适配器数据变化
            if (maintenanceAdapter != null) {
                maintenanceAdapter.notifyDataSetChanged();
            }
            if (emergencyAdapter != null) {
                emergencyAdapter.notifyDataSetChanged();
            }
            
            Log.d(TAG, "🎯 服务数据加载完成 - 维修服务4个，紧急服务3个");
        } catch (Exception e) {
            Log.e(TAG, "❌ 加载服务数据失败", e);
        }
    }
    
    /**
     * 🎯 刷新数据
     */
    private void refreshData() {
        showLoading(true);
        
        // 模拟刷新延迟
        mainHandler.postDelayed(() -> {
            try {
                loadServiceData();
                
                if (statusText != null) {
                    statusText.setText("数据已更新 - " + new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                }
                
                showLoading(false);
                Toast.makeText(getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                
            } catch (Exception e) {
                Log.e(TAG, "❌ 刷新数据失败", e);
                showLoading(false);
                Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
        }, 1500);
    }
    
    /**
     * 🎯 显示/隐藏加载状态
     */
    private void showLoading(boolean show) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(show);
        }
        
        if (statusText != null && show) {
            statusText.setText("正在刷新数据...");
        }
        
        Log.d(TAG, show ? "🔄 显示加载状态" : "✅ 隐藏加载状态");
    }
    
    /**
     * 显示温馨提示详情
     */
    private void showTipsDetail() {
        try {
            Log.d(TAG, "💡 显示温馨提示详情");
            Toast.makeText(getContext(), "💡 查看更多社区公告和提示...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "❌ 显示提示详情失败", e);
        }
    }
    
    /**
     * 🎯 开始日常检查
     */
    private void startDailyInspection() {
        try {
            Log.d(TAG, "🔍 开始日常检查");
            Toast.makeText(getContext(), "🔍 启动日常检查功能", Toast.LENGTH_SHORT).show();
            // TODO: 启动日常检查Activity
            // Intent intent = new Intent(getActivity(), DailyInspectionActivity.class);
            // startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "❌ 启动日常检查失败", e);
        }
    }
    
    /**
     * 🎯 紧急呼叫
     */
    private void startEmergencyCall() {
        try {
            Log.d(TAG, "🚨 启动紧急呼叫");
            Toast.makeText(getContext(), "🚨 启动紧急救助功能", Toast.LENGTH_SHORT).show();
            // TODO: 启动紧急呼叫Activity
            // Intent intent = new Intent(getActivity(), EmergencyCallActivity.class);
            // startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "❌ 启动紧急呼叫失败", e);
        }
    }
    
    /**
     * 🎯 打开维修服务
     */
    private void openMaintenanceService() {
        try {
            Log.d(TAG, "🔧 打开维修服务");
            Toast.makeText(getContext(), "🔧 维修服务功能", Toast.LENGTH_SHORT).show();
            // TODO: 启动维修服务Activity
        } catch (Exception e) {
            Log.e(TAG, "❌ 打开维修服务失败", e);
        }
    }
    
    /**
     * 🎯 打开紧急服务
     */
    private void openEmergencyService() {
        try {
            Log.d(TAG, "🚨 打开紧急服务");
            Toast.makeText(getContext(), "🚨 紧急服务功能", Toast.LENGTH_SHORT).show();
            // TODO: 启动紧急服务Activity
        } catch (Exception e) {
            Log.e(TAG, "❌ 打开紧急服务失败", e);
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
        Log.d(TAG, "🎯 MainPageFragment onDestroyView");
        
        // 清理资源
        if (recyclerMaintenance != null) {
            recyclerMaintenance.setAdapter(null);
        }
        if (recyclerEmergency != null) {
            recyclerEmergency.setAdapter(null);
        }
        
        Log.d(TAG, "🎯 MainPageFragment destroyed, 资源已清理");
    }
    
    /**
     * 🎯 服务项数据类
     */
    public static class ServiceItem {
        private final String name;
        private final int iconRes;
        private final String description;
        private final Runnable action;
        
        public ServiceItem(String name, int iconRes, String description, Runnable action) {
            this.name = name;
            this.iconRes = iconRes;
            this.description = description;
            this.action = action;
        }
        
        public String getName() {
            return name;
        }
        
        public int getIconRes() {
            return iconRes;
        }
        
        public String getDescription() {
            return description;
        }
        
        public Runnable getAction() {
            return action;
        }
    }
    
    /**
     * 🎯 维修服务适配器 - 网格布局版本
     */
    private static class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.ViewHolder> {
        private final android.content.Context context;
        private final List<ServiceItem> serviceList;
        
        public MaintenanceAdapter(android.content.Context context, List<ServiceItem> serviceList) {
            this.context = context;
            this.serviceList = serviceList;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 创建服务卡片视图 - 适配网格布局
            LinearLayout itemView = new LinearLayout(context);
            itemView.setOrientation(LinearLayout.VERTICAL);
            itemView.setPadding(12, 12, 12, 12);
            itemView.setGravity(android.view.Gravity.CENTER);
            
            // 设置卡片样式 - 自适应宽度
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            
            // 添加背景和圆角效果
            try {
                android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
                background.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                background.setCornerRadius(12f);
                background.setColor(0x20484d61); // 半透明蓝色背景
                itemView.setBackground(background);
            } catch (Exception e) {
                // 如果设置背景失败，使用简单颜色
                itemView.setBackgroundColor(0x10484d61);
            }
            
            return new ViewHolder(itemView);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ServiceItem service = serviceList.get(position);
            
            // 创建图标
            ImageView iconView = new ImageView(context);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(36, 36);
            iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
            iconView.setLayoutParams(iconParams);
            iconView.setImageResource(service.getIconRes());
            iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            
            // 创建文本
            TextView nameView = new TextView(context);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.topMargin = 8;
            nameView.setLayoutParams(textParams);
            nameView.setText(service.getName());
            nameView.setTextSize(11);
            nameView.setGravity(android.view.Gravity.CENTER);
            nameView.setTextColor(0xFF484d61);
            nameView.setMaxLines(2);
            nameView.setEllipsize(android.text.TextUtils.TruncateAt.END);
            
            // 清空并添加视图
            LinearLayout container = (LinearLayout) holder.itemView;
            container.removeAllViews();
            container.addView(iconView);
            container.addView(nameView);
            
            // 设置点击监听
            holder.itemView.setOnClickListener(v -> {
                if (service.getAction() != null) {
                    service.getAction().run();
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return serviceList != null ? serviceList.size() : 0;
        }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
    
    /**
     * 🎯 紧急服务适配器 - 网格布局版本
     */
    private static class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {
        private final android.content.Context context;
        private final List<ServiceItem> serviceList;
        
        public EmergencyAdapter(android.content.Context context, List<ServiceItem> serviceList) {
            this.context = context;
            this.serviceList = serviceList;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 创建服务卡片视图 - 适配网格布局
            LinearLayout itemView = new LinearLayout(context);
            itemView.setOrientation(LinearLayout.VERTICAL);
            itemView.setPadding(12, 12, 12, 12);
            itemView.setGravity(android.view.Gravity.CENTER);
            
            // 设置卡片样式 - 自适应宽度
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            
            // 添加背景和圆角效果
            try {
                android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
                background.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                background.setCornerRadius(12f);
                background.setColor(0x20FF6B6B); // 半透明红色背景 (紧急服务)
                itemView.setBackground(background);
            } catch (Exception e) {
                // 如果设置背景失败，使用简单颜色
                itemView.setBackgroundColor(0x10FF6B6B);
            }
            
            return new ViewHolder(itemView);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ServiceItem service = serviceList.get(position);
            
            // 创建图标
            ImageView iconView = new ImageView(context);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(36, 36);
            iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
            iconView.setLayoutParams(iconParams);
            iconView.setImageResource(service.getIconRes());
            iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            
            // 创建文本
            TextView nameView = new TextView(context);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.topMargin = 8;
            nameView.setLayoutParams(textParams);
            nameView.setText(service.getName());
            nameView.setTextSize(11);
            nameView.setGravity(android.view.Gravity.CENTER);
            nameView.setTextColor(0xFF484d61);
            nameView.setMaxLines(2);
            nameView.setEllipsize(android.text.TextUtils.TruncateAt.END);
            
            // 清空并添加视图
            LinearLayout container = (LinearLayout) holder.itemView;
            container.removeAllViews();
            container.addView(iconView);
            container.addView(nameView);
            
            // 设置点击监听
            holder.itemView.setOnClickListener(v -> {
                if (service.getAction() != null) {
                    service.getAction().run();
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return serviceList != null ? serviceList.size() : 0;
        }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}