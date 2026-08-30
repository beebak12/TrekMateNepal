package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingModel;

/**
 * BookingConfirmedActivity — displays booking confirmation with booking ID.
 * This is the success screen after payment is processed.
 */
public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        Intent intent = getIntent();
        String bookingId = intent.getStringExtra("bookingId");
        String guideName = intent.getStringExtra("guideName");

        // Set booking ID
        TextView tvBookingId = findViewById(R.id.tvBookingId);
        if (tvBookingId != null && bookingId != null) {
            tvBookingId.setText(bookingId);
        }

        // Set success message
        TextView tvMessage = findViewById(R.id.tvMessage);
        if (tvMessage != null && guideName != null) {
            tvMessage.setText("Your booking with " + guideName + " has been confirmed.");
        }

        // View My Bookings button
        Button btnViewBookings = findViewById(R.id.btnViewBookings);
        if (btnViewBookings != null) {
            btnViewBookings.setOnClickListener(v -> {
                startActivity(new Intent(this, MyBookingsActivity.class));
                finish();
            });
        }

        // Go to Dashboard button
        Button btnBackHome = findViewById(R.id.btnBackHome);
        if (btnBackHome != null) {
            btnBackHome.setOnClickListener(v -> {
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            });
        }
    }
}
