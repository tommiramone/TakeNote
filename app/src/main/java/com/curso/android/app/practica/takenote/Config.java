package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;

public class Config extends AppCompatActivity {

    private SwitchCompat change_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        change_theme = findViewById(R.id.switchDarkMode);

        //Importacion y funcionalidad de ImageView de toolbar.
        ImageView iconoHome = findViewById(R.id.iconoHome);
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Config.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());


        // Configura el listener del Switch para cambiar el tema dinámicamente
        change_theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guarda el estado del tema en las preferencias compartidas
            saveThemeState(isChecked);

            // Aplica el tema basado en el estado del Switch
            applyThemeBasedOnSwitch(isChecked);
        });

        // Carga y aplica el tema inicialmente
        loadAndApplyTheme();


    }

    //Metodo para aplicar el tema oscuro o claro segun el switch.
    private void loadAndApplyTheme() {
        // Carga el estado del tema desde las preferencias compartidas
        boolean isDarkTheme = loadThemeState();

        // Aplica el tema basado en el estado cargado
        applyThemeBasedOnSwitch(isDarkTheme);

        // Actualiza el estado del Switch
        change_theme.setChecked(isDarkTheme);
    }

    //Función que se encarga de guardar el estado del tema elegido en SharedPreferences
    private void saveThemeState(boolean isDarkTheme) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_theme", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_dark_theme", isDarkTheme);
        editor.apply();
    }

    //Función que se carga el estado del tema desde las preferencias compartidas.
    private boolean loadThemeState() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_theme", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_dark_theme", false);
    }

    //Función que se encarga de aplicar el tema a la actividad basado en el estado del Switch.
    private void applyThemeBasedOnSwitch(boolean isDarkTheme) {
        temaUtils.applyThemeToActivity(this, isDarkTheme);
    }

    //Metodo para enviar al usuario a Gestion de usuario.
    public void goToGestion(View view) {
        Intent intent = new Intent(this, Gestion.class);
        startActivity(intent);
    }

    //Metodo para cerrar sesion utilizando firebaseAuth
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Config.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}

