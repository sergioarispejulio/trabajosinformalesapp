package com.example.sergioarispejulio.proyectogrado;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.example.sergioarispejulio.proyectogrado.clases.Trabajo;
import com.example.sergioarispejulio.proyectogrado.clases.singleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;


public class map_view_profile_or_work extends Fragment implements GoogleMap.OnMapClickListener {

    public GoogleMap mMap;


    public map_view_profile_or_work() {
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
        View v = inflater.inflate(R.layout.fragment_map_view_profile_or_work, container, false);
        setUpMapIfNeeded();
        //ubicar();
        cargardatos();


        return v;

    }

    public void cargardatos()
    {
        Trabajo elemento = singleton.getInstance().trabajo_seleccionado;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(elemento.latitud, elemento.longitud), 10);
        mMap.addMarker(new MarkerOptions().position(new LatLng(elemento.latitud, elemento.longitud)).
                icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.moveCamera(cameraUpdate);
    }

    private void setUpMapIfNeeded() {
// Configuramos el objeto GoogleMaps con valores iniciales.
        if (mMap == null) {
            //Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment)  getActivity().getFragmentManager().findFragmentById(R.id.fragment_mapa)).getMap();
            // Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
            if (mMap != null) {
                // El objeto GoogleMap ha sido referenciado correctamente
                //ahora podemos manipular sus propiedades

                //Seteamos el tipo de mapa
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(this);
            }
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        ;
    }

    public void ubicar() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
            mMap.moveCamera(cameraUpdate);

        }
    }
}
