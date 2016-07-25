package com.example.sergioarispejulio.proyectogrado.clases;

import java.util.InputMismatchException;

/**
 * Created by Sergio Arispe Julio on 4/7/2016.
 */
public class Usuario {


    public String nombre;
    public String apellido;
    public String url_imagen;
    public String ciudad;
    public String fecha_nacimiento;
    public int ci;
    public int id;
    public int telefono;
    public boolean comentado_por_usuario_actual;
    public Perfil perfil_trabajo_actual;

    public String devolver_nombre_completo()
    {
        return  nombre + " " + apellido;
    }

    public int devolver_ano_nacimiento()
    {
        String[] aux;
        if (fecha_nacimiento.contains("/"))
            aux = fecha_nacimiento.split("/");
        else
            aux = fecha_nacimiento.split("-");
        System.out.println("----------------------------------");
        System.out.println(aux.length);
        System.out.println(aux[0]);
        System.out.println(aux[1]);
        System.out.println(aux[2]);
        System.out.println(fecha_nacimiento);
        return Integer.parseInt(aux[0]);
    }

    public int devolver_mes_nacimiento()
    {
        String[] aux;
        if (fecha_nacimiento.contains("/"))
            aux = fecha_nacimiento.split("/");
        else
            aux = fecha_nacimiento.split("-");
        return Integer.parseInt(aux[1]);
    }

    public int devolver_dia_nacimiento()
    {
        String[] aux;
        if (fecha_nacimiento.contains("/"))
            aux = fecha_nacimiento.split("/");
        else
            aux = fecha_nacimiento.split("-");
        return Integer.parseInt(aux[2]);
    }

}
