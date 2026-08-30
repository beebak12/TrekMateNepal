package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PartnerFinderAdapter;
import com.example.trekmatenepal.models.PartnerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class PartnerHomeActivity extends AppCompatActivity {

    private ImageView ivBackButton, ivFilter;
    private EditText etSearch;
    private TabLayout tabLayout;
    private RecyclerView rvPartnersList;
    private PartnerFinderAdapter partnerAdapter;
    private List<PartnerModel> partnersList;
    private List<PartnerModel> filteredList;
    private String currentTab = "all";
    
    // Filter variables
    private String selectedDestination = "All";
    private String selectedDate = "Anytime";
    private String selectedDuration = "Any";
    private String selectedGender = "Any";
    private String selectedGroupType = "Any";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_home);

        initializeViews();
        setupRecyclerView();
        loadPartnersList();
        setupClickListeners();
        setupTabListener();
        setupSearchListener();
    }

    private void initializeViews() {
        ivBackButton = findViewById(R.id.ivBackButton);
        ivFilter = findViewById(R.id.ivFilter);
        etSearch = findViewById(R.id.etSearch);
        tabLayout = findViewById(R.id.tabLayout);
        rvPartnersList = findViewById(R.id.rvPartnersList);
    }

    private void setupRecyclerView() {
        partnersList = new ArrayList<>();
        filteredList = new ArrayList<>();
        partnerAdapter = new PartnerFinderAdapter(filteredList, partner -> {
            Intent intent = new Intent(PartnerHomeActivity.this, PartnerProfileActivity.class);
            intent.putExtra("partner", partner);
            startActivity(intent);
        });

        rvPartnersList.setLayoutManager(new LinearLayoutManager(this));
        rvPartnersList.setAdapter(partnerAdapter);
    }

    private void loadPartnersList() {
        // TODO: Replace with actual backend API call
        partnersList.clear();
        partnersList.addAll(generateMockPartners());
        filterPartners("all");
    }

    private void setupClickListeners() {
        ivBackButton.setOnClickListener(v -> finish());
        ivFilter.setOnClickListener(v -> showFilterBottomSheet());
    }

    private void setupTabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition() == 0 ? "all" :
                            tab.getPosition() == 1 ? "available" : "verified";
                filterPartners(currentTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterPartners(String tab) {
        filteredList.clear();
        for (PartnerModel partner : partnersList) {
            if (tab.equals("all")) {
                filteredList.add(partner);
            } else if (tab.equals("available") && partner.getSpotsAvailable() > 0) {
                filteredList.add(partner);
            } else if (tab.equals("verified") && partner.isVerified()) {
                filteredList.add(partner);
            }
        }
        
        if (partnerAdapter != null) {
            partnerAdapter.notifyDataSetChanged();
        }
    }

    private void filterBySearch(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filterPartners(currentTab);
        } else {
            for (PartnerModel partner : partnersList) {
                if (partner.getName().toLowerCase().contains(query.toLowerCase()) ||
                    partner.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(partner);
                }
            }
            
            if (partnerAdapter != null) {
                partnerAdapter.notifyDataSetChanged();
            }
        }
    }

    // ── Bottom Sheet Filter Dialog ───────────────────────────────────────────
    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        
        // Create filter sheet layout
        LinearLayout filterLayout = new LinearLayout(this);
        filterLayout.setOrientation(LinearLayout.VERTICAL);
        filterLayout.setPadding(16, 24, 16, 24);
        
        // Close button and title
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        
        TextView tvTitle = new TextView(this);
        tvTitle.setText("Find Partners");
        tvTitle.setTextSize(18);
        tvTitle.setTextColor(getResources().getColor(R.color.dark_text));
        tvTitle.setTypeface(tvTitle.getTypeface(), android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        tvTitle.setLayoutParams(titleParams);
        
        ImageView ivClose = new ImageView(this);
        ivClose.setImageResource(R.drawable.ic_cancel);
        ivClose.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivClose.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
        ivClose.setColorFilter(getResources().getColor(R.color.dark_text), android.graphics.PorterDuff.Mode.SRC_IN);
        ivClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        
        titleLayout.addView(tvTitle);
        titleLayout.addView(ivClose);
        filterLayout.addView(titleLayout);
        
        // Destination Filter
        filterLayout.addView(createFilterRow("📍 Destination", selectedDestination,
            new String[]{"All","Everest Base Camp","Annapurna Base Camp",
                        "Langtang Valley","Mardi Himal","Manaslu Circuit"},
            val -> selectedDestination = val));
        
        // Trek Date Filter
        filterLayout.addView(createFilterRow("📅 Trek Date", selectedDate,
            new String[]{"Anytime","This Week","This Month","Next Month",
                        "Apr 2026","May 2026"},
            val -> selectedDate = val));
        
        // Duration Filter
        filterLayout.addView(createFilterRow("⏱ Duration", selectedDuration,
            new String[]{"Any","1-3 Days","4-7 Days","8-14 Days","14+ Days"},
            val -> selectedDuration = val));
        
        // Gender Filter
        filterLayout.addView(createFilterRow("👤 Gender", selectedGender,
            new String[]{"Any","Male","Female","Non-binary"},
            val -> selectedGender = val));
        
        // Group Type Filter
        filterLayout.addView(createFilterRow("👥 Group Type", selectedGroupType,
            new String[]{"Any","Solo","Small Group (2-4)","Medium Group (5-8)"},
            val -> selectedGroupType = val));
        
        // Spacing
        TextView spacer = new TextView(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 16));
        filterLayout.addView(spacer);
        
        // Apply Filters Button
        Button btnApply = new Button(this);
        btnApply.setText("Apply Filters");
        btnApply.setTextSize(16);
        btnApply.setTextColor(getResources().getColor(R.color.white));
        btnApply.setTypeface(btnApply.getTypeface(), android.graphics.Typeface.BOLD);
        btnApply.setBackgroundColor(getResources().getColor(R.color.purple_primary));
        btnApply.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 56));
        btnApply.setOnClickListener(v -> {
            applyFiltersToList();
            bottomSheetDialog.dismiss();
        });
        filterLayout.addView(btnApply);
        
        bottomSheetDialog.setContentView(filterLayout);
        bottomSheetDialog.show();
    }
    
    // ── Create Filter Row ───────────────────────────────────────────────────
    @FunctionalInterface
    private interface FilterCallback {
        void onSelected(String value);
    }
    
    private LinearLayout createFilterRow(String label, String currentValue, 
                                         String[] options, FilterCallback callback) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 56);
        rowParams.setMargins(0, 0, 0, 8);
        row.setLayoutParams(rowParams);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_filter_row));
        row.setPadding(14, 0, 14, 0);
        
        // Label
        TextView tvLabel = new TextView(this);
        tvLabel.setText(label);
        tvLabel.setTextSize(14);
        tvLabel.setTextColor(getResources().getColor(R.color.dark_text));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        labelParams.setMargins(10, 0, 0, 0);
        tvLabel.setLayoutParams(labelParams);
        
        // Current value
        TextView tvValue = new TextView(this);
        tvValue.setText(currentValue + "  ›");
        tvValue.setTextSize(13);
        tvValue.setTextColor(getResources().getColor(R.color.purple_primary));
        tvValue.setTypeface(tvValue.getTypeface(), android.graphics.Typeface.BOLD);
        tvValue.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        
        row.addView(tvLabel);
        row.addView(tvValue);
        
        // Click listener to show dialog
        row.setOnClickListener(v -> showFilterOptionDialog(label, options, callback, tvValue));
        
        return row;
    }
    
    private void showFilterOptionDialog(String title, String[] options, 
                                        FilterCallback callback, TextView tvValue) {
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options, (dialog, which) -> {
                callback.onSelected(options[which]);
                tvValue.setText(options[which] + "  ›");
            })
            .show();
    }
    
    // ── Apply Filters ───────────────────────────────────────────────────────
    private void applyFiltersToList() {
        filteredList.clear();
        
        for (PartnerModel partner : partnersList) {
            boolean matches = true;
            
            // Check location (destination)
            if (!selectedDestination.equals("All") && 
                (partner.getDestination() == null || !partner.getDestination().contains(selectedDestination))) {
                matches = false;
            }
            
            // Check current tab filter
            if (matches) {
                if (currentTab.equals("available") && partner.getSpotsAvailable() <= 0) {
                    matches = false;
                } else if (currentTab.equals("verified") && !partner.isVerified()) {
                    matches = false;
                }
            }
            
            if (matches) {
                filteredList.add(partner);
            }
        }
        
        if (partnerAdapter != null) {
            partnerAdapter.notifyDataSetChanged();
        }
    }

    private List<PartnerModel> generateMockPartners() {
        List<PartnerModel> partners = new ArrayList<>();

        PartnerModel p1 = new PartnerModel("Nirajan Tamang", "4.8", "(32)", "Available", R.drawable.partner1);
        p1.setVerified(true);
        p1.setSpotsAvailable(2);
        p1.setCostPerDay(2000.0);
        p1.setBaseLocation("Lukla");
        p1.setDestination("Everest Base Camp");
        partners.add(p1);

        PartnerModel p2 = new PartnerModel("Pemba Sherpa", "4.6", "(28)", "Available", R.drawable.partner2);
        p2.setVerified(true);
        p2.setSpotsAvailable(1);
        p2.setCostPerDay(2500.0);
        p2.setBaseLocation("Pokhara");
        p2.setDestination("Annapurna");
        partners.add(p2);

        PartnerModel p3 = new PartnerModel("Kami Sherpa", "4.5", "(20)", "Busy", R.drawable.partner3);
        p3.setVerified(false);
        p3.setSpotsAvailable(3);
        p3.setCostPerDay(1800.0);
        p3.setBaseLocation("Kathmandu");
        p3.setDestination("Kili Trek");
        partners.add(p3);

        PartnerModel p4 = new PartnerModel("Dawa Sherpa", "4.9", "(50)", "Available", R.drawable.partner4);
        p4.setVerified(true);
        p4.setSpotsAvailable(0);
        p4.setCostPerDay(3000.0);
        p4.setBaseLocation("Kathmandu");
        p4.setDestination("Manaslu");
        partners.add(p4);

        PartnerModel p5 = new PartnerModel("Ang Dorje", "4.7", "(35)", "Available", R.drawable.partner1);
        p5.setVerified(true);
        p5.setSpotsAvailable(2);
        p5.setCostPerDay(2200.0);
        p5.setBaseLocation("Kathmandu");
        p5.setDestination("Langtang");
        partners.add(p5);

        return partners;
    }
}
