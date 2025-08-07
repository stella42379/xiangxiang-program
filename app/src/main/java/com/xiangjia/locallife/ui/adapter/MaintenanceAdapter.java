package com.xiangjia.locallife.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangjia.locallife.R;
import com.xiangjia.locallife.ui.fragment.MainPageFragment;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.ViewHolder> {
    private Context context;
    private List<MainPageFragment.ServiceItem> serviceList;

    public MaintenanceAdapter(Context context, List<MainPageFragment.ServiceItem> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainPageFragment.ServiceItem service = serviceList.get(position);
        
        holder.serviceIcon.setImageResource(service.getIconRes());
        holder.serviceName.setText(service.getName());
        holder.serviceDescription.setText(service.getDescription());
        
        holder.itemView.setOnClickListener(v -> {
            if (service.getAction() != null) {
                service.getAction().run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList != null ? serviceList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceIcon;
        TextView serviceName;
        TextView serviceDescription;

        ViewHolder(View itemView) {
            super(itemView);
            serviceIcon = itemView.findViewById(R.id.iv_service_icon);
            serviceName = itemView.findViewById(R.id.tv_service_name);
            serviceDescription = itemView.findViewById(R.id.tv_service_description);
        }
    }
}