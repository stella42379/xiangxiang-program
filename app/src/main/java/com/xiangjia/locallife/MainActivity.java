package com.xiangjia.locallife;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity - 支持Fragment的版本
 * 使用ViewPager2 + BottomNavigationView + Fragment架构
 */
public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private TabPagerAdapter pagerAdapter;

    // 对应小程序的4个TabBar页面
    private static final int TAB_MAIN_PAGE = 0;     // mainPage - 首页
    private static final int TAB_LOCAL_NEWS = 1;    // local_news - 今日时讯
    private static final int TAB_DIFY = 2;          // dify - DIFY工作流
    private static final int TAB_MY = 3;            // my - 个人中心

    // 可刷新Fragment接口
    public interface RefreshableFragment {
        void onRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "MainActivity onCreate started");
        
        try {
            // 先尝试加载XML布局
            setContentView(R.layout.activity_main);
            initViews();
            setupViewPager();
            setupBottomNavigation();
            Log.d(TAG, "MainActivity layout created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading XML layout, falling back to simple layout", e);
            // 如果XML布局加载失败，回退到简单布局
            createFallbackLayout();
        }
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_navigation);
        
        if (viewPager == null) {
            Log.e(TAG, "ViewPager not found! Check R.layout.activity_main");
            throw new RuntimeException("ViewPager not found");
        }
        
        if (bottomNav == null) {
            Log.e(TAG, "BottomNavigation not found!");
            throw new RuntimeException("BottomNavigation not found");
        }
    }
    
    /**
     * 设置ViewPager
     */
    private void setupViewPager() {
        try {
            pagerAdapter = new TabPagerAdapter(this);
            
            // 添加Fragment，如果Fragment不存在会用SimpleFragment替代
            pagerAdapter.addFragment(createMainPageFragment(), "首页");
            pagerAdapter.addFragment(createLocalNewsFragment(), "今日时讯");
            pagerAdapter.addFragment(createDifyFragment(), "AI助手");
            pagerAdapter.addFragment(createMyFragment(), "个人中心");

            viewPager.setAdapter(pagerAdapter);
            viewPager.setUserInputEnabled(true);
            
            Log.d(TAG, "ViewPager setup completed with " + pagerAdapter.getItemCount() + " fragments");
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewPager: " + e.getMessage());
        }
    }
    
    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            try {
                if (itemId == R.id.nav_main_page) {
                    viewPager.setCurrentItem(TAB_MAIN_PAGE);
                    setTitle("湘湘管家");
                    return true;
                    
                } else if (itemId == R.id.nav_local_news) {
                    viewPager.setCurrentItem(TAB_LOCAL_NEWS);
                    setTitle("今日时讯");
                    return true;
                    
                } else if (itemId == R.id.nav_dify) {
                    viewPager.setCurrentItem(TAB_DIFY);
                    setTitle("AI助手");
                    return true;
                    
                } else if (itemId == R.id.nav_my) {
                    viewPager.setCurrentItem(TAB_MY);
                    setTitle("个人中心");
                    return true;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error in bottom navigation: " + e.getMessage());
            }
            
            return false;
        });

        // 监听ViewPager滑动，同步底部导航
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                
                switch (position) {
                    case TAB_MAIN_PAGE:
                        bottomNav.setSelectedItemId(R.id.nav_main_page);
                        break;
                    case TAB_LOCAL_NEWS:
                        bottomNav.setSelectedItemId(R.id.nav_local_news);
                        break;
                    case TAB_DIFY:
                        bottomNav.setSelectedItemId(R.id.nav_dify);
                        break;
                    case TAB_MY:
                        bottomNav.setSelectedItemId(R.id.nav_my);
                        break;
                }
            }
        });
    }
    
    /**
     * 创建MainPageFragment
     */
    private Fragment createMainPageFragment() {
        try {
            // 尝试创建真正的MainPageFragment
            Class<?> fragmentClass = Class.forName("com.xiangjia.locallife.ui.fragment.MainPageFragment");
            return (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.w(TAG, "MainPageFragment not found, using SimpleFragment");
            return new SimpleFragment("🏠 首页\n\n主要功能入口");
        }
    }
    
    /**
     * 创建LocalNewsFragment
     */
    private Fragment createLocalNewsFragment() {
        try {
            Class<?> fragmentClass = Class.forName("com.xiangjia.locallife.ui.fragment.LocalNewsFragment");
            return (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.w(TAG, "LocalNewsFragment not found, using SimpleFragment");
            return new SimpleFragment("📰 今日时讯\n\n本地新闻资讯");
        }
    }
    
    /**
     * 创建DifyFragment
     */
    private Fragment createDifyFragment() {
        try {
            Class<?> fragmentClass = Class.forName("com.xiangjia.locallife.ui.fragment.DifyFragment");
            return (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.w(TAG, "DifyFragment not found, using SimpleFragment");
            return new SimpleFragment("🤖 AI助手\n\n智能对话功能");
        }
    }
    
    /**
     * 创建MyFragment
     */
    private Fragment createMyFragment() {
        try {
            Class<?> fragmentClass = Class.forName("com.xiangjia.locallife.ui.fragment.MyFragment");
            return (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.w(TAG, "MyFragment not found, using SimpleFragment");
            return new SimpleFragment("👤 个人中心\n\n个人设置和信息");
        }
    }
    
    /**
     * 备用简单布局
     */
    private void createFallbackLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 60, 40, 40);
        mainLayout.setBackgroundColor(0xFFF8FAFC);
        
        TextView titleText = new TextView(this);
        titleText.setText("🏠 湘湘管家\n(简化模式)");
        titleText.setTextSize(24);
        titleText.setTextColor(0xFF1F2937);
        titleText.setGravity(android.view.Gravity.CENTER);
        titleText.setPadding(0, 0, 0, 40);
        mainLayout.addView(titleText);
        
        TextView infoText = new TextView(this);
        infoText.setText("布局文件加载失败，当前运行在简化模式下。\n请创建必要的布局文件以恢复完整UI。");
        infoText.setTextSize(14);
        infoText.setTextColor(0xFF6B7280);
        infoText.setGravity(android.view.Gravity.CENTER);
        mainLayout.addView(infoText);
        
        setContentView(mainLayout);
    }

    // ViewPager2适配器
    public static class TabPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
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
    
    // 临时简单Fragment
    public static class SimpleFragment extends Fragment {
        private String content;
        
        public SimpleFragment(String content) {
            this.content = content;
        }
        
        @Override
        public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater, 
                                            android.view.ViewGroup container, 
                                            Bundle savedInstanceState) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 80, 40, 40);
            layout.setGravity(android.view.Gravity.CENTER);
            
            android.widget.TextView textView = new android.widget.TextView(getContext());
            textView.setText(content);
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setTextSize(16);
            textView.setTextColor(0xFF374151);
            layout.addView(textView);
            
            return layout;
        }
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
}