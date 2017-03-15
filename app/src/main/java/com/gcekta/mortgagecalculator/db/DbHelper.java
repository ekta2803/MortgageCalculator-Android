package com.gcekta.mortgagecalculator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ekta2803 on 3/11/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_CREATE="Create table if not exists"+TableDetails.TABLE_NAME +
            "("+TableDetails.COLUMN_PROP_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableDetails.COLUMN_PROP_TYPE +" TEXT, " +
            TableDetails.COLUMN_PROP_ADDRESS +" TEXT," +
            TableDetails.COLUMN_PROP_CITY+" TEXT, " +
            TableDetails.COLUMN_PROP_STATE +" TEXT," +
            TableDetails.COLUMN_PROP_ZIPCODE+" TEXT," +
            TableDetails.COLUMN_LOAN_AMT+" REAL," +
            TableDetails.COLUMN_LOAN_DWN_PYMT+" REAL," +
            TableDetails.COLUMN_LOAN_APR+" REAL," +
            TableDetails.COLUMN_LOAN_TERMS+" INTEGER)";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, "morgage_calci", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TableDetails.TABLE_NAME);
        onCreate(db);
    }
}
