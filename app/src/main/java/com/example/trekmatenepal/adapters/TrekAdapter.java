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
import com.example.trekmatenepal.activities.TrekBookingActivity;
import com.example.trekmatenepal.models.TrekModel;

import java.util.List;

public class TrekAdapter extends RecyclerView.Adapter<TrekAdapter.ViewHolder> {

    private List<TrekModel> trekList;

    public TrekAdapter(List<TrekModel> trekList) {
        this.trekList = trekList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trek, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TrekModel trek = trekList.get(position);
        Context context = holder.itemView.getContext();

        holder.image.setImageResource(trek.getImage());
        holder.name.setText(trek.getTrekName());
        holder.duration.setText(trek.getDuration());
        holder.rating.setText(trek.getRating());
        holder.reviews.setText("(" + trek.getReviews() + ")");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TrekBookingActivity.class);
            intent.putExtra("name", trek.getTrekName());
            intent.putExtra("location", trek.getLocation());
            intent.putExtra("duration", trek.getDuration());
            intent.putExtra("rating", trek.getRating());
            intent.putExtra("reviews", trek.getReviews());
            intent.putExtra("image", trek.getImage());
            intent.putExtra("difficulty", trek.getDifficulty());
            intent.putExtra("altitude", trek.getAltitude());
            intent.putExtra("distance", trek.getDistance());
            intent.putExtra("description", trek.getDescription());
            intent.putExtra("fee", trek.getFee());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return trekList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, duration, rating, reviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgTrek);
            name = itemView.findViewById(R.id.txtTrekName);
            duration = itemView.findViewById(R.id.txtDuration);
            rating = itemView.findViewById(R.id.txtRating);
            reviews = itemView.findViewById(R.id.txtReviews);

        }
    }
}