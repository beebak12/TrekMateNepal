package com.example.trekmatenepal.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.BookingCart;
import com.example.trekmatenepal.models.BookingModel;
import com.example.trekmatenepal.models.CartItemModel;
import com.example.trekmatenepal.models.RentalGearModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * BookingActivity — lets the user select rental dates, quantity, and pickup location.
 * Validates input then passes a BookingModel to BookingSummaryActivity.
 *
 * Backend note: when API is ready, replace the local BookingModel construction
 * with an API call and store the server-returned booking ID.
 */
public class BookingActivity extends AppCompatActivity {

    // ── Views ────────────────────────────────────────────────────────────────
    private ImageView btnBack, imgGear;
    private TextView txtGearName, txtGearPrice;
    private TextView txtStartDate, txtEndDate, txtTotalDays;
    private TextView tvQuantity, tvPricePerWeek, tvDuration, tvTotal;
    private Button btnMinusQty, btnPlusQty, btnContinue;
    private EditText etFullName, etContact, etPickupLocation, etNotes;

    // ── State ────────────────────────────────────────────────────────────────
    private RentalGearModel gear;
    private Calendar startCal;
    private Calendar endCal;
    private int quantity = 1;
    private static final int MAX_QTY = 10;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initializeViews();
        loadGearData();
        setupDateDefaults();
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack         = findViewById(R.id.btnBack);
        imgGear         = findViewById(R.id.imgGear);
        txtGearName     = findViewById(R.id.txtGearName);
        txtGearPrice    = findViewById(R.id.txtGearPrice);
        txtStartDate    = findViewById(R.id.txtStartDate);
        txtEndDate      = findViewById(R.id.txtEndDate);
        txtTotalDays    = findViewById(R.id.txtTotalDays);
        tvQuantity      = findViewById(R.id.tvQuantity);
        tvPricePerWeek  = findViewById(R.id.tvPricePerWeek);
        tvDuration      = findViewById(R.id.tvDuration);
        tvTotal         = findViewById(R.id.tvTotal);
        btnMinusQty     = findViewById(R.id.btnMinusQty);
        btnPlusQty      = findViewById(R.id.btnPlusQty);
        etFullName      = findViewById(R.id.etFullName);
        etContact       = findViewById(R.id.etContact);
        etPickupLocation= findViewById(R.id.etPickupLocation);
        etNotes         = findViewById(R.id.etNotes);
        btnContinue     = findViewById(R.id.btnContinue);
    }

    private void loadGearData() {
        gear = (RentalGearModel) getIntent().getSerializableExtra("gear");

        if (gear == null) {
            // Legacy fallback
            String name  = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("price");
            int    image = getIntent().getIntExtra("image", R.drawable.jacket);
            gear = new RentalGearModel(
                    image,
                    name  != null ? name  : "Gear Item",
                    "Trekking", "4.5",
                    price != null ? price : "Rs. 2,000 / week",
                    "Available", "Kathmandu",
                    "Trekking gear."
            );
        }

        int imageRes = gear.getImage();
        if (isValidDrawable(imageRes)) {
            imgGear.setImageResource(imageRes);
        } else {
            imgGear.setImageResource(R.drawable.jacket);
        }

        txtGearName.setText(gear.getName());
        txtGearPrice.setText(gear.getPrice());
    }

    private boolean isValidDrawable(int resourceId) {
        if (resourceId <= 0) return false;
        try {
            String type = getResources().getResourceTypeName(resourceId);
            return "drawable".equals(type) || "mipmap".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    private void setupDateDefaults() {
        startCal = Calendar.getInstance();
        endCal   = Calendar.getInstance();
        endCal.add(Calendar.DAY_OF_MONTH, 7);

        txtStartDate.setText(sdf.format(startCal.getTime()));
        txtEndDate.setText(sdf.format(endCal.getTime()));
        updateDurationAndPrice();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Date pickers — entire row and text are both tappable
        findViewById(R.id.layoutStartDate).setOnClickListener(v -> showStartDatePicker());
        txtStartDate.setOnClickListener(v -> showStartDatePicker());

        findViewById(R.id.layoutEndDate).setOnClickListener(v -> showEndDatePicker());
        txtEndDate.setOnClickListener(v -> showEndDatePicker());

        // Quantity
        btnMinusQty.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateDurationAndPrice();
            }
        });

        btnPlusQty.setOnClickListener(v -> {
            if (quantity < MAX_QTY) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
                updateDurationAndPrice();
            }
        });

        btnContinue.setOnClickListener(v -> validateAndContinue());
    }

    // ── Date pickers ─────────────────────────────────────────────────────────
    private void showStartDatePicker() {
        DatePickerDialog dlg = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    startCal.set(year, month, dayOfMonth);
                    txtStartDate.setText(sdf.format(startCal.getTime()));
                    // Make sure end is still after start
                    if (!endCal.after(startCal)) {
                        endCal = (Calendar) startCal.clone();
                        endCal.add(Calendar.DAY_OF_MONTH, 7);
                        txtEndDate.setText(sdf.format(endCal.getTime()));
                    }
                    updateDurationAndPrice();
                },
                startCal.get(Calendar.YEAR),
                startCal.get(Calendar.MONTH),
                startCal.get(Calendar.DAY_OF_MONTH));
        dlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dlg.show();
    }

    private void showEndDatePicker() {
        DatePickerDialog dlg = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar picked = Calendar.getInstance();
                    picked.set(year, month, dayOfMonth);
                    if (!picked.after(startCal)) {
                        Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    endCal = picked;
                    txtEndDate.setText(sdf.format(endCal.getTime()));
                    updateDurationAndPrice();
                },
                endCal.get(Calendar.YEAR),
                endCal.get(Calendar.MONTH),
                endCal.get(Calendar.DAY_OF_MONTH));
        // Minimum end date is the day after start
        Calendar minEnd = (Calendar) startCal.clone();
        minEnd.add(Calendar.DAY_OF_MONTH, 1);
        dlg.getDatePicker().setMinDate(minEnd.getTimeInMillis());
        dlg.show();
    }

    // ── Price calculation ─────────────────────────────────────────────────────
    private void updateDurationAndPrice() {
        long diffMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        int days = (int) (diffMillis / (1000 * 60 * 60 * 24));
        if (days < 1) days = 1;

        txtTotalDays.setText(days + " day" + (days == 1 ? "" : "s"));

        int pricePerWeekInt = 0;
        try {
            pricePerWeekInt = Integer.parseInt(gear.getPriceRaw().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ignored) {}

        int total = BookingModel.calculateTotal(pricePerWeekInt, quantity, days);
        int weeks = (int) Math.ceil(days / 7.0);
        if (weeks < 1) weeks = 1;

        if (tvPricePerWeek != null) tvPricePerWeek.setText("Rs. " + String.format("%,d", pricePerWeekInt));
        if (tvDuration     != null) tvDuration.setText(weeks + " week" + (weeks == 1 ? "" : "s") + " (" + days + " days)");
        if (tvQuantity     != null) tvQuantity.setText(String.valueOf(quantity));
        if (tvTotal        != null) tvTotal.setText("Rs. " + String.format("%,d", total));
    }

    // ── Validation and navigation ────────────────────────────────────────────
    private void validateAndContinue() {
        String fullName = etFullName.getText().toString().trim();
        String contact  = etContact.getText().toString().trim();
        String pickup   = etPickupLocation.getText().toString().trim();
        String notes    = etNotes != null ? etNotes.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }
        if (contact.isEmpty()) {
            etContact.setError("Contact number is required");
            etContact.requestFocus();
            return;
        }
        if (!endCal.after(startCal)) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pickup.isEmpty()) {
            etPickupLocation.setError("Pickup location is required");
            etPickupLocation.requestFocus();
            return;
        }

        // Split pickup address to find city (e.g. "Kathmandu, Thamel")
        String city = pickup.split(",")[0].trim();

        // Build booking object
        String startStr = sdf.format(startCal.getTime());
        String endStr   = sdf.format(endCal.getTime());
        String datesStr = startStr + " → " + endStr;
        String bookedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

        long diffMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        int days = (int) (diffMillis / (1000 * 60 * 60 * 24));
        if (days < 1) days = 1;

        int pricePerWeekInt = 0;
        try {
            pricePerWeekInt = Integer.parseInt(gear.getPriceRaw().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ignored) {}

        // Add this gear as a line in the booking cart.
        CartItemModel line = new CartItemModel(
                gear.getImage(), gear.getName(), gear.getSeller(), gear.getLocation(),
                pricePerWeekInt, quantity, days,
                datesStr, pickup, notes
        );

        BookingCart cart = BookingCart.get();
        boolean addingMore = cart.isAddingMore();
        if (addingMore) {
            cart.add(line);
            cart.setAddingMore(false);
        } else {
            cart.startNew(line);
        }

        // Set customer info globally for the whole booking session
        cart.setCustomer(fullName, contact, city, pickup, bookedDate);

        String bookingId = "BK-" + System.currentTimeMillis() % 100000;
        int total = cart.getTotal();

        BookingModel booking = new BookingModel(
                gear.getName(),
                startStr, endStr, datesStr,
                quantity, pickup, notes,
                String.valueOf(pricePerWeekInt),
                days, "Rs. " + String.format("%,d", total),
                bookingId, "Pending",
                gear.getImage(),
                fullName, contact, bookedDate
        );

        Intent intent = new Intent(this, BookingSummaryActivity.class);
        intent.putExtra("booking", booking);
        intent.putExtra("gear", gear);
        if (addingMore) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        if (addingMore) finish();
    }
}
