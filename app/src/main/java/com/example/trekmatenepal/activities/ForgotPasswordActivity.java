package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.network.NetworkHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private MaterialCardView cardGmail, cardPhone;
    private MaterialButton btnBackToLogin;
    private ImageView btnBack;
    private TextInputLayout tilIdentifier;
    private TextInputEditText etIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        cardGmail = findViewById(R.id.cardGmail);
        cardPhone = findViewById(R.id.cardPhone);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        btnBack = findViewById(R.id.btnBack);
        tilIdentifier = findViewById(R.id.tilIdentifier);
        etIdentifier = findViewById(R.id.etIdentifier);
    }

    private void setupClickListeners() {
        cardGmail.setOnClickListener(v -> handleRequest("email"));
        cardPhone.setOnClickListener(v -> handleRequest("phone"));
        btnBackToLogin.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
    }

    private void handleRequest(String method) {
        String identifier = etIdentifier.getText().toString().trim();
        if (identifier.isEmpty()) {
            tilIdentifier.setError("Please enter your email or phone");
            return;
        }

        if (method.equals("email") && !Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            tilIdentifier.setError("Invalid email format");
            return;
        }

        if (method.equals("phone") && identifier.length() < 10) {
            tilIdentifier.setError("Invalid phone number");
            return;
        }

        tilIdentifier.setError(null);
        requestOtp(method, identifier);
    }

    private void requestOtp(String method, String identifier) {
        try {
            JSONObject body = new JSONObject();
            if (method.equals("email")) body.put("email", identifier);
            else body.put("phone", identifier);

            NetworkHelper.post("api/auth/forgot-password/request", body, new NetworkHelper.Callback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Intent intent;
                    if (method.equals("email")) {
                        intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordEmailActivity.class);
                        intent.putExtra("email", identifier);
                    } else {
                        intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordPhoneActivity.class);
                        intent.putExtra("phone", identifier);
                    }
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ForgotPasswordActivity.this, "Request failed: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
