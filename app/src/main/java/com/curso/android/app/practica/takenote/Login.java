package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextUsername);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView forgotPasswordTextView = findViewById(R.id.textViewPasswordForgot);

        //Oir el click sobre el boton de Login y actuar en consecuencia.
        buttonLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, task -> {
                            if (task.isSuccessful()) {
                                // Inicio de sesión exitoso
                                Toast.makeText(Login.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                                finish(); // Finaliza la actividad de login solo si el inicio de sesión es exitoso
                            } else {
                                // Error durante el inicio de sesión
                                Toast.makeText(Login.this, "Error al iniciar sesión. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        //Verificar si hay un usuario autenticado sino se lo envia a Login
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }

        // Manejar el clic en el enlace "Restablece tu contraseña"
        forgotPasswordTextView.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Se ha enviado un correo electrónico para restablecer tu contraseña.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "No se pudo enviar el correo electrónico para restablecer la contraseña. Verifica tu dirección de correo electrónico.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(Login.this, "Por favor, introduce tu correo electrónico para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Ir a la pantalla de Registro.
    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }


}

