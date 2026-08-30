package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;

public class HelpInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_information);

        setupHeader();
        setupOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Help & Information");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupOptions() {
        setupRow(R.id.optHelpCenter, R.drawable.ic_info_purple, "Help Center", "Browse help articles", HelpCenterActivity.class);
        setupRow(R.id.optFaq, R.drawable.ic_chat, "Frequently Asked Questions", "Find answers to common questions", FAQActivity.class);
        setupRow(R.id.optContactSupport, R.drawable.ic_phone, "Contact Support", "Get in touch with our team", ContactSupportActivity.class);
        setupRow(R.id.optReportProblem, R.drawable.ic_post, "Report a Problem", "Report bugs or issues", ReportProblemActivity.class);
        setupRow(R.id.optTerms, R.drawable.ic_all, "Terms & Conditions", "Read our terms and conditions", TermsActivity.class);
        setupRow(R.id.optPrivacyPolicy, R.drawable.ic_lock, "Privacy Policy", "How we protect your data", PrivacyPolicyActivity.class);
        setupRow(R.id.optAbout, R.drawable.ic_info, "About TrekMate Nepal", "Learn more about our app", AboutActivity.class);
    }

    private void setupRow(int rowId, int iconRes, String title, String subtitle, Class<?> activityClass) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        row.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
    }
}
