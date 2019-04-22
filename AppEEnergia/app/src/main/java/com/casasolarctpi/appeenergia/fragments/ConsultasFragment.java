package com.casasolarctpi.appeenergia.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.models.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasFragment extends Fragment {

    public static int modoGraficar=0;
    View view;

    public ConsultasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consultas, container, false);

        ViewPager mViewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //dapter.addFrag(new SimpleFragment,"Title");

        adapter.addFrag(new DiaFragment(), getString(R.string.dia));
        adapter.addFrag(new SemanaFragment(), getString(R.string.semana));
        adapter.addFrag(new MesFragment(), getString(R.string.mes));

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);


        return view;
    }

}
