package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GuideBookingAdapter;
import com.example.trekmatenepal.models.GuideBookingModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GuideDashboardActivity extends AppCompatActivity implements GuideBookingAdapter.OnBookingClickListener {

    private TextView txtHello, txtPendingCount, txtUpcomingCount, txtCompletedCount, txtTotalEarnings;
    private LinearLayout actionRequests, actionPackages, actionAvailability, actionEarnings;
    private RecyclerView recyclerUpcoming;
    private BottomNavigationView bottomNavigationGuide;
    private FloatingActionButton fabAddPackage;
    private List<GuideBookingModel> upcomingList;
    private GuideBookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_dashboard);

        initializeViews();
        setupBottomNavigation();
        setupClickListeners();
        loadSummaryData();
        setupRecyclerView();
    }

    private void initializeViews() {
        txtHello = findViewById(R.id.txtHello);
        txtPendingCount = findViewById(R.id.txtPendingCount);
        txtUpcomingCount = findViewById(R.id.txtUpcomingCount);
        txtCompletedCount = findViewById(R.id.txtCompletedCount);
        txtTotalEarnings = findViewById(R.id.txtTotalEarnings);

        actionRequests = findViewById(R.id.actionRequests);
        actionPackages = findViewById(R.id.actionPackages);
        actionAvailability = findViewById(R.id.actionAvailability);
        actionEarnings = findViewById(R.id.actionEarnings);

        recyclerUpcoming = findViewById(R.id.recyclerUpcoming);
        bottomNavigationGuide = findViewById(R.id.bottomNavigationGuide);
        fabAddPackage = findViewById(R.id.fabAddPackage);
    }

    private void setupBottomNavigation() {
        if (bottomNavigationGuide == null) return; // Null check for included layout views
        
        bottomNavigationGuide.setSelectedItemId(R.id.guide_nav_home);
        bottomNavigationGuide.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.guide_nav_home) {
                return true;
            } else if (id == R.id.guide_nav_requests) {
                try {
                    startActivity(new Intent(this, GuideBookingRequestsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.guide_nav_packages) {
                try {
                    startActivity(new Intent(this, GuidePackagesActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.guide_nav_profile) {
                try {
                    startActivity(new Intent(this, GuideProfileActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }

    private void setupClickListeners() {
        actionRequests.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideBookingRequestsActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        actionPackages.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuidePackagesActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        actionAvailability.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideAvailabilityActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        actionEarnings.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideEarningsActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        fabAddPackage.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideAddPackageActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadSummaryData() {
        // Mock data for summary
        txtHello.setText("Hello, Pemba 👋");
        txtPendingCount.setText("5");
        txtUpcomingCount.setText("3");
        txtCompletedCount.setText("28");
        txtTotalEarnings.setText("Rs. 85,000");
    }

    private void setupRecyclerView() {
        upcomingList = new ArrayList<>();
        upcomingList.add(new GuideBookingModel("Bibek Paudel", "Everest Base Camp", "20 May - 31 May", "2 Trekkers", "Rs. 28,000", "Confirmed", R.drawable.partner1));
        upcomingList.add(new GuideBookingModel("Anita Gurung", "Annapurna Base Camp", "15 Jun - 25 Jun", "1 Trekker", "Rs. 18,000", "Confirmed", R.drawable.partner4));

        adapter = new GuideBookingAdapter(upcomingList, this);
        recyclerUpcoming.setLayoutManager(new LinearLayoutManager(this));
        recyclerUpcoming.setAdapter(adapter);
    }

    @Override
    public void onAccept(GuideBookingModel booking) {
        // Not needed for dashboard, already confirmed
    }

    @Override
    public void onReject(GuideBookingModel booking) {
        // Not needed for dashboard
    }

    @Override
    public void onViewDetails(GuideBookingModel booking) {
        startActivity(new Intent(this, GuideBookingDetailsActivity.class));
    }
}