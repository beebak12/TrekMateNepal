package com.example.trekmatenepal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.activities.ChatActivity;
import com.example.trekmatenepal.adapters.ChatSummaryAdapter;
import com.example.trekmatenepal.data.ChatRepository;
import com.example.trekmatenepal.models.ChatSummaryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private ChatSummaryAdapter adapter;
    private List<ChatSummaryModel> chatList = new ArrayList<>();
    private List<ChatSummaryModel> groupList = new ArrayList<>();

    private TextView tvTabChats, tvTabGroups;
    private View indicatorChats, indicatorGroups;
    private boolean isShowingGroups = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chat_bottom_sheet, container, false);

        ChatRepository.loadChats(getContext());
        initViews(view);
        refreshData();
        setupTabs(view);
        setupRecyclerView();
        setupSearch(view);

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerChatSummary);
        tvTabChats = view.findViewById(R.id.tvTabChats);
        tvTabGroups = view.findViewById(R.id.tvTabGroups);
        indicatorChats = view.findViewById(R.id.indicatorChats);
        indicatorGroups = view.findViewById(R.id.indicatorGroups);
    }

    private void refreshData() {
        chatList = ChatRepository.getChats(false);
        groupList = ChatRepository.getChats(true);
    }

    private void setupTabs(View view) {
        view.findViewById(R.id.tabChats).setOnClickListener(v -> {
            isShowingGroups = false;
            updateTabUI();
        });

        view.findViewById(R.id.tabGroups).setOnClickListener(v -> {
            isShowingGroups = true;
            updateTabUI();
        });
        
        updateTabUI();
    }

    private void updateTabUI() {
        if (adapter == null) return; // Safety check - adapter not yet initialized
        
        if (isShowingGroups) {
            tvTabGroups.setTextColor(getResources().getColor(R.color.purple_primary));
            indicatorGroups.setBackgroundColor(getResources().getColor(R.color.purple_primary));
            tvTabChats.setTextColor(getResources().getColor(R.color.secondary_gray));
            indicatorChats.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            adapter.updateList(groupList);
        } else {
            tvTabChats.setTextColor(getResources().getColor(R.color.purple_primary));
            indicatorChats.setBackgroundColor(getResources().getColor(R.color.purple_primary));
            tvTabGroups.setTextColor(getResources().getColor(R.color.secondary_gray));
            indicatorGroups.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            adapter.updateList(chatList);
        }
    }

    private void setupRecyclerView() {
        if (recyclerView == null) return; // Safety check - view not initialized
        
        adapter = new ChatSummaryAdapter(isShowingGroups ? groupList : chatList, chat -> {
            try {
                // Mark as seen
                chat.setUnreadCount(0);
                ChatRepository.saveChats(getContext());
                
                dismiss();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("partnerName", chat.getName());
                intent.putExtra("partnerImage", chat.getImageRes());
                intent.putExtra("groupId", chat.isGroup() ? chat.getId() : null);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch(View view) {
        EditText etSearch = view.findViewById(R.id.etSearchMessages);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String query) {
        if (adapter == null) return; // Safety check
        
        List<ChatSummaryModel> source = isShowingGroups ? groupList : chatList;
        List<ChatSummaryModel> filtered = new ArrayList<>();
        for (ChatSummaryModel chat : source) {
            if (chat.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(chat);
            }
        }
        adapter.updateList(filtered);
    }
}
