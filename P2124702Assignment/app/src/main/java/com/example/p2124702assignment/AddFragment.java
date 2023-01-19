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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    ImageView mImageview;
    TextView location;
    EditText title;
    EditText caption;
    private GPSTracker gpsTracker;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private double myLatitude = 0.0d;
    private double myLongitude = 0.0d;

    PictureHelper helper;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String nameFromHome;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static int newInstance() {
        return 0;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if(getArguments()!=null){
            nameFromHome = getArguments().getString("name");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        helper = new PictureHelper(getContext());
        gpsTracker = new GPSTracker(getActivity());
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        mImageview = (ImageView) view.findViewById(R.id.imageView3);

        location = view.findViewById(R.id.textViewLocation);
        title = view.findViewById(R.id.editTextTitle);
        title.setText(nameFromHome);
        caption = view.findViewById(R.id.editTextCaption);

        location.setOnClickListener(new View.OnClickListener() {
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
//                                    intent.putExtra("NAME", restaurantName.getText().toString());
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

        view.findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(title,caption,location,mImageview)){
//                    if() {
                        double lat = gpsTracker.getLatitude();
                        double lon = gpsTracker.getLongitude();
                        Bitmap img = ((BitmapDrawable) mImageview.getDrawable()).getBitmap();
                        Boolean insert = helper.insertData(title.getText().toString(), caption.getText().toString(), lat, lon, img);
                        if (insert == true) {
                            Toast.makeText(getContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
                            title.setText("");
                            title.setHint("Write a title...");
                            caption.setText("");
                            caption.setHint("Write a caption...");
                            TextView coordinates = view.findViewById(R.id.textViewCoordinates);
                            coordinates.setText("");
                            coordinates.setHint("Coordinates will show here...");
                            mImageview.setImageResource(0);
                        }
//                    } else {

                    }
//                }
            }
        });
        return view;
    }

    private boolean validate(EditText title, EditText caption, TextView location, ImageView mImageview) { //Checks all inputs
        boolean temp=true;
        String checkTitle = title.getText().toString();
        String checkCaption = caption.getText().toString();
        String checkLocation = location.getText().toString();

        if(checkTitle.equals("")||checkCaption.equals("")||checkLocation.equals("")||mImageview.getDrawable()==null){ //Empty fields check
            Toast.makeText(getContext(),"Please enter in all fields",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
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
                mImageview.setImageBitmap(bitmapImage);
            }
        }
    }
}