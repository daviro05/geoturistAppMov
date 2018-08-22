package com.example.drm_asus.geoturistapp;

import android.app.Application;

/**
 * Created by David Rodríguez
 */

public class Entorno extends Application{

    // "http://192.168.1.44/geoturistapp/login_usuario.php";
    // "http://socmica.000webhostapp.com/proyectos/geoturistapp/login_usuario.php";

    private String entorno_des = "http://192.168.1.44/geoturistapp/";
    //private String entorno_des = "http://socmica.000webhostapp.com/proyectos/geoturistapp/";

    public String getEntorno() {
        return entorno_des;
    }

    public void setEntorno(String env) {
        entorno_des = env;
    }
}
