package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.SessionUser;

public class AccountInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        setupHeader();
        loadUserInfo();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        TextView title = header.findViewById(R.id.txtHeaderTitle);
        title.setText("Account Information");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        String userId = SessionUser.getUserId(this);

        setupInfoRow(R.id.rowName, "Full Name", "Bibek Paudel");
        setupInfoRow(R.id.rowUsername, "Username", userId);
        setupInfoRow(R.id.rowEmail, "Email", userId + "@gmail.com");
        setupInfoRow(R.id.rowPhone, "Contact Number", "+977 9812345678");
        setupInfoRow(R.id.rowDob, "Date of Birth", "1998-05-15");
        setupInfoRow(R.id.rowGender, "Gender", "Male");
    }

    private void setupInfoRow(int viewId, String label, String value) {
        View row = findViewById(viewId);
        ((TextView) row.findViewById(R.id.txtInfoLabel)).setText(label);
        ((TextView) row.findViewById(R.id.txtInfoValue)).setText(value);
    }
}
