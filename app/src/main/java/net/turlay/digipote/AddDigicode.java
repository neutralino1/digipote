package net.turlay.digipote;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AddDigicode extends ActionBarActivity implements OnMapReadyCallback {

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
        setupActionBar();
        Bundle extras = getIntent().getExtras();
        loadViews();
        if (extras != null) {
            this.digicode = (Digicode) extras.getSerializable("digicode_object");
            if (this.digicode != null) {
                loadValues();
            }
        } else {
            this.digicode = new Digicode();
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public Digicode getDigicode() {
        return this.digicode;
    }

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        View menuView = getLayoutInflater().inflate(R.layout.add_digicode_menu, null);
        ImageButton saveButton = (ImageButton)menuView.findViewById(R.id.action_save_digicode);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDigicode();
            }
        });
        ImageButton deleteButton = (ImageButton)menuView.findViewById(R.id.action_delete_digicode);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDigicode.this);
                builder.setMessage(R.string.delete_digicode_confirm);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDigicode.this.getDigicode().delete(AddDigicode.this);
                        Toast.makeText(AddDigicode.this, R.string.digicode_was_deleted, Toast.LENGTH_SHORT).show();
                        goHome(null);
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.create().show();
            }
        });
        actionBar.setCustomView(menuView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    protected void loadViews(){
        setContentView(R.layout.add_digicode);
        this.nameEdit = (EditText)findViewById(R.id.edit_name);
        this.codeEdit = (EditText)findViewById(R.id.edit_code);
        this.streetEdit = (EditText)findViewById(R.id.edit_street);
        //this.zipCodeEdit = (EditText)findViewById(R.id.edit_zip_code);
        this.cityEdit = (EditText)findViewById(R.id.edit_city);
        this.countryEdit = (EditText)findViewById(R.id.edit_country);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("map", "3");
        map.setMyLocationEnabled(false);
        LatLng latLng;
        int zoom;
        if (this.digicode.isGeolocated()) {
            latLng = this.digicode.getLatLng();
            zoom = 16;
        } else {
            latLng = new LatLng(48.8567, 2.3508);
            zoom = 11;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.animateCamera(cameraUpdate);
        if (this.digicode.isGeolocated()) {
            map.addMarker(new MarkerOptions().position(latLng));
        }
    }

    protected void loadValues(){
        this.nameEdit.setText(this.digicode.getName());
        this.codeEdit.setText(this.digicode.getCode());
        this.streetEdit.setText(this.digicode.getStreet());
        //this.zipCodeEdit.setText(this.digicode.getZipCode());
        this.cityEdit.setText(this.digicode.getCity());
        this.countryEdit.setText(this.digicode.getCountry());
        setTitle(R.string.edit_digicode_title);
    }

    protected void goHome(Digicode digicode){
        Intent home = new Intent(AddDigicode.this, Digipote.class);
        if (digicode != null) {
            home.putExtra("digicode_object", digicode);
        }
        startActivity(home);
        finish();
    }

    public void saveDigicode() {
        this.digicode.setName(this.nameEdit.getText().toString());
        this.digicode.setCode(this.codeEdit.getText().toString());
        this.digicode.setStreet(this.streetEdit.getText().toString());
        //this.digicode.setZipCode(this.zipCodeEdit.getText().toString());
        this.digicode.setCity(this.cityEdit.getText().toString());
        this.digicode.setCountry(this.countryEdit.getText().toString());
        if (this.digicode.save(this) != null) {
            Toast.makeText(this, "Nice!", Toast.LENGTH_SHORT).show();
        }
        goHome(this.digicode);
    }

}
