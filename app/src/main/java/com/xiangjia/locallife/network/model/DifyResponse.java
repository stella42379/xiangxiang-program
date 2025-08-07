package com.xiangjia.locallife.network.model;

public class DifyResponse {
    private String workflow_run_id;
    private String task_id;
    private DifyData data;
    
    public String getWorkflow_run_id() { return workflow_run_id; }
    public void setWorkflow_run_id(String workflow_run_id) { this.workflow_run_id = workflow_run_id; }
    
    public String getTask_id() { return task_id; }
    public void setTask_id(String task_id) { this.task_id = task_id; }
    
    public DifyData getData() { return data; }
    public void setData(DifyData data) { this.data = data; }
    
    public static class DifyData {
        private DifyOutputs outputs;
        private String status;
        
        public DifyOutputs getOutputs() { return outputs; }
        public void setOutputs(DifyOutputs outputs) { this.outputs = outputs; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class DifyOutputs {
        private String text;
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}