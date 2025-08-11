package com.example.yinggutest.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yinggutest.R;

import com.example.yinggutest.forum.data.sSaveForumData;

public class ForumFragmentSencond extends AppCompatActivity {

    private static final String USERNAME = "username";


    private ImageView ivBlack;
    private TextView tvUserName;
    private TextView tvUserContent;
    private ImageView ivUrl;
    private TextView tvUerTime;
    private TextView tvNumber;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_fragment_sencond);
        findViews();
        initData();
        initEvent();
    }

    private void findViews() {
        ivBlack = findViewById(R.id.iv_black_activity_forum_fragment_sencond);
        tvUserName = findViewById(R.id.tv_user_name_activity_forum_fragment_sencond);
        tvUserContent = findViewById(R.id.tv_user_content_activity_forum_fragment_sencond);
        ivUrl = findViewById(R.id.iv_url_activity_forum_fragment_sencond);
        tvUerTime = findViewById(R.id.tv_user_time_activity_forum_fragment_sencond);
        tvNumber = findViewById(R.id.tv_number_activity_forum_fragment_senconf);
    }

    private void initData() {
        Intent intent  = getIntent();
        position = intent.getIntExtra(USERNAME,0);
        tvUserName.setText(sSaveForumData.sAnforumdataInfo.get(position).getUserName());
        tvUserContent.setText(sSaveForumData.sAnforumdataInfo.get(position).getUserContent());
        String imageUrl = sSaveForumData.sAnforumdataInfo.get(position).getUserImageUrl();
        Glide.with(ForumFragmentSencond.this).load(imageUrl).into(ivUrl);
        tvUerTime.setText("发布时间:"+sSaveForumData.sAnforumdataInfo.get(position).getUserTime());
        tvNumber.setText("获赞数:"+sSaveForumData.sAnforumdataInfo.get(position).getUserNumber());

    }

    private void initEvent() {
        ivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
