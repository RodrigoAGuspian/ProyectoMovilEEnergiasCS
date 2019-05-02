package com.casasolarctpi.appeenergia.models;

public class DatosTiempoReal {
    private String fechaActual,hora, fechaActual1 ,corriente1, corriente2, corriente3, potencia1, potencia2, potencia3;

    public DatosTiempoReal() {
    }

    public DatosTiempoReal(String fechaActual, String hora, String fechaActual1, String corriente1, String corriente2, String corriente3, String potencia1, String potencia2, String potencia3) {
        this.fechaActual = fechaActual;
        this.hora = hora;
        this.fechaActual1 = fechaActual1;
        this.corriente1 = corriente1;
        this.corriente2 = corriente2;
        this.corriente3 = corriente3;
        this.potencia1 = potencia1;
        this.potencia2 = potencia2;
        this.potencia3 = potencia3;
    }

    public String getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFechaActual1() {
        return fechaActual1;
    }

    public void setFechaActual1(String fechaActual1) {
        this.fechaActual1 = fechaActual1;
    }

    public String getCorriente1() {
        return corriente1;
    }

    public void setCorriente1(String corriente1) {
        this.corriente1 = corriente1;
    }

    public String getCorriente2() {
        return corriente2;
    }

    public void setCorriente2(String corriente2) {
        this.corriente2 = corriente2;
    }

    public String getCorriente3() {
        return corriente3;
    }

    public void setCorriente3(String corriente3) {
        this.corriente3 = corriente3;
    }

    public String getPotencia1() {
        return potencia1;
    }

    public void setPotencia1(String potencia1) {
        this.potencia1 = potencia1;
    }

    public String getPotencia2() {
        return potencia2;
    }

    public void setPotencia2(String potencia2) {
        this.potencia2 = potencia2;
    }

    public String getPotencia3() {
        return potencia3;
    }

    public void setPotencia3(String potencia3) {
        this.potencia3 = potencia3;
    }
}
