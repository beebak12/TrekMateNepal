package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;

public class DataStorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_storage);

        setupHeader();
        setupLegend();
        setupOptions();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Data & Storage");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupLegend() {
        setupLegendItem(R.id.legendImages, R.color.purple_primary, "Images", "24 MB");
        setupLegendItem(R.id.legendCache, R.color.orange, "Cached Data", "18 MB");
        setupLegendItem(R.id.legendDownloaded, R.color.material_purple, "Downloaded", "35 MB");
    }

    private void setupLegendItem(int viewId, int colorRes, String label, String size) {
        View view = findViewById(viewId);
        view.findViewById(R.id.viewColor).setBackgroundColor(getResources().getColor(colorRes));
        ((TextView) view.findViewById(R.id.txtLabel)).setText(label);
        ((TextView) view.findViewById(R.id.txtSize)).setText(size);
    }

    private void setupOptions() {
        setupRow(R.id.optClearCache, R.drawable.ic_gear, "Clear Cache", "Free up temporary files", (v) -> {
            new AlertDialog.Builder(this)
                .setTitle("Clear Cache?")
                .setMessage("Temporary files will be removed.")
                .setPositiveButton("Clear Cache", (dialog, which) -> {
                    Toast.makeText(this, "Cache cleared successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        setupRow(R.id.optDownloadedTreks, R.drawable.ic_trek, "Downloaded Trek Data", "Manage offline trek information", (v) -> {
            startActivity(new Intent(this, DownloadedTreksActivity.class));
        });

        setupRow(R.id.optDataUsage, R.drawable.ic_all, "Data Usage", "View data usage statistics", (v) -> {
            startActivity(new Intent(this, DataUsageActivity.class));
        });
    }

    private void setupRow(int rowId, int iconRes, String title, String subtitle, View.OnClickListener listener) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.imgIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) row.findViewById(R.id.txtSubtitle)).setText(subtitle);
        row.setOnClickListener(listener);
    }
}
