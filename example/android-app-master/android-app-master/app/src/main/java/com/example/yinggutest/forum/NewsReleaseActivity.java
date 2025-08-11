package com.example.yinggutest.forum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yinggutest.R;
import com.example.yinggutest.forum.adapter.PhotoGridAdapter;
import com.globusltd.recyclerview.view.OnItemClickListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsReleaseActivity extends AppCompatActivity {

    private static final String TAG = "NewsReleaseActivity";

    private EditText etArticle;
    private TextView tvSave;
    private RecyclerView rvPhotoGrid;

    private NewsReleaseEntity newsReleaseEntity;

    private ArrayList<String> mList = new ArrayList<>();
    private PhotoGridAdapter photoGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_release);
        findViews();
        initData();
        initEvent();

    }

    private void findViews() {

        tvSave = findViewById(R.id.tv_save);
        etArticle = findViewById(R.id.edit_text_news_release_activity);
        rvPhotoGrid = findViewById(R.id.rv_photo_grid);
    }

    private void initData() {
        //单例模式创建对象
        if (newsReleaseEntity == null) {
            newsReleaseEntity = new NewsReleaseEntity();
        }
        //初始化发布者（获取用户名）
        newsReleaseEntity.userName = "用户昵称";
        //初始化点赞数
        newsReleaseEntity.hits = 0;

        if (mList != null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            rvPhotoGrid.setLayoutManager(gridLayoutManager);
            photoGridAdapter = new PhotoGridAdapter(NewsReleaseActivity.this, mList);
            rvPhotoGrid.setAdapter(photoGridAdapter);
        }
    }

    private void initEvent() {

        //保存按钮的点击事件（发布）
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
        Date date = new Date();
//        String time = date.toLocaleString();
        String time = date.toString();
        Log.i("md", "时间time为： "+time);  //md: 时间time为： Wed Nov 18 07:05:11 GMT+00:00 2020
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月dd日 HH时mm分");
        String sim = dateFormat.format(date);
        Log.i("md", "时间sim为： "+sim);  //md: 时间sim为： 2020年-11月18日-07时05分11秒 周三
*/
                Date date = new Date();
                String time = date.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月dd日 HH时mm分");
                String sim = dateFormat.format(date);
                Log.i("md", "时间sim为： "+sim);  //md: 时间sim为： 2020年-11月18日-07时05分11秒 周三

                //发布时间
                newsReleaseEntity.time = sim;
                //发布者
               newsReleaseEntity.article = etArticle.getText().toString();

                if (mList.size()!=0) {
                    newsReleaseEntity.pictureUrl = mList.get(0);
                }


                Log.e(TAG, "onCreate: newsReleaseEntity.article ="
                        +newsReleaseEntity.article);

               // Intent intent = new Intent(NewsReleaseActivity.this, ForumFragmentSencond.class);
                //intent.putExtra("entity", newsReleaseEntity.toString());
//                NewsReleaseActivity.this.setResult(RESULT_OK,intent);
           //     startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String imageUri = data.getStringExtra("TakePhotosActivity_images");
//            Glide.with(this).load(new File(imageUri)).into();
           // mList.addAll()
            mList.add(imageUri);
            photoGridAdapter.notifyDataSetChanged();
            Log.e(TAG, "onActivityResult: "+imageUri);
        }
    }


}