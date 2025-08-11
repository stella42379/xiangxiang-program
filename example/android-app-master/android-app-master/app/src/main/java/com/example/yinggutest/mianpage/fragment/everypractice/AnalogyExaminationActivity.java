package com.example.yinggutest.mianpage.fragment.everypractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yinggutest.PracticeReport;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.ConstantData;
import com.example.yinggutest.Util.ConstantUtil;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.Util.ViewPagerScroller;
import com.example.yinggutest.mianpage.fragment.bean.AnSwerInfo;
import com.example.yinggutest.mianpage.fragment.bean.ErrorQuestionInfo;
import com.example.yinggutest.mianpage.fragment.bean.SaveQuestionInfo;
import com.example.yinggutest.mianpage.fragment.everypractice.adapter.ExaminationSubmitAdapter;
import com.example.yinggutest.mianpage.fragment.everypractice.view.CountDownCircleView;
import com.example.yinggutest.mianpage.fragment.everypractice.view.VoteSubmitViewPager;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class AnalogyExaminationActivity extends AppCompatActivity {

    private static final String TAG = "AnalogyExaminationActiv";
    //传递提交时间
    private final  static  String SUBMIT_TIME ="submit_time";
    //传递答对题目数
    private final  static  String  CORRECT_SUBJECT = "correct_subject";
    //题目ViewPage
    private VoteSubmitViewPager viewPager;
    //用来存放题目数据
    private List<AnSwerInfo> dataItems = new ArrayList<>();
    //用来存放View控件数据
    List<View> viewItems = new ArrayList<View>();
    private int mode = 1;
    //图片地址
    String imgServerUrl = "";
    //题目ViewPage适配器
    ExaminationSubmitAdapter pagerAdapter;
    //用来保存提交后所有题目的数据
    public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();
    //用来保存提交后错题数据
    public List<ErrorQuestionInfo> errorQuestionInfo = new ArrayList<ErrorQuestionInfo>();



    private String pageCode;
    private int pageScore;
    private int errortopicNums;// 错题数
    private int errortopicNums1;// 错题数
    private String isPerfectData = "1";// 是否完善资料0完成 1未完成
    private String type = "0";// 0模拟 1竞赛
    private String errorMsg = "";


    private Handler handlerSubmit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    //获取当前时间
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    String format = simpleDateFormat.format(date);
                    Toast.makeText(AnalogyExaminationActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                    //跳转到 结算页面
                    Intent intent = new Intent(AnalogyExaminationActivity.this, PracticeReport.class);
                    //传时间过去
                    intent.putExtra(SUBMIT_TIME,format);
                    //总的题目数减去错题 为对的题目数
                    int number  = questionInfos.size()-errortopicNums;
                    //传错题数过去
                    intent.putExtra(CORRECT_SUBJECT,number);
                    startActivity(intent);
                    //倒计时停止
                    ccvBlack.stopCountDown(false);
                    finish();
                    break;
                default:
                    break;
            }

        }
    };

    //返回图标
    private ImageView ivBlack;
    //倒计时控件
    private CountDownCircleView ccvBlack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analogy_examination);
        initView();
        loadData();


    }

    private void loadData() {
        /**
         * public String questionId; // 试题主键
         * 	public String questionName; // 试题题目
         * 	public String QuestionFor; // （0模拟试题，1竞赛试题）
         * 	public String questionType; // 试题类型
         * 	public String analysis; // 试题分析
         * 	public String correctAnswer; // 正确答案
         * 	public String optionA; // 正确答案A
         * 	public String optionB; // 正确答案B
         * 	public String optionC; // 正确答案C
         * 	public String optionD; // 正确答案D
         * 	public String optionE; // 正确答案E
         * 	public String score; // 分值
         * 	public String option_type; // 是否是图片题0是1否
         * 	public String isSelect; // 是否选择0是1否
         * 	public String mySelect;
         * 	public String testNo;
         * 	public String videoName;
         */
//        AnSwerInfo anSwerInfo = new AnSwerInfo();
//        anSwerInfo.setQuestionId(1);
//        anSwerInfo.setQuestionName("物理治疗不包含（  ）");
//        anSwerInfo.setQuestionFor("0");
//        anSwerInfo.setQuestionType("0");
//        anSwerInfo.setAnalysis("试题分析");
//        anSwerInfo.setCorrectAnswer("C");
//        anSwerInfo.setOptionA("A.超声波疗法");
//        anSwerInfo.setOptionB("B.运动疗法");
//        anSwerInfo.setOptionC("C.环境改造");
//        anSwerInfo.setOptionD("D.热疗法");
//        anSwerInfo.setScore("2");
//        anSwerInfo.setOption_type("1");
//        anSwerInfo.setIsSelect("0");




        for (int i = 0; i < ConstantData.answerName.length; i++) {
            AnSwerInfo info = new AnSwerInfo();
            info.setQuestionId(ConstantData.answerId[i]);// 试题主键
            info.setQuestionName(ConstantData.answerName[i]);// 试题题目
            info.setQuestionType(ConstantData.answerType[i]);// 试题类型0单选1多选
            info.setQuestionFor("0");// （0模拟试题，1竞赛试题）
            info.setAnalysis(ConstantData.answerAnalysis[i]);// 试题分析
            info.setCorrectAnswer(ConstantData.answerCorrect[i]);// 正确答案
            info.setOptionA(ConstantData.answerOptionA[i]);// 试题选项A
            info.setOptionB(ConstantData.answerOptionB[i]);// 试题选项B
            info.setOptionC(ConstantData.answerOptionC[i]);// 试题选项C
            info.setOptionD(ConstantData.answerOptionD[i]);// 试题选项D
            info.setOptionE(ConstantData.answerOptionE[i]);// 试题选项E
            info.setScore(ConstantData.answerScore[i]);// 分值
            info.setOption_type(ConstantData.answerOptionType[i]);//是否有图片
            info.setUrl(ConstantData.answerOptionTypeImageUrl[i]);

            if (ConstantData.answerOptionType[i] == "1") {

          /*  }
            if (i == ){*/
                info.setOption_type("1");
            }else {
                info.setOption_type("0");
            }

            dataItems.add(info);
        }

        for (int i = 0; i < dataItems.size(); i++) {
            viewItems.add(getLayoutInflater().inflate(
                    R.layout.vote_submit_viewpager_item, null));
        }
        //创建适配器
        pagerAdapter = new ExaminationSubmitAdapter(
                AnalogyExaminationActivity.this, viewItems,
                dataItems, imgServerUrl);
        //设置适配器
        viewPager.setAdapter(pagerAdapter);

        viewPager.getParent()
                .requestDisallowInterceptTouchEvent(false);

    }

    private void initView() {
        viewPager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
        initViewPagerScroll();
        ivBlack = findViewById(R.id.iv_black_activity_analogy_examination);
        ivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnalogyExaminationActivity.this)
                        .setTitle("来自系统的疑问")
                        .setMessage("当前正在考试，确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ccvBlack.stopCountDown(false);
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();

            }
        });
        ccvBlack = findViewById(R.id.ccv_black_activity_analogy_examination);
        ccvBlack.startCountDown();
        ccvBlack.setAddCountDownListener(new CountDownCircleView.OnCountDownFinishListener() {
            @Override
            public void countDownFinished(boolean isStop) {
                if(isStop){
                    // TODO: 2020/11/17 跳转到结果页
                    startActivity(new Intent(AnalogyExaminationActivity.this,PracticeReport.class));
                    finish();
                }else {
                    finish();
                }
            }
        });
    }

    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }


    // 提交试卷
    public void uploadExamination(int errortopicNum) {
        String resultlist = "[";
        errortopicNums = errortopicNum;

        if (questionInfos.size() > 0) {
            //选择过题目
            //全部选中
            if (questionInfos.size() == dataItems.size()) {
                for (int i = 0; i < questionInfos.size(); i++) {
                    if (i == questionInfos.size() - 1) {
                        resultlist += questionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (questionInfos.size() == 0) {
                        resultlist += "]";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(questionInfos.get(i).getScore());
                        pageScore += score;
                    }
                }
            } else {
                //部分选中
                for (int i = 0; i < dataItems.size(); i++) {
                    if (dataItems.get(i).getIsSelect() == null) {
                        errortopicNums1 += 1;
                        //保存数据
                        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                        questionInfo.setQuestionId(dataItems.get(i).getQuestionId());
                        questionInfo.setQuestionType(dataItems.get(i).getQuestionType());
                        questionInfo.setRealAnswer(dataItems.get(i).getCorrectAnswer());
                        questionInfo.setScore(dataItems.get(i).getScore());
                        questionInfo.setIs_correct(ConstantUtil.isError);
                        questionInfos.add(questionInfo);
                    }
                }

                for (int i = 0; i < dataItems.size(); i++) {
                    if (i == dataItems.size() - 1) {
                        resultlist += questionInfos.get(i).toString() + "]";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (dataItems.size() == 0) {
                        resultlist += "]";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(questionInfos.get(i).getScore());
                        pageScore += score;
                    }
                }
            }
        } else {
            //没有选择题目
            for (int i = 0; i < dataItems.size(); i++) {
                if (dataItems.get(i).getIsSelect() == null) {
                    errortopicNums1 += 1;
                    //保存数据
                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(dataItems.get(i).getQuestionId());
                    questionInfo.setQuestionType(dataItems.get(i).getQuestionType());
                    questionInfo.setRealAnswer(dataItems.get(i).getCorrectAnswer());
                    questionInfo.setScore(dataItems.get(i).getScore());
                    questionInfo.setIs_correct(ConstantUtil.isError);
                    questionInfos.add(questionInfo);
                }
            }

            for (int i = 0; i < dataItems.size(); i++) {
                if (i == dataItems.size() - 1) {
                    resultlist += questionInfos.get(i).toString() + "]";
                } else {
                    resultlist += questionInfos.get(i).toString() + ",";
                }
                if (dataItems.size() == 0) {
                    resultlist += "]";
                }
                if (questionInfos.get(i).getIs_correct()
                        .equals(ConstantUtil.isCorrect)) {
                    int score = Integer.parseInt(questionInfos.get(i).getScore());
                    pageScore += score;
                }
            }
        }

        System.out.println("提交的已经选择的题目数组给后台====" + resultlist);
        //用来存储提交后的所有题目数据
        SaveQuestionData.sSaveQuestionList.addAll(questionInfos);
        //用来存储提交后的错题数据
        SaveQuestionData.sSaveErrorQuestionList.addAll(errorQuestionInfo);


        //用来存储提交后的错题(用于错题本)
        SaveQuestionData.sSaveAllErrorQuestionList.addAll(errorQuestionInfo);
        // 利用list中的元素创建HashSet集合，此时set中进行了去重操作
        HashSet<ErrorQuestionInfo> set = new HashSet<ErrorQuestionInfo>(SaveQuestionData.sSaveAllErrorQuestionList);
        // 清空list集合
        SaveQuestionData.sSaveAllErrorQuestionList.clear();
        // 将去重后的元素重新添加到list中
        SaveQuestionData.sSaveAllErrorQuestionList.addAll(set);


        Message msg = handlerSubmit.obtainMessage();
        msg.what = 1;
        handlerSubmit.sendMessage(msg);

    }
}