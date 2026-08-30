package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GearAdapter;
import com.example.trekmatenepal.models.Gear;

import java.util.ArrayList;
import java.util.List;

public class PostedGearActivity extends AppCompatActivity {

    RecyclerView recyclerGear;
    GearAdapter gearAdapter;
    List<Gear> gearList;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_gear);

        recyclerGear = findViewById(R.id.recyclerGear);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        gearList = new ArrayList<>();

        gearList.add(new Gear(
                R.drawable.jacket,
                "The North Face Down Jacket",
                "Rs. 500 / day",
                "Available",
                "Pokhara, Nepal",
                "",
                ""
        ));

        gearList.add(new Gear(
                R.drawable.backpack,
                "Osprey Atmos 50 Backpack",
                "Rs. 300 / day",
                "Booked",
                "Pokhara, Nepal",
                "10 May 2024",
                "20 May 2024"
        ));

        gearList.add(new Gear(
                R.drawable.boots,
                "Trekking Boots (High Ankle)",
                "Rs. 400 / day",
                "Available",
                "Pokhara, Nepal",
                "",
                ""
        ));

        gearList.add(new Gear(
                R.drawable.sleepingbag,
                "Sleeping Bag (-10°C)",
                "Rs. 350 / day",
                "Booked",
                "Pokhara, Nepal",
                "01 Jun 2024",
                "05 Jun 2024"
        ));

        recyclerGear.setLayoutManager(
                new LinearLayoutManager(this)
        );

        gearAdapter = new GearAdapter(gearList);
        recyclerGear.setAdapter(gearAdapter);
    }
}