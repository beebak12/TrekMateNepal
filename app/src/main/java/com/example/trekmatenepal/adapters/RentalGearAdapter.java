package com.example.trekmatenepal.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.activities.GearDetailActivity;
import com.example.trekmatenepal.data.GearFavouriteRepository;
import com.example.trekmatenepal.models.RentalGearModel;
import com.example.trekmatenepal.data.RemoteImageLoader;

import java.util.ArrayList;

/**
 * RentalGearAdapter — drives the gear grid in GearRentalActivity.
 * Clicking a card opens GearDetailActivity with the full gear object.
 */
public class RentalGearAdapter extends RecyclerView.Adapter<RentalGearAdapter.ViewHolder> {

    private static final String TAG = "RentalGearAdapter";

    private final Context context;
    private final ArrayList<RentalGearModel> gearList;
    private final OnFavouriteChangedListener favouriteChangedListener;

    public interface OnFavouriteChangedListener {
        void onFavouriteChanged(RentalGearModel gear);
    }

    public RentalGearAdapter(Context context, ArrayList<RentalGearModel> gearList) {
        this(context, gearList, null);
    }

    public RentalGearAdapter(Context context, ArrayList<RentalGearModel> gearList,
                             OnFavouriteChangedListener listener) {
        this.context = context;
        this.gearList = gearList;
        this.favouriteChangedListener = listener;
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
        bindGearImage(holder.imgGear, gear);

        holder.txtGearName.setText(gear.getName());
        holder.txtGearPrice.setText(gear.getPrice());
        holder.txtGearRating.setText(gear.getRating());
        updateFavouriteIcon(holder.btnFav, GearFavouriteRepository.isFavourite(context, gear));
        holder.btnFav.setOnClickListener(v -> {
            boolean selected = GearFavouriteRepository.toggle(context, gear);
            updateFavouriteIcon(holder.btnFav, selected);
            Toast.makeText(context, selected ? "Added to favourites" : "Removed from favourites",
                    Toast.LENGTH_SHORT).show();
            if (favouriteChangedListener != null) favouriteChangedListener.onFavouriteChanged(gear);
        });

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

    private void updateFavouriteIcon(ImageView view, boolean selected) {
        view.setImageResource(R.drawable.ic_favorite);
        view.setAlpha(selected ? 1f : 0.35f);
        view.setContentDescription(selected ? "Remove from favourites" : "Add to favourites");
    }

    private void bindGearImage(ImageView imageView, RentalGearModel gear) {
        String customImageUri = gear.getCustomImageUri();
        if (customImageUri != null && !customImageUri.trim().isEmpty()) {
            try {
                imageView.setImageURI(Uri.parse(customImageUri));
                if (imageView.getDrawable() != null) {
                    return;
                }
            } catch (RuntimeException error) {
                // GetContent/Photo Picker grants may expire after the app process is restarted.
                Log.w(TAG, "Stored gear image is no longer readable; using fallback", error);
            }
            gear.setCustomImageUri(null);
        }

        if (!gear.getRemoteImageUrl().isEmpty()) {
            RemoteImageLoader.load(imageView, gear.getRemoteImageUrl(), R.drawable.jacket);
            return;
        }

        int imageRes = gear.getImage();
        imageView.setImageResource(isValidDrawable(imageRes) ? imageRes : R.drawable.jacket);
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
