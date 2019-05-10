package com.casasolarctpi.appeenergia.fragments;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.MenuActivity;
import com.casasolarctpi.appeenergia.models.Constants;
import com.casasolarctpi.appeenergia.models.CustomMarkerViewDataA;
import com.casasolarctpi.appeenergia.models.DatosCompletos;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaFragment extends Fragment implements OnClickListener , OnDateSetListener {
    //Declaración de variables
    private View view;
    private LineChart lineChartDia;
    private Button btnConsulta1;
    private TextView txtConsulta1,txtTituloChart;
    public static List<String> labelsChart = new ArrayList<>();
    private DatabaseReference datosDia;
    float valorMaximo1, valorMinimo1, valorMaximo2, valorMinimo2;
    public static List<Entry> [] entries = new List[6];
    LineDataSet [] lineDataSets = new LineDataSet[6];
    private List<ILineDataSet> dataSets = new ArrayList<>();
    XAxis xAxis;
    ProgressBar pbConsulta1;
    private String fechaATexto;
    private Date dateToQuery;


    public DiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dia, container, false);
        inizialite();
        inizialiteValues();
        inizialiteListEntries();
        return view;
    }

    private void inizialite() {
        lineChartDia = view.findViewById(R.id.lineChartDia);
        btnConsulta1 = view.findViewById(R.id.btnConsulta1);
        txtConsulta1 = view.findViewById(R.id.txtConsulta1);
        txtTituloChart = view.findViewById(R.id.txtTituloChart);
        pbConsulta1 = view.findViewById(R.id.pbConsulta1);

        btnConsulta1.setOnClickListener(this);

        btnConsulta1.setEnabled(true);
        lineChartDia.setVisibility(INVISIBLE);
        pbConsulta1.setVisibility(INVISIBLE);

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


    private void inizialiteListEntries(){
        for (int i=0; i<entries.length;i++){
            entries[i] = new ArrayList<>();
        }
    }

    private void cleanListEntries(){
        for (int i=0; i<entries.length;i++){
            entries[i] = new ArrayList<>();
        }
    }

    //Método para mostrar el DatePicker del día.
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    //Método para obtener los valores del día seleccionado.
    private void getDataDayOFFireBase(int year, int month, int dayOfMonth) {

        DatabaseReference dbR = datosDia.child("datos").child("y" + year).child("m" + month).child("d" + dayOfMonth);
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {
                };
                try {
                    showChartDay(dataSnapshot.getValue(t));

                } catch (Exception e) {
                    Log.e("Error consulta",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void showChartDay(List<DatosCompletos> datosCompletos) {
        float[] todosLosDatos = new float[6];
        lineChartDia.clearAnimation();
        lineChartDia.clear();
        dataSets.clear();
        cleanListEntries();
        if (datosCompletos!=null) {
            for (int i = 0; i < datosCompletos.size(); i++) {
                labelsChart.add(datosCompletos.get(i).getHora());
                try {

                    todosLosDatos[0] = Float.parseFloat(datosCompletos.get(i).getCorriente1());
                    todosLosDatos[1] = Float.parseFloat(datosCompletos.get(i).getCorriente2());
                    todosLosDatos[2] = Float.parseFloat(datosCompletos.get(i).getCorriente3());
                    todosLosDatos[3] = Float.parseFloat(datosCompletos.get(i).getPotencia1());
                    todosLosDatos[4] = Float.parseFloat(datosCompletos.get(i).getPotencia2());
                    todosLosDatos[5] = Float.parseFloat(datosCompletos.get(i).getPotencia3());

                    for (int j = 0; j < todosLosDatos.length; j++) {
                        if (j < 3) {
                            if (todosLosDatos[j] > valorMaximo1) {
                                valorMaximo1 = todosLosDatos[j];
                            }

                            if (valorMinimo1 == 0) {
                                valorMinimo1 = todosLosDatos[j];
                            }
                            if (todosLosDatos[j] < valorMinimo1) {
                                valorMinimo1 = todosLosDatos[j];
                            }
                        } else {

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

                } catch (Exception e) {
                    Log.e("Error en las entries", e.getMessage());
                }

                for (int j = 0; j < todosLosDatos.length; j++) {
                    entries[j].add(new Entry(i, todosLosDatos[j]));
                }


                todosLosDatos = new float[6];
            }
        }else {
            pbConsulta1.setVisibility(INVISIBLE);
            lineChartDia.setVisibility(View.INVISIBLE);
            Snackbar.make(view,getString(R.string.no_hay_datos_dia_se),Snackbar.LENGTH_SHORT).show();
        }


        if (entries[0].size() > 0) {


            for (int i = 0; i < lineDataSets.length; i++) {
                lineDataSets[i] = new LineDataSet(entries[i], Constants.tipoDeDato1[i]);
                lineDataSets[i].setColor(getResources().getColor(Constants.coloresGrafica[i]));
                lineDataSets[i].setValueTextColor(getResources().getColor(Constants.coloresGrafica[i]));
                Log.e("s", "color: " + getResources().getColor(Constants.coloresGrafica[i]));
                lineDataSets[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSets[i].setDrawCircles(false);
                if (i < 3) {
                    lineDataSets[i].setAxisDependency(YAxis.AxisDependency.LEFT);
                } else {
                    lineDataSets[i].setAxisDependency(YAxis.AxisDependency.RIGHT);

                }
                dataSets.add(lineDataSets[i]);

            }

            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            Description description = new Description();
            lineChartDia.setData(data);
            description.setText(" ");
            xAxis = lineChartDia.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yAxisLeft = lineChartDia.getAxisLeft();
            YAxis yAxisRight = lineChartDia.getAxisRight();

            if (valorMinimo1 > 10) {
                valorMinimo1 -= 1f;
            }

            if (valorMinimo2 > 10) {
                valorMinimo2 -= 1f;
            }

            valorMaximo1 += 0.1;
            valorMaximo2 += 10;


            yAxisLeft.setAxisMaximum(valorMaximo1 + 0.2f);
            yAxisRight.setAxisMaximum(valorMaximo2 + 0.2f);
            yAxisLeft.setAxisMinimum(valorMinimo1);
            yAxisRight.setAxisMinimum(valorMinimo2);
            valorMaximo1 = 0;
            valorMinimo2 = 0;


            lineChartDia.setDescription(description);
            lineChartDia.setDrawMarkers(true);
            CustomMarkerViewDataA customMarkerView = new CustomMarkerViewDataA(getContext(), R.layout.item_custom_marker, labelsChart, Constants.tipoDeDato, Constants.coloresGrafica);
            customMarkerView.setSizeList(labelsChart.size());
            lineChartDia.setMarker(customMarkerView);
            lineChartDia.setTouchEnabled(true);
            lineChartDia.setVisibility(View.VISIBLE);
            lineChartDia.invalidate();

        }else {

        }
        pbConsulta1.setVisibility(INVISIBLE);
        btnConsulta1.setEnabled(true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConsulta1:
                showDatePickerDialog();
                break;

        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            btnConsulta1.setEnabled(false);
            txtConsulta1.setVisibility(INVISIBLE);
            pbConsulta1.setVisibility(VISIBLE);
            int realMonth = month + 1;
            fechaATexto = dayOfMonth + "-" + realMonth + "-" + year;
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            dateToQuery = calendar.getTime();
            txtConsulta1.setText(getString(R.string.fecha)+": "+fechaATexto);
            //dateDay = new GregorianCalendar(year,month,dayOfMonth).getTime();
            getDataDayOFFireBase(year, realMonth, dayOfMonth);


    }
}
