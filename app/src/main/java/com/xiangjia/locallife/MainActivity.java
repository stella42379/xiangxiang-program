package com.xiangjia.locallife;

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

/**
 * 简化版MainActivity - 专注于Tab导航，主页逻辑由MainPageFragment处理
 */
public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    
    private ViewPager2 viewPager;
    private LinearLayout bottomNavigationContainer;
    private MainPagerAdapter pagerAdapter;
    private int currentTabIndex = 0;
    
    // Tab配置 - 对应小程序的tabBar
    private static final String[] TAB_TITLES = {"主页", "新闻", "AI助手", "个人中心"};
    private static final String[] TAB_ICONS = {"🏠", "📰", "🤖", "👤"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "湘湘管家MainActivity启动");
        
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
     * 设置ViewPager2
     */
    private void setupViewPager() {
        pagerAdapter = new MainPagerAdapter(this);
        
        // 添加湘湘管家主页Fragment（使用专用资源文件的完整实现）
        pagerAdapter.addFragment(new MainPageFragment(), TAB_TITLES[0]);
        
        // 添加其他Fragment（临时简单实现）
        pagerAdapter.addFragment(new SimpleFragment("新闻资讯\n\n本地新闻和资讯信息"), TAB_TITLES[1]);
        pagerAdapter.addFragment(new SimpleFragment("AI助手\n\n湘湘管家智能助手"), TAB_TITLES[2]);
        pagerAdapter.addFragment(new SimpleFragment("个人中心\n\n个人信息和设置"), TAB_TITLES[3]);
        
        viewPager.setAdapter(pagerAdapter);
        
        // 监听页面切换
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                updateBottomNavigationSelection(position);
                Log.d(TAG, "切换到页面: " + pagerAdapter.getTitle(position));
            }
        });
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
        errorDesc.setPadding(0, dp(12), 0, 0);
        
        errorLayout.addView(errorTitle);
        errorLayout.addView(errorDesc);
        
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
     * 临时简单Fragment（后续替换为完整实现）
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