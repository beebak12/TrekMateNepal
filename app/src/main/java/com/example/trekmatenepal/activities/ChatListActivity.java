package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.ChatSummaryAdapter;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.ChatSummaryModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout chatListTopBar;
    private RecyclerView recyclerChats;
    private ChatSummaryAdapter adapter;
    private List<ChatSummaryModel> chatList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("TREKMATE_CHAT", "ChatListActivity onCreate");
        
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_chat_list);

        apiService = ApiClient.getClient().create(ApiService.class);
        
        initializeViews();
        setupSystemBars();
        setupRecyclerView();
        
        loadConversations();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        chatListTopBar = findViewById(R.id.chatListTopBar);
        recyclerChats = findViewById(R.id.recyclerChats);
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupSystemBars() {
        View root = findViewById(R.id.chatListRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int statusBarHeight = systemBars.top;
            int toolbarHeight = dpToPx(56);

            try {
                ViewGroup.LayoutParams lp = chatListTopBar.getLayoutParams();
                if (lp instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lp;
                    params.height = statusBarHeight + toolbarHeight;
                    chatListTopBar.setLayoutParams(params);
                }
            } catch (Exception e) {
                android.util.Log.e("TREKMATE_CHAT", "Error setting layout params", e);
            }

            chatListTopBar.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            chatListTopBar.setPadding(dpToPx(8), 0, dpToPx(8), dpToPx(4));

            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setupRecyclerView() {
        adapter = new ChatSummaryAdapter(chatList, chat -> {
            Intent intent = new Intent(this, ChatActivity.class);
            
            // Robust ID handling
            int convId = 0;
            try {
                convId = Integer.parseInt(chat.getId());
            } catch (Exception ignored) {}
            
            intent.putExtra("conversation_id", convId);
            intent.putExtra("other_user_name", chat.getName());
            intent.putExtra("partnerImage", chat.getImageRes());
            startActivity(intent);
        });
        recyclerChats.setLayoutManager(new LinearLayoutManager(this));
        recyclerChats.setAdapter(adapter);
    }

    private void loadConversations() {
        android.util.Log.d("TREKMATE_CHAT", "loadConversations called");
        String token = SessionUser.getToken(this);
        if (token == null) {
            android.util.Log.e("TREKMATE_CHAT", "Token is null, returning");
            Toast.makeText(this, "Please log in to see chats", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getConversations("Bearer " + token).enqueue(new Callback<List<ChatSummaryModel>>() {
            @Override
            public void onResponse(Call<List<ChatSummaryModel>> call, Response<List<ChatSummaryModel>> response) {
                android.util.Log.d("TREKMATE_CHAT", "onResponse: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    chatList.clear();
                    chatList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChatListActivity.this, "Failed to load chats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatSummaryModel>> call, Throwable t) {
                android.util.Log.e("TREKMATE_CHAT", "onFailure", t);
                // Fallback to local data for UI demo if server is down
                com.example.trekmatenepal.data.ChatRepository.loadChats(ChatListActivity.this);
                chatList.clear();
                chatList.addAll(com.example.trekmatenepal.data.ChatRepository.getChats(false));
                adapter.notifyDataSetChanged();
                
                Toast.makeText(ChatListActivity.this, "Offline mode: Loading local chats", Toast.LENGTH_SHORT).show();
            }
        });
    }
}