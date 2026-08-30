package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class PartnerFinderAdapter extends RecyclerView.Adapter<PartnerFinderAdapter.PartnerViewHolder> {

    private List<PartnerModel> partners;
    private OnPartnerClickListener listener;

    public interface OnPartnerClickListener {
        void onPartnerClick(PartnerModel partner);
    }

    public PartnerFinderAdapter(List<PartnerModel> partners, OnPartnerClickListener listener) {
        this.partners = partners;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partner, parent, false);
        return new PartnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerViewHolder holder, int position) {
        PartnerModel partner = partners.get(position);
        holder.bind(partner, listener);
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }

    static class PartnerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPartnerName, tvLocation, tvRating, tvExperience;
        private ImageView ivPartnerImage, ivVerified, ivFavourite;
        private MaterialButton btnViewProfile;

        PartnerViewHolder(View itemView) {
            super(itemView);
            tvPartnerName = itemView.findViewById(R.id.tvPartnerName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            ivPartnerImage = itemView.findViewById(R.id.ivPartnerImage);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            btnViewProfile = itemView.findViewById(R.id.btnViewProfile);
        }

        void bind(PartnerModel partner, OnPartnerClickListener listener) {
            // Set basic info
            tvPartnerName.setText(partner.getName());
            tvLocation.setText(partner.getBaseLocation());
            tvRating.setText(String.format("%s %s", 
                    partner.getRating(), partner.getReviews()));
            tvExperience.setText(partner.getYearsOfExperience() + " Years Experience");

            // Show verified badge if applicable
            if (partner.isVerified()) {
                ivVerified.setVisibility(View.VISIBLE);
            } else {
                ivVerified.setVisibility(View.GONE);
            }

            // Set profile image
            if (partner.getImage() != 0) {
                ivPartnerImage.setImageResource(partner.getImage());
            } else {
                ivPartnerImage.setImageResource(R.drawable.partner1);
            }

            // Setup click listeners
            btnViewProfile.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPartnerClick(partner);
                }
            });

            ivFavourite.setOnClickListener(v -> {
                // TODO: Handle favorite/wishlist functionality
                // For now, toggle color
                int tint = itemView.getContext().getColor(R.color.purple_primary);
                ivFavourite.setColorFilter(tint);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPartnerClick(partner);
                }
            });
        }
    }

    public void updateList(List<PartnerModel> newList) {
        partners.clear();
        partners.addAll(newList);
        notifyDataSetChanged();
    }
}
