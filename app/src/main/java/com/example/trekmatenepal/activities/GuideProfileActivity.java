package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trekmatenepal.R;

public class GuideProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnEditProfile;

    private LinearLayout navDashboard;
    private LinearLayout navPackages;
    private LinearLayout navBookings;
    private LinearLayout navChat;
    private LinearLayout navMore;

    private LinearLayout profileTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allow the header background to draw behind the status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_guide_profile);

        initializeViews();

        setupSystemBars();

        setupClickListeners();
    }

    private void setupSystemBars() {

        View root = findViewById(R.id.profileRoot);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {

            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );

            int statusBarHeight = systemBars.top;

            // Normal toolbar/control area
            int toolbarHeight = dpToPx(56);

            // Status bar + toolbar
            int totalTopBarHeight = statusBarHeight + toolbarHeight;

            ViewGroup.LayoutParams params = profileTopBar.getLayoutParams();

            params.height = totalTopBarHeight;

            profileTopBar.setLayoutParams(params);

            /*
             * Keep the buttons at the bottom of the top bar.
             * The status-bar area stays empty/purple above them.
             */
            profileTopBar.setGravity(
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
            );

            profileTopBar.setPadding(
                    dpToPx(8),
                    0,
                    dpToPx(8),
                    dpToPx(4)
            );

            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }

    private int dpToPx(int dp) {
        return Math.round(
                dp * getResources().getDisplayMetrics().density
        );
    }

    private void initializeViews() {

        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        navDashboard = findViewById(R.id.navDashboard);
        navPackages = findViewById(R.id.navPackages);
        navBookings = findViewById(R.id.navBookings);
        navChat = findViewById(R.id.navChat);
        navMore = findViewById(R.id.navMore);

        profileTopBar = findViewById(R.id.profileTopBar);
    }

    private void setupClickListeners() {

        // Back
        btnBack.setOnClickListener(v -> finish());

        // Edit Profile
        btnEditProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    GuideProfileActivity.this,
                    GuideEditProfileActivity.class
            );

            startActivity(intent);
        });

        // Dashboard
        navDashboard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    GuideProfileActivity.this,
                    DashboardActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
        });

        // Packages
        navPackages.setOnClickListener(v -> {

            Intent intent = new Intent(
                    GuideProfileActivity.this,
                    GuidePackagesActivity.class
            );

            startActivity(intent);
        });

        // Bookings
        navBookings.setOnClickListener(v -> {
            // Implement later
        });

        // Chat
        navChat.setOnClickListener(v -> {
            // Implement later
        });

        // More
        navMore.setOnClickListener(v -> {
            // Implement later
        });
    }
}