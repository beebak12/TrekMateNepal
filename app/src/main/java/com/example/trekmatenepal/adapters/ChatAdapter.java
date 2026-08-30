package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.ChatMessageModel;

import java.util.List;

/**
 * ChatAdapter — drives the chat messages RecyclerView.
 * Inflates item_chat_sent or item_chat_received based on message type.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessageModel> messages;

    public ChatAdapter(List<ChatMessageModel> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType(); // TYPE_SENT or TYPE_RECEIVED
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ChatMessageModel.TYPE_SENT) {
            View view = inflater.inflate(R.layout.item_chat_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_chat_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageModel msg = messages.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).bind(msg);
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ── Sent message ViewHolder ───────────────────────────────────────────────
    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime, txtAttachmentName;
        ImageView imgAttachment;

        SentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime    = itemView.findViewById(R.id.txtTime);
            imgAttachment = itemView.findViewById(R.id.imgAttachment);
            txtAttachmentName = itemView.findViewById(R.id.txtAttachmentName);
        }

        void bind(ChatMessageModel msg) {
            if (msg.getMessage() != null && !msg.getMessage().isEmpty()) {
                txtMessage.setVisibility(View.VISIBLE);
                txtMessage.setText(msg.getMessage());
            } else {
                txtMessage.setVisibility(View.GONE);
            }
            
            if (txtTime != null) txtTime.setText(msg.getTime());
            
            if (msg.hasAttachment()) {
                if ("image".equals(msg.getAttachmentType())) {
                    imgAttachment.setVisibility(View.VISIBLE);
                    imgAttachment.setImageURI(msg.getAttachmentUri());
                    txtAttachmentName.setVisibility(View.GONE);
                } else {
                    imgAttachment.setVisibility(View.GONE);
                    txtAttachmentName.setVisibility(View.VISIBLE);
                    txtAttachmentName.setText(msg.getAttachmentName());
                }
            } else {
                imgAttachment.setVisibility(View.GONE);
                txtAttachmentName.setVisibility(View.GONE);
            }
        }
    }

    // ── Received message ViewHolder ───────────────────────────────────────────
    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime, txtAttachmentName;
        ImageView imgAttachment;

        ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime    = itemView.findViewById(R.id.txtTime);
            imgAttachment = itemView.findViewById(R.id.imgAttachment);
            txtAttachmentName = itemView.findViewById(R.id.txtAttachmentName);
        }

        void bind(ChatMessageModel msg) {
            if (msg.getMessage() != null && !msg.getMessage().isEmpty()) {
                txtMessage.setVisibility(View.VISIBLE);
                txtMessage.setText(msg.getMessage());
            } else {
                txtMessage.setVisibility(View.GONE);
            }
            
            if (txtTime != null) txtTime.setText(msg.getTime());

            if (msg.hasAttachment()) {
                if ("image".equals(msg.getAttachmentType())) {
                    imgAttachment.setVisibility(View.VISIBLE);
                    imgAttachment.setImageURI(msg.getAttachmentUri());
                    txtAttachmentName.setVisibility(View.GONE);
                } else {
                    imgAttachment.setVisibility(View.GONE);
                    txtAttachmentName.setVisibility(View.VISIBLE);
                    txtAttachmentName.setText(msg.getAttachmentName());
                }
            } else {
                imgAttachment.setVisibility(View.GONE);
                txtAttachmentName.setVisibility(View.GONE);
            }
        }
    }
}
