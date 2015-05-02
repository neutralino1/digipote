package net.turlay.digipote;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Digipote extends ActionBarActivity {

    public static String GEOCODE_API_KEY = "AIzaSyCIM7xibZYimEPLTH4z3g8w8v26x26xZOI";

    public class DigipoteAdapter extends BaseAdapter {
        List<Digicode> digicodeList = getDataForListView();

        @Override
        public int getCount() {
            return digicodeList.size();
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public Digicode getItem(int arg0) {
            return digicodeList.get(arg0);
        }

        public void setItem(int arg0, Digicode digicode) {
            digicodeList.set(arg0, digicode);
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2){
            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater) Digipote.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.digicode_list_item, arg2,false);
            }
            TextView name = (TextView)arg1.findViewById(R.id.name);
            TextView code = (TextView)arg1.findViewById(R.id.code);
            TextView address = (TextView)arg1.findViewById(R.id.address);

            Digicode digicode = digicodeList.get(arg0);
            arg1.setTag(R.id.digicode_object, digicode);

            name.setText(digicode.getName());
            code.setText(digicode.getCode());
            String fullAddress = digicode.getFullAddress();
            if (fullAddress.isEmpty()){
                address.setHeight(0);
            }else {
                address.setText(fullAddress);
            }
            Log.d("getView", digicode.toString());
            Log.d("getView", String.format("%s", digicode.getLatitude()));
            ImageView geoloc = (ImageView)arg1.findViewById(R.id.geoloc_icon);
            if (!digicode.isGeolocated()) {
                geoloc.setVisibility(View.INVISIBLE);
            } else {
                geoloc.setVisibility(View.VISIBLE);
            }
            return arg1;
        }

    }

    public List<Digicode> getDataForListView()
    {
        List<Digicode> digicodeList = new ArrayList<Digicode>();

        DigipoteDbHelper mDbHelper = new DigipoteDbHelper(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digipote);

        ListView mainList = (ListView)findViewById(R.id.main_list);
        mainList.setAdapter(new DigipoteAdapter());
        mainList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView av, View v, int i, long l) {
                editDigicode((Digicode)v.getTag(R.id.digicode_object));
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Digicode digicode = (Digicode)extras.getSerializable("digicode_object");
            if (digicode != null) {
                new GeocodeTask(this).execute(digicode);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_digipote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_digicode) {
            editDigicode(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editDigicode(Digicode digicode){
        Intent add = new Intent(Digipote.this, AddDigicode.class);
        if (digicode != null){
            add.putExtra("digicode_object", digicode);
        }
        startActivity(add);
    }

    private class GeocodeTask extends AsyncTask<Digicode, Integer, Boolean> {

        private Activity activity;
        private Digicode digicode;

        public GeocodeTask (Activity activity) {
            this.activity = activity;
        }
        @Override
        protected Boolean doInBackground(Digicode... params) {
            this.digicode = params[0];
            if (this.digicode.isGeolocated()) return null;
            String fullAddress = this.digicode.getFullAddress();
            if (fullAddress == null) return null;
            if (!Geocoder.isPresent()) return null;
            Geocoder gc = new Geocoder(this.activity);
            try {
                List<Address> list = gc.getFromLocationName(fullAddress, 1);
                Address address = list.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                Log.d("LOCATION", String.format("Geocoding successful %s %s", lat, lng));
                this.digicode.setLatitude(lat);
                this.digicode.setLongitude(lng);
                this.digicode.save(this.activity);
                return true;
            } catch (IOException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result == null) return;
            if (result) {
                ListView list = (ListView)this.activity.findViewById(R.id.main_list);
                DigipoteAdapter adapter = (DigipoteAdapter)list.getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    Log.d("this.digicode.getId()", this.digicode.getId());
                    Log.d("getItem(i).getId()", adapter.getItem(i).getId());
                    if (this.digicode.getId().equals(adapter.getItem(i).getId())) {
                        Log.d("GEOCODE", String.format("%s", this.digicode.getLatitude()));
                        adapter.setItem(i, this.digicode);
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(this.activity, "Trouvé !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.activity, "Pas trouvé :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
