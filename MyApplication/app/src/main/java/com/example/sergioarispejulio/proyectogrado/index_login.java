package com.example.sergioarispejulio.proyectogrado;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.sergioarispejulio.proyectogrado.clases.Comentario;
import com.example.sergioarispejulio.proyectogrado.clases.Perfiltrabajo;
import com.example.sergioarispejulio.proyectogrado.clases.RegistrationService;
import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Tipotrabajo;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
import com.example.sergioarispejulio.proyectogrado.clases.Usuario;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

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

public class index_login extends AppCompatActivity {


    ProgressDialog progress;

    EditText email,contrasena;
    String contenedor_email,contenedor_contrasena;
    boolean se_conecto;
    boolean condi;
    String tipo_error,aux_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_login);
        condi = true;
        email = (EditText) findViewById(R.id.editText);
        contrasena = (EditText) findViewById(R.id.editText2);
        singleton.getInstance().resetear_lista_postulaciones();
        singleton.getInstance().resetear_lista_trabajos();
        /*progress = ProgressDialog.show(this, "", "Verificando datos de logueo", true);
        NetworkOperationAsyncTask_verificartoken verificador = new NetworkOperationAsyncTask_verificartoken();
        verificador.execute();*/


    }



    public void ir_a_registrarse(View v)
    {
        Intent x = new Intent(this, register.class);
        startActivityForResult(x, 2);
    }

    public void ir_a_inicio_logeado()
    {
        startActivity(new Intent(getBaseContext(), index_with_logging_v2.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    public void logearse(View v)
    {
        singleton.getInstance().resetear_lista_postulaciones();
        singleton.getInstance().resetear_lista_trabajos();
        singleton.getInstance().resetear_lista_de_mis_postulacionesv2();
        contenedor_email = email.getText().toString();
        contenedor_contrasena = contrasena.getText().toString();
        if(verificar_ingreso_informacion() == true)
        {
            progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
            NetworkOperationAsyncTask_iniciarseccion asyncTask = new NetworkOperationAsyncTask_iniciarseccion();
            asyncTask.execute();
        }
        else
        {
            mostrar_mensaje_de_error("Falta información", "Complete todos los campos de información");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            startActivity(new Intent(getBaseContext(), index_with_logging_v2.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
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

    private boolean verificar_ingreso_informacion()
    {
        boolean resu = true;
        if(email.getText().toString().contentEquals(""))
            resu = false;
        if(contrasena.getText().toString().contentEquals(""))
            resu = false;
        return resu;
    }




    private class NetworkOperationAsyncTask_iniciarseccion extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("iniciarseccion.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("nickname", contenedor_email);
                json.put("password", contenedor_contrasena);
                json.put("modelo", Build.MANUFACTURER + " " + Build.MODEL);
                json.put("so", android.os.Build.VERSION.SDK_INT);
                json.put("token_mensajeria", singleton.getInstance().token_mensajeria);
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
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        SharedPreferences prefs = getSharedPreferences("Trabajoinformal_preferencias", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", jobPostJSON.getString("id_usuario"));
                        editor.putString("token", jobPostJSON.getString("token"));


                        editor.commit();
                        NetworkOperationAsyncTask_informacion_usuario exe = new NetworkOperationAsyncTask_informacion_usuario();
                        exe.execute();
                    } else {
                        SharedPreferences prefs = getSharedPreferences("Trabajoinformal_preferencias", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", "");
                        editor.putString("token", "");
                        editor.commit();

                        mostrar_mensaje_de_error("Error al acceder", "Nombre de usuario o contraseña incorrecta");
                    }
                }
                else
                {
                    progress.dismiss();
                    mostrar_mensaje_de_error("Error", tipo_error);
                }

            } catch (JSONException ex) {
                progress.dismiss();
            }
        }
    }





    private class NetworkOperationAsyncTask_verificartoken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("verificartoken.json").build();
            try {
                SharedPreferences prefs =  getSharedPreferences("Trabajoinformal_preferencias", Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("token", prefs.getString("token", ""));
                json.put("id", prefs.getString("id", "0"));
                json.put("token_mensajeria", singleton.getInstance().token_mensajeria);
                aux_error = prefs.getString("id", "0");

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
                    System.out.println("NO RECIBIO NADA, NI MIERDA!!!!!");
                    // Server returned HTTP error code.
                }
                output.close();
                return buffer.toString();


            } catch (IOException ex) {
                System.out.println(ex);
            } catch (JSONException e) {
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
                JSONArray array = new JSONArray(json);
                System.out.println(array);
                JSONObject jobPostJSON = array.getJSONObject(0);
                System.out.println(jobPostJSON);
                System.out.println(jobPostJSON.getString("status"));
                if(jobPostJSON.getString("status").contentEquals("OK") == true )
                {
                    System.out.println("");
                    NetworkOperationAsyncTask_informacion_usuario exe = new NetworkOperationAsyncTask_informacion_usuario();
                    exe.execute();



                }
                else
                {
                }

            } catch (JSONException ex) {

            }
        }
    }

    private class NetworkOperationAsyncTask_informacion_usuario extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenerinfousuariodeterminado.json").build();
            try {
                SharedPreferences prefs =  getSharedPreferences("Trabajoinformal_preferencias", Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id", prefs.getString("id", "0"));
                aux_error = prefs.getString("id", "0");

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
                    System.out.println("NO RECIBIO NADA, NI MIERDA!!!!!");
                    // Server returned HTTP error code.
                }
                output.close();
                return buffer.toString();


            } catch (IOException ex) {
                System.out.println(ex);
            } catch (JSONException e) {
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
                JSONArray array = new JSONArray(json);
                System.out.println(array);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject jobPostJSON1 = array.getJSONObject(i);

                    JSONObject array_usuario = jobPostJSON1.getJSONObject("usuario");


                    Usuario usuario = new Usuario();
                    usuario.id = array_usuario.getInt("id");
                    usuario.nombre = array_usuario.getString("nombre");
                    usuario.apellido = array_usuario.getString("apellido");
                    usuario.ci = array_usuario.getInt("ci");
                    usuario.ciudad = array_usuario.getString("ciudad");
                    usuario.telefono = array_usuario.getInt("telefono");
                    usuario.fecha_nacimiento = array_usuario.getString("fecha_nacimiento");
                    usuario.url_imagen = array_usuario.getString("url_foto");

                    singleton.getInstance().usuario_actual = usuario;
                }
                NetworkOperationAsyncTask_obtener_perfiles exe = new NetworkOperationAsyncTask_obtener_perfiles();
                exe.execute();

            } catch (JSONException ex) {

            }
        }
    }

    private class NetworkOperationAsyncTask_obtener_perfiles extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenermisperfilestrabajador.json").build();
            try {
                SharedPreferences prefs = getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                singleton.getInstance().resetear_lista_mis_perfiles();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
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
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject array_usuario = array.getJSONObject(i);

                        Perfiltrabajo perfil = new Perfiltrabajo();
                        perfil.id = array_usuario.getInt("id");
                        perfil.id_tipo_trabajo = array_usuario.getInt("id_tipo_trabajo");
                        perfil.id_usuario = array_usuario.getInt("id_usuario");
                        perfil.puntaje_promedio = array_usuario.getInt("puntaje_promedio");
                        perfil.habilitado = array_usuario.getBoolean("habilitado");

                        singleton.getInstance().agregar_mi_perfil_a_lista(perfil);
                    }

                    NetworkOperationAsyncTask_lista_de_mis_postulaciones exe = new NetworkOperationAsyncTask_lista_de_mis_postulaciones();
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

    private class NetworkOperationAsyncTask_lista_de_mis_postulaciones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenermispeticionesasolicitudes.json").build();
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
                        solicitud.nombre_trabajo = jobPostJSON1.getString("nombre_trabajo");
                        singleton.getInstance().agregar_a_mis_postulaciones(solicitud);
                    }
                    NetworkOperationAsyncTask_buscar_mis_solicitudes exe = new NetworkOperationAsyncTask_buscar_mis_solicitudes();
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
                        JSONObject array_trabajo = array.getJSONObject(i);

                        Trabajo trabajo = new Trabajo();
                        trabajo.id = array_trabajo.getInt("id");
                        trabajo.titulo = array_trabajo.getString("titulo");
                        trabajo.descripcion = array_trabajo.getString("descripcion");
                        trabajo.latitud = array_trabajo.getDouble("coordenadax");
                        trabajo.longitud = array_trabajo.getDouble("coordenaday");
                        trabajo.nombre_solicitante = array_trabajo.getString("nombre_solicitante");
                        trabajo.fecha_de_requerimiento = array_trabajo.getString("fecha_de_requerimiento") ;
                        trabajo.id_tipo_trabajo = array_trabajo.getInt("id_tipo_trabajo");
                        trabajo.id_solicitante = array_trabajo.getInt("id_solicitante");
                        trabajo.activo = array_trabajo.getBoolean("activo");
                        trabajo.url_foto = array_trabajo.getString("url_foto");

                        singleton.getInstance().agregar_trabajo_a_lista(trabajo);
                    }
                    if(singleton.getInstance().lista_tipos_de_trabajo.size() == 0) {
                        NetworkOperationAsyncTask_lista_de_tipos_de_trabajo exe = new NetworkOperationAsyncTask_lista_de_tipos_de_trabajo();
                        exe.execute();
                    }
                    else
                    {
                        ir_a_inicio_logeado();
                    }
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

    private class NetworkOperationAsyncTask_lista_de_tipos_de_trabajo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenertiposdetrabajo.json").build();
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
                //json.put("id_usuario", prefs.getString("id", "0"));
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
                        JSONObject array_tipo_trabajo = array.getJSONObject(i);

                        Tipotrabajo tipo = new Tipotrabajo();
                        tipo.nombre = array_tipo_trabajo.getString("nombre");
                        tipo.id = array_tipo_trabajo.getInt("id");
                        singleton.getInstance().agregar_tipo_de_trabajo_a_lista(tipo);
                    }
                    ir_a_inicio_logeado();

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


}
