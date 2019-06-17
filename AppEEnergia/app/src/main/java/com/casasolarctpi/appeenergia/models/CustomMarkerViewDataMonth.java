package com.casasolarctpi.appeenergia.models;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.fragments.MesFragment;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

public class CustomMarkerViewDataMonth extends MarkerView {
    private TextView txtCustomMarker1, txtCustomMarker2;
    private List<String> labelsChart;

    private String[] datos;
    private int [] colores;

    private MPPointF mOffset;
    private float sizeList;
    private float getX1;

    private boolean cambioDeDatos = true;

    public float getSizeList() {
        return sizeList;
    }

    public void setSizeList(float sizeList) {
        this.sizeList = sizeList;
    }

    public boolean getCambioDeDatos() {
        return cambioDeDatos;
    }

    public void setCambioDeDatos(boolean cambioDeDatos) {
        this.cambioDeDatos = cambioDeDatos;
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    public CustomMarkerViewDataMonth(Context context, int layoutResource, List<String> labelsChart, String[] datos,int[] colores) {
        super(context, layoutResource);
        this.labelsChart = labelsChart;
        this.datos = datos;
        this.colores = colores;
        txtCustomMarker1 = findViewById(R.id.txtCustomMarker1);
        txtCustomMarker2 = findViewById(R.id.txtCustomMarker2);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        getX1 = e.getX();
        int [] numDatos = new int[3];
        if (getCambioDeDatos()) {
            MesFragment.titleData = getContext().getResources().getString(R.string.potencia_vs_tiempo);
            numDatos = new int[]{0, 1, 2};
        }else {
            MesFragment.titleData = getContext().getResources().getString(R.string.corriente_vs_tiempo);
            numDatos = new int[]{3, 4, 5};
        }
        switch (highlight.getDataSetIndex() ) {


            case 0:
                txtCustomMarker1.setText(getResources().getString(R.string.dia) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[numDatos[0]] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[numDatos[0]]));
                break;
            case 1:
                txtCustomMarker1.setText(getResources().getString(R.string.dia) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[numDatos[1]] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[numDatos[1]]));
                break;

            case 2:
                txtCustomMarker1.setText(getResources().getString(R.string.dia) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[numDatos[2]] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[numDatos[2]]));
                break;

        }

    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        posX= 80;
        Log.e("x", Float.toString(posX));
        posY=0;
        canvas.translate(posX, posY);
        draw(canvas);
        canvas.translate(-posX, -posY);

        super.draw(canvas, posX, posY);
    }


}
