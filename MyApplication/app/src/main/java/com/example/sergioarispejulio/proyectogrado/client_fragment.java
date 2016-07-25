package com.example.sergioarispejulio.proyectogrado;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class client_fragment extends Fragment {


    ProgressDialog progress;
    ListView listView;
    boolean se_conecto;
    String tipo_error;
    int posicion;

    public client_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_client_index_with_logging_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.listView_cliente);

        String[] values = singleton.getInstance().devolver_lista_de_mis_solicitudes_de_trabajadores();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posiciontrabajo = position;
                posicion = position;
                singleton.getInstance().resetear_lista_postulaciones();
                progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
                NetworkOperationAsyncTask_buscar_peticion_a_mis_solicitudes conector = new NetworkOperationAsyncTask_buscar_peticion_a_mis_solicitudes();
                conector.execute();
            }

        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.boton_flotante_cliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), select_type_of_job_client.class);
                startActivityForResult(intent, 2);
            }
        });
        return v;
    }

    private void actualizar()
    {
        String[] values = singleton.getInstance().devolver_lista_de_mis_solicitudes_de_trabajadores();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posiciontrabajo = position;
                posicion = position;
                singleton.getInstance().resetear_lista_postulaciones();
                progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
                NetworkOperationAsyncTask_buscar_peticion_a_mis_solicitudes conector = new NetworkOperationAsyncTask_buscar_peticion_a_mis_solicitudes();
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

    public void entrar()
    {
        singleton.getInstance().trabajo_seleccionado = singleton.getInstance().lista_trabajos.get(posicion);
        System.out.println(singleton.getInstance().trabajo_seleccionado.id);
        singleton.getInstance().para_postularse = false;
        singleton.getInstance().soy_creador = true;
        Intent intent = new Intent(getActivity(), view_workV2.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            actualizar();
        }
    }

    private class NetworkOperationAsyncTask_buscar_peticion_a_mis_solicitudes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenerpeticionesalassolicitudes.json").build();
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
                json.put("id_trabajo", singleton.getInstance().devolver_trabajo().id);
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
                System.out.println(ex);
            } catch (JSONException e) {
                tipo_error = "Problemas con la conexión a internet, intentelo mas tarde";
                se_conecto = false;
                e.printStackTrace();
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
                        solicitud.nombre = jobPostJSON1.getString("nombre_usuario");
                        solicitud.precio_medio = array_solicitud.getInt("precio_medio");

                        singleton.getInstance().agregar_postulacion_a_lista(solicitud);
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
                System.out.println(ex);
            }
        }
    }




}