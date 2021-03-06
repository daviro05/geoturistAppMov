package com.example.drm_asus.geoturistapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David Rodríguez
 */

public class Fragment_VerMonumento extends Fragment {

    TextView tv_monumento, tv_information, tv_horario, tv_dias, tv_visitas, tv_valoracion_total;
    ListView lv_comentarios;
    Button btn_add_monumento, btn_multimedia, btn_valorar;
    String id_lugar, nombre_lugar, id_usuario, url_monumento, val_total, num_val, url_comentarios, url_add_lugar;
    Boolean agregado;

    private static String entorno ="http://192.168.1.44/geoturistapp/ver_monumento_usuario.php?";

    //private static String entorno ="http://socmica.000webhostapp.com/proyectos/geoturistapp/ver_monumento_usuario.php?";

    private static String entorno_com ="http://192.168.1.44/geoturistapp/lista_com_usuario.php?";

    //private static String entorno_com ="http://socmica.000webhostapp.com/proyectos/geoturistapp/lista_com_usuario.php?";

    private static String entorno_add_lugar ="http://192.168.1.44/geoturistapp/add_lugar_usuario.php?";

    //private static String entorno_add_lugar ="http://socmica.000webhostapp.com/proyectos/geoturistapp/add_lugar_usuario.php?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity) getActivity())
                .setActionBarTitle("Monumento");

        View v = inflater.inflate(R.layout.fragment_fragment__ver_monumento, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();
        id_lugar = bundl.getString("id_lugar");
        nombre_lugar = bundl.getString("nombre_lugar");
        id_usuario = bundl.getString("id_usuario");
        agregado = bundl.getBoolean("agregado");

        Log.d("Valor de ID_USUARIO",id_usuario);

        url_monumento = entorno + "id_lugar="+id_lugar+"&id_usuario="+id_usuario;

        url_comentarios  =   entorno_com + "id_lugar=" + id_lugar+"&tipo_dato=comentarios";

        // TextView de la vista
        tv_monumento = v.findViewById(R.id.tv_monumento);
        tv_information = v.findViewById(R.id.tv_information);
        tv_horario = v.findViewById(R.id.tv_horario);
        tv_dias = v.findViewById(R.id.tv_dias);
        tv_visitas = v.findViewById(R.id.tv_visitas);
        tv_valoracion_total = v.findViewById(R.id.tv_valoracion_total);

        tv_information.setMovementMethod(new ScrollingMovementMethod());

        // ListView de la vista
        lv_comentarios = v.findViewById(R.id.lv_comentarios);

        // Buttons de la vista
        btn_add_monumento = v.findViewById(R.id.btn_add_monumento);
        btn_multimedia = v.findViewById(R.id.btn_multimedia);
        btn_valorar = v.findViewById(R.id.btn_valorar);

        getJSON(url_monumento, "monumento");
        getJSON(url_comentarios, "comentarios");

        Log.d("ID_LUGAR", id_lugar);
        Log.d("NOMBRE_LUGAR", nombre_lugar);


        // Llamada a la vista de multimedia con la informacion del monumento

        btn_multimedia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment_VerMultimedia fragment_multimedia = new Fragment_VerMultimedia();
                Bundle bundl = new Bundle();

                bundl.putString("id_lugar", id_lugar);
                bundl.putString("nombre_lugar", nombre_lugar);
                bundl.putString("id_usuario",id_usuario);
                bundl.putBoolean("agregado",agregado);

                fragment_multimedia.setArguments(bundl);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.contenedor, fragment_multimedia);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        btn_valorar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
                Fragment_Valorar fragment_valorar = new Fragment_Valorar();

                Bundle bundl = new Bundle();

                bundl.putString("nombre_lugar", nombre_lugar);
                bundl.putString("id_lugar", id_lugar);
                bundl.putString("id_usuario", id_usuario);

                bundl.putString("val_total",val_total);
                bundl.putString("num_val",num_val);

                bundl.putBoolean("agregado",agregado);

                fragment_valorar.setArguments(bundl);


                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.contenedor, fragment_valorar);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        btn_add_monumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_add_lugar = entorno_add_lugar + "id_lugar=" + id_lugar + "&id_usuario=" + id_usuario + "&nombre_lugar=" + nombre_lugar;
                Log.d("URL LUGAR",url_add_lugar);
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

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    if(tipo == "add"){
                        addLugar();
                    }
                    else
                        loadLugar(s, tipo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {

                    URL url = new URL(urlWebService);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null) {

                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadLugar(String json, String tipo) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);

        if(tipo == "monumento") {
            JSONObject obj = jsonArray.getJSONObject(0);

            tv_monumento.setText(obj.getString("nombre"));
            tv_information.setText(obj.getString("descripcion"));
            tv_horario.setText(obj.getString("horario"));
            tv_dias.setText(obj.getString("dias_abre"));
            tv_visitas.setText(obj.getString("visitas"));

            Log.d("Valor de AGREGADO", String.valueOf(obj.getBoolean("agregado")));

            agregado = obj.getBoolean("agregado");
            val_total = obj.getString("val_total");
            num_val = obj.getString("num_val");

            tv_valoracion_total.setText(val_total);


            if (obj.getBoolean("agregado")) {
                //btn_add_monumento.setVisibility(View.GONE);
                btn_add_monumento.setText("Añadido");
                btn_add_monumento.setBackgroundColor(Color.GRAY);
                btn_add_monumento.setTextColor(Color.BLACK);
                btn_add_monumento.setEnabled(false);
            }
        }
        else if(tipo == "comentarios"){

            final String[] comentarios = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                comentarios[i] = obj.getString("comentarios");
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, comentarios);

            if(comentarios != null) {
                lv_comentarios.setAdapter(arrayAdapter);
            }

        }

    }


    public void addLugar(){

        btn_add_monumento.setText("Añadido");
        btn_add_monumento.setBackgroundColor(Color.GRAY);
        btn_add_monumento.setTextColor(Color.BLACK);
        btn_add_monumento.setEnabled(false);

        Toast.makeText(getActivity().getApplicationContext(), "Lugar añadido a tu lista", Toast.LENGTH_SHORT).show();
    }
}
