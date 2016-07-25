package com.example.sergioarispejulio.proyectogrado;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sergioarispejulio.proyectogrado.clases.singleton;

public class result_search_workV2 extends AppCompatActivity {


    ListView listView;
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search_work_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView_search_work);
        String[] values = singleton.getInstance().devolver_lista_de_titulos_trabajos_disponibles();



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                singleton.getInstance().posiciontrabajo = position;
                posicion = position;
                ir_al_resultado();
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



    private void ir_al_resultado()
    {
        singleton.getInstance().trabajo_seleccionado = singleton.getInstance().lista_trabajos_disponibles.get(posicion);
        singleton.getInstance().para_postularse = true;
        singleton.getInstance().soy_creador = false;
        Intent x = new Intent(this, view_workV2.class);
        startActivity(x);
    }
}
