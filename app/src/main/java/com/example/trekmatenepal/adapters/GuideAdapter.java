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
import com.example.trekmatenepal.activities.GuideProfileActivity;
import com.example.trekmatenepal.models.GuideModel;
import com.example.trekmatenepal.models.TrekModel;

import java.util.List;

/**
 * GuideAdapter — displays list of available guides for a trek.
 */
public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private List<GuideModel> guideList;
    private TrekModel selectedTrek;

    public GuideAdapter(List<GuideModel> guideList, TrekModel trek) {
        this.guideList = guideList;
        this.selectedTrek = trek;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuideModel guide = guideList.get(position);
        Context context = holder.itemView.getContext();

        holder.imgGuide.setImageResource(guide.getImage());
        holder.txtName.setText(guide.getName());
        holder.txtDesignation.setText(guide.getDesignation());
        holder.txtExperience.setText(guide.getExperience());
        holder.txtLanguages.setText("Speaks: " + guide.getLanguages());
        holder.txtRating.setText(guide.getRating());
        holder.txtReviews.setText("(" + guide.getReviews() + ")");
        holder.txtPrice.setText(guide.getDailyPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GuideProfileActivity.class);
            intent.putExtra("guide", guide);
            if (selectedTrek != null) {
                intent.putExtra("trek", selectedTrek);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgGuide;
        TextView txtName, txtDesignation, txtExperience, txtLanguages;
        TextView txtRating, txtReviews, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGuide = itemView.findViewById(R.id.imgGuide);
            txtName = itemView.findViewById(R.id.txtGuideName);
            txtDesignation = itemView.findViewById(R.id.txtDesignation);
            txtExperience = itemView.findViewById(R.id.txtExperience);
            txtLanguages = itemView.findViewById(R.id.txtLanguages);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtReviews = itemView.findViewById(R.id.txtReviews);
            txtPrice = itemView.findViewById(R.id.txtDailyPrice);
        }
    }
}
