package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.ProfileResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        showValues("Loading…", SessionUser.getUserId(this), "Loading…", "Loading…", "Loading…", "Loading…", "Loading…");
        String token = SessionUser.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            showValues("—", SessionUser.getUserId(this), "—", "—", "—", "—", "—");
            return;
        }
        ApiClient.getClient().create(ApiService.class).getProfile("Bearer " + token)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess() && response.body().getData() != null) {
                            ProfileResponse.UserData user = response.body().getData();
                            showValues(value(user.getFullName()), value(user.getUsername()), value(user.getEmail()),
                                    value(user.getPhone()), formatDob(user.getDob()), formatGender(user.getGender()),
                                    formatAddress(user.getCity(), user.getCountry()));
                        } else {
                            showValues("—", SessionUser.getUserId(AccountInformationActivity.this),
                                    "Unable to load", "—", "—", "—", "—");
                        }
                    }

                    @Override public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        showValues("—", SessionUser.getUserId(AccountInformationActivity.this),
                                "Cannot connect to server", "—", "—", "—", "—");
                    }
                });
    }

    private void showValues(String name, String username, String email, String phone, String dob,
                            String gender, String address) {
        setupInfoRow(R.id.rowName, "Full Name", name);
        setupInfoRow(R.id.rowUsername, "Username", username);
        setupInfoRow(R.id.rowEmail, "Email", email);
        setupInfoRow(R.id.rowPhone, "Contact Number", phone);
        setupInfoRow(R.id.rowDob, "Date of Birth", dob);
        setupInfoRow(R.id.rowGender, "Gender", gender);
        setupInfoRow(R.id.rowAddress, "Address", address);
    }

    private String value(String text) {
        return text == null || text.trim().isEmpty() ? "—" : text.trim();
    }

    private String formatDob(String dob) {
        if (dob == null || dob.trim().isEmpty()) return "—";
        try {
            String raw = dob.length() >= 10 ? dob.substring(0, 10) : dob;
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            input.setLenient(false);
            Date date = input.parse(raw);
            return date == null ? value(dob) : new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date);
        } catch (Exception ignored) {
            return value(dob);
        }
    }

    private String formatGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) return "—";
        if ("PREFER_NOT_TO_SAY".equalsIgnoreCase(gender)) return "Prefer not to say";
        String lower = gender.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private String formatAddress(String city, String country) {
        boolean hasCity = city != null && !city.trim().isEmpty();
        boolean hasCountry = country != null && !country.trim().isEmpty();
        if (hasCity && hasCountry) return city.trim() + ", " + country.trim();
        if (hasCity) return city.trim();
        if (hasCountry) return country.trim();
        return "—";
    }

    private void setupInfoRow(int viewId, String label, String value) {
        View row = findViewById(viewId);
        ((TextView) row.findViewById(R.id.txtInfoLabel)).setText(label);
        ((TextView) row.findViewById(R.id.txtInfoValue)).setText(value);
    }
}
