package com.example.sergioarispejulio.proyectogrado;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

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


public class information_work extends Fragment {


    ProgressDialog progress;
    TextView titulo,descripcion,solicitante,tipo,titulosolicitante,precio_label;
    EditText precio;
    ImageView foto;
    Button boton;
    boolean se_conecto;
    String tipo_error;
    Integer precio_postulacion;

    Trabajo elemento;

    public information_work() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_information_work, container, false);
        titulo = (TextView) v.findViewById(R.id.textView23);
        tipo = (TextView) v.findViewById(R.id.textView48);
        descripcion = (TextView) v.findViewById(R.id.textView28);
        solicitante = (TextView) v.findViewById(R.id.textView29);
        titulosolicitante = (TextView) v.findViewById(R.id.textView26);
        boton = (Button) v.findViewById(R.id.button9);
        foto = (ImageView) v.findViewById(R.id.imageView10);
        precio = (EditText) v.findViewById(R.id.editText4);
        precio_label = (TextView) v.findViewById(R.id.textView7);
        rellenarinformacion();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postularse();
            }
        });

        return v;
    }

    public void rellenarinformacion()
    {
        elemento = singleton.getInstance().trabajo_seleccionado;
        if(singleton.getInstance().para_postularse == false) {
            solicitante.setVisibility(View.GONE);
            titulosolicitante.setVisibility(View.GONE);
            boton.setVisibility(View.GONE);
            precio.setVisibility(View.GONE);
            precio_label.setVisibility(View.GONE);
        }
        solicitante.setText(elemento.nombre_solicitante);
        titulo.setText(elemento.titulo);
        descripcion.setText(elemento.descripcion);

        if (elemento.url_foto.equals("") == false)
        {
            Picasso.with(getActivity().getApplicationContext()).load(elemento.url_foto).resize(200, 150).into(foto);
        }
        tipo.setText(singleton.getInstance().nombre_tipo_de_trabajo);
    }


    public void postularse()
    {
        if(precio.getText().toString().equals("") == false) {
            precio_postulacion = Integer.parseInt(precio.getText().toString());
            progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
            NetworkOperationAsyncTask_mandar_postulacion conector = new NetworkOperationAsyncTask_mandar_postulacion();
            conector.execute();
        }
        else {
            mostrar_mensaje_de_error("Ponga precio", "Necesita poner precio a su trabajo");
        }
    }

    public void resultado(boolean estado, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (estado == true) {
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se postulo al trabajo");
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



    private class NetworkOperationAsyncTask_mandar_postulacion extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("mandarpeticiondesolicituddetrabajo.json").build();
            try {

                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_usuario", singleton.getInstance().usuario_actual.id);
                json.put("precio_medio", precio_postulacion);
                json.put("id_trabajo", elemento.id);
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
                    System.out.println(urlConnection.getResponseCode());
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
