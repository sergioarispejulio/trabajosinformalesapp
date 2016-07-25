package com.example.sergioarispejulio.proyectogrado.clases;

/**
 * Created by Sergio Arispe Julio on 2/18/2016.
 */
public class Solicitud {

    public int id;
    public int id_solicitante;
    public int id_trabajo;
    public boolean aceptado;
    public int precio_medio=0;
    public String nombre;
    public String nombre_trabajo;

    public String devolver_nombre_con_aceptado()
    {
        String resu = nombre;
        if(aceptado == true)
            resu = resu + " - Acepto a este trabajador";
        return resu;
    }
}
