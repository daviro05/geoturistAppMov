package com.example.drm_asus.geoturistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Usuario extends AppCompatActivity {

    TextView tv_nick, tv_nombre, tv_apellidos, tv_email;
    TextView volver_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        //Aquí realizamos la función de llamar de una activity a otra. De usuario a login
        volver_login = findViewById(R.id.volver_login);

        volver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(Usuario.this, MainActivity.class);
                Usuario.this.startActivity(intentReg);
            }
        });




        //Asociamos nuestras variables con las de la vista
        tv_nick = findViewById(R.id.tv_nick);
        tv_nombre = findViewById(R.id.tv_nombre);
        tv_apellidos = findViewById(R.id.tv_apellidos);
        tv_email = findViewById(R.id.tv_email);

        // Vamos a recibir las variables que nos llegan de MainActivity
        Intent intent = getIntent();

        String nick = intent.getStringExtra("id_usuario");
        String nombre = intent.getStringExtra("nombre");
        String apellidos = intent.getStringExtra("apellidos");
        String email = intent.getStringExtra("email");

        tv_nick.setText(nick);
        tv_nombre.setText(nombre);
        tv_apellidos.setText(apellidos);
        tv_email.setText(email);

    }
}
