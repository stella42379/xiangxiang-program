package com.example.yinggutest.mianpage.fragment.simulation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.yinggutest.mianpage.fragment.everypractice.AnalogyExaminationActivity;
import com.example.yinggutest.R;

import java.util.List;

public class SimulationAdapter extends BaseAdapter {
    private Context mContext;
    private List<SimulationEntity> testList;
    private static final String TAG = "SimulationAdapter";
    private TextView order;

    public SimulationAdapter(Context mContext, List<SimulationEntity> tests) {
        this.mContext = mContext;
        this.testList = tests;
    }



    @Override
    public int getCount() {
        return testList.size();
    }

    @Override
    public Object getItem(int i) {
        return testList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder vh;
        //复用convertView
        if (view==null){
            vh = new ViewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.item_simulation_list,null);
            vh.order = view.findViewById(R.id.tv_order);
            vh.title = view.findViewById(R.id.tv_exercises_title);
            vh.content=view.findViewById(R.id.tv_content);
            //TODO:后续会追加
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        //获取position对应的Item的数据对象
        final SimulationEntity bean = (SimulationEntity) getItem(position);
        if (bean!=null){
            vh.order.setText(position + 1 + "");
            vh.title.setText(bean.title);
            Log.e(TAG, "getView: "+bean.title);
            vh.content.setText(bean.content);
        }

        //每个Item的点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null)
                    return;
                //TODO:跳转到习题详情页面,暂时，后期需要修改
                Intent intent = new Intent(mContext, AnalogyExaminationActivity.class);
                //把章节Id传递到习题详情页面
                intent.putExtra("id", bean.id);
                //把标题传递到习题详情页面
                intent.putExtra("title", bean.title);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    private class ViewHolder {
        public TextView title,content;
        public TextView order;
    }
}
