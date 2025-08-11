package com.example.yinggutest.coursefragment.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;
import com.example.yinggutest.temporaryentity.WXDataEntity;

import java.util.List;

public class ArticleListAdapter extends BaseQuickAdapter<WXDataEntity.DataBean.DatasBean, ArticleListAdapter.WXViewHolder> {


    public ArticleListAdapter(int layoutResId, @Nullable List<WXDataEntity.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final WXViewHolder helper, WXDataEntity.DataBean.DatasBean article) {
            helper.setText(R.id.tv_title_item_article_adapter,article.getTitle());
            helper.setText(R.id.tv_year_item_article_adapter, article.getNiceDate());
    }

    public static class WXViewHolder extends BaseViewHolder {

        TextView tvAuthor;
        TextView tvNiceDate;

        public WXViewHolder(View view) {
            super(view);
            tvAuthor = itemView.findViewById(R.id.tv_title_item_article_adapter);
            tvNiceDate = itemView.findViewById(R.id.tv_year_item_article_adapter);
        }
    }

}
