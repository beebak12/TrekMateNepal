package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.CategoryAdapter;
import com.example.trekmatenepal.adapters.RentalGearAdapter;
import com.example.trekmatenepal.models.CategoryModel;
import com.example.trekmatenepal.models.RentalGearModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class GearRentalActivity extends AppCompatActivity {

    private RecyclerView recyclerGear;
    private ArrayList<RentalGearModel> gearList;
    private ArrayList<RentalGearModel> allGearList;
    private RentalGearAdapter adapter;

    private RecyclerView recyclerCategory;
    private ArrayList<CategoryModel> categoryList;
    
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_rental);

        initializeViews();
        loadCategories();
        setupCategoryRecycler();
        loadSampleGear();
        setupRecyclerView();
        setupBottomNavigation();
    }

    private void initializeViews() {
        recyclerGear = findViewById(R.id.recyclerGear);
        recyclerCategory = findViewById(R.id.recyclerCategory);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void loadSampleGear() {
        gearList = new ArrayList<>();
        allGearList = new ArrayList<>();

        gearList.add(new RentalGearModel(R.drawable.jacket, "Down Jacket", "Trekking", "4.8", "Rs. 300 / day", "Available"));
        gearList.add(new RentalGearModel(R.drawable.backpack, "Trekking Backpack", "Camping", "4.6", "Rs. 250 / day", "Available"));
        gearList.add(new RentalGearModel(R.drawable.boots, "Trekking Boots", "Hiking", "4.7", "Rs. 400 / day", "Available"));
        gearList.add(new RentalGearModel(R.drawable.sleepingbag, "Sleeping Bag", "Camping", "4.5", "Rs. 200 / day", "Available"));

        allGearList.addAll(gearList);
    }

    private void setupRecyclerView() {
        adapter = new RentalGearAdapter(this, gearList);
        recyclerGear.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerGear.setAdapter(adapter);
    }

    private void filterGear(String category) {
        gearList.clear();
        if (category.equals("All")) {
            gearList.addAll(allGearList);
        } else {
            for (RentalGearModel gear : allGearList) {
                if (gear.getCategory().equalsIgnoreCase(category)) {
                    gearList.add(gear);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadCategories() {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryModel(R.drawable.baseline_apps_24, "All"));
        categoryList.add(new CategoryModel(R.drawable.ic_trekking, "Trekking"));
        categoryList.add(new CategoryModel(R.drawable.ic_camping, "Camping"));
        categoryList.add(new CategoryModel(R.drawable.ic_climbing, "Climbing"));
        categoryList.add(new CategoryModel(R.drawable.ic_hiking, "Hiking"));
    }

    private void setupCategoryRecycler() {
        CategoryAdapter adapter = new CategoryAdapter(categoryList, category -> filterGear(category));
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategory.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_gear);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_gear) {
                return true;
            } else if (id == R.id.nav_partner) {
                startActivity(new Intent(this, PartnerFinderActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}