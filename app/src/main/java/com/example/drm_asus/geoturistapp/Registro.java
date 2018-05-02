package com.example.drm_asus.geoturistapp;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    TextView tv_volver_login;
    EditText et_nick,et_nombre,et_apellidos,et_password,et_email,et_sexo,et_fecha_nac;
    Button btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Aquí buscamos los elementos de la interfaz para añadirselo a nuestras variables definidas.

        et_nick = findViewById(R.id.et_nick);
        et_password = findViewById(R.id.et_passwd);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellidos = findViewById(R.id.et_apellidos);
        et_email = findViewById(R.id.et_email);

        btn_registrar = findViewById(R.id.Btn_registrarse);
        btn_registrar.setOnClickListener(this);

        //Aquí realizamos la función de llamar de una activity a otra. De login a registro
        tv_volver_login = findViewById(R.id.tv_volver_login);

          tv_volver_login.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intentReg = new Intent(Registro.this, MainActivity.class);
                  Registro.this.startActivity(intentReg);
              }
          });

    }

    @Override
    public void onClick(View view) {
        final String id_usuario = et_nick.getText().toString();
        final String password = et_password.getText().toString();
        final String nombre = et_nombre.getText().toString();
        final String apellidos = et_apellidos.getText().toString();
        final String email = et_email.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("correcto");

                    if(success){
                        Intent intent = new Intent(Registro.this,MainActivity.class);
                        Registro.this.startActivity(intent);
                        Log.d("Valor de success:", String.valueOf(success));
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                        builder.setMessage("Registro incorrecto")
                                .setNegativeButton("Volver a Intentar",null)
                                .create().show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegistroRequest registroRequest = new RegistroRequest(id_usuario,nombre,apellidos,password,email,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Registro.this);
        queue.add(registroRequest);
    }
}
