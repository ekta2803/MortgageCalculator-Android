package com.gcekta.mortgagecalculator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    
}
