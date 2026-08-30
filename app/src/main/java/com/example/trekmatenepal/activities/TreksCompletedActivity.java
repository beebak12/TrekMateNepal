package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.TrekAdapter;
import com.example.trekmatenepal.models.TrekModel;

import java.util.ArrayList;
import java.util.List;

public class TreksCompletedActivity extends AppCompatActivity {

    RecyclerView recyclerTreks;
    TrekAdapter trekAdapter;
    List<TrekModel> trekList;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treks_completed);

        recyclerTreks = findViewById(R.id.recyclerTreks);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        trekList = new ArrayList<>();

        trekList.add(new TrekModel(
                R.drawable.everest,
                "Everest Base Camp Trek",
                "Solukhumbu, Nepal",
                "20 Oct - 03 Nov 2023",
                "Completed"
        ));

        trekList.add(new TrekModel(
                R.drawable.annapurna,
                "Annapurna Base Camp Trek",
                "Kaski, Nepal",
                "12 Apr - 19 Apr 2023",
                "Completed"
        ));

        trekList.add(new TrekModel(
                R.drawable.langtang,
                "Langtang Valley Trek",
                "Rasuwa, Nepal",
                "05 Oct - 12 Oct 2022",
                "Completed"
        ));

        trekList.add(new TrekModel(
                R.drawable.mardihimal,
                "Mardi Himal Trek",
                "Kaski, Nepal",
                "18 Mar - 24 Mar 2022",
                "Completed"
        ));

        trekList.add(new TrekModel(
                R.drawable.ghorepani,
                "Ghorepani Poon Hill Trek",
                "Myagdi, Nepal",
                "08 Jan - 13 Jan 2022",
                "Completed"
        ));

        recyclerTreks.setLayoutManager(
                new LinearLayoutManager(this)
        );

        trekAdapter = new TrekAdapter(trekList);
        recyclerTreks.setAdapter(trekAdapter);
    }
}