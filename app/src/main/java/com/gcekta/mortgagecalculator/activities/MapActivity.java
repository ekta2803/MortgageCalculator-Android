package com.gcekta.mortgagecalculator.activities;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gcekta.mortgagecalculator.db.PropertyDataSource;
import com.gcekta.mortgagecalculator.dialogs.MapDialog;
import com.gcekta.mortgagecalculator.model.PropertyPojo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gcekta.mortgagecalculator.R;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import java.io.IOException;
import java.util.*;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener{
    private GoogleMap mMap;
    private PropertyDataSource database;
    private Geocoder geocoder;
    private double lattitude;
    private double longitude;
    private Map<Marker,PropertyPojo> markerMap;
    private SupportMapFragment mapFragment;
    private PropertyPojo pojoObj;
    public PropertyPojo getPojoObj() {
        return pojoObj;
    }

    //Views
    FloatingActionButton fabMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmap);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        database = new PropertyDataSource(this);
        database.open();
        markerMap = new HashMap<Marker,PropertyPojo>();

        geocoder = new Geocoder(getBaseContext());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_map);
        mapFragment.getMapAsync(this);

        //get handles of Views
        fabMap = (FloatingActionButton)findViewById(R.id.fabMap);

        View.OnClickListener fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MapActivity.this);
                alertBuilder
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.clear_alert_msg)
                        .setPositiveButton(R.string.clear_alert_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), CalculationActivity.class);
                                startActivity(i);
                                return;
                            }
                        })
                        .setNegativeButton(R.string.clear_alert_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                alertBuilder.create().show();
                return;
            }
        };
        fabMap.setOnClickListener(fabListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.styled_json));
        // Add a marker in Sydney and move the camera

        List<PropertyPojo> list = database.getAllProperties();
        for(int i =0;i<list.size();i++){
            PropertyPojo pojo = list.get(i);
            String address = pojo.getAddress()+", "+pojo.getCity()+", "+pojo.getState() +", "+ pojo.getZipcode();

            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if(addresses.size()>0){
                    Address addr = addresses.get(0);
                    lattitude = addr.getLatitude();
                    longitude = addr.getLongitude();
                    LatLng geoLocation = new LatLng(lattitude, longitude);
                    Marker markerObj = mMap.addMarker(new MarkerOptions().position(geoLocation).title(pojo.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                   try{
                        mMap.setMyLocationEnabled(true);
                    }catch(SecurityException e){
                       e.printStackTrace();
                   }

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geoLocation,12));
                    mMap.setMinZoomPreference(6.0f);
                    mMap.setMaxZoomPreference(14.0f);
                    markerMap.put(markerObj,pojo);
                    mMap.setOnMarkerClickListener(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(markerMap.containsKey(marker)){
            pojoObj = markerMap.get(marker);
            MapDialog mapDialog = new MapDialog(this, marker);
            mapDialog.show();

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_calc) {
            Intent i = new Intent(this, CalculationActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_saved_calc) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
