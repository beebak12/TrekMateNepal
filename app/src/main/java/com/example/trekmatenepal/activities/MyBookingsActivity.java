package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.BookingAdapter;
import com.example.trekmatenepal.models.BookingModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerBookings;
    private BottomNavigationView bottomNavigation;
    private List<BookingModel> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initializeViews();
        loadSampleData();
        setupRecyclerView();
        setupBottomNavigation();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        recyclerBookings = findViewById(R.id.recyclerBookings);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void loadSampleData() {
        bookingList = new ArrayList<>();
        bookingList.add(new BookingModel(
                "Down Jacket",
                "20 May - 25 May 2025",
                "Rs. 1,350",
                "TM0123456",
                "Confirmed",
                R.drawable.jacket
        ));
        bookingList.add(new BookingModel(
                "Trekking Backpack",
                "10 Jun - 15 Jun 2025",
                "Rs. 1,250",
                "TM0123457",
                "Confirmed",
                R.drawable.backpack
        ));
    }

    private void setupRecyclerView() {
        BookingAdapter adapter = new BookingAdapter(bookingList);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerBookings.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_gear) {
                startActivity(new Intent(this, GearRentalActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_partner) {
                startActivity(new Intent(this, PartnerFinderActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}