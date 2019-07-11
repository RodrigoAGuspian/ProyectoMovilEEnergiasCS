package com.casasolarctpi.appeenergia.models;

public class DatosPromedio {
    private String hora;
    private float corriente1=0, corriente2=0, corriente3=0, potencia1=0, potencia2=0, potencia3=0;

    public DatosPromedio() {
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public float getCorriente1() {
        return corriente1;
    }

    public void setCorriente1(float corriente1) {
        this.corriente1 = corriente1;
    }

    public float getCorriente2() {
        return corriente2;
    }

    public void setCorriente2(float corriente2) {
        this.corriente2 = corriente2;
    }

    public float getCorriente3() {
        return corriente3;
    }

    public void setCorriente3(float corriente3) {
        this.corriente3 = corriente3;
    }

    public float getPotencia1() {
        return potencia1;
    }

    public void setPotencia1(float potencia1) {
        this.potencia1 = potencia1;
    }

    public float getPotencia2() {
        return potencia2;
    }

    public void setPotencia2(float potencia2) {
        this.potencia2 = potencia2;
    }

    public float getPotencia3() {
        return potencia3;
    }

    public void setPotencia3(float potencia3) {
        this.potencia3 = potencia3;
    }
}
