package com.gruppe16.bensinkalkulator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

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
}
