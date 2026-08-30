package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PartnerListAdapter;
import com.example.trekmatenepal.models.PartnerModel;

import java.util.ArrayList;
import java.util.List;

public class PartnerFinderActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout tabFindPartners, tabRequests, tabMyRequests;
    private TextView tvTabFindPartners, tvTabRequests, tvTabMyRequests;
    private View indicatorFindPartners, indicatorRequests, indicatorMyRequests;
    private LinearLayout contentFindPartners, contentRequests, contentMyRequests;

    private EditText etSearch;
    private TextView chipEBC, chipABC, chipLangtang, chipMardi, chipManaslu;
    private TextView tvFilterDestination, tvFilterDate, tvFilterDuration, tvFilterGender, tvFilterGroup;
    private LinearLayout layoutResults;
    private Button btnApplyFilters;

    private RecyclerView recyclerPartners, recyclerRequests, recyclerMyRequests;
    private TextView tvEmptyResults;

    private String selectedDestination = "All";
    private String selectedDate        = "Anytime";
    private String selectedDuration    = "Any";
    private String selectedGender      = "Any";
    private String selectedGroupType   = "Any";

    private List<PartnerModel> allPartners = new ArrayList<>();
    private List<PartnerModel> showPartners = new ArrayList<>();
    private PartnerListAdapter partnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_finder);

        initViews();
        setupTabs();
        setupChips();
        setupFilterRows();
        loadInitialData();
        setupRecyclerViews();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        findViewById(R.id.btnChat).setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("open_chat", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        
        tabFindPartners = findViewById(R.id.tabFindPartners);
        tabRequests = findViewById(R.id.tabRequests);
        tabMyRequests = findViewById(R.id.tabMyRequests);
        
        tvTabFindPartners = findViewById(R.id.tvTabFindPartners);
        tvTabRequests = findViewById(R.id.tvTabRequests);
        tvTabMyRequests = findViewById(R.id.tvTabMyRequests);
        
        indicatorFindPartners = findViewById(R.id.indicatorFindPartners);
        indicatorRequests = findViewById(R.id.indicatorRequests);
        indicatorMyRequests = findViewById(R.id.indicatorMyRequests);
        
        contentFindPartners = findViewById(R.id.contentFindPartners);
        contentRequests = findViewById(R.id.contentRequests);
        contentMyRequests = findViewById(R.id.contentMyRequests);

        etSearch = findViewById(R.id.etSearch);
        
        chipEBC = findViewById(R.id.chipEBC);
        chipABC = findViewById(R.id.chipABC);
        chipLangtang = findViewById(R.id.chipLangtang);
        chipMardi = findViewById(R.id.chipMardi);
        chipManaslu = findViewById(R.id.chipManaslu);

        tvFilterDestination = findViewById(R.id.tvFilterDestination);
        tvFilterDate = findViewById(R.id.tvFilterDate);
        tvFilterDuration = findViewById(R.id.tvFilterDuration);
        tvFilterGender = findViewById(R.id.tvFilterGender);
        tvFilterGroup = findViewById(R.id.tvFilterGroup);

        btnApplyFilters = findViewById(R.id.btnApplyFilters);
        layoutResults = findViewById(R.id.layoutResults);
        
        recyclerPartners = findViewById(R.id.recyclerPartners);
        recyclerRequests = findViewById(R.id.recyclerRequests);
        recyclerMyRequests = findViewById(R.id.recyclerMyRequests);
        tvEmptyResults = findViewById(R.id.tvEmptyResults);
    }

    private void setupTabs() {
        tabFindPartners.setOnClickListener(v -> switchTab(1));
        tabRequests.setOnClickListener(v -> switchTab(2));
        tabMyRequests.setOnClickListener(v -> switchTab(3));
    }

    private void switchTab(int tabIndex) {
        tvTabFindPartners.setTextColor(getResources().getColor(R.color.secondary_gray));
        tvTabRequests.setTextColor(getResources().getColor(R.color.secondary_gray));
        tvTabMyRequests.setTextColor(getResources().getColor(R.color.secondary_gray));
        
        indicatorFindPartners.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        indicatorRequests.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        indicatorMyRequests.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        
        contentFindPartners.setVisibility(View.GONE);
        contentRequests.setVisibility(View.GONE);
        contentMyRequests.setVisibility(View.GONE);

        switch (tabIndex) {
            case 1:
                tvTabFindPartners.setTextColor(getResources().getColor(R.color.purple_primary));
                indicatorFindPartners.setBackgroundColor(getResources().getColor(R.color.purple_primary));
                contentFindPartners.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvTabRequests.setTextColor(getResources().getColor(R.color.purple_primary));
                indicatorRequests.setBackgroundColor(getResources().getColor(R.color.purple_primary));
                contentRequests.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvTabMyRequests.setTextColor(getResources().getColor(R.color.purple_primary));
                indicatorMyRequests.setBackgroundColor(getResources().getColor(R.color.purple_primary));
                contentMyRequests.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupChips() {
        TextView[] chips = {chipEBC, chipABC, chipLangtang, chipMardi, chipManaslu};
        String[] labels = {"Everest Base Camp", "Annapurna Base Camp", "Langtang Valley", "Mardi Himal", "Manaslu Circuit"};

        for (int i = 0; i < chips.length; i++) {
            final String label = labels[i];
            chips[i].setOnClickListener(v -> {
                selectedDestination = label;
                tvFilterDestination.setText(label + "  ›");
                for (TextView c : chips) {
                    c.setBackgroundResource(R.drawable.bg_chip_purple);
                    c.setTextColor(getResources().getColor(R.color.purple_primary));
                }
                ((TextView) v).setBackgroundResource(R.drawable.bg_chip_purple_selected);
                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
            });
        }
    }

    private void setupFilterRows() {
        findViewById(R.id.filterDestination).setOnClickListener(v ->
            showFilterDialog("Destination", new String[]{"All", "Everest Base Camp", "Annapurna Base Camp", "Langtang Valley", "Mardi Himal", "Manaslu Circuit"},
                val -> { selectedDestination = val; tvFilterDestination.setText(val + "  ›"); }));

        findViewById(R.id.filterTrekDate).setOnClickListener(v ->
            showFilterDialog("Trek Date", new String[]{"Anytime", "This Week", "This Month", "Next Month", "Apr 2026", "May 2026"},
                val -> { selectedDate = val; tvFilterDate.setText(val + "  ›"); }));

        findViewById(R.id.filterDuration).setOnClickListener(v ->
            showFilterDialog("Duration", new String[]{"Any", "1-3 Days", "4-7 Days", "8-14 Days", "14+ Days"},
                val -> { selectedDuration = val; tvFilterDuration.setText(val + "  ›"); }));

        findViewById(R.id.filterGender).setOnClickListener(v ->
            showFilterDialog("Gender", new String[]{"Any", "Male", "Female", "Non-binary"},
                val -> { selectedGender = val; tvFilterGender.setText(val + "  ›"); }));

        findViewById(R.id.filterGroupType).setOnClickListener(v ->
            showFilterDialog("Group Type", new String[]{"Any", "Solo", "Small Group (2-4)", "Medium Group (5-8)"},
                val -> { selectedGroupType = val; tvFilterGroup.setText(val + "  ›"); }));

        btnApplyFilters.setOnClickListener(v -> applyFilters());
    }

    private interface FilterCallback { void onSelected(String value); }

    private void showFilterDialog(String title, String[] options, FilterCallback cb) {
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options, (dialog, which) -> cb.onSelected(options[which]))
            .show();
    }

    private void applyFilters() {
        String query = etSearch.getText().toString().toLowerCase();
        
        showPartners.clear();
        for (PartnerModel p : allPartners) {
            boolean matchQuery = query.isEmpty() || p.getName().toLowerCase().contains(query) || p.getLocation().toLowerCase().contains(query);
            boolean matchDest = selectedDestination.equals("All") || p.getDestination().contains(selectedDestination);
            
            if (matchQuery && matchDest) {
                showPartners.add(p);
            }
        }
        
        partnerAdapter.updateList(showPartners);
        layoutResults.setVisibility(View.VISIBLE);
        
        if (showPartners.isEmpty()) {
            tvEmptyResults.setVisibility(View.VISIBLE);
            recyclerPartners.setVisibility(View.GONE);
        } else {
            tvEmptyResults.setVisibility(View.GONE);
            recyclerPartners.setVisibility(View.VISIBLE);
        }

        // Scroll to results
        contentFindPartners.post(() -> {
            View v = findViewById(R.id.tvResultsTitle);
            v.getParent().requestChildFocus(v, v);
        });
    }

    private void loadInitialData() {
        allPartners.add(new PartnerModel("Sujan Karki", "4.8", "(24)", "Available", R.drawable.partner1, "Pokhara, Nepal", "Everest Base Camp", "20 Apr – 2 May", "12 Days", "28 Years", "8+", "15", "Adventure lover.", "Photography", "3 – 5 People", R.drawable.everest, true));
        allPartners.add(new PartnerModel("Anita Gurung", "4.9", "(18)", "Available", R.drawable.partner2, "Kathmandu, Nepal", "Annapurna Base Camp", "10 May – 17 May", "7 Days", "25 Years", "5+", "8", "Solo female trekker.", "Nature,Yoga", "2 – 4 People", R.drawable.annapurna, true));
        allPartners.add(new PartnerModel("Ramesh Bhandari", "4.7", "(31)", "Available", R.drawable.partner3, "Lalitpur, Nepal", "Langtang Valley", "5 Apr – 12 Apr", "8 Days", "32 Years", "10+", "20", "Experienced.", "Camping", "4 – 6 People", R.drawable.langtang, false));
        showPartners.addAll(allPartners);
    }

    private void setupRecyclerViews() {
        partnerAdapter = new PartnerListAdapter(this, showPartners, partner -> {
            Intent intent = new Intent(this, PartnerProfileActivity.class);
            intent.putExtra("partner", partner);
            startActivity(intent);
        });
        recyclerPartners.setLayoutManager(new LinearLayoutManager(this));
        recyclerPartners.setAdapter(partnerAdapter);

        // Requests
        List<PartnerModel> receivedRequests = new ArrayList<>();
        receivedRequests.add(new PartnerModel("Bibek Paudel", "5.0", "(12)", "Pending", R.drawable.partner4, "Kathmandu", "Mardi Himal", "15 Oct – 20 Oct", "5 Days", "26 Years", "3+", "5", "Join me!", "Trekking", "2-3 People", R.drawable.mardihimal, true));
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerRequests.setAdapter(new GenericAdapter(receivedRequests, R.layout.item_partner_request));
        
        // My Requests
        List<PartnerModel> myRequests = new ArrayList<>();
        myRequests.add(new PartnerModel("Sandeep Magar", "4.9", "(40)", "Accepted", R.drawable.partner1, "Kathmandu", "Annapurna Circuit", "02 Nov – 15 Nov", "14 Days", "30 Years", "12+", "25", "Ready?", "Adventure", "2-5 People", R.drawable.annapurna, true));
        recyclerMyRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyRequests.setAdapter(new GenericAdapter(myRequests, R.layout.item_request_card));
    }

    private class GenericAdapter extends RecyclerView.Adapter<GenericViewHolder> {
        private List<PartnerModel> data;
        private int layoutId;
        GenericAdapter(List<PartnerModel> data, int layoutId) { this.data = data; this.layoutId = layoutId; }
        @NonNull @Override public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new GenericViewHolder(v, layoutId);
        }
        @Override public void onBindViewHolder(@NonNull GenericViewHolder h, int pos) {
            PartnerModel p = data.get(pos);
            if (layoutId == R.layout.item_partner_request) {
                h.tvName.setText(p.getName());
                h.tvTitle.setText("Wants to join " + p.getDestination());
                h.imgPartner.setImageResource(p.getImage());
                h.tvDate.setText(p.getTrekDate());
            } else if (layoutId == R.layout.item_request_card) {
                h.tvName.setText(p.getName());
                h.tvDestination.setText(p.getDestination());
                h.imgPartner.setImageResource(p.getImage());
                h.tvDate.setText(p.getTrekDate() + " • " + p.getDuration());
                h.tvStatus.setText(p.getStatus());
            }
        }
        @Override public int getItemCount() { return data.size(); }
    }

    private static class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTitle, tvDestination, tvDate, tvStatus;
        ImageView imgPartner;
        GenericViewHolder(@NonNull View itemView, int layoutId) {
            super(itemView);
            if (layoutId == R.layout.item_partner_request) {
                tvName = itemView.findViewById(R.id.txtUserName);
                tvTitle = itemView.findViewById(R.id.txtTitle);
                tvDate = itemView.findViewById(R.id.txtTrekDate);
                imgPartner = itemView.findViewById(R.id.imgTrek);
            } else if (layoutId == R.layout.item_request_card) {
                tvName = itemView.findViewById(R.id.tvName);
                tvDestination = itemView.findViewById(R.id.tvDestination);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                imgPartner = itemView.findViewById(R.id.imgPartner);
            }
        }
    }
}
