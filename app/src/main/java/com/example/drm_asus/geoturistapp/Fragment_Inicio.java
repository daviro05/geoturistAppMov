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

    Button btn_mis_monumentos, btn_descubre_monumentos, btn_mis_comentarios;
    String nick;
    TextView bienvenida;
    public static final String USER_PASS = "user_pass_save";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__inicio, container, false);

        btn_mis_monumentos = v.findViewById(R.id.btn_mis_monumentos);
        btn_descubre_monumentos = v.findViewById(R.id.btn_descubre_monumentos);
        btn_mis_comentarios = v.findViewById(R.id.btn_mis_comentarios);
        bienvenida = v.findViewById(R.id.bienvenida);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        String username = prefs.getString("username", null);

        bienvenida.setText("Hola "+username+"!");

        btn_mis_monumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Fragment_Monumentos fragment_monumentos = new Fragment_Monumentos();

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

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment_descubre)
                        .addToBackStack(null)
                        .commit();

            }
        });



        btn_mis_comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Comentarios fragment_comentarios = new Fragment_Comentarios();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment_comentarios)
                        .addToBackStack(null)
                        .commit();

            }
        });
        // Inflate the layout for this fragment
        return v;
    }

}
