package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.CartItemModel;

import java.util.List;
import java.util.Locale;

/**
 * CartItemAdapter — the gear lines shown on the booking summary screen while
 * the user is still adding items. The X button removes a line.
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    public interface OnRemoveListener {
        void onRemove(int position);
    }

    private final List<CartItemModel> items;
    private final OnRemoveListener removeListener;

    public CartItemAdapter(List<CartItemModel> items, OnRemoveListener removeListener) {
        this.items = items;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemModel item = items.get(position);

        int imageRes = item.getImage();
        if (isValidDrawable(holder.itemView, imageRes)) {
            holder.imgGear.setImageResource(imageRes);
        } else {
            holder.imgGear.setImageResource(R.drawable.jacket);
        }

        holder.txtGearName.setText(item.getGearName());
        holder.txtDates.setText(item.getDates());

        int weeks = item.getWeeks();
        holder.txtQtyWeeks.setText("Qty " + item.getQuantity() + " · "
                + weeks + " week" + (weeks == 1 ? "" : "s"));
        holder.txtSubtotal.setText("Rs. " + String.format(Locale.getDefault(), "%,d", item.getSubtotal()));

        // Only allow removing when more than one item is in the booking
        boolean canRemove = items.size() > 1;
        holder.btnRemoveItem.setVisibility(canRemove ? View.VISIBLE : View.GONE);
        holder.btnRemoveItem.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && removeListener != null) {
                removeListener.onRemove(pos);
            }
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
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGear, btnRemoveItem;
        TextView txtGearName, txtDates, txtQtyWeeks, txtSubtotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGear       = itemView.findViewById(R.id.imgGear);
            btnRemoveItem = itemView.findViewById(R.id.btnRemoveItem);
            txtGearName   = itemView.findViewById(R.id.txtGearName);
            txtDates      = itemView.findViewById(R.id.txtDates);
            txtQtyWeeks   = itemView.findViewById(R.id.txtQtyWeeks);
            txtSubtotal   = itemView.findViewById(R.id.txtSubtotal);
        }
    }
}
