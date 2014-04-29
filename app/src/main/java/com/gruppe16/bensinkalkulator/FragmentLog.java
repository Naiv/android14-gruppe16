package com.gruppe16.bensinkalkulator;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;
//listFragment for fragment_log.xml
public class FragmentLog extends ListFragment {
    public static TripsDataSource datasource;
    public static ArrayAdapter<Trip> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillList();
    }
    //bruker TripsDataSource for å hente ut turene fra databasen, og adapter til å fylle listFragmentet med.
    public void fillList(){
        datasource = new TripsDataSource(getActivity());
        datasource.open();

        List<Trip> values = datasource.getAllTrips();
        adapter = new ArrayAdapter<Trip>(getActivity(),R.layout.fragment_log,R.id.listText, values);
        setListAdapter(adapter);
        datasource.close();
    }
}


