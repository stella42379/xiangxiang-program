package com.example.yinggutest.myinfo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yinggutest.R;

public class AboutUsActivity extends AppCompatActivity {
    private TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        findviews();
        initData();
        initEvent();
    }

    private void findviews() {
        tv_back =  findViewById(R.id.tv_back);
    }

    private void initData() {

    }

    private void initEvent() {
        //返回上一个界面，即我的界面
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }
}
