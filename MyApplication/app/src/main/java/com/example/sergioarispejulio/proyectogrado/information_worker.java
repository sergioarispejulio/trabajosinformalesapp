package com.example.sergioarispejulio.proyectogrado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Usuario;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
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


public class information_worker extends Fragment {

    ProgressDialog progress;

    TextView nombre,ciudad,telefono,ci;
    ImageView foto;
    RatingBar ratingBar;
    int puntaje;
    boolean se_conecto;
    String tipo_error;
    boolean acceder;
    Button aceptar_postulante;
    Solicitud soli;

    public information_worker() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        acceder = true;


        View v = inflater.inflate(R.layout.fragment_information_worker, container, false);

        nombre = (TextView) v.findViewById(R.id.textView10);
        ciudad = (TextView) v.findViewById(R.id.textView12);
        telefono = (TextView) v.findViewById(R.id.textView15);
        ci = (TextView) v.findViewById(R.id.textView17);
        foto = (ImageView) v.findViewById(R.id.imageView5);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        aceptar_postulante = (Button) v.findViewById(R.id.button27);
        aceptar_postulante.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aceptar();
            }
        });

        actualizar();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (acceder == false) {
                    puntaje = (int) rating;
                    NetworkOperationAsyncTask_puntuar_trabajador exe = new NetworkOperationAsyncTask_puntuar_trabajador();
                    exe.execute();
                } else {
                    System.out.println("Entro!!!!!");
                    acceder = false;
                }


            }
        });

        return v;
    }

    public void actualizar() {
        Usuario seleccionado = singleton.getInstance().trabajador_seleccionado;
        nombre.setText(seleccionado.devolver_nombre_completo());
        ciudad.setText(seleccionado.ciudad);
        ci.setText(Integer.toString(seleccionado.ci));
        telefono.setText(Integer.toString(seleccionado.telefono));
        if(seleccionado.perfil_trabajo_actual.puntaje_promedio == 0)
        {
            acceder = false;
        }
        else
        {
            ratingBar.setRating(seleccionado.perfil_trabajo_actual.puntaje_promedio);
        }
        if (seleccionado.url_imagen.equals("") == false)
        {
            //Picasso.with(information_worker.this.getApplicationContext()).load(seleccionado.url_imagen).resize(200, 150).into(foto);
            Picasso.with(getActivity().getApplicationContext()).load(seleccionado.url_imagen).resize(200, 150).into(foto);
        }
        if(singleton.getInstance().trabajador_aceptado == false)
            ratingBar.setEnabled(false);
        if(singleton.getInstance().para_aceptar_postulacion == false)
            aceptar_postulante.setVisibility(View.GONE);


    }

    public void aceptar()
    {
        soli = singleton.getInstance().solicitud_seleccionada;
        progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_mandar_solicitud exe = new NetworkOperationAsyncTask_mandar_solicitud();
        exe.execute();
    }





    public void resultado(boolean estado, String status) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if (estado == true) {
            singleton.getInstance().marcar_solicitud_de_trabajo_como_aceptada();
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Se acepto al trabajor en su solicitud");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().setResult(2);
                    getActivity().finish();
                }
            });

            android.app.AlertDialog alert = builder.create();
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
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void resultado_puntuar(boolean estado, String status) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if (estado == true) {
            singleton.getInstance().marcar_solicitud_de_trabajo_como_aceptada();
            builder.setCancelable(true);
            builder.setTitle("Exito");
            builder.setMessage("Acabo de dar puntuación a este trabajador");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            android.app.AlertDialog alert = builder.create();
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
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        android.app.AlertDialog alertDialog;
        alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }


    private class NetworkOperationAsyncTask_puntuar_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("darpuntajeatrabajador.json").build();
            try {

                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("puntaje", puntaje);
                json.put("id_usuario_que_puntua", singleton.getInstance().usuario_actual.id );
                json.put("id_usuario_que_recibe_puntuacion", singleton.getInstance().trabajador_seleccionado.id);
                json.put("id_perfil_trabajador", singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.id);
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
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    System.out.println(jobPostJSON.getString("status"));
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.puntaje_promedio = jobPostJSON.getInt("puntaje_promedio");
                        resultado_puntuar(true, jobPostJSON.getString("status"));
                    } else {
                        resultado_puntuar(false, jobPostJSON.getString("status"));
                    }
                }
                else
                {
                    mostrar_mensaje_de_error("Error", tipo_error);
                }

            } catch (JSONException ex) {

            }
        }
    }

    private class NetworkOperationAsyncTask_mandar_solicitud extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("aceptarorechazarpeticiondesolicituddetrabajo.json").build();
            try {

                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_solicitud", soli.id);
                json.put("resultado", true);
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
                progress.dismiss();
                if(se_conecto == true) {
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON);
                    System.out.println(jobPostJSON.getString("status"));
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        singleton.getInstance().quitar_activo_de_un_trabajo();
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
