package com.example.sergioarispejulio.proyectogrado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.sergioarispejulio.proyectogrado.clases.Comentario;
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

public class comment_worker extends AppCompatActivity {


    ProgressDialog progress;
    boolean se_conecto;
    String tipo_error;
    EditText contenedor;
    String contenido;
    Comentario nuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_worker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nuevo = new Comentario();

        contenedor = (EditText) findViewById(R.id.editText3);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void comentar_trabajador(View v)
    {
        contenido = contenedor.getText().toString();
        nuevo.comentario = contenido;
        nuevo.nombre_usuario_comento = singleton.getInstance().usuario_actual.devolver_nombre_completo();
        nuevo.id_usuario_que_comento = singleton.getInstance().usuario_actual.id;
        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_comentar_trabajador exe = new NetworkOperationAsyncTask_comentar_trabajador();
        exe.execute();
    }

    public void resultado(boolean estado, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(estado == true)
        {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se comento exitosamente");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    setResult(2);
                    finish();
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
        alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    public void volver_comment_worker(View v)
    {
        finish();
    }

    private class NetworkOperationAsyncTask_comentar_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("comentartrabajador.json").build();
            try {
                SharedPreferences prefs =  getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("comentario", contenido);
                json.put("id_usuario_que_comenta", singleton.getInstance().usuario_actual.id);
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
                progress.dismiss();
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    System.out.println(jobPostJSON.getString("status"));
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.lista_comentarios.add(nuevo);
                        resultado(true, jobPostJSON.getString("status"));
                    } else {
                        resultado(false, jobPostJSON.getString("status"));
                    }
                }
                else
                {
                    mostrar_mensaje_de_error("Error", tipo_error);
                }

            } catch (JSONException ex) {
                progress.dismiss();
            }
        }
    }

}
