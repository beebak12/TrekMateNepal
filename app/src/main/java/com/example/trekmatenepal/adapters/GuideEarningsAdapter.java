package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.GuideEarningsModel;

import java.util.List;

public class GuideEarningsAdapter extends RecyclerView.Adapter<GuideEarningsAdapter.ViewHolder> {

    private List<GuideEarningsModel> earningsList;

    public GuideEarningsAdapter(List<GuideEarningsModel> earningsList) {
        this.earningsList = earningsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_earnings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuideEarningsModel item = earningsList.get(position);
        holder.txtTrekName.setText(item.getTrekName());
        holder.txtDates.setText(item.getDates());
        holder.txtAmount.setText(item.getAmount());
        holder.txtStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return earningsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTrekName, txtDates, txtAmount, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTrekName = itemView.findViewById(R.id.txtTrekName);
            txtDates = itemView.findViewById(R.id.txtDates);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}