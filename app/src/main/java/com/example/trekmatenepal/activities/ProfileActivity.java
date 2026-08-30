package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.database.DatabaseHelpher;
import com.example.trekmatenepal.models.UserModel;

public class ProfileActivity extends AppCompatActivity {

    private View layoutTreks, layoutEdit, layoutGear, layoutPosts, layoutTreksActivity;
    private TextView tvProfileName, tvProfileLocation, tvTrekCountSummary, tvProfileAge, tvProfileGender, tvProfileBio;
    private ImageView profileImage;
    private DatabaseHelpher dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelpher(this);
        initializeViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void initializeViews() {
        layoutTreks = findViewById(R.id.layoutTreks);
        layoutEdit = findViewById(R.id.layoutEdit);
        layoutGear = findViewById(R.id.layoutGear);
        layoutPosts = findViewById(R.id.layoutPosts);
        layoutTreksActivity = findViewById(R.id.layoutTreksActivity);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileLocation = findViewById(R.id.tvProfileLocation);
        tvTrekCountSummary = findViewById(R.id.tvTrekCountSummary);
        tvProfileAge = findViewById(R.id.tvProfileAge);
        tvProfileGender = findViewById(R.id.tvProfileGender);
        tvProfileBio = findViewById(R.id.tvProfileBio);
        profileImage = findViewById(R.id.profileImage);
    }

    private void loadUserData() {
        UserModel user = dbHelper.getUserProfile();
        if (user != null) {
            tvProfileName.setText(user.getFullName());
            tvProfileLocation.setText(user.getLocation());
            tvTrekCountSummary.setText(String.valueOf(user.getTrekCount()));
            tvProfileAge.setText(user.getAge());
            tvProfileGender.setText(user.getGender());
            tvProfileBio.setText(user.getBio());

            if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                profileImage.setImageURI(Uri.parse(user.getImagePath()));
            }
        }
    }

    private void setupListeners() {
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnNotification = findViewById(R.id.btnNotification);
        ImageView btnSettings = findViewById(R.id.btnSettings);

        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        layoutTreks.setOnClickListener(v -> startActivity(new Intent(this, TreksCompletedActivity.class)));
        layoutEdit.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        layoutGear.setOnClickListener(v -> startActivity(new Intent(this, PostedGearActivity.class)));
        layoutPosts.setOnClickListener(v -> startActivity(new Intent(this, PostedGearActivity.class)));
        layoutTreksActivity.setOnClickListener(v -> startActivity(new Intent(this, TreksCompletedActivity.class)));

        btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}
