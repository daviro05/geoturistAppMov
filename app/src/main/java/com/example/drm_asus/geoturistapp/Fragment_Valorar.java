package com.example.drm_asus.geoturistapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David Rodríguez
 */

public class Fragment_Valorar extends Fragment {

    TextView tv_monumento, tv_valoracion_total;
    EditText et_valorar, et_comentario;
    Button btn_valorar, btn_volver_monumento, btn_add_monumento;
    String id_lugar, nombre_lugar, id_usuario, val_total, num_val, url_valorar, comentario, valoracion, url_add_lugar;
    Boolean agregado;

    //private static String entorno ="http://192.168.1.44/geoturistapp/valorar_usuario.php?";

    private static String entorno ="http://socmica.000webhostapp.com/proyectos/geoturistapp/valorar_usuario.php?";

    //private static String entorno_add_lugar ="http://192.168.1.44/geoturistapp/add_lugar_usuario.php?";

    private static String entorno_add_lugar ="http://socmica.000webhostapp.com/proyectos/geoturistapp/add_lugar_usuario.php?";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((HomeActivity) getActivity())
                .setActionBarTitle("Valorar");

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
                bundl.putBoolean("agregado",agregado);

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

                comentario = et_comentario.getText().toString();
                valoracion =  et_valorar.getText().toString();

                url_valorar = entorno + "id_usuario=" + id_usuario + "&id_lugar=" + id_lugar + "&valoracion=" + valoracion
                        + "&comentario=" + comentario;
                // Al hacer click en el botón de Valorar comprobamos que los campos valorar y comentario (no es necesario) están completos.
                // Necesitamos el id_usuario, id_lugar, valoracion y comentario

                if(!TextUtils.isEmpty(valoracion)) {
                    getJSON(url_valorar, "valorar");
                    //Toast.makeText(getActivity().getApplicationContext(), url_valorar, Toast.LENGTH_SHORT).show();

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
                else{
                    et_valorar.setError( "Valoración requerida" );
                }

            }
        });

        btn_add_monumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_add_lugar = entorno_add_lugar + "id_lugar=" + id_lugar + "&id_usuario=" + id_usuario + "&nombre_lugar=" + nombre_lugar;
                getJSON(url_add_lugar,"add");
            }
        });

        return v;
    }

    private void getJSON(final String urlWebService, final String tipo) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if(tipo == "add"){
                    addLugar();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public void addLugar(){

        btn_add_monumento.setText("Añadido");
        btn_add_monumento.setBackgroundColor(Color.GRAY);
        btn_add_monumento.setTextColor(Color.BLACK);
        btn_add_monumento.setEnabled(false);

        Toast.makeText(getActivity().getApplicationContext(), "Lugar añadido a tu lista", Toast.LENGTH_SHORT).show();
    }
}
