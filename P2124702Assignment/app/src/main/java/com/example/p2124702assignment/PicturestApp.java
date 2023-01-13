package com.example.p2124702assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class PicturestApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomScrollMap.OnRouteRequestListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    public void onRouteRequest(LatLng origin, LatLng destination) {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        if(mapFragment!=null){
            mapFragment.Findroutes(origin, destination);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.defaultFragmentContainerView,
                    new DefaultFragment()).commit();
            navigationView.setCheckedItem(R.id.navHomeButton);
        }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navHomeButton:
                getSupportFragmentManager().beginTransaction().replace(R.id.defaultFragmentContainerView,
                        new DefaultFragment()).commit();
                break;
            case R.id.aboutButton:
                getSupportFragmentManager().beginTransaction().replace(R.id.defaultFragmentContainerView,
                        new AboutFragment()).commit();
                break;
            case R.id.exitButton:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing App")
                        .setMessage("Are you sure you want to close this App?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.exit(0);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        invalidateOptionsMenu();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}