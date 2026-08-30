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
import com.example.trekmatenepal.activities.GearDetailsActivity;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

public class RentalGearAdapter extends RecyclerView.Adapter<RentalGearAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RentalGearModel> gearList;

    public RentalGearAdapter(Context context, ArrayList<RentalGearModel> gearList) {
        this.context = context;
        this.gearList = gearList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_rental_gear, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentalGearModel gear = gearList.get(position);

        holder.imgGear.setImageResource(gear.getImage());
        holder.txtGearName.setText(gear.getName());
        holder.txtGearPrice.setText(gear.getPrice());
        holder.txtGearRating.setText(gear.getRating() + " (120)");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GearDetailsActivity.class);
            intent.putExtra("name", gear.getName());
            intent.putExtra("price", gear.getPrice());
            intent.putExtra("image", gear.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gearList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGear, btnFav;
        TextView txtGearName, txtGearPrice, txtGearRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGear = itemView.findViewById(R.id.imgGear);
            btnFav = itemView.findViewById(R.id.btnFav);
            txtGearName = itemView.findViewById(R.id.txtGearName);
            txtGearPrice = itemView.findViewById(R.id.txtGearPrice);
            txtGearRating = itemView.findViewById(R.id.txtGearRating);
        }
    }
}