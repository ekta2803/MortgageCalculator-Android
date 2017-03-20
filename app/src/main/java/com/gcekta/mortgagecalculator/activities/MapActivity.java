package com.gcekta.mortgagecalculator.activities;



import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gcekta.mortgagecalculator.Manifest;
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
import java.io.Serializable;
import java.security.Security;
import java.text.NumberFormat;
import java.util.*;

public class MapActivity extends FragmentActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        database = new PropertyDataSource(this);
        database.open();
        markerMap = new HashMap<Marker,PropertyPojo>();

        geocoder = new Geocoder(getBaseContext());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_map);
        mapFragment.getMapAsync(this);
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
