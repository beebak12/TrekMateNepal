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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.database.DatabaseHelpher;
import com.example.trekmatenepal.models.UserModel;

import java.util.Calendar;

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
    private DatabaseHelpher dbHelper;

    private boolean isEverestSelected = false, isAnnapurnaSelected = false, isLangtangSelected = false;
    private boolean isMustangSelected = false, isManasluSelected = false, isOthersSelected = false;

    // Image Picker Launcher
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImagePath = uri.toString();
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

        dbHelper = new DatabaseHelpher(this);
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
        UserModel user = dbHelper.getUserProfile();
        if (user != null) {
            etFullName.setText(user.getFullName());
            etUsername.setText(user.getUsername());
            etEmail.setText(user.getEmail());
            etPhone.setText(user.getPhone());
            tvDateOfBirth.setText(user.getDob());
            tvAge.setText(user.getAge());
            etLocation.setText(user.getLocation());
            etBio.setText(user.getBio());
            trekCount = user.getTrekCount();
            tvTrekCount.setText(String.valueOf(trekCount));

            if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                selectedImagePath = user.getImagePath();
                imgProfile.setImageURI(Uri.parse(selectedImagePath));
            }

            if (user.getGender() != null) {
                if (user.getGender().equals("Male")) rbMale.setChecked(true);
                else if (user.getGender().equals("Female")) rbFemale.setChecked(true);
                else if (user.getGender().equals("Other")) rbOther.setChecked(true);
            }

            parseRegions(user.getPreferredRegions());
        }
        updateAllChips();
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
                    String dob = dayOfMonth + " " + getMonthName(monthOfYear) + " " + year1;
                    tvDateOfBirth.setText(dob);
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
        UserModel user = new UserModel();
        user.setFullName(etFullName.getText().toString());
        user.setUsername(etUsername.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPhone(etPhone.getText().toString());
        user.setDob(tvDateOfBirth.getText().toString());
        user.setAge(tvAge.getText().toString());
        user.setLocation(etLocation.getText().toString());
        user.setBio(etBio.getText().toString());
        user.setTrekCount(trekCount);
        user.setPreferredRegions(getSerializedRegions());
        user.setImagePath(selectedImagePath);

        int selectedId = radioGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRb = findViewById(selectedId);
            user.setGender(selectedRb.getText().toString());
        }

        dbHelper.saveUserProfile(user);
        Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish();
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
