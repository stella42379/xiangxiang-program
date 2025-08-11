package com.xiangjia.locallife.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.xiangjia.locallife.util.Constants;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网络配置管理类
 * 统一管理网络请求配置，借鉴新闻界面的最佳实践
 */
public class NetworkConfig {
    
    private static final String TAG = "NetworkConfig";
    private static final String PREF_NAME = "network_config";
    
    private static NetworkConfig instance;
    private Context context;
    private OkHttpClient httpClient;
    private SharedPreferences preferences;
    
    private NetworkConfig(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initHttpClient();
    }
    
    public static NetworkConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (NetworkConfig.class) {
                if (instance == null) {
                    instance = new NetworkConfig(context);
                }
            }
        }
        return instance;
    }
    
    /**
     * 初始化HTTP客户端
     */
    private void initHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        
        // 添加请求头拦截器
        builder.addInterceptor(new HeaderInterceptor());
        
        // 添加网络状态检查拦截器
        builder.addInterceptor(new NetworkCheckInterceptor(context));
        
        // 添加错误重试拦截器
        builder.addInterceptor(new RetryInterceptor());
        
        // 调试模式下添加日志拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        
        this.httpClient = builder.build();
        
        Log.d(TAG, "HTTP客户端初始化完成");
    }
    
    /**
     * 获取HTTP客户端
     */
    public OkHttpClient getHttpClient() {
        return httpClient;
    }
    
    /**
     * 检查网络连接状态
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    
    /**
     * 获取网络类型
     */
    public String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return networkInfo.getTypeName();
            }
        }
        return "UNKNOWN";
    }
    
    /**
     * 保存API密钥
     */
    public void saveApiKey(String apiKey) {
        preferences.edit()
                .putString(Constants.PREF_API_KEY, apiKey)
                .apply();
    }
    
    /**
     * 获取API密钥
     */
    public String getApiKey() {
        return preferences.getString(Constants.PREF_API_KEY, Constants.OPENAI_API_KEY);
    }
    
    /**
     * 请求头拦截器
     */
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws java.io.IOException {
            Request originalRequest = chain.request();
            
            Request.Builder builder = originalRequest.newBuilder()
                    .addHeader("User-Agent", Constants.getUserAgent())
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive");
            
            return chain.proceed(builder.build());
        }
    }
    
    /**
     * 网络状态检查拦截器
     */
    private static class NetworkCheckInterceptor implements Interceptor {
        private Context context;
        
        public NetworkCheckInterceptor(Context context) {
            this.context = context;
        }
        
        @Override
        public Response intercept(Chain chain) throws java.io.IOException {
            ConnectivityManager cm = (ConnectivityManager) 
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    throw new java.io.IOException("网络连接不可用");
                }
            }
            
            return chain.proceed(chain.request());
        }
    }
    
    /**
     * 重试拦截器
     */
    private static class RetryInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws java.io.IOException {
            Request request = chain.request();
            Response response = null;
            
            int retryCount = 0;
            while (retryCount < Constants.MAX_RETRY_COUNT) {
                try {
                    response = chain.proceed(request);
                    
                    // 如果响应成功，直接返回
                    if (response.isSuccessful()) {
                        return response;
                    }
                    
                    // 对于某些错误码进行重试
                    int code = response.code();
                    if (code == 500 || code == 502 || code == 503 || code == 504) {
                        retryCount++;
                        if (retryCount < Constants.MAX_RETRY_COUNT) {
                            Log.w(TAG, "服务器错误 " + code + "，进行第 " + retryCount + " 次重试");
                            response.close();
                            
                            // 等待一段时间后重试
                            try {
                                Thread.sleep(Constants.RETRY_DELAY_MS * retryCount);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                            continue;
                        }
                    }
                    
                    // 不需要重试的错误，直接返回
                    return response;
                    
                } catch (java.io.IOException e) {
                    retryCount++;
                    if (retryCount >= Constants.MAX_RETRY_COUNT) {
                        throw e;
                    }
                    
                    Log.w(TAG, "网络请求失败，进行第 " + retryCount + " 次重试: " + e.getMessage());
                    
                    // 等待一段时间后重试
                    try {
                        Thread.sleep(Constants.RETRY_DELAY_MS * retryCount);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                }
            }
            
            return response;
        }
    }
}