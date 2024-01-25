package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;

public class Nueva extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextCuerpo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_nota);
        
        Button buttonGuardar = findViewById(R.id.btnGuardar);
        editTextTitulo = findViewById(R.id.editTituloNota);
        editTextCuerpo = findViewById(R.id.editCuerpoNota);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNota();
                Intent intent = new Intent(Nueva.this, Home.class);
                startActivity(intent);
            }
        });

    }

    private void guardarNota() {
        String titulo = editTextTitulo.getText().toString();
        String cuerpo = editTextCuerpo.getText().toString();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cuerpo.isEmpty()) {
            Toast.makeText(this, "El cuerpo no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.insertarNota(titulo, cuerpo);
        } catch (SQLException e) {
            Log.e("MyApp", "Error al insertar la nota", e);
            Toast.makeText(this, "Error al insertar la nota", Toast.LENGTH_SHORT).show();
        }
    }

}
