package com.xiangjia.locallife;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
// 修改这一行 - 删除 com.local.locallife 的导入
// import com.local.locallife.BaseActivity;
// import com.local.locallife.R;
import androidx.appcompat.app.AppCompatActivity; 
import com.xiangjia.locallife.ui.fragment.DifyFragment;
import com.xiangjia.locallife.ui.fragment.LocalNewsFragment;
import com.xiangjia.locallife.ui.fragment.MainPageFragment;
import com.xiangjia.locallife.ui.fragment.MyFragment;
import java.util.ArrayList;
import java.util.List;

// 直接继承 AppCompatActivity 而不是 BaseActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d(TAG, "MainActivity onCreate");

        initViews();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_navigation);
        
        if (viewPager == null) {
            Log.e(TAG, "ViewPager not found! Check R.layout.activity_main");
            return;
        }
        
        if (bottomNav == null) {
            Log.e(TAG, "BottomNavigation not found!");
            return;
        }
    }

    private void setupViewPager() {
        try {
            pagerAdapter = new TabPagerAdapter(this);
            
            // 临时添加简单的 Fragment，等其他 Fragment 修复后再替换
            pagerAdapter.addFragment(new SimpleFragment("首页"), "首页");
            pagerAdapter.addFragment(new SimpleFragment("今日时讯"), "今日时讯");
            pagerAdapter.addFragment(new SimpleFragment("AI助手"), "AI助手");
            pagerAdapter.addFragment(new SimpleFragment("个人中心"), "个人中心");

            viewPager.setAdapter(pagerAdapter);
            viewPager.setUserInputEnabled(true);
            
            Log.d(TAG, "ViewPager setup completed with " + pagerAdapter.getItemCount() + " fragments");
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewPager: " + e.getMessage());
        }
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        // 刷新当前Fragment（如果实现了RefreshableFragment接口）
        try {
            int currentItem = viewPager.getCurrentItem();
            Fragment currentFragment = pagerAdapter.getFragment(currentItem);
            
            if (currentFragment instanceof RefreshableFragment) {
                ((RefreshableFragment) currentFragment).onRefresh();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing current fragment: " + e.getMessage());
        }
    }

    // ViewPager2 适配器
    private static class TabPagerAdapter extends FragmentStateAdapter {
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

    // 可刷新Fragment接口
    public interface RefreshableFragment {
        void onRefresh();
    }
    
    // 临时简单Fragment
    public static class SimpleFragment extends Fragment {
        private String title;
        
        public SimpleFragment(String title) {
            this.title = title;
        }
        
        @Override
        public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater, 
                                            android.view.ViewGroup container, 
                                            Bundle savedInstanceState) {
            android.widget.TextView textView = new android.widget.TextView(getContext());
            textView.setText(title + " - 功能开发中...");
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setTextSize(18);
            return textView;
        }
    }
}