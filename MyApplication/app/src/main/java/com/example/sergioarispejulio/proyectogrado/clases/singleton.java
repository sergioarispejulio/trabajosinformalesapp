package com.example.sergioarispejulio.proyectogrado.clases;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

/**
 * Created by Sergio Arispe Julio on 2/18/2016.
 */
public class singleton {
    private static singleton ourInstance = new singleton();


    public String direccion_a_conectarse = "https://boiling-reef-72495.herokuapp.com/";
    public String token_mensajeria = "";
    //"http://192.168.56.1:3000"
    //https://aqueous-coast-38586.herokuapp.com/
    public String nombre_para_archivo_preferencias = "Trabajoinformal_preferencias";

    public int id_trabajo;

    public Trabajo trabajo_seleccionado;

    public ArrayList<Trabajo> lista_trabajos = new ArrayList<>();
    public ArrayList<Trabajo> lista_trabajos_disponibles = new ArrayList<>();
    public ArrayList<Solicitud> lista_solicitudes = new ArrayList<>();
    public ArrayList<Solicitud> lista_solicitudes_hechas = new ArrayList<>();
    public ArrayList<Usuario> lista_trabajadores = new ArrayList<>();
    public ArrayList<Perfiltrabajo> lista_mis_perfiles = new ArrayList<>();

    public ArrayList<Tipotrabajo> lista_tipos_de_trabajo = new ArrayList<>();

    public int posiciontrabajo;
    public int posicionsolicitud;

    public int peticion_seleccionada;

    public boolean desconectado = false;

    public Usuario trabajador_seleccionado;

    public Usuario usuario_actual;

    public double longitud; //y
    public double latitud; //x
    public boolean presiono;

    public boolean trabajador_aceptado;
    public boolean para_postularse;
    public boolean para_aceptar_postulacion;
    public boolean soy_creador;

    public Solicitud solicitud_seleccionada;

    public String nombre_tipo_de_trabajo;


    public static singleton getInstance() {
        return ourInstance;
    }
    private singleton() {
    }


    public void resetear_lista_trabajos() { lista_trabajos.clear(); }

    public void resetear_lista_trabajos_disponibles() { lista_trabajos_disponibles.clear(); }

    public void resetear_lista_trabajadores() { lista_trabajadores.clear(); }

    public void resetear_lista_mis_perfiles() { lista_mis_perfiles.clear(); }

    public void resetear_lista_tipos_de_trabajo() { lista_tipos_de_trabajo.clear(); }

    public void resetear_lista_postulaciones()
    {
        peticion_seleccionada = -1;
        lista_solicitudes.clear();
    }

    public void resetear_lista_de_mis_postulacionesv2()
    {
        peticion_seleccionada = -1;
        lista_solicitudes_hechas.clear();
    }


    public void resetear_lista_de_mis_postulaciones()
    {
        peticion_seleccionada = -1;
        lista_solicitudes.clear();
    }

    public void agregar_trabajo_a_lista(Trabajo elemento)
    {
        lista_trabajos.add(elemento);
    }


    public void agregar_tipo_de_trabajo_a_lista(Tipotrabajo elemento)
    {
        lista_tipos_de_trabajo.add(elemento);
    }

    public void agregar_trabajador(Usuario elemento)
    {
        if(elemento.id != usuario_actual.id)
            lista_trabajadores.add(elemento);
    }

    public void agregar_trabajo_disponible_a_lista(Trabajo elemento) {
        if(elemento.id_solicitante != usuario_actual.id)
            lista_trabajos_disponibles.add(elemento);
    }


    public void agregar_a_mis_postulaciones(Solicitud elemento)
    {
        lista_solicitudes_hechas.add(elemento);

    }


    public void agregar_postulacion_a_lista(Solicitud elemento)
    {
        lista_solicitudes.add(elemento);
        if(elemento.aceptado == true) {
            peticion_seleccionada = (lista_solicitudes.size() - 1);
        }
    }

    public void agregar_mi_perfil_a_lista(Perfiltrabajo elemento)
    {
        lista_mis_perfiles.add(elemento);
    }

    public String[] devolver_lista_de_titulos_trabajos_disponibles()
    {
        String[] values = new String[lista_trabajos_disponibles.size()];
        for(int i = 0; i < lista_trabajos_disponibles.size(); i++)
        {
            values[i] = lista_trabajos_disponibles.get(i).titulo;

        }
        return values;

    }

    public String[] devolver_lista_de_mis_solicitudes_de_trabajadores()
    {
        String[] values = new String[lista_trabajos.size()];
        for(int i = 0; i < lista_trabajos.size(); i++)
        {
            values[i] = lista_trabajos.get(i).titulo;

        }
        return values;

    }

    public String[] devolver_lista_de_trabajadores()
    {
        String[] values = new String[lista_trabajadores.size()];
        for(int i = 0; i < lista_trabajadores.size(); i++)
        {
            values[i] = lista_trabajadores.get(i).devolver_nombre_completo();

        }
        return values;

    }

    public Boolean[] devolver_estado_mis_perfiles()
    {
        Boolean[] values = new Boolean[lista_mis_perfiles.size()];
        for(int i = 0; i < lista_mis_perfiles.size(); i++)
        {
            values[i] = lista_mis_perfiles.get(i).habilitado;

        }
        return values;
    }

    public Trabajo devolver_trabajo()
    {
        return lista_trabajos.get(posiciontrabajo);
    }

    public void quitar_activo_de_un_trabajo()
    {
        lista_trabajos.get(posiciontrabajo).activo = false;
    }

    public Trabajo devolver_trabajo_disponible()
    {
        return lista_trabajos_disponibles.get(posiciontrabajo);
    }

    public String[] devolver_lista_de_postulaciones_a_mi_solicitud_de_trabajador()
    {
        String[] values = new String[lista_solicitudes.size()];
        for(int i = 0; i < lista_solicitudes.size(); i++)
        {
            Solicitud elemento = lista_solicitudes.get(i);
            values[i] =elemento.devolver_nombre_con_aceptado() + " - " + elemento.precio_medio + " Bs";
        }
        return values;
    }



    public String[] devolver_lista_de_mis_postulaciones()
    {

        String[] values = new String[lista_solicitudes_hechas.size()];
        for(int i = 0; i < lista_solicitudes_hechas.size(); i++)
        {
            values[i] = lista_solicitudes_hechas.get(i).nombre_trabajo;
        }
        return values;
    }

    public Solicitud devolver_postulacion_a_solicitud_de_trabajo()
    {
        return lista_solicitudes.get(posicionsolicitud);
    }

    public Solicitud devolver_postulacion_que_realize()
    {
        return lista_solicitudes_hechas.get(posicionsolicitud);
    }

    public void marcar_solicitud_de_trabajo_como_aceptada()
    {
        lista_solicitudes.get(posicionsolicitud).aceptado = true;
    }




}
