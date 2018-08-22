package com.example.drm_asus.geoturistapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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

public class Fragment_Descubre extends Fragment {

    TextView tv_visitados, estado;
    ListView lv_monumentos;
    String url_monumentos, username;
    Button btn_geolocalizar;
    public static final String USER_PASS = "user_pass_save";

    private static String entorno ="http://192.168.1.44/geoturistapp/coordenadas_usuario.php?";

    //private static String entorno =" http://socmica.000webhostapp.com/proyectos/geoturistapp/coordenadas_usuario.php?";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity) getActivity())
                .setActionBarTitle("Descubre");

        View v = inflater.inflate(R.layout.fragment_fragment__descubre, container, false);

        tv_visitados = v.findViewById(R.id.tv_visitados);
        estado = v.findViewById(R.id.estado);
        lv_monumentos = v.findViewById(R.id.lv_monumentos);
        btn_geolocalizar = v.findViewById(R.id.btn_geolocalizar);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PASS, MODE_PRIVATE);
        username = prefs.getString("username", null);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            btn_geolocalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationStart();
                }
            });

        }

        return v;
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(Local.getMainActivity());
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        estado.setText("Buscando...");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        Fragment_Descubre descubre_frag;

        public Fragment_Descubre getMainActivity() {
            return descubre_frag;
        }

        public void setMainActivity(Fragment_Descubre descubre_frag) {
            this.descubre_frag = descubre_frag;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            estado.setText(Text);
            obtener_monumentos_coordenadas(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()));


        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            estado.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            estado.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }


    }

    public void obtener_monumentos_coordenadas(String longitud, String latitud){
        url_monumentos = entorno + "longitud=" + longitud + "&latitud=" + latitud;
        getJSON(url_monumentos);
    }


    private void getJSON(final String urlWebService) {

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
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
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
            lv_monumentos.setAdapter(arrayAdapter);
            tv_visitados.setText(String.valueOf(lugares.length));

            lv_monumentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("click",String.valueOf(position));

                    // Create new fragment and transaction
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
}
