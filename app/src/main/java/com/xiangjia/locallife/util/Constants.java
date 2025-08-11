package com.xiangjia.locallife.util;

public class Constants {
    
    // ========== Dify AI配置 ==========
    public static final String DIFY_API_URL = "https://api.dify.ai/v1/";
    public static final String DIFY_TOKEN = "app-Yxrt1HmArkaDiwQo9fu9lebA";
    
    // ========== ChatGPT API配置 (新增) ==========
    // 请替换为您的实际OpenAI API密钥
    public static final String OPENAI_API_KEY = "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";
    public static final String OPENAI_BASE_URL = "https://api.openai.com/v1/";
    public static final String OPENAI_MODEL = "gpt-3.5-turbo"; // 或 "gpt-4"
    
    // 备用API配置（如果使用代理或其他服务）
    public static final String BACKUP_API_URL = "https://your-proxy-server.com/api/";
    
    // ========== 湘湘管家配置 ==========
    public static final String APP_NAME = "湘湘管家";
    public static final String VERSION = "1.0.0";
    public static final String MANAGER_NAME = "湘湘管家";
    public static final String MANAGER_AVATAR_URL = "https://seal-img.nos-jd.163yun.com/obj/w5rCgMKVw6DCmGzCmsK-/61129759279/b41e/16dc/1cec/d90af993a9ae8166b985515140810f3b.png";
    
    // ========== 服务类型常量 ==========
    public static final String SERVICE_REPAIR = "故障报修";
    public static final String SERVICE_INSPECTION = "日常检查";
    public static final String SERVICE_PROGRESS = "进度追踪";
    public static final String SERVICE_OUTAGE = "停水停电";
    public static final String SERVICE_WEATHER = "天气预警";
    public static final String SERVICE_MEDICAL = "紧急送医";
    public static final String SERVICE_HOSPITAL = "附近医院";
    
    // ========== 反馈类型 ==========
    public static final String FEEDBACK_FUNCTION = "功能建议";
    public static final String FEEDBACK_BUG = "问题反馈";
    public static final String FEEDBACK_SERVICE = "服务投诉";
    public static final String FEEDBACK_PRAISE = "表扬建议";
    public static final String FEEDBACK_OTHER = "其他";
    
    // ========== 消息类型 ==========
    public static final String MESSAGE_TYPE_USER = "user";
    public static final String MESSAGE_TYPE_AI = "ai";
    
    // ========== 网络配置 (新增) ==========
    
    // 超时配置（秒）
    public static final int CONNECT_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 15;
    
    // 重试配置
    public static final int MAX_RETRY_COUNT = 3;
    public static final int RETRY_DELAY_MS = 1000;
    
    // 请求限制
    public static final int MAX_MESSAGE_LENGTH = 2000;
    public static final int MAX_TOKENS = 1000;
    public static final double TEMPERATURE = 0.7;
    
    // ========== 数据库配置 (新增) ==========
    
    // 数据库版本
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "xiangxia_chat.db";
    
    // 消息保留期限（毫秒）
    public static final long MESSAGE_RETENTION_PERIOD = 30L * 24 * 60 * 60 * 1000; // 30天
    
    // ========== UI配置 (新增) ==========
    
    // 聊天界面配置
    public static final int CHAT_BUBBLE_MAX_WIDTH_DP = 280;
    public static final int SCROLL_ANIMATION_DURATION = 300;
    
    // 加载状态配置
    public static final int TYPING_SIMULATION_DELAY = 100;
    public static final int MIN_LOADING_TIME = 500;
    
    // ========== 错误码定义 (新增) ==========
    
    public static final int ERROR_CODE_NETWORK = 1001;
    public static final int ERROR_CODE_API_KEY = 1002;
    public static final int ERROR_CODE_RATE_LIMIT = 1003;
    public static final int ERROR_CODE_SERVER_ERROR = 1004;
    public static final int ERROR_CODE_TIMEOUT = 1005;
    public static final int ERROR_CODE_PARSE_ERROR = 1006;
    public static final int ERROR_CODE_UNKNOWN = 1999;
    
    // ========== SharedPreferences Key ==========
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_FIRST_LAUNCH = "first_launch";
    public static final String PREF_CURRENT_SESSION = "current_session";
    
    // 新增聊天相关偏好设置键
    public static final String PREF_API_KEY = "api_key";
    public static final String PREF_MODEL_SELECTION = "model_selection";
    public static final String PREF_TEMPERATURE = "temperature";
    public static final String PREF_MAX_TOKENS = "max_tokens";
    public static final String PREF_CONVERSATION_ID = "conversation_id";
    public static final String PREF_LAST_CHAT_TIME = "last_chat_time";
    
    // ========== 系统提示词 (新增) ==========
    
    public static final String SYSTEM_PROMPT = 
        "你是湘湘管家AI助手，专门为湘湘管家App的用户提供帮助。你需要：\n" +
        "1. 友好、热情地回答用户问题\n" +
        "2. 提供实用的生活服务建议\n" +
        "3. 保持简洁明了的回答风格\n" +
        "4. 如果不确定答案，诚实地说明并建议其他解决方式\n" +
        "5. 回答内容要符合中国用户的使用习惯";
    
    // ========== 工具方法 (新增) ==========
    
    /**
     * 检查API密钥是否已配置
     */
    public static boolean isApiKeyConfigured() {
        return OPENAI_API_KEY != null && 
               !OPENAI_API_KEY.isEmpty() && 
               !OPENAI_API_KEY.equals("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");
    }
    
    /**
     * 获取用户代理字符串
     */
    public static String getUserAgent() {
        return "XiangJia-LocalLife/1.0 (Android)";
    }
    
    /**
     * 获取当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}