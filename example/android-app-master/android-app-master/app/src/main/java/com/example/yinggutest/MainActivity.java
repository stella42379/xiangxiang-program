package com.example.yinggutest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yinggutest.coursefragment.CourseFragment;
import com.example.yinggutest.mianpage.fragment.MainPageFragment;
import com.example.yinggutest.mianpage.fragment.mainpageadapter.MainTabAdapter;
import com.example.yinggutest.myinfo.MyInfoFragment;

import java.util.ArrayList;
import java.util.List;

import com.example.yinggutest.forum.ForumFragment;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<TextView> mTabList = new ArrayList<>();


    //顶部标题
    private TextView tvTitle;
    //顶部搜索小图标
    private ImageView ivTopSearch;
    //fragment位置
    private ViewPager vpFragment;
    //底部按钮栏
    private LinearLayout llTab;
    //底部图库按钮
    private TextView tvTestBank;
    //底部课程按钮
    private TextView tvCourse;
    //底部论坛按钮
    private TextView tvForum;
    //底部我的按钮
    private TextView tvMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();
        initEvent();
    }

    private void initEvent() {
        tvTestBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFragment.setCurrentItem(0);
                tvTitle.setText("题库");
                setSelectTab(0);
            }
        });
        tvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFragment.setCurrentItem(1);
                tvTitle.setText("课程");
                setSelectTab(1);
            }
        });
        tvForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFragment.setCurrentItem(2);
                tvTitle.setText("论坛");
                setSelectTab(2);
            }
        });
        tvMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFragment.setCurrentItem(3);
                tvTitle.setText("我的");
                setSelectTab(3);
            }
        });
        //顶部搜索图标点击事件
        ivTopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mTabList.add(tvTestBank);
        mTabList.add(tvCourse);
        mTabList.add(tvForum);
        mTabList.add(tvMine);

        tvTestBank.setSelected(true);

        MainPageFragment mainPageFragment = new MainPageFragment();
        MyInfoFragment myInfoFragment = new MyInfoFragment();
        CourseFragment courseFragment = new CourseFragment();
        ForumFragment forumFragment = new ForumFragment();
        //填充fragment
        mFragmentList.add(mainPageFragment);
        mFragmentList.add(courseFragment);
        mFragmentList.add(forumFragment);
        mFragmentList.add(myInfoFragment);

        //首页fragment适配器
        MainTabAdapter mainTabAdapter = new MainTabAdapter(getSupportFragmentManager(), mFragmentList);
        vpFragment.setAdapter(mainTabAdapter);
    }

    private void findView() {
        tvTitle = findViewById(R.id.tv_top_title_activity_main);
        ivTopSearch = findViewById(R.id.iv_top_search_activity_main);
        vpFragment = findViewById(R.id.vp_fragment_activity_main);
        llTab = findViewById(R.id.ll_tab_activity_main);
        tvTestBank = findViewById(R.id.tv_test_bank_activity_main);
        tvCourse = findViewById(R.id.tv_course_activity_main);
        tvForum = findViewById(R.id.tv_forum_activity_main);
        tvMine = findViewById(R.id.tv_mine_activity_main);
    }

    public void setSelectTab(int SelectedPosition){
        int size = mTabList.size();
        for(int i = 0;i<size;i++){
            if (i == SelectedPosition) {
                mTabList.get(i).setSelected(true);
            }else {
                mTabList.get(i).setSelected(false);
            }
        }
    }

}
