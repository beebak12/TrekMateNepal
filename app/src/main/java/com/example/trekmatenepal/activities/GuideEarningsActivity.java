package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.GuideEarningsAdapter;
import com.example.trekmatenepal.models.GuideEarningsModel;

import java.util.ArrayList;
import java.util.List;

public class GuideEarningsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerEarnings;
    private List<GuideEarningsModel> earningsList;
    private GuideEarningsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_earnings);

        btnBack = findViewById(R.id.btnBack);
        recyclerEarnings = findViewById(R.id.recyclerEarnings);

        btnBack.setOnClickListener(v -> finish());

        loadMockData();
        setupRecyclerView();
    }

    private void loadMockData() {
        earningsList = new ArrayList<>();
        earningsList.add(new GuideEarningsModel("Everest Base Camp", "20 May - 31 May", "Rs. 28,000", "Completed"));
        earningsList.add(new GuideEarningsModel("Annapurna Base Camp", "10 Apr - 20 Apr", "Rs. 18,500", "Completed"));
        earningsList.add(new GuideEarningsModel("Langtang Valley", "05 Mar - 12 Mar", "Rs. 22,000", "Completed"));
        earningsList.add(new GuideEarningsModel("Mardi Himal", "15 Feb - 22 Feb", "Rs. 17,100", "Completed"));
    }

    private void setupRecyclerView() {
        adapter = new GuideEarningsAdapter(earningsList);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));
        recyclerEarnings.setAdapter(adapter);
    }
}