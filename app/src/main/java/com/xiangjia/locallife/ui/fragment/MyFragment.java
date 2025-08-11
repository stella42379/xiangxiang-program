package com.xiangjia.locallife.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xiangjia.locallife.MainActivity;
import com.xiangjia.locallife.model.NotificationItem;
import com.xiangjia.locallife.model.UserInfo;
import com.xiangjia.locallife.util.NotificationManager;
import com.xiangjia.locallife.util.UserManager;

import java.util.List;

/**
 * 个人中心Fragment - 完整版本，集成所有功能 + 退出登录按钮
 * 与小程序my页面完全一致，包含用户信息、便捷功能、通知公告
 */
public class MyFragment extends Fragment {
    
    private static final String TAG = "MyFragment";
    
    // UI组件
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollView;
    private LinearLayout mainContainer;
    private ImageView userAvatar;
    private TextView userName;
    private TextView userId;
    private TextView userStatus;
    private LinearLayout notificationsList;
    private TextView newNotificationBadge;
    
    // 管理类
    private UserManager userManager;
    private NotificationManager notificationManager;
    
    // 数据
    private UserInfo currentUserInfo;
    private List<NotificationItem> notifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "创建个人中心页面");
        
        // 初始化管理类
        userManager = UserManager.getInstance(getContext());
        notificationManager = NotificationManager.getInstance(getContext());
        
        try {
            View rootView = createPersonalPageLayout();
            setupStatusBar();
            loadData();
            setupAnimations();
            return rootView;
        } catch (Exception e) {
            Log.e(TAG, "创建个人中心页面失败", e);
            return createErrorView();
        }
    }
    
    /**
     * 创建个人中心页面布局 - 完全复刻小程序样式
     */
    private View createPersonalPageLayout() {
        // 下拉刷新容器
        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
        swipeRefreshLayout.setColorSchemeColors(
            Color.parseColor("#6366F1"),
            Color.parseColor("#8B5CF6"),
            Color.parseColor("#EC4899")
        );
        swipeRefreshLayout.setOnRefreshListener(this::onPullDownRefresh);
        
        // 背景渐变
        GradientDrawable bgGradient = new GradientDrawable();
        bgGradient.setOrientation(GradientDrawable.Orientation.TL_BR);
        bgGradient.setColors(new int[]{
            Color.parseColor("#E6F3FF"),
            Color.parseColor("#B3D9FF"),
            Color.parseColor("#87CEEB")
        });
        swipeRefreshLayout.setBackground(bgGradient);

        // 滚动容器
        scrollView = new ScrollView(getContext());
        scrollView.setBackgroundColor(Color.TRANSPARENT);
        scrollView.setFillViewport(true);

        mainContainer = new LinearLayout(getContext());
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(0, getStatusBarHeight(), 0, dp(20));

        // 1. 用户信息区域
        createUserProfileSection();

        // 2. 便捷功能区域
        createQuickActionsSection();

        // 3. 通知公告区域
        createNotificationsSection();

        // 🆕 4. 退出登录区域
        createLogoutSection();
        
        scrollView.addView(mainContainer);
        swipeRefreshLayout.addView(scrollView);
        return swipeRefreshLayout;
    }
    
    /**
     * 1. 创建用户信息区域
     */
    private void createUserProfileSection() {
        CardView profileCard = createGlassCard();
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dp(20), dp(20), dp(20), dp(16));
        profileCard.setLayoutParams(cardParams);
        
        LinearLayout profileContainer = new LinearLayout(getContext());
        profileContainer.setOrientation(LinearLayout.VERTICAL);
        profileContainer.setPadding(dp(24), dp(32), dp(24), dp(32));
        profileContainer.setGravity(android.view.Gravity.CENTER);
        
        // 用户头像
        userAvatar = createUserAvatar();
        profileContainer.addView(userAvatar);
        
        // 用户名
        userName = createUserNameText();
        profileContainer.addView(userName);
        
        // 用户ID
        userId = createUserIdText();
        profileContainer.addView(userId);
        
        // 用户状态
        userStatus = createUserStatusBadge();
        profileContainer.addView(userStatus);
        
        profileCard.addView(profileContainer);
        mainContainer.addView(profileCard);
    }
    
    /**
     * 创建用户头像
     */
    private ImageView createUserAvatar() {
        ImageView avatar = new ImageView(getContext());
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(80), dp(80));
        avatarParams.bottomMargin = dp(16);
        avatar.setLayoutParams(avatarParams);
        
        // 圆形头像背景
        GradientDrawable avatarBg = new GradientDrawable();
        avatarBg.setShape(GradientDrawable.OVAL);
        avatarBg.setColor(Color.parseColor("#E5E7EB"));
        avatar.setBackground(avatarBg);
        avatar.setScaleType(ImageView.ScaleType.CENTER);
        
        // 默认头像图标
        avatar.setImageDrawable(createTextDrawable("👤", dp(32)));
        
        // 头像点击事件
        avatar.setOnClickListener(v -> {
            animateClick(avatar);
            requestUserProfile();
        });
        
        return avatar;
    }
    
    /**
     * 创建用户名文本
     */
    private TextView createUserNameText() {
        TextView nameText = new TextView(getContext());
        nameText.setTextSize(20);
        nameText.setTextColor(Color.parseColor("#484D61"));
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        nameText.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        nameParams.bottomMargin = dp(8);
        nameText.setLayoutParams(nameParams);
        
        return nameText;
    }
    
    /**
     * 创建用户ID文本
     */
    private TextView createUserIdText() {
        TextView idText = new TextView(getContext());
        idText.setTextSize(14);
        idText.setTextColor(Color.parseColor("#88484D61"));
        idText.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams idParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        idParams.bottomMargin = dp(12);
        idText.setLayoutParams(idParams);
        
        return idText;
    }
    
    /**
     * 创建用户状态标签
     */
    private TextView createUserStatusBadge() {
        TextView statusText = new TextView(getContext());
        statusText.setTextSize(12);
        statusText.setTextColor(Color.parseColor("#10B981"));
        statusText.setGravity(android.view.Gravity.CENTER);
        statusText.setPadding(dp(12), dp(6), dp(12), dp(6));
        
        GradientDrawable statusBg = new GradientDrawable();
        statusBg.setCornerRadius(dp(12));
        statusBg.setColor(Color.parseColor("#20D1FAE5"));
        statusText.setBackground(statusBg);
        
        return statusText;
    }
    
    /**
     * 3. 创建便捷功能区域
     */
    private void createQuickActionsSection() {
        CardView actionsCard = createGlassCard();
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dp(20), dp(8), dp(20), dp(16));
        actionsCard.setLayoutParams(cardParams);
        
        LinearLayout actionsContainer = new LinearLayout(getContext());
        actionsContainer.setOrientation(LinearLayout.VERTICAL);
        actionsContainer.setPadding(dp(24), dp(20), dp(24), dp(24));
        
        // 标题
        TextView cardTitle = createSectionTitle("便捷功能");
        actionsContainer.addView(cardTitle);
        
        // 功能网格
        LinearLayout actionsGrid = new LinearLayout(getContext());
        actionsGrid.setOrientation(LinearLayout.HORIZONTAL);
        actionsGrid.setWeightSum(2);
        
        View myServicesItem = createActionItem("🏠", "我的管家", this::openMyServices);
        View helpCenterItem = createActionItem("❓", "帮助中心", this::openHelpCenter);
        
        actionsGrid.addView(myServicesItem);
        actionsGrid.addView(helpCenterItem);
        
        actionsContainer.addView(actionsGrid);
        actionsCard.addView(actionsContainer);
        mainContainer.addView(actionsCard);
    }
    
    /**
     * 4. 创建通知公告区域
     */
    private void createNotificationsSection() {
        CardView notificationsCard = createGlassCard();
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dp(20), dp(8), dp(20), dp(16));
        notificationsCard.setLayoutParams(cardParams);
        
        LinearLayout notificationsContainer = new LinearLayout(getContext());
        notificationsContainer.setOrientation(LinearLayout.VERTICAL);
        notificationsContainer.setPadding(dp(24), dp(20), dp(24), dp(24));
        
        // 标题区域（包含新消息提示）
        LinearLayout titleArea = new LinearLayout(getContext());
        titleArea.setOrientation(LinearLayout.HORIZONTAL);
        titleArea.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        TextView cardTitle = createSectionTitle("通知公告");
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        cardTitle.setLayoutParams(titleParams);
        
        // 新消息角标
        newNotificationBadge = createNotificationBadge();
        
        titleArea.addView(cardTitle);
        titleArea.addView(newNotificationBadge);
        
        // 通知列表容器
        notificationsList = new LinearLayout(getContext());
        notificationsList.setOrientation(LinearLayout.VERTICAL);
        
        notificationsContainer.addView(titleArea);
        notificationsContainer.addView(notificationsList);
        notificationsCard.addView(notificationsContainer);
        mainContainer.addView(notificationsCard);
    }
    
    /**
     * 🆕 5. 创建退出登录区域
     */
    private void createLogoutSection() {
        CardView logoutCard = createGlassCard();
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dp(20), dp(8), dp(20), dp(16));
        logoutCard.setLayoutParams(cardParams);
        
        LinearLayout logoutContainer = new LinearLayout(getContext());
        logoutContainer.setOrientation(LinearLayout.VERTICAL);
        logoutContainer.setPadding(dp(24), dp(20), dp(24), dp(24));
        
        // 退出登录按钮
        TextView logoutButton = createLogoutButton();
        logoutContainer.addView(logoutButton);
        
        logoutCard.addView(logoutContainer);
        mainContainer.addView(logoutCard);
    }
    
    /**
     * 创建退出登录按钮
     */
    private TextView createLogoutButton() {
        TextView logoutBtn = new TextView(getContext());
        logoutBtn.setText("🚪 退出登录");
        logoutBtn.setTextSize(16);
        logoutBtn.setTextColor(Color.parseColor("#EF4444"));
        logoutBtn.setTypeface(null, android.graphics.Typeface.BOLD);
        logoutBtn.setGravity(android.view.Gravity.CENTER);
        logoutBtn.setPadding(dp(24), dp(16), dp(24), dp(16));
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        logoutBtn.setLayoutParams(buttonParams);
        
        // 按钮背景样式
        GradientDrawable buttonBg = new GradientDrawable();
        buttonBg.setCornerRadius(dp(12));
        buttonBg.setColor(Color.parseColor("#FEF2F2"));
        buttonBg.setStroke(dp(2), Color.parseColor("#FECACA"));
        logoutBtn.setBackground(buttonBg);
        
        // 点击事件
        logoutBtn.setOnClickListener(v -> {
            animateClick(logoutBtn);
            showLogoutConfirmDialog();
        });
        
        return logoutBtn;
    }
    
    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutConfirmDialog() {
        // 创建简单的确认提示
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                performLogout();
            })
            .setNegativeButton("取消", (dialog, which) -> {
                dialog.dismiss();
            })
            .show();
    }
    
    /**
     * 执行退出登录
     */
    private void performLogout() {
        try {
            // 显示退出中的提示
            Toast.makeText(getContext(), "正在退出登录...", Toast.LENGTH_SHORT).show();
            
            // 调用MainActivity的退出登录方法
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.logout();
            } else {
                Log.w(TAG, "无法获取MainActivity实例，尝试其他退出方式");
                // 备用退出方式
                performFallbackLogout();
            }
        } catch (Exception e) {
            Log.e(TAG, "退出登录失败", e);
            Toast.makeText(getContext(), "退出登录失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 备用退出登录方式
     */
    private void performFallbackLogout() {
        try {
            // 使用SharedPrefsUtil直接清除登录状态
            com.xiangjia.locallife.util.SharedPrefsUtil.clearUserInfo(getContext());
            
            // 重启应用
            android.content.Intent intent = new android.content.Intent(getContext(), com.xiangjia.locallife.LoginActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            
            if (getActivity() != null) {
                getActivity().finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "备用退出登录也失败", e);
            Toast.makeText(getContext(), "退出登录失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 创建毛玻璃卡片
     */
    private CardView createGlassCard() {
        CardView card = new CardView(getContext());
        card.setRadius(dp(16));
        card.setCardElevation(dp(8));
        card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        return card;
    }
    
    
    /**
     * 创建区域标题
     */
    private TextView createSectionTitle(String title) {
        TextView titleText = new TextView(getContext());
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(Color.parseColor("#484D61"));
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.bottomMargin = dp(20);
        titleText.setLayoutParams(titleParams);
        
        return titleText;
    }
    
    /**
     * 创建新通知角标
     */
    private TextView createNotificationBadge() {
        TextView badge = new TextView(getContext());
        badge.setTextSize(10);
        badge.setTextColor(Color.WHITE);
        badge.setTypeface(null, android.graphics.Typeface.BOLD);
        badge.setGravity(android.view.Gravity.CENTER);
        badge.setPadding(dp(6), dp(4), dp(6), dp(4));
        badge.setMinWidth(dp(20));
        badge.setMinHeight(dp(20));
        
        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setShape(GradientDrawable.OVAL);
        badgeBg.setColor(Color.parseColor("#EF4444"));
        badge.setBackground(badgeBg);
        badge.setVisibility(View.GONE);
        
        return badge;
    }
    
    /**
     * 创建功能项
     */
    private View createActionItem(String icon, String title, Runnable onClickAction) {
        LinearLayout actionItem = new LinearLayout(getContext());
        actionItem.setOrientation(LinearLayout.VERTICAL);
        actionItem.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        itemParams.setMargins(dp(8), dp(8), dp(8), dp(8));
        actionItem.setLayoutParams(itemParams);
        actionItem.setPadding(dp(16), dp(20), dp(16), dp(20));
        
        // 背景
        GradientDrawable itemBg = new GradientDrawable();
        itemBg.setCornerRadius(dp(16));
        itemBg.setColor(Color.parseColor("#FFFFFF"));
        actionItem.setBackground(itemBg);
        
        // 图标
        TextView iconView = new TextView(getContext());
        iconView.setText(icon);
        iconView.setTextSize(24);
        iconView.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        iconParams.bottomMargin = dp(8);
        iconView.setLayoutParams(iconParams);
        
        // 标题
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(14);
        titleView.setTextColor(Color.parseColor("#484D61"));
        titleView.setGravity(android.view.Gravity.CENTER);
        
        actionItem.addView(iconView);
        actionItem.addView(titleView);
        
        // 点击事件
        actionItem.setOnClickListener(v -> {
            animateClick(actionItem);
            onClickAction.run();
        });
        
        return actionItem;
    }
    
    /**
     * 创建通知项
     */
    private View createNotificationItem(NotificationItem notification) {
        LinearLayout notificationItem = new LinearLayout(getContext());
        notificationItem.setOrientation(LinearLayout.HORIZONTAL);
        
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        itemParams.bottomMargin = dp(12);
        notificationItem.setLayoutParams(itemParams);
        notificationItem.setPadding(dp(16), dp(16), dp(16), dp(16));
        
        // 背景
        GradientDrawable itemBg = new GradientDrawable();
        itemBg.setCornerRadius(dp(12));
        itemBg.setColor(Color.parseColor("#FFFFFF"));
        notificationItem.setBackground(itemBg);
        
        // 图标
        TextView iconView = new TextView(getContext());
        iconView.setText(notification.getIcon());
        iconView.setTextSize(20);
        iconView.setGravity(android.view.Gravity.CENTER);
        
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(40), dp(40));
        iconParams.rightMargin = dp(12);
        iconView.setLayoutParams(iconParams);
        
        // 内容区域
        LinearLayout contentArea = new LinearLayout(getContext());
        contentArea.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        contentArea.setLayoutParams(contentParams);
        
        // 标题
        TextView titleView = new TextView(getContext());
        titleView.setText(notification.getTitle());
        titleView.setTextSize(16);
        titleView.setTextColor(Color.parseColor("#484D61"));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.bottomMargin = dp(8);
        titleView.setLayoutParams(titleParams);
        
        // 描述
        TextView descView = new TextView(getContext());
        descView.setText(notification.getDesc());
        descView.setTextSize(14);
        descView.setTextColor(Color.parseColor("#88484D61"));
        descView.setMaxLines(2);
        
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.bottomMargin = dp(8);
        descView.setLayoutParams(descParams);
        
        // 时间和状态
        LinearLayout metaArea = new LinearLayout(getContext());
        metaArea.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView timeView = new TextView(getContext());
        timeView.setText(notification.getTime());
        timeView.setTextSize(12);
        timeView.setTextColor(Color.parseColor("#AA484D61"));
        metaArea.addView(timeView);
        
        // NEW标记
        if (notification.isNew()) {
            TextView newBadge = new TextView(getContext());
            newBadge.setText("NEW");
            newBadge.setTextSize(10);
            newBadge.setTextColor(Color.WHITE);
            newBadge.setPadding(dp(6), dp(2), dp(6), dp(2));
            newBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            
            GradientDrawable badgeBg = new GradientDrawable();
            badgeBg.setCornerRadius(dp(8));
            badgeBg.setColor(Color.parseColor("#EF4444"));
            newBadge.setBackground(badgeBg);
            
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            badgeParams.leftMargin = dp(8);
            newBadge.setLayoutParams(badgeParams);
            
            metaArea.addView(newBadge);
        }
        
        contentArea.addView(titleView);
        contentArea.addView(descView);
        contentArea.addView(metaArea);
        
        notificationItem.addView(iconView);
        notificationItem.addView(contentArea);
        
        // 点击事件
        notificationItem.setOnClickListener(v -> {
            animateClick(notificationItem);
            openNotification(notification);
        });
        
        return notificationItem;
    }
    
    /**
     * 加载数据
     */
    private void loadData() {
        loadUserInfo();
        loadNotifications();
    }
    
    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        currentUserInfo = userManager.getCurrentUserInfo();
        updateUserInfoUI();
    }
    
    /**
     * 更新用户信息UI
     */
    private void updateUserInfoUI() {
        if (currentUserInfo != null) {
            userName.setText(currentUserInfo.getNickName());
            userId.setText("ID: " + currentUserInfo.getMaskedPhoneNumber());
            userStatus.setText(currentUserInfo.getDisplayStatus());
            
            // 根据认证状态设置颜色
            if (currentUserInfo.isAuthenticated()) {
                userStatus.setTextColor(Color.parseColor("#10B981"));
                GradientDrawable statusBg = new GradientDrawable();
                statusBg.setCornerRadius(dp(12));
                statusBg.setColor(Color.parseColor("#20D1FAE5"));
                userStatus.setBackground(statusBg);
            } else {
                userStatus.setTextColor(Color.parseColor("#F59E0B"));
                GradientDrawable statusBg = new GradientDrawable();
                statusBg.setCornerRadius(dp(12));
                statusBg.setColor(Color.parseColor("#20FEF3C7"));
                userStatus.setBackground(statusBg);
            }
        }
    }
    
    /**
     * 加载通知数据
     */
    private void loadNotifications() {
        notifications = notificationManager.getAllNotifications();
        updateNotificationsUI();
        updateNotificationBadge();
    }
    
    /**
     * 更新通知UI
     */
    private void updateNotificationsUI() {
        notificationsList.removeAllViews();
        
        if (notifications != null && !notifications.isEmpty()) {
            for (NotificationItem notification : notifications) {
                View notificationView = createNotificationItem(notification);
                notificationsList.addView(notificationView);
            }
        } else {
            // 空状态
            TextView emptyText = new TextView(getContext());
            emptyText.setText("暂无通知");
            emptyText.setTextColor(Color.parseColor("#9CA3AF"));
            emptyText.setGravity(android.view.Gravity.CENTER);
            emptyText.setPadding(dp(20), dp(40), dp(20), dp(40));
            notificationsList.addView(emptyText);
        }
    }
    
    /**
     * 更新通知角标
     */
    private void updateNotificationBadge() {
        int newCount = notificationManager.getNewNotificationCount();
        if (newCount > 0) {
            newNotificationBadge.setText(String.valueOf(newCount));
            newNotificationBadge.setVisibility(View.VISIBLE);
        } else {
            newNotificationBadge.setVisibility(View.GONE);
        }
    }
    
    /**
     * 设置动画
     */
    private void setupAnimations() {
        mainContainer.setAlpha(0f);
        mainContainer.setTranslationY(dp(30));
        
        mainContainer.animate()
            .alpha(1f)
            .translationY(0)
            .setDuration(600)
            .setInterpolator(new DecelerateInterpolator())
            .start();
        
        animateCardEntry();
    }
    
    /**
     * 卡片入场动画
     */
    private void animateCardEntry() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (int i = 1; i < mainContainer.getChildCount(); i++) {
                View child = mainContainer.getChildAt(i);
                child.setAlpha(0f);
                child.setTranslationY(dp(20));
                
                child.animate()
                    .alpha(1f)
                    .translationY(0)
                    .setDuration(400)
                    .setStartDelay(i * 100)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            }
        }, 200);
    }
    
    /**
     * 点击动画
     */
    private void animateClick(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f);
        ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f);
        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f);
        
        scaleXDown.setDuration(100);
        scaleYDown.setDuration(100);
        scaleXUp.setDuration(100);
        scaleYUp.setDuration(100);
        
        animatorSet.play(scaleXDown).with(scaleYDown);
        animatorSet.play(scaleXUp).with(scaleYUp).after(scaleXDown);
        animatorSet.start();
    }
    
    /**
     * 下拉刷新
     */
    private void onPullDownRefresh() {
        // 刷新用户信息
        loadUserInfo();
        
        // 刷新通知
        notificationManager.refreshNotifications(new NotificationManager.NotificationRefreshCallback() {
            @Override
            public void onSuccess(boolean hasNewNotification) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    loadNotifications();
                    swipeRefreshLayout.setRefreshing(false);
                    
                    String message = hasNewNotification ? "收到新通知" : "刷新成功";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onFailure(String error) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 请求用户信息
     */
    private void requestUserProfile() {
        userManager.requestUserProfile(new UserManager.UserProfileCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    loadUserInfo();
                    Toast.makeText(getContext(), "用户信息已更新", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onFailure(String error) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * 打开我的管家
     */
    private void openMyServices() {
        Toast.makeText(getContext(), "我的管家功能", Toast.LENGTH_SHORT).show();
        // TODO: 跳转到我的管家页面
    }
    
    /**
     * 打开帮助中心
     */
    private void openHelpCenter() {
        Toast.makeText(getContext(), "帮助中心功能", Toast.LENGTH_SHORT).show();
        // TODO: 跳转到帮助中心页面
    }
    
    /**
     * 打开通知详情
     */
    private void openNotification(NotificationItem notification) {
        // 标记为已读
        notificationManager.markAsRead(notification.getId());
        updateNotificationBadge();
        
        Toast.makeText(getContext(), "查看通知: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
        // TODO: 显示通知详情页面
    }
    
    /**
     * 创建文本图标
     */
    private android.graphics.drawable.Drawable createTextDrawable(String text, int size) {
        // 使用系统默认的用户图标
        return getContext().getResources().getDrawable(android.R.drawable.ic_menu_myplaces, null);
    }
    
    /**
     * dp转px工具方法
     */
    private int dp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 设置沉浸式状态栏，文字深色
     */
    private void setupStatusBar() {
        if (getActivity() == null) return;
        Window window = requireActivity().getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    
    /**
     * 创建错误视图
     */
    private View createErrorView() {
        LinearLayout errorLayout = new LinearLayout(getContext());
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(android.view.Gravity.CENTER);
        errorLayout.setPadding(dp(40), dp(100), dp(40), dp(40));
        errorLayout.setBackgroundColor(Color.parseColor("#F8F8F8"));
        
        TextView errorText = new TextView(getContext());
        errorText.setText("个人中心页面加载失败");
        errorText.setTextSize(18);
        errorText.setTextColor(Color.parseColor("#EF4444"));
        errorText.setGravity(android.view.Gravity.CENTER);
        
        errorLayout.addView(errorText);
        return errorLayout;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 页面恢复时刷新数据
        loadData();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清理资源
        if (userManager != null) {
            userManager = null;
        }
        if (notificationManager != null) {
            notificationManager = null;
        }
    }
}