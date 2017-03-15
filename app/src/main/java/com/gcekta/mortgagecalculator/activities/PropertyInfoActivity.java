package com.gcekta.mortgagecalculator.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.gcekta.mortgagecalculator.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class PropertyInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PropertyInfoActivity";

    private FloatingActionButton fab;
    private Spinner propertyType;
    private TextInputLayout streetAddressLayout, zipLayout;
    private EditText streetAddress, zip;
    private AutoCompleteTextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarpropinfo);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton)findViewById(R.id.fabMain);


//        View.OnClickListener fabListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                return;
//            }
//        };
//        fab.setOnClickListener(fabListener);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
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

        state = (AutoCompleteTextView) findViewById(R.id.state_text);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.statearray,
                android.R.layout.simple_list_item_1);
        state.setAdapter(stateAdapter);
        state.setThreshold(1);

        zipLayout = (TextInputLayout) findViewById(R.id.zip_text_layout);
        zip = (EditText) findViewById(R.id.zip_text);


        //Text Field listeners
        TextWatcher streetAddressWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        TextWatcher zipWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };



        //add All listeners

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

        } else if (id == R.id.nav_saved_calc) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
