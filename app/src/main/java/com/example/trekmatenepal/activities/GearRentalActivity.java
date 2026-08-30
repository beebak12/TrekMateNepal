package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.CategoryAdapter;
import com.example.trekmatenepal.adapters.RentalGearAdapter;
import com.example.trekmatenepal.data.GearRepository;
import com.example.trekmatenepal.fragments.ChatBottomSheetFragment;
import com.example.trekmatenepal.models.CategoryModel;
import com.example.trekmatenepal.models.RentalGearModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * GearRentalActivity — main Gear Rental screen.
 * Shows categories (horizontal), search bar, and a grid of all rental gear.
 * Tapping a gear item → GearDetailActivity.
 */
public class GearRentalActivity extends AppCompatActivity {

    private RecyclerView recyclerGear;
    private ArrayList<RentalGearModel> gearList;        // currently displayed
    private ArrayList<RentalGearModel> allGearList;     // master list
    private RentalGearAdapter adapter;

    private RecyclerView recyclerCategory;
    private ArrayList<CategoryModel> categoryList;

    private EditText etSearch;
    private ImageView btnBack;
    private TextView tvEmptyState;

    private BottomNavigationView bottomNavigation;

    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_rental);

        initializeViews();
        loadSampleGear();
        setupCategoryRecycler();
        setupGearRecycler();
        setupSearch();
        setupBottomNavigation();

        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.fabChatContainer).setOnClickListener(v -> {
            ChatBottomSheetFragment chatBottomSheet = new ChatBottomSheetFragment();
            chatBottomSheet.show(getSupportFragmentManager(), chatBottomSheet.getTag());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh so gear just posted in PostGearActivity shows up immediately.
        if (adapter == null) return;
        allGearList.clear();
        addSeedGear();
        allGearList.addAll(0, GearRepository.getUserGear(this));
        applyFilters(); // mutates gearList in place + notifies the adapter
    }

    private void initializeViews() {
        recyclerGear     = findViewById(R.id.recyclerGear);
        recyclerCategory = findViewById(R.id.recyclerCategory);
        etSearch         = findViewById(R.id.etSearch);
        btnBack          = findViewById(R.id.btnBack);
        tvEmptyState     = findViewById(R.id.tvEmptyState);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    // ── Load comprehensive sample gear (replace with API call later) ─────────
    private void loadSampleGear() {
        allGearList = new ArrayList<>();
        addSeedGear();
        // Merge user-posted gear (from PostGearActivity) at the top of the list
        allGearList.addAll(0, GearRepository.getUserGear(this));
        gearList = new ArrayList<>(allGearList);
    }

    // ── Built-in sample gear (replace with API call later) ───────────────────
    private void addSeedGear() {
        allGearList.add(new RentalGearModel(
                R.drawable.jacket, "Down Jacket", "Clothing", "4.8",
                "Rs. 2,000 / week", "2000", "Available",
                "Kathmandu",
                "Warm and lightweight trekking down jacket suitable for high altitude trekking in Nepal.",
                "M / L / XL", "Excellent", "Trek Gear Nepal"));

        allGearList.add(new RentalGearModel(
                R.drawable.sleepingbag, "Sleeping Bag", "Sleeping", "4.7",
                "Rs. 1,000 / week", "1000", "Available",
                "Kathmandu",
                "3-season sleeping bag rated to -10°C, perfect for Nepal trekking.",
                "Standard", "Good", "Himalaya Rentals"));

        allGearList.add(new RentalGearModel(
                R.drawable.backpack, "Trekking Backpack", "Bags", "4.9",
                "Rs. 1,500 / week", "1500", "Available",
                "Pokhara",
                "60L trekking backpack with rain cover and ergonomic back support.",
                "60L", "Excellent", "Nepal Trek Store"));

        allGearList.add(new RentalGearModel(
                R.drawable.boots, "Trekking Boots", "Footwear", "4.8",
                "Rs. 1,800 / week", "1800", "Available",
                "Kathmandu",
                "Waterproof ankle-support trekking boots, ideal for rocky terrain.",
                "Size 40–45", "Good", "Trek Gear Nepal"));

        allGearList.add(new RentalGearModel(
                R.drawable.poles, "Trekking Pole", "Trekking Tools", "4.6",
                "Rs. 500 / week", "500", "Available",
                "Kathmandu",
                "Lightweight aluminum trekking pole with rubber tip, adjustable height.",
                "Adjustable", "Good", "Himalaya Rentals"));

        allGearList.add(new RentalGearModel(
                R.drawable.tent, "Camping Tent", "Camping", "4.7",
                "Rs. 2,500 / week", "2500", "Available",
                "Pokhara",
                "2-person waterproof camping tent, wind resistant up to 60 km/h.",
                "2 Person", "Excellent", "Nepal Trek Store"));

        allGearList.add(new RentalGearModel(
                R.drawable.headlamp, "Headlamp", "Trekking Tools", "4.5",
                "Rs. 300 / week", "300", "Available",
                "Kathmandu",
                "200 lumen rechargeable LED headlamp with adjustable beam.",
                "Universal", "Good", "Trek Gear Nepal"));

        allGearList.add(new RentalGearModel(
                R.drawable.jacket, "Rain Jacket", "Clothing", "4.7",
                "Rs. 1,200 / week", "1200", "Available",
                "Kathmandu",
                "Waterproof and breathable rain jacket with sealed seams.",
                "S / M / L / XL", "Excellent", "Himalaya Rentals"));

        allGearList.add(new RentalGearModel(
                R.drawable.gloves, "Trekking Gloves", "Clothing", "4.4",
                "Rs. 400 / week", "400", "Available",
                "Kathmandu",
                "Insulated trekking gloves with touchscreen-compatible fingertips.",
                "S / M / L", "Good", "Trek Gear Nepal"));

        allGearList.add(new RentalGearModel(
                R.drawable.backpack, "Duffel Bag", "Bags", "4.5",
                "Rs. 800 / week", "800", "Available",
                "Pokhara",
                "80L duffel bag with padlock and waterproof lining, ideal for porters.",
                "80L", "Good", "Nepal Trek Store"));
    }

    private void setupGearRecycler() {
        adapter = new RentalGearAdapter(this, gearList);
        recyclerGear.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerGear.setAdapter(adapter);
        updateEmptyState();
    }

    // ── Category filter ──────────────────────────────────────────────────────
    private void loadCategories() {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryModel(R.drawable.ic_menu,  "All"));
        categoryList.add(new CategoryModel(R.drawable.ic_trekking,  "Clothing"));
        categoryList.add(new CategoryModel(R.drawable.ic_camping,   "Bags"));
        categoryList.add(new CategoryModel(R.drawable.ic_camping,   "Sleeping"));
        categoryList.add(new CategoryModel(R.drawable.ic_hiking,    "Footwear"));
        categoryList.add(new CategoryModel(R.drawable.ic_trekking,  "Trekking Tools"));
        categoryList.add(new CategoryModel(R.drawable.ic_camping,   "Camping"));
    }

    private void setupCategoryRecycler() {
        loadCategories();
        CategoryAdapter catAdapter = new CategoryAdapter(categoryList, category -> {
            selectedCategory = category;
            applyFilters();
        });
        recyclerCategory.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategory.setAdapter(catAdapter);
    }

    // ── Search filter ────────────────────────────────────────────────────────
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { applyFilters(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // ── Combined filter: category + search query ─────────────────────────────
    private void applyFilters() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        gearList.clear();

        for (RentalGearModel gear : allGearList) {
            boolean matchesCategory = selectedCategory.equals("All")
                    || gear.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesSearch = query.isEmpty()
                    || gear.getName().toLowerCase().contains(query)
                    || gear.getCategory().toLowerCase().contains(query);

            if (matchesCategory && matchesSearch) {
                gearList.add(gear);
            }
        }
        
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (tvEmptyState != null) {
            tvEmptyState.setVisibility(gearList.isEmpty()
                    ? android.view.View.VISIBLE
                    : android.view.View.GONE);
        }
    }

    // ── Bottom navigation ────────────────────────────────────────────────────
    private void setupBottomNavigation() {
        if (bottomNavigation == null) return; // Null check for included layout views
        
        bottomNavigation.setSelectedItemId(R.id.nav_gear);
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
