package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class PaymentActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnBack = findViewById(R.id.btnBack);
        btnPay = findViewById(R.id.btnPay);

        btnBack.setOnClickListener(v -> finish());
        btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingSuccessActivity.class);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("price", getIntent().getStringExtra("price"));
            intent.putExtra("image", getIntent().getIntExtra("image", R.drawable.jacket));
            startActivity(intent);
        });
    }
}