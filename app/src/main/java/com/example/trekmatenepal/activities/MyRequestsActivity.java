package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PartnerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MyRequestsActivity — shows all trek requests the user has sent.
 * Uses local sample data.  Replace with API call when backend is ready.
 */
public class MyRequestsActivity extends AppCompatActivity {

    private ImageView    btnBack;
    private RecyclerView recyclerRequests;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        btnBack          = findViewById(R.id.btnBack);
        recyclerRequests = findViewById(R.id.recyclerRequests);
        emptyState       = findViewById(R.id.emptyState);

        btnBack.setOnClickListener(v -> finish());

        List<PartnerModel> requests = buildSampleRequests();

        if (requests.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerRequests.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerRequests.setVisibility(View.VISIBLE);
            recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
            recyclerRequests.setAdapter(new RequestAdapter(requests));
        }
    }

    private List<PartnerModel> buildSampleRequests() {
        List<PartnerModel> list = new ArrayList<>();
        list.add(new PartnerModel(
            "Sujan Karki", "4.8", "(24)", "Pending",
            R.drawable.partner1,
            "Pokhara, Nepal",
            "Everest Base Camp", "20 Apr – 2 May", "12 Days",
            "28 Years", "8+", "15",
            "Adventure lover planning EBC this April.",
            "Photography,Camping,Nature",
            "3 – 5 People",
            R.drawable.everest,
            true
        ));
        list.add(new PartnerModel(
            "Anita Gurung", "4.9", "(18)", "Accepted",
            R.drawable.partner2,
            "Kathmandu, Nepal",
            "Annapurna Base Camp", "10 May – 17 May", "7 Days",
            "25 Years", "5+", "8",
            "Solo female trekker.",
            "Photography,Nature,Yoga",
            "2 – 4 People",
            R.drawable.annapurna,
            true
        ));
        return list;
    }

    // ── Inline adapter for request cards ─────────────────────────────────────
    private class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.VH> {

        private final List<PartnerModel> data;

        RequestAdapter(List<PartnerModel> data) { this.data = data; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_request_card, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            PartnerModel p = data.get(pos);

            h.imgPartner.setImageResource(p.getImage() != 0 ? p.getImage() : R.drawable.partner1);
            h.tvName.setText(p.getName());
            h.tvDestination.setText(p.getDestination());
            h.tvDate.setText(p.getTrekDate() + " • " + p.getDuration());

            // Status badge colour
            h.tvStatus.setText(p.getStatus());
            switch (p.getStatus().toLowerCase()) {
                case "accepted":
                    h.tvStatus.setTextColor(getResources().getColor(R.color.success_green));
                    h.tvStatus.setBackgroundResource(R.drawable.bg_status_confirmed);
                    break;
                case "declined":
                    h.tvStatus.setTextColor(getResources().getColor(R.color.red));
                    h.tvStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
                    break;
                default: // pending
                    h.tvStatus.setTextColor(getResources().getColor(R.color.orange));
                    h.tvStatus.setBackgroundResource(R.drawable.bg_available_tag);
            }

            // "Chat" button — open ChatActivity
            h.btnChat.setOnClickListener(v -> {
                Intent intent = new Intent(MyRequestsActivity.this, ChatActivity.class);
                intent.putExtra("partnerName",  p.getName());
                intent.putExtra("partnerImage", p.getImage());
                intent.putExtra("isOnline",     p.isOnline());
                startActivity(intent);
            });
        }

        @Override public int getItemCount() { return data.size(); }

        class VH extends RecyclerView.ViewHolder {
            ImageView imgPartner;
            TextView  tvName, tvDestination, tvDate, tvStatus;
            Button    btnChat;

            VH(@NonNull View v) {
                super(v);
                imgPartner    = v.findViewById(R.id.imgPartner);
                tvName        = v.findViewById(R.id.tvName);
                tvDestination = v.findViewById(R.id.tvDestination);
                tvDate        = v.findViewById(R.id.tvDate);
                tvStatus      = v.findViewById(R.id.tvStatus);
                btnChat       = v.findViewById(R.id.btnChat);
            }
        }
    }
}
