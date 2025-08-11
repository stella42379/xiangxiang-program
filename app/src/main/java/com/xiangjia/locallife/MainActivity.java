package com.xiangjia.locallife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xiangjia.locallife.ui.fragment.ForumFragment;
import com.xiangjia.locallife.ui.fragment.MainPageFragment;
import com.xiangjia.locallife.ui.fragment.LocalNewsFragment;
import com.xiangjia.locallife.ui.fragment.DifyFragment;
import com.xiangjia.locallife.ui.fragment.MyFragment;
import com.xiangjia.locallife.util.SharedPrefsUtil;

/**
 * 湘湘管家主Activity - 修复版本：只负责容器+导航，让Fragment自己画界面
 */
public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private MainPagerAdapter pagerAdapter;
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "湘湘管家MainActivity启动");
        
        // 🎯 关键修改：使用XML布局，不再手写界面
        setContentView(R.layout.activity_main);
        
        // 初始化组件
        initViews();
        setupViewPager();
        setupBottomNavigation();
        
        Log.d(TAG, "MainActivity创建成功");
    }
    
    /**
     * 初始化视图组件
     */
    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    
    /**
     * 设置ViewPager2 - 🎯 关键：只负责装Fragment，不创建白卡片
     */
    private void setupViewPager() {
        pagerAdapter = new MainPagerAdapter(this);
        
        // 🎯 让Fragment自己画界面，不要手写白卡片
        viewPager.setAdapter(pagerAdapter);
        
        // 监听页面切换
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                // 同步底部导航选中状态
                switch (position) {
                    case 0: bottomNavigationView.setSelectedItemId(R.id.nav_home); break;
                    case 1: bottomNavigationView.setSelectedItemId(R.id.nav_explore); break;
                    case 2: bottomNavigationView.setSelectedItemId(R.id.nav_bookmark); break;
                    case 3: bottomNavigationView.setSelectedItemId(R.id.nav_profile); break;
                }
                Log.d(TAG, "切换到页面: " + position);
            }
        });
    }
    
    /**
     * 设置底部导航 - 🎯 使用XML菜单 + 取消tint
     */
    private void setupBottomNavigation() {
        // 🎯 关键修改：使用XML菜单
        bottomNavigationView.inflateMenu(R.menu.main_bottom_nav);
        
        // 🎯 关键修改：取消图标tint，保持原色
        bottomNavigationView.setItemIconTintList(null);
        
        // 设置点击监听
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_explore) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_bookmark) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(3, true);
                return true;
            }
            return false;
        });
        
        // 默认选中第一个
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
    
    // ========== 🎯 新增：退出登录方法（MyFragment需要调用） ==========
    /**
     * 退出登录
     */
    public void logout() {
        try {
            Log.d(TAG, "用户退出登录");
            SharedPrefsUtil.clearUserInfo(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "退出登录失败: " + e.getMessage());
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
    // ========== 用户信息方法结束 ==========
    
    /**
     * ViewPager2适配器 - 🎯 简化版本，只负责提供Fragment
     */
    class MainPagerAdapter extends FragmentStateAdapter {
        
        public MainPagerAdapter(@NonNull androidx.fragment.app.FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        
        @Override
        public int getItemCount() {
            return 4;
        }
        
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: 
                    Log.d(TAG, "创建MainPageFragment");
                    return new MainPageFragment();   // 🎯 使用真正的主页Fragment
                case 1: 
                    return new LocalNewsFragment();  // 今日时讯
                case 2: 
                    return new ForumFragment();       // AI助手
                case 3: 
                    return new MyFragment();         // 个人中心
                default: 
                    return new MainPageFragment();
            }
        }
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