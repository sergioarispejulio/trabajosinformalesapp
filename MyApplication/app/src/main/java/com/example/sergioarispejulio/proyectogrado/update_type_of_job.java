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
import android.widget.Switch;

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

public class update_type_of_job extends AppCompatActivity {

    ProgressDialog progress;

    Switch albanil,plomero,jardinero;
    boolean se_conecto;
    String tipo_error;
    int id_perfil;
    int posi;
    boolean habilitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_type_of_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        albanil = (Switch) findViewById(R.id.switch1);
        plomero = (Switch) findViewById(R.id.switch2);
        jardinero = (Switch) findViewById(R.id.switch3);

        actualizar_info();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void actualizar_info()
    {
        Boolean[] lista = singleton.getInstance().devolver_estado_mis_perfiles();
        if(lista[0] == true)
            albanil.setChecked(true);
        else
            albanil.setChecked(false);

        if(lista[1] == true)
            plomero.setChecked(true);
        else
            plomero.setChecked(false);

        if(lista[2] == true)
            jardinero.setChecked(true);
        else
            jardinero.setChecked(false);
    }

    public void actualizar__perfil_albanil(View v)
    {
        posi = 0;
        id_perfil = singleton.getInstance().lista_mis_perfiles.get(0).id;
        habilitado = albanil.isChecked();
        actualizar_en_servidor();
    }

    public void actualizar__perfil_plomero(View v)
    {
        posi = 1;
        id_perfil = singleton.getInstance().lista_mis_perfiles.get(1).id;
        habilitado = plomero.isChecked();
        actualizar_en_servidor();
    }

    public void actualizar__perfil_jardinero(View v)
    {
        posi = 2;
        id_perfil = singleton.getInstance().lista_mis_perfiles.get(2).id;
        habilitado = jardinero.isChecked();
        actualizar_en_servidor();
    }

    public void actualizar_en_servidor()
    {
        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_cambiar_estados_perfiles exe = new NetworkOperationAsyncTask_cambiar_estados_perfiles();
        exe.execute();
    }

    public void resultado(boolean estado, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(estado == true)
        {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se actualizo exitosamente su perfil");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
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
        alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    public void volver_update_type_of_job(View v)
    {
        finish();
    }



    private class NetworkOperationAsyncTask_cambiar_estados_perfiles extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("actualizarestadoperfiltrabajador.json").build();
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
                json.put("id", id_perfil);
                json.put("habilitado", habilitado);
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
                        singleton.getInstance().lista_mis_perfiles.get(posi).habilitado = habilitado;
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
