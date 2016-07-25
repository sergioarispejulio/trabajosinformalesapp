package com.example.sergioarispejulio.proyectogrado;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class data_form_create_request_fragment extends Fragment {

    ProgressDialog progress;
    EditText titulo,descripcion;
    TextView inicio;
    String contenedor_titulo,contenedor_descripcion;
    boolean se_conecto;
    DatePicker fecha_solicitud;
    String tipo_error;
    Trabajo nuevo_trabajo;
    Button boton;

    String url_foto;
    boolean selecciono_imagen;
    ImageView imageView;
    public Uri filePath_ori;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    double longitud; //y
    double latitud; //x
    public boolean presiono;

    public data_form_create_request_fragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_data_form_create_request_fragment, container, false);


        presiono = false;

        nuevo_trabajo = new Trabajo();
        selecciono_imagen = false;
        url_foto = "";

        titulo = (EditText)  v.findViewById(R.id.crear_solicitud_trabajo_titulo);
        descripcion = (EditText) v.findViewById(R.id.crear_solicitud_trabajo_descripcion);
        imageView = (ImageView) v.findViewById(R.id.imageView9);
        inicio = (TextView) v.findViewById(R.id.textView13);


        fecha_solicitud = (DatePicker) v.findViewById(R.id.datePicker1);

        boton = (Button) v.findViewById(R.id.button26);
        boton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seleccionar_imagen();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.boton_flotante_fragemto_crear_solicitud_de_trabajador);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrar_oferta();
            }
        });

        cargardatos();

        return v;
    }

    public void seleccionar_imagen() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            filePath_ori = data.getData();

            try {
                //Getting the Bitmap from Gallery
                selecciono_imagen = true;
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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

    public void cargardatos()
    {
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.MILLISECOND, minCalendar.MILLISECOND - 1000);
        fecha_solicitud.setMinDate(minCalendar.getTimeInMillis());
        inicio.setText("Crear solicitud para " + singleton.getInstance().nombre_tipo_de_trabajo);
    }




    public void registrar_oferta()
    {
        presiono = singleton.getInstance().presiono;
        if(verificar_ingreso_informacion() == true) {
            contenedor_descripcion = descripcion.getText().toString();
            contenedor_titulo = titulo.getText().toString();
            progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
            latitud = singleton.getInstance().latitud;
            longitud = singleton.getInstance().longitud;
            if(selecciono_imagen == true)
            {
                subirimagen subir = new subirimagen();
                subir.execute();
            }
            else
            {
                NetworkOperationAsyncTask_creartrabajo exe = new NetworkOperationAsyncTask_creartrabajo();
                exe.execute();
            }
        }
        else
        {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Falta información");
            alertDialog.setMessage("Complete todos los campos de información");
            alertDialog.show();
        }
    }


    private boolean verificar_ingreso_informacion()
    {
        boolean resu = true;
        if(titulo.getText().toString().contentEquals(""))
            resu = false;
        if(descripcion.getText().toString().contentEquals(""))
            resu = false;
        if(presiono == false)
            resu = false;
        return resu;
    }

    public String convertir_fecha()
    {
        String resu = "";
        resu = fecha_solicitud.getYear() + "/" + (fecha_solicitud.getMonth()+1) + "/" + fecha_solicitud.getDayOfMonth();
        return resu;
    }


    public void resultado(boolean estado, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (estado == true) {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se publico el trabajo exitosamente");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().setResult(2);
                    getActivity().finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
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
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    private class subirimagen extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                InputStream inputStream;
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(filePath_ori);
                    Map config = new HashMap();
                    config.put("cloud_name", "dg4qh9bhi");
                    config.put("api_key", "922475895991472");
                    config.put("api_secret", "N7kgo_KChtYaVPDdxdzn28NXmDQ");
                    Cloudinary cloudinary = new Cloudinary(config);
                    String[] a =  getRealPathFromURI(filePath_ori).split("/");
                    String aux = cloudinary.uploader().upload(inputStream, ObjectUtils.asMap("public_id", a[a.length - 1])).toString();
                    url_foto = aux.split(",")[7].replace(" url=","");

                } catch (FileNotFoundException e1) {
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
            NetworkOperationAsyncTask_creartrabajo exe = new NetworkOperationAsyncTask_creartrabajo();
            exe.execute();
        }
    }

    private class NetworkOperationAsyncTask_creartrabajo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("creartrabajo.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();


                JSONObject json = new JSONObject();
                json.put("titulo", contenedor_titulo);
                json.put("descripcion", contenedor_descripcion);
                json.put("coordenadax", latitud);
                json.put("coordenaday", longitud);
                json.put("fecha_de_requerimiento", convertir_fecha());
                json.put("id_tipo_trabajo", singleton.getInstance().id_trabajo);
                json.put("id_solicitante", singleton.getInstance().usuario_actual.id);
                json.put("url_foto", url_foto);
                json.put("ciudad", singleton.getInstance().usuario_actual.ciudad);

                nuevo_trabajo.titulo = contenedor_titulo;
                nuevo_trabajo.descripcion = contenedor_descripcion;
                nuevo_trabajo.fecha_de_requerimiento = convertir_fecha();
                nuevo_trabajo.id_tipo_trabajo = singleton.getInstance().id_trabajo;
                nuevo_trabajo.url_foto = url_foto;
                nuevo_trabajo.latitud = latitud;
                nuevo_trabajo.longitud = longitud;

                DataOutputStream output = new DataOutputStream(urlConnection.getOutputStream());

                output.write(json.toString().getBytes());
                output.flush();
                StringBuffer buffer = new StringBuffer();

                se_conecto = true;

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
                progress.dismiss();
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        singleton.getInstance().agregar_trabajo_a_lista(nuevo_trabajo);
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
