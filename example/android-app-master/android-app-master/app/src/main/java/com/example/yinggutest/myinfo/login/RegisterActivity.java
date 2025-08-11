package com.example.yinggutest.myinfo.login;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.R;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText et_phone; //用户名控件
    private EditText et_checking; //验证码控件
    private EditText et_psw; //密码控件
    private Button btn_checking;//获取验证码控件
    private Button btn_register;//注册控件
    private String userName,psw,checking; //用户名，验证码，密码控件的获取值
    private RelativeLayout title_bars;  //标题布局
    private TextView tv_back_register;   //返回按钮
    private TextView tv_title_register;   //标题控件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        initEvent();
    }

    private void findViews() {
        //获取相应的控件
        title_bars = findViewById(R.id.title_bars);
        tv_back_register = findViewById(R.id.tv_back_bars);
        tv_title_register = findViewById(R.id.tv_main_title_bars);
        tv_title_register.setText("立即注册");
        et_phone = findViewById(R.id.et_activity_register_phone);
        et_checking = findViewById(R.id.et_activity_register_checking);
        et_psw = findViewById(R.id.et_activity_register_psw);
        btn_checking = findViewById(R.id.btn_activity_checking);
        btn_register = findViewById(R.id.btn_activity_register);
    }

    private void initEvent() {

        //返回按钮的点击事件
        tv_back_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });

        //发送验证码的点击事件
        btn_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneText = et_phone.getText().toString().trim();
                Log.e(TAG, "onTick: "+et_phone.getText() );
                Toast.makeText(RegisterActivity.this,phoneText,Toast.LENGTH_SHORT).show();

                CountDownTimer timer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btn_checking.setEnabled(false);
                        btn_checking.setText("已发送(" + millisUntilFinished / 1000 + ")");
                    }

                    @Override
                    public void onFinish() {
                        btn_checking.setEnabled(true);
                        btn_checking.setText("重新获取验证码");

                    }
                }.start();
            }
        });


        //注册按钮的点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取相应控件中的字符串
                getEditString();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(checking)){
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this, "此手机号已经注册", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //把手机号和密码保存到SharedPreferences中
                    saveRegisterInfo(userName,psw);
                    //注册成功后把用户名传递到LoginActivity中
                    Intent data = new Intent();
                    data.putExtra("userName",userName);
                    setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                }
            }

            /**
             * 获取控件中的字符串
             */
            private void getEditString() {
                userName = et_phone.getText().toString().trim();
                checking = et_checking.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
            }

            /**
             * 从sharedPreferences中读取输入的用户名，并判断此用户是否存在
             * @param userName
             * @return
             */
            private boolean isExistUserName(String userName) {
                boolean has_userName = false;
                SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
                String spPsw = sp.getString(userName,"");
                if (!TextUtils.isEmpty(spPsw)){
                    has_userName=true;
                }
                return has_userName;
            }

            /**
             * 保存用户名和密码到SharedPreferences中
             * @param userName
             * @param psw
             */
            private void saveRegisterInfo(String userName, String psw) {
                String md5Psw = MD5Utils.md5(psw); //把密码用MD5加密
                //loginInfo 表示文件名
                SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();  //获取编译器
                editor.putString(userName,md5Psw);
                editor.commit();   //提交修改
            }
        });
    }
}