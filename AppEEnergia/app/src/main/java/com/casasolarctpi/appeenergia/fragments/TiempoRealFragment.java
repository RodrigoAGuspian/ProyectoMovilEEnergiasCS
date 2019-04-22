package com.casasolarctpi.appeenergia.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casasolarctpi.appeenergia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiempoRealFragment extends Fragment {


    public TiempoRealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiempo_real, container, false);
    }

}
