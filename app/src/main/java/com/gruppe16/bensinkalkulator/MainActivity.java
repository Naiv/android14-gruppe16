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

//hovedklasse for programmet
public class MainActivity extends Activity {
    public static EditText textSetConsumption;
    public static EditText textSetFuelCost;
    public static EditText textSetPassengers;
    public static float finalCost = 0;
    public static float finalDistance = 0;
    ProgressDialog ringProgressDialog;
    public static boolean active = false;
    public static boolean tracking = false;

    GpsReceiver gpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bygger actionbar og setter TabListener på den.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Fragment fr;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                //dersom posisjon på tab er 0, inflate FragmentMain (siden der tracking og ny tur utføres)
                if (tab.getPosition() == 0) {
                    fr = new FragmentMain();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                    active = true;
                    //dersom posisjon på tab er 1, inflate FragmentLog (siden der gamle turer vises)
                } else {
                    fr = new FragmentLog();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                    active = false;
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

    //inflater menyen brukt for å tømme loggen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //tømmer først datasource (altså databasen), deretter adapteren som brukes av listView for å vise frem turene.
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

    //dersom av/på knappen blir trykket...
    public void onToggleTracking(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        //...og status på den blir "enabled"/"on", så...
        if (on) {
            tracking = true;
            //...sjekker om GPS er påskrudd, hvis ikke viser en Toast og åpner vindu der du kan skru den på.
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent2);
                Toast.makeText(this, "Turn on GPS...", 3000).show();
                ((ToggleButton) view).setChecked(false);
                return;
            }
            textSetConsumption = (EditText) findViewById(R.id.fuelConValue);
            textSetFuelCost = (EditText) findViewById(R.id.fuelCostPrice);
            textSetPassengers = (EditText) findViewById(R.id.passNum);
            if (textSetConsumption.getText().toString().matches("") || textSetFuelCost.getText().toString().matches("") || textSetPassengers.getText().toString().matches("") || textSetPassengers.getText().toString().matches("0")) {
                Toast.makeText(this, "Fill inn all fields.", 3000).show();
                ((ToggleButton) view).setChecked(false);
            } else {
                //inflater fragment for "on the fly" tracking og starter en ProgressDialog
                ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Finding position ...", true);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fr = new FragmentMainTrack();
                fragmentTransaction.replace(R.id.myFragMain, fr);
                fragmentTransaction.commit();

                //starter service og mottaker for å måle avstand kjørt med GPS og å kunne ta imot data.
                Intent intent = new Intent(MainActivity.this, GpsService.class);
                gpsReceiver = new GpsReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(GpsService.MY_DISTANCE);
                registerReceiver(gpsReceiver, intentFilter);
                startService(intent);
            }

        } else {
            //...og status på den blir "disabled"/"off", så lagres turen i databasen, og programmet klargjøres for ny tur (gjemmer ToggleButton og viser Button). Stopper også servicen
            tracking = false;
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
        }
    }

    //gjemmer button og viser togglebutton. deretter inflater fragment for ny tur.
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

    //mottaker som håndterer intents med ekstra fra service.
    private class GpsReceiver extends BroadcastReceiver {

        //ekstra er avstanden målt til nå. deretter fyller den data inn i textfields som viser utregnede verdier "on the fly"
        @Override
        public void onReceive(Context context, Intent intent) {
            ringProgressDialog.dismiss();
            //gjør utregninger basert på data mottatt fra service og data skrevet inn av bruker.
            float distance = (intent.getFloatExtra("DISTANCE_PASSED", 0) / 1000);
            float currentDistance = changeDecimals(distance, 2);
            float currentConsumption = (currentDistance / 10) * Float.parseFloat(MainActivity.textSetConsumption.getText().toString());
            float currentTotalCost = (currentConsumption * Float.parseFloat(MainActivity.textSetFuelCost.getText().toString()));
            int passengers = Integer.parseInt(MainActivity.textSetPassengers.getText().toString());
            float currentPersonCost = currentTotalCost / passengers;
            //dersom FragmentMainTrack er aktiv oppdateres data i vinduet bruker ser ("on the fly")
            if (active) {
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
            }
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
