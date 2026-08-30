package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.TrekModel;
import com.google.android.material.tabs.TabLayout;

/**
 * TrekPackageDetailsActivity — displays complete trek package details with
 * tabs for Overview, Itinerary, Inclusions, and Exclusions.
 * Receives: trek (TrekModel) via intent extra
 */
public class TrekPackageDetailsActivity extends AppCompatActivity {

    private TrekModel selectedTrek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trek_package_details);

        initializeViews();
        getDataFromIntent();
        setupTabListeners();
        setupButtonListeners();
    }

    private void initializeViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView btnMore = findViewById(R.id.btnMore);
        if (btnMore != null) {
            btnMore.setOnClickListener(v -> {
                // Show options menu or share
            });
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedTrek = (TrekModel) intent.getSerializableExtra("trek");
            if (selectedTrek != null) {
                populateUI();
            }
        }
    }

    private void populateUI() {
        // Set hero image
        ImageView imgTrekHero = findViewById(R.id.imgTrekHero);
        if (imgTrekHero != null) {
            int imageRes = selectedTrek.getImage();
            if (isValidDrawable(imageRes)) {
                imgTrekHero.setImageResource(imageRes);
            } else {
                imgTrekHero.setImageResource(R.drawable.everest);
            }
        }

        // Set text fields
        TextView tvTrekName = findViewById(R.id.tvTrekName);
        if (tvTrekName != null) {
            tvTrekName.setText(selectedTrek.getTrekName());
        }

        TextView tvDuration = findViewById(R.id.tvDuration);
        if (tvDuration != null) {
            tvDuration.setText(selectedTrek.getDuration());
        }

        TextView tvRating = findViewById(R.id.tvRating);
        if (tvRating != null) {
            tvRating.setText(selectedTrek.getRating());
        }

        TextView tvReviews = findViewById(R.id.tvReviews);
        if (tvReviews != null) {
            tvReviews.setText("(" + selectedTrek.getReviews() + " reviews)");
        }

        TextView tvPrice = findViewById(R.id.tvPrice);
        if (tvPrice != null) {
            tvPrice.setText(selectedTrek.getFee());
        }

        TextView tvOverview = findViewById(R.id.tvOverview);
        if (tvOverview != null) {
            String overview = selectedTrek.getDescription() + "\n\n"
                    + "• Stunning mountain views\n"
                    + "• Traditional Sherpa culture\n"
                    + "• Best trekking experience\n"
                    + "• Professional guides included\n"
                    + "• Safe and secure journey\n"
                    + "• High success rate";
            tvOverview.setText(overview);
        }
    }

    private boolean isValidDrawable(int resourceId) {
        if (resourceId <= 0) return false;
        try {
            String type = getResources().getResourceTypeName(resourceId);
            return "drawable".equals(type) || "mipmap".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    private void setupTabListeners() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    updateTabContent(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void updateTabContent(int tabPosition) {
        TextView tvOverview = findViewById(R.id.tvOverview);
        if (tvOverview == null || selectedTrek == null) return;

        switch (tabPosition) {
            case 0: // Overview
                tvOverview.setText(selectedTrek.getDescription() + "\n\n"
                        + "• Stunning mountain views\n"
                        + "• Traditional Sherpa culture\n"
                        + "• Best trekking experience\n"
                        + "• Professional guides included\n"
                        + "• Safe and secure journey\n"
                        + "• High success rate");
                break;

            case 1: // Itinerary
                String itinerary = "Day 1: Arrive in Kathmandu\n"
                        + "Day 2: Kathmandu to Phakding (2,610m)\n"
                        + "Day 3: Phakding to Namche Bazaar (3,440m)\n"
                        + "Day 4: Acclimatization day in Namche Bazaar\n"
                        + "Day 5: Namche Bazaar to Tengboche (3,867m)\n"
                        + "Day 6: Tengboche to Dingboche (4,410m)\n"
                        + "Day 7: Acclimatization day in Dingboche\n"
                        + "Day 8: Dingboche to Lobuche (4,940m)\n"
                        + "Day 9: Lobuche to Everest Base Camp (5,364m)\n"
                        + "Day 10: EBC to Kala Patthar and return to Gorak Shep\n"
                        + "Day 11: Descend to Namche Bazaar\n"
                        + "Day 12: Namche Bazaar to Kathmandu";
                tvOverview.setText(itinerary);
                break;

            case 2: // Inclusions
                String inclusions = "✓ Professional licensed guide\n"
                        + "✓ Accommodation in teahouses\n"
                        + "✓ Meals (breakfast, lunch, dinner)\n"
                        + "✓ All transportation\n"
                        + "✓ First aid kit\n"
                        + "✓ Route map\n"
                        + "✓ Certificate upon completion\n"
                        + "✓ 24/7 emergency support";
                tvOverview.setText(inclusions);
                break;

            case 3: // Exclusions
                String exclusions = "✗ International flights\n"
                        + "✗ Visa fees\n"
                        + "✗ Travel insurance (recommended)\n"
                        + "✗ Personal trekking gear\n"
                        + "✗ Mineral water bottles\n"
                        + "✗ Tips for guides (discretionary)\n"
                        + "✗ Meals in Kathmandu\n"
                        + "✗ Personal medications";
                tvOverview.setText(exclusions);
                break;
        }
    }

    private void setupButtonListeners() {
        Button btnSave = findViewById(R.id.btnSave);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                // Save to favorites/bookmarks
            });
        }

        Button btnBookGuide = findViewById(R.id.btnBookGuide);
        if (btnBookGuide != null) {
            btnBookGuide.setOnClickListener(v -> {
                // Navigate to AvailableGuidesActivity
                Intent intent = new Intent(this, AvailableGuidesActivity.class);
                if (selectedTrek != null) {
                    intent.putExtra("trek", selectedTrek);
                }
                startActivity(intent);
            });
        }
    }
}
