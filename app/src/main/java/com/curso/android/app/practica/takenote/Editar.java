package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.curso.android.app.practica.takenote.utils.temaUtils;

public class Editar extends AppCompatActivity {

    // Declara las variables editTextTitulo y editTextCuerpo
    EditText editTextTitulo;
    EditText editTextCuerpo;

    // Declara la variable idNota
    int idNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_nota);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        editTextTitulo = findViewById(R.id.editTituloNota);
        editTextCuerpo = findViewById(R.id.editCuerpoNota);

        // Obtén los datos de la nota desde el Intent
        Intent intent = getIntent();
        if (intent != null) {
            idNota = intent.getIntExtra("ID_NOTA", -1);
            String tituloNota = intent.getStringExtra("TITULO_NOTA");
            String cuerpoNota = intent.getStringExtra("CUERPO_NOTA");

            // Asigna los valores de la nota a las variables
            editTextTitulo.setText(tituloNota);
            editTextCuerpo.setText(cuerpoNota);
        }

        Button buttonGuardar = findViewById(R.id.btnGuardar);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualiza los valores de la nota en la base de datos
                DatabaseHelper databaseHelper = new DatabaseHelper(Editar.this);
                databaseHelper.actualizarNota(idNota,
                        editTextTitulo.getText().toString(),
                        editTextCuerpo.getText().toString());

                // Envía una notificación al usuario de que la nota se ha editado correctamente
                Toast.makeText(Editar.this, "Nota editada correctamente", Toast.LENGTH_SHORT).show();

                // Vuelve a la actividad principal
                Intent intent = new Intent(Editar.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
