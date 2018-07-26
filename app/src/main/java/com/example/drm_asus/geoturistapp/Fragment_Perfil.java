package com.example.drm_asus.geoturistapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment_Perfil extends Fragment {

    TextView tv_nick, tv_nombre, tv_apellidos, tv_email;
    String nick, nombre, apellidos, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__perfil, container, false);

        tv_nick = v.findViewById(R.id.tv_nick);
        tv_nombre = v.findViewById(R.id.tv_nombre);
        tv_apellidos = v.findViewById(R.id.tv_apellidos);
        tv_email = v.findViewById(R.id.tv_email);


        // Recibimos los datos del activity HomeActivity para poder mostrarlos en el Fragment de Perfil.
        Bundle bundl = getArguments();

        nick = bundl.getString("nick");
        nombre = bundl.getString("nombre");
        apellidos = bundl.getString("apellidos");
        email = bundl.getString("email");

        // Los escribimos en los textview de nuestro layout
        tv_nick.setText(nick);
        tv_nombre.setText(nombre);
        tv_apellidos.setText(apellidos);
        tv_email.setText(email);

        return v;
    }


}
