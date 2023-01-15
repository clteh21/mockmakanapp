package com.example.p2124702assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class HawkerAdapter extends RecyclerView.Adapter<HawkerViewHolder> {

    private ArrayList<String> hawkerName, hawkerLocation, hawkerStatus, hawkerImage;
    private ArrayList<Integer> hawkerId, hawkerStallAmount;
    private Context ct;
    View hawkerView;

    public HawkerAdapter (Context ct, ArrayList<Integer> id, ArrayList<String> name, ArrayList<Integer> amt, ArrayList<String> location, ArrayList<String> status, ArrayList<String> image) {
        this.ct = ct;
        hawkerId = id;
        hawkerName = name;
        hawkerStallAmount = amt;
        hawkerLocation = location;
        hawkerStatus = status;
        hawkerImage = image;
    }

    @Override
    public int getItemViewType(int position) {
        return R.id.cardHawker;
    }

    @NonNull
    @Override
    public HawkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        hawkerView = inflater.inflate(R.layout.hawker_cardview, null);
        HawkerViewHolder hawkerHolder = new HawkerViewHolder(hawkerView);
        return hawkerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HawkerViewHolder holder, int position) {
        String imageUrl = "https://kiasu-hawker.sgp1.digitaloceanspaces.com/hawker/" + hawkerImage.get(position);
        Picasso.get().load(imageUrl).into(holder.hawkerImage);
        holder.hawkerName.setText(hawkerName.get(position));
        holder.hawkerDist_Price.setText("idk yet");
        holder.hawkerStatus.setText(hawkerStatus.get(position));
        holder.hawkerLocation.setText(hawkerLocation.get(position));
    }

    @Override
    public int getItemCount() {
        return hawkerId.size();
    }
}
