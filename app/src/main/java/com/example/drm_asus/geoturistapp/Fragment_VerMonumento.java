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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Fragment_VerMonumento extends Fragment {

    TextView tv_monumento, tv_information, tv_horario, tv_dias, tv_visitas;
    ListView lv_comentarios, lv_valoraciones;
    Button btn_add_monumento, btn_multimedia, btn_valorar;
    String id_lugar, nombre_lugar, id_usuario, url_monumento, val_total;
    Boolean agregado;

    private static String entorno1 ="http://192.168.1.44/geoturistapp/ver_monumento_usuario.php?";
    private static String entorno2 ="http://172.10.2.138/geoturistAppWeb/ver_monumento_usuario.php?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__ver_monumento, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();
        id_lugar = bundl.getString("id_lugar");
        nombre_lugar = bundl.getString("nombre_lugar");
        id_usuario = bundl.getString("id_usuario");

        Log.d("Valor de ID_USUARIO",id_usuario);

        url_monumento = entorno1 + "id_lugar="+id_lugar+"&id_usuario="+id_usuario;

        // TextView de la vista
        tv_monumento = v.findViewById(R.id.tv_monumento);
        tv_information = v.findViewById(R.id.tv_information);
        tv_horario = v.findViewById(R.id.tv_horario);
        tv_dias = v.findViewById(R.id.tv_dias);
        tv_visitas = v.findViewById(R.id.tv_visitas);

        tv_information.setMovementMethod(new ScrollingMovementMethod());

        // ListView de la vista
        lv_comentarios = v.findViewById(R.id.lv_comentarios);
        lv_valoraciones = v.findViewById(R.id.lv_valoraciones);

        // Buttons de la vista
        btn_add_monumento = v.findViewById(R.id.btn_add_monumento);
        btn_multimedia = v.findViewById(R.id.btn_multimedia);
        btn_valorar = v.findViewById(R.id.btn_valorar);

        getJSON(url_monumento);

        Log.d("ID_LUGAR", id_lugar);
        Log.d("NOMBRE_LUGAR", nombre_lugar);


        // Llamada a la vista de multimedia con la informacion del monumento

        btn_multimedia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
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

                // Obtener la valoración TOTAL

                fragment_valorar.setArguments(bundl);


                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.contenedor, fragment_valorar);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });


        return v;
    }


    //this method is actually fetching the json string
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
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
                try {
                    loadLugar(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //in this method we are fetching the json string
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

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadLugar(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        JSONObject obj = jsonArray.getJSONObject(0);

        tv_monumento.setText(obj.getString("nombre"));
        tv_information.setText(obj.getString("descripcion"));
        tv_horario.setText(obj.getString("horario"));
        tv_dias.setText(obj.getString("dias_abre"));
        tv_visitas.setText(obj.getString("visitas"));

        Log.d("Valor de AGREGADO",String.valueOf(obj.getBoolean("agregado")));

        agregado = obj.getBoolean("agregado");
        val_total = obj.getString("val_total");

        if(obj.getBoolean("agregado")){
            //btn_add_monumento.setVisibility(View.GONE);
            btn_add_monumento.setText("Añadido");
            btn_add_monumento.setBackgroundColor(Color.GRAY);
            btn_add_monumento.setTextColor(Color.BLACK);
            btn_add_monumento.setEnabled(false);
        }


        // MIRAR PORQUE SI HAY UN TEXTO MUY LARGO EN LA DESCRIPCIÓN, NO SE MUESTRA NADA
    }
}
