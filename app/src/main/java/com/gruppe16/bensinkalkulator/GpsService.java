package com.gruppe16.bensinkalkulator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

//service for å logge avstand kjørt ved bruk av GPS
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

        //når posisjonen endrer seg, oppdateres avstanden kjørt i service og en intent med den nye avstanden blir sendt tilbake til appen.
        @Override
        public void onLocationChanged(Location location) {
            if (firstRun!=true){
                distance += lastLoc.distanceTo(location);
                Intent intent = new Intent();
                intent.setAction(MY_DISTANCE);
                intent.putExtra("DISTANCE_PASSED", distance);
                sendBroadcast(intent);
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

    //bruker GPS for å finne posisjon. valgt å ikke bruke nettverk også, da dette viste seg å være VELDIG unøyaktig.
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
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

    //posisjonen blir ansett som "ny" når den finner en som er over 10meter unna den forrige lagrede.
    @Override
    public void onCreate() {
        initializeLocationManager();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, mLocationListeners[0]);
    }

    //tømmer gamle posisjoner dersom servicen avsluttes
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                mLocationManager.removeUpdates(mLocationListeners[i]);
            }
        }
    }

    //starter opp LocationManager
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}