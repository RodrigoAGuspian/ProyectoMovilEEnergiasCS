package com.casasolarctpi.appeenergia.models;

import android.annotation.SuppressLint;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.controllers.Splash;

public class Constants {
    @SuppressLint("StaticFieldLeak")

    //Cosntante para el consumo de servicios web
    public static  final String BASE_URL="https://innovatec-server.herokuapp.com/";
    //Declaración de constantes
    public static final String[] GROUP_LIST = {Splash.context.getString(R.string.paginas), Splash.context.getString(R.string.conocenos)};
    public static final String[] PAGES_LIST = {"Sena","Sena Regional", "CTPI Cauca", "Sennova",};
    public static final String[] CONOCENOS_LIST = {Splash.context.getString(R.string.contactanos),Splash.context.getString(R.string.acerca_de),};
    //Vector el cual se encarga de la lista de las paginas y conocenos
    public static final String[] [] CHILDREN_LISTS = {PAGES_LIST,CONOCENOS_LIST};
    //Lista de los links de contáctanos
    public static final String[] LIST_LINKS_CONOCENOS={"http://www.sena.edu.co/es-co/Paginas/default.aspx","http://www.sena.edu.co/es-co/Paginas/default.aspx","https://ctpisenacauca.blogspot.com/","http://www.sena.edu.co/es-co/formacion/Paginas/tecnologia-innovacion.aspx"};

    //Lista de consultas
    public static final String[] LIST_QUERY={Splash.context.getResources().getString(R.string.tarjeta1_tiempo),
            Splash.context.getResources().getString(R.string.tarjeta2_tiempo),
            Splash.context.getResources().getString(R.string.tarjeta3_tiempo),
            Splash.context.getResources().getString(R.string.tarjetas_tiempo),
            Splash.context.getResources().getString(R.string.potencias_tiempo)};

    //Lista de mese del año
    public static final String[] MESES={Splash.context.getString(R.string.enero),Splash.context.getString(R.string.febrero),Splash.context.getString(R.string.marzo),Splash.context.getString(R.string.abril),Splash.context.getString(R.string.mayo),
            Splash.context.getString(R.string.junio),Splash.context.getString(R.string.julio),Splash.context.getString(R.string.agosto),Splash.context.getString(R.string.septiembre),Splash.context.getString(R.string.octubre),Splash.context.getString(R.string.noviembre),Splash.context.getString(R.string.diciembre)};

    //Constante para año máximo y mínimo de la consulta
    public static final int MIN_YEAR = 2018;
    public static final int MAX_YEAR = 2099;

    //Días de la semana
    public static final String[] DIAS_DE_LA_SEMANA = {Splash.context.getString(R.string.domingo),Splash.context.getString(R.string.lunes),Splash.context.getString(R.string.martes),Splash.context.getString(R.string.miercoles), Splash.context.getString(R.string.jueves),Splash.context.getString(R.string.viernes),Splash.context.getString(R.string.sabado)};

    //Lista de tipos de uso
    public static final String[] LIST_TIPO_DE_USO = {Splash.context.getString(R.string.academico),Splash.context.getString(R.string.empresarial),Splash.context.getString(R.string.comercial),Splash.context.getString(R.string.otro)};

    //Lista de los datos a utilizar;
    public  static final String[] tipoDeDato={
            Splash.context.getString(R.string.corriente1),Splash.context.getString(R.string.corriente2),Splash.context.getString(R.string.corriente3),
            Splash.context.getString(R.string.potencia1),Splash.context.getString(R.string.potencia2),Splash.context.getString(R.string.potencia3)
    };

    public  static final String[] tipoDeDato1={
            Splash.context.getString(R.string.c1),Splash.context.getString(R.string.c2),Splash.context.getString(R.string.c3),
            Splash.context.getString(R.string.p1),Splash.context.getString(R.string.p2),Splash.context.getString(R.string.p3)
    };

    //Lista de colores de las gráficas

    public static final int [] coloresGrafica={
            R.color.colorCorriente1,R.color.colorCorriente2,R.color.colorCorriente3,
            R.color.colorPotencia1,R.color.colorPotencia2,R.color.colorPotencia3,
    };




}

