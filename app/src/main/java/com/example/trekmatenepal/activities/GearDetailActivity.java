package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class GearDetailActivity extends AppCompatActivity {

    private ImageView imgGear;
    private ImageView btnBack;

    private TextView txtGearName;
    private TextView txtPrice;
    private TextView txtRating;
    private TextView txtOwnerName;

    private Button btnRentNow;
    private Button btnMessageOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_detail);

        initializeViews();
        getDataFromIntent();
        setupClickListeners();
    }

    private void initializeViews() {
        imgGear = findViewById(R.id.imgGear);
        btnBack = findViewById(R.id.btnBack);

        txtGearName = findViewById(R.id.txtGearName);
        txtPrice = findViewById(R.id.txtPrice);
        txtRating = findViewById(R.id.txtRating);
        txtOwnerName = findViewById(R.id.txtOwnerName);

        btnRentNow = findViewById(R.id.btnRentNow);
        btnMessageOwner = findViewById(R.id.btnMessageOwner);
    }

    private void setupClickListeners() {

        btnBack.setOnClickListener(view -> finish());

        btnRentNow.setOnClickListener(view -> {
            Intent intent = new Intent(
                    GearDetailActivity.this,
                    BookingActivity.class
            );

            intent.putExtra(
                    "name",
                    txtGearName.getText().toString()
            );

            intent.putExtra(
                    "price",
                    txtPrice.getText().toString()
            );

            intent.putExtra(
                    "image",
                    getIntent().getIntExtra(
                            "image",
                            R.drawable.jacket
                    )
            );

            startActivity(intent);
        });

        btnMessageOwner.setOnClickListener(view -> {
            Intent intent = new Intent(
                    GearDetailActivity.this,
                    ChatActivity.class
            );

            intent.putExtra(
                    "chat_name",
                    txtOwnerName.getText().toString()
            );

            intent.putExtra(
                    "chat_subtitle",
                    "Gear Owner"
            );

            startActivity(intent);
        });
    }

    private void getDataFromIntent() {
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");

        int image = getIntent().getIntExtra(
                "image",
                R.drawable.jacket
        );

        if (name != null && !name.trim().isEmpty()) {
            txtGearName.setText(name);
        }

        if (price != null && !price.trim().isEmpty()) {
            txtPrice.setText(price);
        }

        imgGear.setImageResource(image);
    }
}