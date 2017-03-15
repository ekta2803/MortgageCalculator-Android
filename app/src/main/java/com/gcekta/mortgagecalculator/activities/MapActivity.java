package com.gcekta.mortgagecalculator.activities;



import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import com.gcekta.mortgagecalculator.db.PropertyDataSource;
import com.gcekta.mortgagecalculator.model.PropertyPojo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gcekta.mortgagecalculator.R;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import java.io.IOException;
import java.util.*;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener{
    private GoogleMap mMap;
    private PropertyDataSource database;
    private Geocoder geocoder;
    private double lattitude;
    private double longitude;
    private Map<Marker,PropertyPojo> markerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        database = new PropertyDataSource(this);
        database.open();
        markerMap = new HashMap<Marker,PropertyPojo>();
        /*PropertyPojo obj = new PropertyPojo();
        obj.setPropertyType("Housing");
        obj.setAddress("201 S 4th ST");
        obj.setCity("San Jose");
        obj.setState("California");
        obj.setZipcode("95112");
        obj.setPropertyPrice(100000);
        obj.setDownPayment(10000);
        obj.setApr(3.92);
        obj.setLoanTerms(30);

        database.createPropertyInfo(obj);*/
        //database.deleteAll();
        //database.dropTable();
        geocoder = new Geocoder(getBaseContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
                    Log.i("addr",String.valueOf(addr.getLatitude()));
                    lattitude = addr.getLatitude();
                    longitude = addr.getLongitude();
                    LatLng geoLocation = new LatLng(lattitude, longitude);
                    Marker markerObj = mMap.addMarker(new MarkerOptions().position(geoLocation).title(pojo.getAddress()));
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
            PropertyPojo pojoObj = markerMap.get(marker);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);
            TextView propType = (TextView) dialog.findViewById(R.id.property_type_val);
            propType.setText(pojoObj.getPropertyType());

            TextView propAddr = (TextView) dialog.findViewById(R.id.property_addr_val);
            propAddr.setText(pojoObj.getAddress());

            TextView city = (TextView) dialog.findViewById(R.id.city_val);
            city.setText(pojoObj.getCity());

            TextView state = (TextView) dialog.findViewById(R.id.state_val);
            state.setText(pojoObj.getState());

            TextView zipcode = (TextView) dialog.findViewById(R.id.zipcode_val);
            zipcode.setText(pojoObj.getZipcode());

            TextView loanAmt = (TextView) dialog.findViewById(R.id.loan_amt_val);
            loanAmt.setText("$"+String.valueOf(pojoObj.getPropertyPrice()));

            TextView dwnPmt = (TextView) dialog.findViewById(R.id.down_pmt_val);
            dwnPmt.setText("$"+String.valueOf(pojoObj.getDownPayment()));

            TextView apr = (TextView) dialog.findViewById(R.id.apr_val);
            apr.setText(String.valueOf(pojoObj.getApr())+" %");

            TextView term = (TextView) dialog.findViewById(R.id.term_val);
            term.setText(String.valueOf(pojoObj.getLoanTerms()+" years"));

            TextView monthlyPmt = (TextView) dialog.findViewById(R.id.monthly_pmt_val);
            monthlyPmt.setText(String.valueOf("$"+pojoObj.getMonthlyPayment()));
            dialog.show();


        }

        return false;
    }
}
