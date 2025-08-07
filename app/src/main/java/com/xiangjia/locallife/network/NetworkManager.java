package com.xiangjia.locallife.network;

import com.xiangjia.locallife.network.APICallback;

public class NetworkManager {
    
    // 网络管理器单例
    
    private static NetworkManager instance;
    private DifyAPIService difyService;
    
    private NetworkManager() {
        difyService = new DifyAPIService();
    }
    
    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * 发送消息给Dify AI
     * 与小程序保持完全一致的调用方式
     */
    public void sendMessageToAI(String message, String userId, String conversationId, AIResponseCallback callback) {
        // 使用DifyAPIService的chatWithAI方法
        difyService.chatWithAI(message, conversationId != null ? conversationId : "", new APICallback<String>() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response, conversationId);
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
            
            @Override
            public void onFailure(String errorMessage) {
                callback.onError(errorMessage);
            }
            
            @Override
            public void onNetworkError(String errorMessage) {
                callback.onError("网络错误：" + errorMessage);
            }
            
            @Override
            public void onStart() {
                // 可以在这里显示加载状态
            }
            
            @Override
            public void onFinish() {
                // 可以在这里隐藏加载状态
            }
            
            @Override
            public void onProgress(int progress) {
                // 可以在这里更新进度
            }
        });
    }
    
    /**
     * AI响应回调接口
     */
    public interface AIResponseCallback {
        void onSuccess(String response, String conversationId);
        void onError(String error);
    }
}