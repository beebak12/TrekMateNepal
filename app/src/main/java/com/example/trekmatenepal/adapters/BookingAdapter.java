package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<BookingModel> bookingList;

    public BookingAdapter(List<BookingModel> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        holder.imgGear.setImageResource(booking.getImage());
        holder.txtGearName.setText(booking.getGearName());
        holder.txtStatus.setText(booking.getStatus());
        holder.txtBookingDates.setText(booking.getDates());
        holder.txtAmount.setText(booking.getAmount());
        holder.txtBookingId.setText("Booking ID: " + booking.getBookingId());

        // Set status color
        if (booking.getStatus().equalsIgnoreCase("Confirmed")) {
            holder.txtStatus.setTextColor(0xFF4CAF50); // Green
        } else if (booking.getStatus().equalsIgnoreCase("Completed")) {
            holder.txtStatus.setTextColor(0xFF2196F3); // Blue
        } else {
            holder.txtStatus.setTextColor(0xFFF44336); // Red
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGear;
        TextView txtGearName, txtStatus, txtBookingDates, txtAmount, txtBookingId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGear = itemView.findViewById(R.id.imgGear);
            txtGearName = itemView.findViewById(R.id.txtGearName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtBookingDates = itemView.findViewById(R.id.txtBookingDates);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtBookingId = itemView.findViewById(R.id.txtBookingId);
        }
    }
}