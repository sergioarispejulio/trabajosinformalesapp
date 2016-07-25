package com.example.sergioarispejulio.proyectogrado;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.sergioarispejulio.proyectogrado.clases.Usuario;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {

    ProgressDialog progress;
    EditText nombre,apellido,ci,telefono;
    String contenedor_nombre,contenedor_apellido,contenedor_ci,contenedor_telefono,contenedor_email,contenedor_ciudad;
    boolean se_conecto;
    DatePicker fecha_nacimiento;
    String tipo_error;
    Spinner ciudades;
    String url_imagen;
    boolean selecciono_imagen;
    ImageView imageView;

    public Uri filePath_ori;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url_imagen = "";
        selecciono_imagen = false;


        telefono = (EditText) findViewById(R.id.registrarse_telefono_edit_profile);
        ciudades = (Spinner) findViewById(R.id.spinner_edit_profile);
        imageView = (ImageView) findViewById(R.id.imageView7);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.boton_flotante_edit_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contenedor_telefono = telefono.getText().toString();
                contenedor_ciudad = ciudades.getSelectedItem().toString();
                actualizarusuario();
            }
        });
        rellenar_informacion();
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
        Usuario actu = singleton.getInstance().usuario_actual;
        telefono.setText(Integer.toString(actu.telefono));
        ciudades.setSelection(posicion_ciudad(actu.ciudad));

    }

    private int posicion_ciudad(String nombre)
    {
        int posi = 0;
        if(nombre.equals("Cochabamba"))
            posi = 0;
        if(nombre.equals("La Paz"))
            posi = 1;
        if(nombre.equals("Santa Cruz"))
            posi = 2;
        if(nombre.equals("Oruro"))
            posi = 3;
        if(nombre.equals("Potosi"))
            posi = 4;
        if(nombre.equals("Chuquisaca"))
            posi = 5;
        if(nombre.equals("Tarija"))
            posi = 6;
        if(nombre.equals("Beni"))
            posi = 7;
        if(nombre.equals("Pando"))
            posi = 8;
        return posi;
    }

    public void actualizarusuario()
    {
        if(verificar_ingreso_informacion()) {
            if (verificar_telefono(contenedor_telefono) == true) {
                progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
                if(selecciono_imagen == true)
                {
                    subirimagen subir = new subirimagen();
                    subir.execute();
                }
                else
                {
                    NetworkOperationAsyncTask_actualizarusuario exe = new NetworkOperationAsyncTask_actualizarusuario();
                    exe.execute();
                }
            }
            else
            {
                mostrar_mensaje_de_error("Error en el telefono", "Escriba un numero de telefono válido");

            }
        }
        else
        {
            mostrar_mensaje_de_error("Falta información", "Complete todos los campos de información");
        }
    }


    public void seleccionar_imagen(View v) {
        selecciono_imagen = false;
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            filePath_ori = data.getData();

            try {
                //Getting the Bitmap from Gallery
                selecciono_imagen = true;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }





    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
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

        return resu;
    }

    public String convertir_fecha()
    {
        String resu = "";
        resu = fecha_nacimiento.getYear() + "/" + (fecha_nacimiento.getMonth()+1) + "/" + fecha_nacimiento.getDayOfMonth();
        return resu;
    }

    private void actualizar_info()
    {
        Usuario actu = singleton.getInstance().usuario_actual;
        actu.telefono = Integer.parseInt(contenedor_telefono);
        actu.url_imagen = url_imagen;
        actu.ciudad = contenedor_ciudad;

    }

    public void resultado(boolean estado, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(estado == true)
        {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se actualizo su perfil exitosamente");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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


    private class subirimagen extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(filePath_ori);
                    Map config = new HashMap();
                    config.put("cloud_name", "dg4qh9bhi");
                    config.put("api_key", "922475895991472");
                    config.put("api_secret", "N7kgo_KChtYaVPDdxdzn28NXmDQ");
                    Cloudinary cloudinary = new Cloudinary(config);
                    String[] a =  getRealPathFromURI(filePath_ori).split("/");
                    String aux = cloudinary.uploader().upload(inputStream, ObjectUtils.asMap("public_id", a[a.length - 1])).toString();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println(aux);
                    url_imagen = aux.split(",")[7].replace(" url=","");

                } catch (FileNotFoundException e1) {
                    System.out.println("+++++++++++++++++++++++++++++++++++");
                    System.out.println(e1);
                    e1.printStackTrace();
                }

                System.out.println("Entro");
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            NetworkOperationAsyncTask_actualizarusuario exe = new NetworkOperationAsyncTask_actualizarusuario();
            exe.execute();
        }
    }

    private class NetworkOperationAsyncTask_actualizarusuario extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("editarusuario.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();

                json.put("ciudad", contenedor_ciudad);
                json.put("telefono", contenedor_telefono);
                json.put("url_foto", url_imagen);
                json.put("id", singleton.getInstance().usuario_actual.id);

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
                progress.dismiss();
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        actualizar_info();
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
                resultado(false, ex.toString());
            }
        }
    }

}
