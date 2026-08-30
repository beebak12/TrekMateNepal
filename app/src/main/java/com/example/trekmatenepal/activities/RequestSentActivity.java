package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;

/**
 * RequestSentActivity — success screen shown after a trek request is sent.
 * Shows partner name, destination, trek date and duration.
 * "Go to My Requests" → MyRequestsActivity
 * "Back to Partners"  → PartnerListActivity (clears the back stack partially)
 */
public class RequestSentActivity extends AppCompatActivity {

    private TextView tvRequestDesc;
    private TextView tvTrekDestination, tvTrekDate, tvTrekDuration;
    private Button   btnGoToRequests, btnBackToPartners;

    private PartnerModel partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sent);

        initViews();
        loadData();
        populateUI();
        setupClickListeners();
    }

    private void initViews() {
        tvRequestDesc      = findViewById(R.id.tvRequestDesc);
        tvTrekDestination  = findViewById(R.id.tvTrekDestination);
        tvTrekDate         = findViewById(R.id.tvTrekDate);
        tvTrekDuration     = findViewById(R.id.tvTrekDuration);
        btnGoToRequests    = findViewById(R.id.btnGoToRequests);
        btnBackToPartners  = findViewById(R.id.btnBackToPartners);
    }

    private void loadData() {
        partner = (PartnerModel) getIntent().getSerializableExtra("partner");
    }

    private void populateUI() {
        if (partner == null) return;

        // Description references partner's first name
        String firstName = partner.getName().split(" ")[0];
        tvRequestDesc.setText(
            "Your trek request has been sent to " + partner.getName() +
            ". You will be notified once they respond."
        );

        // Trek details from partner model
        tvTrekDestination.setText(partner.getDestination());
        tvTrekDate.setText(partner.getTrekDate());
        tvTrekDuration.setText(partner.getDuration());
    }

    private void setupClickListeners() {
        btnGoToRequests.setOnClickListener(v -> {
            startActivity(new Intent(this, MyRequestsActivity.class));
            finish();
        });

        btnBackToPartners.setOnClickListener(v -> {
            // Go back to PartnerListActivity — clear stack above it
            Intent intent = new Intent(this, PartnerListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to the send-request screen after sending
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
