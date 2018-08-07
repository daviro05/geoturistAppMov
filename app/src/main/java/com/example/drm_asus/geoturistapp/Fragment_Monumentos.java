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


public class Fragment_Monumentos extends Fragment {

    TextView nick_usuario, tv_visitados;
    ListView lista_monumentos;
    String url_monumentos;
    public static final String USER_PASS = "user_pass_save";


    private static String entorno1 ="http://192.168.1.44/geoturistapp/monumentos_usuario.php?id_usuario=";
    private static String entorno2 ="http://172.10.2.138/geoturistAppWeb/monumentos_usuario.php?id_usuario=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__mis_monumentos, container, false);
        nick_usuario = v.findViewById(R.id.tv_nick);
        lista_monumentos = v.findViewById(R.id.lv_monumentos);
        tv_visitados = v.findViewById(R.id.tv_visitados);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        String username = prefs.getString("username", null);

        nick_usuario.setText(username);

        url_monumentos = entorno1+username;

        getJSON (url_monumentos);

        return v;
    }




    private void loadIntoListView(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        final String[] lugares = new String[jsonArray.length()];
        final String[] id_lugares = new String[jsonArray.length()];

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);

            //getting the name from the json object and putting it inside string array
            lugares[i] = obj.getString("nombre_lugar");
            id_lugares[i] = obj.getString("id_lugar");

        }

        //the array adapter to load data into list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lugares);

        //attaching adapter to listview
        if(lugares != null) {
            lista_monumentos.setAdapter(arrayAdapter);
            tv_visitados.setText(String.valueOf(lugares.length));

            lista_monumentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("click",String.valueOf(position));

                    // Create new fragment and transaction
                    Fragment_VerMonumento fragment_monumento = new Fragment_VerMonumento();

                    Bundle bundl = new Bundle();

                    bundl.putString("nombre_lugar", lugares[position]);
                    bundl.putString("id_lugar", id_lugares[position]);

                    fragment_monumento.setArguments(bundl);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.replace(R.id.contenedor, fragment_monumento);
                    transaction.addToBackStack(null);

                    transaction.commit();

                }
            });
        }
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
                    loadIntoListView(s);
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


}
