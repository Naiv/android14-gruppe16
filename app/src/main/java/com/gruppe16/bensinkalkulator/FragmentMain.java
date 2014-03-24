package com.gruppe16.bensinkalkulator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMain extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fragment fr;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fr = new FragmentMainNew();
        fragmentTransaction.replace(R.id.myFragMain, fr);
        fragmentTransaction.commit();

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}


