package com.example.yinggutest.mianpage.fragment.mainpageadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yinggutest.R;

import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.IndicatorHolder> {

    private List<String> mList;

    public TestListAdapter(List<String> li){
        this.mList = li;
    }

    @NonNull
    @Override
    public IndicatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_list_indicator_adapter_main_page, parent, false);
        return new IndicatorHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IndicatorHolder holder, int position) {
        String list = mList.get(position);
        holder.tvIndicator.setText(list.toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class IndicatorHolder extends RecyclerView.ViewHolder {

        private final TextView tvIndicator;

        public IndicatorHolder(@NonNull View itemView) {
            super(itemView);
            tvIndicator = itemView.findViewById(R.id.tv_test_list_indicator_item_indicator_adapter_maim_page);
        }
    }
}
