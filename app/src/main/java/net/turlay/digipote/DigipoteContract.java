package net.turlay.digipote;

import android.provider.BaseColumns;

public final class DigipoteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DigipoteContract() {}

    /* Inner class that defines the table contents */
    public static abstract class DigicodeEntry implements BaseColumns {
        public static final String TABLE_NAME = "digicodes";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String[] PROJECTION = {
                DigicodeEntry._ID,
                DigicodeEntry.COLUMN_NAME_NAME,
                DigicodeEntry.COLUMN_NAME_CODE,
                DigicodeEntry.COLUMN_NAME_STREET,
                DigicodeEntry.COLUMN_NAME_ZIP_CODE,
                DigicodeEntry.COLUMN_NAME_CITY,
                DigicodeEntry.COLUMN_NAME_COUNTRY,
                DigicodeEntry.COLUMN_NAME_LATITUDE,
                DigicodeEntry.COLUMN_NAME_LONGITUDE
        };

    }
}