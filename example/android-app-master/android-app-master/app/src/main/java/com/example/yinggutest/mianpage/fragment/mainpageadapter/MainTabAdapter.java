package com.example.yinggutest.mianpage.fragment.mainpageadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 董优秀 on 2020/9/28.
 */

//首页fragment适配器
public class MainTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public MainTabAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
