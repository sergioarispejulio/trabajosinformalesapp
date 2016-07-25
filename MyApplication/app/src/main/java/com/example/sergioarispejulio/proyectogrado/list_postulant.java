package com.example.sergioarispejulio.proyectogrado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sergioarispejulio.proyectogrado.clases.Comentario;
import com.example.sergioarispejulio.proyectogrado.clases.Perfil;
import com.example.sergioarispejulio.proyectogrado.clases.Usuario;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class list_postulant extends Fragment {

    ProgressDialog progress;
    ListView listView;

    boolean se_conecto;
    String tipo_error;

    public list_postulant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_postulant, container, false);
        listView = (ListView) v.findViewById(R.id.listView_postulantes);
        String[] values = singleton.getInstance().devolver_lista_de_postulaciones_a_mi_solicitud_de_trabajador();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posicionsolicitud = position;
                entrar_listview();

            }

        });

        return v;
    }


    public void entrar_listview()
    {
        progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_buscar_trabajador exe = new NetworkOperationAsyncTask_buscar_trabajador();
        exe.execute();
    }

    public void entrar()
    {
        singleton.getInstance().para_postularse = false;
        if(singleton.getInstance().lista_solicitudes.get(singleton.getInstance().posicionsolicitud).aceptado == true) {
            singleton.getInstance().para_aceptar_postulacion = false;
            singleton.getInstance().trabajador_aceptado = true;
        }
        else {
            singleton.getInstance().para_aceptar_postulacion = true;
            singleton.getInstance().trabajador_aceptado = false;
        }
        Intent x = new Intent(getActivity(), view_profile_workerV2.class);
        startActivityForResult(x, 2);
    }

    public void refrescar()
    {
        String[] values = singleton.getInstance().devolver_lista_de_postulaciones_a_mi_solicitud_de_trabajador();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posicionsolicitud = position;
                entrar_listview();

            }

        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            refrescar();
        }
    }


    public void resultado(boolean estado, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(estado == true)
        {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se dio el puntaje exitosamente");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            builder.setCancelable(true);
            builder.setTitle("Error");
            builder.setMessage(status);
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        android.app.AlertDialog alertDialog;
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    private class NetworkOperationAsyncTask_buscar_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenerinfotrabajadordeterminado.json").build();
            try {

                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_trabajador", singleton.getInstance().lista_solicitudes.get(singleton.getInstance().posicionsolicitud).id_solicitante);
                json.put("id_tipo_trabajo", singleton.getInstance().devolver_trabajo().id_tipo_trabajo);
                json.put("id_usuario", singleton.getInstance().usuario_actual.id);
                se_conecto = true;

                DataOutputStream output = new DataOutputStream(urlConnection.getOutputStream());

                output.write(json.toString().getBytes());
                output.flush();
                StringBuffer buffer = new StringBuffer();


                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                    InputStream inputStream = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));


                    // Save the data in a String
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }

                } else {
                    tipo_error = "No se puede acceder al servidor web, intentelo mas tarde";
                    se_conecto = false;
                }
                output.close();
                return buffer.toString();


            } catch (IOException ex) {
                tipo_error = "Problemas con la conexión a internet, intentelo mas tarde";
                se_conecto = false;
            } catch (JSONException e) {
                tipo_error = "Problemas con la conexión a internet, intentelo mas tarde";
                se_conecto = false;
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
            return "";
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject jobPostJSON1 = array.getJSONObject(i);
                        JSONObject array_usuario = jobPostJSON1.getJSONObject("usuario");
                        JSONObject array_perfil = jobPostJSON1.getJSONObject("perfil");


                        Usuario usuario = new Usuario();
                        usuario.id = array_usuario.getInt("id");
                        usuario.nombre = array_usuario.getString("nombre");
                        usuario.apellido = array_usuario.getString("apellido");
                        usuario.ci = array_usuario.getInt("ci");
                        usuario.ciudad = array_usuario.getString("ciudad");
                        usuario.telefono = array_usuario.getInt("telefono");
                        usuario.url_imagen = array_usuario.getString("url_foto");

                        Perfil perfil = new Perfil();
                        perfil.id = array_perfil.getInt("id");
                        perfil.id_trabajo = array_perfil.getInt("id_usuario");
                        perfil.puntaje_promedio = array_perfil.getInt("puntaje_promedio");
                        perfil.habilitado = array_perfil.getBoolean("habilitado");

                        System.out.println(jobPostJSON1);
                        usuario.perfil_trabajo_actual = perfil;
                        singleton.getInstance().solicitud_seleccionada = singleton.getInstance().lista_solicitudes.get(singleton.getInstance().posicionsolicitud);
                        singleton.getInstance().trabajador_seleccionado = usuario;
                    }

                    NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador exe = new NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador();
                    exe.execute();
                }
                else
                {
                    mostrar_mensaje_de_error("Error", tipo_error);
                }

            } catch (JSONException ex) {
                System.out.println(ex);
            }
        }
    }

    private class NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenercomentariostrabajador.json").build();
            try {

                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_trabajador_que_recibe_comentario", singleton.getInstance().trabajador_seleccionado.id);
                json.put("id_perfil_trabajo", singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.id);
                se_conecto = true;

                DataOutputStream output = new DataOutputStream(urlConnection.getOutputStream());

                output.write(json.toString().getBytes());
                output.flush();
                StringBuffer buffer = new StringBuffer();


                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                    InputStream inputStream = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));


                    // Save the data in a String
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }

                } else {
                    tipo_error = "No se puede acceder al servidor web, intentelo mas tarde";
                    se_conecto = false;
                }
                output.close();
                return buffer.toString();


            } catch (IOException ex) {
                tipo_error = "Problemas con la conexión a internet, intentelo mas tarde";
                se_conecto = false;
            } catch (JSONException e) {
                tipo_error = "Problemas con la conexión a internet, intentelo mas tarde";
                se_conecto = false;
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
            return "";
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject jobPostJSON1 = array.getJSONObject(i);

                        Comentario comentario = new Comentario();
                        comentario.id = jobPostJSON1.getInt("id");
                        comentario.comentario = jobPostJSON1.getString("comentario");
                        comentario.id_usuario_que_comento = jobPostJSON1.getInt("id_usuario_que_comenta");
                        comentario.nombre_usuario_comento = jobPostJSON1.getString("nombre_usuario_que_comenta");
                        singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.lista_comentarios.add(comentario);
                    }

                    progress.dismiss();
                    entrar();
                }
                else
                {
                    progress.dismiss();
                    mostrar_mensaje_de_error("Error", tipo_error);
                }

            } catch (JSONException ex) {
                progress.dismiss();
                System.out.println(ex);
            }
        }
    }





}
