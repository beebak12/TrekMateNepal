package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.ChatListModel;

import java.util.List;

public class ChatListAdapter
        extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private final List<ChatListModel> chatList;
    private final OnChatClickListener clickListener;

    public interface OnChatClickListener {
        void onChatClick(ChatListModel chat);
    }

    public ChatListAdapter(
            List<ChatListModel> chatList,
            OnChatClickListener clickListener
    ) {
        this.chatList = chatList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_chat_preview,
                        parent,
                        false
                );

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChatViewHolder holder,
            int position
    ) {
        ChatListModel chat = chatList.get(position);

        holder.imageChat.setImageResource(
                chat.getChatImage()
        );

        holder.textChatName.setText(
                chat.getChatName()
        );

        holder.textLastMessage.setText(
                chat.getLastMessage()
        );

        holder.textChatTime.setText(
                chat.getMessageTime()
        );

        if (chat.getUnreadCount() > 0) {
            holder.textUnreadCount.setVisibility(View.VISIBLE);
            holder.textUnreadCount.setText(
                    String.valueOf(chat.getUnreadCount())
            );
        } else {
            holder.textUnreadCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view ->
                clickListener.onChatClick(chat)
        );
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imageChat;
        TextView textChatName;
        TextView textLastMessage;
        TextView textChatTime;
        TextView textUnreadCount;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            imageChat =
                    itemView.findViewById(R.id.imageChat);

            textChatName =
                    itemView.findViewById(R.id.textChatName);

            textLastMessage =
                    itemView.findViewById(R.id.textLastMessage);

            textChatTime =
                    itemView.findViewById(R.id.textChatTime);

            textUnreadCount =
                    itemView.findViewById(R.id.textUnreadCount);
        }
    }
}