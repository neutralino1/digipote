package net.turlay.digipote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BOOT", intent.getAction());
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BOOT", "YEAH!");
            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener(context);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
            //AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            //Intent intnt = new Intent(context, AlarmReceiver.class);
            //PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intnt, 0);
            //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, alarmIntent);
        }
    }

    public class MyLocationListener implements LocationListener {

        private Location previousLocation;
        private Context context;

        public MyLocationListener (Context ctxt) {
            context = ctxt;
        }

        public void onLocationChanged(Location location) {
            List<Digicode> digicodeList = Digicode.getAll(context);
            for (Digicode digicode : digicodeList) {
                if (digicode.isGeolocated()) {
                    Location digiLoc = digicode.getLocation();
                    if (location.distanceTo(digiLoc) < 500 && (previousLocation == null || previousLocation.distanceTo(digiLoc) > 500)) {
                        Log.d("LOCA", "You are within " + digicode.toString());
                    }
                }
            }
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    }
}

