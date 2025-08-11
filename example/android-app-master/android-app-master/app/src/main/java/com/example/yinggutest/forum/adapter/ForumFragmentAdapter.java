package com.example.yinggutest.forum.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;

import java.util.List;

import com.example.yinggutest.forum.entity.AnForumDataInfo;

public class ForumFragmentAdapter extends BaseQuickAdapter<AnForumDataInfo, BaseViewHolder> {
    public ForumFragmentAdapter(int layoutResId, @Nullable List<AnForumDataInfo> data) {
        super(layoutResId, data);




    }

    @Override
    protected void convert(BaseViewHolder helper, AnForumDataInfo item) {

        helper.setText(R.id.tv_user_name_item_fragment_forum_page,item.getUserName());
        helper.setText(R.id.tv_content_item_fragment_forum_page,item.getUserContent());
        Glide.with(mContext).load(item.getUserImageUrl()).
                into((ImageView) helper.getView(R.id.iv_url_item_fragment_forum_page));
        helper.setText(R.id.tv_time_item_fragment_forum_page,"发布时间："+item.getUserTime());
        helper.setText(R.id.tv_number_item_fragment_forum_page,item.getUserNumber()+"");

        helper.addOnClickListener(R.id.tv_content_item_fragment_forum_page)
                .addOnClickListener(R.id.iv_like_item_fragment_forum_page);


    }
}
