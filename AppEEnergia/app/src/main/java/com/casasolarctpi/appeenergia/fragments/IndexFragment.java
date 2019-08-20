package com.casasolarctpi.appeenergia.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.MenuActivity;
import com.casasolarctpi.appeenergia.models.DataSummary;
import com.casasolarctpi.appeenergia.models.DatosCompletos;
import com.casasolarctpi.appeenergia.models.DatosPromedio;
import com.casasolarctpi.appeenergia.models.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    //Declaración de variables
    private View view;
    private Button btnLeerMas;
    public static DataSummary dataSummaryC1 = new DataSummary();
    public static DataSummary dataSummaryC2 = new DataSummary();
    public static DataSummary dataSummaryC3 = new DataSummary();
    private ResumenC1Fragment resumenC1Fragment = new ResumenC1Fragment();
    private ResumenC2Fragment resumenC2Fragment = new ResumenC2Fragment();
    private ResumenC3Fragment resumenC3Fragment = new ResumenC3Fragment();
    public static String fecha;
    private int page = 0;
    private boolean isChangeData = true;
    private volatile boolean isViewLoad = true;
    ViewPager mViewPager;
    TabLayout tabLayout;
    int conteo = 0;
    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_index, container, false);
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        inizialite();
        onListener();
        queryResume();
        isChangeData = true;
        isViewLoad = true;
        switchSummary();
        return view;
    }


    //inicializacion de vistas
    @SuppressLint("ClickableViewAccessibility")
    private void inizialite() {
        btnLeerMas = view.findViewById(R.id.btnLeerMas);
        mViewPager = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tabLayout2);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());


        adapter.addFrag(resumenC1Fragment, getString(R.string.circuito_1));
        adapter.addFrag(resumenC2Fragment, getString(R.string.circuito_2));
        adapter.addFrag(resumenC3Fragment, getString(R.string.circuito_3));
        mViewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page = tab.getPosition();
                conteo = 0;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                page = tab.getPosition();
                conteo = 0;
            }
        });

        mViewPager.beginFakeDrag();

    }

    private void queryResume(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int realMonth = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH)-1;
        fecha = day+"/"+realMonth+"/"+year;
        getDataDayOFFireBase(year,realMonth,day);

    }

    //Método para obtener los valores del día seleccionado.
    private void getDataDayOFFireBase(int year, int month, int dayOfMonth) {
        DatabaseReference datosDia = MenuActivity.reference.child("tarjeta1").child("datos").child("y" + year).child("m" + month).child("d" + dayOfMonth);
        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {
                };
                try {
                    promedioDia(dataSnapshot.getValue(t));


                } catch (Exception e) {

                    Log.e("error datos", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("asd", "error");
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onListener(){
        btnLeerMas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.item_mas_info);
                final TextView txtInfo = dialog.findViewById(R.id.txtInfoPrincipal);
                ScrollView scrollMasInfo = dialog.findViewById(R.id.scrollMasInfo);
                txtInfo.setMovementMethod(new ScrollingMovementMethod());

                scrollMasInfo.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        txtInfo.getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });

                txtInfo.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        txtInfo.getParent().requestDisallowInterceptTouchEvent(true);

                        return false;
                    }
                });
                Button btnOK = dialog.findViewById(R.id.btnOk);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.setCancelable(true);
                dialog.show();

            }
        });

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int envId = event.getAction();
                switch (envId) {

                    case MotionEvent.ACTION_DOWN:
                        isChangeData = false;
                        break;

                    case MotionEvent.ACTION_UP:
                        isChangeData = true;
                        break;

                }
                return false;
            }
        });
    }

    //Método para promediar los datos del diá.
    private void promedioDia(List<DatosCompletos> datosFiltrado) {
        DatosPromedio acumulador= new DatosPromedio();
        DatosPromedio datosPromedioFinales= new DatosPromedio();
        int  acmH = 0;
        int contador = 0;
        List<DatosPromedio> datosPorHoras = new ArrayList<>(1);
        try {
            for (int i =0 ; i<datosFiltrado.size(); i++){
                DatosCompletos el1 = datosFiltrado.get(i);
                try {
                    acumulador.setCorriente1(acumulador.getCorriente1() + Float.parseFloat(el1.getCorriente1()));
                    acumulador.setCorriente2(acumulador.getCorriente2() + Float.parseFloat(el1.getCorriente2()));
                    acumulador.setCorriente3(acumulador.getCorriente3() + Float.parseFloat(el1.getCorriente3()));
                    acumulador.setPotencia1(acumulador.getPotencia1() + Float.parseFloat(el1.getPotencia1()));
                    acumulador.setPotencia2(acumulador.getPotencia2() + Float.parseFloat(el1.getPotencia2()));
                    acumulador.setPotencia3(acumulador.getPotencia3() + Float.parseFloat(el1.getPotencia3()));
                    contador++;
                }catch (Exception ignore1) {
                    Log.e("tafg",ignore1.getMessage());
                }
                try {
                    Date horaDato;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    horaDato = timeFormat.parse(el1.getHora());
                    if (acmH == 0){
                        acmH = horaDato.getHours();
                    }
                    if (horaDato.getHours() == acmH){
                        int tmpAcmH = acmH + 1;
                        if (tmpAcmH==24){
                            tmpAcmH = 0;
                        }
                        acumulador.setCorriente1(acumulador.getCorriente1() / contador);
                        acumulador.setCorriente2(acumulador.getCorriente2() / contador);
                        acumulador.setCorriente3(acumulador.getCorriente3() / contador);
                        acumulador.setPotencia1(acumulador.getPotencia1() / contador);
                        acumulador.setPotencia2(acumulador.getPotencia2() / contador);
                        acumulador.setPotencia3(acumulador.getPotencia3() / contador);
                        acumulador.setHora(acmH + " a " + tmpAcmH);
                        datosPorHoras.add(acumulador);
                        acumulador = new DatosPromedio();
                        acmH++;
                        contador = 0;

                    } else {
                        if (horaDato.getHours() - 1 > acmH || acmH ==0){
                            acmH = horaDato.getHours() + 1;
                        }
                    }

                } catch (Exception ignored) {

                }

            }

            for ( DatosPromedio element : datosPorHoras ) {
                datosPromedioFinales.setCorriente1(element.getCorriente1() + datosPromedioFinales.getCorriente1());
                datosPromedioFinales.setCorriente2(element.getCorriente2() + datosPromedioFinales.getCorriente2());
                datosPromedioFinales.setCorriente3(element.getCorriente3() + datosPromedioFinales.getCorriente3());
                datosPromedioFinales.setPotencia1(element.getPotencia1() + datosPromedioFinales.getPotencia1());
                datosPromedioFinales.setPotencia2(element.getPotencia2() + datosPromedioFinales.getPotencia2());
                datosPromedioFinales.setPotencia3(element.getPotencia3() + datosPromedioFinales.getPotencia3());

            }




        }catch (Exception ignore){

        }

        averageData(datosPorHoras);

    }

    private void averageData(List<DatosPromedio> datosPorHoras){
        List<List<DatosPromedio>>  tmpListSortDatosPorHoras = new ArrayList<>();
        for (int i=0; i<3 ; i++){
            tmpListSortDatosPorHoras.add(datosPorHoras);
            final int finalI = i;
            Collections.sort(tmpListSortDatosPorHoras.get(i), new Comparator<DatosPromedio>() {
                @Override
                public int compare(DatosPromedio o1, DatosPromedio o2) {
                    try {
                        switch (finalI){
                            case 0:
                                return (int) (o1.getPotencia1() - o2.getPotencia1());

                            case 1:
                                return (int) (o1.getPotencia2() - o2.getPotencia2());

                            case 2:
                                return (int) (o1.getPotencia3() - o2.getPotencia3());

                            default:
                                return 0;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            List<DatosPromedio> tmpDatosPromedio = tmpListSortDatosPorHoras.get(i);
            int posFinDato = tmpDatosPromedio.size() - 1;
            int tmpEnergiaDia = 0;
            Log.e("datos1",""+posFinDato);
            switch (finalI){
                case 0:
                    dataSummaryC1.setPotenciaMax(tmpDatosPromedio.get(0).getPotencia1());
                    dataSummaryC1.setPotenciaMin(tmpDatosPromedio.get(posFinDato).getPotencia1());
                    dataSummaryC1.setHoraMax(tmpDatosPromedio.get(0).getHora());
                    dataSummaryC1.setHoraMin(tmpDatosPromedio.get(posFinDato).getHora());
                    for ( DatosPromedio element : tmpDatosPromedio ) {
                        tmpEnergiaDia += element.getPotencia1();
                    }
                    dataSummaryC1.setEnergiaDia(tmpEnergiaDia);
                    break;

                case 1:
                    dataSummaryC2.setPotenciaMax(tmpDatosPromedio.get(0).getPotencia2());
                    dataSummaryC2.setPotenciaMin(tmpDatosPromedio.get(posFinDato).getPotencia2());
                    dataSummaryC2.setHoraMax(tmpDatosPromedio.get(0).getHora());
                    dataSummaryC2.setHoraMin(tmpDatosPromedio.get(posFinDato).getHora());
                    for ( DatosPromedio element : tmpDatosPromedio ) {
                        tmpEnergiaDia += element.getPotencia2();
                    }
                    dataSummaryC2.setEnergiaDia(tmpEnergiaDia);
                    break;

                case 2:
                    dataSummaryC3.setPotenciaMax(tmpDatosPromedio.get(0).getPotencia3());
                    dataSummaryC3.setPotenciaMin(tmpDatosPromedio.get(posFinDato).getPotencia3());
                    dataSummaryC3.setHoraMax(tmpDatosPromedio.get(0).getHora());
                    dataSummaryC3.setHoraMin(tmpDatosPromedio.get(posFinDato).getHora());
                    for ( DatosPromedio element : tmpDatosPromedio ) {
                        tmpEnergiaDia += element.getPotencia3();
                    }
                    dataSummaryC3.setEnergiaDia(tmpEnergiaDia);
                    break;
            }
        }

        resumenC1Fragment.inputDataToRecyclerView();
        resumenC2Fragment.inputDataToRecyclerView();
        resumenC3Fragment.inputDataToRecyclerView();



    }


    private void switchSummary(){
        Thread switcherData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isViewLoad){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                conteo++;
                                if(conteo==5){
                                    if (isChangeData){
                                        if (page<3){
                                            page++;
                                            mViewPager.setCurrentItem(page, true);
                                        }else {
                                            page = 0;
                                            mViewPager.setCurrentItem(page, true);

                                        }
                                    }else {
                                        page = mViewPager.getCurrentItem();

                                    }
                                    conteo = 0;
                                }else {
                                    page = mViewPager.getCurrentItem();
                                }

                            }
                        });
                    }catch (Exception ignored){

                    }

                }
            }
        });

        switcherData.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        isChangeData = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isChangeData = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isChangeData = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isChangeData = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isChangeData = false;
        isViewLoad = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isChangeData = false;
        isViewLoad = false;
    }
}
