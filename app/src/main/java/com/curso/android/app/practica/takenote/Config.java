package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;

public class Config extends AppCompatActivity {

    private Switch change_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        change_theme = findViewById(R.id.switchDarkMode);

        ImageView iconoHome = findViewById(R.id.iconoHome);
        iconoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        iconoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Config.this, Config.class);
                startActivity(intent);
            }
        });

        ImageView logOut = findViewById(R.id.iconoCerrarSesion);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });


        // Configura el listener del Switch para cambiar el tema dinámicamente
        change_theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guarda el estado del tema en las preferencias compartidas
                saveThemeState(isChecked);

                // Aplica el tema basado en el estado del Switch
                applyThemeBasedOnSwitch(isChecked);
            }
        });


        // Carga y aplica el tema inicialmente
        loadAndApplyTheme();


    }

    private void loadAndApplyTheme() {
        // Carga el estado del tema desde las preferencias compartidas
        boolean isDarkTheme = loadThemeState();

        // Aplica el tema basado en el estado cargado
        applyThemeBasedOnSwitch(isDarkTheme);

        // Actualiza el estado del Switch
        change_theme.setChecked(isDarkTheme);
    }

    private void saveThemeState(boolean isDarkTheme) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_theme", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_dark_theme", isDarkTheme);
        editor.apply();
    }

    private boolean loadThemeState() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_theme", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_dark_theme", false);
    }

    private void applyThemeBasedOnSwitch(boolean isDarkTheme) {
        temaUtils.applyThemeToActivity(this, isDarkTheme);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void goToGestion(View view) {
        Intent intent = new Intent(this, Gestion.class);
        startActivity(intent);
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Config.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}

