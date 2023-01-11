package com.example.p2124702assignment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    ImageButton buttonBack, buttonConfirm;
    TextView title, caption, location, locationButton;
    ImageView image;
    private GPSTracker gpsTracker;
    PictureHelper helper;

    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private double myLatitude = 0.0d;
    private double myLongitude = 0.0d;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String titleStr, captionsStr, locationStr;
    private byte[] imageEdit;
    private double latEdit, lonEdit;
    private int _id;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        gpsTracker.stopUsingGPS();
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleStr = getArguments().getString("title");
            captionsStr = getArguments().getString("captions");
            locationStr = getArguments().getString("location");
            imageEdit = getArguments().getByteArray("image");
            latEdit = getArguments().getDouble("lat");
            lonEdit = getArguments().getDouble("lon");
            _id = getArguments().getInt("id");
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helper = new PictureHelper(getContext());
        gpsTracker = new GPSTracker(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonConfirm = view.findViewById(R.id.buttonConfirm);
        title = view.findViewById(R.id.editTextTitle);
        caption = view.findViewById(R.id.editTextCaption);
        locationButton = view.findViewById(R.id.textViewLocation);
        location = view.findViewById(R.id.textViewCoordinates);
        image = view.findViewById(R.id.imageView3);

        title.setText(titleStr);
        caption.setText(captionsStr);
        location.setText(locationStr);
        image.setImageBitmap(getImage(imageEdit));

        latitude = latEdit;
        longitude = lonEdit;
        int id = _id;

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLocationPermission()==true) {
                    PopupMenu menu = new PopupMenu(getContext(), v);
                    menu.getMenuInflater().inflate(R.menu.map_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()) {
                                case R.id.get_location:
                                    if (gpsTracker.canGetLocation()) {
                                        latitude = gpsTracker.getLatitude();
                                        longitude = gpsTracker.getLongitude();
                                        TextView coordinates = view.findViewById(R.id.textViewCoordinates);
                                        coordinates.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
                                        coordinates.setTextColor(Color.DKGRAY);
                                        Toast.makeText(getContext(), "Your location is - \nLat: " + latitude + "\nLong:" + longitude, Toast.LENGTH_LONG).show();
                                    }
                                    return (true);
                                case R.id.show_map:
                                    myLatitude = gpsTracker.getLatitude();
                                    myLongitude = gpsTracker.getLongitude();

                                    Intent intent = new Intent(getContext(), MapsActivity.class);
                                    intent.putExtra("LATITUDE", latitude);
                                    intent.putExtra("LONGITUDE", longitude);
                                    intent.putExtra("MYLATITUDE", myLatitude);
                                    intent.putExtra("MYLONGITUDE", myLongitude);
                                    startActivity(intent);
                                    return (true);
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            }
        });

        view.findViewById(R.id.textViewImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FragmentContainer, homeFragment)
                        .commit();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap img = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Boolean insert = helper.update(title.getText().toString(), caption.getText().toString(), String.valueOf(id), latitude, longitude, img);
                if (insert == true){
                    Toast.makeText(getContext(), "Data edited successfully", Toast.LENGTH_SHORT).show();
                    HomeFragment homeFragment = new HomeFragment();
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FragmentContainer, homeFragment)
                            .commit();
                }
            }
        });


        return view;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Access Needed")
                        .setMessage("Location access is needed in order for this feature to work")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bitmapImage);
            }
        }
    }
}

