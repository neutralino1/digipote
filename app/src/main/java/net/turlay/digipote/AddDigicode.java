package net.turlay.digipote;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class AddDigicode extends ActionBarActivity {

    private Digicode digicode;
    private EditText nameEdit;
    private EditText codeEdit;
    private EditText streetEdit;
    private EditText zipCodeEdit;
    private EditText cityEdit;
    private EditText countryEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.digicode = (Digicode) extras.getSerializable("digicode_object");
            if (this.digicode != null) {
                loadValues();
            }
        }
    }

    protected void loadViews(){
        setContentView(R.layout.add_digicode);
        this.nameEdit = (EditText)findViewById(R.id.edit_name);
        this.codeEdit = (EditText)findViewById(R.id.edit_code);
        this.streetEdit = (EditText)findViewById(R.id.edit_street);
        this.zipCodeEdit = (EditText)findViewById(R.id.edit_zip_code);
        this.cityEdit = (EditText)findViewById(R.id.edit_city);
        this.countryEdit = (EditText)findViewById(R.id.edit_country);
    }

    protected void loadValues(){
        this.nameEdit.setText(this.digicode.getName());
        this.codeEdit.setText(this.digicode.getCode());
        this.streetEdit.setText(this.digicode.getStreet());
        this.zipCodeEdit.setText(this.digicode.getZipCode());
        this.cityEdit.setText(this.digicode.getCity());
        this.countryEdit.setText(this.digicode.getCountry());
        setTitle(R.string.edit_digicode_title);
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
        if (id == R.id.action_save_digicode) {
            saveDigicode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveDigicode() {
        if (this.digicode == null) {
            this.digicode = new Digicode();
        }
        this.digicode.setName(this.nameEdit.getText().toString());
        this.digicode.setCode(this.codeEdit.getText().toString());
        this.digicode.setStreet(this.streetEdit.getText().toString());
        this.digicode.setZipCode(this.zipCodeEdit.getText().toString());
        this.digicode.setCity(this.cityEdit.getText().toString());
        this.digicode.setCountry(this.countryEdit.getText().toString());
        this.digicode.save(this);
        new GeocodeTask(this).execute(this.digicode);
        Toast.makeText(this, "Nice!", Toast.LENGTH_SHORT).show();
        goHome();
    }

    private class GeocodeTask extends AsyncTask<Digicode, Integer, Boolean> {

        private Context context;

        public GeocodeTask (Context context) {
            this.context = context;
        }
        @Override
        protected Boolean doInBackground(Digicode... params) {
            Digicode digicode = params[0];
            if (digicode.getStreet() == null || digicode.getStreet() == "") return false;
            if (!Geocoder.isPresent()) return false;
            Geocoder gc = new Geocoder(this.context);
            try {
                List<Address> list = gc.getFromLocationName(digicode.getFullAddress(), 1);
                Address address = list.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                Log.d("LOCATION", String.format("%s %s", lat, lng));
                digicode.setLatitude(lat);
                digicode.setLongitude(lng);
                digicode.save(this.context);
                return true;
            } catch (IOException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(this.context, "Trouvé !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.context, "Pas trouvé :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
