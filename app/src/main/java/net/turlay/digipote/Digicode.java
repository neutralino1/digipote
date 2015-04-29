package net.turlay.digipote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public class Digicode  implements Serializable {
    private static final long serialVersionUID = 2L;
    private String id;
    private String name;
    private String code;
    private String street;
    private String zipCode;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;

    public void setFromCursor(Cursor cursor) {
        setId(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry._ID)));
        setName(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME)));
        setCode(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE)));
        setStreet(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET)));
        setZipCode(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE)));
        setCity(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY)));
        setCountry(cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY)));
        setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_LATITUDE)));
        setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_LONGITUDE)));
    }

    public String getFullAddress() {
        if (this.street == null && this.zipCode == null && this.city == null){
            return null;
        }
        return this.street + ", " + this.zipCode + " " + this.city;
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getStreet() {
        return this.street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return this.zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String toString() {
        return "Digicode[id=" + this.id + "]";
    }

    public Double getLatitude() { return this.latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return this.longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Boolean isNew() { return getId() == null; }
    public Boolean isGeolocated() { return getLatitude() != 0. && getLongitude() != 0.; }

    public void save(Context context) {
        DigipoteDbHelper mDbHelper = new DigipoteDbHelper(context);
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME, getName());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE, getCode());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET, getStreet());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE, getZipCode());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY, getCity());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY, getCountry());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_LATITUDE, getLatitude());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_LONGITUDE, getLongitude());

        if (isNew()) {
            wdb.insert(DigipoteContract.DigicodeEntry.TABLE_NAME, null, values);
        } else {
            wdb.update(DigipoteContract.DigicodeEntry.TABLE_NAME, values, DigipoteContract.DigicodeEntry._ID + " = ?", new String[] {getId()});
        }

    }
}
