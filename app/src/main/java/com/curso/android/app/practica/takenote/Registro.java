package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //Lectura y aplicación del tema oscuro si es debido.
        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText contraseniaEditText = findViewById(R.id.editTextPasswordRegister);
        Button buttonRegistro = findViewById(R.id.buttonRegister);

        //Oir y actuar con el click en el boton de registro.
        buttonRegistro.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String contrasenia = contraseniaEditText.getText().toString().trim();

            //Validación de campos y funcionalidad de registro
            if (email.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(Registro.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, contrasenia)
                        .addOnCompleteListener(Registro.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registro.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(Registro.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Registro.this, "Error al registrar usuario. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
