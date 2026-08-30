package com.example.trekmatenepal.activities;


import android.util.Log;

import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.model.ErrorResponse;
import com.example.trekmatenepal.model.RegisterRequest;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.trekmatenepal.utils.ApiDateFormatter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    // =========================================================
    // NORMAL EDIT TEXT FIELDS
    // =========================================================

    private EditText etFullName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etContact;
    private EditText etDateOfBirth;
    private EditText etAge;
    private EditText etAddress;

    // =========================================================
    // PASSWORD FIELDS
    // =========================================================

    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;

    // =========================================================
    // GENDER
    // =========================================================

    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbOther;
    private RadioButton rbPreferNotToSay;

    // =========================================================
    // TERMS
    // =========================================================

    private CheckBox cbTerms;
    private TextView tvTerms;

    // =========================================================
    // BUTTONS
    // =========================================================

    private Button btnCreateAccount;
    private Button btnGmail;
    private Button btnFacebook;

    // =========================================================
    // LOGIN
    // =========================================================

    private TextView tvLogin;

    // =========================================================
// DATE
// =========================================================

    private Calendar selectedDate = Calendar.getInstance();

    private final SimpleDateFormat dateFormatter =
            new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        initializeViews();

        setupDatePicker();

        setupPasswordValidation();

        setupClickListeners();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        // Normal fields

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etAge = findViewById(R.id.etAge);
        etAddress = findViewById(R.id.etAddress);

        // Password fields

        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        // Gender

        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        rbPreferNotToSay = findViewById(R.id.rbPreferNotToSay);

        // Terms

        cbTerms = findViewById(R.id.cbTerms);
        tvTerms = findViewById(R.id.tvTerms);

        // Buttons

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnGmail = findViewById(R.id.btnGmail);
        btnFacebook = findViewById(R.id.btnFacebook);

        // Login

        tvLogin = findViewById(R.id.tvLogin);


        // DOB should not open keyboard

        etDateOfBirth.setFocusable(false);
        etDateOfBirth.setClickable(true);

        // Age is automatically calculated

        etAge.setFocusable(false);
        etAge.setClickable(false);
    }


    // =========================================================
    // DATE PICKER
    // =========================================================

    private void setupDatePicker() {

        etDateOfBirth.setOnClickListener(v -> showDatePicker());
    }


    private void showDatePicker() {

        Calendar today = Calendar.getInstance();

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        SignupActivity.this,

                        (view, year, month, dayOfMonth) -> {

                            selectedDate.set(
                                    year,
                                    month,
                                    dayOfMonth
                            );

                            String date =
                                    dateFormatter.format(
                                            selectedDate.getTime()
                                    );

                            etDateOfBirth.setText(date);

                            calculateAge();
                        },

                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)
                );

        // User cannot select future date

        datePickerDialog
                .getDatePicker()
                .setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }


    // =========================================================
    // AGE CALCULATION
    // =========================================================

    private void calculateAge() {

        Calendar today = Calendar.getInstance();

        int age =
                today.get(Calendar.YEAR)
                        - selectedDate.get(Calendar.YEAR);

        if (
                today.get(Calendar.MONTH)
                        < selectedDate.get(Calendar.MONTH)

                        ||

                        (
                                today.get(Calendar.MONTH)
                                        == selectedDate.get(Calendar.MONTH)

                                        &&

                                        today.get(Calendar.DAY_OF_MONTH)
                                                < selectedDate.get(Calendar.DAY_OF_MONTH)
                        )
        ) {
            age--;
        }

        if (age >= 0) {
            etAge.setText(String.valueOf(age));
        }
    }


    // =========================================================
    // PASSWORD VALIDATION
    // =========================================================

    private void setupPasswordValidation() {

        etPassword.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        validatePasswordWhileTyping();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );


        etConfirmPassword.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        validateConfirmPassword();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }


    private void validatePasswordWhileTyping() {

        String password =
                etPassword.getText()
                        .toString();

        if (password.isEmpty()) {

            tilPassword.setError(null);

        } else if (password.length() < 8) {

            tilPassword.setError(
                    "Password must contain at least 8 characters"
            );

        } else if (!password.matches(".*\\d.*")) {

            tilPassword.setError(
                    "Password must contain at least one number"
            );

        } else {

            tilPassword.setError(null);
        }

        validateConfirmPassword();
    }


    private void validateConfirmPassword() {

        String password =
                etPassword.getText()
                        .toString();

        String confirmPassword =
                etConfirmPassword.getText()
                        .toString();

        if (confirmPassword.isEmpty()) {

            tilConfirmPassword.setError(null);

        } else if (!password.equals(confirmPassword)) {

            tilConfirmPassword.setError(
                    "Passwords do not match"
            );

        } else {

            tilConfirmPassword.setError(null);
        }
    }


    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // Create account

        btnCreateAccount.setOnClickListener(
                v -> handleCreateAccount()
        );


        // Gmail

        btnGmail.setOnClickListener(v -> {

            Toast.makeText(
                    SignupActivity.this,
                    "Gmail signup will be connected later.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Facebook

        btnFacebook.setOnClickListener(v -> {

            Toast.makeText(
                    SignupActivity.this,
                    "Facebook signup will be connected later.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Login

        tvLogin.setOnClickListener(
                v -> navigateToLogin()
        );


        // Terms text

        tvTerms.setOnClickListener(v -> {

            Toast.makeText(
                    SignupActivity.this,
                    "Please read and accept the Terms & Conditions and Privacy Policy.",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }


    // =========================================================
    // CREATE ACCOUNT
    // =========================================================

    private void handleCreateAccount() {

        hideKeyboard();

        if (!validateAllFields()) {

            return;
        }


        // Get all data

        String fullName =
                etFullName.getText()
                        .toString()
                        .trim();

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String contact =
                etContact.getText()
                        .toString()
                        .trim();

        String dobText =
                etDateOfBirth.getText()
                        .toString()
                        .trim();

        String dob = ApiDateFormatter.format(dobText);

        String age =
                etAge.getText()
                        .toString()
                        .trim();

        String gender =
                getSelectedGender();

        String address =
                etAddress.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString();


        // DOB check again to be safe
        if (dob == null || dob.isEmpty()) {
            Toast.makeText(this, "Invalid Date of Birth", Toast.LENGTH_SHORT).show();
            return;
        }


        createAccount(
                fullName,
                username,
                email,
                contact,
                dob,
                age,
                gender,
                address,
                password
        );
    }


    // =========================================================
    // VALIDATE ALL FIELDS
    // =========================================================

    private boolean validateAllFields() {

        boolean valid = true;


        // -----------------------------------------
        // FULL NAME
        // -----------------------------------------

        String fullName =
                etFullName.getText()
                        .toString()
                        .trim();

        if (fullName.isEmpty()) {

            etFullName.setError(
                    "Full name is required"
            );

            etFullName.requestFocus();

            valid = false;

        } else if (fullName.length() < 2) {

            etFullName.setError(
                    "Enter a valid full name"
            );

            valid = false;

        } else {

            etFullName.setError(null);
        }


        // -----------------------------------------
        // USERNAME
        // -----------------------------------------

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        if (username.isEmpty()) {

            etUsername.setError(
                    "Username is required"
            );

            if (valid) {
                etUsername.requestFocus();
            }

            valid = false;

        } else if (username.length() < 3) {

            etUsername.setError(
                    "Username must contain at least 3 characters"
            );

            valid = false;

        } else if (username.contains(" ")) {

            etUsername.setError(
                    "Username cannot contain spaces"
            );

            valid = false;

        } else {

            etUsername.setError(null);
        }


        // -----------------------------------------
        // EMAIL
        // -----------------------------------------

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        if (email.isEmpty()) {

            etEmail.setError(
                    "Email is required"
            );

            if (valid) {
                etEmail.requestFocus();
            }

            valid = false;

        } else if (!isValidEmail(email)) {

            etEmail.setError(
                    "Enter a valid email address"
            );

            valid = false;

        } else {

            etEmail.setError(null);
        }


        // -----------------------------------------
        // CONTACT - OPTIONAL
        // -----------------------------------------

        String contact =
                etContact.getText()
                        .toString()
                        .trim();

        if (!contact.isEmpty()
                && !isValidPhone(contact)) {

            etContact.setError(
                    "Enter a valid Nepal phone number"
            );

            valid = false;

        } else {

            etContact.setError(null);
        }


        // -----------------------------------------
        // DATE OF BIRTH
        // -----------------------------------------

        String dobText =
                etDateOfBirth.getText()
                        .toString()
                        .trim();

        String dob = ApiDateFormatter.format(dobText);

        if (dob == null || dob.isEmpty()) {

            etDateOfBirth.setError(
                    "Date of birth is required"
            );

            if (valid) {
                etDateOfBirth.requestFocus();
            }

            valid = false;

        } else {

            etDateOfBirth.setError(null);
        }


        // -----------------------------------------
        // AGE
        // -----------------------------------------

        String ageText =
                etAge.getText()
                        .toString()
                        .trim();

        if (ageText.isEmpty()) {

            etAge.setError(
                    "Select your date of birth"
            );

            valid = false;

        } else {

            try {

                int age =
                        Integer.parseInt(ageText);

                if (age < 13) {

                    etAge.setError(
                            "You must be at least 13 years old"
                    );

                    valid = false;

                } else {

                    etAge.setError(null);
                }

            } catch (NumberFormatException e) {

                etAge.setError(
                        "Invalid age"
                );

                valid = false;
            }
        }


        // -----------------------------------------
        // GENDER
        // -----------------------------------------

        if (rgGender.getCheckedRadioButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Please select your gender",
                    Toast.LENGTH_SHORT
            ).show();

            valid = false;
        }


        // -----------------------------------------
        // ADDRESS
        // -----------------------------------------

        String address =
                etAddress.getText()
                        .toString()
                        .trim();

        if (address.isEmpty()) {

            etAddress.setError(
                    "Address is required"
            );

            if (valid) {
                etAddress.requestFocus();
            }

            valid = false;

        } else {

            etAddress.setError(null);
        }


        // -----------------------------------------
        // PASSWORD
        // -----------------------------------------

        String password =
                etPassword.getText()
                        .toString();

        if (password.isEmpty()) {

            tilPassword.setError(
                    "Password is required"
            );

            if (valid) {
                etPassword.requestFocus();
            }

            valid = false;

        } else if (password.length() < 8) {

            tilPassword.setError(
                    "Password must contain at least 8 characters"
            );

            valid = false;

        } else if (!password.matches(".*\\d.*")) {

            tilPassword.setError(
                    "Password must contain at least one number"
            );

            valid = false;

        } else {

            tilPassword.setError(null);
        }


        // -----------------------------------------
        // CONFIRM PASSWORD
        // -----------------------------------------

        String confirmPassword =
                etConfirmPassword.getText()
                        .toString();

        if (confirmPassword.isEmpty()) {

            tilConfirmPassword.setError(
                    "Please confirm your password"
            );

            valid = false;

        } else if (!password.equals(confirmPassword)) {

            tilConfirmPassword.setError(
                    "Passwords do not match"
            );

            valid = false;

        } else {

            tilConfirmPassword.setError(null);
        }


        // -----------------------------------------
        // TERMS
        // -----------------------------------------

        if (!cbTerms.isChecked()) {

            Toast.makeText(
                    this,
                    "Please agree to Terms & Conditions and Privacy Policy",
                    Toast.LENGTH_SHORT
            ).show();

            valid = false;
        }


        return valid;
    }


    // =========================================================
    // EMAIL VALIDATION
    // =========================================================

    private boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches();
    }


    // =========================================================
    // NEPAL PHONE VALIDATION
    // =========================================================

    private boolean isValidPhone(String phone) {

        String cleanPhone =
                phone.replaceAll("[\\s-]", "");

        return Pattern
                .matches(
                        "^(\\+977)?9[678]\\d{8}$",
                        cleanPhone
                );
    }


    // =========================================================
    // GET GENDER
    // =========================================================

    private String getSelectedGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();

        if (selectedId == R.id.rbMale) {
            return "MALE";
        } else if (selectedId == R.id.rbFemale) {
            return "FEMALE";
        } else if (selectedId == R.id.rbOther) {
            return "OTHER";
        } else if (selectedId == R.id.rbPreferNotToSay) {
            return "PREFER_NOT_TO_SAY";
        }

        return "";
    }


    // =========================================================
    // CREATE ACCOUNT
    // =========================================================
    private void createAccount(
            String fullName,
            String username,
            String email,
            String contact,
            String dob,
            String age,
            String gender,
            String address,
            String password) {

        // Create registration request
        RegisterRequest request = new RegisterRequest(
                fullName,
                username,
                email,
                password,
                contact,
                dob,
                gender,
                address
        );

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        // Mask password for logging
        StringBuilder maskedBuilder = new StringBuilder();
        if (password != null) {
            for (int i = 0; i < password.length(); i++) {
                maskedBuilder.append("*");
            }
        }
        String maskedPassword = maskedBuilder.toString();

        Toast.makeText(this, "Sending gender: " + gender, Toast.LENGTH_SHORT).show();

        Log.d("TREKMATE_API", "Sending registration request...");
        Log.d("TREKMATE_API", "Full Name: " + fullName);
        Log.d("TREKMATE_API", "Username: " + username);
        Log.d("TREKMATE_API", "Email: " + email);
        Log.d("TREKMATE_API", "Contact: " + contact);
        Log.d("TREKMATE_API", "DOB: " + dob);
        Log.d("TREKMATE_API", "Sending gender: " + gender);
        Log.d("TREKMATE_API", "Address: " + address);
        Log.d("TREKMATE_API", "Password: " + maskedPassword);

        // Final verification of JSON format (masking password in JSON too)
        RegisterRequest logRequest = new RegisterRequest(
                fullName, username, email, maskedPassword, contact, dob, gender, address
        );
        Log.d("TREKMATE_API", "JSON Request (Masked): " + new Gson().toJson(logRequest));

        apiService.registerUser(request)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response) {

                        Log.d(
                                "TREKMATE_API",
                                "Register response: HTTP "
                                        + response.code()
                        );

                        if (response.isSuccessful()) {

                            Log.d(
                                    "TREKMATE_API",
                                    "Registration successful!"
                            );

                            Toast.makeText(
                                    SignupActivity.this,
                                    "Account created successfully!",
                                    Toast.LENGTH_LONG
                            ).show();

                            Intent intent =
                                    new Intent(
                                            SignupActivity.this,
                                            LoginActivity.class
                                    );

                            startActivity(intent);
                            finish();

                        } else {

                            // Get the REAL backend error
                            String errorMessage = "";
                            StringBuilder displayMessage = new StringBuilder();

                            try {
                                if (response.errorBody() != null) {
                                    errorMessage = response.errorBody().string();

                                    // Try to parse validation errors
                                    ErrorResponse errorRes = new Gson().fromJson(
                                            errorMessage,
                                            ErrorResponse.class
                                    );

                                    if (errorRes != null && errorRes.getErrors() != null) {
                                        for (ErrorResponse.ValidationError error : errorRes.getErrors()) {
                                            if (displayMessage.length() > 0) {
                                                displayMessage.append("\n");
                                            }
                                            displayMessage.append(error.getMsg());
                                        }
                                    } else if (errorRes != null && errorRes.getMessage() != null) {
                                        displayMessage.append(errorRes.getMessage());
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(
                                        "TREKMATE_API",
                                        "Could not read error response",
                                        e
                                );
                            }

                            Log.e(
                                    "TREKMATE_API",
                                    "Register failed: HTTP "
                                            + response.code()
                                            + " BODY: "
                                            + errorMessage
                            );

                            String message;

                            if (response.code() == 409) {

                                message = "Email or username already exists";

                            } else if (response.code() == 400) {

                                if (displayMessage.length() > 0) {
                                    message = displayMessage.toString();
                                } else {
                                    message = "Registration validation failed";
                                }

                            } else {

                                message = "Registration failed. Please try again.";
                            }

                            Toast.makeText(
                                    SignupActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Object> call,
                            Throwable t) {

                        Log.e(
                                "TREKMATE_API",
                                "Registration API error",
                                t
                        );

                        Toast.makeText(
                                SignupActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    // =========================================================
    // LOGIN NAVIGATION
    // =========================================================

    private void navigateToLogin() {

        Intent intent =
                new Intent(
                        SignupActivity.this,
                        LoginActivity.class
                );

        startActivity(intent);

        finish();
    }


    // =========================================================
    // HIDE KEYBOARD
    // =========================================================

    private void hideKeyboard() {

        View view =
                getCurrentFocus();

        if (view != null) {

            InputMethodManager imm =
                    (InputMethodManager)
                            getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            );

            if (imm != null) {

                imm.hideSoftInputFromWindow(
                        view.getWindowToken(),
                        0
                );
            }
        }
    }


    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}