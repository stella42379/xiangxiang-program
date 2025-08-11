package com.example.yinggutest.myinfo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yinggutest.R;
import com.example.yinggutest.Util.AnalysisUtils;
import com.example.yinggutest.myinfo.SettingActivity;
import com.example.yinggutest.myinfo.login.LoginActivity;
import com.example.yinggutest.myinfo.login.MD5Utils;

public class ModifyPswActivity extends AppCompatActivity {

    private RelativeLayout title_bars; //标题布局
    private TextView tv_back_bars; //返回按钮
    private TextView tv_main_bars; //标题
    private TextView et_old_psw;  //输入原始的密码
    private TextView et_new_psw;  //输入新的密码
    private TextView et_new_psw_again;  //再次输入新的密码
    private View btn_psw_modify;  //保存按钮
    private String oldPsw,newPsw,newPswAgain; //旧密码，新密码，再次输入新密码的控件获取值
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);

        userName = AnalysisUtils.readLoginUserName(this);
        findViews();
        initEvent();
    }

    private void findViews() {
        //获取main_title_bars布局的对应的ui控件
        title_bars = findViewById(R.id.title_bars);
        tv_back_bars = findViewById(R.id.tv_back_bars);
        tv_main_bars = findViewById(R.id.tv_main_title_bars);
        tv_main_bars.setText("修改密码");
        //获取activity_modify_psw布局相对应的ui控件
        et_old_psw = findViewById(R.id.et_old_psw_modify);
        et_new_psw = findViewById(R.id.et_new_psw_modify);
        et_new_psw_again = findViewById(R.id.et_new_psw_again_modify);
        btn_psw_modify = findViewById(R.id.btn_modify_psw);
    }


    private void initEvent() {
        //返回按钮的点击事件
        tv_back_bars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPswActivity.this.finish();
            }
        });

        //保存按钮的点击事件
        btn_psw_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditString();
                if (TextUtils.isEmpty(oldPsw)){
                    Toast.makeText(ModifyPswActivity.this,"请输入原始密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!MD5Utils.md5(oldPsw).equals(readPsw())){
                    Toast.makeText(ModifyPswActivity.this,"输入的密码和原始密码不一样",Toast.LENGTH_SHORT).show();
                    return;
                }else if (MD5Utils.md5(newPsw).equals(readPsw())){
                    Toast.makeText(ModifyPswActivity.this,"输入的新密码和原始密码不能一样",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPsw)){
                    Toast.makeText(ModifyPswActivity.this,"请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPswAgain)){
                    Toast.makeText(ModifyPswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!newPsw.equals(newPswAgain)){
                    Toast.makeText(ModifyPswActivity.this, "两次输入的新密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(ModifyPswActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();

                    //修改登录成功时保存在SharedPreferences中的密码
                    modifyPsw(newPsw);
                    Intent intent = new Intent(ModifyPswActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SettingActivity.instance.finish();   //关闭设置界面
                    ModifyPswActivity.this.finish();  //关闭本界面
                }
            }
        });
    }
    /**
     * 获取控件上的字符串
     */
    private void getEditString() {
        oldPsw = et_old_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
        newPswAgain = et_new_psw_again.getText().toString().trim();
    }
    //修改登录成功时保存在SharePreferences中的密码
    private void modifyPsw(String newPsw) {
        String md5Psw = MD5Utils.md5(newPsw);   //把密码用md5加密
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();  //获取编译器
        editor.putString(userName,md5Psw);    //保存新密码
        editor.commit();     //提交修改
    }

    /**
     * 从SharedPreferences中读取原始密码
     * @return
     */
    private String readPsw() {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        return spPsw;
    }
}