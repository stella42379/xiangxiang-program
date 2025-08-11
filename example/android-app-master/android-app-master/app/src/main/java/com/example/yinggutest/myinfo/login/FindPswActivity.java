
package com.example.yinggutest.myinfo.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.R;

import org.w3c.dom.Text;

public class FindPswActivity extends AppCompatActivity {

    private RelativeLayout bars_find_psw;   // main_title_bars布局控件
    private TextView tv_back_bars_find_psw;   //返回按钮控件
    private TextView tv_title_bars_find_psw;  //头部控件
    private EditText et_find_phone;  //请输入手机号控件
    private Button btn_find;  //完成按钮控件
    private EditText et_find_checking; //输入验证码控件
    private Button btn_checking_find;   //发送验证码按钮
    private TextView tv_reset_psw;  //隐藏密码显示
    private String userName,findChecking; //获取控件上的字符串
    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);

        //获取从登陆界面和设置界面传递过来的数据
        from = getIntent().getStringExtra("from");
        findViews();
        initData();
    }

    private void findViews() {
        // 寻找 main_title_bars布局的控件
        bars_find_psw = findViewById(R.id.title_bars);
        tv_back_bars_find_psw = findViewById(R.id.tv_back_bars);
        tv_title_bars_find_psw = findViewById(R.id.tv_main_title_bars);
        tv_title_bars_find_psw.setText("找回密码");
        //寻找activity_find_psw布局的控件
        et_find_phone = findViewById(R.id.et_activity_find_phone);
        et_find_checking = findViewById(R.id.et_activity_find_checking);
        btn_checking_find = findViewById(R.id.btn_activity_find_checking);
        tv_reset_psw = findViewById(R.id.tv_reset_psw);
        btn_find = findViewById(R.id.btn_activity_find);
    }

    private void initData() {
        // 返回按钮点击事件
        tv_back_bars_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPswActivity.this.finish();
            }
        });

        //发送验证码按钮的点击事件
        btn_checking_find.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CountDownTimer timer = new CountDownTimer(10000,1000) {
                    @Override
                    public void onTick(long l) {
                        btn_checking_find.setEnabled(false);
                        btn_checking_find.setText("已发送（"+ l / 1000 + "）");
                    }

                    @Override
                    public void onFinish() {
                        btn_checking_find.setEnabled(true);
                        btn_checking_find.setText("重新发送验证码");
                    }
                }.start();
            }
        });

        //找回密码点击事件
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取相应控件上的字符串
                getEditString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(FindPswActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isExistUserName(userName)) {
                    Toast.makeText(FindPswActivity.this, "您输入的用户名已经存在", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(findChecking)) {
                    Toast.makeText(FindPswActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                   //输入的手机号正确，验证码正确，重新给用户设置一个新的密码
                    tv_reset_psw.setVisibility(View.VISIBLE);
                    tv_reset_psw.setText("初始密码：123456");
                    savePsw(userName);
                }
            }
        });
    }


    /**
     * 保存初始化的密码
     * @param
     */
    private void savePsw(String userName) {
        String md5Psw = MD5Utils.md5("123456");  //把密码用md5加密起来
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();  //获取编译器
        editor.putString(userName,md5Psw);
        editor.commit();     //提交修改
    }

    //获取控件上的字符串
    private void getEditString() {
        userName = et_find_phone.getText().toString().trim();
        findChecking = et_find_checking.getText().toString().trim();
    }

    /**
     * 从SharedPreferences中根据用户输入用户名来判断是否有此用户
     * @param userName
     * @return
     */
    private boolean isExistUserName(String userName) {
        boolean hasUserName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString(userName,"");
        if (!TextUtils.isEmpty(spPsw)){
            hasUserName=true;
        }
        return hasUserName;
    }
}