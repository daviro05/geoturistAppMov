package com.example.drm_asus.geoturistapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Fragment_Valorar extends Fragment {

    TextView tv_monumento, tv_valoracion_total;
    EditText et_valorar, et_comentario;
    Button btn_valorar, btn_volver_monumento, btn_add_monumento;
    String id_lugar, nombre_lugar, id_usuario, val_total, num_val;
    Boolean agregado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__valorar, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();

        id_lugar = bundl.getString("id_lugar");
        nombre_lugar = bundl.getString("nombre_lugar");
        id_usuario = bundl.getString("id_usuario");

        val_total = bundl.getString("val_total");
        num_val = bundl.getString("num_val");

        agregado = bundl.getBoolean("agregado");

        // TextView de la vista
        tv_monumento = v.findViewById(R.id.tv_monumento);
        tv_valoracion_total = v.findViewById(R.id.tv_valoracion_total);

        // EditText de la vista
        et_valorar = v.findViewById(R.id.et_valorar);
        et_valorar.setFilters(new InputFilter[]{ new MinMaxFilter("1", "10")});

        et_comentario = v.findViewById(R.id.et_comentario);
        et_comentario.setMovementMethod(new ScrollingMovementMethod());

        // Button de la vista
        btn_volver_monumento = v.findViewById(R.id.btn_volver_monumento);
        btn_add_monumento = v.findViewById(R.id.btn_add_monumento);
        btn_valorar = v.findViewById(R.id.btn_valorar);


        // Ponemos la información recibida
        tv_monumento.setText(nombre_lugar);
        tv_valoracion_total.setText(val_total);


        if(agregado){
            //btn_add_monumento.setVisibility(View.GONE);
            btn_add_monumento.setText("Añadido");
            btn_add_monumento.setBackgroundColor(Color.GRAY);
            btn_add_monumento.setTextColor(Color.BLACK);
            btn_add_monumento.setEnabled(false);
        }


        btn_volver_monumento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
                Fragment_VerMonumento fragment_ver_monumento = new Fragment_VerMonumento();

                Bundle bundl = new Bundle();

                bundl.putString("nombre_lugar", nombre_lugar);
                bundl.putString("id_lugar", id_lugar);
                bundl.putString("id_usuario", id_usuario);

                fragment_ver_monumento.setArguments(bundl);


                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.contenedor, fragment_ver_monumento);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        // Funcionalidad del boton de valorar
        btn_valorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Al hacer click en el botón de Valorar comprobamos que los campos valorar y comentario (no es necesario) están completos.
                // Necesitamos el id_usuario, id_lugar, valoracion y comentario


            }
        });

        return v;
    }
}
