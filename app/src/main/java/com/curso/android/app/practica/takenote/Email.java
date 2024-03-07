package com.curso.android.app.practica.takenote;

import static android.content.ContentValues.TAG;
import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
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

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        mAuth = FirebaseAuth.getInstance();

        //Obtener el usuario actual
        user = mAuth.getCurrentUser();

        Button buttonChangeEmail = findViewById(R.id.buttonChangeEmail);

        //Oir el click en el boton para cambiar email.
        buttonChangeEmail.setOnClickListener(v -> {
            EditText editEmail = findViewById(R.id.editTextNewEmail);
            String email = editEmail.getText().toString();
            updateEmail(email);
        });

        //Importacion y funcionalidad de imageView de Toolbar.
        ImageView iconoHome = findViewById(R.id.iconoHome);
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Email.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());

    }

    //Funcionalidad de cerrar sesion
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Email.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Metodo para actualizar el email.
    private void updateEmail(String newEmail) {
        if (user != null) {
            Log.d(TAG, "Iniciando proceso de actualización de email");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.change_email_password, null);
            EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);

            // Configurar el cuadro de diálogo
            builder.setView(dialogView)
                    .setTitle("Ingrese su contraseña")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        String password = editTextPassword.getText().toString();

                        if (user.getEmail() != null) {
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "El usuario se reautenticó con éxito");

                                            // El usuario se reautenticó con éxito, proceder con la actualización del correo electrónico
                                            user.verifyBeforeUpdateEmail(newEmail)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Log.d(TAG, "Email actualizado exitosamente");

                                                            // Enviar una notificación de éxito
                                                            sendEmailChangeNotification(newEmail);

                                                            // Cerrar sesión
                                                            mAuth.signOut();

                                                            // Iniciar la actividad de inicio de sesión
                                                            startActivity(new Intent(Email.this, Login.class));
                                                            finish(); // Finaliza esta actividad para evitar que el usuario regrese a ella con el botón "Atrás"
                                                        } else {
                                                            Exception e = task1.getException();
                                                            if (e != null) {
                                                                Log.d(TAG, "Error al actualizar el email: " + e.getMessage());
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Exception e = task.getException();
                                            if (e != null) {
                                                Log.d(TAG, "Error al reautenticar al usuario: " + e.getMessage());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "El correo electrónico del usuario es nulo");
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                        Log.d(TAG, "Proceso de actualización de email cancelado por el usuario");
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
