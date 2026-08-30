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
import com.example.trekmatenepal.models.PartnerModel;

/**
 * SendTrekRequestActivity — compose a request message to a trek partner.
 * Pre-fills a sample message; the user can edit it.
 * Tapping "Send Request" → RequestSentActivity.
 */
public class SendTrekRequestActivity extends AppCompatActivity {

    private ImageView btnBack, imgPartnerTo;
    private TextView  tvPartnerName;
    private EditText  etMessage;
    private Button    btnSendRequest;

    private PartnerModel partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_trek_request);

        initViews();
        loadPartner();
        populateUI();
        setupClickListeners();
    }

    private void initViews() {
        btnBack        = findViewById(R.id.btnBack);
        imgPartnerTo   = findViewById(R.id.imgPartnerTo);
        tvPartnerName  = findViewById(R.id.tvPartnerName);
        etMessage      = findViewById(R.id.etMessage);
        btnSendRequest = findViewById(R.id.btnSendRequest);
    }

    private void loadPartner() {
        partner = (PartnerModel) getIntent().getSerializableExtra("partner");
        if (partner == null) finish();
    }

    private void populateUI() {
        if (partner == null) return;

        // Partner "To" section
        imgPartnerTo.setImageResource(partner.getImage() != 0
                ? partner.getImage() : R.drawable.partner1);
        tvPartnerName.setText(partner.getName());

        // Pre-fill message
        String firstName = partner.getName().split(" ")[0];
        etMessage.setText(
            "Hi " + firstName + ",\n\n" +
            "I'm also planning to do " + partner.getDestination() +
            " on the same dates. I'd love to join your group if you don't mind.\n\n" +
            "Let's make this an amazing adventure together! 😊"
        );
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSendRequest.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to RequestSentActivity — pass partner data so
            // the confirmation screen shows trek details correctly
            Intent intent = new Intent(this, RequestSentActivity.class);
            intent.putExtra("partner", partner);
            intent.putExtra("message", message);
            startActivity(intent);
            finish(); // don't allow going back to the compose screen after sending
        });
    }
}
