package com.gcekta.mortgagecalculator.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gcekta.mortgagecalculator.R;
import com.gcekta.mortgagecalculator.db.PropertyDataSource;
import com.gcekta.mortgagecalculator.model.PropertyPojo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;

public class PropertyInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PropertyInfoActivity";

    private FloatingActionButton fabMainPropInfo;
    private Spinner propertyType;
    private TextInputLayout streetAddressLayout, zipLayout;
    private EditText streetAddress, zip, city;
    private AutoCompleteTextView state;
    private Button saveProperty;
    private Geocoder geocoder;
    private PropertyPojo pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarpropinfo);
        setSupportActionBar(toolbar);

        fabMainPropInfo = (FloatingActionButton)findViewById(R.id.fabMainPropInfo);

        View.OnClickListener fabListenerProp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PropertyInfoActivity.this);
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
        fabMainPropInfo.setOnClickListener(fabListenerProp);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter countryFilter = new AutocompleteFilter.Builder()
                .setCountry("US")
                .build();
        autocompleteFragment.setFilter(countryFilter);

        AutocompleteFilter precAddress = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(precAddress);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                String fullAddress = (String) place.getAddress();

                String[] addressArr = fullAddress.split(",");
                String streetAddressText = addressArr[0].trim();
                String cityText = addressArr[1].trim();
                String[] stateZip = addressArr[2].trim().split("\\s+");
                String stateText = stateZip[0].trim();
                String zipText = stateZip[1].trim();

                streetAddress.setText(streetAddressText);
                city.setText(cityText);
                state.setText(stateText);
                zip.setText(zipText);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get handle to the Views
        propertyType = (Spinner) findViewById(R.id.propertyType);
        ArrayAdapter<CharSequence> propTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.proptypearray,
                android.R.layout.simple_spinner_item);
        propTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        propertyType.setAdapter(propTypeAdapter);

        streetAddressLayout = (TextInputLayout) findViewById(R.id.street_text_layout);
        streetAddress = (EditText) findViewById(R.id.street_text);
        city = (EditText) findViewById(R.id.city_text);

        state = (AutoCompleteTextView) findViewById(R.id.state_text);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.statearray,
                android.R.layout.simple_list_item_1);
        state.setAdapter(stateAdapter);
        state.setThreshold(1);

        zipLayout = (TextInputLayout) findViewById(R.id.zip_text_layout);
        zip = (EditText) findViewById(R.id.zip_text);

        saveProperty = (Button)findViewById(R.id.savePropertyBtn);

        View.OnClickListener savePropertyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAddress()){
                    updatePropertyDetails(pp);

                    PropertyDataSource database = new PropertyDataSource(getApplicationContext());
                    database.open();
                    if(database.createPropertyInfo(pp)){
                        Toast.makeText(getApplicationContext(), R.string.property_saved_message, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.something_wrong_msg, Toast.LENGTH_LONG).show();
                    }
                    database.close();
                }
                return;
            }
        };
        saveProperty.setOnClickListener(savePropertyListener);

        Intent propertyIntent = getIntent();
        pp = (PropertyPojo) propertyIntent.getSerializableExtra("PPPOJO");
        if(pp.getAddress() != null){

            switch (pp.getPropertyType()){
                case "House":
                    propertyType.setSelection(0);
                    break;
                case "Town House":
                    propertyType.setSelection(1);
                    break;
                case "Condo":
                    propertyType.setSelection(2);
                    break;
            }
            streetAddress.setText(pp.getAddress());
            city.setText(pp.getCity());
            state.setText(pp.getState());
            zip.setText(pp.getZipcode());

        }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_calc) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PropertyInfoActivity.this);
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

        } else if (id == R.id.nav_saved_calc) {
            Intent i = new Intent(this,MapActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean validateAddress(){
        List<Address> addresses = null;
        try {

            geocoder = new Geocoder(getBaseContext());
            String fullAddr = String.valueOf(streetAddress.getText()) + ", "+ String.valueOf(city.getText())+"," +
                    " "+String.valueOf(state.getText())+", "+String.valueOf(zip.getText());
            addresses = geocoder.getFromLocationName(String.valueOf(streetAddress.getText()),10);
            if(addresses.size()>0){
                return true;
            }else{
                Toast.makeText(this, R.string.address_invalid_message, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updatePropertyDetails(PropertyPojo pp) {
        pp.setPropertyType(propertyType.getSelectedItem().toString());
        pp.setAddress(String.valueOf(streetAddress.getText()));
        pp.setCity(String.valueOf(city.getText()));
        pp.setState(String.valueOf(state.getText()));
        pp.setZipcode(String.valueOf(zip.getText()));
    }

}
