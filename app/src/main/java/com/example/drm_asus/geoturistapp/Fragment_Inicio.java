package com.example.drm_asus.geoturistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class Fragment_Inicio extends Fragment {

    Button btn_mis_monumentos, btn_descubre_monumentos, btn_mis_valoraciones;
    String nick;
    TextView bienvenida;
    public static final String USER_PASS = "user_pass_save";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__inicio, container, false);

        btn_mis_monumentos = v.findViewById(R.id.btn_mis_monumentos);
        btn_descubre_monumentos = v.findViewById(R.id.btn_descubre_monumentos);
        btn_mis_valoraciones = v.findViewById(R.id.btn_mis_valoraciones);
        bienvenida = v.findViewById(R.id.bienvenida);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        String username = prefs.getString("username", null);

        bienvenida.setText("Hola "+username+"!");

        btn_mis_monumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Fragment_Monumentos fragment_monumentos = new Fragment_Monumentos();

                Bundle bundl = getArguments();
                // nick = bundl.getString("nick");

                //fragment_monumentos.setArguments(bundl);

                // Log.d("Inicio: ", nick);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment_monumentos)
                        .addToBackStack(null)
                        .commit();
            }
        });


        btn_descubre_monumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Descubre fragment_descubre = new Fragment_Descubre();
                Bundle bundl = new Bundle();
                bundl.putString("usuario", "David");

                fragment_descubre.setArguments(bundl);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment_descubre)
                        .addToBackStack(null)
                        .commit();

            }
        });



        btn_mis_valoraciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Valorar fragment_valorar = new Fragment_Valorar();
                Bundle bundl = new Bundle();
                bundl.putString("usuario", "David");

                fragment_valorar.setArguments(bundl);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment_valorar)
                        .addToBackStack(null)
                        .commit();

            }
        });
        // Inflate the layout for this fragment
        return v;
    }

}
