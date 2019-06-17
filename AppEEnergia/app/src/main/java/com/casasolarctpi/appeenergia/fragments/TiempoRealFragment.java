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
import android.widget.TextView;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.LoginActivity;
import com.casasolarctpi.appeenergia.controllers.MenuActivity;
import com.casasolarctpi.appeenergia.models.Constants;
import com.casasolarctpi.appeenergia.models.CustomMarkerViewDataA;
import com.casasolarctpi.appeenergia.models.DatosTiempoReal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiempoRealFragment extends Fragment implements OnClickListener {
    //Declaración de variables
    public static int modoGraficar=0;
    Button btnCorriente, btnPotencia, btnTodo;
    View view;
    TextView txtChart;
    public static LineChart chartTR;
    DatabaseReference datosTiempoReal;
    private List<DatosTiempoReal> datosTiempoRealList;
    float valorMaximo1, valorMinimo1, valorMaximo2, valorMinimo2;
    boolean bandera;
    private List<String> labelsChart = new ArrayList<>();
    public static List<Entry> [] entries = new List[6];
    LineDataSet [] lineDataSets = new LineDataSet[6];
    private List<ILineDataSet> dataSets = new ArrayList<>();
    XAxis xAxis;

    public TiempoRealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tiempo_real, container, false);
        inizialite();
        inizialiteValues();
        inizialiteListEntries();
        inputDataFirebase();
        return view;
    }

    //Método para inicializar las vistas
    private void inizialite() {
        txtChart = view.findViewById(R.id.txtTituloChartRT);
        chartTR = view.findViewById(R.id.tiempoRealChart);
        btnCorriente = view.findViewById(R.id.btnCorriente);
        btnPotencia = view.findViewById(R.id.btnPotencias);
        btnTodo = view.findViewById(R.id.btnTodo);

        btnCorriente.setOnClickListener(this);
        btnPotencia.setOnClickListener(this);
        btnTodo.setOnClickListener(this);
        txtChart.setText(getString(R.string.potencia_corriente_vs_tiempo));
    }

    //Método que define que valores se van a graficar
    private void inizialiteValues(){
        datosTiempoReal = FirebaseDatabase.getInstance().getReference().child("tarjeta1").child("tiempoReal");
        switch (modoGraficar){
            case 0:
                datosTiempoReal = FirebaseDatabase.getInstance().getReference().child("tarjeta1").child("tiempoReal");

                break;

            case 1:
                break;

            case 2:

                break;
        }
        chartTR.setVisibility(View.INVISIBLE);
        bandera=false;
    }


    //Método para el ingreso de datos desde de la base de datos  (Real time DataBase)de Firebase
    private void inputDataFirebase() {

        //Query para limitar los datos
        datosTiempoReal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosTiempoReal>> t = new GenericTypeIndicator<ArrayList<DatosTiempoReal>>(){};
                datosTiempoRealList = dataSnapshot.getValue(t);
                Log.e("datos",""+datosTiempoRealList.size());
                if (!bandera) {
                    inputValuesChart();
                    bandera=true;
                }else {
                    if (entries[0].size()>0) {
                        inputValuesRealTime();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inizialiteListEntries(){
        for (int i=0; i<entries.length;i++){
            entries[i] = new ArrayList<>();
        }
    }

    //Método que gráfica la información de la base de datos
    private void inputValuesChart() {

        final Date[] date1 = {new Date(),new Date()};
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Collections.sort(datosTiempoRealList, new Comparator<DatosTiempoReal>() {
            @Override
            public int compare(DatosTiempoReal o1, DatosTiempoReal o2) {
                try {
                    date1[0] =dateFormat.parse(o1.getFechaActual1());
                    date1[1] =dateFormat.parse(o2.getFechaActual1());
                    if (date1[0].getTime() < date1[1].getTime()) {
                        return -1;
                    }
                    if (date1[0].getTime() > date1[1].getTime()) {
                        return 1;
                    }
                    return 0;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        float [] todosLosDatos = new float[6];
        for (int i=0; i<datosTiempoRealList.size();i++){
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            try {

                todosLosDatos[0] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente1());
                todosLosDatos[1] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente2());
                todosLosDatos[2] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente3());
                todosLosDatos[3] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia1());
                todosLosDatos[4] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia2());
                todosLosDatos[5] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia3());

                for (int j=0; j<todosLosDatos.length;j++){
                    if (j<3) {
                        if (todosLosDatos[j] > valorMaximo1) {
                            valorMaximo1 = todosLosDatos[j];
                        }

                        if (valorMinimo1 == 0) {
                            valorMinimo1 = todosLosDatos[j];
                        }
                        if (todosLosDatos[j] < valorMinimo1) {
                            valorMinimo1 = todosLosDatos[j];
                        }
                    }else {

                        if (todosLosDatos[j] > valorMaximo2) {
                            valorMaximo2 = todosLosDatos[j];
                        }

                        if (valorMinimo2 == 0) {
                            valorMinimo2 = todosLosDatos[j];
                        }
                        if (todosLosDatos[j] < valorMinimo2) {
                            valorMinimo2 = todosLosDatos[j];
                        }

                    }

                }

            }catch (Exception ignore){

            }

            for (int j=0; j<todosLosDatos.length;j++){
                entries[j].add(new Entry(i,todosLosDatos[j]));
            }


            todosLosDatos = new float[6];
        }


        if (entries[0].size()>0) {



            for (int i=0;i<lineDataSets.length;i++){
                lineDataSets[i] = new LineDataSet(entries[i], Constants.tipoDeDato1[i]);
                lineDataSets[i].setColor(getResources().getColor(Constants.coloresGrafica[i]));
                lineDataSets[i].setValueTextColor(getResources().getColor(Constants.coloresGrafica[i]));
                Log.e("s","color: "+getResources().getColor(Constants.coloresGrafica[i]));
                lineDataSets[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSets[i].setDrawCircles(false);
                if (i<3){
                    lineDataSets[i].setAxisDependency(YAxis.AxisDependency.LEFT);
                }else {
                    lineDataSets[i].setAxisDependency(YAxis.AxisDependency.RIGHT);

                }
                dataSets.add(lineDataSets[i]);

            }

            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            Description description = new Description();
            chartTR.setData(data);
            description.setText(" ");
            xAxis = chartTR.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yAxisLeft = chartTR.getAxisLeft();
            YAxis yAxisRight = chartTR.getAxisRight();

            if (valorMinimo1 > 10) {
                valorMinimo1= (float) (valorMinimo1*0.995);
            }

            if (valorMinimo2 > 10) {
                valorMinimo2= (float) (valorMinimo2*0.995);
            }

            yAxisLeft.setAxisMaximum((float) (valorMaximo1*1.005));
            yAxisRight.setAxisMaximum((float) (valorMaximo2*1.005));
            yAxisLeft.setAxisMinimum(valorMinimo1);
            yAxisRight.setAxisMinimum(valorMinimo2);
            valorMaximo1 = 0;
            valorMaximo2 = 0;
            valorMinimo1 = 0;
            valorMinimo2 = 0;

            chartTR.setDescription(description);
            chartTR.setDrawMarkers(true);
            CustomMarkerViewDataA customMarkerView = new CustomMarkerViewDataA(getContext(),R.layout.item_custom_marker,labelsChart,Constants.tipoDeDato, Constants.coloresGrafica);
            customMarkerView.setSizeList(labelsChart.size());
            chartTR.setMarker(customMarkerView);
            chartTR.setTouchEnabled(true);
            chartTR.setVisibility(View.VISIBLE);
            chartTR.invalidate();

        }

    }

    private void inputValuesRealTime() {
        clearEntries();
        labelsChart.clear();

        final Date[] date1 = {new Date(),new Date()};
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Collections.sort(datosTiempoRealList, new Comparator<DatosTiempoReal>() {
            @Override
            public int compare(DatosTiempoReal o1, DatosTiempoReal o2) {
                try {
                    date1[0] =dateFormat.parse(o1.getFechaActual1());
                    date1[1] =dateFormat.parse(o2.getFechaActual1());
                    if (date1[0].getTime() < date1[1].getTime()) {
                        return -1;
                    }
                    if (date1[0].getTime() > date1[1].getTime()) {
                        return 1;
                    }
                    return 0;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        float [] todosLosDatos = new float[6];
        for (int i=0; i<datosTiempoRealList.size();i++){
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            try {

                todosLosDatos[0] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente1());
                todosLosDatos[1] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente2());
                todosLosDatos[2] = Float.parseFloat(datosTiempoRealList.get(i).getCorriente3());
                todosLosDatos[3] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia1());
                todosLosDatos[4] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia2());
                todosLosDatos[5] = Float.parseFloat(datosTiempoRealList.get(i).getPotencia3());

                for (int j=0; j<todosLosDatos.length;j++){
                    if (j<3) {
                        if (todosLosDatos[j] > valorMaximo1) {
                            valorMaximo1 = todosLosDatos[j];
                        }

                        if (valorMinimo1 == 0) {
                            valorMinimo1 = todosLosDatos[j];
                        }
                        if (todosLosDatos[j] < valorMinimo1) {
                            valorMinimo1 = todosLosDatos[j];
                        }
                    }else {

                        if (todosLosDatos[j] > valorMaximo2) {
                            valorMaximo2 = todosLosDatos[j];
                        }

                        if (valorMinimo2 == 0) {
                            valorMinimo2 = todosLosDatos[j];
                        }
                        if (todosLosDatos[j] < valorMinimo2) {
                            valorMinimo2 = todosLosDatos[j];
                        }

                    }

                }

            }catch (Exception ignore){

            }

            for (int j=0; j<todosLosDatos.length;j++){
                entries[j].add(new Entry(i,todosLosDatos[j]));
            }


            todosLosDatos = new float[6];
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));

        YAxis yAxisLeft = chartTR.getAxisLeft();
        YAxis yAxisRight = chartTR.getAxisRight();


        if (valorMinimo1 > 10) {
            valorMinimo1= (float) (valorMinimo1*0.995);
        }

        if (valorMinimo2 > 10) {
            valorMinimo2= (float) (valorMinimo2*0.995);
        }

        yAxisLeft.setAxisMaximum((float) (valorMaximo1*1.005));
        yAxisRight.setAxisMaximum((float) (valorMaximo2*1.005));
        yAxisLeft.setAxisMinimum(valorMinimo1);
        yAxisRight.setAxisMinimum(valorMinimo2);
        valorMaximo1 = 0;
        valorMaximo2 = 0;
        valorMinimo1 = 0;
        valorMinimo2 = 0;


        if (entries[0].size()>0){
            chartTR.notifyDataSetChanged();
            chartTR.invalidate();
            chartTR.setVisibility(View.VISIBLE);
        }




    }

    public static void clearEntries(){
        for (List<Entry> entry : entries) {
            entry.clear();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCorriente:
                soloCorriente();
                break;

            case R.id.btnPotencias:
                soloPotencias();
                break;

            case R.id.btnTodo:
                habilitarTodo();
                break;

        }
    }

    private void soloCorriente() {
        for (int i=0; i<dataSets.size();i++){
            if (i<3){
                dataSets.get(i).setVisible(true);
                dataSets.get(i).setHighlightEnabled(true);
            }else {
                dataSets.get(i).setVisible(false);
                dataSets.get(i).setHighlightEnabled(false);
            }
        }
        txtChart.setText(getString(R.string.corriente_vs_tiempo));
        chartTR.notifyDataSetChanged();
        chartTR.invalidate();
    }

    private void soloPotencias() {
        for (int i=0; i<dataSets.size();i++){
            if (i<3){
                dataSets.get(i).setVisible(false);
                dataSets.get(i).setHighlightEnabled(false);
            }else {
                dataSets.get(i).setVisible(true);
                dataSets.get(i).setHighlightEnabled(true);
            }
        }
        txtChart.setText(getString(R.string.potencias_tiempo));
        chartTR.notifyDataSetChanged();
        chartTR.invalidate();
    }

    private void habilitarTodo() {
        for (int i=0; i<dataSets.size();i++){
            dataSets.get(i).setVisible(true);
            dataSets.get(i).setHighlightEnabled(true);
        }
        txtChart.setText(getString(R.string.potencia_corriente_vs_tiempo));
        chartTR.notifyDataSetChanged();
        chartTR.invalidate();
    }
}
