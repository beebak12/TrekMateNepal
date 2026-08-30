package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.ChatSummaryModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatSummaryAdapter extends RecyclerView.Adapter<ChatSummaryAdapter.ViewHolder> {

    private List<ChatSummaryModel> chatList;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(ChatSummaryModel chat);
    }

    public ChatSummaryAdapter(List<ChatSummaryModel> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatSummaryModel chat = chatList.get(position);
        holder.tvName.setText(chat.getName());
        holder.tvLastMessage.setText(chat.getLastMessage());
        holder.tvTime.setText(chat.getTime());
        holder.imgProfile.setImageResource(chat.getImageRes());

        if (chat.getUnreadCount() > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(String.valueOf(chat.getUnreadCount()));
        } else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChatClick(chat);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateList(List<ChatSummaryModel> newList) {
        this.chatList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgProfile;
        TextView tvName, tvLastMessage, tvTime, tvUnreadCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgChatProfile);
            tvName = itemView.findViewById(R.id.tvChatName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvChatTime);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
        }
    }
}
