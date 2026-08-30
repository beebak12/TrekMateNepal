package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PartnerListAdapter;
import com.example.trekmatenepal.models.PartnerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * PartnerListActivity — shows the filtered list of trek partners.
 * Tapping "View Profile" opens PartnerProfileActivity with the selected partner.
 */
public class PartnerListActivity extends AppCompatActivity {

    private ImageView      btnBack;
    private RecyclerView   recyclerPartners;
    private TextView       tvPartnerCount, tvEmpty;
    private PartnerListAdapter adapter;

    private List<PartnerModel> allPartners  = new ArrayList<>();
    private List<PartnerModel> showPartners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_list);

        btnBack          = findViewById(R.id.btnBack);
        recyclerPartners = findViewById(R.id.recyclerPartners);
        tvPartnerCount   = findViewById(R.id.tvPartnerCount);
        tvEmpty          = findViewById(R.id.tvEmpty);

        btnBack.setOnClickListener(v -> finish());

        loadPartners();
        applyIncomingFilters();
        setupRecycler();
    }

    // ── 8 realistic sample partners ─────────────────────────────────────────
    private void loadPartners() {
        allPartners.add(new PartnerModel(
            "Sujan Karki", "4.8", "(24)", "Available",
            R.drawable.partner1,
            "Pokhara, Nepal",
            "Everest Base Camp",
            "20 Apr – 2 May",
            "12 Days",
            "28 Years", "8+", "15",
            "Adventure lover and nature enthusiast. Planning to do EBC trek this April. Looking for friendly trekking partners.",
            "Photography,Camping,Nature,Adventure",
            "3 – 5 People",
            R.drawable.everest,
            true
        ));

        allPartners.add(new PartnerModel(
            "Anita Gurung", "4.9", "(18)", "Available",
            R.drawable.partner2,
            "Kathmandu, Nepal",
            "Annapurna Base Camp",
            "10 May – 17 May",
            "7 Days",
            "25 Years", "5+", "8",
            "Solo female trekker looking for a safe and friendly group for ABC.",
            "Photography,Nature,Yoga",
            "2 – 4 People",
            R.drawable.annapurna,
            true
        ));

        allPartners.add(new PartnerModel(
            "Ramesh Bhandari", "4.7", "(31)", "Available",
            R.drawable.partner3,
            "Lalitpur, Nepal",
            "Langtang Valley",
            "5 Apr – 12 Apr",
            "8 Days",
            "32 Years", "10+", "20",
            "Experienced trekker. Have completed EBC and Annapurna. Now heading to Langtang.",
            "Nature,Camping,History",
            "4 – 6 People",
            R.drawable.langtang,
            false
        ));

        allPartners.add(new PartnerModel(
            "Prakriti Thapa", "4.6", "(12)", "Available",
            R.drawable.partner4,
            "Pokhara, Nepal",
            "Mardi Himal Trek",
            "15 Apr – 19 Apr",
            "5 Days",
            "23 Years", "3+", "6",
            "First time doing Mardi Himal. Looking for experienced partners for guidance.",
            "Photography,Adventure,Nature",
            "2 – 3 People",
            R.drawable.mardihimal,
            true
        ));

        allPartners.add(new PartnerModel(
            "Dipesh Rai", "4.8", "(22)", "Available",
            R.drawable.partner1,
            "Bhaktapur, Nepal",
            "Manaslu Circuit",
            "1 May – 15 May",
            "14 Days",
            "30 Years", "12+", "25",
            "Seasoned trekker and part-time guide. Manaslu is my favourite route.",
            "Camping,Trekking,Photography",
            "3 – 5 People",
            R.drawable.mardihimal,
            false
        ));

        allPartners.add(new PartnerModel(
            "Sita Shrestha", "4.9", "(15)", "Available",
            R.drawable.partner2,
            "Pokhara, Nepal",
            "Everest Base Camp",
            "25 Apr – 7 May",
            "12 Days",
            "27 Years", "6+", "11",
            "Nature and adventure lover. EBC has always been my dream trek.",
            "Nature,Adventure,Camping",
            "2 – 5 People",
            R.drawable.everest,
            true
        ));

        allPartners.add(new PartnerModel(
            "Bikash Tamang", "4.5", "(8)", "Available",
            R.drawable.partner3,
            "Chitwan, Nepal",
            "Langtang Valley",
            "3 Jun – 10 Jun",
            "8 Days",
            "26 Years", "4+", "7",
            "Wildlife enthusiast and part-time photographer. Looking for like-minded trekkers.",
            "Photography,Wildlife,Nature",
            "2 – 4 People",
            R.drawable.langtang,
            true
        ));

        allPartners.add(new PartnerModel(
            "Nima Sherpa", "5.0", "(40)", "Available",
            R.drawable.partner4,
            "Solukhumbu, Nepal",
            "Everest Base Camp",
            "15 Apr – 29 Apr",
            "14 Days",
            "35 Years", "20+", "50",
            "Professional high-altitude trekking guide. Born and raised in Khumbu Valley.",
            "Climbing,Camping,Photography,Nature",
            "1 – 8 People",
            R.drawable.everest,
            true
        ));
    }

    // ── Apply filters passed from PartnerFinderActivity ───────────────────────
    private void applyIncomingFilters() {
        String destination  = getIntent().getStringExtra("destination");
        String searchQuery  = getIntent().getStringExtra("searchQuery");

        showPartners.clear();
        for (PartnerModel p : allPartners) {
            boolean matchDest = destination == null
                    || destination.equals("All")
                    || p.getDestination().toLowerCase().contains(destination.toLowerCase());

            boolean matchSearch = searchQuery == null || searchQuery.isEmpty()
                    || p.getName().toLowerCase().contains(searchQuery.toLowerCase())
                    || p.getDestination().toLowerCase().contains(searchQuery.toLowerCase())
                    || p.getLocation().toLowerCase().contains(searchQuery.toLowerCase());

            if (matchDest && matchSearch) showPartners.add(p);
        }

        if (showPartners.isEmpty()) showPartners.addAll(allPartners); // fallback: show all

        tvPartnerCount.setText(showPartners.size() + " partners found");
    }

    private void setupRecycler() {
        adapter = new PartnerListAdapter(this, showPartners, partner -> {
            Intent intent = new Intent(this, PartnerProfileActivity.class);
            intent.putExtra("partner", partner);
            startActivity(intent);
        });
        recyclerPartners.setLayoutManager(new LinearLayoutManager(this));
        recyclerPartners.setAdapter(adapter);

        if (showPartners.isEmpty()) {
            tvEmpty.setVisibility(android.view.View.VISIBLE);
            recyclerPartners.setVisibility(android.view.View.GONE);
        } else {
            tvEmpty.setVisibility(android.view.View.GONE);
            recyclerPartners.setVisibility(android.view.View.VISIBLE);
        }
    }
}
