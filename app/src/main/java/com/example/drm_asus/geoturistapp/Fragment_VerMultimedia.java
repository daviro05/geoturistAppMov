package com.example.drm_asus.geoturistapp;

import android.content.Intent;
import android.graphics.Color;
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

public class Fragment_VerMultimedia extends Fragment {

    private static String entorno ="http://192.168.1.44/geoturistapp/multimedia_usuario.php?";

    //private static String entorno ="http://socmica.000webhostapp.com/proyectos/geoturistapp/multimedia_usuario.php?";

    private static String url_entorno ="http://192.168.1.44/geoturistapp/";

    //private static String url_entorno ="http://socmica.000webhostapp.com/proyectos/geoturistapp/";

    private static String entorno_add_lugar ="http://192.168.1.44/geoturistapp/add_lugar_usuario.php?";

    //private static String entorno_add_lugar ="http://socmica.000webhostapp.com/proyectos/geoturistapp/add_lugar_usuario.php?";

    TextView tv_monumento;
    ListView lv_imagenes, lv_audios, lv_documentos;
    Button btn_add_monumento, btn_volver_monumento;
    String id_lugar, nombre_lugar, id_usuario, tipo_multimedia, url_multimedia_imagenes, url_multimedia_audios, url_multimedia_documentos, url_add_lugar;
    Boolean agregado;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity) getActivity())
                .setActionBarTitle("Multimedia");

        View v = inflater.inflate(R.layout.fragment_fragment__ver_multimedia, container, false);

        // Recibimos los datos del activity Fragment_Monumentos para poder mostrarlos en el Fragment de VerMonumentos.
        Bundle bundl = getArguments();
        id_lugar = bundl.getString("id_lugar");
        nombre_lugar = bundl.getString("nombre_lugar");
        id_usuario = bundl.getString("id_usuario");
        agregado = bundl.getBoolean("agregado");


        url_multimedia_imagenes =       entorno + "id_lugar=" + id_lugar+"&tipo_multimedia=imagenes";
        url_multimedia_audios =         entorno + "id_lugar=" + id_lugar+"&tipo_multimedia=audios";
        url_multimedia_documentos =     entorno + "id_lugar=" + id_lugar+"&tipo_multimedia=documentos";



        // TextView de la vista
        tv_monumento = v.findViewById(R.id.tv_monumento);


        //ListView de la vista
        lv_imagenes = v.findViewById(R.id.lv_imagenes);
        lv_audios   = v.findViewById(R.id.lv_audios);
        lv_documentos = v.findViewById(R.id.lv_documentos);


        // Button de la vista
        btn_add_monumento = v.findViewById(R.id.btn_add_monumento);
        btn_volver_monumento = v.findViewById(R.id.btn_volver_monumento);


        btn_volver_monumento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        Log.d("Valor de agregado:",String.valueOf(agregado));

        if(agregado){
            //btn_add_monumento.setVisibility(View.GONE);
            btn_add_monumento.setText("Añadido");
            btn_add_monumento.setBackgroundColor(Color.GRAY);
            btn_add_monumento.setTextColor(Color.BLACK);
            btn_add_monumento.setEnabled(false);
        }

        getJSON(url_multimedia_imagenes, "imagenes");
        getJSON(url_multimedia_audios, "audios");
        getJSON(url_multimedia_documentos, "documentos");

        tv_monumento.setText(nombre_lugar);

        btn_add_monumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_add_lugar = entorno_add_lugar + "id_lugar=" + id_lugar + "&id_usuario=" + id_usuario + "&nombre_lugar=" + nombre_lugar;
                getJSON(url_add_lugar,"add");
            }
        });

        return v;
    }


    //this method is actually fetching the json string
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
                    else {
                        loadMultimedia(s, tipo);
                    }
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

    private void loadMultimedia(String json, String tipo) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);

        if(tipo == "imagenes"){
            final String[] imagenes = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                imagenes[i] = obj.getString("url_imagen");

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, imagenes);

            if(imagenes != null) {
                lv_imagenes.setAdapter(arrayAdapter);

                lv_imagenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        String url_im = url_entorno + "multimedia/img_lugares/"+imagenes[position];
                        intent.setData(Uri.parse(url_im));
                        startActivity(intent);

                        // LLAMADA A LA RUTA DE LAS IMAGENES: entorno+multimedia/img_lugares/nombre_img.jpg
                    }
                });
            }
        }
        else if(tipo == "audios"){
            final String[] audios = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                audios[i] = obj.getString("url_audio");

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, audios);

            if(audios != null) {
                lv_audios.setAdapter(arrayAdapter);

                lv_audios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        String url_im = url_entorno + "multimedia/audio_lugares/"+audios[position];
                        intent.setData(Uri.parse(url_im));
                        startActivity(intent);

                        // LLAMADA A LA RUTA DE LOS AUDIOS: entorno+multimedia/audio_lugares/nombre_audio.mp3
                    }
                });
            }
        }
        else if(tipo == "documentos"){
            final String[] documentos = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                documentos[i] = obj.getString("url_doc");

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, documentos);

            if(documentos != null) {
                lv_documentos.setAdapter(arrayAdapter);

                lv_documentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("click",String.valueOf(position));

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        String url_im = url_entorno + "multimedia/doc_lugares/"+documentos[position];
                        intent.setData(Uri.parse(url_im));
                        startActivity(intent);

                        // LLAMADA A LA RUTA DE LOS DOC: entorno+multimedia/doc_lugares/nombre_doc.pdf
                    }
                });
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
