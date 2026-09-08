package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "TREKMATE_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Log.d(TAG, "SplashActivity started");

        // Create Retrofit client
        Retrofit retrofit = ApiClient.getClient();

        Log.d(TAG, "Retrofit client ready");

        // Create API service
        ApiService apiService =
                retrofit.create(ApiService.class);

        Log.d(TAG, "ApiService created");

        // Test backend connection
        apiService.testConnection().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(
                    Call<Object> call,
                    Response<Object> response) {

                Log.d(
                        TAG,
                        "Backend response received: HTTP "
                                + response.code()
                );

                if (response.isSuccessful()) {

                    Log.d(
                            TAG,
                            "Backend connected successfully!"
                    );

                } else {

                    Log.e(
                            TAG,
                            "Backend returned error: HTTP "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<Object> call,
                    Throwable t) {

                Log.e(
                        TAG,
                        "Backend connection FAILED"
                );

                Log.e(
                        TAG,
                        "Error: "
                                + t.getMessage()
                );
            }
        });

        // Continue after 2 seconds
        new Handler().postDelayed(() -> {
            Class<?> nextActivity;
            
            String token = com.example.trekmatenepal.data.SessionUser.getToken(this);
            String role = getSharedPreferences("TrekMatePrefs", MODE_PRIVATE).getString("user_role", "");

            if (token != null && !token.isEmpty()) {
                if ("GUIDE".equalsIgnoreCase(role)) {
                    nextActivity = GuideDashboardActivity.class;
                } else {
                    nextActivity = DashboardActivity.class;
                }
            } else if ("GUIDE".equalsIgnoreCase(role)) {
                // Handle local guide testing mode
                nextActivity = GuideDashboardActivity.class;
            } else {
                nextActivity = StartscreenActivity.class;
            }

            Intent intent = new Intent(SplashActivity.this, nextActivity);
            startActivity(intent);
            finish();
        }, 2000);
    }
}

