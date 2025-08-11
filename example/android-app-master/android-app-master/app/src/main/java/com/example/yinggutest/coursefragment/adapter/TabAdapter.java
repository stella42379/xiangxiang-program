package com.example.yinggutest.coursefragment.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 董优秀 on 2020/9/28.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public TabAdapter(FragmentManager fm, List<Fragment> list) {
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
