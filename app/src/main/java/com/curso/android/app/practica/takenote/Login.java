package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextUsername);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();


                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso
                                        Toast.makeText(Login.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, Home.class);
                                        startActivity(intent);
                                        finish(); // Finaliza la actividad de login
                                    } else {
                                        // Error durante el inicio de sesión
                                        Toast.makeText(Login.this, "Error al iniciar sesión. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }
    }
}
