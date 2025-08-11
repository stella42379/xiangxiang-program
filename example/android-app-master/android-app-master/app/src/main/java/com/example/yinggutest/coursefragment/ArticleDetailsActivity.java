package com.example.yinggutest.coursefragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.R;

public class ArticleDetailsActivity extends AppCompatActivity {

    private TextView mBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_article_details);
        findViews();
        initEvent();
    }

    private void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailsActivity.this.finish();
            }
        });
    }

    private void findViews() {
        mBack = findViewById(R.id.tv_back);
    }
}
