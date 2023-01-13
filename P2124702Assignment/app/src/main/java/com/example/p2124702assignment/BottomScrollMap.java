package com.example.p2124702assignment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class BottomScrollMap extends BottomSheetDialogFragment {
    BottomSheetDialog dialog;
    BottomSheetBehavior bottomSheetBehavior;
    View rootView;
    TextView hawkerText, distanceText;
    ImageView hawkerPicture;
    String hawkerTitle;
    float distance;
    LatLng start, end;
    Context ct;

    private OnRouteRequestListener listener;

    public interface OnRouteRequestListener {
        void onRouteRequest(LatLng origin, LatLng destination);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRouteRequestListener) {
            listener = (OnRouteRequestListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRouteRequestListener");
        }
    }

    //constructor
    public BottomScrollMap(String title, float distance, LatLng start, LatLng end) {
        hawkerTitle = title;
        this.distance = distance;
        this.start = start;
        this.end = end;
    }

    //course of execution onCreate >> onCreateDialog >> onCreateView >> onViewCreated


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the custom layout for bottom sheet fragment
        rootView = inflater.inflate(R.layout.fragment_bottom_scroll_map, container, false);

        Button findRouteButton = rootView.findViewById(R.id.find_route);
        findRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //call the function in other fragment
//                MapFragment mapFragment = (MapFragment) getS().findFragmentById(R.id.FragmentContainer);
////                mapFragment.start = start;
////                mapFragment.end = end;
//                mapFragment.Findroutes(start,end);
                listener.onRouteRequest(start, end);
                dialog.dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hawkerText = view.findViewById(R.id.textViewHawker);
        hawkerText.setText(hawkerTitle);
        distanceText = view.findViewById(R.id.textViewDistance);
        distanceText.setText(String.valueOf(distance/1000)+"km away from you bitch");
        hawkerPicture = view.findViewById(R.id.hawkerImageView);
        String imageUrl = "https://kiasu-hawker.sgp1.digitaloceanspaces.com/hawker/hawker-margaretdrive.jpg ";
        Picasso.get().load(imageUrl).into(hawkerPicture);



        //sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
//        bottomSheetBehavior.setPeekHeight((int) (getResources().getDisplayMetrics().heightPixels * 0.25));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetBehavior.setDraggable(true);
//        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState== BottomSheetBehavior.STATE_EXPANDED){
                    HomeFragment homeFragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FragmentContainer, homeFragment)
                            .addToBackStack(null)
                            .commit();
                    dialog.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0);
        dialog.show();
        //set to behaviour to expanded and minimum height to parent layout
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
