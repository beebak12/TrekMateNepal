package com.example.trekmatenepal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList){
        this.postList=postList;
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

        holder.image.setImageResource(post.getImage());
        holder.title.setText(post.getTitle());
        holder.author.setText(post.getAuthor());
        holder.time.setText(post.getTime());
        holder.likes.setText(post.getLikes());
        holder.comments.setText(post.getComments());

    }

    @Override
    public int getItemCount(){
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, author, time, likes, comments;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image=itemView.findViewById(R.id.imgPost);
            title=itemView.findViewById(R.id.txtPostTitle);
            author=itemView.findViewById(R.id.txtAuthor);
            time=itemView.findViewById(R.id.txtTime);
            likes=itemView.findViewById(R.id.txtLikes);
            comments=itemView.findViewById(R.id.txtComments);
        }
    }

}