package com.xiangjia.locallife.network.model;

public class DifyRequest {
    private DifyInputs inputs;
    private String response_mode = "blocking";  // 与小程序保持一致
    private String user;
    private String conversation_id;
    
    public DifyRequest() {}
    
    public DifyRequest(String message, String userId) {
        this.inputs = new DifyInputs(message);
        this.user = "android_user_" + userId;
        this.response_mode = "blocking";
    }
    
    // Getter和Setter
    public DifyInputs getInputs() { return inputs; }
    public void setInputs(DifyInputs inputs) { this.inputs = inputs; }
    
    public String getResponse_mode() { return response_mode; }
    public void setResponse_mode(String response_mode) { this.response_mode = response_mode; }
    
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    
    public String getConversation_id() { return conversation_id; }
    public void setConversation_id(String conversation_id) { this.conversation_id = conversation_id; }
    
    public static class DifyInputs {
        private String query;
        
        public DifyInputs() {}
        
        public DifyInputs(String query) {
            this.query = query;
        }
        
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
}