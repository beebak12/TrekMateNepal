package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class GuideAddPackageActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputEditText etPackageName, etDestination, etDuration, etPrice, etDescription;
    private MaterialButton btnCreatePackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_add_package);

        initializeViews();

        btnBack.setOnClickListener(v -> finish());
        btnCreatePackage.setOnClickListener(v -> createPackage());
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        etPackageName = findViewById(R.id.etPackageName);
        etDestination = findViewById(R.id.etDestination);
        etDuration = findViewById(R.id.etDuration);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnCreatePackage = findViewById(R.id.btnCreatePackage);
    }

    private void createPackage() {
        if (etPackageName.getText().toString().isEmpty() ||
            etDestination.getText().toString().isEmpty() ||
            etDuration.getText().toString().isEmpty() ||
            etPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mock success
        Toast.makeText(this, "Package created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}