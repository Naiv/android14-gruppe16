package com.gruppe16.bensinkalkulator;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

public class FragmentLog extends ListFragment {
    public static TripsDataSource datasource;
    public static ArrayAdapter<Trip> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillList();
    }
    public void fillList(){
        datasource = new TripsDataSource(getActivity());
        datasource.open();

        List<Trip> values = datasource.getAllTrips();
        adapter = new ArrayAdapter<Trip>(getActivity(),R.layout.fragment_log,R.id.listText, values);
        setListAdapter(adapter);
        datasource.close();
    }
}


