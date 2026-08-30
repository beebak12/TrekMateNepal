package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuideProfileActivity extends AppCompatActivity {

    private Button btnEditProfile, btnLogout;
    private BottomNavigationView bottomNavigationGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_profile);

        initializeViews();
        setupBottomNavigation();
        setupClickListeners();
    }

    private void initializeViews() {
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationGuide = findViewById(R.id.bottomNavigationGuide);
    }

    private void setupBottomNavigation() {
        if (bottomNavigationGuide == null) return; // Null check for included layout views
        
        bottomNavigationGuide.setSelectedItemId(R.id.guide_nav_profile);
        bottomNavigationGuide.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.guide_nav_home) {
                try {
                    startActivity(new Intent(this, GuideDashboardActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.guide_nav_requests) {
                try {
                    startActivity(new Intent(this, GuideBookingRequestsActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else if (id == R.id.guide_nav_packages) {
                try {
                    startActivity(new Intent(this, GuidePackagesActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else return id == R.id.guide_nav_profile;
        });
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideEditProfileActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    clearSession();
                    startActivity(new Intent(this, LoginActivity.class));
                    finishAffinity();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearSession() {
        SharedPreferences prefs = getSharedPreferences("TrekMatePrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}