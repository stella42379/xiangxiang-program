package com.example.yinggutest.mianpage.fragment.truetitle.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.mianpage.fragment.bean.TruetitleInfo;


import java.util.List;

public class TrueTitleRecyclerAdapter extends BaseQuickAdapter<TruetitleInfo, BaseViewHolder> {
    public TrueTitleRecyclerAdapter(int layoutResId, @Nullable List<TruetitleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TruetitleInfo item) {
           helper.setText(R.id.tv_content_item_true_recycler_adapter,item.getTrueTile());
    }
}
