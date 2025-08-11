package com.example.yinggutest.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 董优秀 on 2020/9/29.
 */

public class OkHttpUtil {

    public static void enqueue(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void enqueue(String url, final MainThreadCallBack callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                HandlerUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String result = response.body().string();
                HandlerUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(call, result);
                    }
                });
            }
        });
    }


    public interface MainThreadCallBack {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, String result);
    }
}
