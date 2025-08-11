package com.example.yinggutest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.Util.ViewPagerScroller;
import com.example.yinggutest.mianpage.fragment.everypractice.adapter.AnalusisQuestionErrorAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AnalysisErrorQuestion extends AppCompatActivity {

    //显示页面ViewPage
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
        setContentView(R.layout.activity_analysis_error_question);
        findViews();
        initData();
        initEvent();
    }

    private void findViews() {
        vpAnalysis = findViewById(R.id.vp_activity_analysis_error_question);
        ivBlack = findViewById(R.id.iv_black_analusis_error_question);
        initViewPagerScroll();
    }

    private void initData() {
        for (int i = 0; i < SaveQuestionData.sSaveErrorQuestionList.size(); i++) {
            viewItems.add(getLayoutInflater().inflate(R.layout.item_analusis_question_adapter, null));
        }
        AnalusisQuestionErrorAdapter examinationSubmitAdapter =
                new AnalusisQuestionErrorAdapter(AnalysisErrorQuestion.this,viewItems,SaveQuestionData.sSaveErrorQuestionList,imgServerUrl);
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
