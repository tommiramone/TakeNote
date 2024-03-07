package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class Detalle extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota_detalle);

        dbHelper = DatabaseHelper.getInstance(this);

        String titulo = getIntent().getStringExtra("titulo");
        String cuerpo = getIntent().getStringExtra("cuerpo");
        int notaId = getIntent().getIntExtra("notaId", -1); // Obtener el ID de la nota

        TextView tituloTextView = findViewById(R.id.tituloNota);
        TextView cuerpoTextView = findViewById(R.id.cuerpoNota);
        Button botonRestaurar = findViewById(R.id.botonRestaurar); // Botón Restaurar

        tituloTextView.setText(titulo);
        cuerpoTextView.setText(cuerpo);


        // Configurar el botón de cerrar sesión
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Detalle.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
