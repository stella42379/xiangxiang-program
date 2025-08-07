package com.xiangjia.locallife.network;

/**
 * 网络回调接口
 * 定义网络请求的回调方法
 */
public interface APICallback<T> {
    
    /**
     * 请求成功回调
     */
    void onSuccess(T response);

    void onError(String errorMessage); 
    
    /**
     * 请求失败回调
     */
    void onFailure(String errorMessage);
    
    /**
     * 网络错误回调
     */
    void onNetworkError(String errorMessage);
    
    /**
     * 请求开始回调
     */
    void onStart();
    
    /**
     * 请求结束回调
     */
    void onFinish();
    
    /**
     * 进度回调
     */
    void onProgress(int progress);
}
