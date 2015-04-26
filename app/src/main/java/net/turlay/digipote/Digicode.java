package net.turlay.digipote;

import android.database.Cursor;

public class Digicode {
    String firstName;
    String code;
    String street;
    String zipCode;
    String city;
    String country;

    public void setFromCursor(Cursor cursor) {
        this.firstName = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME));
        this.code = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE));
        this.street = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET));
        this.zipCode = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE));
        this.city = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY));
        this.country = cursor.getString(cursor.getColumnIndexOrThrow(DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY));
    }

    public String getFullAddress() {
        if (this.street == null && this.zipCode == null && this.city == null){
            return null;
        }
        return this.street + ", " + this.zipCode + " " + this.city;
    }
}
