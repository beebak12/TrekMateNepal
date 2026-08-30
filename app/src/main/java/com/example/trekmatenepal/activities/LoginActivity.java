package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TREKMATE_LOGIN";

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtSignup, txtForgot;

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

        // Create Retrofit API service
        apiService = ApiClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(v -> {

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
                new LoginRequest(email, password);

        apiService.loginUser(request).enqueue(
                new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response) {

                        btnLogin.setEnabled(true);

                        Log.d(
                                TAG,
                                "Login response: HTTP "
                                        + response.code()
                        );

                        if (response.isSuccessful()) {

                            Log.d(
                                    TAG,
                                    "Login successful"
                            );

                            /*
                             * At this stage we have confirmed that
                             * the backend accepted the login.
                             *
                             * We will add proper JWT parsing/storage
                             * after confirming the API works.
                             */

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Remember the logged-in user
                            SessionUser.setUserId(
                                    LoginActivity.this,
                                    email
                            );

                            checkUserRole();

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
                            Call<Object> call,
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

    private void checkUserRole() {

        android.content.SharedPreferences prefs =
                getSharedPreferences(
                        "TrekMatePrefs",
                        MODE_PRIVATE
                );

        String role =
                prefs.getString("user_role", "");

        Intent intent;

        if (role.equals("TREKKER")) {

            intent =
                    new Intent(
                            this,
                            DashboardActivity.class
                    );

        } else if (role.equals("GUIDE")) {

            intent =
                    new Intent(
                            this,
                            GuideDashboardActivity.class
                    );

        } else {

            intent =
                    new Intent(
                            this,
                            RoleSelectionActivity.class
                    );
        }

        startActivity(intent);
        finish();
    }
}

