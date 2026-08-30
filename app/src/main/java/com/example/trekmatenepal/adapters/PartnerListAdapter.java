package com.example.trekmatenepal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * PartnerListAdapter — drives the partner list RecyclerView in PartnerListActivity.
 * Each card has a "View Profile" button that opens PartnerProfileActivity.
 */
public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {

    public interface OnPartnerClickListener {
        void onViewProfile(PartnerModel partner);
    }

    private final Context context;
    private List<PartnerModel> partnerList;
    private final OnPartnerClickListener listener;

    public PartnerListAdapter(Context context, List<PartnerModel> partnerList,
                               OnPartnerClickListener listener) {
        this.context     = context;
        this.partnerList = new ArrayList<>(partnerList);
        this.listener    = listener;
    }

    public void updateList(List<PartnerModel> newList) {
        this.partnerList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_partner_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PartnerModel partner = partnerList.get(position);

        int imageRes = partner.getImage();
        if (isValidDrawable(imageRes)) {
            holder.imgPartner.setImageResource(imageRes);
        } else {
            holder.imgPartner.setImageResource(R.drawable.partner1);
        }

        holder.txtName.setText(partner.getName());
        holder.txtLocation.setText("📍 " + partner.getLocation());
        holder.txtDestination.setText(partner.getDestination());
        holder.txtDateDuration.setText(partner.getTrekDate() + " • " + partner.getDuration());

        // Online status dot
        if (partner.isOnline()) {
            holder.tvOnline.setVisibility(View.VISIBLE);
        } else {
            holder.tvOnline.setVisibility(View.GONE);
        }

        View.OnClickListener openProfile = v -> {
            if (listener != null) listener.onViewProfile(partner);
        };
        holder.btnViewProfile.setOnClickListener(openProfile);
        holder.itemView.setOnClickListener(openProfile);
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
        return partnerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPartner;
        TextView  txtName, txtLocation, txtDestination, txtDateDuration, tvOnline;
        Button    btnViewProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPartner       = itemView.findViewById(R.id.imgPartner);
            txtName          = itemView.findViewById(R.id.txtName);
            txtLocation      = itemView.findViewById(R.id.txtLocation);
            txtDestination   = itemView.findViewById(R.id.txtDestination);
            txtDateDuration  = itemView.findViewById(R.id.txtDateDuration);
            tvOnline         = itemView.findViewById(R.id.tvOnline);
            btnViewProfile   = itemView.findViewById(R.id.btnViewProfile);
        }
    }
}
