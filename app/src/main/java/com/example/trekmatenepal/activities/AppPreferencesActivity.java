package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.SettingsRepository;
import com.example.trekmatenepal.models.UserSettingsModel;
import com.google.android.material.materialswitch.MaterialSwitch;

public class AppPreferencesActivity extends AppCompatActivity {

    private SettingsRepository repository;
    private UserSettingsModel settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_preferences);

        repository = SettingsRepository.getInstance(this);
        settings = repository.getSettings();

        setupHeader();
        setupOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("App Preferences");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupOptions() {
        // Language
        setupSelectionRow(R.id.optLanguage, R.drawable.ic_all, "Language", "Choose your preferred language", settings.getLanguage(), (v) -> {
            showSelectionDialog("Language", new String[]{"English", "Nepali"}, settings.getLanguage(), (selected) -> {
                settings.setLanguage(selected);
                repository.saveSettings(settings);
                updateRowValue(R.id.optLanguage, selected);
                Toast.makeText(this, "Language changed to " + selected, Toast.LENGTH_SHORT).show();
            });
        });

        // Appearance
        setupSelectionRow(R.id.optAppearance, R.drawable.ic_star, "Dark Mode", "Choose app appearance", settings.getAppearance(), (v) -> {
            showSelectionDialog("Appearance", new String[]{"System Default", "Light", "Dark"}, settings.getAppearance(), (selected) -> {
                settings.setAppearance(selected);
                repository.saveSettings(settings);
                updateRowValue(R.id.optAppearance, selected);
            });
        });

        // Location Services
        setupSwitchRow(R.id.optLocation, R.drawable.ic_location, "Location Services", "Used for location-based features", settings.isLocationServices(), (v, isChecked) -> {
            settings.setLocationServices(isChecked);
            repository.saveSettings(settings);
        });

        // Auto-play
        setupSwitchRow(R.id.optAutoPlay, R.drawable.ic_post, "Auto-play Images", "Automatically play images in feeds", settings.isAutoPlayImages(), (v, isChecked) -> {
            settings.setAutoPlayImages(isChecked);
            repository.saveSettings(settings);
        });

        // Clear Search History
        setupClickableRow(R.id.optClearHistory, R.drawable.ic_cancel, "Clear Search History", "Remove your search history", (v) -> {
            new AlertDialog.Builder(this)
                .setTitle("Clear Search History?")
                .setMessage("Your recent searches will be removed.")
                .setPositiveButton("Clear", (dialog, which) -> {
                    Toast.makeText(this, "Search history cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
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

    private void setupClickableRow(int rowId, int iconRes, String title, String subtitle, View.OnClickListener listener) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        row.setOnClickListener(listener);
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
