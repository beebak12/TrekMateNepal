package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.NotificationModel;

import java.util.List;

/** NotificationAdapter — lists the notifications addressed to the current user id. */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationModel> items;

    public NotificationAdapter(List<NotificationModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel n = items.get(position);
        holder.txtTitle.setText(n.getTitle());
        holder.txtMessage.setText(n.getMessage());
        holder.txtTime.setText(n.getTimeLabel());
        holder.imgIcon.setImageResource("listing".equals(n.getType())
                ? R.drawable.ic_gear
                : R.drawable.ic_notification);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtMessage, txtTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon    = itemView.findViewById(R.id.imgIcon);
            txtTitle   = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime    = itemView.findViewById(R.id.txtTime);
        }
    }
}
