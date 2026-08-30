package com.example.trekmatenepal.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.activities.PostDetailActivity;
import com.example.trekmatenepal.models.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList){
        this.postList=postList;
    }

    public void addPost(PostModel post) {
        postList.add(0, post);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        PostModel post=postList.get(position);

        if (post.getCustomImageUri() != null) {
            holder.image.setImageURI(Uri.parse(post.getCustomImageUri()));
        } else {
            holder.image.setImageResource(post.getImageRes() != 0 ? post.getImageRes() : R.drawable.everest);
        }
        
        holder.title.setText(post.getTitle());
        holder.authorLocation.setText(post.getAuthor() + "  •  " + post.getLocation());
        holder.dateDuration.setText(post.getDateRange() + "  •  " + post.getDuration());
        holder.interested.setText(post.getInterestedCount() + " interested");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
            intent.putExtra("post", post);
            v.getContext().startActivity(intent);
        });

        holder.btnJoin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
            intent.putExtra("post", post);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, authorLocation, dateDuration, interested;
        View btnJoin;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.imgPost);
            title = itemView.findViewById(R.id.txtPostTitle);
            authorLocation = itemView.findViewById(R.id.txtAuthorLocation);
            dateDuration = itemView.findViewById(R.id.txtDateDuration);
            interested = itemView.findViewById(R.id.txtInterested);
            btnJoin = itemView.findViewById(R.id.btnJoin);
        }
    }
}
