package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;

import java.security.SecureRandom;
import java.math.BigInteger;

public class Login extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        databaseHelper = new DatabaseHelper(this);

        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contraseniaEditText = findViewById(R.id.editTextPassword);
                EditText emailEditText = findViewById(R.id.editTextUsername);
                String contrasenia = contraseniaEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (validarCredenciales(email, contrasenia)) {
                    // Credenciales válidas, permitir acceso y generar token
                    String token = generarToken();
                    guardarTokenEnBaseDeDatos(email, token);

                    // Por ejemplo, iniciar una nueva actividad
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad de login para evitar volver atrás
                } else {
                    // Credenciales inválidas, mostrar mensaje de error
                    Toast.makeText(Login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validarCredenciales(String email, String contrasenia){
        boolean credencialesValidas = databaseHelper.login(email, contrasenia);

        if (credencialesValidas) {
            Log.d("LoginActivity", "Inicio de sesión exitoso para el usuario: " + email);
        } else {
            Log.d("LoginActivity", "Inicio de sesión fallido para el usuario: " + email);
        }

        return credencialesValidas;
    }

    // Método para generar un token aleatorio y seguro
    private String generarToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[64];
        secureRandom.nextBytes(tokenBytes);
        return new BigInteger(1, tokenBytes).toString(16); // Representación hexadecimal
    }

    // Método para guardar el token en la base de datos
    private void guardarTokenEnBaseDeDatos(String email, String token) {
       databaseHelper.guardarTokenUsuario(email, token);
    }
}
