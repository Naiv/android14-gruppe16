package com.gruppe16.bensinkalkulator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.NumberFormat;

public class MainActivity extends Activity {
    float distance;
    GpsReceiver gpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                Fragment fr;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if (tab.getPosition() == 0) {
                    fr = new FragmentMain();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                } else {
                    fr = new FragmentLog();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
        actionBar.addTab(actionBar.newTab().setText("Drive").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Log").setTabListener(tabListener));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.emptyLog) {
            FragmentLog.datasource = new TripsDataSource(this);
            FragmentLog.datasource.open();
            FragmentLog.datasource.deleteAll();
            FragmentLog.datasource.close();
            FragmentLog.adapter.clear();
            FragmentLog.adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addDummy(View view) {
        FragmentLog.datasource = new TripsDataSource(this);
        FragmentLog.datasource.open();
        FragmentLog.datasource.createTrip("23.08.1991 kl 14:01", 202, 23);
        FragmentLog.datasource.close();
    }

    public void onToggleTracking(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent2);
                Toast.makeText(this, "Turn on GPS...", 3000).show();
                ((ToggleButton) view).setChecked(false);
                return;
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Fragment fr = new FragmentMainTrack();
            fragmentTransaction.replace(R.id.myFragMain, fr);
            fragmentTransaction.commit();

            Intent intent = new Intent(MainActivity.this, GpsService.class);
            gpsReceiver = new GpsReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GpsService.MY_DISTANCE);
            registerReceiver(gpsReceiver, intentFilter);
            startService(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, GpsService.class);
            stopService(intent);
            //gpsReceiver = new GpsReceiver();
            //unregisterReceiver(gpsReceiver);
        }
    }

    private class GpsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            distance = intent.getFloatExtra("DISTANCE_PASSED", 0);
            String strDistance = format.format(distance/1000);
            TextView t=(TextView)findViewById(R.id.driven);
            t.setText("Driven: "+strDistance+"km");
            //Toast.makeText(MainActivity.this, Float.toString(distance), Toast.LENGTH_LONG).show();

        }

    }
}
