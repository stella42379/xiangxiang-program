package com.example.yinggutest.mianpage.fragment.truetitle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.ConstantData;
import com.example.yinggutest.Util.MyLayoutManager;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.mianpage.fragment.bean.TruetitleInfo;
import com.example.yinggutest.mianpage.fragment.everypractice.AnalogyExaminationActivity;
import com.example.yinggutest.mianpage.fragment.truetitle.adapter.TrueTitleRecyclerAdapter;
import com.example.yinggutest.mianpage.fragment.truetitle.adapter.TrueTitleSortRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrueTitleActivity extends AppCompatActivity {

    private static final String TAG = "TrueTitleActivity";
    //左边分类部分
    private RecyclerView rvLeft;
    //右边具体真题部分
    private RecyclerView rvRight;
    //右侧真题部分暂存区
    List<TruetitleInfo> mListIDTitle = new ArrayList<>();
    //返回图标
    private ImageView ivBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_title);
        rvLeft = findViewById(R.id.rv_left_activity_true_title);
        rvRight = findViewById(R.id.rv_right_activity_true_title);
        ivBlack = findViewById(R.id.tv_black_activity_true_title);
        initData();
        initEvent();

    }

    private void initEvent() {
        ivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {

        //存储真题的集合先清零
        SaveQuestionData.sTrueQuestionList.clear();
        for(int i = 0; i < ConstantData.trueSort.length; i++){
            TruetitleInfo truetitleInfo = new TruetitleInfo();
            truetitleInfo.setTrueSort(ConstantData.trueSort[i]);
            SaveQuestionData.sTrueQuestionList.add(truetitleInfo);
        }

        //左侧分类部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TrueTitleActivity.this);
        rvLeft.setLayoutManager(linearLayoutManager);
        TrueTitleSortRecyclerAdapter trueTitleSortRecyclerAdapter =
                new TrueTitleSortRecyclerAdapter(R.layout.item_true_title_sort_recycler_adapter, SaveQuestionData.sTrueQuestionList);
        rvLeft.setAdapter(trueTitleSortRecyclerAdapter);

        //右侧部分第一次进入默认赋值为第一条
        for(int i=0;i<ConstantData.XingCe.length;i++){
            TruetitleInfo truetitleInfo = new TruetitleInfo();
            truetitleInfo.setTrueTile(ConstantData.XingCe[i]);
            mListIDTitle.add(truetitleInfo);
        }
        MyLayoutManager myLayoutManager = new MyLayoutManager();
        rvRight.setLayoutManager(myLayoutManager);
        TrueTitleRecyclerAdapter trueTitleRecyclerAdapter = new TrueTitleRecyclerAdapter(R.layout.item_true_title_recycler_adapter, mListIDTitle);
        rvRight.setAdapter(trueTitleRecyclerAdapter);
        //首次加载时点击真题跳转操作
        trueTitleRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到每日一练界面
                startActivity(new Intent(TrueTitleActivity.this, AnalogyExaminationActivity.class));
            }
        });

        //左侧分类部分点击事件
        trueTitleSortRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击时清除
                mListIDTitle.clear();
                int id =  ConstantData.trueSortID[position];
                switch (id){
                    case 1:
                        for(int i=0;i<ConstantData.XingCe.length;i++){
                            TruetitleInfo truetitleInfo = new TruetitleInfo();
                            truetitleInfo.setTrueTile(ConstantData.XingCe[i]);
                            mListIDTitle.add(truetitleInfo);
                        }
                        break;
                    case 2:
                        for (int i=0;i<ConstantData.Zonghe.length;i++){
                            TruetitleInfo truetitleInfo = new TruetitleInfo();
                            truetitleInfo.setTrueTile(ConstantData.Zonghe[i]);
                            mListIDTitle.add(truetitleInfo);                        }
                        break;
                    case 3:
                        for (int i=0;i<ConstantData.Siji.length;i++){
                            TruetitleInfo truetitleInfo = new TruetitleInfo();
                            truetitleInfo.setTrueTile(ConstantData.Siji[i]);
                            mListIDTitle.add(truetitleInfo);                            }
                        break;
                    case 4:
                        for (int i=0;i<ConstantData.Gongji.length;i++){
                            TruetitleInfo truetitleInfo = new TruetitleInfo();
                            truetitleInfo.setTrueTile(ConstantData.Gongji[i]);
                            mListIDTitle.add(truetitleInfo);                            }
                        break;
                    case 5:
                        for (int i=0;i<ConstantData.JiaoBian.length;i++){
                            TruetitleInfo truetitleInfo = new TruetitleInfo();
                            truetitleInfo.setTrueTile(ConstantData.JiaoBian[i]);
                            mListIDTitle.add(truetitleInfo);                            }
                        break;
                    default:
                        break;
                }
                TrueTitleRecyclerAdapter trueTitleRecyclerAdapter1 = new TrueTitleRecyclerAdapter(R.layout.item_true_title_recycler_adapter, mListIDTitle);
                rvRight.setAdapter(trueTitleRecyclerAdapter1);
                trueTitleRecyclerAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //跳转到每日一练界面
                        startActivity(new Intent(TrueTitleActivity.this, AnalogyExaminationActivity.class));
                    }
                });
            }



        });


    }

}

