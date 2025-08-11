package com.example.yinggutest.Util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 自动换行布局管理
 * Created by chengxiakuan on 2016/10/14.
 */
public class MyLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    private int mTotalHeight = 0;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        int sumWidth = getWidth();

        int curLineWidth = 0, curLineTop = 0;
        int lastLineMaxHeight = 0;
        int offsetY = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            curLineWidth += width;
            if (curLineWidth <= sumWidth) {//不需要换行
                layoutDecorated(view, curLineWidth - width, curLineTop, curLineWidth, curLineTop + height);
                //比较当前行多有item的最大高度
                lastLineMaxHeight = Math.max(lastLineMaxHeight, height);
                if(i == 0){
                    offsetY = lastLineMaxHeight;
                }
            } else {//换行
                curLineWidth = width;
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height;
                }
                //记录当前行top
                curLineTop += lastLineMaxHeight;

                layoutDecorated(view, 0, curLineTop, width, curLineTop + height);
                lastLineMaxHeight = height;
                offsetY += height;
            }
        }
        mTotalHeight = Math.max(offsetY , getVerticalSpace());
    }

    private int getVerticalSpace(){
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int mSumDy = 0;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int travel = dy;

        if(mSumDy + dy < 0){
            travel = -mSumDy;
        }else if(mSumDy + dy > mTotalHeight - getVerticalSpace()){
            travel = mTotalHeight - getVerticalSpace() - mSumDy;
        }
        mSumDy += travel;
        offsetChildrenVertical(-travel);
        return dy;
    }
}