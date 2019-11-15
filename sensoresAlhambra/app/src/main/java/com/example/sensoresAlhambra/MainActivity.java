package com.example.sensoresAlhambra;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

/**
 * Actividad principal
 * Se piden permisos y se lanza la actividad de comienzo deseada (en este caso ViewAR)
 */
public class MainActivity extends AppCompatActivity{

    /**
     * Variable que contiene la barra de navegación
     */
    private AppBarConfiguration mAppBarConfiguration;

    /**
     * Código de permiso usado cuando se piden los permisos
     * Su valor da igual, pero tiene que ser >= 0
     */
    private static final int PERMISSION_ALL = 1;

    /**
     * Array con los permisos a pedir (localización y cámara)
     */
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                                                 Manifest.permission.CAMERA};

    /**
     * Método que comprueba si se tienen los permisos necesarios para poder ejecutar
     * la aplicación
     * @return True si se tienen todos los permisos, false en caso contrario
     */
    private boolean hasPermisions() {
        // Establecer valor de salida inicial
        boolean hasPermisions = true;

        // Comprobar cada uno de los permisos
        for (String permission: MainActivity.PERMISSIONS) {
            // Si alguno de ellos no se tiene, se pone la variable a false
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)  != PackageManager.PERMISSION_GRANTED) {
                hasPermisions = false;
            }
        }

        return hasPermisions;
    }

    /**
     * Método que inicializa la barra de navegacion y solicita los permisos
     * necesarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Comprobar permisos y pedirlos en caso de ser necesario
        if (!hasPermisions()) {
            ActivityCompat.requestPermissions(this, MainActivity.PERMISSIONS, MainActivity.PERMISSION_ALL);
        }

        // Constructor de la barra de navegación
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_viewar, R.id.nav_camera, R.id.nav_navigation, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();

        // Establecer barra de navegación
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Método llamado al crear el menú
     * @param menu Menú con el que hacer operaciones
     * @return Devuelve siempre true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Método llamado cuando se despliega la barra. Muestra los elementos
     * de la barra
     * @return True si se ha desplegado correctamente, false en caso contrario
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
