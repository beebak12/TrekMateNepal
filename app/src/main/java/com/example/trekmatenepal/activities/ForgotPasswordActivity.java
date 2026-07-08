package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnReset = findViewById(R.id.btnReset);

        btnReset.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Password Reset Link Sent",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}