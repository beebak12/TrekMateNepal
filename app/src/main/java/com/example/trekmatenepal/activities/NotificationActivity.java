package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.NotificationAdapter;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.NotificationModel;

import java.util.ArrayList;

/**
 * NotificationActivity — the in-app inbox for the signed-in id.
 *
 * Shows rental updates addressed to this user: bookings they made, and gear of
 * theirs that somebody rented. Written by NotificationRepository, so a seller
 * and a renter each see only their own side of the same booking.
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;
    private View layoutEmpty;
    private TextView txtRecipient, btnClearAll;

    private NotificationAdapter adapter;
    private final ArrayList<NotificationModel> notifications = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        userId = SessionUser.getUserId(this);

        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        layoutEmpty           = findViewById(R.id.layoutEmpty);
        txtRecipient          = findViewById(R.id.txtRecipient);
        btnClearAll           = findViewById(R.id.btnClearAll);
        ImageView btnBack     = findViewById(R.id.btnBack);

        txtRecipient.setText("Updates for " + userId);

        adapter = new NotificationAdapter(notifications);
        recyclerNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotifications.setAdapter(adapter);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        btnClearAll.setOnClickListener(v -> clearAll());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        notifications.clear();
        notifications.addAll(NotificationRepository.getFor(this, userId));
        adapter.notifyDataSetChanged();
        showEmptyState(notifications.isEmpty());
    }

    private void clearAll() {
        if (notifications.isEmpty()) return;
        NotificationRepository.clearFor(this, userId);
        loadNotifications();
        Toast.makeText(this, "Notifications cleared", Toast.LENGTH_SHORT).show();
    }

    private void showEmptyState(boolean empty) {
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerNotifications.setVisibility(empty ? View.GONE : View.VISIBLE);
        btnClearAll.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}
