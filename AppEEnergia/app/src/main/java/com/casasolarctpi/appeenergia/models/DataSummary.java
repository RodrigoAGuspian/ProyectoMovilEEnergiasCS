package com.casasolarctpi.appeenergia.models;

public class DataSummary {
    private String dia, horaMax, horaMin;
    private float energiaDia, potenciaMax, potenciaMin;

    public DataSummary() {

    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraMax() {
        return horaMax;
    }

    public void setHoraMax(String horaMax) {
        this.horaMax = horaMax;
    }

    public String getHoraMin() {
        return horaMin;
    }

    public void setHoraMin(String horaMin) {
        this.horaMin = horaMin;
    }

    public float getEnergiaDia() {
        return energiaDia;
    }

    public void setEnergiaDia(float energiaDia) {
        this.energiaDia = energiaDia;
    }

    public float getPotenciaMax() {
        return potenciaMax;
    }

    public void setPotenciaMax(float potenciaMax) {
        this.potenciaMax = potenciaMax;
    }

    public float getPotenciaMin() {
        return potenciaMin;
    }

    public void setPotenciaMin(float potenciaMin) {
        this.potenciaMin = potenciaMin;
    }
}
