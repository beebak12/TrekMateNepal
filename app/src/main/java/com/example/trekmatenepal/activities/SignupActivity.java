package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class SignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPassword, etPhone;
    Button btnSignup;
    CheckBox checkTerms;
    TextView txtLogin;

    Button btnGoogle, btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);

        btnSignup = findViewById(R.id.btnSignup);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);

        checkTerms = findViewById(R.id.checkTerms);
        txtLogin = findViewById(R.id.txtLogin);

        btnSignup.setOnClickListener(v -> registerUser());

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this,
                        "Google Sign Up Coming Soon",
                        Toast.LENGTH_SHORT).show());

        btnFacebook.setOnClickListener(v ->
                Toast.makeText(this,
                        "Facebook Sign Up Coming Soon",
                        Toast.LENGTH_SHORT).show());

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phone = etPhone.getText().toString().trim();

        if(name.isEmpty()){
            etName.setError("Enter your full name");
            etName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            etConfirmPassword.setError("Confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword)){
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        if(!checkTerms.isChecked()){
            Toast.makeText(this,
                    "Please accept Terms & Conditions",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Registration Successful",
                Toast.LENGTH_LONG).show();

        startActivity(new Intent(SignupActivity.this,
                LoginActivity.class));

        finish();
    }
}