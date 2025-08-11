package com.example.yinggutest.mianpage.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.temporaryentity.WXDataEntity;

import java.util.List;

//题库内容部分的recyclerView适配器
public class MainPageArticleAdapter extends BaseQuickAdapter<WXDataEntity.DataBean.DatasBean,MainPageArticleAdapter.ViewHolder> {
    public MainPageArticleAdapter(int layoutResId, @Nullable List<WXDataEntity.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(MainPageArticleAdapter.ViewHolder helper, WXDataEntity.DataBean.DatasBean data) {
        helper.setText(R.id.item_search_pager_author,data.getAuthor());
        helper.setText(R.id.item_search_pager_chapterName,data.getChapterName());
        helper.setText(R.id.item_search_pager_title,data.getTitle());
        helper.setText(R.id.item_search_pager_niceDate,data.getNiceDate());
    }

    public class ViewHolder extends BaseViewHolder {

        TextView mAuthor;
        TextView mChapterName;
        TextView mTitle;
        TextView mNiceData;

        public ViewHolder(View view) {
            super(view);
            mAuthor = itemView.findViewById(R.id.item_search_pager_author);
            mChapterName = itemView.findViewById(R.id.item_search_pager_chapterName);
            mTitle = itemView.findViewById(R.id.item_search_pager_title);
            mNiceData = itemView.findViewById(R.id.item_search_pager_niceDate);
        }
    }
}
