package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.BookingModel;
import com.example.trekmatenepal.models.GuideModel;
import com.example.trekmatenepal.models.TrekModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * SelectDatesActivity — allows user to select trek dates, number of trekkers,
 * and add special requests. Calculates total cost dynamically.
 */
public class SelectDatesActivity extends AppCompatActivity {

    private GuideModel selectedGuide;
    private TrekModel selectedTrek;
    private Calendar startCal, endCal;
    private int numTrekkers = 2;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dates);

        initializeViews();
        getDataFromIntent();
        populateUI();
        setupClickListeners();

        // Initialize calendars to sample dates
        startCal = Calendar.getInstance();
        startCal.set(2025, Calendar.MAY, 20);
        endCal = Calendar.getInstance();
        endCal.set(2025, Calendar.MAY, 31);
    }

    private void initializeViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedGuide = (GuideModel) intent.getSerializableExtra("guide");
            selectedTrek = (TrekModel) intent.getSerializableExtra("trek");
        }
    }

    private void populateUI() {
        // Guide card
        if (selectedGuide != null) {
            TextView tvGuideName = findViewById(R.id.tvGuideName);
            if (tvGuideName != null) tvGuideName.setText(selectedGuide.getName());

            TextView tvGuidePrice = findViewById(R.id.tvGuidePrice);
            if (tvGuidePrice != null) tvGuidePrice.setText(selectedGuide.getDailyPrice());
        }

        // Trek info
        if (selectedTrek != null) {
            TextView tvTrekName = findViewById(R.id.tvTrekName);
            if (tvTrekName != null) tvTrekName.setText(selectedTrek.getTrekName());
        }

        updateDateDisplay();
        updateTotalCost();
    }

    private void setupClickListeners() {
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        if (tvStartDate != null) {
            tvStartDate.setOnClickListener(v -> showDatePicker(true));
        }

        TextView tvEndDate = findViewById(R.id.tvEndDate);
        if (tvEndDate != null) {
            tvEndDate.setOnClickListener(v -> showDatePicker(false));
        }

        ImageView btnDecrease = findViewById(R.id.btnDecrease);
        if (btnDecrease != null) {
            btnDecrease.setOnClickListener(v -> {
                if (numTrekkers > 1) {
                    numTrekkers--;
                    updateTrekkerCount();
                    updateTotalCost();
                }
            });
        }

        ImageView btnIncrease = findViewById(R.id.btnIncrease);
        if (btnIncrease != null) {
            btnIncrease.setOnClickListener(v -> {
                if (numTrekkers < 12) {
                    numTrekkers++;
                    updateTrekkerCount();
                    updateTotalCost();
                }
            });
        }

        Button btnContinue = findViewById(R.id.btnContinue);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> validateAndProceed());
        }
    }

    private void showDatePicker(boolean isStartDate) {
        // For now, just show a toast. In production, use DatePickerDialog
        Toast.makeText(this, "Date picker will open", Toast.LENGTH_SHORT).show();
    }

    private void updateTrekkerCount() {
        TextView tvTrekkerCount = findViewById(R.id.tvTrekkerCount);
        if (tvTrekkerCount != null) {
            tvTrekkerCount.setText(String.valueOf(numTrekkers));
        }
    }

    private void updateDateDisplay() {
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        if (tvStartDate != null) {
            tvStartDate.setText(dateFmt.format(startCal.getTime()));
        }

        TextView tvEndDate = findViewById(R.id.tvEndDate);
        if (tvEndDate != null) {
            tvEndDate.setText(dateFmt.format(endCal.getTime()));
        }
    }

    private void updateTotalCost() {
        long diffMs = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        int durationDays = (int) (diffMs / (1000 * 60 * 60 * 24)) + 1;

        // Extract price from guide's dailyPrice (e.g., "Rs. 2,500/day" -> 2500)
        int pricePerDay = extractPriceFromString(selectedGuide.getDailyPrice());
        int totalCost = pricePerDay * durationDays;

        TextView tvDurationDays = findViewById(R.id.tvDurationDays);
        if (tvDurationDays != null) {
            tvDurationDays.setText("Total (" + durationDays + " days)");
        }

        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        if (tvTotalAmount != null) {
            tvTotalAmount.setText("Rs. " + String.format("%,d", totalCost));
        }
    }

    private int extractPriceFromString(String priceStr) {
        // Extract number from "Rs. 2,500/day"
        if (priceStr == null) return 0;
        String cleanStr = priceStr.replaceAll("[^0-9]", "");
        try {
            return Integer.parseInt(cleanStr);
        } catch (Exception e) {
            return 0;
        }
    }

    private void validateAndProceed() {
        // Validation
        if (startCal.after(endCal)) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numTrekkers < 1 || numTrekkers > 12) {
            Toast.makeText(this, "Number of trekkers must be between 1 and 12", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create BookingModel
        long diffMs = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        int durationDays = (int) (diffMs / (1000 * 60 * 60 * 24)) + 1;

        int pricePerDay = extractPriceFromString(selectedGuide.getDailyPrice());
        int totalCost = pricePerDay * durationDays;

        EditText etSpecialRequests = findViewById(R.id.etSpecialRequests);
        String specialRequests = etSpecialRequests != null ? etSpecialRequests.getText().toString() : "";

        BookingModel booking = new BookingModel(
                selectedTrek.getTrekName(),
                dateFmt.format(startCal.getTime()),
                dateFmt.format(endCal.getTime()),
                dateFmt.format(startCal.getTime()) + " - " + dateFmt.format(endCal.getTime()),
                numTrekkers,
                "Kathmandu",
                specialRequests,
                String.valueOf(pricePerDay),
                durationDays,
                "Rs. " + String.format("%,d", totalCost),
                "BK-" + System.currentTimeMillis(),
                "Pending",
                selectedTrek.getImage()
        );

        // Navigate to BookingSummaryActivity
        Intent intent = new Intent(this, BookingSummaryActivity.class);
        intent.putExtra("booking", booking);
        intent.putExtra("guide", selectedGuide);
        intent.putExtra("trek", selectedTrek);
        startActivity(intent);
    }
}
