package com.example.yinggutest.myinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.R;
import com.example.yinggutest.myinfo.activity.ModifyPswActivity;
import com.example.yinggutest.myinfo.activity.SecurityActivity;
import com.example.yinggutest.myinfo.login.LoginActivity;


public class SettingActivity extends AppCompatActivity{
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_modify_psw,rl_security_setting,rl_exit_login;
    public static SettingActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        instance=this;
        findViews();
        initData();
        initEvent();
    }
    private void findViews() {
        tv_main_title= findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back= findViewById(R.id.tv_back);
        rl_title_bar= findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        rl_modify_psw= findViewById(R.id.rl_modify_psw);
        rl_security_setting= findViewById(R.id.rl_security_setting);
        rl_exit_login=findViewById(R.id.rl_exit_login);
    }
    private void initData() {
    }

    private void initEvent() {
        //返回上一个界面，即我的界面
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });

        //修改密码的点击事件
        rl_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 跳转到修改密码界面
                Intent intent = new Intent(SettingActivity.this, ModifyPswActivity.class);
                startActivity(intent);
            }
        });

        //设置密保的点击事件
        rl_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 跳转到设置密保界面
                Intent intent = new Intent(SettingActivity.this, SecurityActivity.class);
                startActivity(intent);
            }
        });
        //退出登录的点击事件
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                clearLoginStatus();//清除登录状态和登录时的用户名
                //退出登录成功后把退出成功的状态传递到MainActivity中
                Intent intent =new Intent();
                intent.putExtra("isLogin", false);
                setResult(RESULT_OK, intent);
                SettingActivity.this.finish();
                //点击退出登录跳转到登录界面
                Intent exit = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(exit);
            }
        });
    }

    /**
     * 清除SharedPreferences中的登录状态和登录时的用户名
     */
    private void clearLoginStatus(){
        SharedPreferences sp=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin", false);
        editor.putString("loginUserName", "");
        editor.commit();//提交修改
    }
}
