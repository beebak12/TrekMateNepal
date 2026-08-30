package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.card.MaterialCardView;

public class RoleSelectionActivity extends AppCompatActivity {

    private MaterialCardView cardTrekker, cardGuide;
    private Button btnTrekker, btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        cardTrekker = findViewById(R.id.cardTrekker);
        cardGuide = findViewById(R.id.cardGuide);
        btnTrekker = findViewById(R.id.btnTrekker);
        btnGuide = findViewById(R.id.btnGuide);

        btnTrekker.setOnClickListener(v -> {
            saveRole("TREKKER");
            startActivity(new Intent(RoleSelectionActivity.this, DashboardActivity.class));
            finish();
        });

        btnGuide.setOnClickListener(v -> {
            saveRole("GUIDE");
            startActivity(new Intent(RoleSelectionActivity.this, GuideDashboardActivity.class));
            finish();
        });

        cardTrekker.setOnClickListener(v -> btnTrekker.performClick());
        cardGuide.setOnClickListener(v -> btnGuide.performClick());
    }

    private void saveRole(String role) {
        SharedPreferences prefs = getSharedPreferences("TrekMatePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_role", role);
        editor.apply();
    }
}