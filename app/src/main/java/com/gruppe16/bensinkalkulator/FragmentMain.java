package com.gruppe16.bensinkalkulator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

//fragment for fragment_main.xml
public class FragmentMain extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fragment fr;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        //dersom tracking er deaktivert, inflate FragmentMainNew.
        if(!MainActivity.tracking){
            fr = new FragmentMainNew();
        }
        //dersom tracking er aktivert, inflate FragmentMainTrack.
        else{
            fr = new FragmentMainTrack();
        }
        fragmentTransaction.replace(R.id.myFragMain, fr);
        fragmentTransaction.commit();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    //kjører etter at layout er opprettet
    public void onActivityCreated (Bundle savedInstanceState){
        //sørger for at ToggleButton beholder sin "state" gjennom ny inflatering
        ToggleButton tButton = (ToggleButton) getView().findViewById(R.id.toggleButton);
        if(MainActivity.tracking){
            tButton.setChecked(true);
        }
        else{
            tButton.setChecked(false);
        }
        super.onActivityCreated(savedInstanceState);

    }
}


