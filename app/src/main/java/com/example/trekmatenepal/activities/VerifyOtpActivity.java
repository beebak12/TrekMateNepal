package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

/**
 * VerifyOtpActivity — Second screen of password recovery flow
 * User enters the 6-digit OTP sent to their email or phone
 */
public class VerifyOtpActivity extends AppCompatActivity {

    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private Button btnVerifyCode;
    private ImageView btnBack;
    private TextView tvResendCode, tvResendTimer, tvError;
    private ImageView imgMethodIcon;
    private TextView tvSubtitle;

    private String method = "email";
    private String identifier = "";
    private CountDownTimer resendTimer;
    private static final int TIMER_DURATION = 45000; // 45 seconds

    // Mock OTP for testing - replace with actual backend OTP
    private static final String MOCK_OTP = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initializeViews();
        getDataFromIntent();
        setupClickListeners();
        setupOtpInputs();
        startResendTimer();
    }

    private void initializeViews() {
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);
        etOtp6 = findViewById(R.id.etOtp6);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        btnBack = findViewById(R.id.btnBack);
        tvResendCode = findViewById(R.id.tvResendCode);
        tvResendTimer = findViewById(R.id.tvResendTimer);
        tvError = findViewById(R.id.tvError);
        imgMethodIcon = findViewById(R.id.imgMethodIcon);
        tvSubtitle = findViewById(R.id.tvSubtitle);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        method = intent.getStringExtra("method");
        identifier = intent.getStringExtra("identifier");

        // Update UI based on method
        if ("phone".equals(method)) {
            imgMethodIcon.setImageResource(R.drawable.ic_phone);
            tvSubtitle.setText("We've sent a 6-digit verification code to your phone.");
        } else {
            imgMethodIcon.setImageResource(R.drawable.ic_email);
            tvSubtitle.setText("We've sent a 6-digit verification code to your email.");
        }
    }

    private void setupClickListeners() {
        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Verify Code Button
        btnVerifyCode.setOnClickListener(v -> handleVerifyCode());

        // Resend Code
        tvResendCode.setOnClickListener(v -> handleResendCode());
    }

    private void setupOtpInputs() {
        EditText[] otpFields = {etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6};

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        // Move to next field
                        otpFields[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Handle backspace
            otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && index > 0) {
                    if (otpFields[index].getText().toString().isEmpty()) {
                        otpFields[index - 1].requestFocus();
                    }
                }
                return false;
            });
        }
    }

    private void handleVerifyCode() {
        String otp = getOtpCode();

        if (otp.isEmpty()) {
            tvError.setText("Please enter all 6 digits");
            tvError.setVisibility(android.view.View.VISIBLE);
            return;
        }

        if (otp.length() != 6) {
            tvError.setText("Please enter exactly 6 digits");
            tvError.setVisibility(android.view.View.VISIBLE);
            return;
        }

        // Verify OTP
        verifyOtp(otp);
    }

    private String getOtpCode() {
        return etOtp1.getText().toString() +
               etOtp2.getText().toString() +
               etOtp3.getText().toString() +
               etOtp4.getText().toString() +
               etOtp5.getText().toString() +
               etOtp6.getText().toString();
    }

    /**
     * Verify OTP with backend
     * TODO: Connect to Node.js API
     */
    private void verifyOtp(String otp) {
        btnVerifyCode.setEnabled(false);

        // TODO: Call backend API
        // POST /api/auth/verify-otp
        // Request body:
        // {
        //   "identifier": identifier,
        //   "otp": otp
        // }
        // Backend should:
        // 1. Check if OTP matches and is not expired
        // 2. If valid, generate temporary reset token
        // 3. Return success with reset token

        // Mock verification - replace with actual backend call
        if (otp.equals(MOCK_OTP)) {
            tvError.setVisibility(android.view.View.GONE);
            Toast.makeText(this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to Reset Password
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            intent.putExtra("method", method);
            intent.putExtra("identifier", identifier);
            intent.putExtra("resetToken", "temp_token_" + System.currentTimeMillis()); // Mock token
            startActivity(intent);
            finish();
        } else {
            tvError.setText("Invalid verification code. Please try again.");
            tvError.setVisibility(android.view.View.VISIBLE);
            clearOtpFields();
        }

        btnVerifyCode.setEnabled(true);
    }

    private void clearOtpFields() {
        EditText[] otpFields = {etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6};
        for (EditText field : otpFields) {
            field.setText("");
        }
        etOtp1.requestFocus();
    }

    private void startResendTimer() {
        resendTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tvResendTimer.setText(String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
            }

            @Override
            public void onFinish() {
                enableResend();
            }
        }.start();
    }

    private void enableResend() {
        tvResendCode.setClickable(true);
        tvResendCode.setFocusable(true);
        tvResendCode.setAlpha(1.0f);
        tvResendTimer.setText("00:00");
    }

    private void handleResendCode() {
        if (!tvResendCode.isClickable()) {
            return;
        }

        // Disable resend button
        tvResendCode.setClickable(false);
        tvResendCode.setFocusable(false);
        tvResendCode.setAlpha(0.5f);

        // Clear OTP fields
        clearOtpFields();
        tvError.setVisibility(android.view.View.GONE);

        // TODO: Call backend API to resend OTP
        // POST /api/auth/resend-otp
        // Request body:
        // {
        //   "method": method,
        //   "identifier": identifier
        // }

        Toast.makeText(this, "New code sent to " + identifier, Toast.LENGTH_SHORT).show();

        // Restart timer
        if (resendTimer != null) {
            resendTimer.cancel();
        }
        startResendTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }
}
