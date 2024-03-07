package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Editar extends AppCompatActivity {

    // Declara las variables editTextTitulo y editTextCuerpo
    EditText editTextTitulo;
    EditText editTextCuerpo;
    int idNota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_nota);

        // Verificar si el usuario está autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Si el usuario no está autenticado, redirigirlo a la pantalla de inicio de sesión
            Intent intent = new Intent(Editar.this, Login.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual para evitar que el usuario regrese presionando el botón de retroceso
        } else {
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

            //Oir el click en el boton de guardar y realizar las acciones correspondientes
            buttonGuardar.setOnClickListener(v -> {
                String titulo = editTextTitulo.getText().toString();
                String cuerpo = editTextCuerpo.getText().toString();

                if (titulo.isEmpty() || cuerpo.isEmpty()) {
                    Toast.makeText(Editar.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Actualiza los valores de la nota en la base de datos
                try (DatabaseHelper databaseHelper = new DatabaseHelper(Editar.this)) {
                    databaseHelper.actualizarNota(idNota, titulo, cuerpo);

                    // Envía una notificación al usuario de que la nota se ha editado correctamente
                    Toast.makeText(Editar.this, "Nota editada correctamente", Toast.LENGTH_SHORT).show();

                    // Vuelve a la actividad principal
                    Intent intent1 = new Intent(Editar.this, Home.class);
                    startActivity(intent1);
                } catch (Exception e) {
                    // Maneja cualquier excepción que pueda ocurrir durante la actualización de la nota
                    Log.e("Editar", "Error al actualizar la nota", e);
                    Toast.makeText(Editar.this, "Error al actualizar la nota", Toast.LENGTH_SHORT).show();
                }
            });

            //Importacion y funcionalidad de imageView de Toolbar
            ImageView iconoHome = findViewById(R.id.iconoHome);
            ImageView iconoConfig = findViewById(R.id.iconoConfig);
            ImageView logOut = findViewById(R.id.iconoCerrarSesion);

            iconoHome.setOnClickListener(v -> {
                Intent intent12 = new Intent(getApplicationContext(), Home.class);
                startActivity(intent12);
            });

            iconoConfig.setOnClickListener(v -> {
                Intent intent13 = new Intent(Editar.this, Config.class);
                startActivity(intent13);
            });

            logOut.setOnClickListener(v -> cerrarSesion());

    }
        }

        //Funcionalidad de cerrar sesion
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Editar.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
