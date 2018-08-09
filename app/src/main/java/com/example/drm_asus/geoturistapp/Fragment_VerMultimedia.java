package com.example.drm_asus.geoturistapp;

import android.content.Intent;
import android.net.Uri;
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


public class Fragment_VerMultimedia extends Fragment {

    TextView tv_monumento;
    ListView lv_imagenes, lv_audios, lv_documentos;
    Button btn_add_monumento, btn_volver_monumento;
    String id_lugar, nombre_lugar, tipo_multimedia, url_multimedia_imagenes, url_multimedia_audios, url_multimedia_documentos;

    private static String entorno1 ="http://192.168.1.44/geoturistapp/multimedia_usuario.php?";
    private static String entorno2 ="http://172.10.2.138/geoturistAppWeb/multimedia_usuario.php?";

    private static String url_entorno1 ="http://192.168.1.44/geoturistapp/";
    private static String url_entorno2 ="http://172.10.2.138/geoturistAppWeb/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__ver_multimedia, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();
        id_lugar = bundl.getString("id_lugar");
        nombre_lugar = bundl.getString("nombre_lugar");

        url_multimedia_imagenes = entorno2+"id_lugar="+id_lugar+"&tipo_multimedia=imagenes";
        url_multimedia_audios = entorno2+"id_lugar="+id_lugar+"&tipo_multimedia=audios";
        url_multimedia_documentos = entorno2+"id_lugar="+id_lugar+"&tipo_multimedia=documentos";


        // TextView de la vista
        tv_monumento = v.findViewById(R.id.tv_monumento);


        //ListView de la vista
        lv_imagenes = v.findViewById(R.id.lv_imagenes);
        lv_audios   = v.findViewById(R.id.lv_audios);
        lv_documentos = v.findViewById(R.id.lv_documentos);


        // Button de la vista
        btn_add_monumento = v.findViewById(R.id.btn_add_monumento);
        btn_volver_monumento = v.findViewById(R.id.btn_volver_monumento);

        getJSON(url_multimedia_imagenes, "imagenes");
        getJSON(url_multimedia_audios, "audios");
        getJSON(url_multimedia_documentos, "documentos");

        tv_monumento.setText(nombre_lugar);

        // Inflate the layout for this fragment
        return v;
    }


    //this method is actually fetching the json string
    private void getJSON(final String urlWebService, final String tipo) {

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
                    loadMultimedia(s, tipo);
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

    private void loadMultimedia(String json, String tipo) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        if(tipo == "imagenes"){
            final String[] imagenes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                imagenes[i] = obj.getString("url_imagen");

            }

            //the array adapter to load data into list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, imagenes);

            //attaching adapter to listview
            if(imagenes != null) {
                lv_imagenes.setAdapter(arrayAdapter);

                lv_imagenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        String url_im = url_entorno2+"multimedia/img_lugares/"+imagenes[position];
                        intent.setData(Uri.parse(url_im));
                        startActivity(intent);

                        // LLAMADA A LA RUTA DE LAS IMAGENES: entorno2+multimedia/img_lugares/nombre_img.jpg
                    }
                });
            }
        }
        else if(tipo == "audios"){
            final String[] audios = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                audios[i] = obj.getString("url_audio");

            }

            //the array adapter to load data into list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, audios);

            //attaching adapter to listview
            if(audios != null) {
                lv_audios.setAdapter(arrayAdapter);

                lv_audios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        // LLAMADA A LA RUTA DE LAS IMAGENES: entorno2+multimedia/audio_lugares/nombre_audio.mp3
                    }
                });
            }
        }
        else if(tipo == "documentos"){
            final String[] documentos = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                documentos[i] = obj.getString("url_doc");

            }

            //the array adapter to load data into list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, documentos);

            //attaching adapter to listview
            if(documentos != null) {
                lv_documentos.setAdapter(arrayAdapter);

                lv_documentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        // LLAMADA A LA RUTA DE LAS IMAGENES: entorno2+multimedia/doc_lugares/nombre_doc.pdf
                    }
                });
            }
        }


    }
}
