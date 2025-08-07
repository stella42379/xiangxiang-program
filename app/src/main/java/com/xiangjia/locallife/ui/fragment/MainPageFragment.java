package com.xiangjia.locallife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiangjia.locallife.MainActivity;

/**
 * 主页Fragment - 完整功能版本
 * 对应小程序的mainPage，显示所有主要功能入口
 */
public class MainPageFragment extends Fragment implements MainActivity.RefreshableFragment {
    
    private LinearLayout mainContainer;
    private TextView greetingText;
    private LinearLayout serviceContainer;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 创建完整的首页布局
        return createMainPageLayout();
    }
    
    /**
     * 创建主页布局
     */
    private View createMainPageLayout() {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setBackgroundColor(0xFFF8FAFC);
        
        mainContainer = new LinearLayout(getContext());
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(20, 30, 20, 30);
        
        // 欢迎区域
        createGreetingSection();
        
        // 快捷服务区域
        createQuickServiceSection();
        
        // 维护服务区域
        createMaintenanceServiceSection();
        
        // 紧急服务区域
        createEmergencyServiceSection();
        
        scrollView.addView(mainContainer);
        return scrollView;
    }
    
    /**
     * 创建欢迎区域
     */
    private void createGreetingSection() {
        // 欢迎卡片
        LinearLayout greetingCard = createCard();
        greetingCard.setBackgroundColor(0xFF6366F1); // 蓝色背景
        
        greetingText = new TextView(getContext());
        greetingText.setText("👋 欢迎使用湘湘管家\n您的智能生活助手");
        greetingText.setTextSize(18);
        greetingText.setTextColor(0xFFFFFFFF);
        greetingText.setGravity(android.view.Gravity.CENTER);
        greetingText.setPadding(20, 30, 20, 30);
        
        greetingCard.addView(greetingText);
        mainContainer.addView(greetingCard);
        
        // 添加间距
        addSpacing(20);
    }
    
    /**
     * 创建快捷服务区域
     */
    private void createQuickServiceSection() {
        // 标题
        TextView sectionTitle = new TextView(getContext());
        sectionTitle.setText("🚀 快捷服务");
        sectionTitle.setTextSize(20);
        sectionTitle.setTextColor(0xFF1F2937);
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        sectionTitle.setPadding(0, 0, 0, 16);
        mainContainer.addView(sectionTitle);
        
        // 服务网格
        LinearLayout serviceGrid = new LinearLayout(getContext());
        serviceGrid.setOrientation(LinearLayout.VERTICAL);
        
        // 第一行：故障报修、日常检查
        LinearLayout row1 = new LinearLayout(getContext());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(createServiceButton("🔧 故障报修", "快速报修设施故障", () -> {
            navigateToRepair();
        }));
        addHorizontalSpacing(row1, 12);
        row1.addView(createServiceButton("📋 日常检查", "设施日常检查", () -> {
            navigateToInspection();
        }));
        serviceGrid.addView(row1);
        
        addSpacing(serviceGrid, 12);
        
        // 第二行：进度追踪、停水停电
        LinearLayout row2 = new LinearLayout(getContext());
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(createServiceButton("📊 进度追踪", "查看服务进度", () -> {
            Toast.makeText(getContext(), "进度追踪功能开发中", Toast.LENGTH_SHORT).show();
        }));
        addHorizontalSpacing(row2, 12);
        row2.addView(createServiceButton("⚡ 停水停电", "停水停电报告", () -> {
            Toast.makeText(getContext(), "停水停电功能开发中", Toast.LENGTH_SHORT).show();
        }));
        serviceGrid.addView(row2);
        
        mainContainer.addView(serviceGrid);
        addSpacing(20);
    }
    
    /**
     * 创建维护服务区域
     */
    private void createMaintenanceServiceSection() {
        TextView sectionTitle = new TextView(getContext());
        sectionTitle.setText("🛠️ 维护服务");
        sectionTitle.setTextSize(20);
        sectionTitle.setTextColor(0xFF1F2937);
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        sectionTitle.setPadding(0, 0, 0, 16);
        mainContainer.addView(sectionTitle);
        
        // 维护服务列表
        LinearLayout maintenanceList = new LinearLayout(getContext());
        maintenanceList.setOrientation(LinearLayout.VERTICAL);
        
        maintenanceList.addView(createListServiceButton("🌤️ 天气预警", "实时天气信息和预警提醒", () -> {
            navigateToWeather();
        }));
        
        addSpacing(maintenanceList, 8);
        
        maintenanceList.addView(createListServiceButton("🔧 设备维护", "设备维护预约和管理", () -> {
            Toast.makeText(getContext(), "设备维护功能开发中", Toast.LENGTH_SHORT).show();
        }));
        
        mainContainer.addView(maintenanceList);
        addSpacing(20);
    }
    
    /**
     * 创建紧急服务区域
     */
    private void createEmergencyServiceSection() {
        TextView sectionTitle = new TextView(getContext());
        sectionTitle.setText("🚨 紧急服务");
        sectionTitle.setTextSize(20);
        sectionTitle.setTextColor(0xFF1F2937);
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        sectionTitle.setPadding(0, 0, 0, 16);
        mainContainer.addView(sectionTitle);
        
        // 紧急服务卡片
        LinearLayout emergencyCard = createCard();
        emergencyCard.setBackgroundColor(0xFFEF4444); // 红色背景
        
        LinearLayout emergencyList = new LinearLayout(getContext());
        emergencyList.setOrientation(LinearLayout.VERTICAL);
        emergencyList.setPadding(20, 20, 20, 20);
        
        Button medicalButton = new Button(getContext());
        medicalButton.setText("🚑 紧急送医");
        medicalButton.setTextSize(16);
        medicalButton.setTextColor(0xFFFFFFFF);
        medicalButton.setBackgroundColor(0xFFDC2626);
        medicalButton.setPadding(20, 15, 20, 15);
        medicalButton.setOnClickListener(v -> navigateToMedical());
        emergencyList.addView(medicalButton);
        
        addSpacing(emergencyList, 12);
        
        Button hospitalButton = new Button(getContext());
        hospitalButton.setText("🏥 附近医院");
        hospitalButton.setTextSize(16);
        hospitalButton.setTextColor(0xFFFFFFFF);
        hospitalButton.setBackgroundColor(0xFFDC2626);
        hospitalButton.setPadding(20, 15, 20, 15);
        hospitalButton.setOnClickListener(v -> navigateToHospital());
        emergencyList.addView(hospitalButton);
        
        emergencyCard.addView(emergencyList);
        mainContainer.addView(emergencyCard);
    }
    
    /**
     * 创建卡片容器
     */
    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFFFFFFFF);
        // 简单的圆角效果
        card.setPadding(2, 2, 2, 2);
        
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);
        
        return card;
    }
    
    /**
     * 创建服务按钮（网格样式）
     */
    private View createServiceButton(String title, String description, Runnable action) {
        LinearLayout button = new LinearLayout(getContext());
        button.setOrientation(LinearLayout.VERTICAL);
        button.setBackgroundColor(0xFFFFFFFF);
        button.setPadding(20, 24, 20, 24);
        button.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.weight = 1;
        button.setLayoutParams(buttonParams);
        
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setTextColor(0xFF1F2937);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setGravity(android.view.Gravity.CENTER);
        button.addView(titleView);
        
        TextView descView = new TextView(getContext());
        descView.setText(description);
        descView.setTextSize(12);
        descView.setTextColor(0xFF6B7280);
        descView.setGravity(android.view.Gravity.CENTER);
        descView.setPadding(0, 8, 0, 0);
        button.addView(descView);
        
        button.setOnClickListener(v -> {
            if (action != null) action.run();
        });
        
        return button;
    }
    
    /**
     * 创建列表式服务按钮
     */
    private View createListServiceButton(String title, String description, Runnable action) {
        LinearLayout button = new LinearLayout(getContext());
        button.setOrientation(LinearLayout.VERTICAL);
        button.setBackgroundColor(0xFFFFFFFF);
        button.setPadding(24, 20, 24, 20);
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        button.setLayoutParams(buttonParams);
        
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(18);
        titleView.setTextColor(0xFF1F2937);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        button.addView(titleView);
        
        TextView descView = new TextView(getContext());
        descView.setText(description);
        descView.setTextSize(14);
        descView.setTextColor(0xFF6B7280);
        descView.setPadding(0, 8, 0, 0);
        button.addView(descView);
        
        button.setOnClickListener(v -> {
            if (action != null) action.run();
        });
        
        return button;
    }
    
    /**
     * 添加垂直间距
     */
    private void addSpacing(int dp) {
        addSpacing(mainContainer, dp);
    }
    
    private void addSpacing(LinearLayout parent, int dp) {
        View space = new View(getContext());
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp
        );
        space.setLayoutParams(spaceParams);
        parent.addView(space);
    }
    
    /**
     * 添加水平间距
     */
    private void addHorizontalSpacing(LinearLayout parent, int dp) {
        View space = new View(getContext());
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(dp, 
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        space.setLayoutParams(spaceParams);
        parent.addView(space);
    }
    
    // ========== 导航方法 ==========
    
    /**
     * 跳转到故障报修
     */
    private void navigateToRepair() {
        Toast.makeText(getContext(), "正在打开故障报修功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现跳转
        // Intent intent = new Intent(getActivity(), RepairActivity.class);
        // startActivity(intent);
    }
    
    /**
     * 跳转到日常检查
     */
    private void navigateToInspection() {
        Toast.makeText(getContext(), "正在打开日常检查功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现跳转
        // Intent intent = new Intent(getActivity(), InspectionActivity.class);
        // startActivity(intent);
    }
    
    /**
     * 跳转到天气预警
     */
    private void navigateToWeather() {
        Toast.makeText(getContext(), "正在打开天气预警功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现跳转
        // Intent intent = new Intent(getActivity(), WeatherActivity.class);
        // startActivity(intent);
    }
    
    /**
     * 跳转到紧急送医
     */
    private void navigateToMedical() {
        Toast.makeText(getContext(), "正在打开紧急送医功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现跳转
        // Intent intent = new Intent(getActivity(), MedicalActivity.class);
        // startActivity(intent);
    }
    
    /**
     * 跳转到附近医院
     */
    private void navigateToHospital() {
        Toast.makeText(getContext(), "正在打开附近医院功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现跳转
        // Intent intent = new Intent(getActivity(), HospitalActivity.class);
        // startActivity(intent);
    }
    
    @Override
    public void onRefresh() {
        // 刷新首页数据
        updateGreeting();
        // TODO: 刷新其他数据
    }
    
    /**
     * 更新欢迎信息
     */
    private void updateGreeting() {
        if (greetingText != null) {
            // 根据时间显示不同的问候语
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
            
            String greeting;
            if (hour < 6) {
                greeting = "🌙 夜深了，注意休息";
            } else if (hour < 12) {
                greeting = "🌅 早上好";
            } else if (hour < 14) {
                greeting = "☀️ 中午好";
            } else if (hour < 18) {
                greeting = "🌤️ 下午好";
            } else {
                greeting = "🌆 晚上好";
            }
            
            greetingText.setText(greeting + "\n欢迎使用湘湘管家");
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 每次显示时更新问候语
        updateGreeting();
    }
    
    // ========== ServiceItem数据类 ==========
    
    /**
     * 服务项数据类
     * 供Adapter使用，保持与旧版本的兼容性
     */
    public static class ServiceItem {
        private String name;
        private int iconRes;
        private String description;
        private Runnable action;

        public ServiceItem(String name, int iconRes, String description, Runnable action) {
            this.name = name;
            this.iconRes = iconRes;
            this.description = description;
            this.action = action;
        }

        // Getters
        public String getName() { return name; }
        public int getIconRes() { return iconRes; }
        public String getDescription() { return description; }
        public Runnable getAction() { return action; }
        
        // Setters  
        public void setName(String name) { this.name = name; }
        public void setIconRes(int iconRes) { this.iconRes = iconRes; }
        public void setDescription(String description) { this.description = description; }
        public void setAction(Runnable action) { this.action = action; }
    }
}