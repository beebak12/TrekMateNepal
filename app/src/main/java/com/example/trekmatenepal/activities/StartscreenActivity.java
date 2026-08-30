package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class StartscreenActivity extends AppCompatActivity {

    Button btnGetStarted, btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnSignin = findViewById(R.id.btnSignin);

        // ==========================================
        // GET STARTED BUTTON
        // ==========================================

        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(StartscreenActivity.this, LoginActivity.class));
        });

        // ==========================================
        // SIGN UP BUTTON
        // ==========================================

        btnSignin.setOnClickListener(v -> {
            startActivity(new Intent(StartscreenActivity.this, SignupActivity.class));
        });
    }
}
