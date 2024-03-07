package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Nueva extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextCuerpo;
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_nota);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        dbHelper = DatabaseHelper.getInstance(this);

        Button buttonGuardar = findViewById(R.id.btnGuardar);
        editTextTitulo = findViewById(R.id.editTituloNota);
        editTextCuerpo = findViewById(R.id.editCuerpoNota);

        mAuth = FirebaseAuth.getInstance();

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNota();
                Intent intent = new Intent(Nueva.this, Home.class);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(Nueva.this, Config.class);
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

    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Nueva.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String user_id = user.getUid();

            try {
                dbHelper.insertarNota(user_id, titulo, cuerpo);
                Toast.makeText(this, "Nota guardada correctamente", Toast.LENGTH_SHORT).show();

                Log.d("Nueva", "Nota guardada correctamente:");
                Log.d("Nueva", "Usuario ID: " + user_id);
                Log.d("Nueva", "Título: " + titulo);
                Log.d("Nueva", "Cuerpo: " + cuerpo);
            } catch (Exception e) {
                Log.e("Nueva", "Error al insertar la nota", e);
                Toast.makeText(this, "Error al insertar la nota", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

}
