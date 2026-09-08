package com.example.trekmatenepal.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.ProfileResponse;
import com.example.trekmatenepal.model.UpdateProfileRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuideEditProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnSaveTop;
    private MaterialButton btnSaveChanges;
    private LinearLayout editProfileTopBar;

    private TextInputEditText etFullName, etProfessionalTitle, etPhone, etEmail, etDob;
    private AutoCompleteTextView actvGender;
    private TextInputEditText etExperience, etLanguages, etTotalTreks, etLicenseNumber, etIssueDate, etExpiryDate;
    private TextInputEditText etAboutMe;
    private TextInputEditText etFacebook, etInstagram, etWebsite;

    private ApiService apiService;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-edge support
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_guide_edit_profile);

        apiService = ApiClient.getClient().create(ApiService.class);

        initializeViews();
        setupSystemBars();
        setupGenderDropdown();
        setupDatePickers();
        setupClickListeners();

        loadProfileData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSaveTop = findViewById(R.id.btnSave);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        editProfileTopBar = findViewById(R.id.editProfileTopBar);

        etFullName = findViewById(R.id.etFullName);
        etProfessionalTitle = findViewById(R.id.etProfessionalTitle);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        actvGender = findViewById(R.id.actvGender);

        etExperience = findViewById(R.id.etExperience);
        etLanguages = findViewById(R.id.etLanguages);
        etTotalTreks = findViewById(R.id.etTotalTreks);
        etLicenseNumber = findViewById(R.id.etLicenseNumber);
        etIssueDate = findViewById(R.id.etIssueDate);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        etAboutMe = findViewById(R.id.etAboutMe);

        etFacebook = findViewById(R.id.etFacebook);
        etInstagram = findViewById(R.id.etInstagram);
        etWebsite = findViewById(R.id.etWebsite);
    }

    private void setupSystemBars() {
        View root = findViewById(R.id.editProfileRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int statusBarHeight = systemBars.top;
            int toolbarHeight = dpToPx(56);

            ViewGroup.LayoutParams params = editProfileTopBar.getLayoutParams();
            params.height = statusBarHeight + toolbarHeight;
            editProfileTopBar.setLayoutParams(params);

            editProfileTopBar.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            editProfileTopBar.setPadding(dpToPx(8), 0, dpToPx(8), dpToPx(4));

            return insets;
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        actvGender.setAdapter(adapter);
    }

    private void setupDatePickers() {
        etDob.setOnClickListener(v -> showDatePicker(etDob));
        etIssueDate.setOnClickListener(v -> showDatePicker(etIssueDate));
        etExpiryDate.setOnClickListener(v -> showDatePicker(etExpiryDate));
    }

    private void showDatePicker(TextInputEditText editText) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            editText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSaveTop.setOnClickListener(v -> validateAndSave());
        btnSaveChanges.setOnClickListener(v -> validateAndSave());

        findViewById(R.id.fabChangePhoto).setOnClickListener(v ->
                Toast.makeText(this, "Change profile photo", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadProfileData() {
        String token = SessionUser.getToken(this);
        if (token == null) return;

        apiService.getProfile("Bearer " + token).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    bindData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(GuideEditProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindData(ProfileResponse.UserData user) {
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etDob.setText(user.getDob());
        actvGender.setText(user.getGender(), false);
        etAboutMe.setText(user.getBio());

        // Placeholders for non-API fields
        etProfessionalTitle.setText("Professional Trekking Guide");
        etExperience.setText("8+ Years");
        etLanguages.setText("English, Nepali, Hindi");
        etTotalTreks.setText("156");
        etLicenseNumber.setText("NTB/Guide/9842");
        etIssueDate.setText("2022-01-15");
        etExpiryDate.setText("2027-01-14");
    }

    private void validateAndSave() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = actvGender.getText().toString().trim();
        String bio = etAboutMe.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            return;
        }

        saveProfile(fullName, email, phone, dob, gender, bio);
    }

    private void saveProfile(String fullName, String email, String phone, String dob, String gender, String bio) {
        String token = SessionUser.getToken(this);
        if (token == null) return;

        // Note: UpdateProfileRequest expects username, city, country too.
        // We'll pass null or empty if not in this form.
        UpdateProfileRequest request = new UpdateProfileRequest(
                fullName,
                null, // username
                email,
                phone,
                dob,
                gender,
                bio,
                null, // city
                null  // country
        );

        apiService.updateProfile("Bearer " + token, request).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GuideEditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GuideEditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(GuideEditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}