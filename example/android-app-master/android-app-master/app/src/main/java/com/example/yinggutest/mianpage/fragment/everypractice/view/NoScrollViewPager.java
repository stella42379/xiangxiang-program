package com.example.yinggutest.mianpage.fragment.everypractice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    /**
     * 触摸事件
     * 返回true   代表消耗掉  我拿着这个触摸事件用掉了，谁都别想用了
     * 返回false  代表不处理  这个触摸事件对我来说没大用，给子控件用好了
     * @param arg0
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    /**
     * 拦截触摸事件
     * 返回true拦截        子控件无法获取到触摸事件
     * 返回false不拦截     子控件可以获取到触摸事件
     * @param arg0
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //false 去除滚动效果
        super.setCurrentItem(item,false);
    }
}