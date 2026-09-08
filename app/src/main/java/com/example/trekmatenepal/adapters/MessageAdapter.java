package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.ChatMessageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SENT = 1;
    private static final int RECEIVED = 2;

    private final List<ChatMessageModel> messageList;
    private final int currentUserId;

    public MessageAdapter(
            List<ChatMessageModel> messageList,
            int currentUserId
    ) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {

        ChatMessageModel message = messageList.get(position);

        if (message.getSenderId() == currentUserId) {
            return SENT;
        }

        return RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        if (viewType == SENT) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(
                            R.layout.item_message_sent,
                            parent,
                            false
                    );

            return new SentMessageHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(
                            R.layout.item_message_received,
                            parent,
                            false
                    );

            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        ChatMessageModel message = messageList.get(position);

        if (holder instanceof SentMessageHolder) {

            SentMessageHolder sent =
                    (SentMessageHolder) holder;

            sent.txtMessage.setText(message.getMessage());
            sent.txtTime.setText(formatTime(message.getTime()));

        } else {

            ReceivedMessageHolder received =
                    (ReceivedMessageHolder) holder;

            received.txtMessage.setText(message.getMessage());
            received.txtTime.setText(formatTime(message.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private String formatTime(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss",
                            Locale.getDefault()
                    );

            Date date = input.parse(value);

            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                    );

            return output.format(date);

        } catch (ParseException e) {
            return value;
        }
    }

    static class SentMessageHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;
        TextView txtTime;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage =
                    itemView.findViewById(R.id.txtMessage);

            txtTime =
                    itemView.findViewById(R.id.txtTime);
        }
    }

    static class ReceivedMessageHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;
        TextView txtTime;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage =
                    itemView.findViewById(R.id.txtMessage);

            txtTime =
                    itemView.findViewById(R.id.txtTime);
        }
    }
}