package com.example.sergioarispejulio.proyectogrado;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sergioarispejulio.proyectogrado.clases.Perfiltrabajo;
import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
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
import java.util.Calendar;

public class register extends AppCompatActivity {

    ProgressDialog progress;

    EditText nombre,apellido,ci,contrasena,repetir_contrasena,telefono,nickname;
    String contenedor_nombre,contenedor_apellido,contenedor_ci,contenedor_telefono,contenedor_email,contenedor_contrasena,contenedor_repetir_contrasena,contenedor_ciudad, contenedor_nickname;
    boolean se_conecto;
    DatePicker fecha_nacimiento;
    String tipo_error,aux_error;
    Spinner ciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nombre = (EditText) findViewById(R.id.registrarse_nombre);
        apellido = (EditText) findViewById(R.id.registrarse_apellido);
        telefono = (EditText) findViewById(R.id.registrarse_telefono);
        nickname = (EditText) findViewById(R.id.registrarse_nickname);
        ci = (EditText) findViewById(R.id.registrarse_ci);
        contrasena = (EditText) findViewById(R.id.registrarse_contrasena);
        repetir_contrasena = (EditText) findViewById(R.id.registrarse_repetircontrasena);
        ciudades = (Spinner) findViewById(R.id.spinner);
        fecha_nacimiento = (DatePicker) findViewById(R.id.datePicker);
        rellenar_informacion();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.boton_flotante_registrarse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.getInstance().resetear_lista_postulaciones();
                singleton.getInstance().resetear_lista_trabajos();
                singleton.getInstance().resetear_lista_de_mis_postulacionesv2();
                contenedor_contrasena = contrasena.getText().toString();
                contenedor_repetir_contrasena = repetir_contrasena.getText().toString();
                contenedor_telefono = telefono.getText().toString();
                contenedor_nickname = nickname.getText().toString();
                contenedor_nombre = nombre.getText().toString();
                contenedor_apellido = apellido.getText().toString();
                contenedor_ci = ci.getText().toString();
                contenedor_email = "";
                contenedor_ciudad = ciudades.getSelectedItem().toString();
                crearusuario();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void rellenar_informacion()
    {
        String[] lista_ciudades = {"Cochabamba","La Paz","Santa Cruz", "Oruro", "Potosi", "Chuquisaca", "Tarija", "Beni", "Pando"};
        ArrayAdapter<String> stringArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, lista_ciudades);
        ciudades.setAdapter(stringArray);
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.MILLISECOND, minCalendar.MILLISECOND - 1000);
        fecha_nacimiento.setMaxDate(minCalendar.getTimeInMillis());
    }

    public void crearusuario()
    {
        if(verificar_ingreso_informacion()) {
            if (contenedor_contrasena.contentEquals(contenedor_repetir_contrasena) == true) {
                if (verificar_contrasena(contenedor_contrasena) == true) {
                    if (verificar_telefono(contenedor_telefono) == true) {
                        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
                        NetworkOperationAsyncTask_crearusuario acceso = new NetworkOperationAsyncTask_crearusuario();
                        acceso.execute();
                    }
                    else
                    {
                        mostrar_mensaje_de_error("Error en el telefono", "Escriba un numero de telefono válido");
                    }
                }
                else
                {
                    mostrar_mensaje_de_error("Error con la contraseña", "Las contraseñas tiene que tener 6 caracteres como mínimo");
                }
            }
            else
            {
                mostrar_mensaje_de_error("Error con las contraseña", "Las contraseñas que escribiste no coinciden");

            }
        }
        else
        {
            mostrar_mensaje_de_error("Falta información", "Complete todos los campos de información");
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

    private boolean verificar_contrasena(String contrasena)
    {
        boolean resu = true;
        if(contrasena.length() < 6)
            resu = false;
        return resu;
    }

    private boolean verificar_telefono(String telefono)
    {
        boolean resu = true;
        if(telefono.length() < 8)
            resu = false;
        else
        {

            if (telefono.substring(0, 1).contains("6") == false)
                if (telefono.substring(0, 1).contains("7") == false)
                    resu = false;
        }
        return resu;
    }


    private boolean verificar_ingreso_informacion()
    {
        boolean resu = true;
        if(contenedor_nombre.contentEquals(""))
            resu = false;
        if(contenedor_apellido.contentEquals(""))
            resu = false;
        if(contenedor_ci.contentEquals(""))
            resu = false;
        return resu;
    }

    public String convertir_fecha()
    {
        String resu = "";
        resu = fecha_nacimiento.getYear() + "/" + (fecha_nacimiento.getMonth()+1) + "/" + fecha_nacimiento.getDayOfMonth();
        return resu;
    }

    public void ir_a_inicio_logeado()
    {
        this.setResult(2);
        this.finish();
    }


    public void resultado(boolean estado, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(estado == true)
        {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se creo exitosamente su cuenta");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Iniciar sección", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ir_a_inicio_logeado();
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



    private class NetworkOperationAsyncTask_crearusuario extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("crearusuario.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("email", contenedor_email);
                json.put("password", contenedor_contrasena);
                json.put("password_confirmation", contenedor_contrasena);
                json.put("modelo", Build.MANUFACTURER + " " + Build.MODEL);
                json.put("so", android.os.Build.VERSION.SDK_INT);
                json.put("nombre", contenedor_nombre);
                json.put("apellido", contenedor_apellido);
                json.put("ciudad", contenedor_ciudad);
                json.put("ci", contenedor_ci);
                json.put("fecha_nacimiento", convertir_fecha());
                json.put("nickname", contenedor_nickname);
                json.put("telefono", contenedor_telefono);

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
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        NetworkOperationAsyncTask_iniciarseccion exe = new NetworkOperationAsyncTask_iniciarseccion();
                        exe.execute();
                    } else {
                        progress.dismiss();
                        resultado(false, jobPostJSON.getString("status"));
                    }
                }
                else
                {
                    mostrar_mensaje_de_error("Error", tipo_error);
                }
            } catch (JSONException ex) {
                progress.dismiss();
                resultado(false, ex.toString());
            }
        }
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
                json.put("nickname", contenedor_nickname);
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

                        mostrar_mensaje_de_error("Error al acceder", "Correo electronico o contraseña incorrectos");
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
                    progress.dismiss();
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
                        solicitud.precio_medio = jobPostJSON1.getInt("precio_medio");
                        singleton.getInstance().agregar_a_mis_postulaciones(solicitud);
                    }
                    NetworkOperationAsyncTask_buscar_mis_solicitudes exe = new NetworkOperationAsyncTask_buscar_mis_solicitudes();
                    exe.execute();
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
                    progress.dismiss();
                    ir_a_inicio_logeado();
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
