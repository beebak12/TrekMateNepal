package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GuideAdapter;
import com.example.trekmatenepal.models.GuideModel;
import com.example.trekmatenepal.models.TrekModel;

import java.util.ArrayList;

/**
 * AvailableGuidesActivity — shows guides available for a selected trek.
 * Receives: trek (TrekModel) via intent extra
 */
public class AvailableGuidesActivity extends AppCompatActivity {

    private TrekModel selectedTrek;
    private ArrayList<GuideModel> guideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_guides);

        initializeViews();
        getDataFromIntent();
        loadGuides();
        setupRecyclerView();
    }

    private void initializeViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedTrek = (TrekModel) intent.getSerializableExtra("trek");
        }

        // Update header with trek name if available
        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
        if (tvSubtitle != null && selectedTrek != null) {
            tvSubtitle.setText("Guides for " + selectedTrek.getTrekName());
        }
    }

    private void loadGuides() {
        guideList = new ArrayList<>();

        // Sample guide data matching the design
        guideList.add(new GuideModel(
                "guide_001",
                "Pemba Sherpa",
                "Licensed Guide",
                "8+ Years",
                "English, Nepali, Sherpa",
                "4.9",
                120,
                "Rs. 2,500/day",
                R.drawable.partner1,
                "Government licensed guide with 8 years of experience in Everest region and other major treks.",
                "Everest Region,High Altitude,Safety First",
                R.drawable.everest,
                true,
                "98%",
                120
        ));

        guideList.add(new GuideModel(
                "guide_002",
                "Nima Tamang",
                "Licensed Guide",
                "6+ Years",
                "English, Nepali",
                "4.8",
                95,
                "Rs. 2,200/day",
                R.drawable.partner2,
                "Experienced guide specializing in Annapurna treks with excellent customer reviews.",
                "Annapurna,Multi-day Treks",
                R.drawable.annapurna,
                true,
                "96%",
                95
        ));

        guideList.add(new GuideModel(
                "guide_003",
                "Dawa Sherpa",
                "Licensed Guide",
                "10+ Years",
                "English, Nepali",
                "4.9",
                156,
                "Rs. 2,800/day",
                R.drawable.partner3,
                "Senior guide with extensive knowledge of high-altitude trekking and mountaineering safety.",
                "High Altitude,Everest Region,Mountaineering",
                R.drawable.everest,
                true,
                "99%",
                156
        ));

        guideList.add(new GuideModel(
                "guide_004",
                "Ang Temba Sherpa",
                "Licensed Guide",
                "5+ Years",
                "English, Nepali",
                "4.7",
                78,
                "Rs. 2,000/day",
                R.drawable.partner4,
                "Friendly and knowledgeable guide perfect for first-time trekkers.",
                "Beginner Treks,Langtang,Safety",
                R.drawable.langtang,
                true,
                "94%",
                78
        ));

        // Update guide count
        TextView tvGuideCount = findViewById(R.id.tvGuideCount);
        if (tvGuideCount != null) {
            tvGuideCount.setText(guideList.size() + " Guides Found");
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerGuides = findViewById(R.id.recyclerGuides);
        if (recyclerGuides != null) {
            recyclerGuides.setLayoutManager(new LinearLayoutManager(this));
            GuideAdapter adapter = new GuideAdapter(guideList, selectedTrek);
            recyclerGuides.setAdapter(adapter);
        }
    }
}
