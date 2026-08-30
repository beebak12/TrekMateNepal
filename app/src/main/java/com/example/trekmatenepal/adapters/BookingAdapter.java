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
import com.example.trekmatenepal.models.BookingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * BookingAdapter — drives the RecyclerView in MyBookingsActivity.
 * Supports click listener for "View Details" button.
 */
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    public interface OnBookingClickListener {
        void onViewDetails(BookingModel booking);
    }

    private List<BookingModel> bookingList;
    private final OnBookingClickListener listener;

    public BookingAdapter(List<BookingModel> bookingList, OnBookingClickListener listener) {
        this.bookingList = new ArrayList<>(bookingList);
        this.listener    = listener;
    }

    /** Constructor without click listener (legacy compatibility). */
    public BookingAdapter(List<BookingModel> bookingList) {
        this.bookingList = new ArrayList<>(bookingList);
        this.listener    = null;
    }

    /** Swap the dataset and refresh. */
    public void updateList(List<BookingModel> newList) {
        this.bookingList = new ArrayList<>(newList);
        notifyDataSetChanged();
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

        holder.imgGear.setImageResource(
                booking.getImage() != 0 ? booking.getImage() : R.drawable.jacket);
        holder.txtGearName.setText(booking.getGearName());
        holder.txtStatus.setText(booking.getStatus());
        holder.txtBookingDates.setText(booking.getDates());
        holder.txtAmount.setText(booking.getAmount());
        holder.txtBookingId.setText("ID: " + booking.getBookingId());

        // Status colour
        int color;
        switch (booking.getStatus().toLowerCase()) {
            case "confirmed":
            case "upcoming":
                color = 0xFF2E7D32; break;   // success green
            case "completed":
                color = 0xFF1565C0; break;   // blue
            case "cancelled":
                color = 0xFFD32F2F; break;   // red
            default:
                color = 0xFF777777;
        }
        holder.txtStatus.setTextColor(color);

        // View Details button
        if (holder.btnViewDetails != null) {
            holder.btnViewDetails.setOnClickListener(v -> {
                if (listener != null) listener.onViewDetails(booking);
            });
        }

        // Whole card also clickable
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewDetails(booking);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGear;
        TextView txtGearName, txtStatus, txtBookingDates, txtAmount, txtBookingId;
        Button btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGear          = itemView.findViewById(R.id.imgGear);
            txtGearName      = itemView.findViewById(R.id.txtGearName);
            txtStatus        = itemView.findViewById(R.id.txtStatus);
            txtBookingDates  = itemView.findViewById(R.id.txtBookingDates);
            txtAmount        = itemView.findViewById(R.id.txtAmount);
            txtBookingId     = itemView.findViewById(R.id.txtBookingId);
            btnViewDetails   = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
