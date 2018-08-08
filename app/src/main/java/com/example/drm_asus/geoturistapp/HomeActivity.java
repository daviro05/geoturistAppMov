package com.example.drm_asus.geoturistapp;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;


// Clase que contiene el menu principa de la aplicación

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

    String nick, nombre, apellidos, email, valoraciones,comentarios;
    public static final String USER_PASS = "user_pass_save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_Inicio()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Vamos a recibir las variables que nos llegan de MainActivity

        Intent intent = getIntent();

        nick = intent.getStringExtra("id_usuario");
        nombre = intent.getStringExtra("nombre");
        apellidos = intent.getStringExtra("apellidos");
        email = intent.getStringExtra("email");
        comentarios = intent.getStringExtra("comentarios");
        valoraciones = intent.getStringExtra("valoraciones");


        Log.d("Usuario: ", nick);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment_Inicio fragment_inicio = new Fragment_Inicio();
            fragmentManager.beginTransaction().replace(R.id.contenedor, fragment_inicio).commit();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Creamos un bundle para poder pasarle los datos del usuario logeado y poder pasarselos a todos los fragments
            Fragment_Inicio fragment_inicio = new Fragment_Inicio();

            // SOLUCIONAR PROBLEMA DE NULL CUANDO NO SE MARCA EL BOTON DE RECORDAR

            /*Bundle bundl = new Bundle();
            bundl.putString("nick", nick);
            fragment_inicio.setArguments(bundl);*/

            fragmentManager.beginTransaction().replace(R.id.contenedor, fragment_inicio).commit();
        } else if (id == R.id.nav_monumentos) {

            Fragment_Monumentos fragment_monumentos = new Fragment_Monumentos();
            fragmentManager.beginTransaction().replace(R.id.contenedor, fragment_monumentos).commit();

        } else if (id == R.id.nav_descubre) {

            // Intent intentCoords = new Intent(HomeActivity.this, Coordenadas.class);
            // HomeActivity.this.startActivity(intentCoords);
            Fragment_Descubre fragment_descubre = new Fragment_Descubre();
            fragmentManager.beginTransaction().replace(R.id.contenedor, fragment_descubre).commit();

        } else if (id == R.id.nav_perfil) {

            // Creamos un bundle para poder pasarle los datos del usuario logeado y verlos en el fragment de Mi Perfil
            Fragment_Perfil fragment_perfil = new Fragment_Perfil();
            Bundle bundl = new Bundle();

            bundl.putString("nick", nick);
            bundl.putString("nombre", nombre);
            bundl.putString("apellidos", apellidos);
            bundl.putString("email", email);
            bundl.putString("comentarios",comentarios);
            bundl.putString("valoraciones",valoraciones);
            

            fragment_perfil.setArguments(bundl);
            fragmentManager.beginTransaction().replace(R.id.contenedor, fragment_perfil).commit();
        }
        else if (id == R.id.nav_logout) {

            // Elemento del menu lateral para cerar la sesión.

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("¿Desea salir de la aplicación?");
            //alertDialogBuilder.setIcon(R.drawable.question);
            alertDialogBuilder.setMessage("Saldrás de GeoturistApp");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // BORRAMOS EL USERNAME Y PASSWORD en SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences(USER_PASS, MODE_PRIVATE).edit();
                    editor.putString("username", null);
                    editor.putString("password", null);
                    editor.apply();
                    Intent intentLogout = new Intent(HomeActivity.this, MainActivity.class);
                    HomeActivity.this.startActivity(intentLogout);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
