package com.example.p2124702assignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HawkerAdapter extends RecyclerView.Adapter<HawkerViewHolder> {

    private List<Data> dataList = null;
    private Context ct;
    View hawkerView;

    public HawkerAdapter (Context ct, List<Data> data) {
        this.ct = ct;
        dataList = data;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.hawker_cardview;
    }

    @NonNull
    @Override
    public HawkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        hawkerView = inflater.inflate(R.layout.hawker_cardview, null);
        return new HawkerViewHolder(hawkerView);
    }

    @Override
    public void onBindViewHolder(@NonNull HawkerViewHolder holder, int position) {
        hawkerView.findViewById(R.id.cardHawker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        String imageUrl = "https://kiasu-hawker.sgp1.digitaloceanspaces.com/hawker/" + dataList.get(position).getImage();
        Picasso.get().load(imageUrl).into(holder.hawkerImage);
        Toast.makeText(hawkerView.getContext(), dataList.get(position).getName(),Toast.LENGTH_SHORT).show();
        holder.hawkerName.setText(dataList.get(position).getName());
        holder.hawkerDist_Price.setText("idk yet");
        holder.hawkerStatus.setText(dataList.get(position).getStatus());
        holder.hawkerLocation.setText(dataList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        if(dataList!=null){
            return dataList.size();
        }
        return 0;
    }
}
