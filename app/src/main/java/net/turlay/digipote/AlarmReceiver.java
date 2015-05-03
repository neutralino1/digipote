package net.turlay.digipote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    LocationManager locationManager;
    Context context;
    @Override
    public void onReceive(Context ctxt, Intent intent) {
        context = ctxt;
        Log.d("ALARM", "hello hello");
        Location location = getLastKnownLocation();
        if (location == null) {
            Log.d("ALARM", "No location");
        } else {
            Log.d("ALARM", location.toString());
        }
    }

    protected Location getLastKnownLocation(){
        Location networkLocation = getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location gpsLocation = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation == null && networkLocation != null) return networkLocation;
        if (gpsLocation != null && networkLocation == null) return gpsLocation;
        if (gpsLocation != null && networkLocation != null) {
            if (networkLocation.getTime() > gpsLocation.getTime()) {
                return networkLocation;
            } else {
                return gpsLocation;
            }
        }
        return null;
    }

    protected LocationManager getLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        }
        return locationManager;
    }
}