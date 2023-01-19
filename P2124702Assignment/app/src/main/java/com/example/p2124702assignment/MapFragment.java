package com.example.p2124702assignment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;


import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.maps.model.MapStyleOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        LocationListener, GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private static final String TAG = MapFragment.class.getSimpleName();

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private Marker mSelectedMarker;

    protected LatLng start=null;
    protected LatLng end=null;

    private List<Data> dataList;

    //polyline object
    private List<Polyline> polylines=null;

    boolean isFirstTime = true;

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_raw));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Add hawker centres marker
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(1.3134, 103.7646))
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker))
//                        .title("Clementi Hawker Centre"));
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(1.3347, 103.7215))
//                .title("Taman Jurong Hawker Centre")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker)));
        interfaceAPI apiService = RestAPI.getClient().create(interfaceAPI.class);
        Call<Data> call = apiService.getData();

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                dataList = data.getData();

                if(GPSUtils.getInstance().getLatitude()!=null){
                    for(int i=0;i<dataList.size();i++){
                        if(Objects.equals(dataList.get(i).getName(), "Margaret Drive Hawker Centre")){
                            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(1.2977, 103.8043))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker))
                                    .title(dataList.get(i).getName()));
                        } else {
                            mGoogleMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getActivity(),dataList.get(i).getLocation()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker))
                                    .title(dataList.get(i).getName()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("TAG","Response Failure = "+t.toString());
            }
        });



        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    public void setList(List<Data> list){
        dataList = list;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        if(isFirstTime){
            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

            isFirstTime = false;
        }

        //get destination location when user click on map
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (polylines != null) {
                    for(Polyline line : polylines){
                        line.remove();
                    }
                    polylines.clear();
                }

                //hides title of marker
                marker.hideInfoWindow();

                //centers the marker on the map
                int zoom = (int)mGoogleMap.getCameraPosition().zoom;
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new
                        LatLng(marker.getPosition().latitude + (double)90/Math.pow(10, zoom),
                        marker.getPosition().longitude), zoom);
                mGoogleMap.animateCamera(cu,500,null);

                //checks if marker is selected anot
                if (null != mSelectedMarker) {
                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker));
                }
                mSelectedMarker = marker;
                mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());


                end=marker.getPosition();
                start=new LatLng(location.getLatitude(),location.getLongitude());

//                //start route finding
//                Findroutes(start,end);

                //calculates and finds distance between 2 points
                Location a = new Location("point A");
                a.setLatitude(location.getLatitude());
                a.setLongitude(location.getLongitude());
                Location b = new Location("point B");
                b.setLongitude(end.longitude);
                b.setLatitude(end.latitude);

                float distance = a.distanceTo(b);
//                Toast.makeText(getContext(),String.valueOf(distance)+"m",Toast.LENGTH_SHORT).show();

                //Start bottom sheet dialog
                BottomScrollMap bottomScrollMap = new BottomScrollMap(marker.getTitle(),distance,start,end);
                bottomScrollMap.show(getParentFragmentManager(),bottomScrollMap.getTag());

                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(null != mSelectedMarker) {
                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_restaurant_marker));
                }
                mSelectedMarker = null;
                if (polylines != null) {
                    for(Polyline line : polylines){
                        line.remove();
                    }
                    polylines.clear();
                }
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public float calculateDistance(String destination){
        //calculates and finds distance between 2 points
        Location a = new Location("point A");
        a.setLatitude(mLastLocation.getLatitude());
        a.setLongitude(mLastLocation.getLongitude());
        Location b = new Location("point B");
        LatLng dest = getLocationFromAddress(getContext(),destination);
        b.setLatitude(dest.latitude);
        b.setLongitude(dest.longitude);
        float distance = a.distanceTo(b);
        return distance;
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {

        if(Start==null || End==null) {
            Toast.makeText(getContext(),"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCh76pZ6ZDCc64di5WtcOCbSSI4tBiipsc")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = getActivity().findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
//        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        //clears other routes before add new route
        if (polylines != null) {
            for(Polyline line : polylines){
                line.remove();
            }
            polylines.clear();
        }

        polylines = new ArrayList<>();

        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;

        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.instagram_iconblue));
                polyOptions.width(17);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mGoogleMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(start)
                .include(end)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,350));

//        //Add Marker on route starting position
//        MarkerOptions startMarker = new MarkerOptions();
//        startMarker.position(polylineStartLatLng);
//        startMarker.title("My Location");
//        mGoogleMap.addMarker(startMarker);
//
//        //Add Marker on route ending position
//        MarkerOptions endMarker = new MarkerOptions();
//        endMarker.position(polylineEndLatLng);
//        endMarker.title("Destination");
//        mGoogleMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

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
}