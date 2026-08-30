package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.SettingsRepository;
import com.example.trekmatenepal.models.UserSettingsModel;
import com.google.android.material.materialswitch.MaterialSwitch;

public class PrivacySettingsActivity extends AppCompatActivity {

    private SettingsRepository repository;
    private UserSettingsModel settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        repository = SettingsRepository.getInstance(this);
        settings = repository.getSettings();

        setupHeader();
        setupOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Privacy Settings");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupOptions() {
        // Profile Visibility
        setupSelectionRow(R.id.optProfileVisibility, R.drawable.ic_person, "Profile Visibility", "Who can see your profile", settings.getProfileVisibility(), (v) -> {
            showSelectionDialog("Profile Visibility", new String[]{"Everyone", "Trek Partners Only", "Private"}, settings.getProfileVisibility(), (selected) -> {
                settings.setProfileVisibility(selected);
                repository.saveSettings(settings);
                updateRowValue(R.id.optProfileVisibility, selected);
            });
        });

        // Contact Info
        setupSwitchRow(R.id.optShowPhone, R.drawable.ic_phone, "Show Contact Number", "Allow others to see your number", settings.isShowContactNumber(), (v, isChecked) -> {
            settings.setShowContactNumber(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optShowEmail, R.drawable.ic_email, "Show Email Address", "Allow others to see your email", settings.isShowEmailAddress(), (v, isChecked) -> {
            settings.setShowEmailAddress(isChecked);
            repository.saveSettings(settings);
        });

        // Messaging & Requests
        setupSelectionRow(R.id.optWhoCanMessage, R.drawable.ic_chat, "Who Can Message Me", "Choose who can send you messages", settings.getWhoCanMessage(), (v) -> {
            showSelectionDialog("Who Can Message Me", new String[]{"Everyone", "My Trek Partners", "Nobody"}, settings.getWhoCanMessage(), (selected) -> {
                settings.setWhoCanMessage(selected);
                repository.saveSettings(settings);
                updateRowValue(R.id.optWhoCanMessage, selected);
            });
        });

        setupSelectionRow(R.id.optWhoCanSendRequests, R.drawable.ic_people_purple, "Who Can Send Trek Requests", "Choose who can send you requests", settings.getWhoCanSendTrekRequests(), (v) -> {
            showSelectionDialog("Who Can Send Trek Requests", new String[]{"Everyone", "Users on My Treks", "Nobody"}, settings.getWhoCanSendTrekRequests(), (selected) -> {
                settings.setWhoCanSendTrekRequests(selected);
                repository.saveSettings(settings);
                updateRowValue(R.id.optWhoCanSendRequests, selected);
            });
        });

        // Online Status
        setupSwitchRow(R.id.optShowOnline, R.drawable.ic_online_dot, "Show Online Status", "Let others see when you are online", settings.isShowOnlineStatus(), (v, isChecked) -> {
            settings.setShowOnlineStatus(isChecked);
            repository.saveSettings(settings);
        });

        // Blocked Users
        setupRow(R.id.optBlockedUsers, R.drawable.ic_cancel, "Blocked Users", "Manage your blocked users", BlockedUsersActivity.class);
    }

    private void setupSelectionRow(int rowId, int iconRes, String title, String subtitle, String value, View.OnClickListener listener) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        
        TextView tvValue = row.findViewById(R.id.txtValue);
        tvValue.setVisibility(View.VISIBLE);
        tvValue.setText(value);
        
        row.setOnClickListener(listener);
    }

    private void updateRowValue(int rowId, String value) {
        View row = findViewById(rowId);
        ((TextView) row.findViewById(R.id.txtValue)).setText(value);
    }

    private void setupSwitchRow(int rowId, int iconRes, String title, String subtitle, boolean isChecked, MaterialSwitch.OnCheckedChangeListener listener) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        
        row.findViewById(R.id.imgChevron).setVisibility(View.GONE);
        MaterialSwitch sw = row.findViewById(R.id.switchControl);
        sw.setVisibility(View.VISIBLE);
        sw.setChecked(isChecked);
        sw.setOnCheckedChangeListener(listener);
    }

    private void setupRow(int rowId, int iconRes, String title, String subtitle, Class<?> activityClass) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        row.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
    }

    private void showSelectionDialog(String title, String[] options, String currentValue, SelectionCallback callback) {
        int checkedItem = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(currentValue)) {
                checkedItem = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                callback.onSelected(options[which]);
                dialog.dismiss();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private interface SelectionCallback {
        void onSelected(String selected);
    }
}
