package com.example.sergioarispejulio.proyectogrado;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sergioarispejulio.proyectogrado.clases.Usuario;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
import com.squareup.picasso.Picasso;


public class list_comments_worker extends Fragment {

    ListView listView;
    Button registrar;

    public list_comments_worker() {
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
        View v =inflater.inflate(R.layout.fragment_list_comments_worker, container, false);

        registrar = (Button) v.findViewById(R.id.button19);
        listView = (ListView) v.findViewById(R.id.listView_view_worker);

        String[] values = singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.devolver_lista_de_comentarios();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentar_trabajador();
            }
        });

        actualizar();
        return v;
    }

    public void actualizar() {

        if(singleton.getInstance().trabajador_aceptado == false)
            registrar.setVisibility(View.GONE);

    }


    public void actualizar_listwiev()
    {
        String[] values = singleton.getInstance().trabajador_seleccionado.perfil_trabajo_actual.devolver_lista_de_comentarios();



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //
            }

        });
    }

    public void comentar_trabajador()
    {
        Intent x = new Intent(getActivity(), comment_worker.class);
        startActivityForResult(x, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            actualizar_listwiev();
        }
    }


}
