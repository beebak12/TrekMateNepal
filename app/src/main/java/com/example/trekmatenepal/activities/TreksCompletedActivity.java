package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.adapters.TrekAdapter;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.model.ProfileResponse;
import com.example.trekmatenepal.models.TrekModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreksCompletedActivity extends AppCompatActivity {

    RecyclerView recyclerTreks;
    TrekAdapter trekAdapter;
    List<TrekModel> trekList;

    ImageView btnBack;
    TextView tvCompletedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treks_completed);

        recyclerTreks = findViewById(R.id.recyclerTreks);
        btnBack = findViewById(R.id.btnBack);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);

        btnBack.setOnClickListener(v -> finish());

        trekList = new ArrayList<>();

        recyclerTreks.setLayoutManager(
                new LinearLayoutManager(this)
        );

        trekAdapter = new TrekAdapter(trekList);
        recyclerTreks.setAdapter(trekAdapter);
        loadCompletedCount();
    }

    private void loadCompletedCount() {
        String token = SessionUser.getToken(this);
        if (token == null || token.trim().isEmpty()) {
            tvCompletedCount.setText("0");
            return;
        }
        ApiClient.getClient().create(ApiService.class)
                .getProfile("Bearer " + token)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call,
                                           Response<ProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getData() != null) {
                            tvCompletedCount.setText(String.valueOf(Math.max(0,
                                    response.body().getData().getTreksCompleted())));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                        tvCompletedCount.setText("0");
                    }
                });
    }
}
