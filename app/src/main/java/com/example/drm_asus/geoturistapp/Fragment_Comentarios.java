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

public class Fragment_Comentarios extends Fragment {

    TextView tv_comentarios;
    ListView lista_comentarios;
    String url_comentarios, username;
    Button btn_descubre_monumentos;

    public static final String USER_PASS = "user_pass_save";


    private static String entorno ="http://192.168.1.44/geoturistapp/comentarios_usuario.php?";
    //private static String entorno =" http://socmica.000webhostapp.com/proyectos/geoturistapp/comentarios_usuario.php?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity) getActivity())
                .setActionBarTitle("Comentarios");

        View v = inflater.inflate(R.layout.fragment_fragment__mis_comentarios, container, false);
        lista_comentarios = v.findViewById(R.id.lv_comentarios);
        tv_comentarios = v.findViewById(R.id.tv_comentarios);

        btn_descubre_monumentos = v.findViewById(R.id.btn_descubre_monumentos);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        username = prefs.getString("username", null);

        url_comentarios = entorno + "id_usuario=" + username;

        getJSON (url_comentarios);

        btn_descubre_monumentos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        final String[] comentarios = new String[jsonArray.length()];
        final String[] nombre_lugar = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);

            comentarios[i] = obj.getString("comentario");
            nombre_lugar[i] = obj.getString("nombre_lugar");

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, comentarios);

        if(comentarios != null) {
            lista_comentarios.setAdapter(arrayAdapter);
            tv_comentarios.setText(String.valueOf(comentarios.length));
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


}
