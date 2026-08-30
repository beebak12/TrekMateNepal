package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingModel;

/**
 * BookingConfirmationActivity — shown after a booking is confirmed.
 * Displays booking ID, gear details, dates, total amount.
 *
 * Backend note: when API is connected, receive the server-generated booking ID
 * via Intent extras and display it here instead of the local one.
 */
public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView tvBookingId, tvGearName, tvDates,
            tvUserName, tvUserContact, tvBookedDate,
            tvQuantity, tvPickup, tvTotal, btnBackHome;
    private Button btnViewBookings;

    private BookingModel booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        initializeViews();
        loadData();
        populateUI();
        setupClickListeners();
    }

    private void initializeViews() {
        tvBookingId   = findViewById(R.id.tvBookingId);
        tvGearName    = findViewById(R.id.tvGearName);
        tvDates       = findViewById(R.id.tvDates);
        tvUserName    = findViewById(R.id.tvUserName);
        tvUserContact = findViewById(R.id.tvUserContact);
        tvBookedDate  = findViewById(R.id.tvBookedDate);
        tvQuantity    = findViewById(R.id.tvQuantity);
        tvPickup      = findViewById(R.id.tvPickup);
        tvTotal       = findViewById(R.id.tvTotal);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnBackHome   = findViewById(R.id.btnBackHome);
    }

    private void loadData() {
        booking = (BookingModel) getIntent().getSerializableExtra("booking");
    }

    private void populateUI() {
        if (booking == null) return;

        tvBookingId.setText("Booking ID: " + booking.getBookingId());
        tvGearName.setText(booking.getGearName());
        tvDates.setText(booking.getDates());
        tvUserName.setText(booking.getRenterName());
        tvUserContact.setText(booking.getRenterPhone());
        tvBookedDate.setText(booking.getBookedDate());
        tvQuantity.setText("Qty: " + booking.getQuantity());
        tvPickup.setText(booking.getPickupLocation());
        tvTotal.setText(booking.getAmount());
    }

    private void setupClickListeners() {
        btnViewBookings.setOnClickListener(v -> {
            startActivity(new Intent(this, MyBookingsActivity.class));
            finish();
        });

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to summary after confirmation
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
