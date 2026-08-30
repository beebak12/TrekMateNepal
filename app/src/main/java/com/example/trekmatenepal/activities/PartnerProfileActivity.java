package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartnerProfileActivity extends AppCompatActivity {

    private ImageView ivBackButton, ivProfileImage, ivVerified;
    private TextView tvPartnerName, tvRating, tvLocation, tvExperienceValue, tvSpotsValue, 
                    tvCostValue, tvBio;
    private LinearLayout llExpertise;
    private RecyclerView rvTreks;
    private MaterialButton btnCall, btnMessage, btnSendRequest;
    private PartnerModel partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile);

        initializeViews();
        getPartnerData();
        if (partner != null) {
            displayPartnerInfo();
            setupClickListeners();
        }
    }

    private void initializeViews() {
        ivBackButton = findViewById(R.id.ivBackButton);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivVerified = findViewById(R.id.ivVerified);
        tvPartnerName = findViewById(R.id.tvPartnerName);
        tvRating = findViewById(R.id.tvRating);
        tvLocation = findViewById(R.id.tvLocation);
        tvExperienceValue = findViewById(R.id.tvExperienceValue);
        tvSpotsValue = findViewById(R.id.tvSpotsValue);
        tvCostValue = findViewById(R.id.tvCostValue);
        tvBio = findViewById(R.id.tvBio);
        llExpertise = findViewById(R.id.llExpertise);
        rvTreks = findViewById(R.id.rvTreks);
        btnCall = findViewById(R.id.btnCall);
        btnMessage = findViewById(R.id.btnMessage);
        btnSendRequest = findViewById(R.id.btnSendRequest);
    }

    private void getPartnerData() {
        Intent intent = getIntent();
        if (intent != null) {
            try {
                partner = (PartnerModel) intent.getSerializableExtra("partner");
            } catch (ClassCastException e) {
                // Handle legacy Partner objects if any
                partner = null;
            }
        }
    }

    private void displayPartnerInfo() {
        // Display basic info
        tvPartnerName.setText(partner.getName());
        tvRating.setText(String.format("%s %s", partner.getRating(), partner.getReviews()));
        tvLocation.setText(partner.getBaseLocation());
        tvExperienceValue.setText(String.valueOf(partner.getYearsOfExperience()));
        tvSpotsValue.setText(String.valueOf(partner.getSpotsAvailable()));
        tvCostValue.setText(String.format("%.1f", partner.getCostPerDay()));
        tvBio.setText(partner.getAbout());

        // Safety check for profile image
        int profileImageRes = partner.getImage();
        if (isValidDrawable(profileImageRes)) {
            ivProfileImage.setImageResource(profileImageRes);
        } else {
            ivProfileImage.setImageResource(R.drawable.ic_person);
        }

        // Show verified badge if verified
        if (partner.isVerified()) {
            ivVerified.setVisibility(ImageView.VISIBLE);
        }

        // Display expertise tags
        if (partner.getInterests() != null && !partner.getInterests().isEmpty()) {
            List<String> expertise = Arrays.asList(partner.getInterests().split(","));
            displayExpertiseTags(expertise);
        }

        // Display treks
        if (partner.getTreks() != null && !partner.getTreks().isEmpty()) {
            setupTreksRecyclerView(Arrays.asList(partner.getTreks().split(",")));
        }
    }

    private void displayExpertiseTags(List<String> expertise) {
        llExpertise.removeAllViews();
        
        for (String exp : expertise) {
            LinearLayout tagLayout = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
            tagLayout.setLayoutParams(lp);
            tagLayout.setBackground(getDrawable(R.drawable.bg_badge));
            tagLayout.setPadding(24, 12, 24, 12);

            TextView tagText = new TextView(this);
            tagText.setText(exp);
            tagText.setTextColor(ContextCompat.getColor(this, R.color.purple_primary));
            tagText.setTextSize(11);

            tagLayout.addView(tagText);
            llExpertise.addView(tagLayout);
        }
    }

    private void setupTreksRecyclerView(List<String> treks) {
        // TODO: Create TrekAdapter to display treks
        // For now, just setup empty recyclerview
        rvTreks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupClickListeners() {
        ivBackButton.setOnClickListener(v -> finish());

        btnCall.setOnClickListener(v -> {
            if (partner.getPhone() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + partner.getPhone()));
                startActivity(dialIntent);
            }
        });

        btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("partnerName", partner.getName());
            intent.putExtra("partnerImage", partner.getImage());
            startActivity(intent);
        });

        btnSendRequest.setOnClickListener(v -> {
            Intent intent = new Intent(PartnerProfileActivity.this, SendJoinRequestActivity.class);
            intent.putExtra("partner", partner);
            startActivity(intent);
        });
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
}
