package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.network.NetworkHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilPassword, tilConfirmPassword;
    private TextInputEditText etPassword, etConfirmPassword;
    private MaterialButton btnSave;
    private TextView tvReq1, tvReq2, tvReq3;
    private ImageView btnBack;
    private String resetToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetToken = getIntent().getStringExtra("resetToken");

        initializeViews();
        setupTextWatchers();
        setupClickListeners();
    }

    private void initializeViews() {
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSavePassword);
        tvReq1 = findViewById(R.id.tvRequirement1);
        tvReq2 = findViewById(R.id.tvRequirement2);
        tvReq3 = findViewById(R.id.tvRequirement3);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupTextWatchers() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void validatePassword(String password) {
        boolean hasMinLen = password.length() >= 8;
        boolean hasUpperLower = password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
        boolean hasNumSpecial = password.matches(".*[0-9].*") || password.matches(".*[!@#$%^&*].*");

        updateRequirementUI(tvReq1, hasMinLen, "At least 8 characters");
        updateRequirementUI(tvReq2, hasUpperLower, "Include uppercase & lowercase");
        updateRequirementUI(tvReq3, hasNumSpecial, "Include number or special character");

        btnSave.setEnabled(hasMinLen && hasUpperLower && hasNumSpecial);
    }

    private void updateRequirementUI(TextView tv, boolean isValid, String text) {
        if (isValid) {
            tv.setText("✓ " + text);
            tv.setTextColor(getResources().getColor(R.color.forgot_success));
        } else {
            tv.setText(text);
            tv.setTextColor(getResources().getColor(R.color.forgot_secondary_text));
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String pass = etPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();

        if (!pass.equals(confirm)) {
            tilConfirmPassword.setError("Passwords do not match");
            return;
        }

        btnSave.setEnabled(false);
        try {
            JSONObject body = new JSONObject();
            body.put("resetToken", resetToken);
            body.put("newPassword", pass);

            NetworkHelper.post("api/auth/forgot-password/reset", body, new NetworkHelper.Callback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Intent intent = new Intent(ResetPasswordActivity.this, PasswordUpdatedActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    btnSave.setEnabled(true);
                    Toast.makeText(ResetPasswordActivity.this, "Failed to reset password: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            btnSave.setEnabled(true);
        }
    }
}
