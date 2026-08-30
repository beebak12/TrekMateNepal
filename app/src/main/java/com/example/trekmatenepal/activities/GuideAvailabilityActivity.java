package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GuideAvailabilityActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CalendarView calendarView;
    private TextView txtSelectedDate, txtStatusLabel;
    private MaterialButton btnMarkAvailable, btnMarkUnavailable;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_availability);

        initializeViews();
        selectedCalendar = Calendar.getInstance();
        updateDateDisplay();

        btnBack.setOnClickListener(v -> finish());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedCalendar.set(year, month, dayOfMonth);
            updateDateDisplay();
        });

        btnMarkAvailable.setOnClickListener(v -> {
            txtStatusLabel.setText("Current Status: Available");
            txtStatusLabel.setTextColor(getResources().getColor(R.color.success_green));
            Toast.makeText(this, "Marked as Available", Toast.LENGTH_SHORT).show();
        });

        btnMarkUnavailable.setOnClickListener(v -> {
            txtStatusLabel.setText("Current Status: Unavailable");
            txtStatusLabel.setTextColor(getResources().getColor(R.color.red));
            Toast.makeText(this, "Marked as Unavailable", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        calendarView = findViewById(R.id.calendarView);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtStatusLabel = findViewById(R.id.txtStatusLabel);
        btnMarkAvailable = findViewById(R.id.btnMarkAvailable);
        btnMarkUnavailable = findViewById(R.id.btnMarkUnavailable);
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        txtSelectedDate.setText(sdf.format(selectedCalendar.getTime()));
        
        // Mock logic: randomly assign status or keep as available
        txtStatusLabel.setText("Current Status: Available");
        txtStatusLabel.setTextColor(getResources().getColor(R.color.success_green));
    }
}