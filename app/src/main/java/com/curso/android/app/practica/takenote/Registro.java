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

public class Registro extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        mAuth = FirebaseAuth.getInstance();

        EditText nombreEditText = findViewById(R.id.editTextUsername);
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText contraseniaEditText = findViewById(R.id.editTextPassword);
        Button buttonRegistro = findViewById(R.id.buttonRegister);

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String contrasenia = contraseniaEditText.getText().toString().trim();

                if (nombre.isEmpty() || email.isEmpty() || contrasenia.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, contrasenia)
                            .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registro.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Registro.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Registro.this, "Error al registrar usuario. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
