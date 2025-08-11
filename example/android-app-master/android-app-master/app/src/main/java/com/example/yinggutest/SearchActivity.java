package com.example.yinggutest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yinggutest.Util.ToastUtil;



public class SearchActivity extends AppCompatActivity {

    //返回上一级
    private TextView tvback;
    //搜索按钮
    private Button btnsearch;
    //搜索框
    private EditText etsearch;
    //删除按钮
    private ImageView ivdelete;
    private ListView mlistview;
    Context context;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;

        findView();
        initData();
        initEvent();
    }

    private void findView() {
        tvback = findViewById(R.id.tv_back);
        btnsearch = findViewById(R.id.btn_Search);
        etsearch = findViewById(R.id.et_search);
        ivdelete = findViewById(R.id.iv_delete);
        mlistview = findViewById(R.id.listview);
    }

    private void initData() {

    }

    private void initEvent() {
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
        ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //内容为空
                etsearch.setText("");
                //把listview隐藏
                mlistview.setVisibility(View.GONE);
            }
        });
        //搜索框的监听事件
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //文本改变时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏删除图片
                    ivdelete.setVisibility(View.GONE);
                } else {
                    //显示删除图片
                    ivdelete.setVisibility(View.VISIBLE);
                    showListView();
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.showToast(context, etsearch.getText().toString());
                //如果输入框内容为空，提示请输入搜索内容
                if (TextUtils.isEmpty(etsearch.getText().toString().trim())) {
                    ToastUtil.showToast(context, "请输入您要搜索的内容");
                } else {
                    //涉及后台数据库
                    //判断cursor是否为空
                    if (cursor != null) {
                        int columnCount = cursor.getCount();
                        if (columnCount == 0) {
                            ToastUtil.showToast(context, "对不起，没有您要搜索的内容");
                        }
                    }
                }
            }
        });
    }

    private void showListView() {
        mlistview.setVisibility(View.VISIBLE);
        //获得输入的内容
        String str = etsearch.getText().toString().trim();
        //获取数据库对象
//        MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
//        SQLiteDatabase db = myOpenHelper.getReadableDatabase();
//        //得到cursoor
//        cursor = db.rawQuery("select * from lol where name like '%" + str + "%'", null);
//        MyListViewCursorAdapter adapter = new MyListViewCursorAdapter(context, cursor);
//
//        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把cursor移动到指定行
//                cursor.moveToPosition(Position);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                ToastUtil.showToast(context, name);

            }
        });
    }
}
