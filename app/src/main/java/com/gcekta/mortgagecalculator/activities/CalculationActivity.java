package com.gcekta.mortgagecalculator.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gcekta.mortgagecalculator.R;
import com.gcekta.mortgagecalculator.db.PropertyDataSource;
import com.gcekta.mortgagecalculator.model.Calculations;
import com.gcekta.mortgagecalculator.model.PropertyPojo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CalculationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CalculationActivity";

    TextView editTitle, calcFor;
    EditText propertyPrice, downPayment, apr;
    TextInputLayout propertyPriceLayout, downPaymentLayout, aprLayout;
    RadioGroup loanterm;
    Button btnCalc, btnSaveEdits;
    TextView monthlyPayment;
    PropertyPojo pp;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fabClear,fabSave;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcalc);
        setSupportActionBar(toolbar);

        //get handle to the Views
        propertyPriceLayout = (TextInputLayout) findViewById(R.id.prop_price_text_layout);
        propertyPrice = (EditText) findViewById(R.id.prop_price_text);
        downPaymentLayout = (TextInputLayout) findViewById(R.id.down_payment_text_layout);
        downPayment = (EditText) findViewById(R.id.down_payment);
        aprLayout = (TextInputLayout) findViewById(R.id.apr_text_layout);
        apr = (EditText) findViewById(R.id.apr);
        loanterm = (RadioGroup) findViewById(R.id.loanterm);
        btnCalc = (Button) findViewById(R.id.calcbtn);
        monthlyPayment = (TextView) findViewById(R.id.monthlypayment);
        fab = (FloatingActionButton)findViewById(R.id.fabMain);
        fabClear = (FloatingActionButton)findViewById(R.id.fabClear);
        fabSave = (FloatingActionButton)findViewById(R.id.fabSave);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        editTitle = (TextView) findViewById(R.id.edit_title);
        calcFor = (TextView) findViewById(R.id.calc_for);
        btnSaveEdits = (Button) findViewById(R.id.save_edits);

        Intent propertyIntent = getIntent();
        PropertyPojo ppFromMap = (PropertyPojo) propertyIntent.getSerializableExtra("PPPOJO");
        if(ppFromMap != null){
            //Load the activity for edits
            editTitle.setVisibility(View.VISIBLE);
            calcFor.setVisibility(View.VISIBLE);
            btnSaveEdits.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            pp = ppFromMap;
            propertyPrice.setText(NumberFormat.getCurrencyInstance().format((pp.getPropertyPrice())));
            downPayment.setText(NumberFormat.getCurrencyInstance().format((pp.getDownPayment())));

            DecimalFormat df = new DecimalFormat("##0.00");
            apr.setText(df.format(pp.getApr()));

            int lt = pp.getLoanTerms();
            if(lt == 15){
                loanterm.check(R.id.years15);
            }else{
                loanterm.check(R.id.years30);
            }
            monthlyPayment.setText(NumberFormat.getCurrencyInstance().format((pp.getMonthlyPayment())));

            String address = pp.getAddress() + " " +
                                pp.getCity() + " " +
                                pp.getState() + " " +
                                pp.getZipcode();

            calcFor.setText(address);

        }else{
            pp = new PropertyPojo();
        }

        View.OnClickListener fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.fabMain:
                        animateFAB();
                        break;
                    case R.id.fabClear:
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalculationActivity.this);
                        alertBuilder
                                .setTitle(R.string.alert_title)
                                .setMessage(R.string.clear_alert_msg)
                                .setPositiveButton(R.string.clear_alert_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        propertyPrice.getText().clear();
                                        downPayment.getText().clear();
                                        apr.getText().clear();
                                        monthlyPayment.setText(R.string.monthly_payment_default);
                                        animateFAB();
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
                        break;
                    case R.id.fabSave:
                        Intent savePropIntent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                        savePropIntent.putExtra("PPPOJO", (Serializable) pp);
                        startActivity(savePropIntent);
                        break;
                }
            }
        };

        fab.setOnClickListener(fabListener);
        fabClear.setOnClickListener(fabListener);
        fabSave.setOnClickListener(fabListener);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Text Field listeners
        TextWatcher propertyPriceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private String current = "";
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(current)){
                    propertyPrice.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    if(!cleanString.isEmpty()){
                        double parsed = (Double.parseDouble(cleanString))/100;
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed));

                        current = formatted;
                        propertyPrice.setText(formatted);
                        propertyPrice.setSelection(formatted.length());

                        propertyPrice.addTextChangedListener(this);
                        if(validateTextFields(propertyPrice, propertyPriceLayout)){
                            pp.setPropertyPrice(parsed);
                        }
                    }

                }

            }

        };

        TextWatcher downPaymentWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private String current = "";
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(current)){
                    downPayment.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    if(!cleanString.isEmpty()){
                        double parsed = (Double.parseDouble(cleanString))/100;
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed));

                        current = formatted;
                        downPayment.setText(formatted);
                        downPayment.setSelection(formatted.length());

                        downPayment.addTextChangedListener(this);

                        if(validateTextFields(downPayment, downPaymentLayout)){
                            pp.setDownPayment(parsed);
                        }
                    }

                }
            }

        };
        TextWatcher aprWatcher = new TextWatcher() {
            DecimalFormat df = new DecimalFormat("##0.00");
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private String current = "";
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(current)){

                    apr.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[,.]","");
                    if(!cleanString.isEmpty()){
                        double parsed = (Double.parseDouble(cleanString))/100;
                        String formatted = df.format(parsed);
                        current = formatted;
                        apr.setText(formatted);
                        apr.setSelection(formatted.length());

                        apr.addTextChangedListener(this);

                        if(validateTextFields(apr, aprLayout)){
                            pp.setApr(parsed);
                        }
                    }

                }
            }

        };

        View.OnClickListener btnsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.calcbtn:
                        calculateMonthlyPayment();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    case R.id.save_edits:
                        if(calculateMonthlyPayment()){
                            PropertyDataSource database = new PropertyDataSource(getApplicationContext());
                            database.open();
                            if(database.updateProperty(pp) == 1){
                                Toast.makeText(getApplicationContext(), R.string.calc_edited_message, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.something_wrong_msg, Toast.LENGTH_LONG).show();
                            }
                            database.close();
                        }
                        break;
                }
            }
        };

        //add All listeners
        propertyPrice.addTextChangedListener(propertyPriceWatcher);
        downPayment.addTextChangedListener(downPaymentWatcher);
        apr.addTextChangedListener(aprWatcher);
        btnCalc.setOnClickListener(btnsListener);
        btnSaveEdits.setOnClickListener(btnsListener);
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
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalculationActivity.this);
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



    private boolean calculateMonthlyPayment(){
        if( validateTextFields(propertyPrice, propertyPriceLayout) &&
                validateTextFields(downPayment, downPaymentLayout) &&
                validateTextFields(apr, aprLayout)){

            switch (loanterm.getCheckedRadioButtonId()){
                case R.id.years15:
                    pp.setLoanTerms(15);
                    break;
                case R.id.years30:
                    pp.setLoanTerms(30);
                    break;
            }

            if(pp.getDownPayment()<pp.getPropertyPrice()){
                String monthlyPaymentValue = NumberFormat.getCurrencyInstance().format((Calculations.calculateMonthlyPayment(pp)));
                monthlyPayment.setText(monthlyPaymentValue);
                return true;
            }else{
                Toast.makeText(getApplicationContext(), R.string.down_pmt_grt_thn_prop_price, Toast.LENGTH_LONG).show();
                return false;
            }


        }else{
            return false;
        }
    }


    private boolean validateTextFields(EditText textField, TextInputLayout textInputLayout) {
        if (textField.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.err_req_msg));
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fabClear.startAnimation(fab_close);
            fabSave.startAnimation(fab_close);
            fabClear.setClickable(false);
            fabSave.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fabClear.startAnimation(fab_open);
            fabSave.startAnimation(fab_open);
            fabClear.setClickable(true);
            fabSave.setClickable(true);
            isFabOpen = true;

        }
    }

}
