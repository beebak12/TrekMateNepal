package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.trekmatenepal.R;

public class PartnerFindingActivity extends AppCompatActivity {

    private CardView cardOpenPartners, cardPartnersHome, cardSearchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_finding);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        cardOpenPartners = findViewById(R.id.cardOpenPartners);
        cardPartnersHome = findViewById(R.id.cardPartnersHome);
        cardSearchFilter = findViewById(R.id.cardSearchFilter);
    }

    private void setupClickListeners() {
        // Option 1: Open Partners (Bottom Navigation)
        cardOpenPartners.setOnClickListener(v -> {
            Intent intent = new Intent(PartnerFindingActivity.this, PartnerHomeActivity.class);
            intent.putExtra("source", "bottom_navigation");
            startActivity(intent);
        });

        // Option 2: Partners Home (Tabs)
        cardPartnersHome.setOnClickListener(v -> {
            Intent intent = new Intent(PartnerFindingActivity.this, PartnerHomeActivity.class);
            intent.putExtra("source", "home_tabs");
            startActivity(intent);
        });

        // Option 3: Search & Filter
        cardSearchFilter.setOnClickListener(v -> {
            Intent intent = new Intent(PartnerFindingActivity.this, PartnerSearchActivity.class);
            startActivity(intent);
        });
    }
}
