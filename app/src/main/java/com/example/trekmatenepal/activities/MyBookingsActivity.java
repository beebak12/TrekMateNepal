package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.BookingAdapter;
import com.example.trekmatenepal.models.BookingModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBookingsActivity — shows all bookings in Upcoming / Completed / Cancelled tabs.
 * Tapping "View Details" on any card → BookingDetailsActivity.
 */
public class MyBookingsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerBookings;
    private BottomNavigationView bottomNavigation;
    private TabLayout tabLayout;
    private TextView tvEmptyBookings;

    private List<BookingModel> allBookings;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initializeViews();
        loadSampleData();
        setupRecyclerView();
        setupTabs();
        setupBottomNavigation();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        btnBack          = findViewById(R.id.btnBack);
        recyclerBookings = findViewById(R.id.recyclerBookings);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        tabLayout        = findViewById(R.id.tabLayout);
        tvEmptyBookings  = findViewById(R.id.tvEmptyBookings);
    }

    // ── Sample data (replace with API call later) ─────────────────────────────
    private void loadSampleData() {
        allBookings = new ArrayList<>();

        allBookings.add(new BookingModel(
                "Down Jacket",
                "20 Aug 2026", "27 Aug 2026",
                "20 Aug 2026 → 27 Aug 2026",
                1, "Kathmandu", "",
                "2000", 7,
                "Rs. 2,100", "BK-2026-0001", "Confirmed",
                R.drawable.jacket
        ));

        allBookings.add(new BookingModel(
                "Trekking Backpack",
                "10 Sep 2026", "17 Sep 2026",
                "10 Sep 2026 → 17 Sep 2026",
                1, "Pokhara", "",
                "1500", 7,
                "Rs. 1,575", "BK-2026-0002", "Confirmed",
                R.drawable.backpack
        ));

        allBookings.add(new BookingModel(
                "Sleeping Bag",
                "01 Jul 2026", "08 Jul 2026",
                "01 Jul 2026 → 08 Jul 2026",
                1, "Kathmandu", "",
                "1000", 7,
                "Rs. 1,050", "BK-2026-0003", "Completed",
                R.drawable.sleepingbag
        ));

        allBookings.add(new BookingModel(
                "Trekking Boots",
                "15 Jun 2026", "22 Jun 2026",
                "15 Jun 2026 → 22 Jun 2026",
                1, "Kathmandu", "",
                "1800", 7,
                "Rs. 1,890", "BK-2026-0004", "Cancelled",
                R.drawable.boots
        ));
    }

    private void setupRecyclerView() {
        adapter = new BookingAdapter(filterByTab("Upcoming"), booking -> {
            Intent intent = new Intent(this, BookingDetailsActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerBookings.setAdapter(adapter);
        updateEmptyState(filterByTab("Upcoming"));
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (adapter == null) return; // Safety check
                
                String label = tab.getText() != null ? tab.getText().toString() : "Upcoming";
                List<BookingModel> filtered = filterByTab(label);
                adapter.updateList(filtered);
                updateEmptyState(filtered);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private List<BookingModel> filterByTab(String label) {
        List<BookingModel> result = new ArrayList<>();
        for (BookingModel b : allBookings) {
            switch (label) {
                case "Upcoming":
                    if ("Confirmed".equalsIgnoreCase(b.getStatus())
                            || "Pending".equalsIgnoreCase(b.getStatus()))
                        result.add(b);
                    break;
                case "Completed":
                    if ("Completed".equalsIgnoreCase(b.getStatus()))
                        result.add(b);
                    break;
                case "Cancelled":
                    if ("Cancelled".equalsIgnoreCase(b.getStatus()))
                        result.add(b);
                    break;
            }
        }
        return result;
    }

    private void updateEmptyState(List<BookingModel> list) {
        if (tvEmptyBookings != null) {
            tvEmptyBookings.setVisibility(
                    list.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        }
        recyclerBookings.setVisibility(
                list.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
    }

    private void setupBottomNavigation() {
        if (bottomNavigation == null) return; // Null check for included layout views
        
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                try {
                    startActivity(new Intent(this, DashboardActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.nav_gear) {
                try {
                    startActivity(new Intent(this, GearRentalActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.nav_partner) {
                try {
                    startActivity(new Intent(this, PartnerFinderActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.nav_profile) {
                try {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.putExtra("open_menu", true);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }
}
