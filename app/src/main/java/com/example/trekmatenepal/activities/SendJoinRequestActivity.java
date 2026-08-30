package com.example.trekmatenepal.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;
import com.example.trekmatenepal.models.JoinRequest;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendJoinRequestActivity extends AppCompatActivity {

    private ImageView ivBackButton, ivPartnerImage;
    private TextView tvPartnerName, tvPartnerLocation, tvCharCount;
    private EditText etMessage, etTrekDate;
    private Spinner spinnerDays;
    private MaterialButton btnSendRequest;
    private PartnerModel partner;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_join_request);

        initializeViews();
        getPartnerData();
        if (partner != null) {
            displayPartnerInfo();
            setupUI();
        }
    }

    private void initializeViews() {
        ivBackButton = findViewById(R.id.ivBackButton);
        ivPartnerImage = findViewById(R.id.ivPartnerImage);
        tvPartnerName = findViewById(R.id.tvPartnerName);
        tvPartnerLocation = findViewById(R.id.tvPartnerLocation);
        etMessage = findViewById(R.id.etMessage);
        tvCharCount = findViewById(R.id.tvCharCount);
        etTrekDate = findViewById(R.id.etTrekDate);
        spinnerDays = findViewById(R.id.spinnerDays);
        btnSendRequest = findViewById(R.id.btnSendRequest);
    }

    private void getPartnerData() {
        Intent intent = getIntent();
        if (intent != null) {
            partner = (PartnerModel) intent.getSerializableExtra("partner");
        }
    }

    private void displayPartnerInfo() {
        tvPartnerName.setText(partner.getName());
        tvPartnerLocation.setText(partner.getBaseLocation());
        // TODO: Load partner image with Glide/Picasso
    }

    private void setupUI() {
        setupMessageListener();
        setupDatePicker();
        setupSpinner();
        setupClickListeners();
    }

    private void setupMessageListener() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tvCharCount.setText(length + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupDatePicker() {
        selectedCalendar = Calendar.getInstance();
        
        etTrekDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SendJoinRequestActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedCalendar.set(Calendar.YEAR, year);
                        selectedCalendar.set(Calendar.MONTH, month);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        etTrekDate.setText(sdf.format(selectedCalendar.getTime()));
                    },
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH),
                    selectedCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);
    }

    private void setupClickListeners() {
        ivBackButton.setOnClickListener(v -> finish());

        btnSendRequest.setOnClickListener(v -> {
            if (validateInputs()) {
                sendJoinRequest();
            }
        });
    }

    private boolean validateInputs() {
        if (etMessage.getText().toString().trim().isEmpty()) {
            etMessage.setError("Please enter a message");
            return false;
        }
        if (etTrekDate.getText().toString().isEmpty()) {
            etTrekDate.setError("Please select a date");
            return false;
        }
        return true;
    }

    private void sendJoinRequest() {
        String message = etMessage.getText().toString();
        String trekDate = etTrekDate.getText().toString();
        String numberOfDays = spinnerDays.getSelectedItem().toString();

        // Create JoinRequest object
        JoinRequest joinRequest = new JoinRequest(
                "current_user_id", // TODO: Get from current user session
                "Current User Name", // TODO: Get from current user session
                partner.getName(), // Partner ID placeholder if no ID in PartnerModel
                partner.getName(),
                "Trek Name", // TODO: Get selected trek name
                message
        );
        joinRequest.setTrekDate(trekDate);

        // TODO: Send to backend API
        // For now, show success message and navigate
        showSuccessAndNavigate();
    }

    private void showSuccessAndNavigate() {
        // TODO: Show success toast/snackbar
        Intent intent = new Intent(SendJoinRequestActivity.this, ChatActivity.class);
        intent.putExtra("partner", partner);
        intent.putExtra("chat_status", "request_sent");
        startActivity(intent);
        finish();
    }
}
