package com.example.yinggutest.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 董优秀 on 2020/10/20.
 */

public class ToastUtil {
    public static void showToast(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show();
    }
}
