package com.example.yinggutest.myinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yinggutest.R;
import com.example.yinggutest.Util.AnalysisUtils;
import com.example.yinggutest.Util.ToastUtil;
import com.example.yinggutest.myinfo.activity.AboutUsActivity;
import com.example.yinggutest.myinfo.activity.FindbackActivity;
import com.example.yinggutest.myinfo.activity.UserInfoActivity;
import com.example.yinggutest.myinfo.login.LoginActivity;

public class MyInfoFragment extends Fragment {

    //头布局
    private LinearLayout ll_head;
    //头像
    public ImageView iv_head_icon;
    //用户名
    private TextView tv_user_name;
    //账户信息
    private RelativeLayout rl_user_info;
    private RelativeLayout rl_setting;
    private Context mContext;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_about_us;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_view_myinfo_activity, null, false);
        ll_head = inflate.findViewById(R.id.ll_head);
        iv_head_icon = inflate.findViewById(R.id.iv_head_icon);
        tv_user_name = inflate.findViewById(R.id.tv_user_name);
        rl_user_info = inflate.findViewById(R.id.rl_user_info);
        rl_setting = inflate.findViewById(R.id.rl_setting);
        rl_feedback = inflate.findViewById(R.id.rl_feedback);
        rl_about_us = inflate.findViewById(R.id.rl_about_us);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        setLoginParams(readLoginStatus());
        initEvent();
    }

    private void initEvent() {
        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 点击头像后判断是否已登录
                //未登录跳转到登录界面
                //登录跳转到个人资料界面
                ToastUtil.showToast(mContext, "头像被点击了");
                //判断是否已经登录
                if (readLoginStatus()) {
                    //已登录跳转到个人资料界面
//                    Intent intent=new Intent(this,UserInfoActivity.class);
//                    startActivity(intent);
                } else {
                    //未登录跳转到登录界面
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        rl_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readLoginStatus()) {
                    //TODO: 跳转到账户信息界面
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(mContext, "您还未登陆，请先登录");
                }

            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readLoginStatus()) {
                    //跳转到设置界面
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    //返回登录状态
                    startActivityForResult(intent, 1);
                } else {
                    ToastUtil.showToast(mContext, "您还未登陆，请先登录");
                }

            }
        });
        rl_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FindbackActivity.class);
                startActivity(intent);
            }
        });
        rl_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * 根据登录状态初始化我的界面
     */
    public void setLoginParams(boolean isLogin) {
        if (isLogin) {
            tv_user_name.setText(AnalysisUtils.readLoginUserName(mContext));
        }
    }

    /**
     * 从SharedPreferences中读取登录状态
     */
    private boolean readLoginStatus() {
        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    /**
     * 退出登录后重置
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!readLoginStatus()) {
            tv_user_name.setText("登录/注册");
        }
    }
}
