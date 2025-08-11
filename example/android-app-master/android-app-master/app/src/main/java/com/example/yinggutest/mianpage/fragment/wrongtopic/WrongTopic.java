package com.example.yinggutest.mianpage.fragment.wrongtopic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yinggutest.R;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.Util.ViewPagerScroller;
import com.example.yinggutest.mianpage.fragment.wrongtopic.adapter.WrongTopicAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WrongTopic extends AppCompatActivity {

    private static final String TAG = "WrongTopic";
    private static final String TILTE = "title";
    //显示内容ViewPage
    private ViewPager vpAnalysis;
    //用来存放控件
    List<View> viewItems = new ArrayList<View>();
    //用来存放图片地址
    String imgServerUrl = "";
    //返回图标
    private ImageView ivBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_topic);
        findViews();
        initData();
        initEvent();
    }

    private void findViews() {
        vpAnalysis = findViewById(R.id.vp_activity_wrong_topic);
        ivBlack = findViewById(R.id.iv_black_activity_wrong_topic);
        initViewPagerScroll();
    }

    private void initData() {
        Intent intent = getIntent();
        int number = intent.getIntExtra(TILTE, 0);
        for (int i = 0; i < SaveQuestionData.sSaveQuestionList.size(); i++) {
            viewItems.add(getLayoutInflater().inflate(R.layout.item_wrong_topic, null));
        }
        WrongTopicAdapter examinationSubmitAdapter =
                new WrongTopicAdapter(WrongTopic.this,viewItems,number,imgServerUrl);
       /* MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter();
        vpAnalysis.setAdapter(myViewPageAdapter);*/
        //设置适配器
        vpAnalysis.setAdapter(examinationSubmitAdapter);

        vpAnalysis.getParent()
                .requestDisallowInterceptTouchEvent(false);

    }

    public void setCurrentView(int index) {
        vpAnalysis.setCurrentItem(index);
    }
    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(vpAnalysis.getContext());
            mScroller.set(vpAnalysis, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }
    private void initEvent() {

        ivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
