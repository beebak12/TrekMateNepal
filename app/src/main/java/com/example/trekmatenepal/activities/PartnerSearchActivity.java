package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PartnerFinderAdapter;
import com.example.trekmatenepal.models.PartnerModel;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartnerSearchActivity extends AppCompatActivity {

    private ImageView ivBackButton;
    private Spinner spinnerCategory, spinnerExperience, spinnerAvailable;
    private MaterialButton btnSearch;
    private RecyclerView rvSearchResults;
    private LinearLayout llEmptyState;
    private PartnerFinderAdapter partnerAdapter;
    private List<PartnerModel> partnersList;
    private List<PartnerModel> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_search);

        initializeViews();
        setupRecyclerView();
        setupSpinners();
        setupClickListeners();
        loadPartnersList();
    }

    private void initializeViews() {
        ivBackButton = findViewById(R.id.ivBackButton);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerExperience = findViewById(R.id.spinnerExperience);
        spinnerAvailable = findViewById(R.id.spinnerAvailable);
        btnSearch = findViewById(R.id.btnSearch);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        llEmptyState = findViewById(R.id.llEmptyState);
    }

    private void setupRecyclerView() {
        partnersList = new ArrayList<>();
        filteredList = new ArrayList<>();
        partnerAdapter = new PartnerFinderAdapter(filteredList, partner -> {
            Intent intent = new Intent(PartnerSearchActivity.this, PartnerProfileActivity.class);
            intent.putExtra("partner", partner);
            startActivity(intent);
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(partnerAdapter);
    }

    private void setupSpinners() {
        // Trek Category
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.trek_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Experience Level
        ArrayAdapter<CharSequence> experienceAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience_levels, android.R.layout.simple_spinner_item);
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperience.setAdapter(experienceAdapter);

        // Available Partners
        ArrayAdapter<CharSequence> availableAdapter = ArrayAdapter.createFromResource(this,
                R.array.available_options, android.R.layout.simple_spinner_item);
        availableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailable.setAdapter(availableAdapter);
    }

    private void setupClickListeners() {
        ivBackButton.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String category = spinnerCategory.getSelectedItem().toString();
            String experience = spinnerExperience.getSelectedItem().toString();
            String availability = spinnerAvailable.getSelectedItem().toString();

            performSearch(category, experience, availability);
        });
    }

    private void loadPartnersList() {
        // TODO: Replace with actual backend API call
        partnersList.clear();
        partnersList.addAll(generateMockPartners());
    }

    private void performSearch(String category, String experience, String availability) {
        filteredList.clear();

        for (PartnerModel partner : partnersList) {
            boolean matchesCategory = category.equals("All") || 
                (partner.getTreks() != null && partner.getTreks().contains(category));
            
            boolean matchesExperience = experience.equals("Any") ||
                (experience.equals("Beginner") && partner.getYearsOfExperience() <= 2) ||
                (experience.equals("Intermediate") && partner.getYearsOfExperience() > 2 && partner.getYearsOfExperience() <= 5) ||
                (experience.equals("Expert") && partner.getYearsOfExperience() > 5);

            boolean matchesAvailability = availability.equals("Any Partner") ||
                (availability.equals("Available Now") && partner.getSpotsAvailable() > 0) ||
                (availability.equals("Verified Only") && partner.isVerified());

            if (matchesCategory && matchesExperience && matchesAvailability) {
                filteredList.add(partner);
            }
        }

        updateUI();
        partnerAdapter.notifyDataSetChanged();
    }

    private void updateUI() {
        if (filteredList.isEmpty()) {
            rvSearchResults.setVisibility(RecyclerView.GONE);
            llEmptyState.setVisibility(LinearLayout.VISIBLE);
        } else {
            rvSearchResults.setVisibility(RecyclerView.VISIBLE);
            llEmptyState.setVisibility(LinearLayout.GONE);
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
        p1.setYearsOfExperience(8);
        partners.add(p1);

        PartnerModel p2 = new PartnerModel("Pemba Sherpa", "4.6", "(28)", "Available", R.drawable.partner2);
        p2.setVerified(true);
        p2.setSpotsAvailable(1);
        p2.setCostPerDay(2500.0);
        p2.setBaseLocation("Pokhara");
        p2.setDestination("Annapurna");
        p2.setYearsOfExperience(6);
        partners.add(p2);

        PartnerModel p3 = new PartnerModel("Kami Sherpa", "4.5", "(20)", "Busy", R.drawable.partner3);
        p3.setVerified(false);
        p3.setSpotsAvailable(3);
        p3.setCostPerDay(1800.0);
        p3.setBaseLocation("Kathmandu");
        p3.setDestination("Kili Trek");
        p3.setYearsOfExperience(5);
        partners.add(p3);

        PartnerModel p4 = new PartnerModel("Dawa Sherpa", "4.9", "(50)", "Available", R.drawable.partner4);
        p4.setVerified(true);
        p4.setSpotsAvailable(0);
        p4.setCostPerDay(3000.0);
        p4.setBaseLocation("Kathmandu");
        p4.setDestination("Manaslu");
        p4.setYearsOfExperience(10);
        partners.add(p4);

        PartnerModel p5 = new PartnerModel("Ang Dorje", "4.7", "(35)", "Available", R.drawable.partner1);
        p5.setVerified(true);
        p5.setSpotsAvailable(2);
        p5.setCostPerDay(2200.0);
        p5.setBaseLocation("Kathmandu");
        p5.setDestination("Langtang");
        p5.setYearsOfExperience(7);
        partners.add(p5);

        return partners;
    }
}
