package com.gcekta.mortgagecalculator.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.util.Log;

import com.gcekta.mortgagecalculator.model.PropertyPojo;

import java.util.*;

/**
 * Created by ekta2803 on 3/11/17.
 */

public class PropertyDataSource {

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public PropertyDataSource(Context context){
        dbHelper = new DbHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    /*
    * Method to insert property details into DB
    * parameter: PropertyPojo
    * return: void
    * */
    public boolean createPropertyInfo(PropertyPojo property){

        ContentValues values = new ContentValues();
        values.put(TableDetails.COLUMN_PROP_TYPE,property.getPropertyType());
        values.put(TableDetails.COLUMN_PROP_ADDRESS,property.getAddress());
        values.put(TableDetails.COLUMN_PROP_CITY,property.getCity());
        values.put(TableDetails.COLUMN_PROP_STATE,property.getState());
        values.put(TableDetails.COLUMN_PROP_ZIPCODE,property.getZipcode());
        values.put(TableDetails.COLUMN_LOAN_AMT,property.getPropertyPrice());
        values.put(TableDetails.COLUMN_LOAN_DWN_PYMT,property.getDownPayment());
        values.put(TableDetails.COLUMN_LOAN_APR,property.getApr());
        values.put(TableDetails.COLUMN_LOAN_TERMS,property.getLoanTerms());
        values.put(TableDetails.COLUMN_MONTHLY_PMT,property.getMonthlyPayment());
        long status = database.insert(TableDetails.TABLE_NAME,null,values);
        if(status == -1){
            return false;
        }
        return true;

    }

    /*
    * Method to get the property details based on the id from DB
    * parameter: property id
    * return: PropertyPojo object
    * */
    public PropertyPojo getProperty(int id){

        Cursor cursor = database.query(TableDetails.TABLE_NAME, new String[] { TableDetails.COLUMN_PROP_ID,
                        TableDetails.COLUMN_PROP_TYPE, TableDetails.COLUMN_PROP_ADDRESS,TableDetails.COLUMN_PROP_CITY,
                        TableDetails.COLUMN_PROP_STATE,TableDetails.COLUMN_PROP_ZIPCODE,TableDetails.COLUMN_LOAN_AMT,
                        TableDetails.COLUMN_LOAN_DWN_PYMT,TableDetails.COLUMN_LOAN_APR,TableDetails.COLUMN_LOAN_TERMS,TableDetails.COLUMN_MONTHLY_PMT},
                        TableDetails.COLUMN_PROP_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            PropertyPojo property = new PropertyPojo();
            property.setPropertyId(Integer.parseInt(cursor.getString(0)));
            property.setPropertyType(cursor.getString(1));
            property.setAddress(cursor.getString(2));
            property.setCity(cursor.getString(3));
            property.setState(cursor.getString(4));
            property.setZipcode(cursor.getString(5));
            property.setPropertyPrice(Double.parseDouble(cursor.getString(6)));
            property.setDownPayment(Double.parseDouble(cursor.getString(7)));
            property.setApr(Double.parseDouble(cursor.getString(8)));
            property.setLoanTerms(Integer.parseInt(cursor.getString(9)));
            property.setMonthlyPayment(Double.parseDouble(cursor.getString(10)));
            return property;
        }else{
            return null;
        }

    }

    /*
    * Method to get the list of all the properties from DB
    * parameter: null
    * return: List of PropertyPojo object
    * */
    public List<PropertyPojo> getAllProperties(){
        List<PropertyPojo> propertyList = new ArrayList<PropertyPojo>();
        String selectQuery = "SELECT  * FROM " + TableDetails.TABLE_NAME;
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PropertyPojo property = new PropertyPojo();
                property.setPropertyId(Integer.parseInt(cursor.getString(0)));
                property.setPropertyType(cursor.getString(1));
                property.setAddress(cursor.getString(2));
                property.setCity(cursor.getString(3));
                property.setState(cursor.getString(4));
                property.setZipcode(cursor.getString(5));
                property.setPropertyPrice(Double.parseDouble(cursor.getString(6)));
                property.setDownPayment(Double.parseDouble(cursor.getString(7)));
                property.setApr(Double.parseDouble(cursor.getString(8)));
                property.setLoanTerms(Integer.parseInt(cursor.getString(9)));
                property.setMonthlyPayment(Double.parseDouble(cursor.getString(10)));
                // Adding contact to list
                propertyList.add(property);
            } while (cursor.moveToNext());
        }

        return propertyList;
    }

    /*
   * Method to update the property details
   * parameter: PropertyPogo object
   * return: number of rows affected
   * */
    public int updateProperty(PropertyPojo property){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableDetails.COLUMN_PROP_TYPE,property.getPropertyType());
        values.put(TableDetails.COLUMN_PROP_ADDRESS,property.getAddress());
        values.put(TableDetails.COLUMN_PROP_CITY,property.getCity());
        values.put(TableDetails.COLUMN_PROP_STATE,property.getState());
        values.put(TableDetails.COLUMN_PROP_ZIPCODE,property.getZipcode());
        values.put(TableDetails.COLUMN_LOAN_AMT,property.getPropertyPrice());
        values.put(TableDetails.COLUMN_LOAN_DWN_PYMT,property.getDownPayment());
        values.put(TableDetails.COLUMN_LOAN_APR,property.getApr());
        values.put(TableDetails.COLUMN_LOAN_TERMS,property.getLoanTerms());
        values.put(TableDetails.COLUMN_MONTHLY_PMT,property.getMonthlyPayment());
        return database.update(TableDetails.TABLE_NAME,values, TableDetails.COLUMN_PROP_ID + "=?",
                new String[] { String.valueOf(property.getPropertyId()) });
    }

    /*
   * Method to delete the property
   * parameter: PropertyPogo object
   * return: void
   * */
    public int deleteProperty(PropertyPojo property){
        database = dbHelper.getWritableDatabase();
        return database.delete(TableDetails.TABLE_NAME, TableDetails.COLUMN_PROP_ID+"=?",
                new String[] { String.valueOf(property.getPropertyId()) });
    }

    public void deleteAll(){
        database = dbHelper.getWritableDatabase();
        int status = database.delete(TableDetails.TABLE_NAME,"1",null);
    }


}
