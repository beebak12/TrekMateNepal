package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class BookingActivity extends AppCompatActivity {

    private ImageView btnBack, imgGear;
    private TextView txtGearName, txtGearPrice;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initializeViews();
        getDataFromIntent();

        btnBack.setOnClickListener(v -> finish());
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("name", txtGearName.getText().toString());
            intent.putExtra("price", txtGearPrice.getText().toString());
            intent.putExtra("image", getIntent().getIntExtra("image", R.drawable.jacket));
            startActivity(intent);
        });
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        imgGear = findViewById(R.id.imgGear);
        txtGearName = findViewById(R.id.txtGearName);
        txtGearPrice = findViewById(R.id.txtGearPrice);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void getDataFromIntent() {
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        int image = getIntent().getIntExtra("image", R.drawable.jacket);

        txtGearName.setText(name);
        txtGearPrice.setText(price);
        imgGear.setImageResource(image);
    }
}