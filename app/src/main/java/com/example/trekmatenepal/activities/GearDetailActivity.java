package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.RentalGearModel;

/**
 * GearDetailActivity — shows full details for a selected gear item.
 * Receives a RentalGearModel via Intent ("gear" key).
 * "Rent Now" opens BookingActivity with the same model.
 */
public class GearDetailActivity extends AppCompatActivity {

    private ImageView imgGear, btnBack;
    private TextView txtGearName, txtPrice, txtRating, txtAvailability,
            txtDescription, detailCategory, detailSize, detailCondition,
            detailSeller, detailLocation;
    private Button btnRentNow;

    private RentalGearModel gear;
    private int gearImageRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_detail);

        initializeViews();
        loadGearData();
        setupClickListeners();
    }

    private void initializeViews() {
        imgGear         = findViewById(R.id.imgGear);
        btnBack         = findViewById(R.id.btnBack);
        txtGearName     = findViewById(R.id.txtGearName);
        txtPrice        = findViewById(R.id.txtPrice);
        txtRating       = findViewById(R.id.txtRating);
        txtAvailability = findViewById(R.id.detailAvailability);
        txtDescription  = findViewById(R.id.txtDescription);
        detailCategory  = findViewById(R.id.detailCategory);
        detailSize      = findViewById(R.id.detailSize);
        detailCondition = findViewById(R.id.detailCondition);
        detailSeller    = findViewById(R.id.detailSeller);
        detailLocation  = findViewById(R.id.detailDelivery);    // reusing Delivery row for Location
        btnRentNow      = findViewById(R.id.btnRentNow);
    }

    private void loadGearData() {
        // Try Serializable (new flow from RentalGearAdapter)
        gear = (RentalGearModel) getIntent().getSerializableExtra("gear");

        if (gear == null) {
            // Fallback: legacy intent extras from old code paths
            String name  = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("price");
            int    image = getIntent().getIntExtra("image", R.drawable.jacket);
            String cat   = getIntent().getStringExtra("category");
            String avail = getIntent().getStringExtra("availability");
            gear = new RentalGearModel(
                    image,
                    name  != null ? name  : "Gear Item",
                    cat   != null ? cat   : "Trekking",
                    "4.5",
                    price != null ? price : "Rs. 0 / week",
                    avail != null ? avail : "Available",
                    "Kathmandu",
                    "High quality trekking gear suitable for Nepal trekking."
            );
        }

        // Populate UI
        if (gear.getCustomImageUri() != null) {
            imgGear.setImageURI(Uri.parse(gear.getCustomImageUri()));
            gearImageRes = 0; // URI based
        } else {
            gearImageRes = gear.getImage();
            if (isValidDrawable(gearImageRes)) {
                imgGear.setImageResource(gearImageRes);
            } else {
                gearImageRes = R.drawable.jacket;
                imgGear.setImageResource(gearImageRes);
            }
        }

        txtGearName.setText(gear.getName());
        txtPrice.setText(gear.getPrice());
        txtRating.setText("⭐ " + gear.getRating());

        boolean available = "Available".equalsIgnoreCase(gear.getAvailability());
        if (txtAvailability != null) {
            txtAvailability.setText(available ? "Available" : "Currently Unavailable");
            txtAvailability.setTextColor(ContextCompat.getColor(this,
                    available ? R.color.success_green : R.color.red));
        }

        if (txtDescription != null) txtDescription.setText(gear.getDescription());
        if (detailCategory  != null) detailCategory.setText(gear.getCategory());
        if (detailSize       != null) detailSize.setText(gear.getSize());
        if (detailCondition  != null) detailCondition.setText(gear.getCondition());
        if (detailSeller     != null) detailSeller.setText(gear.getSeller());
        if (detailLocation   != null) detailLocation.setText(gear.getLocation());

        // Update Author section if present in layout
        TextView txtAuthorName = findViewById(R.id.txtAuthorName);
        if (txtAuthorName != null) {
            txtAuthorName.setText(gear.getSeller());
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

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Tap the gear image → open it full screen
        imgGear.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullScreenImageActivity.class);
            intent.putExtra(FullScreenImageActivity.EXTRA_IMAGE_RES, gearImageRes);
            startActivity(intent);
        });

        btnRentNow.setOnClickListener(v -> {
            if (gear == null) return;
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("gear", gear);
            startActivity(intent);
        });

        // Favourite button (visual only for now)
        ImageView btnFav = findViewById(R.id.btnFav);
        if (btnFav != null) {
            btnFav.setOnClickListener(v ->
                    android.widget.Toast.makeText(this, "Added to favourites", android.widget.Toast.LENGTH_SHORT).show());
        }
    }
}
