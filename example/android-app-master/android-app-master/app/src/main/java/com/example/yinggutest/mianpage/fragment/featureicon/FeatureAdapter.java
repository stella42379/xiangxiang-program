package com.example.yinggutest.mianpage.fragment.featureicon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yinggutest.R;

import java.util.List;

public class FeatureAdapter extends BaseQuickAdapter<FeatureIconEntity,FeatureAdapter.ViewHolder> {
    public FeatureAdapter(int layoutResId, @Nullable List<FeatureIconEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(FeatureAdapter.ViewHolder helper, FeatureIconEntity item) {
        helper.setText(R.id.tv_name_feature,item.getFeatureName());
        helper.setImageResource(R.id.iv_icon_feature,item.getFeatureIcon());
        helper.addOnClickListener(R.id.ll_feature);
    }

    public class ViewHolder extends BaseViewHolder {

        TextView mIconName;
        ImageView mIconImage;

        public ViewHolder(View view) {
            super(view);
            mIconImage = itemView.findViewById(R.id.iv_icon_feature);
            mIconName = itemView.findViewById(R.id.tv_name_feature);
        }
    }
}
