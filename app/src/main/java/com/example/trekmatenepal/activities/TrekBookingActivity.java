package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class TrekBookingActivity extends AppCompatActivity {

    private ImageView imgTrekHero;
    private ImageButton btnBack;
    private TextView txtDifficultyTag, txtTrekNameHero, txtLocationHero;
    private TextView txtAltitude, txtDuration, txtDistance, txtRatingScore, txtAbout, txtTrekFee;
    private Button btnJoinTrek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trek_booking);

        initializeViews();
        getDataFromIntent();

        btnBack.setOnClickListener(v -> finish());
        
        btnJoinTrek.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("name", txtTrekNameHero.getText().toString());
            intent.putExtra("price", txtTrekFee.getText().toString());
            intent.putExtra("image", getIntent().getIntExtra("image", R.drawable.everest));
            startActivity(intent);
        });
    }

    private void initializeViews() {
        imgTrekHero = findViewById(R.id.imgTrekHero);
        btnBack = findViewById(R.id.btnBack);
        txtDifficultyTag = findViewById(R.id.txtDifficultyTag);
        txtTrekNameHero = findViewById(R.id.txtTrekNameHero);
        txtLocationHero = findViewById(R.id.txtLocationHero);
        txtAltitude = findViewById(R.id.txtAltitude);
        txtDuration = findViewById(R.id.txtDuration);
        txtDistance = findViewById(R.id.txtDistance);
        txtRatingScore = findViewById(R.id.txtRatingScore);
        txtAbout = findViewById(R.id.txtAbout);
        txtTrekFee = findViewById(R.id.txtTrekFee);
        btnJoinTrek = findViewById(R.id.btnJoinTrek);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String location = intent.getStringExtra("location");
            String duration = intent.getStringExtra("duration");
            String rating = intent.getStringExtra("rating");
            int reviews = intent.getIntExtra("reviews", 0);
            int image = intent.getIntExtra("image", R.drawable.everest);
            String difficulty = intent.getStringExtra("difficulty");
            String altitude = intent.getStringExtra("altitude");
            String distance = intent.getStringExtra("distance");
            String description = intent.getStringExtra("description");
            String fee = intent.getStringExtra("fee");

            txtTrekNameHero.setText(name);
            txtLocationHero.setText("📍 " + location);
            txtDuration.setText(duration);
            txtRatingScore.setText(rating + " (" + reviews + ")");
            imgTrekHero.setImageResource(image);
            txtDifficultyTag.setText(difficulty);
            txtAltitude.setText(altitude);
            txtDistance.setText(distance);
            txtAbout.setText(description);
            txtTrekFee.setText(fee);

            // Style difficulty tag
            if (difficulty != null) {
                if (difficulty.equalsIgnoreCase("Easy")) {
                    txtDifficultyTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE8F5E9));
                    txtDifficultyTag.setTextColor(0xFF4CAF50);
                } else if (difficulty.equalsIgnoreCase("Moderate")) {
                    txtDifficultyTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFF3E0));
                    txtDifficultyTag.setTextColor(0xFFFF9800);
                } else {
                    txtDifficultyTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFEBEE));
                    txtDifficultyTag.setTextColor(0xFFF44336);
                }
            }
        }
    }
}