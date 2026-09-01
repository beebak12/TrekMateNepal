package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.ErrorResponse;
import com.example.trekmatenepal.model.ProfileResponse;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private View layoutTreks, layoutEdit, layoutGear, layoutPosts, layoutTreksActivity;
    private TextView tvProfileName, tvProfileLocation, tvTrekCountSummary, tvProfileAge,
            tvProfileGender, tvProfileBio, tvProfileUsername, tvProfileEmail,
            tvProfilePhone, tvProfileDob, tvProfileAddress, tvProfileError;
    private ImageView profileImage;
    private ProgressBar progressProfile;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        apiService = ApiClient.getClient().create(ApiService.class);
        initializeViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void initializeViews() {
        layoutTreks = findViewById(R.id.layoutTreks);
        layoutEdit = findViewById(R.id.layoutEdit);
        layoutGear = findViewById(R.id.layoutGear);
        layoutPosts = findViewById(R.id.layoutPosts);
        layoutTreksActivity = findViewById(R.id.layoutTreksActivity);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileLocation = findViewById(R.id.tvProfileLocation);
        tvTrekCountSummary = findViewById(R.id.tvTrekCountSummary);
        tvProfileAge = findViewById(R.id.tvProfileAge);
        tvProfileGender = findViewById(R.id.tvProfileGender);
        tvProfileBio = findViewById(R.id.tvProfileBio);
        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        tvProfileDob = findViewById(R.id.tvProfileDob);
        tvProfileAddress = findViewById(R.id.tvProfileAddress);
        tvProfileError = findViewById(R.id.tvProfileError);
        progressProfile = findViewById(R.id.progressProfile);
        profileImage = findViewById(R.id.profileImage);
        tvTrekCountSummary.setText("0");
    }

    private void loadUserData() {
        String token = SessionUser.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            showError("Your session has expired. Please log in again.");
            return;
        }
        showLoading(true);
        apiService.getProfile("Bearer " + token).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess() && response.body().getData() != null) {
                    bindProfile(response.body().getData());
                } else if (response.code() == 401) {
                    showError("Your session has expired. Please log in again.");
                } else if (response.code() == 404) {
                    showError("Your profile could not be found.");
                } else {
                    showError(readError(response, "Unable to load profile right now."));
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                showLoading(false);
                showError("Cannot connect to the server. Check your internet connection and try again.");
            }
        });
    }

    private void bindProfile(ProfileResponse.UserData user) {
        tvProfileError.setVisibility(View.GONE);
        tvProfileName.setText(valueOrDash(user.getFullName()));
        tvProfileUsername.setText(valueOrDash(user.getUsername()));
        tvProfileEmail.setText(valueOrDash(user.getEmail()));
        tvProfilePhone.setText(valueOrDash(user.getPhone()));
        tvProfileDob.setText(formatDobForDisplay(user.getDob()));
        tvProfileGender.setText(formatGender(user.getGender()));
        tvProfileBio.setText(valueOrDefault(user.getBio(), "No bio added yet."));
        String address = joinLocation(user.getCity(), user.getCountry());
        tvProfileAddress.setText(address);
        tvProfileLocation.setText(address);
        tvProfileAge.setText(calculateAge(user.getDob()));
        if (user.getProfileImage() != null && !user.getProfileImage().trim().isEmpty()) {
            loadProfileImage(user.getProfileImage());
        } else {
            profileImage.setImageResource(R.drawable.profile_photo);
        }
    }

    private void loadProfileImage(String imagePath) {
        String url = imagePath.startsWith("http")
                ? imagePath : ApiClient.getBaseUrl() + imagePath.replaceFirst("^/", "");
        apiService.downloadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    if (bitmap != null) profileImage.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                profileImage.setImageResource(R.drawable.profile_photo);
            }
        });
    }

    private void showLoading(boolean loading) {
        progressProfile.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) tvProfileError.setVisibility(View.GONE);
    }

    private void showError(String message) {
        showLoading(false);
        tvProfileError.setText(message + "\nTap to retry");
        tvProfileError.setVisibility(View.VISIBLE);
    }

    private String readError(Response<?> response, String fallback) {
        try {
            if (response.errorBody() != null) {
                ErrorResponse error = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                if (error != null && error.getMessage() != null && !error.getMessage().trim().isEmpty()) {
                    return error.getMessage();
                }
            }
        } catch (Exception ignored) { }
        return fallback;
    }

    private String calculateAge(String dob) {
        Date date = parseDob(dob);
        if (date == null) return "—";
        Calendar birth = Calendar.getInstance();
        birth.setTime(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) age--;
        return Math.max(age, 0) + " Years";
    }

    private String formatDobForDisplay(String dob) {
        Date date = parseDob(dob);
        return date == null ? valueOrDash(dob)
                : new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date);
    }

    private Date parseDob(String dob) {
        if (dob == null || dob.trim().isEmpty()) return null;
        String value = dob.length() >= 10 ? dob.substring(0, 10) : dob;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            format.setLenient(false);
            return format.parse(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String formatGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) return "—";
        if ("PREFER_NOT_TO_SAY".equalsIgnoreCase(gender)) return "Prefer not to say";
        String lower = gender.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private String joinLocation(String city, String country) {
        boolean hasCity = city != null && !city.trim().isEmpty();
        boolean hasCountry = country != null && !country.trim().isEmpty();
        if (hasCity && hasCountry) return city.trim() + ", " + country.trim();
        if (hasCity) return city.trim();
        if (hasCountry) return country.trim();
        return "No address added";
    }

    private String valueOrDash(String value) {
        return valueOrDefault(value, "—");
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private void setupListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        tvProfileError.setOnClickListener(v -> loadUserData());
        layoutTreks.setOnClickListener(v -> startActivity(new Intent(this, TreksCompletedActivity.class)));
        layoutEdit.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        layoutGear.setOnClickListener(v -> startActivity(new Intent(this, PostedGearActivity.class)));
        layoutPosts.setOnClickListener(v -> startActivity(new Intent(this, PostedGearActivity.class)));
        layoutTreksActivity.setOnClickListener(v -> startActivity(new Intent(this, TreksCompletedActivity.class)));
        findViewById(R.id.btnNotification).setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}
