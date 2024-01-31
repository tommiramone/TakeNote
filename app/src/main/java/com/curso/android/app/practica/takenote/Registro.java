package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.database.DatabaseHelper;

public class Registro extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        databaseHelper = new DatabaseHelper(this);

        Button buttonRegistro = findViewById(R.id.buttonRegister);

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreEditText = findViewById(R.id.editTextUsername);
                EditText emailEditText = findViewById(R.id.editTextEmail);
                EditText contraseniaEditText = findViewById(R.id.editTextPassword);

                String nombre = nombreEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String contrasenia = contraseniaEditText.getText().toString();

                registroUsuario(nombre, email, contrasenia);

                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void registroUsuario(String nombre, String email, String contrasenia){
        // Llama al método de DatabaseHelper para registrar un nuevo usuario
        databaseHelper.registrarUsuario(nombre, email, contrasenia);
    }

}
