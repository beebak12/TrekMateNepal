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

import java.util.List;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.ViewHolder> {

    private List<PartnerModel> partnerList;

    public PartnerAdapter(List<PartnerModel> partnerList) {
        this.partnerList = partnerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PartnerModel partner = partnerList.get(position);

        holder.image.setImageResource(partner.getImage());
        holder.name.setText(partner.getName());
        holder.rating.setText(partner.getRating());
        holder.reviews.setText(partner.getReviews());
        holder.status.setText(partner.getStatus());

    }

    @Override
    public int getItemCount() {
        return partnerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, rating, reviews, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imgPartner);
            name = itemView.findViewById(R.id.txtPartnerName);
            rating = itemView.findViewById(R.id.txtPartnerRating);
            reviews = itemView.findViewById(R.id.txtPartnerReviews);
            status = itemView.findViewById(R.id.txtStatus);
        }
    }
}