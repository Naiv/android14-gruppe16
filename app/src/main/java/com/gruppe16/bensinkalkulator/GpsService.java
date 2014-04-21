package com.gruppe16.bensinkalkulator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by Mikael on 03.04.2014.
 */

public class GpsService extends Service {
    final static String MY_DISTANCE = "MY_DISTANCE";
    private LocationManager mLocationManager = null;
    public float distance = 0;

    private class LocationListener implements android.location.LocationListener {
        Location lastLoc;
        private boolean firstRun=true;

        public LocationListener(String provider) {
            lastLoc = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            if (firstRun!=true){
                distance += lastLoc.distanceTo(location);
                Intent intent = new Intent();
                intent.setAction(MY_DISTANCE);

                intent.putExtra("DISTANCE_PASSED", distance);

                sendBroadcast(intent);
                //Toast.makeText(GpsService.this, Float.toString(distance), 3000).show();
            }
            else {
                firstRun=false;
            }
            lastLoc = location;

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, mLocationListeners[1]);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListeners[0]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                mLocationManager.removeUpdates(mLocationListeners[i]);
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}