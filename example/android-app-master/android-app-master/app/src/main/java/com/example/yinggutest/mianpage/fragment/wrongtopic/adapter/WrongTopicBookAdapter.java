package com.example.yinggutest.mianpage.fragment.wrongtopic.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.mianpage.fragment.bean.ErrorQuestionInfo;

import java.util.List;

public class WrongTopicBookAdapter extends BaseQuickAdapter<ErrorQuestionInfo, BaseViewHolder> {
    public WrongTopicBookAdapter(int layoutResId, @Nullable List<ErrorQuestionInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ErrorQuestionInfo item) {

        helper.setText(R.id.tv_title_number_item_wrong_topic_book,"第"+item.getQuestionId()+"题");
        helper.setText(R.id.tv_content_item_wrong_topic_book,item.getQuestionName());

    }
}
