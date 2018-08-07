package com.example.drm_asus.geoturistapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Rodríguez
 */


public class LoginRequest extends StringRequest {
    private static String entorno1 = "http://192.168.1.44/geoturistapp/login_usuario.php";
    private static String entorno2 = "http://172.10.2.138/geoturistAppWeb/login_usuario.php";

    private static final String LOGIN_REQUEST_URL=entorno1;
    private Map<String,String> params;
    public LoginRequest(String id_usuario, String password, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("id_usuario",id_usuario);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
