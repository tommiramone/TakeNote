package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;

public class Detalle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota_detalle);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        String titulo = getIntent().getStringExtra("titulo");
        String cuerpo = getIntent().getStringExtra("cuerpo");

        // Mostrar el título y el cuerpo en los TextViews
        TextView tituloTextView = findViewById(R.id.tituloNota);
        TextView cuerpoTextView = findViewById(R.id.cuerpoNota);

        tituloTextView.setText(titulo);
        cuerpoTextView.setText(cuerpo);
    }
}
