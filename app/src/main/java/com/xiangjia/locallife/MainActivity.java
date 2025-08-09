package com.xiangjia.locallife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.xiangjia.locallife.ui.fragment.MainPageFragment;
import com.xiangjia.locallife.ui.fragment.LocalNewsFragment;
import com.xiangjia.locallife.ui.fragment.DifyFragment;
import com.xiangjia.locallife.ui.fragment.MyFragment;
import com.xiangjia.locallife.ui.fragment.ForumFragment;
import com.xiangjia.locallife.util.SharedPrefsUtil;


/**
 * 湘湘管家主Activity - 安全集成新MyFragment版本 + 登录检查
 */
public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    
    private ViewPager2 viewPager;
    private LinearLayout bottomNavigationContainer;
    private MainPagerAdapter pagerAdapter;
    private int currentTabIndex = 0;
    
    // Tab配置 - 对应小程序的tabBar
    private static final String[] TAB_TITLES = {"主页", "今日时讯", "AI助手", "个人中心"};
    private static final String[] TAB_ICONS = {"🏠", "📰", "🤖", "👤"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "湘湘管家MainActivity启动");
        
        // ========== 新增：登录状态检查 ==========
        if (!checkLoginStatus()) {
            return; // 如果未登录，直接返回（已跳转到登录页）
        }
        
        // 显示欢迎信息
        showWelcomeMessage();
        // ========== 登录检查结束 ==========
        
        try {
            createMainLayout();
            setupViewPager();
            setupBottomNavigation();
            Log.d(TAG, "MainActivity创建成功");
        } catch (Exception e) {
            Log.e(TAG, "MainActivity创建失败", e);
            createErrorLayout();
        }
    }
    
    // ========== 新增：登录相关方法 ==========
    /**
     * 检查登录状态
     */
    private boolean checkLoginStatus() {
        try {
            if (!SharedPrefsUtil.isLoggedIn(this)) {
                Log.d(TAG, "用户未登录，跳转到登录页面");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return false;
            }
            
            Log.d(TAG, "用户已登录: " + SharedPrefsUtil.getUsername(this));
            return true;
        } catch (Exception e) {
            Log.w(TAG, "登录状态检查失败，允许继续: " + e.getMessage());
            return true; // 如果检查失败，允许继续运行
        }
    }
    
    /**
     * 显示欢迎信息
     */
    private void showWelcomeMessage() {
        try {
            String username = SharedPrefsUtil.getUsername(this);
            String nickname = SharedPrefsUtil.getNickname(this);
            String displayName = (nickname != null && !nickname.isEmpty()) ? nickname : username;
            
            if (displayName != null && !displayName.isEmpty()) {
                Log.d(TAG, "欢迎回来，" + displayName + "!");
            }
        } catch (Exception e) {
            Log.w(TAG, "获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息用于Fragment使用
     */
    public String getCurrentUserId() {
        try {
            return SharedPrefsUtil.getUserId(this);
        } catch (Exception e) {
            Log.w(TAG, "获取用户ID失败: " + e.getMessage());
            return "guest_user";
        }
    }
    
    public String getCurrentUsername() {
        try {
            return SharedPrefsUtil.getUsername(this);
        } catch (Exception e) {
            Log.w(TAG, "获取用户名失败: " + e.getMessage());
            return "游客用户";
        }
    }
    
    public String getCurrentUserNickname() {
        try {
            return SharedPrefsUtil.getNickname(this);
        } catch (Exception e) {
            Log.w(TAG, "获取昵称失败: " + e.getMessage());
            return getCurrentUsername();
        }
    }
    
    public String getCurrentUserRole() {
        try {
            return SharedPrefsUtil.getUserRole(this);
        } catch (Exception e) {
            Log.w(TAG, "获取用户角色失败: " + e.getMessage());
            return "user";
        }
    }
    
    /**
     * 退出登录
     */
    public void logout() {
        try {
            SharedPrefsUtil.clearUserInfo(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "退出登录失败: " + e.getMessage());
        }
    }
    // ========== 登录相关方法结束 ==========
    
    /**
     * 创建主布局
     */
    private void createMainLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.WHITE);
        
        // ViewPager2容器
        viewPager = new ViewPager2(this);
        LinearLayout.LayoutParams vpParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        );
        viewPager.setLayoutParams(vpParams);
        
        // 底部导航容器
        bottomNavigationContainer = new LinearLayout(this);
        bottomNavigationContainer.setOrientation(LinearLayout.HORIZONTAL);
        bottomNavigationContainer.setBackgroundColor(Color.WHITE);
        bottomNavigationContainer.setElevation(dp(8));
        
        LinearLayout.LayoutParams navParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(80)
        );
        bottomNavigationContainer.setLayoutParams(navParams);
        
        mainLayout.addView(viewPager);
        mainLayout.addView(bottomNavigationContainer);
        
        setContentView(mainLayout);
    }
    
    /**
     * 设置ViewPager2 - 安全集成新MyFragment
     */
    private void setupViewPager() {
        pagerAdapter = new MainPagerAdapter(this);
        
        // 安全地添加Fragment，避免崩溃
        try {
            // 1. 主页Fragment - 使用简单版本避免复杂依赖
            try {
                pagerAdapter.addFragment(new MainPageFragment(), TAB_TITLES[0]);
                Log.d(TAG, "主页Fragment添加成功");
            } catch (Exception e) {
                Log.w(TAG, "MainPageFragment创建失败，使用备用", e);
                pagerAdapter.addFragment(new SimpleFragment("湘湘管家\n\n社区服务平台"), TAB_TITLES[0]);
            }
            
            // 2. 新闻Fragment - 使用安全版本
            try {
                pagerAdapter.addFragment(new LocalNewsFragment(), TAB_TITLES[1]);
                Log.d(TAG, "新闻Fragment添加成功");
            } catch (Exception e) {
                Log.w(TAG, "LocalNewsFragment创建失败，使用备用", e);
                pagerAdapter.addFragment(new SimpleFragment("今日时讯\n\n新闻资讯\n(正在加载...)"), TAB_TITLES[1]);
            }
            
            // 3. 论坛Fragment - AI助手位置
            try {
                pagerAdapter.addFragment(new ForumFragment(), TAB_TITLES[2]);
                Log.d(TAG, "论坛Fragment添加成功");
            } catch (Exception e) {
                Log.w(TAG, "ForumFragment创建失败，使用备用", e);
                pagerAdapter.addFragment(new SimpleFragment("AI助手\n\n智能对话平台\n(正在加载...)"), TAB_TITLES[2]);
            }
            
            // 4. 个人中心Fragment - ⭐ 关键改动：安全集成新MyFragment
            try {
                // 首先检查是否有必需的依赖类
                if (isMyFragmentDependenciesAvailable()) {
                    MyFragment myFragment = new MyFragment();
                    pagerAdapter.addFragment(myFragment, TAB_TITLES[3]);
                    Log.d(TAG, "新版个人中心Fragment添加成功！");
                } else {
                    Log.w(TAG, "个人中心Fragment依赖不完整，使用简化版本");
                    pagerAdapter.addFragment(new SimplePersonalFragment(), TAB_TITLES[3]);
                }
            } catch (Exception e) {
                Log.w(TAG, "MyFragment创建失败，使用备用", e);
                // 备用简单个人中心
                pagerAdapter.addFragment(new SimplePersonalFragment(), TAB_TITLES[3]);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Fragment创建失败", e);
            // 完全备用方案
            createFallbackFragments();
        }
        
        viewPager.setAdapter(pagerAdapter);
        
        // 监听页面切换
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                updateBottomNavigationSelection(position);
                Log.d(TAG, "切换到页面: " + pagerAdapter.getTitle(position));

                // 特殊处理
                switch (position) {
                    case 2:
                        Log.d(TAG, "用户进入AI助手页面");
                        break;
                    case 3:
                        Log.d(TAG, "用户进入个人中心页面");
                        break;
                }
            }
        });
    }
    
    /**
     * 检查MyFragment依赖是否可用
     */
    private boolean isMyFragmentDependenciesAvailable() {
        try {
            // 检查关键类是否存在
            Class.forName("com.xiangjia.locallife.model.UserInfo");
            Class.forName("com.xiangjia.locallife.model.NotificationItem");
            Class.forName("com.xiangjia.locallife.util.UserManager");
            Class.forName("com.xiangjia.locallife.util.NotificationManager");
            
            // 检查SwipeRefreshLayout是否可用
            Class.forName("androidx.swiperefreshlayout.widget.SwipeRefreshLayout");
            
            Log.d(TAG, "个人中心Fragment所有依赖检查通过");
            return true;
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "个人中心Fragment依赖检查失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 创建备用Fragment
     */
    private void createFallbackFragments() {
        pagerAdapter.addFragment(new SimpleFragment("湘湘管家\n\n主页"), TAB_TITLES[0]);
        pagerAdapter.addFragment(new SimpleFragment("今日时讯\n\n加载中..."), TAB_TITLES[1]);
        pagerAdapter.addFragment(new SimpleFragment("AI助手\n\n开发中"), TAB_TITLES[2]);
        pagerAdapter.addFragment(new SimplePersonalFragment(), TAB_TITLES[3]);
    }
    
    /**
     * 设置底部导航 - 对应小程序的tabBar配置
     */
    private void setupBottomNavigation() {
        for (int i = 0; i < TAB_TITLES.length; i++) {
            View tabView = createBottomNavigationTab(TAB_ICONS[i], TAB_TITLES[i], i);
            bottomNavigationContainer.addView(tabView);
        }
        
        // 默认选中第一个Tab（主页）
        updateBottomNavigationSelection(0);
    }
    
    /**
     * 创建底部导航Tab
     */
    private View createBottomNavigationTab(String icon, String title, int index) {
        LinearLayout tabLayout = new LinearLayout(this);
        tabLayout.setOrientation(LinearLayout.VERTICAL);
        tabLayout.setGravity(android.view.Gravity.CENTER);
        tabLayout.setPadding(dp(8), dp(12), dp(8), dp(12));
        
        LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.MATCH_PARENT, 1f
        );
        tabLayout.setLayoutParams(tabParams);
        
        // 图标
        TextView iconView = new TextView(this);
        iconView.setText(icon);
        iconView.setTextSize(20);
        iconView.setGravity(android.view.Gravity.CENTER);
        tabLayout.addView(iconView);
        
        // 标题
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(12);
        titleView.setGravity(android.view.Gravity.CENTER);
        titleView.setPadding(0, dp(4), 0, 0);
        tabLayout.addView(titleView);
        
        // 点击事件
        tabLayout.setOnClickListener(v -> {
            if (currentTabIndex != index) {
                viewPager.setCurrentItem(index, true);

                // 特殊页面点击日志
                switch (index) {
                    case 2:
                        Log.d(TAG, "用户点击AI助手Tab");
                        break;
                    case 3:
                        Log.d(TAG, "用户点击个人中心Tab");
                        break;
                }
            }
        });
        
        // 设置Tag用于后续更新样式
        tabLayout.setTag("tab_" + index);
        
        return tabLayout;
    }
    
    /**
     * 更新底部导航选中状态 - 使用小程序tabBar的颜色配置
     */
    private void updateBottomNavigationSelection(int selectedIndex) {
        for (int i = 0; i < bottomNavigationContainer.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) bottomNavigationContainer.getChildAt(i);
            TextView iconView = (TextView) tab.getChildAt(0);
            TextView titleView = (TextView) tab.getChildAt(1);
            
            if (i == selectedIndex) {
                // 选中状态 - 对应小程序的selectedColor: "#2d8cf0"
                iconView.setTextColor(Color.parseColor("#2d8cf0"));
                titleView.setTextColor(Color.parseColor("#2d8cf0"));
                titleView.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                // 未选中状态 - 对应小程序的color: "#80848f"
                iconView.setTextColor(Color.parseColor("#80848f"));
                titleView.setTextColor(Color.parseColor("#80848f"));
                titleView.setTypeface(null, android.graphics.Typeface.NORMAL);
            }
        }
    }
    
    /**
     * 创建错误布局
     */
    private void createErrorLayout() {
        LinearLayout errorLayout = new LinearLayout(this);
        errorLayout.setOrientation(LinearLayout.VERTICAL);
        errorLayout.setGravity(android.view.Gravity.CENTER);
        errorLayout.setBackgroundColor(Color.WHITE);
        errorLayout.setPadding(dp(40), dp(100), dp(40), dp(40));
        
        TextView errorTitle = new TextView(this);
        errorTitle.setText("湘湘管家");
        errorTitle.setTextSize(24);
        errorTitle.setTextColor(Color.parseColor("#EF4444"));
        errorTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        errorTitle.setGravity(android.view.Gravity.CENTER);
        
        TextView errorDesc = new TextView(this);
        errorDesc.setText("应用启动异常，请重新打开");
        errorDesc.setTextSize(16);
        errorDesc.setTextColor(Color.parseColor("#6B7280"));
        errorDesc.setGravity(android.view.Gravity.CENTER);
        errorDesc.setPadding(0, dp(12), 0, dp(20));
        
        // ========== 新增：退出登录按钮 ==========
        TextView logoutButton = new TextView(this);
        logoutButton.setText("退出登录");
        logoutButton.setTextSize(14);
        logoutButton.setTextColor(Color.parseColor("#6B7280"));
        logoutButton.setPadding(20, 20, 20, 20);
        logoutButton.setGravity(android.view.Gravity.CENTER);
        logoutButton.setOnClickListener(v -> logout());
        
        errorLayout.addView(errorTitle);
        errorLayout.addView(errorDesc);
        errorLayout.addView(logoutButton);
        // ========== 退出登录按钮结束 ==========
        
        setContentView(errorLayout);
    }
    
    /**
     * ViewPager2适配器
     */
    class MainPagerAdapter extends FragmentStateAdapter {
        
        private final java.util.List<Fragment> fragmentList = new java.util.ArrayList<>();
        private final java.util.List<String> fragmentTitleList = new java.util.ArrayList<>();
        
        public MainPagerAdapter(@NonNull androidx.fragment.app.FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        
        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
        
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }
        
        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
        
        public Fragment getFragment(int position) {
            if (position >= 0 && position < fragmentList.size()) {
                return fragmentList.get(position);
            }
            return null;
        }
        
        public String getTitle(int position) {
            if (position >= 0 && position < fragmentTitleList.size()) {
                return fragmentTitleList.get(position);
            }
            return "";
        }
    }
    
    /**
     * 简化版个人中心Fragment - 在新MyFragment不可用时使用
     */
    public static class SimplePersonalFragment extends Fragment {
        
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(dp(20), dp(40), dp(20), dp(40));
            layout.setBackgroundColor(Color.parseColor("#F8F8F8"));
            
            // 模拟个人中心的基本结构
            
            // 用户信息区域
            LinearLayout userInfoCard = createSimpleCard();
            userInfoCard.setPadding(dp(20), dp(30), dp(20), dp(30));
            
            TextView userIcon = new TextView(getContext());
            userIcon.setText("👤");
            userIcon.setTextSize(40);
            userIcon.setGravity(android.view.Gravity.CENTER);
            
            TextView userName = new TextView(getContext());
            // ========== 新增：显示登录用户信息 ==========
            try {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    String displayName = mainActivity.getCurrentUserNickname();
                    userName.setText(displayName != null ? displayName : "湘湘用户");
                } else {
                    userName.setText("湘湘用户");
                }
            } catch (Exception e) {
                userName.setText("湘湘用户");
            }
            // ========== 用户信息显示结束 ==========
            userName.setTextSize(18);
            userName.setTextColor(Color.parseColor("#484D61"));
            userName.setTypeface(null, android.graphics.Typeface.BOLD);
            userName.setGravity(android.view.Gravity.CENTER);
            userName.setPadding(0, dp(10), 0, dp(5));
            
            TextView userStatus = new TextView(getContext());
            userStatus.setText("个人中心开发中...");
            userStatus.setTextSize(14);
            userStatus.setTextColor(Color.parseColor("#9CA3AF"));
            userStatus.setGravity(android.view.Gravity.CENTER);
            
            userInfoCard.addView(userIcon);
            userInfoCard.addView(userName);
            userInfoCard.addView(userStatus);
            
            // 功能提示
            LinearLayout tipsCard = createSimpleCard();
            tipsCard.setPadding(dp(20), dp(20), dp(20), dp(20));
            
            TextView tipsTitle = new TextView(getContext());
            tipsTitle.setText("💡 开发提示");
            tipsTitle.setTextSize(16);
            tipsTitle.setTextColor(Color.parseColor("#484D61"));
            tipsTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            
            TextView tipsDesc = new TextView(getContext());
            tipsDesc.setText("完整的个人中心功能正在开发中\n包括：用户信息、便捷功能、通知公告等");
            tipsDesc.setTextSize(14);
            tipsDesc.setTextColor(Color.parseColor("#6B7280"));
            tipsDesc.setPadding(0, dp(10), 0, 0);
            tipsDesc.setLineSpacing(dp(4), 1.0f);
            
            // ========== 新增：退出登录按钮 ==========
            TextView logoutButton = new TextView(getContext());
            logoutButton.setText("🚪 退出登录");
            logoutButton.setTextSize(16);
            logoutButton.setTextColor(Color.parseColor("#EF4444"));
            logoutButton.setGravity(android.view.Gravity.CENTER);
            logoutButton.setPadding(dp(20), dp(15), dp(20), dp(15));
            logoutButton.setBackground(createButtonBackground());
            logoutButton.setOnClickListener(v -> {
                try {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.logout();
                    }
                } catch (Exception e) {
                    Log.e("SimplePersonalFragment", "退出登录失败", e);
                }
            });
            // ========== 退出登录按钮结束 ==========
            
            tipsCard.addView(tipsTitle);
            tipsCard.addView(tipsDesc);
            tipsCard.addView(logoutButton);
            
            layout.addView(userInfoCard);
            layout.addView(tipsCard);
            
            return layout;
        }
        
        private LinearLayout createSimpleCard() {
            LinearLayout card = new LinearLayout(getContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundColor(Color.WHITE);
            
            // 简单的圆角效果
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setCornerRadius(dp(12));
            bg.setColor(Color.WHITE);
            bg.setStroke(dp(1), Color.parseColor("#E5E7EB"));
            card.setBackground(bg);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dp(16));
            card.setLayoutParams(cardParams);
            
            return card;
        }
        
        // ========== 新增：按钮背景样式 ==========
        private android.graphics.drawable.Drawable createButtonBackground() {
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setCornerRadius(dp(8));
            bg.setColor(Color.parseColor("#FEF2F2"));
            bg.setStroke(dp(1), Color.parseColor("#FECACA"));
            return bg;
        }
        // ========== 按钮背景样式结束 ==========
        
        private int dp(int dp) {
            float density = getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
    
    /**
     * 临时简单Fragment（用于开发中的功能）
     */
    public static class SimpleFragment extends Fragment {
        private String content;
        
        public SimpleFragment(String content) {
            this.content = content;
        }
        
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(dp(40), dp(80), dp(40), dp(40));
            layout.setGravity(android.view.Gravity.CENTER);
            layout.setBackgroundColor(Color.parseColor("#F9FAFB"));
            
            TextView textView = new TextView(getContext());
            textView.setText(content);
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setTextSize(16);
            textView.setTextColor(Color.parseColor("#374151"));
            layout.addView(textView);
            
            return layout;
        }
        
        private int dp(int dp) {
            float density = getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
    
    /**
     * dp转px工具方法
     */
    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity onResume");
        
        // ========== 新增：检查登录状态 ==========
        // 检查登录状态，如果用户在其他地方退出登录，则返回登录页
        try {
            if (!SharedPrefsUtil.isLoggedIn(this)) {
                Log.d(TAG, "用户已退出登录，返回登录页面");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.w(TAG, "登录状态检查失败: " + e.getMessage());
        }
        // ========== 登录状态检查结束 ==========
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity onPause");
    }
    
    @Override
    public void onBackPressed() {
        // 如果不在主页，返回主页；如果在主页，退出应用
        if (currentTabIndex != 0) {
            viewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }
}