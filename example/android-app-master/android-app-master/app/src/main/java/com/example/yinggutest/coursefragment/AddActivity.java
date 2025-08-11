package com.example.yinggutest.coursefragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.MyLayoutManager;
import com.example.yinggutest.coursefragment.adapter.BottomRecyclerAdapter;
import com.example.yinggutest.coursefragment.adapter.TopRecyclerViewAdapter;
import com.example.yinggutest.coursefragment.data.sSaveData;

public class AddActivity extends AppCompatActivity {

    //顶部删除部分
    private RecyclerView rvTop;
    //返回按钮
    private ImageView ivBack;
    //底部添加部分
    private RecyclerView rvBottom;
    //顶部适配器
    private TopRecyclerViewAdapter topRecyclerViewAdapter;
    //底部适配器
    private BottomRecyclerAdapter bottomRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findViews();
        initData();
        initEvent();
    }

    private void findViews() {
        ivBack = findViewById(R.id.back_activity_add);
        rvTop = findViewById(R.id.rv_top_activity_add);
        rvBottom = findViewById(R.id.rv_bottom_activity_add);
    }

    private void initData() {

        MyLayoutManager myLayoutManager = new MyLayoutManager();
        rvTop.setLayoutManager(myLayoutManager);
        MyLayoutManager myLayoutManager1 = new MyLayoutManager();
        rvBottom.setLayoutManager(myLayoutManager1);
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddActivity.this);
        rvTop.setLayoutManager(linearLayoutManager);*/
        topRecyclerViewAdapter = new TopRecyclerViewAdapter(R.layout.item_course_recycler_top_adapter, sSaveData.mStlTab);
        rvTop.setAdapter(topRecyclerViewAdapter);

        bottomRecyclerAdapter = new BottomRecyclerAdapter(R.layout.item_course_recycler_bottom_adapter, sSaveData.mStlAfter);
        rvBottom.setAdapter(bottomRecyclerAdapter);
    }

    private void initEvent() {
        topRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sSaveData.mStlAfter.add(sSaveData.mStlTab.get(position));
                sSaveData.mStlTab.remove(position);
                topRecyclerViewAdapter.notifyDataSetChanged();
                bottomRecyclerAdapter.notifyDataSetChanged();

            }
        });
        bottomRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sSaveData.mStlTab.add(sSaveData.mStlAfter.get(position));
                sSaveData.mStlAfter.remove(position);
                topRecyclerViewAdapter.notifyDataSetChanged();
                bottomRecyclerAdapter.notifyDataSetChanged();

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 finish();
            }
        });
    }


}
