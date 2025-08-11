package com.example.yinggutest.mianpage.fragment.truetitle.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.mianpage.fragment.bean.TruetitleInfo;


import java.util.List;

public class TrueTitleSortRecyclerAdapter extends BaseQuickAdapter<TruetitleInfo, BaseViewHolder> {
    public TrueTitleSortRecyclerAdapter(int layoutResId, @Nullable List<TruetitleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TruetitleInfo item) {
        helper.setText(R.id.tv_sort_item_true_sort_recycler_adapter,item.getTrueSort());
    }
}
