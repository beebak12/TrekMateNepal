package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class AddPackageActivity extends AppCompatActivity {

    private EditText edtPackageName;
    private EditText edtLocation;
    private EditText edtDuration;
    private EditText edtPrice;
    private EditText edtDescription;

    private Button btnSavePackage;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_package);

        edtPackageName = findViewById(R.id.edtPackageName);
        edtLocation = findViewById(R.id.edtLocation);
        edtDuration = findViewById(R.id.edtDuration);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);

        btnSavePackage = findViewById(R.id.btnSavePackage);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnSavePackage.setOnClickListener(v -> savePackage());
    }

    private void savePackage() {

        String name = edtPackageName.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String duration = edtDuration.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (name.isEmpty()) {
            edtPackageName.setError("Enter package name");
            edtPackageName.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            edtLocation.setError("Enter location");
            edtLocation.requestFocus();
            return;
        }

        if (duration.isEmpty()) {
            edtDuration.setError("Enter duration");
            edtDuration.requestFocus();
            return;
        }

        if (price.isEmpty()) {
            edtPrice.setError("Enter price");
            edtPrice.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            edtDescription.setError("Enter description");
            edtDescription.requestFocus();
            return;
        }

        Toast.makeText(
                this,
                "Package saved successfully",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}