package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ProfileActivity — shows the current user's profile.
 * Placeholder implementation; full backend integration pending.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v ->
                android.widget.Toast.makeText(this,
                    "Edit profile coming soon", android.widget.Toast.LENGTH_SHORT).show());
        }

        Button btnMyBookings = findViewById(R.id.btnMyBookings);
        if (btnMyBookings != null) {
            btnMyBookings.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(this, MyBookingsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Button btnMyRequests = findViewById(R.id.btnMyRequests);
        if (btnMyRequests != null) {
            btnMyRequests.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(this, MyRequestsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
            bottomNav.setOnItemSelectedListener(item -> {
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
                    return true;
                }
                return false;
            });
        }
    }
}
