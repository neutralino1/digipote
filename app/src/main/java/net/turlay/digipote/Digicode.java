package net.turlay.digipote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.BoringLayout;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Digicode  implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String tableName = DigipoteContract.DigicodeEntry.TABLE_NAME;
    private String id;
    private String name;
    private String code;
    private String street;
    private String zipCode;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean changed = false;
    private transient LatLng latLng;

    public Digicode () {
        this.changed = false;
    }

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
        this.changed = false;
    }

    public Boolean hasChanged(){
        return this.changed;
    }

    public String getFullAddress() {
        if ((getStreet().isEmpty() && getZipCode().isEmpty() && getCity().isEmpty())){
            return "";
        }
        return getStreet() + ", " + getZipCode() + " " + getCity();
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        if (id != null && id.equals(this.id)) return;
        this.changed = true;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        if (!sanitizeStringAttribute(name, this.name)) return;
        this.changed = true;
        this.name = name;
    }

    protected Boolean sanitizeStringAttribute(String nnew, String old) {
        return !((nnew != null && nnew.equals(old)) || (nnew != null && nnew.isEmpty() && old == null));
    }

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        if (!sanitizeStringAttribute(code, this.code)) return;
        this.changed = true;
        this.code = code;
    }

    public String getStreet() {
        if (this.street == null) return "";
        return this.street;
    }
    public void setStreet(String street) {
        if (!sanitizeStringAttribute(street, this.street)) return;
        this.changed = true;
        if (street != null && street.isEmpty()) {
            this.street = null;
        } else {
            this.street = street;
        }
    }

    public String getZipCode() {
        if (this.zipCode == null) return "";
        return this.zipCode;
    }
    public void setZipCode(String zipCode) {
        if (!sanitizeStringAttribute(zipCode, this.zipCode)) return;
        this.changed = true;
        if (zipCode != null && zipCode.isEmpty()) {
            this.zipCode = null;
        } else {
            this.zipCode = zipCode;
        }
    }

    public String getCity() {
        if (this.city == null) return "";
        return this.city;
    }
    public void setCity(String city) {
        if (!sanitizeStringAttribute(city, this.city)) return;
        this.changed = true;
        if (city != null && city.isEmpty()) {
            this.city = null;
        } else {
            this.city = city;
        }
    }

    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        if (!sanitizeStringAttribute(country, this.country)) return;
        this.changed = true;
        this.country = country;
    }

    public String toString() {
        return "Digicode[id=" + this.id + "]";
    }

    public Double getLatitude() {
        if (this.latitude == null || this.latitude == 0.0) return null;
        return this.latitude;
    }
    public void setLatitude(Double latitude) {
        if (latitude != null && latitude.equals(this.latitude)) return;
        this.changed = true;
        this.latitude = latitude;
        this.latLng = null;
    }

    public Double getLongitude() {
        if (this.latitude == null || this.longitude == 0.0) return null;
        return this.longitude;
    }
    public void setLongitude(Double longitude) {
        if (longitude != null && longitude.equals(this.longitude)) return;
        this.changed = true;
        this.longitude = longitude;
        this.latLng = null;
    }

    public LatLng getLatLng() {
        if (this.latLng == null) {
            this.latLng = new LatLng(this.latitude, this.longitude);
        }
        return this.latLng;
    }

    public Boolean isNew() { return getId() == null; }
    public Boolean isGeolocated() {
        return getLatitude() != null && getLongitude() != null; }

    protected SQLiteDatabase getWritableDB(Context context) {
        DigipoteDbHelper mDbHelper = new DigipoteDbHelper(context);
        return mDbHelper.getWritableDatabase();
    }

    public String getTableName(){
        return this.tableName;
    }

    public Boolean delete(Context context) {
        SQLiteDatabase wdb = getWritableDB(context);
        wdb.delete(getTableName(), DigipoteContract.DigicodeEntry._ID + " = ?", new String[] {getId()});
        return true;
    }

    public Boolean save(Context context) {
        if (!hasChanged()) return null;
        SQLiteDatabase wdb = getWritableDB(context);
        if (isNew()) {
            Long id = wdb.insert(getTableName(), null, getContentValues());
            if (id != -1) {
                setId(Objects.toString(id));
                this.changed = false;
                return true;
            }
            else return false;
        } else {
            wdb.update(getTableName(), getContentValues(), DigipoteContract.DigicodeEntry._ID + " = ?", new String[] {getId()});
            this.changed = false;
            return true;
        }

    }

    protected ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME, getName());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE, getCode());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET, getStreet());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE, getZipCode());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY, getCity());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY, getCountry());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_LATITUDE, getLatitude());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_LONGITUDE, getLongitude());
        return values;
    }

    public Location getLocation() {
        if (!isGeolocated()) return null;
        Location location = new Location("");
        location.setLatitude(getLatitude());
        location.setLongitude(getLongitude());
        return location;
    }

    public static List<Digicode> getAll(Context context){
        List<Digicode> digicodeList = new ArrayList<Digicode>();

        DigipoteDbHelper mDbHelper = new DigipoteDbHelper(context);
        SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        Cursor cursor = rdb.query(DigipoteContract.DigicodeEntry.TABLE_NAME, DigipoteContract.DigicodeEntry.PROJECTION, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Digicode digicode = new Digicode();
            digicode.setFromCursor(cursor);
            digicodeList.add(digicode);
            cursor.moveToNext();
        }
        cursor.close();
        return digicodeList;
    }
}
