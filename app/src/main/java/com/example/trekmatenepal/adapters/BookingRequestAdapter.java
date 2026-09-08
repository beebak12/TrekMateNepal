package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingRequest;

import java.util.List;

public class BookingRequestAdapter
        extends RecyclerView.Adapter<BookingRequestAdapter.ViewHolder> {

    private final List<BookingRequest> bookingList;

    public BookingRequestAdapter(List<BookingRequest> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_booking_request,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        BookingRequest booking =
                bookingList.get(position);

        holder.tvTrekName.setText(
                booking.getTrekName()
        );

        holder.tvUserName.setText(
                booking.getTrekkerName()
        );

        holder.tvBookingInfo.setText(
                booking.getNumberOfTrekkers()
                        + "  •  "
                        + booking.getBookingDate()
        );

        holder.tvAmount.setText(
                booking.getPrice()
        );

        // ACCEPT

        holder.btnAccept.setOnClickListener(v -> {

            bookingList.remove(holder.getAdapterPosition());

            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(
                    holder.getAdapterPosition(),
                    bookingList.size()
            );
        });


        // REJECT

        holder.btnReject.setOnClickListener(v -> {

            bookingList.remove(holder.getAdapterPosition());

            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(
                    holder.getAdapterPosition(),
                    bookingList.size()
            );
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTrekName;
        TextView tvUserName;
        TextView tvBookingInfo;
        TextView tvAmount;

        Button btnAccept;
        Button btnReject;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvTrekName =
                    itemView.findViewById(R.id.tvTrekName);

            tvUserName =
                    itemView.findViewById(R.id.tvUserName);

            tvBookingInfo =
                    itemView.findViewById(R.id.tvBookingInfo);

            tvAmount =
                    itemView.findViewById(R.id.tvAmount);

            btnAccept =
                    itemView.findViewById(R.id.btnAccept);

            btnReject =
                    itemView.findViewById(R.id.btnReject);
        }
    }
}