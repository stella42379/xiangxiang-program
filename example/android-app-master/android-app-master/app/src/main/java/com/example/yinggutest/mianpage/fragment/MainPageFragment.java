package com.example.yinggutest.mianpage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yinggutest.R;
import com.example.yinggutest.Util.OkHttpUtil;
import com.example.yinggutest.constant.TemporaryApiInterface;
import com.example.yinggutest.temporaryentity.WXArticleEntity;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

//题库主页面
public class MainPageFragment extends Fragment {

    //题库页面顶部指示器
    private SlidingTabLayout srlTab;
    //题库页面顶部指示器小加号
    private ImageView ivAddButton;
    //题库主要内容的容器
    private ViewPager vpContainer;
    //用于存放fragment
    private List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_page_new, null, false);
        srlTab = inflate.findViewById(R.id.stl_tab_fragment_main_page_new);
        ivAddButton = inflate.findViewById(R.id.iv_add_fragment_main_page_new);
        vpContainer = inflate.findViewById(R.id.vp_container_fragment_main_page_new);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //请求顶部指示器的数据
        requestTab();
    }

    private void requestTab() {
        OkHttpUtil.enqueue(TemporaryApiInterface.PUBLIC_TAB_URL, new OkHttpUtil.MainThreadCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String result) {
                WXArticleEntity wxArticleEntity = new Gson().fromJson(result, WXArticleEntity.class);
                if (wxArticleEntity.getErrorCode() == 0) {
                    mFragments.clear();
                    List<WXArticleEntity.DataBean> data = wxArticleEntity.getData();
                    for (int i = 0;i<data.size();i++){
                        WXArticleEntity.DataBean dataBean = data.get(i);
                        MainPageArticleFragment mainPageArticleFragment = MainPageArticleFragment.newInstance(dataBean.getId(), dataBean.getName());
                        mFragments.add(mainPageArticleFragment);
                    }
                    initViewPagerAndTabLayout(data);
                }
            }
        });
    }

    //将viewpager与顶部指示器关联
    private void initViewPagerAndTabLayout(List<WXArticleEntity.DataBean> tabAuthors) {
        vpContainer.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments == null? 0 :mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabAuthors.get(position).getName();
            }
        });

        srlTab.setViewPager(vpContainer);
    }
}
