package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupHeader();
        setupMenuOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupMenuOptions() {
        // Account & Security
        setupRow(R.id.optChangePassword, R.drawable.ic_lock, "Change Password", "Update your account password", ChangePasswordActivity.class);
        setupRow(R.id.optLoginSecurity, R.drawable.ic_check_circle, "Login & Security", "Manage your security", LoginSecurityActivity.class);

        // Notifications
        setupRow(R.id.optNotificationPrefs, R.drawable.ic_notification, "Notification Preferences", "Manage your notifications", NotificationSettingsActivity.class);

        // Privacy
        setupRow(R.id.optPrivacySettings, R.drawable.ic_person, "Privacy Settings", "Control your privacy", PrivacySettingsActivity.class);

        // App Preferences
        setupRow(R.id.optAppPrefs, R.drawable.ic_settings_purple, "App Preferences", "Customize your app experience", AppPreferencesActivity.class);

        // Data & Storage
        setupRow(R.id.optDataStorage, R.drawable.ic_gear_purple, "Data & Storage", "Manage cache and storage", DataStorageActivity.class);

        // Help & Information
        setupRow(R.id.optHelpInfo, R.drawable.ic_info_purple, "Help & Information", "Get help and support", HelpInformationActivity.class);
    }

    private void setupRow(int viewId, int iconRes, String title, String subtitle, Class<?> activityClass) {
        View row = findViewById(viewId);
        ImageView imgIcon = row.findViewById(R.id.imgIcon);
        TextView txtTitle = row.findViewById(R.id.txtTitle);
        TextView txtSubtitle = row.findViewById(R.id.txtSubtitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);

        row.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
    }
}
