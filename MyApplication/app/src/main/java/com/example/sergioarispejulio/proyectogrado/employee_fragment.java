package com.example.sergioarispejulio.proyectogrado;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
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

/**
 * Created by Sergio Arispe Julio on 2/22/2016.
 */
public class employee_fragment extends Fragment {

    ProgressDialog progress;
    ListView listView;
    boolean se_conecto;
    String tipo_error;

    public employee_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee_index_with_logging_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.listView_trabajador);

        String[] values = singleton.getInstance().devolver_lista_de_mis_postulaciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posicionsolicitud = position;
                progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
                NetworkOperationAsyncTask_buscar_trabajos_especifico conector = new NetworkOperationAsyncTask_buscar_trabajos_especifico();
                conector.execute();
            }

        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.boton_flotante_trabajador_buscar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), search_work.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.boton_flotante_trabajador_actualizar);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
                singleton.getInstance().resetear_lista_de_mis_postulaciones();
                NetworkOperationAsyncTask_lista_de_mis_postulaciones exe = new NetworkOperationAsyncTask_lista_de_mis_postulaciones();
                exe.execute();
            }
        });

        return v;
    }

    public void actualizar()
    {
        String[] values = singleton.getInstance().devolver_lista_de_mis_postulaciones();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posicionsolicitud = position;
                NetworkOperationAsyncTask_buscar_trabajos_especifico conector = new NetworkOperationAsyncTask_buscar_trabajos_especifico();
                conector.execute();
            }

        });
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    public void entrar_trabajador()
    {
        singleton.getInstance().para_postularse = false;
        singleton.getInstance().soy_creador = false;
        Intent intent = new Intent(getActivity(), view_workV2.class);
        startActivityForResult(intent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            /*NetworkOperationAsyncTask_lista_de_mis_postulaciones exe = new NetworkOperationAsyncTask_lista_de_mis_postulaciones();
            exe.execute();*/
        }
    }





    private class NetworkOperationAsyncTask_buscar_trabajos_especifico extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("buscartrabajoporid.json").build();
            try {
                SharedPreferences prefs = getActivity().getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_trabajo", singleton.getInstance().devolver_postulacion_que_realize().id_trabajo);
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
                    JSONObject jobPostJSON1 = array.getJSONObject(0);
                    JSONObject jobPostJSON = jobPostJSON1.getJSONObject("trabajo");

                    Trabajo trabajo = new Trabajo();
                    trabajo.id = jobPostJSON.getInt("id");
                    trabajo.titulo = jobPostJSON.getString("titulo");
                    trabajo.descripcion = jobPostJSON.getString("descripcion");
                    trabajo.latitud = jobPostJSON.getDouble("coordenadax");
                    trabajo.longitud = jobPostJSON.getDouble("coordenaday");
                    trabajo.nombre_solicitante = jobPostJSON.getString("nombre_solicitante");
                    trabajo.id_tipo_trabajo = jobPostJSON.getInt("id_tipo_trabajo");
                    trabajo.id_solicitante = jobPostJSON.getInt("id_solicitante");
                    trabajo.activo = jobPostJSON.getBoolean("activo");
                    trabajo.url_foto = jobPostJSON.getString("url_foto");

                    singleton.getInstance().trabajo_seleccionado = trabajo;

                    progress.dismiss();
                    entrar_trabajador();
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

    private class NetworkOperationAsyncTask_lista_de_mis_postulaciones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenermispeticionesasolicitudes.json").build();
            try {
                SharedPreferences prefs = getActivity().getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_usuario", prefs.getString("id", "0"));
                System.out.println(prefs.getString("id", "0"));
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
                    // Server returned HTTP error code.
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
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jobPostJSON1 = array.getJSONObject(i);
                        JSONObject array_solicitud = jobPostJSON1.getJSONObject("solicitud");

                        Solicitud solicitud = new Solicitud();
                        solicitud.id = array_solicitud.getInt("id");
                        solicitud.id_trabajo = array_solicitud.getInt("id_trabajo");
                        solicitud.id_solicitante = array_solicitud.getInt("id_usuario");
                        solicitud.aceptado = array_solicitud.getBoolean("aceptado");
                        solicitud.nombre_trabajo = jobPostJSON1.getString("nombre_trabajo");
                        singleton.getInstance().agregar_a_mis_postulaciones(solicitud);
                    }
                    progress.dismiss();
                    actualizar();
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

