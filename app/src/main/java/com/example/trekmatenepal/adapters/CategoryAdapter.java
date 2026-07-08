package com.example.trekmatenepal.adapters;
import com.google.android.material.card.MaterialCardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<CategoryModel> categoryList;
    private OnCategoryClickListener listener;

    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter(ArrayList<CategoryModel> categoryList,
                           OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryModel category = categoryList.get(position);

        holder.imgCategory.setImageResource(category.getImage());

        holder.txtCategoryName.setText(category.getCategoryName());

        if (selectedPosition == position) {

            holder.card.setCardBackgroundColor(
                    holder.itemView.getResources().getColor(R.color.purple));

            holder.txtCategoryName.setTextColor(
                    holder.itemView.getResources().getColor(android.R.color.white));

        } else {

            holder.card.setCardBackgroundColor(
                    holder.itemView.getResources().getColor(android.R.color.white));

            holder.txtCategoryName.setTextColor(
                    holder.itemView.getResources().getColor(android.R.color.black));

        }

        holder.itemView.setOnClickListener(v -> {

            selectedPosition = holder.getAdapterPosition();

            notifyDataSetChanged();

            listener.onCategoryClick(category.getCategoryName());

        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialCardView card;

        ImageView imgCategory;
        TextView txtCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.categoryCard);

            imgCategory = itemView.findViewById(R.id.imgCategory);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}