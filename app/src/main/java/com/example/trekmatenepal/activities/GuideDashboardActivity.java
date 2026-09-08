package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.BookingRequestAdapter;
import com.example.trekmatenepal.models.BookingRequest;

import java.util.ArrayList;
import java.util.List;

public class GuideDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerBookingRequests;

    private BookingRequestAdapter bookingAdapter;

    private List<BookingRequest> bookingList;

    private View btnCreatePackage;
    private View btnMyPackages;
    private View btnQuickChat;
    private View btnProfile;

    private ImageButton btnNotifications;
    private ImageButton btnChat;

    private TextView tvViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide_dashboard);

        initializeViews();

        setupBookingRecyclerView();

        setupClickListeners();
    }


    private void initializeViews() {

        recyclerBookingRequests =
                findViewById(R.id.recyclerBookingRequests);

        btnCreatePackage =
                findViewById(R.id.btnCreatePackage);

        btnMyPackages =
                findViewById(R.id.btnMyPackages);

        btnQuickChat =
                findViewById(R.id.btnQuickChat);

        btnProfile =
                findViewById(R.id.btnProfile);

        btnNotifications =
                findViewById(R.id.btnNotifications);

        btnChat =
                findViewById(R.id.btnChat);

        tvViewAll =
                findViewById(R.id.tvViewAll);
    }


    private void setupBookingRecyclerView() {

        bookingList = new ArrayList<>();

        bookingList.add(
                new BookingRequest(
                        "John Anderson",
                        "Everest Base Camp Trek",
                        "guide_profile",
                        "May 20 - May 30",
                        "2 Trekkers",
                        "Rs. 95,000"
                )
        );

        bookingList.add(
                new BookingRequest(
                        "Sarah Wilson",
                        "Annapurna Circuit Trek",
                        "guide_profile",
                        "Jun 05 - Jun 15",
                        "1 Trekker",
                        "Rs. 75,000"
                )
        );

        bookingAdapter =
                new BookingRequestAdapter(bookingList);

        recyclerBookingRequests.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerBookingRequests.setAdapter(
                bookingAdapter
        );

        recyclerBookingRequests.setNestedScrollingEnabled(
                false
        );
    }


    private void setupClickListeners() {

        // CREATE PACKAGE

        btnCreatePackage.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Create Package selected",
                    Toast.LENGTH_SHORT
            ).show();


            // Later:
            startActivity(
                new Intent(
                    this,
                    CreatePackageActivity.class
                )
            );

        });


        // MY PACKAGES

        btnMyPackages.setOnClickListener(v -> {
            try {

                Intent intent = new Intent(
                        GuideDashboardActivity.this,
                        GuidePackagesActivity.class
                );

                startActivity(intent);
            } catch (Exception e) {

                        Toast.makeText(
                                this,
                                "Packages screen is not available yet",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
        });


        // QUICK CHAT

        btnQuickChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatListActivity.class));
        });


        // PROFILE

        btnProfile.setOnClickListener(v -> {

            try {

                Intent intent =
                        new Intent(
                                GuideDashboardActivity.this,
                                GuideProfileActivity.class
                        );

                startActivity(intent);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "Profile screen is not available yet",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // NOTIFICATIONS

        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationActivity.class));
        });


        // HEADER CHAT

        btnChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatListActivity.class));
        });


        // VIEW ALL

        tvViewAll.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "All booking requests",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // BOTTOM NAVIGATION - DASHBOARD

        findViewById(R.id.navDashboard)
                .setOnClickListener(v -> {

                    Toast.makeText(
                            this,
                            "Dashboard",
                            Toast.LENGTH_SHORT
                    ).show();
                });


        // BOTTOM NAVIGATION - PACKAGES

        findViewById(R.id.navPackages)
                .setOnClickListener(v -> {

                    Intent intent = new Intent(
                            GuideDashboardActivity.this,
                            GuidePackagesActivity.class
                    );

                    startActivity(intent);
                });


        // BOTTOM NAVIGATION - BOOKINGS

        findViewById(R.id.navBookings)
                .setOnClickListener(v -> {

                    Toast.makeText(
                            this,
                            "Bookings",
                            Toast.LENGTH_SHORT
                    ).show();
                });


        // BOTTOM NAVIGATION - CHAT

        findViewById(R.id.navChat)
                .setOnClickListener(v -> {
                    startActivity(new Intent(this, ChatListActivity.class));
                });


        // BOTTOM NAVIGATION - MORE

        findViewById(R.id.navMore)
                .setOnClickListener(v -> {

                    Intent intent = new Intent(
                            GuideDashboardActivity.this,
                            GuideProfileActivity.class
                    );

                    startActivity(intent);
                });
    }
}