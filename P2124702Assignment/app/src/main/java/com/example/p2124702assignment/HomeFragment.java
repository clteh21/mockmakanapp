package com.example.p2124702assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements LocationListener{

    //Add RecyclerView member
    private RecyclerView recyclerView;
    private PictureHelper helper = null;
    private PictureListAdapter adapter = null;

    private ArrayList<String> Title, Captions, Location;
    private ArrayList<Double> lat, lon;
    private ArrayList<byte[]> images;
    private ArrayList<Integer> id;

    private RecyclerView hawkerRecycler;
    private HawkerAdapter hawkerAdapter = null;
    private List<Data> dataArrayList = null;

    private double lati, longi;


    public LatLng getLocationFromAddress(String strAddress, Context context) {
        LatLng p1 = null;
        if(getActivity() != null){

            Geocoder coder = new Geocoder(context);
            List<Address> address;

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
        return p1;
    }

    public float calculateDistance(Location location, String destination, Context context) {
        float distance=0;
        //calculates and finds distance between 2 points
        Location a = new Location("point A");
        a.setLatitude(location.getLatitude());
        a.setLongitude(location.getLongitude());
        Location b = new Location("point B");
        LatLng dest = getLocationFromAddress(destination,context);
        if(dest!=null){
            b.setLatitude(dest.latitude);
            b.setLongitude(dest.longitude);
            distance = a.distanceTo(b);
        }
        Log.d("Tag:","Name:"+destination+"\n Distance: "+distance);
        return distance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lati = getArguments().getDouble("latitude");
            longi = getArguments().getDouble("longitude");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        helper = new PictureHelper(getContext());
        Title = new ArrayList<>();
        Captions = new ArrayList<>();
        Location = new ArrayList<>();
        images = new ArrayList<>();
        id = new ArrayList<>();
        lat = new ArrayList<>();
        lon = new ArrayList<>();

        adapter = new PictureListAdapter(getContext(),Title,Captions,Location,images, id, lat, lon);
        recyclerView.setAdapter(adapter);

        hawkerRecycler = view.findViewById(R.id.recyclerHawker);
        hawkerRecycler.setHasFixedSize(false);
        hawkerRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        dataArrayList = new ArrayList<>();

        hawkerAdapter = new HawkerAdapter(getContext(), dataArrayList);
        hawkerRecycler.setAdapter(hawkerAdapter);

        displaydata();

        interfaceAPI apiService = RestAPI.getClient().create(interfaceAPI.class);
        Call<Data> call = apiService.getData();

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                dataArrayList = data.getData();

                Location location = new Location("myself");
                location.setLatitude(lati);
                location.setLongitude(longi);
                Log.d("location","latlng is "+location);

                for(int i =0; i<dataArrayList.size(); i++){
                    float f = calculateDistance(location,dataArrayList.get(i).getLocation(),getActivity());
                    dataArrayList.get(i).setDistance(f);
                }
                Collections.sort(dataArrayList, new Comparator() {
                    @Override
                    public int compare(Object o, Object t1) {
                        Data a = (Data) o;
                        Data b= (Data) t1;
                        return Float.compare(a.getDistance(), b.getDistance());
                    }
                });
//                Log.d("TAG","Response name = "+dataArrayList.get(0).getName());
//                Log.d("TAG","Response id = "+dataArrayList.get(0).getId());
//                Log.d("TAG","Response id = "+dataArrayList.get(0).getImage());
                hawkerAdapter.setDataList(dataArrayList);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("TAG","Response Failure = "+t.toString());
            }
        });

        return view;
    }

    //display SQLite data
    private void displaydata() {
        Cursor cursor = helper.getAll();
        if(cursor.getCount()==0){
        }
        else
        {
            while(cursor.moveToNext()){
                id.add(cursor.getInt(0));
                Title.add(cursor.getString(1));
                Captions.add(cursor.getString(2));
                images.add(cursor.getBlob(3));
                lat.add(cursor.getDouble(4));
                lon.add(cursor.getDouble(5));
                Location.add(String.valueOf(cursor.getDouble(4)+" "+cursor.getDouble(5)));
            }
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {

    }
}

