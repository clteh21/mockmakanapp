package com.example.p2124702assignment;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DefaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultFragment extends Fragment {

    private BottomNavigationView navView;
    private HomeFragment homeFragment;
    private AddFragment addFragment;
    private MapFragment mapFragment;

    private double lat, lon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DefaultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DefaultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefaultFragment newInstance(String param1, String param2) {
        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble("latitude");
            lon = getArguments().getDouble("longitude");
        }
    }

    public void setCurrentLocation(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeFragment = new HomeFragment();
        addFragment = new AddFragment();
        mapFragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putDouble("latitude",lat);
        bundle.putDouble("longitude",lon);
        homeFragment.setArguments(bundle);
        Log.d("tag:", "passed from activity :"+lat + lon);

        navView = getView().findViewById(R.id.bottomNavigationView);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.addButton:
                        item.setChecked(true);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FragmentContainer, addFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.homeButton:
                        item.setChecked(true);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FragmentContainer, homeFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.mapButton:
                        item.setChecked(true);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FragmentContainer, mapFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return false;
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.FragmentContainer,homeFragment)
                .addToBackStack(null)
                .commit();

//        getView().findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.FragmentContainer,homeFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//
//        getView().findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.FragmentContainer,addFragment)
//                                .addToBackStack(null)
//                        .commit();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        navView = getView().findViewById(R.id.bottomNavigationView);
//        navView.setOnItemSelectedListener(menuSelected);
//        homeFragment = new HomeFragment();
//        addFragment = new AddFragment();
//
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.FragmentContainer,homeFragment)
//                .addToBackStack(null)
//                .commit();
//
//        getView().findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.FragmentContainer,homeFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//
//        getView().findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.FragmentContainer,addFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });


//        NavigationBarView.OnItemSelectedListener menuSelected = new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                invalidateOptionsMenu();
//                switch(id){
//                    case (R.id.homeButton): fragmentManager.beginTransaction()
//                            .replace(R.id.FragmentContainer, homeFragment)
//                            .setReorderingAllowed(true)
//                            .commit();
//                        return true;
//                    case(R.id.addButton): fragmentManager.beginTransaction()
//                            .replace(R.id.FragmentContainer, addFragment)
//                            .setReorderingAllowed(true)
//                            .commit();
//                        return true;
//                }
//                return false;
//            }
//        };
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default, container, false);
    }
}