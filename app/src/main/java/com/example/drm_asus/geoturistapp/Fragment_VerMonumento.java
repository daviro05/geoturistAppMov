package com.example.drm_asus.geoturistapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment_VerMonumento extends Fragment {

    String id_lugar, nombre_lugar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__ver_monumento, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();

        id_lugar = bundl.getString("id_lugar");

        nombre_lugar = bundl.getString("nombre_lugar");

        Log.d("ID_LUGAR", id_lugar);
        Log.d("NOMBRE_LUGAR", nombre_lugar);


        return v;
    }
}
