package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText etCurrent, etNew, etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setupHeader();
        initViews();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Change Password");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        etCurrent = findViewById(R.id.etCurrentPassword);
        etNew = findViewById(R.id.etNewPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);

        findViewById(R.id.btnChangePassword).setOnClickListener(v -> validateAndChange());
    }

    private void validateAndChange() {
        String current = etCurrent.getText().toString();
        String newPass = etNew.getText().toString();
        String confirm = etConfirm.getText().toString();

        if (TextUtils.isEmpty(current)) {
            etCurrent.setError("Current password required");
            return;
        }
        if (newPass.length() < 6) {
            etNew.setError("Password must be at least 6 characters");
            return;
        }
        if (!newPass.equals(confirm)) {
            etConfirm.setError("Passwords do not match");
            return;
        }

        // Simulating API call
        Toast.makeText(this, "Password updated locally", Toast.LENGTH_SHORT).show();
        finish();
    }
}
