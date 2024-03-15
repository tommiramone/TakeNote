package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;

public class Detalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota_detalle);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        String titulo = getIntent().getStringExtra("titulo");
        String cuerpo = getIntent().getStringExtra("cuerpo");

        TextView tituloTextView = findViewById(R.id.tituloNota);
        TextView cuerpoTextView = findViewById(R.id.cuerpoNota);

        tituloTextView.setText(titulo);
        cuerpoTextView.setText(cuerpo);

        //Importación y funcionalidad de imageView de toolbar.
        ImageView iconoHome = findViewById(R.id.iconoHome);
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Detalle.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());    }

    //Funcionalidad de cerrar sesion
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Detalle.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
