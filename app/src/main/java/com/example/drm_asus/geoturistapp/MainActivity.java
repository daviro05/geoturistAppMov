package com.example.drm_asus.geoturistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tv_ir_registro;
    TextView tv_ir_login;
    Button btn_login;
    EditText et_usuario, et_password;
    CheckBox chk_recordar;
    public static final String USER_PASS = "user_pass_save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_password = findViewById(R.id.et_password);
        et_usuario = findViewById(R.id.et_usuario);
        chk_recordar = findViewById(R.id.chk_recordar);


        //Aquí realizamos la función de llamar de una activity a otra. De login a registro
        tv_ir_registro = findViewById(R.id.tv_ir_registro);

        tv_ir_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(MainActivity.this, Registro.class);
                MainActivity.this.startActivity(intentReg);
            }
        });

        btn_login = findViewById(R.id.btn_login);

        SharedPreferences prefs = getSharedPreferences(USER_PASS, MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);
        if (restoredText != null) {
            String id_usuario_save = prefs.getString("username", "");
            String password_save = prefs.getString("password", "");
            final String id_usuario = id_usuario_save;
            final String password  = password_save;

            logarse(id_usuario,password);
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String id_usuario = et_usuario.getText().toString();
                final String password = et_password.getText().toString();

                if(id_usuario.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Rellena los campos Usuario y Password")
                            .setNegativeButton("Volver a Intentar",null)
                            .create().show();
                }
                else {
                    logarse(id_usuario, password);
                }

            }
        });

    }

    public void logarse(String id_usuario,String password) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Crearemos un objeto JSON
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("correcto");

                    if(success){

                        if(chk_recordar.isChecked()) {
                            // SI ESSTÁ MARCADO EL CHECKBOX EN LOGIN GUARDAMOS EL USERNAME Y PASSWORD en SharedPreferences
                            SharedPreferences.Editor editor = getSharedPreferences(USER_PASS, MODE_PRIVATE).edit();
                            editor.putString("username", et_usuario.getText().toString());
                            editor.putString("password", et_password.getText().toString());
                            editor.apply();
                        }

                        String id_usuario = jsonResponse.getString("id_usuario");
                        String nombre_usuario = jsonResponse.getString("nombre");
                        String apellidos_usuario = jsonResponse.getString("apellidos");
                        String email_usuario = jsonResponse.getString("email");

                        //Con Intent lo que hacemos es especificar desde cual es la vista origen y cual la de destino.
                        //Y mandamos nuestras variables para que se muestren en la vista principal despues del Logarnos.

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);


                        intent.putExtra("id_usuario",id_usuario);
                        intent.putExtra("nombre",nombre_usuario);
                        intent.putExtra("apellidos",apellidos_usuario);
                        intent.putExtra("email",email_usuario);

                        MainActivity.this.startActivity(intent);
                        Log.d("Valor de success:", String.valueOf(success));
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login incorrecto")
                                .setNegativeButton("Volver a Intentar",null)
                                .create().show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Vamos a crear nuestra clase LoginRequest
        LoginRequest loginRequest = new LoginRequest(id_usuario,password,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }

}
