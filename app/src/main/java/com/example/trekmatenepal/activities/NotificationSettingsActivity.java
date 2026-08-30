package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.SettingsRepository;
import com.example.trekmatenepal.models.UserSettingsModel;
import com.google.android.material.materialswitch.MaterialSwitch;

public class NotificationSettingsActivity extends AppCompatActivity {

    private SettingsRepository repository;
    private UserSettingsModel settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        repository = SettingsRepository.getInstance(this);
        settings = repository.getSettings();

        setupHeader();
        setupOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Notification Preferences");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupOptions() {
        setupSwitchRow(R.id.optPushNotify, R.drawable.ic_notification, "Push Notifications", "Receive notifications about your activities", settings.isPushNotifications(), (v, isChecked) -> {
            settings.setPushNotifications(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optTrekRequests, R.drawable.ic_people_purple, "Trek Requests", "Notify me when someone requests to join my trek", settings.isTrekRequests(), (v, isChecked) -> {
            settings.setTrekRequests(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optPartnerRequests, R.drawable.ic_person, "Partner Requests", "Notify me when someone wants to become a partner", settings.isPartnerRequests(), (v, isChecked) -> {
            settings.setPartnerRequests(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optBookingUpdates, R.drawable.ic_calendar_purple, "Booking Updates", "Get updates about trek and gear bookings", settings.isBookingUpdates(), (v, isChecked) -> {
            settings.setBookingUpdates(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optGearUpdates, R.drawable.ic_gear_purple, "Gear Rental Updates", "Receive updates about your rented gear", settings.isGearRentalUpdates(), (v, isChecked) -> {
            settings.setGearRentalUpdates(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optChatMessages, R.drawable.ic_chat, "Chat Messages", "Get notified about new messages", settings.isChatMessages(), (v, isChecked) -> {
            settings.setChatMessages(isChecked);
            repository.saveSettings(settings);
        });

        setupSwitchRow(R.id.optPromotional, R.drawable.ic_compose, "Promotional Notifications", "Receive offers and special updates", settings.isPromotionalNotifications(), (v, isChecked) -> {
            settings.setPromotionalNotifications(isChecked);
            repository.saveSettings(settings);
        });
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
}
