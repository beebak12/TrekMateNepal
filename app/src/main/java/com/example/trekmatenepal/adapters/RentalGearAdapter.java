package com.example.trekmatenepal.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.activities.GearDetailActivity;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

/**
 * RentalGearAdapter — drives the gear grid in GearRentalActivity.
 * Clicking a card opens GearDetailActivity with the full gear object.
 */
public class RentalGearAdapter extends RecyclerView.Adapter<RentalGearAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<RentalGearModel> gearList;

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

        // Image loading logic
        if (gear.getCustomImageUri() != null) {
            holder.imgGear.setImageURI(Uri.parse(gear.getCustomImageUri()));
        } else {
            int imageRes = gear.getImage();
            if (isValidDrawable(imageRes)) {
                holder.imgGear.setImageResource(imageRes);
            } else {
                holder.imgGear.setImageResource(R.drawable.jacket);
            }
        }

        holder.txtGearName.setText(gear.getName());
        holder.txtGearPrice.setText(gear.getPrice());
        holder.txtGearRating.setText(gear.getRating());

        // Availability badge
        if ("Available".equalsIgnoreCase(gear.getAvailability())) {
            holder.txtAvailability.setTextColor(ContextCompat.getColor(context, R.color.success_green));
            holder.txtAvailability.setText("Available");
        } else {
            holder.txtAvailability.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.txtAvailability.setText("Unavailable");
        }

        // Open GearDetailActivity — pass entire model as Serializable
        View.OnClickListener openDetail = v -> openGearDetail(gear);
        holder.itemView.setOnClickListener(openDetail);
        holder.btnRentNow.setOnClickListener(openDetail);
    }

    private void openGearDetail(RentalGearModel gear) {
        Intent intent = new Intent(context, GearDetailActivity.class);
        intent.putExtra("gear", gear);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return gearList.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGear, btnFav;
        TextView txtGearName, txtGearPrice, txtGearRating, txtAvailability, btnRentNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGear        = itemView.findViewById(R.id.imgGear);
            btnFav         = itemView.findViewById(R.id.btnFav);
            txtGearName    = itemView.findViewById(R.id.txtGearName);
            txtGearPrice   = itemView.findViewById(R.id.txtGearPrice);
            txtGearRating  = itemView.findViewById(R.id.txtGearRating);
            txtAvailability= itemView.findViewById(R.id.txtAvailability);
            btnRentNow     = itemView.findViewById(R.id.btnRentNow);
        }
    }
}
