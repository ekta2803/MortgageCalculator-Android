package com.gcekta.mortgagecalculator.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gcekta.mortgagecalculator.R;
import com.gcekta.mortgagecalculator.activities.CalculationActivity;
import com.gcekta.mortgagecalculator.activities.MapActivity;
import com.gcekta.mortgagecalculator.db.PropertyDataSource;
import com.gcekta.mortgagecalculator.model.PropertyPojo;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Created by gauravchodwadia on 3/16/17.
 */

public class MapDialog extends Dialog {
    private MapActivity mapActivity;
    private Marker marker;
    private Button btnEdit, btnDelete;
    private PropertyPojo pojoObj;

    public MapDialog(Activity activity, Marker marker) {
        super(activity);
        this.marker = marker;
        mapActivity = (MapActivity) activity;
        pojoObj = mapActivity.getPojoObj();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_marker_dialog);


        TextView propType = (TextView) findViewById(R.id.property_type_val);
        propType.setText(pojoObj.getPropertyType());

        TextView propAddr = (TextView) findViewById(R.id.property_addr_val);
        propAddr.setText(pojoObj.getAddress());

        TextView city = (TextView) findViewById(R.id.city_val);
        city.setText(pojoObj.getCity());

        TextView state = (TextView) findViewById(R.id.state_val);
        state.setText(pojoObj.getState());

        TextView zipcode = (TextView) findViewById(R.id.zipcode_val);
        zipcode.setText(pojoObj.getZipcode());

        TextView loanAmt = (TextView) findViewById(R.id.property_price_val);
        loanAmt.setText(NumberFormat.getCurrencyInstance().format((pojoObj.getPropertyPrice())));

        TextView dwnPmt = (TextView) findViewById(R.id.down_pmt_val);
        dwnPmt.setText(NumberFormat.getCurrencyInstance().format((pojoObj.getDownPayment())));

        TextView apr = (TextView) findViewById(R.id.apr_val);
        apr.setText(String.valueOf(pojoObj.getApr())+" %");

        TextView term = (TextView) findViewById(R.id.term_val);
        term.setText(String.valueOf(pojoObj.getLoanTerms()+" years"));

        TextView monthlyPmt = (TextView) findViewById(R.id.monthly_pmt_val);
        monthlyPmt.setText(NumberFormat.getCurrencyInstance().format((pojoObj.getMonthlyPayment())));

        btnEdit = (Button) findViewById(R.id.edit_prop);
        btnDelete = (Button) findViewById(R.id.delete_prop);

        View.OnClickListener btnListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.edit_prop:
                        Intent intentForCalculation = new Intent(mapActivity.getApplicationContext(), CalculationActivity.class);
                        intentForCalculation.putExtra("PPPOJO", (Serializable) pojoObj);
                        mapActivity.startActivity(intentForCalculation);
                        break;
                    case R.id.delete_prop:

                        PropertyDataSource database = new PropertyDataSource(mapActivity.getApplicationContext());
                        database.open();
                        if(database.deleteProperty(pojoObj) == 1){
                            MapDialog.this.hide();
                            marker.remove();
                            Toast.makeText(mapActivity.getApplicationContext(), R.string.deleted_message, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(mapActivity.getApplicationContext(), R.string.something_wrong_msg, Toast.LENGTH_LONG).show();
                        }
                        database.close();

                        break;
                }
            }
        };

        btnEdit.setOnClickListener(btnListner);
        btnDelete.setOnClickListener(btnListner);
    }

}
