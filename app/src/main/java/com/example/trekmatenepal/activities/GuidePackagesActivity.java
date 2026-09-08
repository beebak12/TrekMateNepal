package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PackageAdapter;
import com.example.trekmatenepal.models.Package;

import java.util.ArrayList;

public class GuidePackagesActivity extends AppCompatActivity {

    private RecyclerView recyclerPackages;

    private TextView tabAll;
    private TextView tabActive;
    private TextView tabInactive;

    private ImageView btnBack, btnAddPackage;

    private ArrayList<Package> allPackages;
    private ArrayList<Package> filteredPackages;

    private PackageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide_packages);

        // Find views
        recyclerPackages = findViewById(R.id.recyclerPackages);

        tabAll = findViewById(R.id.tabAll);
        tabActive = findViewById(R.id.tabActive);
        tabInactive = findViewById(R.id.tabInactive);

        btnBack = findViewById(R.id.btnBack);
        btnAddPackage = findViewById(R.id.btnAddPackage);

        // Create lists
        allPackages = new ArrayList<>();
        filteredPackages = new ArrayList<>();

        // Load package data
        loadPackages();

        // Initially show all packages
        filteredPackages.addAll(allPackages);

        // Set adapter
        adapter = new PackageAdapter(filteredPackages);

        if (recyclerPackages != null) {
            recyclerPackages.setLayoutManager(
                    new LinearLayoutManager(this)
            );
            recyclerPackages.setAdapter(adapter);
        }

        // Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
            });
        }

        // Add Package button
        if (btnAddPackage != null) {
            btnAddPackage.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddPackageActivity.class);
                startActivity(intent);
            });
        } else {
            // Log or show toast to debug why it is null
            Toast.makeText(this, "Add Package button not found", Toast.LENGTH_SHORT).show();
        }

        // Tab click listeners
        if (tabAll != null) tabAll.setOnClickListener(v -> showAllPackages());
        if (tabActive != null) tabActive.setOnClickListener(v -> showActivePackages());
        if (tabInactive != null) tabInactive.setOnClickListener(v -> showInactivePackages());

        // Select All tab when page opens
        if (tabAll != null) selectTab(tabAll);
    }

    private void loadPackages() {

        allPackages.add(new Package(
                R.drawable.everest,
                "Everest Base Camp Trek",
                "Solukhumbu, Nepal",
                "14 Days",
                "Experience an unforgettable journey to the base of Mount Everest through beautiful Himalayan landscapes.",
                "ACTIVE"
        ));

        allPackages.add(new Package(
                R.drawable.annapurna,
                "Annapurna Circuit Trek",
                "Annapurna Region, Nepal",
                "12 Days",
                "Explore diverse landscapes, traditional villages and spectacular mountain views around the Annapurna region.",
                "ACTIVE"
        ));

        allPackages.add(new Package(
                R.drawable.langtang,
                "Langtang Valley Trek",
                "Langtang, Nepal",
                "8 Days",
                "Discover beautiful valleys, mountain scenery and traditional Tamang culture on this scenic trek.",
                "ACTIVE"
        ));

        allPackages.add(new Package(
                R.drawable.mardihimal,
                "Mardi Himal Trek",
                "Kaski, Nepal",
                "7 Days",
                "Enjoy a short and beautiful Himalayan adventure with stunning views of Mardi Himal and the Annapurna range.",
                "INACTIVE"
        ));

        allPackages.add(new Package(
                R.drawable.mountain_bg,
                "Upper Mustang Trek",
                "Mustang, Nepal",
                "15 Days",
                "Explore the unique landscapes, ancient monasteries and fascinating culture of the Upper Mustang region.",
                "INACTIVE"
        ));
    }

    private void showAllPackages() {
        filteredPackages.clear();
        filteredPackages.addAll(allPackages);
        adapter.notifyDataSetChanged();
        selectTab(tabAll);
    }

    private void showActivePackages() {
        filteredPackages.clear();
        for (Package packageItem : allPackages) {
            if (packageItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                filteredPackages.add(packageItem);
            }
        }
        adapter.notifyDataSetChanged();
        selectTab(tabActive);
    }

    private void showInactivePackages() {
        filteredPackages.clear();
        for (Package packageItem : allPackages) {
            if (packageItem.getStatus().equalsIgnoreCase("INACTIVE")) {
                filteredPackages.add(packageItem);
            }
        }
        adapter.notifyDataSetChanged();
        selectTab(tabInactive);
    }

    private void selectTab(TextView selectedTab) {
        if (tabAll == null || tabActive == null || tabInactive == null) return;

        int normalColor = android.graphics.Color.rgb(117, 117, 117);
        int activeColor = android.graphics.Color.rgb(106, 27, 154);

        tabAll.setTextColor(normalColor);
        tabActive.setTextColor(normalColor);
        tabInactive.setTextColor(normalColor);

        selectedTab.setTextColor(activeColor);

        tabAll.setTypeface(null, android.graphics.Typeface.NORMAL);
        tabActive.setTypeface(null, android.graphics.Typeface.NORMAL);
        tabInactive.setTypeface(null, android.graphics.Typeface.NORMAL);

        selectedTab.setTypeface(null, android.graphics.Typeface.BOLD);
    }
}