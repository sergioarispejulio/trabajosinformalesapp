package com.example.sergioarispejulio.proyectogrado;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class select_type_of_job_client extends AppCompatActivity {

    ProgressDialog progress;
    boolean se_conecto;
    String tipo_error;

    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type_of_job_client);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GridView gridview = (GridView) findViewById(R.id.gridview);

        /*Integer[] imageId = new Integer[singleton.getInstance().lista_tipos_de_trabajo.size()];
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(singleton.getInstance().lista_tipos_de_trabajo.size());
        for(int i = 0; i < singleton.getInstance().lista_tipos_de_trabajo.size(); i++)
        {
            imageId[i] = devolver_identificador_imagen(singleton.getInstance().lista_tipos_de_trabajo.get(i));
        }
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");


        gridview.setAdapter(new ImageAdapter(this, imageId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(position == 0)
                    seleccion_albanil_cliente();
                if(position == 1)
                    seleccion_plomero_cliente();
                if(position == 2)
                    seleccion_jardinero_cliente();


            }
        });*/

        String[] web  = new String[singleton.getInstance().lista_tipos_de_trabajo.size()];
        int[] imageId = new int[singleton.getInstance().lista_tipos_de_trabajo.size()];
        for(int i = 0; i < singleton.getInstance().lista_tipos_de_trabajo.size(); i++)
        {
            web[i] = singleton.getInstance().lista_tipos_de_trabajo.get(i).nombre;
            imageId[i] = devolver_identificador_imagen(singleton.getInstance().lista_tipos_de_trabajo.get(i));
        }
        CustomGrid adapter = new CustomGrid(select_type_of_job_client.this, web, imageId);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().id_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).id;
                singleton.getInstance().nombre_tipo_de_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).nombre;
                siguiente_ventana();
            }
        });

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


    public void seleccion_albanil_cliente()
    {
        singleton.getInstance().id_trabajo = 1;
        siguiente_ventana();
    }

    public void seleccion_plomero_cliente()
    {
        singleton.getInstance().id_trabajo = 2;
        siguiente_ventana();
    }

    public void seleccion_jardinero_cliente()
    {
        singleton.getInstance().id_trabajo = 3;
        siguiente_ventana();
    }

    private void siguiente_ventana()
    {
        singleton.getInstance().resetear_lista_trabajos();
        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_buscar_mis_solicitudes conector = new NetworkOperationAsyncTask_buscar_mis_solicitudes();
        conector.execute();

    }

    public void entrar()
    {
        Intent x = new Intent(this, form_create_request_of_employeeV2.class);
        startActivityForResult(x, 2);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            System.out.println("ENTRO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! X 1");
            setResult(2);
            finish();
        }
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    private class NetworkOperationAsyncTask_buscar_mis_solicitudes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenertrabajospublicados.json").build();
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
                json.put("id_solicitante", prefs.getString("id", "0"));
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
                        JSONObject array_trabajo = array.getJSONObject(i);

                        Trabajo trabajo = new Trabajo();
                        trabajo.id = array_trabajo.getInt("id");
                        trabajo.titulo = array_trabajo.getString("titulo");
                        trabajo.descripcion = array_trabajo.getString("descripcion");
                        trabajo.latitud = array_trabajo.getDouble("coordenadax");
                        trabajo.longitud = array_trabajo.getDouble("coordenaday");
                        trabajo.nombre_solicitante = array_trabajo.getString("nombre_solicitante");
                        //trabajo.fecha_publicacion = array_trabajo.getInt("id") ;
                        //trabajo.fecha_limite_publicacion = array_trabajo.getInt("id") ;
                        trabajo.id_tipo_trabajo = array_trabajo.getInt("id_tipo_trabajo");
                        trabajo.id_solicitante = array_trabajo.getInt("id_solicitante");
                        trabajo.activo = array_trabajo.getBoolean("activo");

                        singleton.getInstance().agregar_trabajo_a_lista(trabajo);
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
                System.out.println("BAI BAI");
                System.out.println(ex);
            }
        }
    }
}
