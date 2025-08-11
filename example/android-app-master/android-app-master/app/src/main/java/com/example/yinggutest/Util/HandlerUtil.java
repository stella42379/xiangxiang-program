package com.example.yinggutest.Util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by 董优秀 on 2020/9/27.
 */

public class HandlerUtil {

    public static final Handler sHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        sHandler.post(runnable);
    }
}
