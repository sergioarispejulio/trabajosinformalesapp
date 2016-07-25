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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sergioarispejulio.proyectogrado.clases.CustomGrid;
import com.example.sergioarispejulio.proyectogrado.clases.ImageAdapter;
import com.example.sergioarispejulio.proyectogrado.clases.Tipotrabajo;
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

public class search_work extends AppCompatActivity {

    ProgressDialog progress;

    ListView listView;
    boolean se_conecto;
    String tipo_error;
    int posicion;

    GridView grid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Integer[] imageId = new Integer[singleton.getInstance().lista_tipos_de_trabajo.size()];
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(singleton.getInstance().lista_tipos_de_trabajo.size());
        for(int i = 0; i < singleton.getInstance().lista_tipos_de_trabajo.size(); i++)
        {
            imageId[i] = devolver_identificador_imagen(singleton.getInstance().lista_tipos_de_trabajo.get(i));
        }
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");

        GridView gridview = (GridView) findViewById(R.id.gridview2);
        gridview.setAdapter(new ImageAdapter(this,imageId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == 0)
                {
                    seleccion_albanil_busqueda();
                }
                if (position == 1)
                {
                    seleccion_plomero_busqueda();
                }
                if (position == 2)
                {
                    seleccion_jardinero_busqueda();
                }


            }
        });*/
        String[] web  = new String[singleton.getInstance().lista_tipos_de_trabajo.size()];
        int[] imageId = new int[singleton.getInstance().lista_tipos_de_trabajo.size()];
        for(int i = 0; i < singleton.getInstance().lista_tipos_de_trabajo.size(); i++)
        {
            web[i] = singleton.getInstance().lista_tipos_de_trabajo.get(i).nombre;
            imageId[i] = devolver_identificador_imagen(singleton.getInstance().lista_tipos_de_trabajo.get(i));
        }
        CustomGrid adapter = new CustomGrid(search_work.this, web, imageId);
        grid=(GridView)findViewById(R.id.gridview2);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().id_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).id;
                singleton.getInstance().nombre_tipo_de_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).nombre;
                obtener_datos();


            }
        });

        restringir();
    }

    public int devolver_identificador_imagen(Tipotrabajo elemento)
    {
        int drawableResourceId = this.getResources().getIdentifier(elemento.nombre, "drawable", this.getPackageName());
        System.out.println(drawableResourceId);
        if(drawableResourceId != 0)
            return drawableResourceId;
        else
            return R.drawable.no_existe;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void restringir()
    {
        /*Boolean[] lista = singleton.getInstance().devolver_estado_mis_perfiles();
        if(lista[0] == true)
            albanil.setVisibility(View.VISIBLE);
        else
            albanil.setVisibility(View.GONE);

        if(lista[1] == true)
            plomero.setVisibility(View.VISIBLE);
        else
            plomero.setVisibility(View.GONE);

        if(lista[2] == true)
            jardinero.setVisibility(View.VISIBLE);
        else
            jardinero.setVisibility(View.GONE);*/
    }

    public void seleccion_albanil_busqueda()
    {
        singleton.getInstance().id_trabajo = 1;
        obtener_datos();
    }

    public void seleccion_plomero_busqueda()
    {
        singleton.getInstance().id_trabajo = 2;
        obtener_datos();
    }

    public void seleccion_jardinero_busqueda()
    {
        singleton.getInstance().id_trabajo = 3;
        obtener_datos();
    }

    private void obtener_datos()
    {
        singleton.getInstance().resetear_lista_trabajos_disponibles();
        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_lista_de_trabajos_disponibles conector = new NetworkOperationAsyncTask_lista_de_trabajos_disponibles();
        conector.execute();
    }

    private void cargar_datos()
    {
        Intent x = new Intent(this, result_search_workV2.class);
        startActivity(x);
    }

    private void ir_al_resultado()
    {
        singleton.getInstance().trabajo_seleccionado = singleton.getInstance().lista_trabajos_disponibles.get(posicion);
        singleton.getInstance().para_postularse = true;
        singleton.getInstance().soy_creador = false;
        Intent x = new Intent(this, view_workV2.class);
        startActivity(x);
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    public void volver_search_work(View v)
    {
        finish();
    }

    private class NetworkOperationAsyncTask_lista_de_trabajos_disponibles extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenertrabajosactivos.json").build();
            try {
                SharedPreferences prefs = getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_tipo_trabajo", singleton.getInstance().id_trabajo);
                json.put("activo", true);
                json.put("ciudad", singleton.getInstance().usuario_actual.ciudad);
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
                    // Server returned HTTP error code.
                    tipo_error = "No se puede acceder al servidor web, intentelo mas tarde";
                    se_conecto = false;
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
                        System.out.println(i);
                        JSONObject jobPostJSON = array.getJSONObject(i);

                        Trabajo trabajo = new Trabajo();
                        trabajo.id = jobPostJSON.getInt("id");
                        trabajo.titulo = jobPostJSON.getString("titulo");
                        trabajo.descripcion = jobPostJSON.getString("descripcion");
                        trabajo.latitud = jobPostJSON.getDouble("coordenadax");
                        trabajo.longitud = jobPostJSON.getDouble("coordenaday");
                        trabajo.nombre_solicitante = jobPostJSON.getString("nombre_solicitante");
                        trabajo.fecha_de_requerimiento = jobPostJSON.getString("fecha_de_requerimiento") ;
                        trabajo.id_tipo_trabajo = jobPostJSON.getInt("id_tipo_trabajo");
                        trabajo.id_solicitante = jobPostJSON.getInt("id_solicitante");
                        trabajo.activo = jobPostJSON.getBoolean("activo");
                        trabajo.url_foto = jobPostJSON.getString("url_foto");

                        singleton.getInstance().agregar_trabajo_disponible_a_lista(trabajo);
                    }
                    progress.dismiss();
                    cargar_datos();

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
