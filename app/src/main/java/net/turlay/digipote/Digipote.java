package net.turlay.digipote;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
            if (fullAddress == null){
                address.setHeight(0);
            }else {
                address.setText(digicode.getFullAddress());
            }
            Log.d("sdsds", String.format("%s", digicode.getLatitude()));
            if (!digicode.isGeolocated()) {
                Log.d("lool", digicode.toString());
                ImageView geoloc = (ImageView)arg1.findViewById(R.id.geoloc_icon);
                geoloc.setVisibility(View.INVISIBLE);
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
}
