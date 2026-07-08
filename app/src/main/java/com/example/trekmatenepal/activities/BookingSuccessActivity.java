package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class BookingSuccessActivity extends AppCompatActivity {

    private Button btnViewBookings;
    private TextView btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnViewBookings.setOnClickListener(v -> {
            startActivity(new Intent(this, MyBookingsActivity.class));
            finish();
        });

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}