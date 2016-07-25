package com.example.sergioarispejulio.proyectogrado.clases;

import java.util.ArrayList;

/**
 * Created by Sergio Arispe Julio on 4/14/2016.
 */
public class Perfil {

    public int id_trabajo;
    public int id;
    public int puntaje_promedio;
    public boolean habilitado;

    public ArrayList<Comentario> lista_comentarios = new ArrayList<>();

    public String[] devolver_lista_de_comentarios()
    {
        String[] resultado = new String[lista_comentarios.size()];
        for(int i = 0; i < lista_comentarios.size(); i++)
        {
            Comentario aux = lista_comentarios.get(i);
            resultado[i] = aux.nombre_usuario_comento + " --- " + aux.comentario;
        }
        return resultado;
    }
}
