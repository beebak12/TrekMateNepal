package com.example.trekmatenepal.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.ErrorResponse;
import com.example.trekmatenepal.model.ProfileResponse;
import com.example.trekmatenepal.model.UpdateProfileRequest;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack, btnCamera, imgProfile;
    private Button btnMinus, btnPlus, btnSaveChanges;
    private EditText etFullName, etUsername, etEmail, etPhone, etLocation, etBio;
    private TextView tvDateOfBirth, tvAge, tvTrekCount;
    private RadioGroup radioGender;
    private RadioButton rbMale, rbFemale, rbOther;

    private TextView chipEverest, chipAnnapurna, chipLangtang, chipMustang, chipManaslu, chipOthers;

    private int trekCount = 0;
    private String selectedImagePath = "";
    private ProgressBar progressEditProfile;
    private ApiService apiService;
    private String selectedDobIso = "";
    private String authToken;
    private boolean newImageSelected;

    private boolean isEverestSelected = false, isAnnapurnaSelected = false, isLangtangSelected = false;
    private boolean isMustangSelected = false, isManasluSelected = false, isOthersSelected = false;

    // Image Picker Launcher
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImagePath = uri.toString();
                    newImageSelected = true;
                    imgProfile.setImageURI(uri);
                }
            }
    );

    // Permission Launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        apiService = ApiClient.getClient().create(ApiService.class);
        authToken = SessionUser.getToken(this);
        initializeViews();
        loadUserData();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCamera = findViewById(R.id.btnCamera);
        imgProfile = findViewById(R.id.imgProfile);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        progressEditProfile = findViewById(R.id.progressEditProfile);

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        etBio = findViewById(R.id.etBio);

        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        tvAge = findViewById(R.id.tvAge);
        tvTrekCount = findViewById(R.id.tvTrekCount);

        radioGender = findViewById(R.id.radioGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);

        chipEverest = findViewById(R.id.chipEverest);
        chipAnnapurna = findViewById(R.id.chipAnnapurna);
        chipLangtang = findViewById(R.id.chipLangtang);
        chipMustang = findViewById(R.id.chipMustang);
        chipManaslu = findViewById(R.id.chipManaslu);
        chipOthers = findViewById(R.id.chipOthers);
    }

    private void loadUserData() {
        if (authToken == null || authToken.trim().isEmpty()) {
            Toast.makeText(this, "Your session has expired. Please log in again.", Toast.LENGTH_LONG).show();
            btnSaveChanges.setEnabled(false);
            return;
        }
        setLoading(true, "Loading profile…");
        apiService.getProfile("Bearer " + authToken).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                setLoading(false, null);
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess() && response.body().getData() != null) {
                    bindUserData(response.body().getData());
                } else if (response.code() == 401) {
                    btnSaveChanges.setEnabled(false);
                    Toast.makeText(EditProfileActivity.this, "Your session has expired. Please log in again.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            readError(response, "Unable to load your profile."), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                setLoading(false, null);
                Toast.makeText(EditProfileActivity.this,
                        "Cannot connect to the server. Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        });
        updateAllChips();
    }

    private void bindUserData(ProfileResponse.UserData user) {
        etFullName.setText(value(user.getFullName()));
        etUsername.setText(value(user.getUsername()));
        etEmail.setText(value(user.getEmail()));
        etEmail.setEnabled(false);
        etPhone.setText(value(user.getPhone()));
        etLocation.setText(value(user.getCity()));
        etBio.setText(value(user.getBio()));
        selectedDobIso = normalizeIsoDate(user.getDob());
        updateDobDisplay(selectedDobIso);
        if ("MALE".equalsIgnoreCase(user.getGender())) rbMale.setChecked(true);
        else if ("FEMALE".equalsIgnoreCase(user.getGender())) rbFemale.setChecked(true);
        else if ("OTHER".equalsIgnoreCase(user.getGender())) rbOther.setChecked(true);
        if (user.getProfileImage() != null && !user.getProfileImage().trim().isEmpty()) {
            loadRemoteImage(user.getProfileImage());
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCamera.setOnClickListener(v -> checkPermissionAndOpenGallery());

        btnPlus.setOnClickListener(v -> {
            trekCount++;
            tvTrekCount.setText(String.valueOf(trekCount));
        });

        btnMinus.setOnClickListener(v -> {
            if (trekCount > 0) {
                trekCount--;
                tvTrekCount.setText(String.valueOf(trekCount));
            }
        });

        tvDateOfBirth.setOnClickListener(v -> showDatePicker());

        btnSaveChanges.setOnClickListener(v -> saveChanges());

        // Chip Listeners
        chipEverest.setOnClickListener(v -> { isEverestSelected = !isEverestSelected; updateAllChips(); });
        chipAnnapurna.setOnClickListener(v -> { isAnnapurnaSelected = !isAnnapurnaSelected; updateAllChips(); });
        chipLangtang.setOnClickListener(v -> { isLangtangSelected = !isLangtangSelected; updateAllChips(); });
        chipMustang.setOnClickListener(v -> { isMustangSelected = !isMustangSelected; updateAllChips(); });
        chipManaslu.setOnClickListener(v -> { isManasluSelected = !isManasluSelected; updateAllChips(); });
        chipOthers.setOnClickListener(v -> { isOthersSelected = !isOthersSelected; updateAllChips(); });
    }

    private void checkPermissionAndOpenGallery() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDobIso = String.format(Locale.US, "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    updateDobDisplay(selectedDobIso);
                    calculateAge(year1, monthOfYear, dayOfMonth);
                }, year, month, day);
        datePickerDialog.show();
    }

    private String getMonthName(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    private void calculateAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        tvAge.setText("Age: " + age);
    }

    private void updateAllChips() {
        updateChip(chipEverest, isEverestSelected, "Everest Region");
        updateChip(chipAnnapurna, isAnnapurnaSelected, "Annapurna Region");
        updateChip(chipLangtang, isLangtangSelected, "Langtang Region");
        updateChip(chipMustang, isMustangSelected, "Mustang");
        updateChip(chipManaslu, isManasluSelected, "Manaslu Region");
        updateChip(chipOthers, isOthersSelected, "Others");
    }

    private void updateChip(TextView chip, boolean isSelected, String text) {
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.chip_selected_bg);
            chip.setText(text + " ✓");
            chip.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.chip_unselected_bg);
            chip.setText(text);
            chip.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void saveChanges() {
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etLocation.getText().toString().trim();
        String bio = etBio.getText().toString().trim();
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }
        if (username.length() < 3) {
            etUsername.setError("Username must be at least 3 characters");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Profile email is missing. Reload the profile and try again.", Toast.LENGTH_LONG).show();
            return;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return;
        }
        if (selectedDobIso.isEmpty()) {
            Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (city.isEmpty()) {
            etLocation.setError("Address is required");
            etLocation.requestFocus();
            return;
        }
        int selectedId = radioGender.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }
        String gender = selectedId == R.id.rbMale ? "MALE"
                : selectedId == R.id.rbFemale ? "FEMALE" : "OTHER";
        UpdateProfileRequest request = new UpdateProfileRequest(
                fullName, username, email, phone, selectedDobIso, gender, bio, city, null);
        setLoading(true, "Saving…");
        apiService.updateProfile("Bearer " + authToken, request).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    if (newImageSelected) uploadSelectedImage();
                    else finishSuccessfulSave();
                } else {
                    setLoading(false, null);
                    String fallback = response.code() == 409
                            ? "That username is already in use." : "Profile update failed. Please check your information.";
                    Toast.makeText(EditProfileActivity.this, readError(response, fallback), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                setLoading(false, null);
                Toast.makeText(EditProfileActivity.this,
                        "Cannot connect to the server. Your changes were not saved.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadSelectedImage() {
        try (InputStream input = getContentResolver().openInputStream(Uri.parse(selectedImagePath));
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            if (input == null) throw new IllegalStateException("Unable to read selected image");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);
            String mime = getContentResolver().getType(Uri.parse(selectedImagePath));
            if (mime == null) mime = "image/jpeg";
            RequestBody body = RequestBody.create(MediaType.parse(mime), output.toByteArray());
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", "profile.jpg", body);
            apiService.uploadProfileImage("Bearer " + authToken, part).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        finishSuccessfulSave();
                    } else {
                        setLoading(false, null);
                        Toast.makeText(EditProfileActivity.this,
                                "Profile details were saved, but the photo upload failed.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    setLoading(false, null);
                    Toast.makeText(EditProfileActivity.this,
                            "Profile details were saved, but the photo upload failed.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            setLoading(false, null);
            Toast.makeText(this, "Profile details were saved, but the selected photo could not be read.", Toast.LENGTH_LONG).show();
        }
    }

    private void finishSuccessfulSave() {
        setLoading(false, null);
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void setLoading(boolean loading, String buttonText) {
        progressEditProfile.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSaveChanges.setEnabled(!loading);
        btnSaveChanges.setText(loading && buttonText != null ? buttonText : "Save Changes");
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

    private void loadRemoteImage(String path) {
        String url = path.startsWith("http") ? path : ApiClient.getBaseUrl() + path.replaceFirst("^/", "");
        apiService.downloadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    if (bitmap != null) imgProfile.setImageBitmap(bitmap);
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });
    }

    private String normalizeIsoDate(String dob) {
        if (dob == null || dob.trim().isEmpty()) return "";
        return dob.length() >= 10 ? dob.substring(0, 10) : dob;
    }

    private void updateDobDisplay(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            tvDateOfBirth.setText("Select date of birth");
            tvAge.setText("Age: —");
            return;
        }
        try {
            SimpleDateFormat api = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            api.setLenient(false);
            Date parsed = api.parse(isoDate);
            tvDateOfBirth.setText(new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(parsed));
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsed);
            calculateAge(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            tvDateOfBirth.setText(isoDate);
        }
    }

    private String value(String input) {
        return input == null ? "" : input;
    }

    private String getSerializedRegions() {
        StringBuilder sb = new StringBuilder();
        if (isEverestSelected) sb.append("Everest,");
        if (isAnnapurnaSelected) sb.append("Annapurna,");
        if (isLangtangSelected) sb.append("Langtang,");
        if (isMustangSelected) sb.append("Mustang,");
        if (isManasluSelected) sb.append("Manaslu,");
        if (isOthersSelected) sb.append("Others,");
        return sb.toString();
    }

    private void parseRegions(String regions) {
        if (regions == null) return;
        isEverestSelected = regions.contains("Everest");
        isAnnapurnaSelected = regions.contains("Annapurna");
        isLangtangSelected = regions.contains("Langtang");
        isMustangSelected = regions.contains("Mustang");
        isManasluSelected = regions.contains("Manaslu");
        isOthersSelected = regions.contains("Others");
    }
}
