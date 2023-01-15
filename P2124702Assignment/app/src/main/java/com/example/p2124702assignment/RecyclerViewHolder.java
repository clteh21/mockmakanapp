package com.example.p2124702assignment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private CardView view;
    TextView postTitle, postLocation, postCaption;
    ImageView postImage;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postImage = itemView.findViewById(R.id.postImageView);
            postLocation = itemView.findViewById(R.id.postLocation);
            postCaption = itemView.findViewById(R.id.postCaption);

    }

    public FrameLayout getView(){
        return view;
    }
}