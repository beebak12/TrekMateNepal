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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PartnerModel partner = partnerList.get(position);
        holder.tvPartnerName.setText(partner.getName());
        holder.tvRating.setText(partner.getRating());

        int imageRes = partner.getImage();
        if (isValidDrawable(holder.itemView, imageRes)) {
            holder.ivPartnerImage.setImageResource(imageRes);
        } else {
            holder.ivPartnerImage.setImageResource(R.drawable.ic_person);
        }
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
        return partnerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPartnerImage;
        TextView tvPartnerName, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPartnerImage = itemView.findViewById(R.id.ivPartnerImage);
            tvPartnerName = itemView.findViewById(R.id.tvPartnerName);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
