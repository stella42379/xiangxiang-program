package com.example.yinggutest.mianpage.fragment.everypractice.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.mianpage.fragment.bean.SaveQuestionInfo;

import java.util.List;

/*PracticeReport 中小圆点统计题目数 适配器*/
public class PracticeReportRecyclerViewAdapter extends BaseQuickAdapter<SaveQuestionInfo,BaseViewHolder> {

    public PracticeReportRecyclerViewAdapter(int layoutResId, @Nullable List<SaveQuestionInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SaveQuestionInfo item) {
        if (item.getIs_correct().equals("0")) {
            helper.setBackgroundRes(R.id.tv_item_practice_report,R.drawable.tv_item_practice_report_error);
            helper.setText(R.id.tv_item_practice_report,item.getQuestionId());
        }else {
            helper.setText(R.id.tv_item_practice_report,item.getQuestionId());
        }

    }
}
