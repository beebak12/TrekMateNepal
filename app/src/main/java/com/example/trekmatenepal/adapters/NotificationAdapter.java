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

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_notification,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        NotificationModel notification =
                notificationList.get(position);

        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.time.setText(notification.getTimeLabel());

        // --------------------------------
        // Notification icon
        // --------------------------------

        switch (notification.getType()) {

            case "booking":
                holder.icon.setImageResource(
                        R.drawable.ic_calendar
                );
                break;

            case "chat":
                holder.icon.setImageResource(
                        R.drawable.ic_chat
                );
                break;

            case "request":
                holder.icon.setImageResource(
                        R.drawable.ic_person_add
                );
                break;

            case "verified":
                holder.icon.setImageResource(
                        R.drawable.ic_info
                );
                break;

            default:
                holder.icon.setImageResource(
                        R.drawable.ic_notifications
                );
                break;
        }

        // --------------------------------
        // Read / unread
        // --------------------------------

        if (notification.isRead()) {

            holder.unreadDot.setVisibility(View.GONE);

        } else {

            holder.unreadDot.setVisibility(View.VISIBLE);
        }

        // --------------------------------
        // Click notification
        // --------------------------------

        holder.itemView.setOnClickListener(v -> {

            notification.setRead(true);

            holder.unreadDot.setVisibility(View.GONE);

            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // --------------------------------
    // Mark all read
    // --------------------------------

    public void markAllAsRead() {

        for (NotificationModel notification :
                notificationList) {

            notification.setRead(true);
        }

        notifyDataSetChanged();
    }


    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView icon;

        TextView title;
        TextView message;
        TextView time;

        View unreadDot;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            icon = itemView.findViewById(
                    R.id.notificationIcon
            );

            title = itemView.findViewById(
                    R.id.tvNotificationItemTitle
            );

            message = itemView.findViewById(
                    R.id.tvNotificationItemMessage
            );

            time = itemView.findViewById(
                    R.id.tvNotificationItemTime
            );

            unreadDot = itemView.findViewById(
                    R.id.unreadDot
            );
        }
    }
}
