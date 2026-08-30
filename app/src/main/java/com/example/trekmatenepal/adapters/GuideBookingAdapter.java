package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.GuideBookingModel;

import java.util.List;

public class GuideBookingAdapter extends RecyclerView.Adapter<GuideBookingAdapter.ViewHolder> {

    private List<GuideBookingModel> bookingList;
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onAccept(GuideBookingModel booking);
        void onReject(GuideBookingModel booking);
        void onViewDetails(GuideBookingModel booking);
    }

    public GuideBookingAdapter(List<GuideBookingModel> bookingList, OnBookingClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_booking_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuideBookingModel booking = bookingList.get(position);

        holder.txtTrekkerName.setText(booking.getTrekkerName());
        holder.txtDestination.setText(booking.getDestination());
        holder.txtDates.setText(booking.getDates());
        holder.txtGroupSize.setText(booking.getGroupSize());
        holder.txtPrice.setText(booking.getPrice());
        holder.txtStatus.setText(booking.getStatus());
        holder.imgTrekker.setImageResource(booking.getTrekkerImage());

        if (booking.getStatus().equalsIgnoreCase("Pending")) {
            holder.layoutActionButtons.setVisibility(View.VISIBLE);
            holder.txtStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFF5F3FF));
            holder.txtStatus.setTextColor(0xFF5E35B1);
        } else if (booking.getStatus().equalsIgnoreCase("Confirmed")) {
            holder.layoutActionButtons.setVisibility(View.GONE);
            holder.txtStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE8F5E9));
            holder.txtStatus.setTextColor(0xFF2E7D32);
        } else {
            holder.layoutActionButtons.setVisibility(View.GONE);
            holder.txtStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFEBEE));
            holder.txtStatus.setTextColor(0xFFD32F2F);
        }

        holder.btnAccept.setOnClickListener(v -> listener.onAccept(booking));
        holder.btnReject.setOnClickListener(v -> listener.onReject(booking));
        holder.itemView.setOnClickListener(v -> listener.onViewDetails(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTrekker;
        TextView txtTrekkerName, txtDestination, txtDates, txtGroupSize, txtPrice, txtStatus;
        Button btnAccept, btnReject;
        View layoutActionButtons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrekker = itemView.findViewById(R.id.imgTrekker);
            txtTrekkerName = itemView.findViewById(R.id.txtTrekkerName);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            txtDates = itemView.findViewById(R.id.txtDates);
            txtGroupSize = itemView.findViewById(R.id.txtGroupSize);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            layoutActionButtons = itemView.findViewById(R.id.layoutActionButtons);
        }
    }
}