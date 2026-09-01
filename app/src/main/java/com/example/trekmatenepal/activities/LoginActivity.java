package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.LoginRequest;

import com.example.trekmatenepal.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TREKMATE_LOGIN";

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtSignup, txtForgot;
    RadioGroup loginRoleGroup;
    RadioButton rbLoginTrekker, rbLoginGuide;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgot = findViewById(R.id.txtForgot);
        loginRoleGroup = findViewById(R.id.loginRoleGroup);
        rbLoginTrekker = findViewById(R.id.rbLoginTrekker);
        rbLoginGuide = findViewById(R.id.rbLoginGuide);

        // Create Retrofit API service
        apiService = ApiClient.getClient().create(ApiService.class);

        loginRoleGroup.setOnCheckedChangeListener((group, checkedId) -> updateLoginMode());
        updateLoginMode();

        btnLogin.setOnClickListener(v -> {

            if (rbLoginGuide.isChecked()) {
                loginAsGuideLocally();
                return;
            }

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        LoginActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            loginUser(email, password);
        });

        txtSignup.setOnClickListener(v -> {
            startActivity(
                    new Intent(
                            LoginActivity.this,
                            SignupActivity.class
                    )
            );
        });

        txtForgot.setOnClickListener(v -> {
            startActivity(
                    new Intent(
                            LoginActivity.this,
                            ForgotPasswordActivity.class
                    )
            );
        });
    }

    private void loginUser(String email, String password) {

        Log.d(TAG, "Sending login request to backend");

        // Prevent multiple clicks while request is running
        btnLogin.setEnabled(false);

        LoginRequest request =
                new LoginRequest(email, password, "USER");

        apiService.loginUser(request).enqueue(
                new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(
                            Call<LoginResponse> call,
                            Response<LoginResponse> response) {

                        btnLogin.setEnabled(true);

                        Log.d(
                                TAG,
                                "Login response: HTTP "
                                        + response.code()
                        );

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getToken() != null
                                && response.body().getUser() != null) {

                            Log.d(
                                    TAG,
                                    "Login successful"
                            );

                            LoginResponse loginResponse = response.body();

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // The backend has verified that this is a database USER/Trekker.
                            SessionUser.clear(LoginActivity.this);
                            SessionUser.setUserId(LoginActivity.this,
                                    String.valueOf(loginResponse.getUser().getId()));
                            SessionUser.setToken(LoginActivity.this, loginResponse.getToken());
                            saveRole("TREKKER");

                            openDestination(DashboardActivity.class);

                        } else {

                            String errorMessage =
                                    "Login failed";

                            try {
                                if (response.errorBody() != null) {
                                    errorMessage =
                                            response.errorBody()
                                                    .string();
                                }
                            } catch (Exception e) {
                                Log.e(
                                        TAG,
                                        "Could not read error",
                                        e
                                );
                            }

                            Log.e(
                                    TAG,
                                    "Login failed: "
                                            + errorMessage
                            );

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Invalid email or password",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<LoginResponse> call,
                            Throwable t) {

                        btnLogin.setEnabled(true);

                        Log.e(
                                TAG,
                                "Login request failed",
                                t
                        );

                        Toast.makeText(
                                LoginActivity.this,
                                "Cannot connect to server",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void updateLoginMode() {
        boolean guideMode = rbLoginGuide.isChecked();
        etEmail.setEnabled(!guideMode);
        etPassword.setEnabled(!guideMode);
        etEmail.setAlpha(guideMode ? 0.6f : 1f);
        etPassword.setAlpha(guideMode ? 0.6f : 1f);
        btnLogin.setText(guideMode ? "CONTINUE AS GUIDE" : "LOGIN AS TREKKER");
        txtForgot.setVisibility(guideMode ? android.view.View.GONE : android.view.View.VISIBLE);
    }

    /** Temporary guide access requested by the project owner; no backend call is made. */
    private void loginAsGuideLocally() {
        SessionUser.clear(this);
        SessionUser.setUserId(this, "Local Guide");
        saveRole("GUIDE");
        Toast.makeText(this, "Continuing in Guide mode", Toast.LENGTH_SHORT).show();
        openDestination(GuideDashboardActivity.class);
    }

    private void saveRole(String role) {
        getSharedPreferences("TrekMatePrefs", MODE_PRIVATE)
                .edit()
                .putString("user_role", role)
                .apply();
    }

    private void openDestination(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

