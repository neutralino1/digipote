package net.turlay.digipote;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddDigicode extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_digicode);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer id = extras.getInt("digicode_id");
        }
    }

    protected void goHome(){
        Intent home = new Intent(AddDigicode.this, Digipote.class);
        startActivity(home);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_digicode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_digicode) {
            saveDigicode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveDigicode() {
        EditText nameEdit = (EditText)findViewById(R.id.edit_name);
        EditText codeEdit = (EditText)findViewById(R.id.edit_code);
        EditText streetEdit = (EditText)findViewById(R.id.edit_street);
        EditText zipCodeEdit = (EditText)findViewById(R.id.edit_zip_code);
        EditText cityEdit = (EditText)findViewById(R.id.edit_city);
        EditText countryEdit = (EditText)findViewById(R.id.edit_country);
        DigipoteDbHelper mDbHelper = new DigipoteDbHelper(AddDigicode.this);
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_NAME, nameEdit.getText().toString());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CODE, codeEdit.getText().toString());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_STREET, streetEdit.getText().toString());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_ZIP_CODE, zipCodeEdit.getText().toString());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_CITY, cityEdit.getText().toString());
        values.put(DigipoteContract.DigicodeEntry.COLUMN_NAME_COUNTRY, countryEdit.getText().toString());
        long newRowId;
        newRowId = wdb.insert(DigipoteContract.DigicodeEntry.TABLE_NAME, null, values);
        Log.d("Digipote", String.format("%s", newRowId));
        goHome();
    }
}
