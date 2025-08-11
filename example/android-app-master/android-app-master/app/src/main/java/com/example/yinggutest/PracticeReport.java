package com.example.yinggutest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yinggutest.Util.MyLayoutManager;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.mianpage.fragment.bean.SaveQuestionInfo;
import com.example.yinggutest.mianpage.fragment.everypractice.adapter.PracticeReportRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PracticeReport extends AppCompatActivity {

    private static final String TAG = "PracticeReport";


    //提交时间
    private final  static  String SUBMIT_TIME ="submit_time";
    //答对题目数
    private final  static  String  CORRECT_SUBJECT = "correct_subject";
    //用来存储 提交之后 题目的数据
    private List<SaveQuestionInfo> mList = new ArrayList<>();
    //显示做完题目小圆点
    private RecyclerView rvPracticeReport;
    //显示提交时间
    private TextView tvTime;
    //答对题目数量
    private TextView tvNumber;
    //左上角返回图标
    private ImageView ivReturn;
    //答对题目数
    private int number;
    //错题解析按钮
    private TextView tvWrong;
    //全部解析按钮
    private TextView tvAllQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_report);
        findViews();
        initData();
        initEvent();
    }
    private void findViews() {
        rvPracticeReport = findViewById(R.id.rv_activity_practice_report);
        tvTime = findViewById(R.id.tv_time);
        tvNumber = findViewById(R.id.tv_number_activity_practice_report);
        ivReturn = findViewById(R.id.iv_return_activity_practice_report);
        tvWrong = findViewById(R.id.tv_wrong_topic);
        tvAllQuestion = findViewById(R.id.tv_all_question_activity_practice_report);
    }
    private void initData() {
        //接受来自 AnalogyExaminationActivity 的值
        Intent intent = this.getIntent();
        String submit_time = intent.getStringExtra(SUBMIT_TIME);
        number = intent.getIntExtra(CORRECT_SUBJECT, 0);
        Log.e(TAG, "initData: "+submit_time );
        // 提交时间获取时间
        tvTime.setText(submit_time);

        /*
        通过 SaveQuestionData 中 sSaveQuestionList集合
        来存放题目 并将题目存放到使用集合中
        */
        for (int i = 0; i< SaveQuestionData.sSaveQuestionList.size(); i++){
            //用来存储提交后的题目内容
                mList.add(SaveQuestionData.sSaveQuestionList.get(i));

        }
        //为 显示 原点统计 设置 适配器
        PracticeReportRecyclerViewAdapter practiceReportRecyclerViewAdapter =
                new PracticeReportRecyclerViewAdapter(R.layout.item_practice_report,mList);
        Log.e(TAG, "onCreate: "+ mList);

       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PracticeReport.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPracticeReport.setLayoutManager(linearLayoutManager);*/

       //通过自定义布局文件来为 RecyclerView 设置布局样式
        MyLayoutManager myLayoutManager = new MyLayoutManager();
        rvPracticeReport.setLayoutManager(myLayoutManager);
        rvPracticeReport.setAdapter(practiceReportRecyclerViewAdapter);

        //为答对题目TextView设置题目数
        tvNumber.setText("答对"+number+"题");
        //点击错题解析跳转事件
        tvWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PracticeReport.this, AnalysisErrorQuestion.class));
            }
        });
        tvAllQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PracticeReport.this, AnalusisQuestion.class));
            }
        });
    }
    private void initEvent() {

        //左上角返回图标
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
