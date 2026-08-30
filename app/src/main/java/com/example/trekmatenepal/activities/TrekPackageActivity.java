package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.TrekAdapter;
import com.example.trekmatenepal.models.TrekModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * TrekPackageActivity — shows curated trek packages.
 * Placeholder using same data as TrekListActivity.
 */
public class TrekPackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trek_package);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        RecyclerView recycler = findViewById(R.id.recyclerPackages);
        if (recycler != null) {
            ArrayList<TrekModel> list = new ArrayList<>();
            list.add(new TrekModel("Everest Base Camp","Khumbu, Nepal","14 Days",
                R.drawable.everest,"4.9",287,"Difficult","5,364m","130 km",
                "Stand at the foot of the world's highest peak.","Rs. 25,000"));
            list.add(new TrekModel("Annapurna Circuit","Manang/Mustang, Nepal","12 Days",
                R.drawable.annapurna,"4.7",198,"Moderate","5,416m","160 km",
                "One of the most diverse treks in the world.","Rs. 22,000"));
            list.add(new TrekModel("Langtang Valley","Rasuwa, Nepal","7 Days",
                R.drawable.langtang,"4.6",80,"Easy","3,800m","77 km",
                "A beautiful trek close to Kathmandu.","Rs. 15,000"));

            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setAdapter(new TrekAdapter(list));
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
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
}
