package com.example.trekmatenepal.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.RentalGearModel;
import com.example.trekmatenepal.data.RemoteImageLoader;

import java.util.ArrayList;

public class MyPostedGearAdapter extends RecyclerView.Adapter<MyPostedGearAdapter.Holder> {
    public interface Actions {
        void edit(RentalGearModel gear);
        void changeStatus(RentalGearModel gear);
        void delete(RentalGearModel gear);
        void open(RentalGearModel gear);
    }

    private final ArrayList<RentalGearModel> gear;
    private final Actions actions;

    public MyPostedGearAdapter(ArrayList<RentalGearModel> gear, Actions actions) {
        this.gear = gear;
        this.actions = actions;
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_posted_gear, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        RentalGearModel item = gear.get(position);
        bindImage(holder.image, item);
        holder.name.setText(item.getName());
        holder.details.setText(item.getCategory() + " • " + item.getPrice());
        holder.status.setText(item.getAvailability());
        holder.itemView.setOnClickListener(v -> actions.open(item));
        holder.edit.setOnClickListener(v -> actions.edit(item));
        holder.changeStatus.setOnClickListener(v -> actions.changeStatus(item));
        holder.delete.setOnClickListener(v -> actions.delete(item));
    }

    private void bindImage(ImageView view, RentalGearModel item) {
        if (!item.getCustomImageUri().isEmpty()) {
            try {
                view.setImageURI(Uri.parse(item.getCustomImageUri()));
                if (view.getDrawable() != null) return;
            } catch (RuntimeException error) {
                Log.w("MyPostedGearAdapter", "Unable to read gear image", error);
            }
        }
        if (!item.getRemoteImageUrl().isEmpty()) {
            RemoteImageLoader.load(view, item.getRemoteImageUrl(), R.drawable.jacket);
            return;
        }
        view.setImageResource(item.getImage() > 0 ? item.getImage() : R.drawable.jacket);
    }

    @Override public int getItemCount() { return gear.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name, details, status;
        final Button edit, changeStatus, delete;
        Holder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.imgGear);
            name = view.findViewById(R.id.txtGearName);
            details = view.findViewById(R.id.txtGearDetails);
            status = view.findViewById(R.id.txtGearStatus);
            edit = view.findViewById(R.id.btnEditGear);
            changeStatus = view.findViewById(R.id.btnGearStatus);
            delete = view.findViewById(R.id.btnDeleteGear);
        }
    }
}
