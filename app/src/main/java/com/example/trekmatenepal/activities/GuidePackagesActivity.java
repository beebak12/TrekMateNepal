package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GuidePackageAdapter;
import com.example.trekmatenepal.models.GuidePackageModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GuidePackagesActivity extends AppCompatActivity implements GuidePackageAdapter.OnPackageClickListener {

    private ImageView btnBack;
    private RecyclerView recyclerPackages;
    private List<GuidePackageModel> packageList;
    private GuidePackageAdapter adapter;
    private BottomNavigationView bottomNavigationGuide;
    private FloatingActionButton fabAddPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_packages);

        initializeViews();
        setupBottomNavigation();
        loadMockData();
        setupRecyclerView();

        btnBack.setOnClickListener(v -> finish());
        fabAddPackage.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GuideAddPackageActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        recyclerPackages = findViewById(R.id.recyclerPackages);
        bottomNavigationGuide = findViewById(R.id.bottomNavigationGuide);
        fabAddPackage = findViewById(R.id.fabAddPackage);
    }

    private void setupBottomNavigation() {
        if (bottomNavigationGuide == null) return; // Null check for included layout views
        
        bottomNavigationGuide.setSelectedItemId(R.id.guide_nav_packages);
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
                return true;
            } else if (id == R.id.guide_nav_profile) {
                try {
                    startActivity(new Intent(this, GuideProfileActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }

    private void loadMockData() {
        packageList = new ArrayList<>();
        packageList.add(new GuidePackageModel("Everest Base Camp Guide Package", "Solukhumbu", "12 Days", "Difficult", "Rs. 30,000", "Active", R.drawable.everest));
        packageList.add(new GuidePackageModel("Annapurna Circuit Guide Package", "Manang/Mustang", "10 Days", "Moderate", "Rs. 25,000", "Active", R.drawable.annapurna));
    }

    private void setupRecyclerView() {
        adapter = new GuidePackageAdapter(packageList, this);
        recyclerPackages.setLayoutManager(new LinearLayoutManager(this));
        recyclerPackages.setAdapter(adapter);
    }

    @Override
    public void onEdit(GuidePackageModel pkg) {
        startActivity(new Intent(this, GuideEditPackageActivity.class));
    }

    @Override
    public void onView(GuidePackageModel pkg) {
        Toast.makeText(this, "Viewing " + pkg.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(GuidePackageModel pkg) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Package")
                .setMessage("Are you sure you want to delete this package?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    packageList.remove(pkg);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Package Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}