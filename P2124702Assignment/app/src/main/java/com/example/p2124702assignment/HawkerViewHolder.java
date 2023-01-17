package com.example.p2124702assignment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HawkerViewHolder extends RecyclerView.ViewHolder {

    TextView hawkerName, hawkerLocation, hawkerDist_Price, hawkerStatus;
    ImageView hawkerImage;

    private CardView view;

    public HawkerViewHolder(@NonNull View itemView) {
        super(itemView);
        hawkerImage = itemView.findViewById(R.id.hawkerImageView);
        hawkerDist_Price = itemView.findViewById(R.id.hawkerDist_Price);
        hawkerName = itemView.findViewById(R.id.hawkerName);
        hawkerLocation = itemView.findViewById(R.id.hawkerLoc);
        hawkerStatus = itemView.findViewById(R.id.hawkerStatus);
    }

    public FrameLayout getView() {return view;}
}
