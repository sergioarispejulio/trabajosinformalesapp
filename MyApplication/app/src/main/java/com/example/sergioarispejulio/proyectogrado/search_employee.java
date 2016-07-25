package com.example.sergioarispejulio.proyectogrado;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sergioarispejulio.proyectogrado.clases.Comentario;
import com.example.sergioarispejulio.proyectogrado.clases.CustomGrid;
import com.example.sergioarispejulio.proyectogrado.clases.ImageAdapter;
import com.example.sergioarispejulio.proyectogrado.clases.Perfil;
import com.example.sergioarispejulio.proyectogrado.clases.Tipotrabajo;
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


public class search_employee extends Fragment {

    ProgressDialog progress;

    ListView listView;
    boolean se_conecto;
    String tipo_error;
    int posicion;

    GridView grid;

    public search_employee() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_employee, container, false);

        /*Integer[] imageId = new Integer[singleton.getInstance().lista_tipos_de_trabajo.size()];
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(singleton.getInstance().lista_tipos_de_trabajo.size());
        for(int i = 0; i < singleton.getInstance().lista_tipos_de_trabajo.size(); i++)
        {
            imageId[i] = devolver_identificador_imagen(singleton.getInstance().lista_tipos_de_trabajo.get(i));
        }
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000");


        GridView gridview = (GridView) v.findViewById(R.id.gridview1);
        gridview.setAdapter(new ImageAdapter(getActivity(), imageId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == 0) {
                    singleton.getInstance().id_trabajo = 1;
                    buscar_trabajadores();
                }
                if (position == 1) {
                    singleton.getInstance().id_trabajo = 2;
                    buscar_trabajadores();
                }
                if (position == 2) {
                    singleton.getInstance().id_trabajo = 3;
                    buscar_trabajadores();
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
        CustomGrid adapter = new CustomGrid(getActivity(), web, imageId);
        grid=(GridView) v.findViewById(R.id.gridview1);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().id_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).id;
                singleton.getInstance().nombre_tipo_de_trabajo = singleton.getInstance().lista_tipos_de_trabajo.get(position).nombre;
                buscar_trabajadores();

            }
        });

        return v;
    }

    public int devolver_identificador_imagen(Tipotrabajo elemento)
    {
        int drawableResourceId = this.getResources().getIdentifier(elemento.nombre, "drawable", getActivity().getPackageName());
        System.out.println(drawableResourceId);
        if(drawableResourceId != 0)
            return drawableResourceId;
        else
            return R.drawable.no_existe;
    }


    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }

    public void entrar()
    {

        singleton.getInstance().para_postularse = false;
        singleton.getInstance().para_aceptar_postulacion = false;
        singleton.getInstance().trabajador_aceptado = false;

        Intent x = new Intent(getActivity(), view_profile_workerV2.class);
        startActivityForResult(x, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            //
        }
    }


    public void actualizar_listwiev()
    {

        /*String[] values = singleton.getInstance().devolver_lista_de_trabajadores();
        System.out.println(singleton.getInstance().lista_trabajadores.size());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                posicion = position;
                progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);
                NetworkOperationAsyncTask_buscar_trabajador exe = new NetworkOperationAsyncTask_buscar_trabajador();
                exe.execute();
            }

        });*/

        Intent x = new Intent(getActivity(), result_search_employee.class);
        startActivityForResult(x, 2);

    }

    public void buscar_trabajadores()
    {
        progress = ProgressDialog.show(getActivity(), "", "Enviando información, espere", true);

        NetworkOperationAsyncTask_buscar_trabajadores exe = new NetworkOperationAsyncTask_buscar_trabajadores();
        exe.execute();
    }


    private class NetworkOperationAsyncTask_buscar_trabajadores extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenerlistatrabajadoresportipotrabajo.json").build();
            try {
                SharedPreferences prefs = getActivity().getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                singleton.getInstance().resetear_lista_trabajadores();

                JSONObject json = new JSONObject();
                json.put("tipo_trabajo", singleton.getInstance().id_trabajo);
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
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject array_usuario = array.getJSONObject(i);

                        Usuario usuario = new Usuario();
                        usuario.id = array_usuario.getInt("id");
                        usuario.nombre = array_usuario.getString("nombre");
                        usuario.apellido = array_usuario.getString("apellido");
                        usuario.ci = array_usuario.getInt("ci");
                        usuario.ciudad = array_usuario.getString("ciudad");
                        usuario.telefono = array_usuario.getInt("telefono");
                        usuario.url_imagen = array_usuario.getString("url_foto");

                        singleton.getInstance().agregar_trabajador(usuario);
                    }
                    progress.dismiss();
                    actualizar_listwiev();
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

    private class NetworkOperationAsyncTask_buscar_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenerinfotrabajadordeterminado.json").build();
            try {
                SharedPreferences prefs = getActivity().getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
                json.put("id_trabajador", singleton.getInstance().lista_trabajadores.get(posicion).id);
                json.put("id_tipo_trabajo", singleton.getInstance().id_trabajo);
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
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject jobPostJSON1 = array.getJSONObject(i);
                        JSONObject array_usuario = jobPostJSON1.getJSONObject("usuario");
                        JSONObject array_perfil = jobPostJSON1.getJSONObject("perfil");


                        Usuario usuario = new Usuario();
                        usuario.id = array_usuario.getInt("id");
                        usuario.nombre = array_usuario.getString("nombre");
                        usuario.apellido = array_usuario.getString("apellido");
                        usuario.ci = array_usuario.getInt("ci");
                        usuario.ciudad = array_usuario.getString("ciudad");
                        usuario.telefono = array_usuario.getInt("telefono");
                        usuario.url_imagen = array_usuario.getString("url_foto");

                        Perfil perfil = new Perfil();
                        perfil.id = array_perfil.getInt("id");
                        perfil.id_trabajo = array_perfil.getInt("id_usuario");
                        perfil.puntaje_promedio = array_perfil.getInt("puntaje_promedio");
                        perfil.habilitado = array_perfil.getBoolean("habilitado");

                        System.out.println(jobPostJSON1);
                        usuario.perfil_trabajo_actual = perfil;
                        singleton.getInstance().trabajador_seleccionado = usuario;
                    }

                    NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador exe = new NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador();
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

    private class NetworkOperationAsyncTask_buscar_comentarios_perfil_trabajador extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("obtenercomentariostrabajador.json").build();
            try {
                SharedPreferences prefs = getActivity().getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                URL url = new URL(buildUri.toString()); // Create a new URL
                System.out.println(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject json = new JSONObject();
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
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject jobPostJSON1 = array.getJSONObject(i);

                        Comentario comentario = new Comentario();
                        comentario.id = jobPostJSON1.getInt("id");
                        comentario.comentario = jobPostJSON1.getString("comentario");
                        comentario.id_usuario_que_comento = jobPostJSON1.getInt("id_usuario_que_comenta");
                        comentario.nombre_usuario_comento = jobPostJSON1.getString("nombre_usuario_que_comenta");
                        singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.lista_comentarios.add(comentario);
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
                System.out.println(ex);
            }
        }
    }



}
