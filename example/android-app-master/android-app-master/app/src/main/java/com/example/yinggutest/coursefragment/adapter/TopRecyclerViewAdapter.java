package com.example.yinggutest.coursefragment.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.temporaryentity.WXArticleEntity;

import java.util.List;

public class TopRecyclerViewAdapter extends BaseQuickAdapter<WXArticleEntity.DataBean, BaseViewHolder> {

    public TopRecyclerViewAdapter(int layoutResId, @Nullable List<WXArticleEntity.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WXArticleEntity.DataBean item) {
        helper.setText(R.id.top_item_course_recycler_top_adapter,item.getName());
    }
}
