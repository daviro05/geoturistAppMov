package com.example.drm_asus.geoturistapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment_Monumentos extends Fragment {

    TextView nick_usuario;
    String nick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__mis_monumentos, container, false);
        nick_usuario = v.findViewById(R.id.tv_nick);

        Bundle bundl = getArguments();

        nick = bundl.getString("nick");

        nick_usuario.setText(nick);


        // Inflate the layout for this fragment
        return v;
    }
}
