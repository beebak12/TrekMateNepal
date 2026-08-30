package com.example.trekmatenepal.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.activities.GearDetailsActivity;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.Gear;

import java.util.List;

public class GearAdapter extends RecyclerView.Adapter<GearAdapter.ViewHolder> {

    private List<Gear> gearList;

    public GearAdapter(List<Gear> gearList) {
        this.gearList = gearList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView gearImage;
        TextView gearName, gearPrice, gearLocation;
        TextView gearStatus, bookedFrom, bookedTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gearImage = itemView.findViewById(R.id.gearImage);
            gearName = itemView.findViewById(R.id.gearName);
            gearPrice = itemView.findViewById(R.id.gearPrice);
            gearLocation = itemView.findViewById(R.id.gearLocation);
            gearStatus = itemView.findViewById(R.id.gearStatus);
            bookedFrom = itemView.findViewById(R.id.bookedFrom);
            bookedTo = itemView.findViewById(R.id.bookedTo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gear, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Gear gear = gearList.get(position);

        holder.gearImage.setImageResource(gear.getImage());
        holder.gearName.setText(gear.getName());
        holder.gearPrice.setText(gear.getPrice());
        holder.gearLocation.setText("📍 " + gear.getLocation());
        holder.gearStatus.setText(gear.getStatus());

        if (gear.getStatus().equals("Available")) {

            holder.bookedFrom.setVisibility(View.GONE);
            holder.bookedTo.setVisibility(View.GONE);

        } else {

            holder.bookedFrom.setVisibility(View.VISIBLE);
            holder.bookedTo.setVisibility(View.VISIBLE);

            holder.bookedFrom.setText(
                    "Booked From\n" + gear.getBookedFrom()
            );

            holder.bookedTo.setText(
                    "Booked To\n" + gear.getBookedTo()
            );
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    GearDetailsActivity.class
            );

            intent.putExtra("name", gear.getName());
            intent.putExtra("price", gear.getPrice());
            intent.putExtra("status", gear.getStatus());
            intent.putExtra("location", gear.getLocation());
            intent.putExtra("image", gear.getImage());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gearList.size();
    }
}