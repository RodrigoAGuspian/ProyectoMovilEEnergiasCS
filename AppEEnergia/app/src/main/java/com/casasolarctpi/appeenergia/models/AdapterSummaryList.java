package com.casasolarctpi.appeenergia.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.casasolarctpi.appeenergia.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSummaryList extends RecyclerView.Adapter<AdapterSummaryList.Holder> {
    private List<SummaryData> summaryDataList = new ArrayList<>();

    public AdapterSummaryList(List<SummaryData> summaryDataList) {
        this.summaryDataList = summaryDataList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resumen, parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.connectData(summaryDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return summaryDataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtTitle = itemView.findViewById(R.id.txtTitle);
        TextView txtData = itemView.findViewById(R.id.txtData);
        public Holder(View itemView) {
            super(itemView);
        }

        public void connectData(SummaryData summaryData) {
            txtTitle.setText(summaryData.getTitle());
            txtData.setText(summaryData.getData());
        }
    }
}
