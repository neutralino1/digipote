package net.turlay.digipote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DigipoteDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Digipote.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DigipoteContract.DigicodeEntry.TABLE_NAME + " (" +
                    DigipoteContract.DigicodeEntry._ID + " INTEGER PRIMARY KEY," +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    DigipoteContract.DigicodeEntry.COLUMN_NAME_LONGITUDE + REAL_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DigipoteContract.DigicodeEntry.TABLE_NAME;

    public DigipoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}