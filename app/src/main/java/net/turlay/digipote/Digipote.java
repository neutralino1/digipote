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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class Digipote extends ActionBarActivity {

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

            TextView firstName = (TextView)arg1.findViewById(R.id.name);
            TextView code = (TextView)arg1.findViewById(R.id.code);
            TextView address = (TextView)arg1.findViewById(R.id.address);

            Digicode digicode = digicodeList.get(arg0);

            firstName.setText(digicode.firstName);
            code.setText(digicode.code);
            String fullAddress = digicode.getFullAddress();
            if (fullAddress == null){
                address.setHeight(0);
            }else {
                address.setText(digicode.getFullAddress());
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
                Log.d("aaa", "eee");
                editDigicode(null);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_digicode) {
            editDigicode(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editDigicode(Integer id){
        Intent add = new Intent(Digipote.this, AddDigicode.class);
        if (id != null){
            add.putExtra("digicode_id", id);
        }
        startActivity(add);
    }
}
