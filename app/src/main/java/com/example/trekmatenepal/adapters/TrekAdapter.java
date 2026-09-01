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
import com.example.trekmatenepal.activities.TrekPackageDetailsActivity;
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

        int imageRes = trek.getImage();
        if (isValidDrawable(holder.itemView, imageRes)) {
            holder.image.setImageResource(imageRes);
        } else {
            holder.image.setImageResource(R.drawable.everest);
        }

        holder.name.setText(trek.getTrekName());
        holder.location.setText(trek.getLocation());
        holder.duration.setText(trek.getDuration());
        holder.status.setText(trek.getDifficulty());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TrekPackageDetailsActivity.class);
            intent.putExtra("trek", trek);
            context.startActivity(intent);
        });

    }

    private boolean isValidDrawable(View v, int resourceId) {
        if (resourceId <= 0) return false;
        try {
            String type = v.getContext().getResources().getResourceTypeName(resourceId);
            return "drawable".equals(type) || "mipmap".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return trekList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, location, duration, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.trekImage);
            name = itemView.findViewById(R.id.trekName);
            location = itemView.findViewById(R.id.trekLocation);
            duration = itemView.findViewById(R.id.trekDate);
            status = itemView.findViewById(R.id.trekStatus);

        }
    }
}
