package com.gruppe16.bensinkalkulator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    public static EditText textSetConsumption;
    public static EditText textSetFuelCost;
    public static EditText textSetPassengers;
    public static float finalCost = 0;
    public static float finalDistance = 0;
    ProgressDialog ringProgressDialog;

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
            if (FragmentLog.adapter != null) {
                FragmentLog.adapter.clear();
                FragmentLog.adapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
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
            ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Finding position ...", true);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Fragment fr = new FragmentMainTrack();
            fragmentTransaction.replace(R.id.myFragMain, fr);
            fragmentTransaction.commit();

            //TODO: Check for null
            textSetConsumption = (EditText) findViewById(R.id.fuelConValue);
            textSetFuelCost = (EditText) findViewById(R.id.fuelCostPrice);
            textSetPassengers = (EditText) findViewById(R.id.passNum);

            Intent intent = new Intent(MainActivity.this, GpsService.class);
            gpsReceiver = new GpsReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GpsService.MY_DISTANCE);
            registerReceiver(gpsReceiver, intentFilter);
            startService(intent);
        } else {
            ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
            tb.setVisibility(View.GONE);
            Button b1 = (Button) findViewById(R.id.newTrip);
            b1.setVisibility(View.VISIBLE);
            FragmentLog.datasource = new TripsDataSource(this);
            Date d = new Date();
            String finalDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(d);
            FragmentLog.datasource.open();
            FragmentLog.datasource.createTrip(finalDate, Math.round(finalDistance), Math.round(finalCost));
            FragmentLog.datasource.close();
            Intent intent = new Intent(MainActivity.this, GpsService.class);
            stopService(intent);
            //gpsReceiver = new GpsReceiver();
            //unregisterReceiver(gpsReceiver);
        }
    }

    public void newTrip(View view) {
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
        tb.setVisibility(View.VISIBLE);
        Button b1 = (Button) findViewById(R.id.newTrip);
        b1.setVisibility(View.GONE);

        Fragment fr = new FragmentMainNew();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.myFragMain, fr);
        fragmentTransaction.commit();
    }

    private class GpsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ringProgressDialog.dismiss();
            float distance = (intent.getFloatExtra("DISTANCE_PASSED", 0) / 1000);

            float currentDistance = changeDecimals(distance, 2);
            float currentConsumption = (currentDistance / 10) * Float.parseFloat(MainActivity.textSetConsumption.getText().toString());
            float currentTotalCost = (currentConsumption * Float.parseFloat(MainActivity.textSetFuelCost.getText().toString()));
            int passengers = Integer.parseInt(MainActivity.textSetPassengers.getText().toString());
            float currentPersonCost = currentTotalCost / passengers;

            TextView textDriven = (TextView) findViewById(R.id.distanceNow);
            TextView textConsumption = (TextView) findViewById(R.id.fuelConsumptionNow);
            TextView textTotalCost = (TextView) findViewById(R.id.fuelCostTotalNow);
            TextView textPersonCost = (TextView) findViewById(R.id.fuelCostPersonNow);
            TextView textPassengers = (TextView) findViewById(R.id.passengersNow);
            textDriven.setText("Driven: " + currentDistance + "km");
            textConsumption.setText("Consumption: " + changeDecimals(currentConsumption, 2) + "L");
            textTotalCost.setText("Cost: " + changeDecimals(currentTotalCost, 2) + ",-");
            textPersonCost.setText("Individual cost: " + changeDecimals(currentPersonCost, 2) + ",-");
            textPassengers.setText("Passengers: " + passengers);
            //Toast.makeText(MainActivity.this, Float.toString(distance), Toast.LENGTH_LONG).show();
            finalDistance = currentDistance;
            finalCost = changeDecimals(currentTotalCost, 2);

        }

        //setter antall desimaler og erstatter komma med punktum
        public float changeDecimals(float number, int decimals) {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(decimals);
            format.setMaximumFractionDigits(decimals);
            String strNumber = format.format(number);
            strNumber = strNumber.replace(',', '.');
            return Float.parseFloat(strNumber);
        }

    }
}
