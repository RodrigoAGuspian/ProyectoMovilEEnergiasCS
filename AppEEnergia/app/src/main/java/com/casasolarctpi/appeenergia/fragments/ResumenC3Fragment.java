package com.casasolarctpi.appeenergia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.models.AdapterSummaryList;
import com.casasolarctpi.appeenergia.models.DataSummary;
import com.casasolarctpi.appeenergia.models.SummaryData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResumenC3Fragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DataSummary dataSummary = new DataSummary();

    public ResumenC3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resumen_c3, container, false);
        inizialite();
        inputDataToRecyclerView();
        return view;
    }

    private void inizialite() {
        recyclerView = view.findViewById(R.id.recyclerViewC3);
        dataSummary = IndexFragment.dataSummaryC3;
    }

    public void inputDataToRecyclerView(){
        List<SummaryData> summaryData = new ArrayList<>();

        summaryData.add(new SummaryData(getString(R.string.dia),IndexFragment.fecha));
        summaryData.add(new SummaryData(getString(R.string.energia_dia),Float.toString(dataSummary.getEnergiaDia())));
        summaryData.add(new SummaryData(getString(R.string.potencia_maxima),Float.toString(dataSummary.getPotenciaMax())));
        summaryData.add(new SummaryData(getString(R.string.hora_potencia_maxima),dataSummary.getHoraMax()));
        summaryData.add(new SummaryData(getString(R.string.potencia_minima),Float.toString(dataSummary.getPotenciaMin()/1000)));
        summaryData.add(new SummaryData(getString(R.string.hora_potencia_minima),dataSummary.getHoraMin()));

        AdapterSummaryList adapterSummaryList = new AdapterSummaryList(summaryData);
        recyclerView.setAdapter(adapterSummaryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

    }

}
