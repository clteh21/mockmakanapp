package com.example.p2124702assignment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class PictureListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private PictureHelper helper;
    private Cursor cursor;
    private Context context;
    private ArrayList<String> Title, Captions, Location;
    private ArrayList<Double> lat, lon;
    private ArrayList<byte[]> images;
    private ArrayList<Integer> id;
    View viewer;

    public PictureListAdapter(Context ct, ArrayList<String> Title, ArrayList<String> Captions, ArrayList<String> Location, ArrayList<byte[]> images, ArrayList<Integer> id, ArrayList<Double> lat, ArrayList<Double> lon){
        context = ct;
        this.Title = Title;
        this.Captions = Captions;
        this.Location = Location;
        this.images = images;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.post_imageview;
    }

    @Override
    public int getItemCount() {
        return Title.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        viewer = inflater.inflate(R.layout.post_imageview,null);
        RecyclerViewHolder holder = new RecyclerViewHolder(viewer);

//        View.OnClickListener editClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int position = holder.getView().get(View) v.getParent());
//                Log.v(TAG, "gd clicked, row %d", position);
//            }
//        };
//        view.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditFragment editFragment = new EditFragment();
//                ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.FragmentContainer, editFragment)
//                        .commit();
//
//                TextView postTitle = v.findViewById(R.id.postTitle);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("title",postTitle.getText().toString());
//            }
//        });

//        cursor = helper.getAll();
//        holder.postTitle.setText(helper.getTitle(cursor));
//        holder.postCaption.setText(helper.getCaptions(cursor));
//        double latitude = helper.getLatitude(cursor);
//        double longitude = helper.getLongitude(cursor);
//        String locationText = "Lat: " + latitude + " Long:" + longitude;
//        holder.postLocation.setText(locationText);
//        holder.postImage.setImageBitmap(getImage(helper.getImage(cursor)));
        return holder;
//        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        int position1 = position;
            holder.postTitle.setText(Title.get(position1));
            holder.postCaption.setText(Captions.get(position1));
//            double latitude = helper.getLatitude(cursor);
//            double longitude = helper.getLongitude(cursor);
//            String locationText = "Lat: " + latitude + " Long:" + longitude;
            holder.postLocation.setText(Location.get(position1));
            holder.postImage.setImageBitmap(getImage(images.get(position)));

        viewer.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title",Title.get(position1));
                bundle.putString("captions", Captions.get(position1));
                bundle.putString("location", Location.get(position1));
                bundle.putByteArray("image", images.get(position1));
                bundle.putInt("id", id.get(position1));
                bundle.putDouble("lat",lat.get(position1));
                bundle.putDouble("lon", lon.get(position1));

                EditFragment editFragment = new EditFragment();
                editFragment.setArguments(bundle);

                ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FragmentContainer, editFragment)
                        .commit();
            }
        });
    }
    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
