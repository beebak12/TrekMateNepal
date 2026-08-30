package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GuideBookingAdapter;
import com.example.trekmatenepal.models.GuideBookingModel;

import java.util.ArrayList;
import java.util.List;

public class GuideBookingRequestsActivity extends AppCompatActivity implements GuideBookingAdapter.OnBookingClickListener {

    private ImageView btnBack;
    private RecyclerView recyclerRequests;
    private List<GuideBookingModel> requestList;
    private GuideBookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_booking_requests);

        btnBack = findViewById(R.id.btnBack);
        recyclerRequests = findViewById(R.id.recyclerRequests);

        btnBack.setOnClickListener(v -> finish());

        loadMockData();
        setupRecyclerView();
    }

    private void loadMockData() {
        requestList = new ArrayList<>();
        requestList.add(new GuideBookingModel("Bibek Paudel", "Everest Base Camp", "20 May - 31 May", "2 Trekkers", "Rs. 28,000", "Pending", R.drawable.partner1));
        requestList.add(new GuideBookingModel("Anita Gurung", "Annapurna Base Camp", "15 Jun - 25 Jun", "1 Trekker", "Rs. 18,000", "Pending", R.drawable.partner4));
        requestList.add(new GuideBookingModel("Ramesh Bhandari", "Mardi Himal", "01 Jul - 07 Jul", "4 Trekkers", "Rs. 12,000", "Pending", R.drawable.partner3));
    }

    private void setupRecyclerView() {
        adapter = new GuideBookingAdapter(requestList, this);
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerRequests.setAdapter(adapter);
    }

    @Override
    public void onAccept(GuideBookingModel booking) {
        new AlertDialog.Builder(this)
                .setTitle("Accept Booking")
                .setMessage("Are you sure you want to accept this booking from " + booking.getTrekkerName() + "?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    booking.setStatus("Confirmed");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Booking Accepted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onReject(GuideBookingModel booking) {
        new AlertDialog.Builder(this)
                .setTitle("Reject Booking")
                .setMessage("Are you sure you want to reject this booking?")
                .setPositiveButton("Reject", (dialog, which) -> {
                    booking.setStatus("Rejected");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Booking Rejected", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onViewDetails(GuideBookingModel booking) {
        // Will implement details navigation later
        Toast.makeText(this, "Opening details for " + booking.getTrekkerName(), Toast.LENGTH_SHORT).show();
    }
}