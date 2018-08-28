package com.example.drm_asus.geoturistapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by David Rodríguez
 */

public class Fragment_Monumentos extends Fragment {

    TextView nick_usuario, tv_visitados;
    ListView lista_monumentos;
    String url_monumentos, username;
    Button btn_descubre_monumentos;

    public static final String USER_PASS = "user_pass_save";

    private static String entorno ="http://192.168.1.44/geoturistapp/monumentos_usuario.php?id_usuario=";

    //private static String entorno ="http://socmica.000webhostapp.com/proyectos/geoturistapp/monumentos_usuario.php?id_usuario=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity) getActivity())
                .setActionBarTitle("Monumentos");

        View v = inflater.inflate(R.layout.fragment_fragment__mis_monumentos, container, false);
        nick_usuario = v.findViewById(R.id.tv_nick);
        lista_monumentos = v.findViewById(R.id.lv_monumentos);
        tv_visitados = v.findViewById(R.id.tv_visitados);
        btn_descubre_monumentos = v.findViewById(R.id.btn_descubre_monumentos);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        username = prefs.getString("username", null);

        nick_usuario.setText(username);

        url_monumentos = entorno + username;

        getJSON (url_monumentos);

        btn_descubre_monumentos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
                Fragment_Descubre fragment_descubre = new Fragment_Descubre();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.contenedor, fragment_descubre);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        return v;
    }




    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        //Creamos una array de strings para agregarlo al ListView
        final String[] lugares = new String[jsonArray.length()];
        final String[] id_lugares = new String[jsonArray.length()];

        //Recorremos los datos obtenidos en el json
        for (int i = 0; i < jsonArray.length(); i++) {

            //Obtenemos el objeto json del array de jsons
            JSONObject obj = jsonArray.getJSONObject(i);

            //obtenemos los parametros que queremos del objeto json
            lugares[i] = obj.getString("nombre_lugar");
            id_lugares[i] = obj.getString("id_lugar");

        }

        //Creamos un array adapter para almacenar los datos anteriores
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lugares);

        if(lugares != null) {
            lista_monumentos.setAdapter(arrayAdapter);
            tv_visitados.setText(String.valueOf(lugares.length));

            lista_monumentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("click",String.valueOf(position));

                    Fragment_VerMonumento fragment_monumento = new Fragment_VerMonumento();

                    Bundle bundl = new Bundle();

                    bundl.putString("nombre_lugar", lugares[position]);
                    bundl.putString("id_lugar", id_lugares[position]);
                    bundl.putString("id_usuario", username);

                    fragment_monumento.setArguments(bundl);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.replace(R.id.contenedor, fragment_monumento);
                    //transaction.addToBackStack(null);

                    transaction.commit();

                }
            });
        }
    }

    private void getJSON(final String urlWebService) {

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
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //creamos la URL
                    URL url = new URL(urlWebService);

                    //Se abre la URL usando HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder para leer la información
                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null) {

                        sb.append(json + "\n");
                    }

                    //Retornamos los datos leidos de la petición realizada. En este caso los datos del json
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


}
