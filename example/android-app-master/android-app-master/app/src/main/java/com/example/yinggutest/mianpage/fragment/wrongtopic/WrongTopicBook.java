package com.example.yinggutest.mianpage.fragment.wrongtopic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.mianpage.fragment.wrongtopic.adapter.WrongTopicBookAdapter;

public class WrongTopicBook extends AppCompatActivity {

    private static final String TAG = "WrongTopicBook";
    //用来传递具体哪个题的id
    private static final String TILTE = "title";
    //错题列表
    private RecyclerView rvWrongContent;
    //返回图标
    private ImageView ivBlack;
    //全部清除按钮
    private TextView tvClean;
    //RecyclerView 适配器
    private WrongTopicBookAdapter wrongTopicBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_topic_book);
        findViews();
        initData();
        initEvent();
    }

    private void findViews() {
        rvWrongContent = findViewById(R.id.rv_content_activity_wrong_topic_book);
        ivBlack = findViewById(R.id.iv_balck_activity_wrong_topic_book);
        tvClean = findViewById(R.id.tv_clean_activity_wrong_topic_book);
    }

    private void initData() {
        rvWrongContent.setLayoutManager(new LinearLayoutManager(WrongTopicBook.this));
        wrongTopicBookAdapter = new WrongTopicBookAdapter(R.layout.item_wrong_topic_book, SaveQuestionData.sSaveAllErrorQuestionList);
        rvWrongContent.setAdapter(wrongTopicBookAdapter);

    }

    private void initEvent() {
        //返回图标点击事件
        ivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //清除点击事件
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WrongTopicBook.this)
                .setTitle("来自系统的疑问")
                        .setMessage("确定要清空？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SaveQuestionData.sSaveAllErrorQuestionList.clear();
                                wrongTopicBookAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消",null);
                        builder.show();
            }
        });
        //适配器点击事件
        wrongTopicBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView tvTitle = view.findViewById(R.id.tv_title_number_item_wrong_topic_book);
                Intent intent = new Intent(WrongTopicBook.this, WrongTopic.class);
                intent.putExtra(TILTE,position);
                startActivity(intent);
            }
        });

        wrongTopicBookAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WrongTopicBook.this)
                        .setTitle("来自系统的疑问")
                        .setMessage("确定删除此错题？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SaveQuestionData.sSaveAllErrorQuestionList.remove(position);
                                wrongTopicBookAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
                return false;
            }
        });


    }
}
