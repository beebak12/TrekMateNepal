package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.network.NetworkHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.Locale;

public class ForgotPasswordPhoneActivity extends AppCompatActivity {

    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private MaterialButton btnVerify;
    private TextView tvResend, tvPhone;
    private ImageView btnBack;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000;
    private String userPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_phone);

        userPhone = getIntent().getStringExtra("phone");

        initializeViews();
        setupOtpFocus();
        setupClickListeners();
        startTimer();
    }

    private void initializeViews() {
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);
        etOtp6 = findViewById(R.id.etOtp6);
        btnVerify = findViewById(R.id.btnVerifyCode);
        tvResend = findViewById(R.id.tvResend);
        tvPhone = findViewById(R.id.tvPhone);
        btnBack = findViewById(R.id.btnBack);

        if (userPhone != null) {
            String masked = userPhone.substring(0, Math.min(userPhone.length(), 6)) + "********";
            tvPhone.setText(masked);
        }
    }

    private void setupOtpFocus() {
        EditText[] otps = {etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6};
        for (int i = 0; i < 6; i++) {
            final int index = i;
            otps[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < 5) {
                        otps[index + 1].requestFocus();
                    }
                    checkAllFields();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otps[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && otps[index].getText().length() == 0 && index > 0) {
                    otps[index - 1].requestFocus();
                }
                return false;
            });
        }
    }

    private void checkAllFields() {
        String otp = getOtp();
        btnVerify.setEnabled(otp.length() == 6);
    }

    private String getOtp() {
        return etOtp1.getText().toString() + etOtp2.getText().toString() +
               etOtp3.getText().toString() + etOtp4.getText().toString() +
               etOtp5.getText().toString() + etOtp6.getText().toString();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnVerify.setOnClickListener(v -> verifyOtp());
        tvResend.setOnClickListener(v -> resendOtp());
    }

    private void verifyOtp() {
        String otp = getOtp();
        btnVerify.setEnabled(false);
        
        try {
            JSONObject body = new JSONObject();
            body.put("phone", userPhone);
            body.put("otp", otp);

            NetworkHelper.post("api/auth/forgot-password/verify", body, new NetworkHelper.Callback() {
                @Override
                public void onSuccess(JSONObject response) {
                    btnVerify.setEnabled(true);
                    String token = response.optString("resetToken");
                    Intent intent = new Intent(ForgotPasswordPhoneActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("resetToken", token);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    btnVerify.setEnabled(true);
                    Toast.makeText(ForgotPasswordPhoneActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            btnVerify.setEnabled(true);
        }
    }

    private void resendOtp() {
        if (timeLeftInMillis <= 0) {
            timeLeftInMillis = 60000;
            startTimer();
            // TODO: Call resend API
            Toast.makeText(this, "OTP Resent", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvResend.setText("Didn't receive code? Resend (" + timeFormatted + ")");
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                tvResend.setText("Didn't receive code? Resend");
                tvResend.setTextColor(getResources().getColor(R.color.forgot_primary));
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
