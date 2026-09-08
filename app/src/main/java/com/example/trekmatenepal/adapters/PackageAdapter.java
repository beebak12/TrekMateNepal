package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.Package;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private ArrayList<Package> packageList;

    public PackageAdapter(ArrayList<Package> packageList) {
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false);

        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PackageViewHolder holder, int position) {

        Package packageItem = packageList.get(position);

        holder.imgPackage.setImageResource(packageItem.getImage());

        holder.txtPackageName.setText(packageItem.getName());

        holder.txtLocation.setText(
                "📍 " + packageItem.getLocation()
        );

        holder.txtDuration.setText(
                "📅 " + packageItem.getDuration()
        );

        holder.txtDescription.setText(
                packageItem.getDescription()
        );

        holder.txtStatus.setText(
                packageItem.getStatus()
        );

        if (packageItem.getStatus().equalsIgnoreCase("ACTIVE")) {

            holder.txtStatus.setBackgroundResource(
                    R.drawable.bg_active
            );

            holder.txtStatus.setTextColor(
                    android.graphics.Color.rgb(46, 125, 50)
            );

        } else {

            holder.txtStatus.setBackgroundResource(
                    R.drawable.bg_inactive
            );

            holder.txtStatus.setTextColor(
                    android.graphics.Color.rgb(198, 40, 40)
            );
        }
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgPackage;

        TextView txtPackageName;
        TextView txtLocation;
        TextView txtDuration;
        TextView txtDescription;
        TextView txtStatus;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPackage = itemView.findViewById(
                    R.id.imgPackage
            );

            txtPackageName = itemView.findViewById(
                    R.id.txtPackageName
            );

            txtLocation = itemView.findViewById(
                    R.id.txtLocation
            );

            txtDuration = itemView.findViewById(
                    R.id.txtDuration
            );

            txtDescription = itemView.findViewById(
                    R.id.txtDescription
            );

            txtStatus = itemView.findViewById(
                    R.id.txtStatus
            );
        }
    }
}