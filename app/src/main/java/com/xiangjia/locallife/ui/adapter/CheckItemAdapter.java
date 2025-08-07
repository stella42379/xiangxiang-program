package com.xiangjia.locallife.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查项目适配器
 * 用于显示检查项目列表
 */
public class CheckItemAdapter extends RecyclerView.Adapter<CheckItemAdapter.ViewHolder> {
    
    private List<CheckItem> checkItemList;
    private OnItemClickListener onItemClickListener;
    
    public CheckItemAdapter() {
        this.checkItemList = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_check, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckItem checkItem = checkItemList.get(position);
        holder.bind(checkItem);
    }
    
    @Override
    public int getItemCount() {
        return checkItemList.size();
    }
    
    /**
     * 设置数据
     */
    public void setData(List<CheckItem> checkItemList) {
        this.checkItemList.clear();
        this.checkItemList.addAll(checkItemList);
        notifyDataSetChanged();
    }
    
    /**
     * 添加数据
     */
    public void addData(CheckItem checkItem) {
        this.checkItemList.add(checkItem);
        notifyItemInserted(this.checkItemList.size() - 1);
    }
    
    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    /**
     * 获取已检查的项目数量
     */
    public int getCheckedCount() {
        int count = 0;
        for (CheckItem item : checkItemList) {
            if (item.isChecked()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 点击监听器接口
     */
    public interface OnItemClickListener {
        void onItemClick(CheckItem checkItem);
        void onCheckChanged(CheckItem checkItem, boolean isChecked);
    }
    
    /**
     * 检查项目数据类
     */
    public static class CheckItem {
        private String name;
        private String description;
        private boolean isChecked;
        private String category;
        private int priority;
        
        public CheckItem(String name, String description, String category, int priority) {
            this.name = name;
            this.description = description;
            this.category = category;
            this.priority = priority;
            this.isChecked = false;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public boolean isChecked() { return isChecked; }
        public void setChecked(boolean checked) { isChecked = checked; }
        public String getCategory() { return category; }
        public int getPriority() { return priority; }
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        
        private CheckBox checkBox;
        private TextView itemName;
        private TextView itemDescription;
        private TextView itemCategory;
        private TextView itemPriority;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            checkBox = itemView.findViewById(R.id.check_box);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemPriority = itemView.findViewById(R.id.item_priority);
            
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(checkItemList.get(position));
                    }
                }
            });
            
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CheckItem item = checkItemList.get(position);
                        item.setChecked(isChecked);
                        onItemClickListener.onCheckChanged(item, isChecked);
                    }
                }
            });
        }
        
        public void bind(CheckItem checkItem) {
            itemName.setText(checkItem.getName());
            itemDescription.setText(checkItem.getDescription());
            itemCategory.setText(checkItem.getCategory());
            itemPriority.setText("优先级: " + checkItem.getPriority());
            checkBox.setChecked(checkItem.isChecked());
            
            // 根据优先级设置颜色
            setPriorityColor(checkItem.getPriority());
        }
        
        /**
         * 根据优先级设置颜色
         */
        private void setPriorityColor(int priority) {
            // TODO: 根据优先级设置不同的颜色
        }
    }
}
