package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.MyPostedGearAdapter;
import com.example.trekmatenepal.data.GearBackendRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

public class MyPostedGearsActivity extends AppCompatActivity {
    private final ArrayList<RentalGearModel> myGear = new ArrayList<>();
    private MyPostedGearAdapter adapter;
    private RecyclerView recycler;
    private View empty;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_gears);
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("My Posted Gear");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddGear).setOnClickListener(v -> startActivity(new Intent(this, PostGearActivity.class)));
        recycler = findViewById(R.id.recyclerPostedGear);
        empty = findViewById(R.id.tvEmptyPostedGear);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyPostedGearAdapter(myGear, new MyPostedGearAdapter.Actions() {
            @Override public void edit(RentalGearModel gear) { openEditor(gear); }
            @Override public void changeStatus(RentalGearModel gear) { chooseStatus(gear); }
            @Override public void delete(RentalGearModel gear) { confirmDelete(gear); }
            @Override public void open(RentalGearModel gear) { openDetails(gear); }
        });
        recycler.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        GearBackendRepository.getMine(this, new GearBackendRepository.ListCallback() {
            @Override public void success(ArrayList<RentalGearModel> gear) {
                myGear.clear(); myGear.addAll(gear); adapter.notifyDataSetChanged(); updateEmpty();
            }
            @Override public void error(String message) {
                android.widget.Toast.makeText(MyPostedGearsActivity.this, message,
                        android.widget.Toast.LENGTH_LONG).show(); updateEmpty();
            }
        });
    }

    private void updateEmpty() {
        empty.setVisibility(myGear.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(myGear.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void openEditor(RentalGearModel gear) {
        Intent intent = new Intent(this, PostGearActivity.class);
        intent.putExtra(PostGearActivity.EXTRA_EDIT_GEAR, gear);
        startActivity(intent);
    }

    private void openDetails(RentalGearModel gear) {
        Intent intent = new Intent(this, GearDetailActivity.class);
        intent.putExtra("gear", gear);
        startActivity(intent);
    }

    private void chooseStatus(RentalGearModel gear) {
        String[] statuses = {"Available", "Booked", "Unavailable"};
        new AlertDialog.Builder(this).setTitle("Update availability")
                .setSingleChoiceItems(statuses, statusIndex(gear.getAvailability()), null)
                .setPositiveButton("Update", (dialog, which) -> {
                    int selected = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    if (selected >= 0) {
                        gear.setAvailability(statuses[selected]);
                        GearBackendRepository.updateStatus(this, gear, action("Availability updated."));
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private int statusIndex(String status) {
        if ("Booked".equalsIgnoreCase(status)) return 1;
        if ("Unavailable".equalsIgnoreCase(status)) return 2;
        return 0;
    }

    private void confirmDelete(RentalGearModel gear) {
        new AlertDialog.Builder(this).setTitle("Delete gear?")
                .setMessage("This will remove " + gear.getName() + " from your posted gear.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    GearBackendRepository.delete(this, gear, action("Gear deleted."));
                }).setNegativeButton("Cancel", null).show();
    }

    private GearBackendRepository.ActionCallback action(String successMessage) {
        return new GearBackendRepository.ActionCallback() {
            @Override public void success(RentalGearModel gear) {
                android.widget.Toast.makeText(MyPostedGearsActivity.this, successMessage,
                        android.widget.Toast.LENGTH_SHORT).show(); reload();
            }
            @Override public void error(String message) {
                android.widget.Toast.makeText(MyPostedGearsActivity.this, message,
                        android.widget.Toast.LENGTH_LONG).show(); reload();
            }
        };
    }
}
