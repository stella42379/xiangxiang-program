package com.example.yinggutest.coursefragment;

;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.yinggutest.coursefragment.adapter.TabAdapter;
import com.example.yinggutest.coursefragment.data.sSaveData;
import com.example.yinggutest.temporaryentity.WXArticleEntity;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    //顶部指示器
    private SlidingTabLayout stlTab;
    //顶部指示器菜单“加号”按钮
    private ImageView ivAddButton;
    //显示“课程”内容的ViewPager部分
    private ViewPager vpContainer;
    //将课程内容的fragment添加到列表中
    private List<Fragment> mFragments = new ArrayList<>();
    //改变之前的最后一个数
    private String number = "";
    //改变之前的长度
    private int  sizze = 0;

    @Override
    public void onResume() {
        super.onResume();
        int size = sSaveData.mStlTab.size();
        if (size>0) {
            String str = sSaveData.mStlTab.get(size-1).getName();
//number != str ||  size != sizze
            if (  size != sizze ) {
                mFragments.clear();
                for (int i = 0; i < sSaveData.mStlTab.size(); i++) {
                    CourseArticleFragment courseArticleFragment = CourseArticleFragment.newInstance(sSaveData.mStlTab.get(i).getId(), sSaveData.mStlTab.get(i).getName());
                    mFragments.add(courseArticleFragment);
                }
                initViewPagerAndTabLayout(sSaveData.mStlTab);

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_course_main, null, false);
        stlTab = inflate.findViewById(R.id.stl_tab_fragment_course_main);
        ivAddButton = inflate.findViewById(R.id.iv_add_fragment_course_main);
        vpContainer = inflate.findViewById(R.id.vp_container_fragment_course_main);
        //点击事件
        initEvent();
        return inflate;
    }
    private void initEvent() {
        ivAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestTab();
    }



    //内容暂时
    private void requestTab() {
        OkHttpUtil.enqueue(TemporaryApiInterface.PUBLIC_TAB_URL, new OkHttpUtil.MainThreadCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String result) {
                Gson gson = new Gson();
                WXArticleEntity wxArticleEntity = gson.fromJson(result, WXArticleEntity.class);
                if (wxArticleEntity.getErrorCode()==0) {
                    mFragments.clear();

                    List<WXArticleEntity.DataBean> data = wxArticleEntity.getData();
                    int size = data.size();
                    if (sSaveData.mStlTab.size()==0) {
                        for (int i = 0;i < size;i++){
                            WXArticleEntity.DataBean dataBean = data.get(i);
                            sSaveData.mStlTab.add(dataBean);

                        }
                        int y = sSaveData.mStlTab.size();
                        number = sSaveData.mStlTab.get(y-1).getName();
                        sizze = sSaveData.mStlTab.size();
                    }

                    for(int i=0;i<sSaveData.mStlTab.size();i++){
                    CourseArticleFragment courseArticleFragment = CourseArticleFragment.newInstance(sSaveData.mStlTab.get(i).getId(), sSaveData.mStlTab.get(i).getName());
                    mFragments.add(courseArticleFragment);}
                    initViewPagerAndTabLayout(sSaveData.mStlTab);
                }

            }
        });
    }

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

        stlTab.setViewPager(vpContainer);
    }


}
