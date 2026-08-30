package com.example.trekmatenepal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.activities.GearDetailActivity;
import com.example.trekmatenepal.models.GearModel;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.List;

/**
 * GearAdapter — drives the "Featured Gear" horizontal RecyclerView on the Dashboard.
 * Clicking a card opens GearDetailActivity.
 */
public class GearAdapter extends RecyclerView.Adapter<GearAdapter.ViewHolder> {

    private final List<GearModel> gearList;
    private Context context;

    public GearAdapter(List<GearModel> gearList) {
        this.gearList = gearList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_featured_gear, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GearModel gear = gearList.get(position);

        int imageRes = gear.getImage();
        if (isValidDrawable(imageRes)) {
            holder.image.setImageResource(imageRes);
        } else {
            holder.image.setImageResource(R.drawable.jacket);
        }
        
        holder.name.setText(gear.getName());
        holder.price.setText(gear.getPrice());
        holder.duration.setText(gear.getDuration());

        // Open GearDetailActivity with a RentalGearModel built from this GearModel
        holder.itemView.setOnClickListener(v -> {
            RentalGearModel rentalGear = new RentalGearModel(
                    gear.getImage(),
                    gear.getName(),
                    "Featured",
                    "4.8",
                    gear.getPrice() + " " + gear.getDuration(),
                    "Available"
            );
            Intent intent = new Intent(context, GearDetailActivity.class);
            intent.putExtra("gear", rentalGear);
            context.startActivity(intent);
        });
    }

    private boolean isValidDrawable(int resourceId) {
        if (resourceId <= 0) return false;
        try {
            String type = context.getResources().getResourceTypeName(resourceId);
            return "drawable".equals(type) || "mipmap".equals(type);
        } catch (Exception e) {
            return false;
        }
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
            image    = itemView.findViewById(R.id.imgGear);
            name     = itemView.findViewById(R.id.txtGearName);
            price    = itemView.findViewById(R.id.txtGearPrice);
            duration = itemView.findViewById(R.id.txtGearDuration);
        }
    }
}
