package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.BookingCart;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.BookingModel;

/**
 * PaymentMethodActivity — handles payment selection (COD, Bank, eSewa, Khalti)
 * and payment proof submission.
 */
public class PaymentMethodActivity extends AppCompatActivity {

    private RadioGroup rgPaymentOptions;
    private View detailsCard, proofUploadSection;
    private TextView tvPaymentTitle, tvPaymentDetails;
    private EditText etTransactionCode;
    private ImageView imgProof;
    private Button btnSubmit;

    private BookingModel booking;
    private String selectedMethod = "";
    private Uri selectedImageUri = null;

    // Registers a photo picker activity launcher in single-select mode.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    selectedImageUri = uri;
                    imgProof.setImageURI(uri);
                    imgProof.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgProof.setColorFilter(null); // Remove any placeholder tints
                    Toast.makeText(this, "Screenshot selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        booking = (BookingModel) getIntent().getSerializableExtra("booking");

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        rgPaymentOptions   = findViewById(R.id.rgPaymentOptions);
        detailsCard        = findViewById(R.id.detailsCard);
        proofUploadSection = findViewById(R.id.proofUploadSection);
        tvPaymentTitle     = findViewById(R.id.tvPaymentTitle);
        tvPaymentDetails   = findViewById(R.id.tvPaymentDetails);
        etTransactionCode  = findViewById(R.id.etTransactionCode);
        imgProof           = findViewById(R.id.imgProof);
        btnSubmit          = findViewById(R.id.btnSubmit);
    }

    private void setupListeners() {
        rgPaymentOptions.setOnCheckedChangeListener((group, checkedId) -> {
            detailsCard.setVisibility(View.VISIBLE);
            
            if (checkedId == R.id.rbCOD) {
                selectedMethod = "Cash on Delivery";
                proofUploadSection.setVisibility(View.GONE);
                tvPaymentTitle.setText("Cash on Delivery");
                tvPaymentDetails.setText("You can pay in cash when the gear is delivered to your address.");
            } else {
                proofUploadSection.setVisibility(View.VISIBLE);
                if (checkedId == R.id.rbBank) {
                    selectedMethod = "Bank Transfer";
                    tvPaymentTitle.setText("Bank Details");
                    tvPaymentDetails.setText("Bank Name: Nabil Bank\nAccount Name: TrekMate Nepal Pvt Ltd\nAccount Number: 0123456789012");
                } else if (checkedId == R.id.rbEsewa) {
                    selectedMethod = "eSewa";
                    tvPaymentTitle.setText("eSewa Details");
                    tvPaymentDetails.setText("eSewa ID: 9801234567\nName: TrekMate Nepal");
                } else if (checkedId == R.id.rbKhalti) {
                    selectedMethod = "Khalti";
                    tvPaymentTitle.setText("Khalti Details");
                    tvPaymentDetails.setText("Khalti ID: 9801234567\nName: TrekMate Nepal");
                }
            }
        });

        findViewById(R.id.layoutUploadProof).setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        if (selectedMethod.isEmpty()) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!selectedMethod.equals("Cash on Delivery")) {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please upload a payment screenshot", Toast.LENGTH_SHORT).show();
                return;
            }
            String code = etTransactionCode.getText().toString().trim();
            if (code.isEmpty()) {
                etTransactionCode.setError("Required for verification");
                return;
            }
        }

        processBooking();
    }

    private void processBooking() {
        if (booking == null) return;

        BookingCart cart = BookingCart.get();
        int total = cart.getTotal();

        // Update booking status
        booking.setStatus("Confirmed");

        // Notify sellers and trekker
        NotificationRepository.notifyRentalConfirmed(
                this, cart.getItems(), booking.getBookingId(), SessionUser.getUserId(this), total);

        cart.clear();

        // Show toast as requested
        Toast.makeText(this, "Booking has been proceed and user will be notified for delivery", Toast.LENGTH_LONG).show();

        // Proceed to booking confirmed page
        Intent intent = new Intent(this, BookingConfirmationActivity.class);
        intent.putExtra("booking", booking);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
