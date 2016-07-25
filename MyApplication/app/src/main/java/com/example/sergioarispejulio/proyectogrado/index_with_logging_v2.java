package com.example.sergioarispejulio.proyectogrado;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergioarispejulio.proyectogrado.clases.Perfiltrabajo;
import com.example.sergioarispejulio.proyectogrado.clases.Solicitud;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
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
import java.util.ArrayList;
import java.util.List;

public class index_with_logging_v2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progress;

    TextView nombre;
    ImageView foto;

    boolean se_conecto;
    String tipo_error;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_with_logging_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //nombre = (TextView) findViewById(R.id.textview_opciones);
        //foto = (ImageView) findViewById(R.id.imageView_opciones);

        //cargar_datos();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager_index_logging);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_index_logging);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void cargar_datos()
    {
        nombre.setText(singleton.getInstance().usuario_actual.devolver_nombre_completo());
        if (singleton.getInstance().usuario_actual.url_imagen.equals("") == false)
        {
            //Picasso.with(information_worker.this.getApplicationContext()).load(seleccionado.url_imagen).resize(200, 150).into(foto);
            Picasso.with(this.getApplicationContext()).load(singleton.getInstance().usuario_actual.url_imagen).resize(200, 150).into(foto);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.index_with_logging_v2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_editar_perfil) {
            Intent intent = new Intent(getBaseContext(), edit_profile.class);
            startActivity(intent);
        } else if (id == R.id.menu_perfiles) {
            Intent intent = new Intent(getBaseContext(), update_type_of_job.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            desconectarse();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new client_fragment(), "Cliente");
        adapter.addFragment(new employee_fragment(), "Trabajador");
        adapter.addFragment(new search_employee(), "Buscar Trabajadores");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    public void desconectarse()
    {
        singleton.getInstance().desconectado = true;
        progress = ProgressDialog.show(this, "", "Enviando información, espere", true);
        NetworkOperationAsyncTask_desconectarse verificador = new NetworkOperationAsyncTask_desconectarse();
        verificador.execute();
    }

    private void mostrar_mensaje_de_error(String titulo, String contenido)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(contenido);
        alertDialog.show();
    }




    private class NetworkOperationAsyncTask_desconectarse extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse(singleton.getInstance().direccion_a_conectarse).buildUpon() // Build the URL using the Uri class
                    .appendPath("cerrarseccion.json").build();
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
                json.put("token", prefs.getString("token", ""));
                json.put("id_usuario", prefs.getString("id", "0"));
                json.put("modelo", Build.MANUFACTURER + " " + Build.MODEL);
                json.put("so", android.os.Build.VERSION.SDK_INT);

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
                    progress.dismiss();
                    SharedPreferences prefs = getSharedPreferences(singleton.getInstance().nombre_para_archivo_preferencias, Context.MODE_PRIVATE);
                    JSONArray array = new JSONArray(json);
                    System.out.println(array);
                    JSONObject jobPostJSON = array.getJSONObject(0);
                    System.out.println(jobPostJSON.getString("status"));
                    if (jobPostJSON.getString("status").contentEquals("OK") == true) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", "");
                        editor.putString("token", "");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(), index_login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    } else {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", "");
                        editor.putString("token", "");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(), index_login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }
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
