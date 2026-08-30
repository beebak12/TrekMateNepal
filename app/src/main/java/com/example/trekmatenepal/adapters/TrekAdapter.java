package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.TrekModel;

import java.util.List;

public class TrekAdapter extends RecyclerView.Adapter<TrekAdapter.ViewHolder> {

    private List<TrekModel> trekList;

    public TrekAdapter(List<TrekModel> trekList) {
        this.trekList = trekList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView trekImage;
        TextView trekName, trekLocation, trekDate, trekStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trekImage = itemView.findViewById(R.id.trekImage);
            trekName = itemView.findViewById(R.id.trekName);
            trekLocation = itemView.findViewById(R.id.trekLocation);
            trekDate = itemView.findViewById(R.id.trekDate);
            trekStatus = itemView.findViewById(R.id.trekStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trek, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        TrekModel trek = trekList.get(position);

        holder.trekImage.setImageResource(trek.getImage());
        holder.trekName.setText(trek.getName());
        holder.trekLocation.setText("📍 " + trek.getLocation());
        
        // Handle duration/date
        if (trek.getDuration() != null) {
            holder.trekDate.setText("⏳ " + trek.getDuration());
        }
        
        // Handle status if present (e.g. for completed treks)
        // Since item_trek has trekStatus, we can use rating or a default
        if (trek.getRating() != null) {
            holder.trekStatus.setText("⭐ " + trek.getRating());
        } else {
            holder.trekStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return trekList.size();
    }
}