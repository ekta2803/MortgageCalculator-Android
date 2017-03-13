package com.gcekta.mortgagecalculator.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Spinner;

import com.gcekta.mortgagecalculator.R;

public class PropertyInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PropertyInfoActivity";

    private FloatingActionButton fab;
    private Spinner propertyType;

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

        //Text Field listeners


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
