package com.gruppe16.bensinkalkulator;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                Fragment fr;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if (tab.getPosition()==0){
                    fr = new FragmentMain();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                }
                else{
                    fr = new FragmentLog();
                    fragmentTransaction.replace(R.id.myFrag, fr);
                    fragmentTransaction.commit();
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText("Drive").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Log").setTabListener(tabListener));
	}

}
