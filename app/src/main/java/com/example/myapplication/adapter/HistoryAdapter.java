package com.example.myapplication.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.HealthHistoryBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    List<HealthHistoryBean> healthHistoryBeans;

    public void setHealthHistoryBeans(List<HealthHistoryBean> healthHistoryBeans) {
        this.healthHistoryBeans = healthHistoryBeans;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_history, null);
        return new HistoryHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryHolder holder, int position) {
        HealthHistoryBean healthHistoryBean = healthHistoryBeans.get(position);
        holder.tvName.setText(healthHistoryBean.getName());
        holder.tvAddress.setText(healthHistoryBean.getLocation());
        holder.tvTem.setText(healthHistoryBean.getTem());
        holder.tvTime.setText(healthHistoryBean.getTime());
        try {
            float tem = Float.parseFloat(healthHistoryBean.getTem());

            if (tem > 37.5) {
                holder.tvTem.setTextColor(Color.RED);
            } else {
                holder.tvTem.setTextColor(Color.GREEN);

            }

        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return healthHistoryBeans == null ? 0 : healthHistoryBeans.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTem, tvAddress, tvTime;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);

        }
    }
}
