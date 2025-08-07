package com.xiangjia.locallife.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.xiangjia.locallife.MainActivity;
import com.xiangjia.locallife.R;

public class MainPageFragment extends Fragment implements MainActivity.RefreshableFragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 创建一个简单的布局，暂时不依赖 layout 文件
        TextView textView = new TextView(getContext());
        textView.setText("主页Fragment - 待完善");
        textView.setTextSize(18);
        textView.setPadding(50, 50, 50, 50);
        return textView;
    }
    
    @Override
    public void onRefresh() {
        // 刷新方法实现
    }
    
    // ServiceItem 类
    public static class ServiceItem {
        private String name;
        private int iconRes;
        private String description;
        private Runnable action;

        public ServiceItem(String name, int iconRes, String description, Runnable action) {
            this.name = name;
            this.iconRes = iconRes;
            this.description = description;
            this.action = action;
        }

        public String getName() { return name; }
        public int getIconRes() { return iconRes; }
        public String getDescription() { return description; }
        public Runnable getAction() { return action; }
    }
}