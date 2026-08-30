package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class GearDetailsActivity extends AppCompatActivity {

    ImageView detailImage, btnBack;
    TextView detailName, detailPrice, detailStatus, detailLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_details);

        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailPrice = findViewById(R.id.detailPrice);
        detailStatus = findViewById(R.id.detailStatus);
        detailLocation = findViewById(R.id.detailLocation);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String status = getIntent().getStringExtra("status");
        String location = getIntent().getStringExtra("location");
        int image = getIntent().getIntExtra("image", 0);

        detailName.setText(name);
        detailPrice.setText(price);
        detailStatus.setText(status);
        detailLocation.setText("📍 " + location);

        if (image != 0) {
            detailImage.setImageResource(image);
        }
    }
}