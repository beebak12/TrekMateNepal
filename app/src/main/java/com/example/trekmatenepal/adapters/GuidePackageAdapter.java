package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.GuidePackageModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class GuidePackageAdapter extends RecyclerView.Adapter<GuidePackageAdapter.ViewHolder> {

    private List<GuidePackageModel> packageList;
    private OnPackageClickListener listener;

    public interface OnPackageClickListener {
        void onEdit(GuidePackageModel pkg);
        void onView(GuidePackageModel pkg);
        void onDelete(GuidePackageModel pkg);
    }

    public GuidePackageAdapter(List<GuidePackageModel> packageList, OnPackageClickListener listener) {
        this.packageList = packageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuidePackageModel pkg = packageList.get(position);

        holder.txtPackageName.setText(pkg.getName());
        holder.txtDuration.setText(pkg.getDuration());
        holder.txtPrice.setText(pkg.getPrice());
        holder.txtStatus.setText(pkg.getStatus());
        holder.imgPackage.setImageResource(pkg.getImage());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(pkg));
        holder.btnView.setOnClickListener(v -> listener.onView(pkg));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(pkg));
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPackage;
        TextView txtPackageName, txtDuration, txtPrice, txtStatus;
        MaterialButton btnEdit, btnView;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPackage = itemView.findViewById(R.id.imgPackage);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnView = itemView.findViewById(R.id.btnView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}