package com.example.sergioarispejulio.proyectogrado.clases;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.sergioarispejulio.proyectogrado.R;

import java.util.ArrayList;

/**
 * Created by Sergio Arispe Julio on 5/16/2016.
 */
public class ImageAdapter_search_work extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mThumbIds;

    public ImageAdapter_search_work (Context c) {
        mContext = c;
        cargar_datos();
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(170, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }

    // references to our images
    private void cargar_datos()
    {
        ArrayList<Perfiltrabajo> lista = singleton.getInstance().lista_mis_perfiles;
        if(lista.get(0).habilitado == true)
            mThumbIds.add(R.mipmap.albanil);
        if(lista.get(1).habilitado == true)
            mThumbIds.add(R.mipmap.plomero);
        if(lista.get(2).habilitado == true)
            mThumbIds.add(R.mipmap.jardinero);
    }
}
