package com.example.trekmatenepal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingModel;

/**
 * BookingDetailsActivity — shows full details for a specific booking.
 * Received via Intent extra "booking" (BookingModel Serializable).
 *
 * - Chat with Seller → ChatActivity (existing)
 * - Cancel Booking → confirmation dialog, then updates status in UI
 *
 * Backend note: Cancel action should call an API endpoint to update status.
 */
public class BookingDetailsActivity extends AppCompatActivity {

    private ImageView btnBack, imgGear;
    private TextView tvBookingId, tvGearName, tvDates, tvQuantity,
            tvPickup, tvTotal, tvStatus;
    private Button btnChatSeller, btnCancelBooking;

    private BookingModel booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        initializeViews();
        loadData();
        populateUI();
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack          = findViewById(R.id.btnBack);
        imgGear          = findViewById(R.id.imgGear);
        tvBookingId      = findViewById(R.id.tvBookingId);
        tvGearName       = findViewById(R.id.tvGearName);
        tvDates          = findViewById(R.id.tvDates);
        tvQuantity       = findViewById(R.id.tvQuantity);
        tvPickup         = findViewById(R.id.tvPickup);
        tvTotal          = findViewById(R.id.tvTotal);
        tvStatus         = findViewById(R.id.tvStatus);
        btnChatSeller    = findViewById(R.id.btnChatSeller);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
    }

    private void loadData() {
        booking = (BookingModel) getIntent().getSerializableExtra("booking");
        if (booking == null) {
            finish();
        }
    }

    private void populateUI() {
        if (booking == null) return;

        int img = booking.getImage() != 0 ? booking.getImage() : R.drawable.jacket;
        imgGear.setImageResource(img);

        tvBookingId.setText(booking.getBookingId());
        tvGearName.setText(booking.getGearName());
        tvDates.setText(booking.getDates());
        tvQuantity.setText(String.valueOf(booking.getQuantity()));
        tvPickup.setText(booking.getPickupLocation().isEmpty()
                ? "Kathmandu" : booking.getPickupLocation());
        tvTotal.setText(booking.getAmount());

        setStatusAppearance(booking.getStatus());
    }

    private void setStatusAppearance(String status) {
        tvStatus.setText(status);
        int colorRes;
        int bgRes;
        switch (status.toLowerCase()) {
            case "confirmed":
            case "upcoming":
                colorRes = R.color.success_green;
                bgRes    = R.drawable.bg_status_confirmed;
                break;
            case "cancelled":
                colorRes = R.color.red;
                bgRes    = R.drawable.bg_status_cancelled;
                // Disable cancel button if already cancelled
                if (btnCancelBooking != null) btnCancelBooking.setEnabled(false);
                break;
            case "completed":
                colorRes = android.R.color.holo_blue_dark;
                bgRes    = R.drawable.bg_status_completed;
                if (btnCancelBooking != null) btnCancelBooking.setEnabled(false);
                break;
            default:
                colorRes = R.color.secondary_gray;
                bgRes    = R.drawable.bg_available_tag;
        }
        tvStatus.setTextColor(getResources().getColor(colorRes));
        tvStatus.setBackgroundResource(bgRes);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnChatSeller.setOnClickListener(v -> {
            // Open existing ChatActivity
            startActivity(new Intent(this, ChatActivity.class));
        });

        btnCancelBooking.setOnClickListener(v -> showCancelDialog());
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Cancel Booking", (dialog, which) -> cancelBooking())
                .setNegativeButton("Keep Booking", null)
                .show();
    }

    private void cancelBooking() {
        // Update locally — replace with API call when backend is ready
        if (booking != null) {
            booking.setStatus("Cancelled");
            populateUI();
            Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
