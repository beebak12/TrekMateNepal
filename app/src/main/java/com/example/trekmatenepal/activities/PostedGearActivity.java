package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GearAdapter;
import com.example.trekmatenepal.models.GearModel;

import java.util.ArrayList;
import java.util.List;

public class PostedGearActivity extends AppCompatActivity {

    RecyclerView recyclerGear;
    GearAdapter gearAdapter;
    List<GearModel> gearList;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_gear);

        recyclerGear = findViewById(R.id.recyclerGear);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        gearList = new ArrayList<>();

        gearList.add(new GearModel(
                "The North Face Down Jacket",
                "Rs. 500",
                "/ day",
                R.drawable.jacket
        ));

        gearList.add(new GearModel(
                "Osprey Atmos 50 Backpack",
                "Rs. 300",
                "/ day",
                R.drawable.backpack
        ));

        gearList.add(new GearModel(
                "Trekking Boots (High Ankle)",
                "Rs. 400",
                "/ day",
                R.drawable.boots
        ));

        gearList.add(new GearModel(
                "Sleeping Bag (-10°C)",
                "Rs. 350",
                "/ day",
                R.drawable.sleepingbag
        ));

        recyclerGear.setLayoutManager(
                new LinearLayoutManager(this)
        );

        gearAdapter = new GearAdapter(gearList);
        recyclerGear.setAdapter(gearAdapter);
    }
}