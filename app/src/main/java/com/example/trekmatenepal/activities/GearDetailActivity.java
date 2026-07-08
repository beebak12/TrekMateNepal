package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class GearDetailActivity extends AppCompatActivity {

    private ImageView imgGear, btnBack;
    private TextView txtGearName, txtPrice, txtRating;
    private Button btnRentNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_detail);

        initializeViews();
        getDataFromIntent();
        
        btnBack.setOnClickListener(v -> finish());
        btnRentNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("name", txtGearName.getText().toString());
            intent.putExtra("price", txtPrice.getText().toString());
            intent.putExtra("image", getIntent().getIntExtra("image", R.drawable.jacket));
            startActivity(intent);
        });
    }

    private void initializeViews() {
        imgGear = findViewById(R.id.imgGear);
        btnBack = findViewById(R.id.btnBack);
        txtGearName = findViewById(R.id.txtGearName);
        txtPrice = findViewById(R.id.txtPrice);
        txtRating = findViewById(R.id.txtRating);
        btnRentNow = findViewById(R.id.btnRentNow);
    }

    private void getDataFromIntent() {
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        int image = getIntent().getIntExtra("image", R.drawable.jacket);

        txtGearName.setText(name);
        txtPrice.setText(price);
        imgGear.setImageResource(image);
    }
}