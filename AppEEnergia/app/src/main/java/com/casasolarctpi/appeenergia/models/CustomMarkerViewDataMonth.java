package com.casasolarctpi.appeenergia.models;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.casasolarctpi.appeenergia.R;
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


    public float getSizeList() {
        return sizeList;
    }

    public void setSizeList(float sizeList) {
        this.sizeList = sizeList;
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
        getX1=e.getX();
        switch (highlight.getDataSetIndex() ) {
            case 0:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[0] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[0]));
                break;
            case 1:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[1] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[1]));
                break;

            case 2:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[2] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[2]));
                break;
            case 3:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[3] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[3]));
                break;

            case 4:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[4] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[4]));
                break;
            case 5:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(datos[5] + ": " + e.getY());
                txtCustomMarker2.setTextColor(getContext().getResources().getColor(colores[5]));
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
