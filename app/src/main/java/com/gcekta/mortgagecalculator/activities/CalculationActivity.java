package com.gcekta.mortgagecalculator.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcekta.mortgagecalculator.R;
import com.gcekta.mortgagecalculator.model.Calculations;
import com.gcekta.mortgagecalculator.model.PropertyPojo;

import java.text.NumberFormat;

public class CalculationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CalculationActivity";

    EditText loanAmount, downPayment, apr;
    TextInputLayout loanAmountLayout, downPaymentLayout, aprLayout;
    RadioGroup loanterm;
    Button calcButton;
    TextView monthlyPayment;
    PropertyPojo pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pp = new PropertyPojo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalculationActivity.this);

                alertBuilder
                        .setTitle(R.string.clear_alert_title)
                        .setMessage(R.string.clear_alert_msg)
                        .setPositiveButton(R.string.clear_alert_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loanAmount.getText().clear();
                                downPayment.getText().clear();
                                apr.getText().clear();
                                monthlyPayment.setText(R.string.monthly_payment_default);
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
        loanAmountLayout = (TextInputLayout) findViewById(R.id.loan_amt_text_layout);
        loanAmount = (EditText) findViewById(R.id.loan_amt_text);
        downPaymentLayout = (TextInputLayout) findViewById(R.id.down_payment_text_layout);
        downPayment = (EditText) findViewById(R.id.down_payment);
        aprLayout = (TextInputLayout) findViewById(R.id.apr_text_layout);
        apr = (EditText) findViewById(R.id.apr);
        loanterm = (RadioGroup) findViewById(R.id.loanterm);
        calcButton = (Button) findViewById(R.id.calcbtn);
        monthlyPayment = (TextView) findViewById(R.id.monthlypayment);

        //add All listeners

        loanAmount.addTextChangedListener(new TextWatcher() {
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
                        loanAmount.removeTextChangedListener(this);

                        String cleanString = s.toString().replaceAll("[$,.]", "");
                        if(!cleanString.isEmpty()){
                            double parsed = (Double.parseDouble(cleanString))/100;
                            String formatted = NumberFormat.getCurrencyInstance().format((parsed));

                            current = formatted;
                            loanAmount.setText(formatted);
                            loanAmount.setSelection(formatted.length());

                            loanAmount.addTextChangedListener(this);
                            if(validateTextFields(loanAmount, loanAmountLayout)){
                                pp.setLoanAmount(parsed);
                            }
                        }

                    }

                }

        });

        downPayment.addTextChangedListener(new TextWatcher() {
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

        });

        apr.addTextChangedListener(new TextWatcher() {
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
                    String cleanString = s.toString().replaceAll("[,.% ]","");
                    if(!cleanString.isEmpty()){
                        double parsed = (Double.parseDouble(cleanString))/100;
                        String formatted = NumberFormat.getNumberInstance().format((parsed)) + " %";
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

        });

//        loanterm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.years15:
//                        break;
//                    case R.id.years20:
//                        break;
//                }
//            }
//        });

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( validateTextFields(loanAmount, loanAmountLayout) &&
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

                    String monthlyPaymentValue = NumberFormat.getCurrencyInstance().format((Calculations.calculateMonthlyPayment(pp)));
                    monthlyPayment.setText(monthlyPaymentValue);
                }
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calculation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_calc) {


        } else if (id == R.id.nav_saved_calc) {
            Intent i = new Intent(this,MapActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
