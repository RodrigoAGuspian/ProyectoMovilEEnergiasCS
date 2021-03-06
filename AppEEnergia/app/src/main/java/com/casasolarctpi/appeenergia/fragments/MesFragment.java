package com.casasolarctpi.appeenergia.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.MenuActivity;
import com.casasolarctpi.appeenergia.models.Constants;
import com.casasolarctpi.appeenergia.models.CustomMarkerViewDataMonth;
import com.casasolarctpi.appeenergia.models.DataAverage;
import com.casasolarctpi.appeenergia.models.DatosCompletos;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesFragment extends Fragment implements OnClickListener {
    TextView txtTituloChar;
    ProgressBar pBMes;
    MaterialSpinner mSMes;
    NumberPicker nPAnio;
    BarChart barChart2;
    Button btnConsultaMes, btnCambio2;
    View view;
    int yearM, month,numDias;
    List<DatosCompletos>[] datosCompletosMes = new List[32];
    List<BarEntry>[] entriesBar = new List[6];
    BarDataSet [] barDataSets = new BarDataSet[6];
    List<IBarDataSet> dataBarSets = new ArrayList<>();
    List<String> labelC = new ArrayList<>();
    private DatabaseReference datosMes;
    float yAxisMax1, yAxisMin1, yAxisMax2, yAxisMin2;
    private boolean bandera = true;
    CustomMarkerViewDataMonth markerMonth;
    public MesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mes, container, false);
        inizialite();
        inizialiteValues();
        inizialiteListEntries();
        inputDataToSpinner();
        return view;
    }

    private void inizialite() {
        txtTituloChar = view.findViewById(R.id.txtTituloChar);
        pBMes = view.findViewById(R.id.pbMes);
        nPAnio = view.findViewById(R.id.nPAnio);
        barChart2 = view.findViewById(R.id.barChart2);
        btnConsultaMes = view.findViewById(R.id.btnConsulta3);
        btnCambio2 = view.findViewById(R.id.btnCambio2);
        mSMes = view.findViewById(R.id.spinnerMes);
        btnConsultaMes.setOnClickListener(this);
        btnCambio2.setOnClickListener(this);

        btnCambio2.setVisibility(INVISIBLE);
        barChart2.setVisibility(INVISIBLE);
        pBMes.setVisibility(INVISIBLE);
        txtTituloChar.setVisibility(INVISIBLE);

    }


    private void inizialiteValues() {
        switch (ConsultasFragment.modoGraficar){
            case 0:
                datosMes = MenuActivity.reference.child("tarjeta1");
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

    private void inizialiteListEntries(){
        for (int i=0; i<entriesBar.length;i++){
            entriesBar[i] = new ArrayList<>();
        }
    }

    //Métodd para ingresar valores al spinner de la vista de mes.
    public void inputDataToSpinner() {
        mSMes.setItems(Constants.MESES);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        nPAnio.setMinValue(Constants.MIN_YEAR);
        nPAnio.setMaxValue(Constants.MAX_YEAR);
        nPAnio.setValue(year);


    }

    //Método para obtener el número de días de mes seleccionado en el MaterialSpinner
    private void getDataMonth() {
        pBMes.setVisibility(VISIBLE);
        barChart2.setVisibility(INVISIBLE);
        yearM = nPAnio.getValue();
        month = mSMes.getSelectedIndex();
        switch(month){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                numDias=31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                numDias=30;
                break;
            case 1:
                if ( ((yearM%100 == 0) && (yearM%400 == 0)) ||
                        ((yearM%100 != 0) && (yearM%  4 == 0))   )
                    numDias=29;
                else
                    numDias=28;
            default:
        }
        datosCompletosMes = new List[numDias];


        for (int i=0; i<numDias+1;i++){
            //getDataDayOFFireBaseDay(yearM,realMonth,i);
            labelC.add(i,Integer.toString(i + 1));

        }
        getDataToFirebaseForMonth(yearM,month+1);

    }

    //Método para la obtención de datos del mes
    private void getDataToFirebaseForMonth(int year, int month){
        DatabaseReference dbrMonth = datosMes.child("datos").child("y"+year).child("m"+month);
        dbrMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int tmpIndex;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    try {
                        tmpIndex = Integer.parseInt(Objects.requireNonNull(postSnapshot.getKey()).substring(1));
                        Log.e("asd",""+tmpIndex);
                        GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {};
                        datosCompletosMes[tmpIndex-1] = postSnapshot.getValue(t);
                    }catch (Exception e){
                        Log.e("Error consulta mes", e.getMessage());
                    }

                }
                try {
                    showChartMonth();
                    Log.e("Pasa dato","!");

                }catch (Exception e){
                    Toast.makeText(getContext(), R.string.no_hay_datos_disponibles, Toast.LENGTH_SHORT).show();
                    btnConsultaMes.setEnabled(true);
                    pBMes.setVisibility(View.INVISIBLE);
                    btnCambio2.setVisibility(VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método para graficar los datos del mes.
    public void showChartMonth() {
        inizialiteListEntries();
        barChart2.clearAnimation();
        barChart2.clear();
        XAxis xAxis1;
        int j;
        float tmpValue=0;
        for (int i = 0; i < datosCompletosMes.length; i++) {
            for (j = 0; j < entriesBar.length; j++) {
                DataAverage tmpDataAverage = promedioDia(datosCompletosMes[i]);
                switch (j) {
                    case 0:
                        tmpValue = tmpDataAverage.getCorrientePromedio1();
                        break;
                    case 1:
                        tmpValue = tmpDataAverage.getCorrientePromedio2();
                        break;
                    case 2:
                        tmpValue = tmpDataAverage.getCorrientePromedio3();
                        break;
                    case 3:
                        tmpValue = tmpDataAverage.getPotenciaPromedio1();
                        break;
                    case 4:
                        tmpValue = tmpDataAverage.getPotenciaPromedio2();
                        break;
                    case 5:
                        tmpValue = tmpDataAverage.getPotenciaPromedio3();
                        break;
                }
                entriesBar[j].add(new BarEntry(i, tmpValue));
                if (j < 3) {
                    if (tmpValue > yAxisMax1) {
                        yAxisMax1 = tmpValue;
                    }

                    if (yAxisMin1 == 0) {
                        yAxisMin1 = tmpValue;
                    }
                    if (tmpValue < yAxisMin1) {
                        yAxisMin1 = tmpValue;
                    }
                } else {

                    if (tmpValue > yAxisMax2) {
                        yAxisMax2 = tmpValue;
                    }

                    if (yAxisMin2 == 0) {
                        yAxisMin2 = tmpValue;
                    }
                    if (tmpValue < yAxisMin2) {
                        yAxisMin2 = tmpValue;
                    }

                }

            }

            if (j<=entriesBar.length){

                YAxis yAxisLeft = barChart2.getAxisLeft();
                YAxis yAxisRight = barChart2.getAxisRight();
                yAxisLeft.setAxisMaximum(yAxisMax1);
                yAxisLeft.setAxisMinimum(0);
                yAxisRight.setAxisMaximum(yAxisMax2);
                yAxisRight.setAxisMinimum(0);

                barChart2.notifyDataSetChanged();
                barChart2.invalidate();
            }

        }

        loadDataBarSets();


        if (entriesBar[0].size() != 0) {

            txtTituloChar.setVisibility(VISIBLE);


            final Description description = new Description();
            description.setText(" ");
            xAxis1 = barChart2.getXAxis();
            xAxis1.setCenterAxisLabels(true);
            xAxis1.setLabelRotationAngle(-10f);
            xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis1.setValueFormatter(new IndexAxisValueFormatter(labelC));
            xAxis1.setAxisMaximum(datosCompletosMes.length);
            xAxis1.setLabelCount(2, true);


            barChart2.setDescription(description);
            markerMonth =new CustomMarkerViewDataMonth(getContext(), R.layout.item_custom_marker, labelC, Constants.tipoDeDato, Constants.coloresGrafica);
            markerMonth.setCambioDeDatos(bandera);
            barChart2.setMarker(markerMonth);
            barChart2.highlightValue(null);
            barChart2.setVisibility(VISIBLE);


        } else {
            Toast.makeText(getContext(), R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
            txtTituloChar.setVisibility(INVISIBLE);
        }
        btnConsultaMes.setEnabled(true);
        pBMes.setVisibility(View.INVISIBLE);

        btnCambio2.setVisibility(VISIBLE);

    }



    private void loadDataBarSets() {
        dataBarSets = new ArrayList<>();
        txtTituloChar.setVisibility(VISIBLE);
        final DecimalFormat decimalFormat = new DecimalFormat("####.##");
        for (int i = 0; i < barDataSets.length; i++) {
            barDataSets[i] = new BarDataSet(entriesBar[i], Constants.tipoDeDato1[i]);
            barDataSets[i].setColor(getResources().getColor(Constants.coloresGrafica[i]));
            barDataSets[i].setValueTextColor(getResources().getColor(Constants.coloresGrafica[i]));
            Log.e("s", "color: " + getResources().getColor(Constants.coloresGrafica[i]));
            if (i < 3) {
                barDataSets[i].setAxisDependency(YAxis.AxisDependency.LEFT);
            } else {
                barDataSets[i].setAxisDependency(YAxis.AxisDependency.RIGHT);

            }

            barDataSets[i].setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return decimalFormat.format(value);
                }
            });

            if (bandera){
                if (i<3) {
                    dataBarSets.add(barDataSets[i]);
                }

            }else {
                if (i>=3) {
                    Log.e("asd",""+i);
                    dataBarSets.add(barDataSets[i]);
                }
            }

            if (i==barDataSets.length-1){
                barChart2.notifyDataSetChanged();
                barChart2.invalidate();

            }

        }

        if (bandera){
            txtTituloChar.setText(getString(R.string.corriente_vs_tiempo));
            YAxis yAxisLeft = barChart2.getAxisLeft();
            YAxis yAxisRight = barChart2.getAxisRight();
            float tmpYAxisMax= (float) (yAxisMax1*1.014);
            yAxisLeft.setAxisMaximum(tmpYAxisMax);
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMaximum(tmpYAxisMax);
            yAxisRight.setAxisMinimum(0);

        }else {
            txtTituloChar.setText(getString(R.string.potencia_vs_tiempo));
            YAxis yAxisLeft = barChart2.getAxisLeft();
            YAxis yAxisRight = barChart2.getAxisRight();
            float tmpYAxisMax= (float) (yAxisMax2*1.014);
            yAxisLeft.setAxisMaximum(tmpYAxisMax);
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMaximum(tmpYAxisMax);
            yAxisRight.setAxisMinimum(0);
        }

        barChart2.highlightValue(null);
        BarData data = new BarData(dataBarSets);
        barChart2.clear();
        barChart2.setData(data);
        data.setBarWidth(0.3264f); // set custom bar width
        barChart2.groupBars(0, 0.02f, 0f);

    }

    private void changeDataBar(){
        bandera = !bandera;
        markerMonth.setCambioDeDatos(bandera);
        loadDataBarSets();
        barChart2.notifyDataSetChanged();
        barChart2.invalidate();
    }

    //Método para promediar los datos del diá.
    private DataAverage promedioDia(List<DatosCompletos> datosFiltrado) {
        DataAverage acumulador= new DataAverage();
        int  acmH = 0;
        int contador = 0;
        List<DataAverage> irradianciaPorHoras = new ArrayList<>(1);
        try {
            for (int i =0 ; i<datosFiltrado.size(); i++){
                DatosCompletos el1 = datosFiltrado.get(i);
                try {
                    acumulador.setPotenciaPromedio1(acumulador.getPotenciaPromedio1() +Float.parseFloat(el1.getPotencia1()));
                    acumulador.setPotenciaPromedio2(acumulador.getPotenciaPromedio2() +Float.parseFloat(el1.getPotencia2()));
                    acumulador.setPotenciaPromedio3(acumulador.getPotenciaPromedio3() +Float.parseFloat(el1.getPotencia3()));
                    acumulador.setCorrientePromedio1(acumulador.getCorrientePromedio1() +Float.parseFloat(el1.getCorriente1()));
                    acumulador.setCorrientePromedio2(acumulador.getCorrientePromedio2() +Float.parseFloat(el1.getCorriente2()));
                    acumulador.setCorrientePromedio3(acumulador.getCorrientePromedio3() +Float.parseFloat(el1.getCorriente3()));
                    contador++;
                }catch (Exception ignore1) {

                }
                try {
                    Date horaDato;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    horaDato = timeFormat.parse(el1.getHora());
                    if (acmH == 0){
                        acmH = horaDato.getHours();
                    }
                    if (horaDato.getHours() == acmH){
                        acumulador.setPotenciaPromedio1(acumulador.getPotenciaPromedio1() / contador);
                        acumulador.setPotenciaPromedio2(acumulador.getPotenciaPromedio2() / contador);
                        acumulador.setPotenciaPromedio3(acumulador.getPotenciaPromedio3() / contador);
                        acumulador.setCorrientePromedio1(acumulador.getCorrientePromedio1() / contador);
                        acumulador.setCorrientePromedio2(acumulador.getCorrientePromedio2() / contador);
                        acumulador.setCorrientePromedio3(acumulador.getCorrientePromedio3() / contador);
                        irradianciaPorHoras.add(acumulador);
                        acumulador = new DataAverage();
                        contador=0;
                        acmH++;
                    } else {
                        if (horaDato.getHours() - 1 > acmH || acmH ==0){
                            acmH = horaDato.getHours() + 1;
                        }
                    }

                } catch (Exception e) {
                    Log.e("error",e.getMessage()+ " "+ el1.getHora());

                }

            }

            for ( DataAverage element : irradianciaPorHoras ) {

                acumulador.setPotenciaPromedio1(element.getPotenciaPromedio1() + acumulador.getPotenciaPromedio1());
                acumulador.setPotenciaPromedio2(element.getPotenciaPromedio2() + acumulador.getPotenciaPromedio2());
                acumulador.setPotenciaPromedio3(element.getPotenciaPromedio3() + acumulador.getPotenciaPromedio3());
                acumulador.setCorrientePromedio1(element.getCorrientePromedio1() + acumulador.getCorrientePromedio1());
                acumulador.setCorrientePromedio2(element.getCorrientePromedio2() + acumulador.getCorrientePromedio2());
                acumulador.setCorrientePromedio3(element.getCorrientePromedio3() + acumulador.getCorrientePromedio3());

            }


        }catch (Exception ignore){

        }
        return acumulador;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConsulta3:
                getDataMonth();
                break;
            case R.id.btnCambio2:
                changeDataBar();
                break;
        }
    }
}
