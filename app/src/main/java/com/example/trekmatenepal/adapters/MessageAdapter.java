package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.MessageModel;

import java.util.List;

public class MessageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private final List<MessageModel> messageList;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);

        if (message.isSentByCurrentUser()) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_SENT) {
            View view = inflater.inflate(
                    R.layout.item_message_sent,
                    parent,
                    false
            );

            return new SentMessageViewHolder(view);
        } else {
            View view = inflater.inflate(
                    R.layout.item_message_received,
                    parent,
                    false
            );

            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {
        MessageModel message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            SentMessageViewHolder sentHolder =
                    (SentMessageViewHolder) holder;

            sentHolder.textMessage.setText(
                    message.getMessageText()
            );

            sentHolder.textTime.setText(
                    message.getMessageTime()
            );

        } else if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder receivedHolder =
                    (ReceivedMessageViewHolder) holder;

            receivedHolder.textSenderName.setText(
                    message.getSenderName()
            );

            receivedHolder.textMessage.setText(
                    message.getMessageText()
            );

            receivedHolder.textTime.setText(
                    message.getMessageTime()
            );

            receivedHolder.imageSender.setImageResource(
                    message.getSenderImage()
            );
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder
            extends RecyclerView.ViewHolder {

        TextView textMessage;
        TextView textTime;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage =
                    itemView.findViewById(R.id.textSentMessage);

            textTime =
                    itemView.findViewById(R.id.textSentTime);
        }
    }

    static class ReceivedMessageViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imageSender;
        TextView textSenderName;
        TextView textMessage;
        TextView textTime;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSender =
                    itemView.findViewById(R.id.imageSender);

            textSenderName =
                    itemView.findViewById(R.id.textSenderName);

            textMessage =
                    itemView.findViewById(R.id.textReceivedMessage);

            textTime =
                    itemView.findViewById(R.id.textReceivedTime);
        }
    }
}