package com.curso.android.app.practica.takenote;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Button buttonChangeEmail = findViewById(R.id.buttonChangeEmail);
        buttonChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editEmail = findViewById(R.id.editTextNewEmail);
                String email = editEmail.getText().toString();
                updateEmail(email);
            }
        });
    }

    private void updateEmail(String newEmail) {
        if (user != null) {
            Log.d(TAG, "Iniciando proceso de actualización de email");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.change_email_password, null);
            EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);

            // Configurar el cuadro de diálogo
            builder.setView(dialogView)
                    .setTitle("Ingrese su contraseña")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String password = editTextPassword.getText().toString();

                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "El usuario se reautenticó con éxito");

                                                // El usuario se reautenticó con éxito, proceder con la actualización del correo electrónico
                                                user.verifyBeforeUpdateEmail(newEmail)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "Email actualizado exitosamente");

                                                                    // Enviar una notificación de éxito
                                                                    sendEmailChangeNotification(newEmail);

                                                                    // Cerrar sesión
                                                                    mAuth.signOut();

                                                                    // Iniciar la actividad de inicio de sesión
                                                                    startActivity(new Intent(Email.this, Login.class));
                                                                    finish(); // Finaliza esta actividad para evitar que el usuario regrese a ella con el botón "Atrás"
                                                                } else {
                                                                    Exception e = task.getException();
                                                                    Log.d(TAG, "Error al actualizar el email: " + e.getMessage());
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d(TAG, "Error al reautenticar al usuario: " + task.getException().getMessage());
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Log.d(TAG, "Proceso de actualización de email cancelado por el usuario");
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Log.d(TAG, "El usuario es nulo");
        }
    }

    private void sendEmailChangeNotification(String newEmail) {
        Toast.makeText(Email.this, "Se ha enviado un correo electrónico de confirmación a " + newEmail, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Correo electrónico de confirmación enviado a " + newEmail);
    }



}
