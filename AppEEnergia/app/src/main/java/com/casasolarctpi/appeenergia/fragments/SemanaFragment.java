package com.casasolarctpi.appeenergia.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SemanaFragment extends Fragment implements OnClickListener {
    
    View view;
    private DatabaseReference datosMes;
    Dialog dialog;
    Button btnConsulta2, btnCambio;
    ProgressBar pbSemana;
    TextView txtTituloGrafica2;
    TextView txtDate2;
    BarChart barChart1;
    private List<DatosCompletos>[] datosCompletosSemana = new List[31];
    List<BarEntry>[] entriesBarWeek = new List[6];
    BarDataSet [] barDataSets = new BarDataSet[6];
    List<IBarDataSet> dataBarSets = new ArrayList<>();
    private XAxis xAxis;
    float yAxisMax1, yAxisMin1, yAxisMax2, yAxisMin2;
    private boolean bandera = true;
    public static String titleData;
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
        inizialiteListEntries();
        return view;
    }

    private void inizialite() {
        btnConsulta2 = view.findViewById(R.id.btnConsulta2);
        btnCambio = view.findViewById(R.id.btnCambio);
        pbSemana = view.findViewById(R.id.pbSemana);
        txtTituloGrafica2 = view.findViewById(R.id.txtTituloGrafica2);
        txtDate2 = view.findViewById(R.id.txtConsulta2);
        barChart1 = view.findViewById(R.id.barChart1);

        btnConsulta2.setOnClickListener(this);
        btnCambio.setOnClickListener(this);
        btnCambio.setVisibility(INVISIBLE);

        pbSemana.setVisibility(INVISIBLE);
        txtTituloGrafica2.setVisibility(INVISIBLE);
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
        for (int i=0; i<entriesBarWeek.length;i++){
            entriesBarWeek[i] = new ArrayList<>();
        }
    }


    //Método para mostar el DatePicker de la consulta por semana.
    public void showDatePickerWeekDialog(){

        Toast.makeText(getContext(), R.string.mensaje_week, Toast.LENGTH_SHORT).show();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_select_week);
        final DatePicker datePicker = dialog.findViewById(R.id.calendarioWeek);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        final Button btnCancelar = dialog.findViewById(R.id.btnCancelar);


        btnAceptar.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                btnConsulta2.setEnabled(false);
                pbSemana.setVisibility(VISIBLE);
                int mes = datePicker.getMonth()+1;
                txtTituloGrafica2.setVisibility(INVISIBLE);
                String fecha1 = datePicker.getDayOfMonth()+"-"+mes+"-"+datePicker.getYear();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Date date = new Date();
                try {
                    date = dateFormat.parse(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();

                }

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Calendar calendar1 = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());

                calendar.setTime(date);


                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);


                calendar1.setTime(date);
                calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar1.set(Calendar.AM_PM, Calendar.PM);
                calendar1.set(Calendar.HOUR, 11);
                calendar1.set(Calendar.MINUTE, 59);
                calendar1.set(Calendar.SECOND, 59);

                Date primerDia = calendar.getTime();
                Date ultimoDia = calendar1.getTime();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                txtDate2.setText(getString(R.string.fecha)+": "+format.format(primerDia)+" "+getResources().getString(R.string.a)+" "+format.format(ultimoDia));

                searchDate(primerDia);
                dialog.cancel();

            }
        });



        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    //Método para saber cual es la semana seleccionada por el dia obtenido por el DatePicker de la semana
    private void searchDate(Date primerDia){
        Calendar tmpCalendar = new GregorianCalendar();
        tmpCalendar.setTime(primerDia);
        Date tmpDate = tmpCalendar.getTime();
        int realMonth;
        int dia;
        int anio;

        for (int i=0; i<7;i++){
            realMonth=tmpDate.getMonth()+1;
            dia = tmpCalendar.get(Calendar.DAY_OF_MONTH);
            anio = tmpCalendar.get(Calendar.YEAR);
            getDataDayOFFireBaseWeek(anio,realMonth,dia,i);
            tmpCalendar.add(Calendar.DAY_OF_MONTH,1);
            tmpDate=tmpCalendar.getTime();

        }

    }

    //Método para obtención de los datos de la semana
    private void getDataDayOFFireBaseWeek(int year, int month, int dayOfMonth,final int contador){

        DatabaseReference datosDia = datosMes.child("datos").child("y"+year).child("m"+month).child("d"+dayOfMonth);
        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {};
                datosCompletosSemana[contador] = dataSnapshot.getValue(t);
                if (contador==6){
                    showChartWeek();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método para graficar los datos de la semana
    private void showChartWeek() {

        barChart1.clearAnimation();
        barChart1.clear();
        clearEntries();
        dataBarSets = new ArrayList<>();
        float tmpValue=0;
        int j;
        for (int i=0; i<7;i++){
            DataAverage tmpDataAverage = promedioDia(datosCompletosSemana[i]);
            for (j=0;j<entriesBarWeek.length;j++){
                switch (j){
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
                entriesBarWeek[j].add(new BarEntry(i,tmpValue));
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

            if (j<=entriesBarWeek.length){
                YAxis yAxisLeft = barChart1.getAxisLeft();
                YAxis yAxisRight = barChart1.getAxisRight();
                yAxisLeft.setAxisMaximum(yAxisMax1);
                yAxisLeft.setAxisMinimum(0);
                yAxisRight.setAxisMaximum(yAxisMax2);
                yAxisRight.setAxisMinimum(0);

                barChart1.notifyDataSetChanged();
                barChart1.invalidate();
            }




        }



        if(entriesBarWeek[0].size()>0){
            txtTituloGrafica2.setVisibility(VISIBLE);
        }

        loadDataBarSets();

        Description description = new Description();
        description.setText(" ");
        barChart1.groupBars(0, 0.02f, 0f);

        xAxis = barChart1.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Constants.DIAS_DE_LA_SEMANA));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(7);

        barChart1.setVisibility(VISIBLE);
        barChart1.setDescription(description);
        pbSemana.setVisibility(INVISIBLE);
        btnConsulta2.setEnabled(true);
        btnCambio.setVisibility(VISIBLE);
        barChart1.invalidate(); // refresh


    }

    private void loadDataBarSets() {
        dataBarSets = new ArrayList<>();
        final DecimalFormat decimalFormat = new DecimalFormat("####.##");
        for (int i = 0; i < barDataSets.length; i++) {
            barDataSets[i] = new BarDataSet(entriesBarWeek[i], Constants.tipoDeDato1[i]);
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
                    dataBarSets.add(barDataSets[i]);
                }
            }

        }

        if (bandera){
            titleData = getString(R.string.potencia_vs_tiempo);
            YAxis yAxisLeft = barChart1.getAxisLeft();
            YAxis yAxisRight = barChart1.getAxisRight();
            float tmpYAxisMax= (float) (yAxisMax1*1.014);

            yAxisLeft.setAxisMaximum(tmpYAxisMax);
            yAxisLeft.setAxisMinimum(yAxisMin1);
            yAxisRight.setAxisMaximum(tmpYAxisMax);
            yAxisRight.setAxisMinimum(yAxisMin1);

        }else {
            titleData = getString(R.string.corriente_vs_tiempo);
            YAxis yAxisLeft = barChart1.getAxisLeft();
            YAxis yAxisRight = barChart1.getAxisRight();
            float tmpYAxisMax= (float) (yAxisMax2*1.014);
            yAxisLeft.setAxisMaximum(tmpYAxisMax);
            yAxisLeft.setAxisMinimum(yAxisMin2);
            yAxisRight.setAxisMaximum(tmpYAxisMax);
            yAxisRight.setAxisMinimum(yAxisMin2);
        }

        BarData data = new BarData(dataBarSets);
        barChart1.clear();
        barChart1.setData(data);
        data.setBarWidth(0.3264f); // set custom bar width
        barChart1.groupBars(0, 0.02f, 0f);

    }

    private void changeDataBar(){
        bandera = !bandera;
        loadDataBarSets();
        barChart1.notifyDataSetChanged();
        barChart1.invalidate();
    }

    private void clearEntries() {
        for (int i=0; i<entriesBarWeek.length;i++){
            entriesBarWeek[i].clear();
        }
    }


    //Método para promediar los datos del diá.
    private DataAverage promedioDia(List<DatosCompletos> datosFiltrado) {
        DataAverage acumulador= new DataAverage();
        int  acmH = 0;

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
                        acumulador.setPotenciaPromedio1(Math.round(acumulador.getPotenciaPromedio1() * 1000)/1000);
                        acumulador.setPotenciaPromedio2(Math.round(acumulador.getPotenciaPromedio2() * 1000)/1000);
                        acumulador.setPotenciaPromedio3(Math.round(acumulador.getPotenciaPromedio3() * 1000)/1000);

                        acumulador.setCorrientePromedio1(Math.round(acumulador.getCorrientePromedio1() * 1000)/1000);
                        acumulador.setCorrientePromedio2(Math.round(acumulador.getCorrientePromedio2() * 1000)/1000);
                        acumulador.setCorrientePromedio3(Math.round(acumulador.getCorrientePromedio3() * 1000)/1000);
                        irradianciaPorHoras.add(acumulador);
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
            case R.id.btnConsulta2:
                showDatePickerWeekDialog();
                break;

            case R.id.btnCambio:
                changeDataBar();
                break;
        }
    }
}
