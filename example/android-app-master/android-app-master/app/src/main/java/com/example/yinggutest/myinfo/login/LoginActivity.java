package com.example.yinggutest.myinfo.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.MainActivity;
import com.example.yinggutest.R;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_user;//用户名控件
    private EditText edt_psw;//密码控件
    private Button btn_register;//登录按钮
    private TextView tv_register;//立即注册控件
    private TextView tv_find_psw;//找回密码控件
    //用户名，密码的控件的获取值
    private String userName,psw;
    private String spPsw ;
    private Activity mContext;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (readLoginStatus()){
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        }*/
//        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
//        boolean isLogin = sp.getBoolean("isLogin",false);
//        Log.e(TAG, "onCreate: "+isLogin );
        setContentView(R.layout.activity_login);

        //设置此界面为竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();
        initEvent();
    }

    private void findViews() {
        //从activity.register.xml页面布局中获得对应的控件
        edt_user = findViewById(R.id.edt_user_login_activity);
        edt_psw = findViewById(R.id.edt_psw_login_activity);
        btn_register = findViewById(R.id.btn_login_activity);
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
    }

    private void initEvent() {
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPswActivity.class);
                startActivityForResult(intent,1);
            }
        });
        //登录按钮的点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的用户名、密码
                userName = edt_user.getText().toString().trim();
                psw = edt_psw.getText().toString().trim();
                //对密码加密
                String md5Psw = MD5Utils.md5(psw);
                //从SharedPreferences中根据用户名读取密码
                spPsw = readPsw(userName);

                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态和登录的用户名
                    saveLoginStatus(true,userName);
                    //把登录成功的状态专递到MainActivity中
                    Intent data = new Intent(LoginActivity.this, MainActivity.class);
                    data.putExtra("login",true);
                    setResult(RESULT_OK,data);
                    LoginActivity.this.finish();
                    startActivity(data);;
                    return;
                }else if ((!TextUtils.isEmpty(spPsw) &&! md5Psw.equals(spPsw))){
                    Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(LoginActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    /**
     * 从SharedPreferences中根据用户名读取密码
     * @param userName
     * @return
     */
    private String readPsw(String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(userName,"");
    }
    /**
     * 保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo 表示文件名
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit(); //获取编译器
        editor.putBoolean("isLogin",status);
        editor.putString("loginUserName",userName); //存入登录时的用户名
        editor.commit();                        //提交修改
    }

    private boolean readLoginStatus(){
        SharedPreferences sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            //从注册界面专递过来的用户名
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)){
                edt_user.setText(userName);
                //设置光标的位置
                edt_user.setSelection(userName.length());
            }
        }
    }
}