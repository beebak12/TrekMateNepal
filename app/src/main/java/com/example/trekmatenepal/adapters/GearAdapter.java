package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.GearModel;

import java.util.List;

public class GearAdapter extends RecyclerView.Adapter<GearAdapter.ViewHolder> {

    private List<GearModel> gearList;

    public GearAdapter(List<GearModel> gearList) {
        this.gearList = gearList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_gear, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GearModel gear = gearList.get(position);

        holder.image.setImageResource(gear.getImage());
        holder.name.setText(gear.getName());
        holder.price.setText(gear.getPrice());
        holder.duration.setText(gear.getDuration());

    }

    @Override
    public int getItemCount() {
        return gearList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgGear);
            name = itemView.findViewById(R.id.txtGearName);
            price = itemView.findViewById(R.id.txtGearPrice);
            duration = itemView.findViewById(R.id.txtGearDuration);
        }
    }
}