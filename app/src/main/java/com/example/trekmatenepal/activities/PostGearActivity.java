package com.example.trekmatenepal.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.GearRepository;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.RentalGearModel;

/**
 * PostGearActivity — lets a user list their gear for rental.
 */
public class PostGearActivity extends AppCompatActivity {

    private EditText etName, etCategory, etSize, etPrice, etDescription, etLocation;
    private FrameLayout layoutSelectImage;
    private ImageView imgPreview;
    private LinearLayout layoutAddIcon;
    private Button btnPost;

    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgPreview.setImageURI(uri);
                    imgPreview.setVisibility(View.VISIBLE);
                    layoutAddIcon.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_gear);

        initViews();
        setupListeners();
    }

    private void initViews() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        etName = findViewById(R.id.etGearName);
        etCategory = findViewById(R.id.etCategory);
        etSize = findViewById(R.id.etSize);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        
        layoutSelectImage = findViewById(R.id.layoutSelectImage);
        imgPreview = findViewById(R.id.imgPreview);
        layoutAddIcon = findViewById(R.id.layoutAddIcon);
        btnPost = findViewById(R.id.btnPostGear);
    }

    private void setupListeners() {
        layoutSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnPost.setOnClickListener(v -> handlePostGear());
    }

    private void handlePostGear() {
        String name = text(etName);
        String category = text(etCategory);
        String size = text(etSize);
        String priceNum = text(etPrice);
        String description = text(etDescription);
        String location = text(etLocation);

        if (TextUtils.isEmpty(name)) { etName.setError("Required"); return; }
        if (TextUtils.isEmpty(category)) { etCategory.setError("Required"); return; }
        if (TextUtils.isEmpty(priceNum)) { etPrice.setError("Required"); return; }

        String currentUserId = SessionUser.getUserId(this);
        String sellerName = "Bibek Paudel"; // Should ideally come from User Profile Repo
        
        String priceDisplay = "Rs. " + priceNum + " / week";

        RentalGearModel gear = new RentalGearModel(
                0, // No default resource
                selectedImageUri != null ? selectedImageUri.toString() : null,
                name,
                category,
                "New",
                priceDisplay,
                priceNum,
                "Available",
                location,
                description,
                size,
                "Good",
                sellerName,
                currentUserId
        );

        GearRepository.addGear(this, gear);
        NotificationRepository.notifyGearListed(this, currentUserId, name);
        
        Toast.makeText(this, "Gear posted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String text(EditText e) {
        return e == null ? "" : e.getText().toString().trim();
    }
}
