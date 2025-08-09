package com.xiangjia.locallife.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * 湘湘管家主页Fragment - 完全复刻小程序mainPage效果
 * 注意：资源文件必须放在 res/values/ 目录下，不能放在子目录
 */
public class MainPageFragment extends Fragment {
    
    private static final String TAG = "MainPageFragment";
    
    private ScrollView scrollView;
    private LinearLayout mainContainer;
    private CardView chatContainer;
    private EditText inputMessage;
    private View sendButton;
    private String currentMessage = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "创建湘湘管家主页");
        
        try {
            View rootView = createMainPageLayout();
            setupAnimations();
            return rootView;
        } catch (Exception e) {
            Log.e(TAG, "创建主页失败", e);
            return createErrorView();
        }
    }
    
    /**
     * 创建主页布局 - 完全复刻小程序样式
     */
    private View createMainPageLayout() {
        // 根容器 - 对应小程序的 .mainpage
        scrollView = new ScrollView(getContext());
        scrollView.setBackgroundColor(Color.WHITE);
        scrollView.setFillViewport(true);
        
        mainContainer = new LinearLayout(getContext());
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(0, 0, 0, dp(20));
        
        // 1. 顶部欢迎区域 - 对应 .seal_group
        createWelcomeSection();
        
        // 2. 湘湘管家聊天区域 - 对应 .seal_group_1  
        createChatSection();
        
        // 3. 主要业务标题 - 对应 .signup_2
        createBusinessTitleSection();
        
        // 4. 主要功能区域 - 对应 .seal_group_3
        createMainServicesSection();
        
        // 5. 底部文本
        createBottomText();
        
        scrollView.addView(mainContainer);
        return scrollView;
    }
    
    /**
     * 1. 创建顶部欢迎区域 - 对应小程序的渐变背景头部
     */
    private void createWelcomeSection() {
        LinearLayout welcomeSection = new LinearLayout(getContext());
        welcomeSection.setOrientation(LinearLayout.HORIZONTAL);
        
        // 设置渐变背景，使用硬编码颜色（避免资源文件问题）
        GradientDrawable gradient = new GradientDrawable();
        gradient.setOrientation(GradientDrawable.Orientation.TL_BR);
        gradient.setColors(new int[]{
            Color.parseColor("#6366F1"), // 蓝紫色
            Color.parseColor("#8B5CF6"), // 紫色  
            Color.parseColor("#EC4899")  // 粉色
        });
        gradient.setCornerRadius(0);
        welcomeSection.setBackground(gradient);
        
        LinearLayout.LayoutParams welcomeParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(200)
        );
        welcomeSection.setLayoutParams(welcomeParams);
        welcomeSection.setPadding(dp(30), dp(40), dp(30), dp(20));
        
        // 文字区域 - 对应 .signup
        LinearLayout textArea = new LinearLayout(getContext());
        textArea.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        textArea.setLayoutParams(textParams);
        
        TextView welcomeText = new TextView(getContext());
        welcomeText.setText("欢迎回来！");
        welcomeText.setTextSize(24);
        welcomeText.setTextColor(Color.parseColor("#041830"));
        welcomeText.setTypeface(null, android.graphics.Typeface.BOLD);
        textArea.addView(welcomeText);
        
        TextView subText = new TextView(getContext());
        subText.setText("Welcome Back!");
        subText.setTextSize(16);
        subText.setTextColor(Color.parseColor("#041830"));
        subText.setPadding(0, dp(8), 0, 0);
        textArea.addView(subText);
        
        welcomeSection.addView(textArea);
        mainContainer.addView(welcomeSection);
    }
    
    /**
     * 2. 创建湘湘管家聊天区域 - 核心功能
     */
    private void createChatSection() {
        // 聊天容器 - 对应 .seal_group_1，具有毛玻璃效果
        chatContainer = new CardView(getContext());
        chatContainer.setRadius(dp(20));
        chatContainer.setCardElevation(dp(12));
        chatContainer.setCardBackgroundColor(Color.parseColor("#1AFFFFFF")); // 10%透明白色
        
        LinearLayout.LayoutParams chatParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        chatParams.setMargins(dp(15), dp(25), dp(15), 0);
        chatContainer.setLayoutParams(chatParams);
        
        LinearLayout chatContent = new LinearLayout(getContext());
        chatContent.setOrientation(LinearLayout.VERTICAL);
        chatContent.setPadding(dp(20), dp(25), dp(20), dp(25));
        
        // 第一条AI消息 - 对应 .flexcontainer
        createAIMessage(chatContent, "湘湘管家", "我是你的湘湘橘子头管家！");
        
        addSpacing(chatContent, 15);
        
        // 第二条AI消息 - 对应 .flexcontainer_1  
        createAIMessage(chatContent, "湘湘管家", "请问有什么需要帮助的吗？");
        
        addSpacing(chatContent, 20);
        
        // 输入区域 - 对应 .input_and_button_row
        createInputSection(chatContent);
        
        chatContainer.addView(chatContent);
        
        // 设置点击事件跳转到聊天页面
        chatContainer.setOnClickListener(v -> goToChat());
        
        mainContainer.addView(chatContainer);
    }
    
    /**
     * 创建AI消息气泡
     */
    private void createAIMessage(LinearLayout parent, String name, String message) {
        LinearLayout messageRow = new LinearLayout(getContext());
        messageRow.setOrientation(LinearLayout.HORIZONTAL);
        messageRow.setPadding(0, dp(8), 0, dp(8));
        
        // AI头像
        ImageView avatar = new ImageView(getContext());
        avatar.setImageResource(android.R.drawable.ic_menu_gallery); // 临时图标
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(45), dp(45));
        avatarParams.setMargins(0, 0, dp(12), 0);
        avatar.setLayoutParams(avatarParams);
        
        // 设置圆形头像
        GradientDrawable avatarBg = new GradientDrawable();
        avatarBg.setShape(GradientDrawable.OVAL);
        avatarBg.setColor(Color.parseColor("#E0E7FF"));
        avatar.setBackground(avatarBg);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // 消息气泡容器
        CardView messageBubble = new CardView(getContext());
        messageBubble.setRadius(dp(15));
        messageBubble.setCardElevation(dp(4));
        messageBubble.setCardBackgroundColor(Color.parseColor("#4DFFFFFF")); // 30%透明白色
        
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        messageBubble.setLayoutParams(bubbleParams);
        
        LinearLayout bubbleContent = new LinearLayout(getContext());
        bubbleContent.setOrientation(LinearLayout.VERTICAL);
        bubbleContent.setPadding(dp(15), dp(12), dp(15), dp(12));
        
        // 用户名
        TextView nameText = new TextView(getContext());
        nameText.setText(name);
        nameText.setTextSize(14);
        nameText.setTextColor(Color.parseColor("#484D61"));
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // 消息内容
        TextView messageText = new TextView(getContext());
        messageText.setText(message);
        messageText.setTextSize(13);
        messageText.setTextColor(Color.parseColor("#484D61"));
        messageText.setPadding(0, dp(4), 0, 0);
        
        bubbleContent.addView(nameText);
        bubbleContent.addView(messageText);
        messageBubble.addView(bubbleContent);
        
        messageRow.addView(avatar);
        messageRow.addView(messageBubble);
        parent.addView(messageRow);
    }
    
    /**
     * 创建输入区域 - 对应小程序的输入框和发送按钮
     */
    private void createInputSection(LinearLayout parent) {
        LinearLayout inputRow = new LinearLayout(getContext());
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        inputRow.setPadding(dp(10), 0, dp(10), 0);
        
        // 输入框容器
        CardView inputCard = new CardView(getContext());
        inputCard.setRadius(dp(20));
        inputCard.setCardElevation(dp(6));
        inputCard.setCardBackgroundColor(Color.parseColor("#33FFFFFF")); // 20%透明白色
        
        LinearLayout.LayoutParams inputCardParams = new LinearLayout.LayoutParams(
            0, dp(50), 1f
        );
        inputCardParams.setMargins(0, 0, dp(12), 0);
        inputCard.setLayoutParams(inputCardParams);
        
        inputMessage = new EditText(getContext());
        inputMessage.setHint("有什么可以帮助你的吗？");
        inputMessage.setTextSize(14);
        inputMessage.setTextColor(Color.parseColor("#484D61"));
        inputMessage.setHintTextColor(Color.parseColor("#9CA3AF"));
        inputMessage.setBackground(null);
        inputMessage.setPadding(dp(20), dp(12), dp(20), dp(12));
        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentMessage = s.toString();
                updateSendButtonState();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        inputCard.addView(inputMessage);
        
        // 发送按钮 - 圆形毛玻璃效果
        sendButton = new View(getContext()) {
            @Override
            protected void onDraw(android.graphics.Canvas canvas) {
                super.onDraw(canvas);
                // 绘制发送图标（简单的箭头）
                android.graphics.Paint paint = new android.graphics.Paint();
                paint.setColor(Color.parseColor("#6366F1"));
                paint.setStrokeWidth(6);
                paint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int size = Math.min(getWidth(), getHeight()) / 4;
                
                // 绘制简单箭头
                canvas.drawLine(centerX - size, centerY, centerX + size, centerY, paint);
                canvas.drawLine(centerX + size - size/2, centerY - size/2, centerX + size, centerY, paint);
                canvas.drawLine(centerX + size - size/2, centerY + size/2, centerX + size, centerY, paint);
            }
        };
        
        LinearLayout.LayoutParams sendParams = new LinearLayout.LayoutParams(dp(50), dp(50));
        sendButton.setLayoutParams(sendParams);
        
        // 发送按钮毛玻璃背景
        GradientDrawable sendBg = new GradientDrawable();
        sendBg.setShape(GradientDrawable.OVAL);
        sendBg.setColor(Color.parseColor("#40FFFFFF")); // 25%透明白色
        sendBg.setStroke(dp(1), Color.parseColor("#66FFFFFF")); // 40%透明白色边框
        sendButton.setBackground(sendBg);
        sendButton.setElevation(dp(8));
        
        sendButton.setOnClickListener(v -> onSendClick());
        
        inputRow.addView(inputCard);
        inputRow.addView(sendButton);
        parent.addView(inputRow);
    }
    
    /**
     * 3. 创建主要业务标题区域
     */
    private void createBusinessTitleSection() {
        addSpacing(20);
        
        LinearLayout titleSection = new LinearLayout(getContext());
        titleSection.setOrientation(LinearLayout.VERTICAL);
        titleSection.setPadding(dp(30), 0, dp(30), 0);
        
        TextView mainTitle = new TextView(getContext());
        mainTitle.setText("主要业务");
        mainTitle.setTextSize(20);
        mainTitle.setTextColor(Color.parseColor("#1B1F26"));
        mainTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView subTitle = new TextView(getContext());
        subTitle.setText("主要的一些功能");
        subTitle.setTextSize(16);
        subTitle.setTextColor(Color.parseColor("#1B1F26"));
        subTitle.setPadding(0, dp(8), 0, 0);
        subTitle.setAlpha(0.72f);
        
        titleSection.addView(mainTitle);
        titleSection.addView(subTitle);
        mainContainer.addView(titleSection);
    }
    
    /**
     * 4. 创建主要功能服务区域 - 对应小程序的 .seal_group_3
     */
    private void createMainServicesSection() {
        addSpacing(15);
        
        // 主要功能容器 - 毛玻璃效果
        CardView servicesContainer = new CardView(getContext());
        servicesContainer.setRadius(dp(20));
        servicesContainer.setCardElevation(dp(8));
        servicesContainer.setCardBackgroundColor(Color.parseColor("#05FFFFFF")); // 2%透明白色
        
        LinearLayout.LayoutParams servicesParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        servicesParams.setMargins(dp(15), 0, dp(15), 0);
        servicesContainer.setLayoutParams(servicesParams);
        
        LinearLayout servicesContent = new LinearLayout(getContext());
        servicesContent.setOrientation(LinearLayout.VERTICAL);
        servicesContent.setPadding(dp(20), dp(25), dp(20), dp(25));
        
        // 维修服务区域标题
        createSectionTitle(servicesContent, "维修服务", "Maintenance Services");
        
        // 维修服务按钮 - 对应 maintenanceSwiper
        createMaintenanceServices(servicesContent);
        
        addSpacing(servicesContent, 25);
        
        // 紧急服务区域标题  
        createSectionTitle(servicesContent, "紧急服务", "Emergency Services");
        
        // 紧急服务按钮 - 对应 emergencySwiper
        createEmergencyServices(servicesContent);
        
        servicesContainer.addView(servicesContent);
        mainContainer.addView(servicesContainer);
    }
    
    /**
     * 创建区域标题
     */
    private void createSectionTitle(LinearLayout parent, String mainTitle, String subTitle) {
        LinearLayout titleContainer = new LinearLayout(getContext());
        titleContainer.setOrientation(LinearLayout.VERTICAL);
        titleContainer.setGravity(android.view.Gravity.CENTER);
        titleContainer.setPadding(0, 0, 0, dp(15));
        
        TextView mainTitleView = new TextView(getContext());
        mainTitleView.setText(mainTitle);
        mainTitleView.setTextSize(18);
        mainTitleView.setTextColor(Color.parseColor("#F5F5F5")); // 95%白色
        mainTitleView.setTypeface(null, android.graphics.Typeface.BOLD);
        mainTitleView.setGravity(android.view.Gravity.CENTER);
        mainTitleView.setShadowLayer(4, 0, 2, Color.parseColor("#30000000"));
        
        TextView subTitleView = new TextView(getContext());
        subTitleView.setText(subTitle);
        subTitleView.setTextSize(13);
        subTitleView.setTextColor(Color.parseColor("#CCFFFFFF")); // 80%透明白色
        subTitleView.setGravity(android.view.Gravity.CENTER);
        subTitleView.setPadding(0, dp(4), 0, 0);
        subTitleView.setShadowLayer(2, 0, 1, Color.parseColor("#20000000"));
        
        titleContainer.addView(mainTitleView);
        titleContainer.addView(subTitleView);
        parent.addView(titleContainer);
    }
    
    /**
     * 创建维修服务按钮 - 对应 maintenanceSwiper
     */
    private void createMaintenanceServices(LinearLayout parent) {
        LinearLayout servicesGrid = new LinearLayout(getContext());
        servicesGrid.setOrientation(LinearLayout.VERTICAL);
        
        // 第一行
        LinearLayout row1 = new LinearLayout(getContext());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setWeightSum(2f);
        
        createServiceButton(row1, "🔧", "智能报修", "设备故障快速报修", () -> navigateToRepair());
        addHorizontalSpacing(row1, 12);
        createServiceButton(row1, "🏠", "日常检查", "定期安全检查", () -> navigateToInspection());
        
        // 第二行
        LinearLayout row2 = new LinearLayout(getContext());
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setWeightSum(2f);
        
        createServiceButton(row2, "📋", "提交工单", "提交服务请求", () -> navigateToWorkOrder());
        addHorizontalSpacing(row2, 12);
        createServiceButton(row2, "📊", "追踪进度", "查看工单状态", () -> navigateToProgress());
        
        servicesGrid.addView(row1);
        addSpacing(servicesGrid, 12);
        servicesGrid.addView(row2);
        
        parent.addView(servicesGrid);
    }
    
    /**
     * 创建紧急服务按钮 - 对应 emergencySwiper
     */
    private void createEmergencyServices(LinearLayout parent) {
        LinearLayout servicesGrid = new LinearLayout(getContext());
        servicesGrid.setOrientation(LinearLayout.VERTICAL);
        
        // 第一行
        LinearLayout row1 = new LinearLayout(getContext());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setWeightSum(2f);
        
        createServiceButton(row1, "🚑", "帮我送医", "紧急医疗救助", () -> navigateToMedical());
        addHorizontalSpacing(row1, 12);
        createServiceButton(row1, "🏥", "附近医院", "寻找最近医院", () -> navigateToHospital());
        
        // 第二行
        LinearLayout row2 = new LinearLayout(getContext());
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setWeightSum(2f);
        
        createServiceButton(row2, "⚡", "停水停电", "公用设施报障", () -> navigateToUtilities());
        addHorizontalSpacing(row2, 12);
        createServiceButton(row2, "🌤️", "天气预警", "查看天气信息", () -> navigateToWeather());
        
        servicesGrid.addView(row1);
        addSpacing(servicesGrid, 12);
        servicesGrid.addView(row2);
        
        parent.addView(servicesGrid);
    }
    
    /**
     * 创建服务按钮 - 毛玻璃卡片效果
     */
    private void createServiceButton(LinearLayout parent, String icon, String title, String description, Runnable action) {
        CardView button = new CardView(getContext());
        button.setRadius(dp(16));
        button.setCardElevation(dp(6));
        button.setCardBackgroundColor(Color.parseColor("#26FFFFFF")); // 透明白色
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            0, dp(90), 1f
        );
        button.setLayoutParams(buttonParams);
        
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(android.view.Gravity.CENTER);
        content.setPadding(dp(12), dp(15), dp(12), dp(15));
        
        // 图标
        TextView iconView = new TextView(getContext());
        iconView.setText(icon);
        iconView.setTextSize(20);
        iconView.setGravity(android.view.Gravity.CENTER);
        
        // 标题
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(14);
        titleView.setTextColor(Color.parseColor("#1F2937"));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setGravity(android.view.Gravity.CENTER);
        titleView.setPadding(0, dp(4), 0, 0);
        
        // 描述
        TextView descView = new TextView(getContext());
        descView.setText(description);
        descView.setTextSize(11);
        descView.setTextColor(Color.parseColor("#6B7280"));
        descView.setGravity(android.view.Gravity.CENTER);
        descView.setPadding(0, dp(2), 0, 0);
        
        content.addView(iconView);
        content.addView(titleView);
        content.addView(descView);
        button.addView(content);
        
        // 点击效果
        button.setOnClickListener(v -> {
            // 点击动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0.95f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 0.95f, 1f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(scaleX, scaleY);
            animSet.setDuration(150);
            animSet.start();
            
            // 延迟执行动作
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (action != null) action.run();
            }, 100);
        });
        
        parent.addView(button);
    }
    
    /**
     * 5. 创建底部文本
     */
    private void createBottomText() {
        addSpacing(25);
        
        TextView bottomText = new TextView(getContext());
        bottomText.setText("下面没有了~");
        bottomText.setTextSize(14);
        bottomText.setTextColor(Color.parseColor("#1B1F26"));
        bottomText.setAlpha(0.5f);
        bottomText.setGravity(android.view.Gravity.CENTER);
        bottomText.setPadding(0, dp(20), 0, dp(15));
        
        mainContainer.addView(bottomText);
    }
    
    /**
     * 设置入场动画 - 复刻小程序的动画效果
     */
    private void setupAnimations() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getView() != null) {
                // 1. 欢迎区域滑入动画 (对应 headerSlideDown)
                animateWelcomeSection();
                
                // 2. 聊天容器缩放进入 (对应 scaleIn) 
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    animateChatSection();
                }, 200);
                
                // 3. 功能区域淡入 (对应 fadeInUp)
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    animateServicesSection();
                }, 600);
                
                // 4. 启动持续的浮动动画
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startFloatingAnimation();
                }, 1500);
            }
        }, 100);
    }
    
    /**
     * 欢迎区域动画
     */
    private void animateWelcomeSection() {
        View welcomeSection = mainContainer.getChildAt(0);
        if (welcomeSection != null) {
            welcomeSection.setAlpha(0f);
            welcomeSection.setTranslationY(-dp(30));
            
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(welcomeSection, "alpha", 0f, 1f);
            ObjectAnimator slideDown = ObjectAnimator.ofFloat(welcomeSection, "translationY", -dp(30), 0f);
            
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(fadeIn, slideDown);
            animSet.setDuration(600);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.start();
        }
    }
    
    /**
     * 聊天区域动画
     */
    private void animateChatSection() {
        if (chatContainer != null) {
            chatContainer.setAlpha(0f);
            chatContainer.setScaleX(0.8f);
            chatContainer.setScaleY(0.8f);
            
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(chatContainer, "alpha", 0f, 1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(chatContainer, "scaleX", 0.8f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(chatContainer, "scaleY", 0.8f, 1f);
            
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(fadeIn, scaleX, scaleY);
            animSet.setDuration(600);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.start();
        }
    }
    
    /**
     * 功能区域动画
     */
    private void animateServicesSection() {
        // 找到功能区域容器
        for (int i = 0; i < mainContainer.getChildCount(); i++) {
            View child = mainContainer.getChildAt(i);
            if (child instanceof CardView && child != chatContainer) {
                child.setAlpha(0f);
                child.setTranslationY(dp(30));
                
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(child, "alpha", 0f, 1f);
                ObjectAnimator slideUp = ObjectAnimator.ofFloat(child, "translationY", dp(30), 0f);
                
                AnimatorSet animSet = new AnimatorSet();
                animSet.playTogether(fadeIn, slideUp);
                animSet.setDuration(600);
                animSet.setInterpolator(new DecelerateInterpolator());
                animSet.start();
                break;
            }
        }
    }
    
    /**
     * 启动持续浮动动画 - 对应小程序的 chatFloat
     */
    private void startFloatingAnimation() {
        if (chatContainer != null) {
            // 持续的轻微上下浮动
            ObjectAnimator floatAnim = ObjectAnimator.ofFloat(chatContainer, "translationY", 0f, -dp(6), 0f);
            floatAnim.setDuration(3000);
            floatAnim.setRepeatCount(ValueAnimator.INFINITE);
            floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            floatAnim.start();
        }
    }
    
    /**
     * 发送按钮状态更新
     */
    private void updateSendButtonState() {
        if (sendButton != null) {
            boolean hasText = !currentMessage.trim().isEmpty();
            sendButton.setAlpha(hasText ? 1f : 0.6f);
            sendButton.setEnabled(hasText);
        }
    }
    
    /**
     * 发送消息点击事件
     */
    private void onSendClick() {
        if (currentMessage.trim().isEmpty()) {
            Toast.makeText(getContext(), "请输入消息", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Log.d(TAG, "发送消息: " + currentMessage);
        
        // 显示发送成功提示
        Toast.makeText(getContext(), "消息已发送", Toast.LENGTH_SHORT).show();
        
        // 清空输入框
        if (inputMessage != null) {
            inputMessage.setText("");
            currentMessage = "";
        }
        
        // 跳转到聊天页面
        goToChat();
    }
    
    /**
     * 跳转到聊天页面 - 对应小程序的 goToChat
     */
    private void goToChat() {
        try {
            Toast.makeText(getContext(), "即将跳转到聊天页面", Toast.LENGTH_SHORT).show();
            
            // TODO: 创建ChatActivity后取消注释
            // Intent intent = new Intent(getActivity(), ChatActivity.class);
            // startActivity(intent);
            
        } catch (Exception e) {
            Log.e(TAG, "跳转到聊天页面失败", e);
            Toast.makeText(getContext(), "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 导航方法 - 对应小程序的各种 navigateTo
     */
    private void navigateToRepair() {
        showToastAndNavigate("智能报修", "RepairActivity");
        // TODO: Intent intent = new Intent(getActivity(), RepairActivity.class);
        // startActivity(intent);
    }
    
    private void navigateToMedical() {
        showToastAndNavigate("帮我送医", "MedicalActivity");
        // TODO: Intent intent = new Intent(getActivity(), MedicalActivity.class);
        // startActivity(intent);
    }
    
    private void navigateToInspection() {
        showToastAndNavigate("日常检查", "InspectionActivity");
        // TODO: Intent intent = new Intent(getActivity(), InspectionActivity.class);
        // startActivity(intent);
    }
    
    private void navigateToWorkOrder() {
        showToastAndNavigate("提交工单", "PublishActivity");
    }
    
    private void navigateToProgress() {
        showToastAndNavigate("追踪进度", "MyOrderActivity");
    }
    
    private void navigateToUtilities() {
        showToastAndNavigate("停水停电", "OutageActivity");
    }
    
    private void navigateToWeather() {
        showToastAndNavigate("天气预警", "WeatherActivity");
    }
    
    private void navigateToHospital() {
        showToastAndNavigate("附近医院", "HospitalActivity");
    }
    
    /**
     * 显示提示并导航（临时方法，后续替换为真实导航）
     */
    private void showToastAndNavigate(String serviceName, String activityName) {
        Toast.makeText(getContext(), "即将跳转到" + serviceName + "页面", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "导航到: " + activityName);
    }
    
    /**
     * 创建错误视图
     */
    private View createErrorView() {
        LinearLayout errorLayout = new LinearLayout(getContext());
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(android.view.Gravity.CENTER);
        errorLayout.setPadding(dp(40), dp(100), dp(40), dp(40));
        
        TextView errorText = new TextView(getContext());
        errorText.setText("页面加载失败");
        errorText.setTextSize(18);
        errorText.setTextColor(Color.parseColor("#EF4444"));
        errorText.setGravity(android.view.Gravity.CENTER);
        
        errorLayout.addView(errorText);
        return errorLayout;
    }
    
    /**
     * 工具方法 - dp转px
     */
    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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
            LinearLayout.LayoutParams.MATCH_PARENT, dp(dp)
        );
        space.setLayoutParams(spaceParams);
        parent.addView(space);
    }
    
    /**
     * 添加水平间距
     */
    private void addHorizontalSpacing(LinearLayout parent, int dp) {
        View space = new View(getContext());
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
            dp(dp), LinearLayout.LayoutParams.MATCH_PARENT
        );
        space.setLayoutParams(spaceParams);
        parent.addView(space);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "湘湘管家主页视图创建完成");
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "湘湘管家主页恢复显示");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "湘湘管家主页视图销毁");
        
        // 清理资源
        scrollView = null;
        mainContainer = null;
        chatContainer = null;
        inputMessage = null;
        sendButton = null;
    }
}