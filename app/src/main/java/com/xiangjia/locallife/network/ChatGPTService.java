package com.xiangjia.locallife.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.xiangjia.locallife.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ChatGPT API 服务类
 * 处理与 OpenAI API 的通信，包含完整的错误处理和重试机制
 */
public class ChatGPTService {
    
    private static final String TAG = "ChatGPTService";
    
    // API配置 - 从Constants获取
    private static final String API_KEY = Constants.OPENAI_API_KEY;
    private static final String BASE_URL = Constants.OPENAI_BASE_URL;
    private static final String CHAT_ENDPOINT = "chat/completions";
    
    // 模型配置
    private static final String MODEL = Constants.OPENAI_MODEL;
    private static final int MAX_TOKENS = Constants.MAX_TOKENS;
    private static final double TEMPERATURE = Constants.TEMPERATURE;
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final List<Call> pendingCalls;
    
    public ChatGPTService() {
        // 配置HTTP客户端，借鉴新闻服务的超时设置
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        
        this.gson = new Gson();
        this.pendingCalls = new ArrayList<>();
        
        Log.d(TAG, "ChatGPT服务初始化完成");
    }
    
    /**
     * 发送消息到ChatGPT
     * @param message 用户消息
     * @param conversationId 会话ID（用于上下文管理）
     * @param callback 回调接口
     */
    public void sendMessage(String message, String conversationId, APICallback<String> callback) {
        if (callback != null) {
            callback.onStart();
        }
        
        try {
            // 构建请求体
            ChatRequest chatRequest = buildChatRequest(message, conversationId);
            String jsonBody = gson.toJson(chatRequest);
            
            RequestBody requestBody = RequestBody.create(
                jsonBody, 
                MediaType.get("application/json; charset=utf-8")
            );
            
            // 构建请求
            Request request = new Request.Builder()
                    .url(BASE_URL + CHAT_ENDPOINT)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", Constants.getUserAgent())
                    .post(requestBody)
                    .build();
            
            Log.d(TAG, "发送ChatGPT请求: " + message);
            
            // 异步执行请求
            Call call = httpClient.newCall(request);
            pendingCalls.add(call);
            
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    pendingCalls.remove(call);
                    handleResponse(response, callback);
                }
                
                @Override
                public void onFailure(Call call, IOException e) {
                    pendingCalls.remove(call);
                    handleFailure(e, callback);
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "构建请求失败", e);
            if (callback != null) {
                callback.onError("请求构建失败: " + e.getMessage());
                callback.onFinish();
            }
        }
    }
    
    /**
     * 构建ChatGPT请求对象
     */
    private ChatRequest buildChatRequest(String message, String conversationId) {
        ChatRequest request = new ChatRequest();
        request.model = MODEL;
        request.maxTokens = MAX_TOKENS;
        request.temperature = TEMPERATURE;
        request.stream = false; // 不使用流式响应，避免复杂性
        
        // 构建消息列表
        request.messages = new ArrayList<>();
        
        // 系统提示词，定义AI助手的角色
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.role = "system";
        systemMessage.content = Constants.SYSTEM_PROMPT;
        request.messages.add(systemMessage);
        
        // 用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.role = "user";
        userMessage.content = message;
        request.messages.add(userMessage);
        
        return request;
    }
    
    /**
     * 处理API响应
     */
    private void handleResponse(Response response, APICallback<String> callback) {
        try {
            String responseBody = response.body() != null ? response.body().string() : "";
            
            if (response.isSuccessful()) {
                // 解析成功响应
                ChatResponse chatResponse = gson.fromJson(responseBody, ChatResponse.class);
                
                if (chatResponse != null && chatResponse.choices != null && !chatResponse.choices.isEmpty()) {
                    String aiReply = chatResponse.choices.get(0).message.content;
                    Log.d(TAG, "ChatGPT响应成功: " + aiReply);
                    
                    if (callback != null) {
                        callback.onSuccess(aiReply.trim());
                    }
                } else {
                    Log.e(TAG, "ChatGPT响应格式错误: " + responseBody);
                    if (callback != null) {
                        callback.onError("响应格式错误");
                    }
                }
            } else {
                // 处理HTTP错误
                handleHttpError(response.code(), responseBody, callback);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "解析响应失败", e);
            if (callback != null) {
                callback.onError("响应解析失败: " + e.getMessage());
            }
        } finally {
            if (callback != null) {
                callback.onFinish();
            }
        }
    }
    
    /**
     * 处理HTTP错误响应
     */
    private void handleHttpError(int code, String responseBody, APICallback<String> callback) {
        String errorMessage;
        
        switch (code) {
            case 400:
                errorMessage = "请求参数错误";
                break;
            case 401:
                errorMessage = "API密钥无效，请检查配置";
                break;
            case 403:
                errorMessage = "访问被拒绝，请检查API权限";
                break;
            case 429:
                errorMessage = "请求过于频繁，请稍后再试";
                break;
            case 500:
            case 502:
            case 503:
                errorMessage = "服务器暂时不可用，请稍后再试";
                break;
            case 504:
                errorMessage = "服务器响应超时，请重试";
                break;
            default:
                errorMessage = "请求失败 (错误代码: " + code + ")";
                break;
        }
        
        // 尝试解析错误详情
        try {
            if (responseBody != null && !responseBody.isEmpty()) {
                ErrorResponse errorResponse = gson.fromJson(responseBody, ErrorResponse.class);
                if (errorResponse != null && errorResponse.error != null && errorResponse.error.message != null) {
                    errorMessage += ": " + errorResponse.error.message;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "解析错误响应失败", e);
        }
        
        Log.e(TAG, "HTTP错误 " + code + ": " + errorMessage);
        
        if (callback != null) {
            callback.onError(errorMessage);
        }
    }
    
    /**
     * 处理网络失败
     */
    private void handleFailure(IOException e, APICallback<String> callback) {
        String errorMessage;
        
        if (e instanceof java.net.SocketTimeoutException) {
            errorMessage = "请求超时，请检查网络连接";
        } else if (e instanceof java.net.UnknownHostException) {
            errorMessage = "无法连接到服务器，请检查网络";
        } else if (e instanceof java.net.ConnectException) {
            errorMessage = "网络连接失败";
        } else if (e instanceof javax.net.ssl.SSLException) {
            errorMessage = "安全连接失败，请检查系统时间";
        } else {
            errorMessage = "网络错误: " + e.getMessage();
        }
        
        Log.e(TAG, "网络请求失败: " + errorMessage, e);
        
        if (callback != null) {
            callback.onNetworkError(errorMessage);
            callback.onFinish();
        }
    }
    
    /**
     * 取消所有待处理的请求
     */
    public void cancelPendingRequests() {
        Log.d(TAG, "取消 " + pendingCalls.size() + " 个待处理请求");
        for (Call call : pendingCalls) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
        pendingCalls.clear();
    }
    
    /**
     * 检查API密钥是否配置
     */
    public boolean isApiKeyConfigured() {
        return Constants.isApiKeyConfigured();
    }
    
    // ==================== 数据模型类 ====================
    
    /**
     * ChatGPT请求模型
     */
    private static class ChatRequest {
        @SerializedName("model")
        String model;
        
        @SerializedName("messages")
        List<ChatMessage> messages;
        
        @SerializedName("max_tokens")
        int maxTokens;
        
        @SerializedName("temperature")
        double temperature;
        
        @SerializedName("stream")
        boolean stream;
    }
    
    /**
     * 聊天消息模型
     */
    private static class ChatMessage {
        @SerializedName("role")
        String role; // "system", "user", "assistant"
        
        @SerializedName("content")
        String content;
    }
    
    /**
     * ChatGPT响应模型
     */
    private static class ChatResponse {
        @SerializedName("id")
        String id;
        
        @SerializedName("object")
        String object;
        
        @SerializedName("created")
        long created;
        
        @SerializedName("model")
        String model;
        
        @SerializedName("choices")
        List<Choice> choices;
        
        @SerializedName("usage")
        Usage usage;
    }
    
    /**
     * 响应选项模型
     */
    private static class Choice {
        @SerializedName("index")
        int index;
        
        @SerializedName("message")
        ChatMessage message;
        
        @SerializedName("finish_reason")
        String finishReason;
    }
    
    /**
     * 使用统计模型
     */
    private static class Usage {
        @SerializedName("prompt_tokens")
        int promptTokens;
        
        @SerializedName("completion_tokens")
        int completionTokens;
        
        @SerializedName("total_tokens")
        int totalTokens;
    }
    
    /**
     * 错误响应模型
     */
    private static class ErrorResponse {
        @SerializedName("error")
        ErrorDetail error;
    }
    
    /**
     * 错误详情模型
     */
    private static class ErrorDetail {
        @SerializedName("message")
        String message;
        
        @SerializedName("type")
        String type;
        
        @SerializedName("param")
        String param;
        
        @SerializedName("code")
        String code;
    }
}