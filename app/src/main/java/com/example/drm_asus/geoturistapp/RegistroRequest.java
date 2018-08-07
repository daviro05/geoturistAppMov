package com.example.drm_asus.geoturistapp;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Rodríguez
 */

public class RegistroRequest extends StringRequest {

    private static String entorno1 = "http://192.168.1.44/geoturistapp/registro_usuario.php";
    private static String entorno2 = "http://172.10.2.138/geoturistAppWeb/registro_usuario.php";

    private static final String REGISTER_REQUEST_URL=entorno2;
    private Map<String,String> params;
    public RegistroRequest(String id_usuario, String nombre, String apellidos, String password, String email, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("id_usuario",id_usuario);
        params.put("nombre",nombre);
        params.put("apellidos",apellidos);
        params.put("password",password);
        params.put("email",email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
