package com.gruppe16.bensinkalkulator;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

public class FragmentLog extends ListFragment {
    private TripsDataSource datasource;

    //mulig redundant
    public static FragmentLog newInstance() {
        return new FragmentLog();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        datasource = new TripsDataSource(getActivity());
        datasource.open();

        List<Trip> values = datasource.getAllTrips();
        ArrayAdapter<Trip> adapter = new ArrayAdapter<Trip>(getActivity(),R.layout.fragment_log,R.id.listText, values);
        setListAdapter(adapter);
    }
}


