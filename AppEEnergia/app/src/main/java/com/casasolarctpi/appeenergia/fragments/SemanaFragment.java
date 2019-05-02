package com.casasolarctpi.appeenergia.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.MenuActivity;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class SemanaFragment extends Fragment {
    
    View view;
    private DatabaseReference datosDia;


    public SemanaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_semana, container, false);
        inizialite();
        inizialiteValues();
        return view;
    }

    private void inizialite() {
    }

    private void inizialiteValues() {
        switch (ConsultasFragment.modoGraficar){
            case 0:
                datosDia = MenuActivity.reference.child("tarjeta1");
                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

        }
    }

}
