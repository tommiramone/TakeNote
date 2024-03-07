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

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

public class Contra extends AppCompatActivity {

    private static final String TAG = "ContraActivity";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contra);

        //Leyendo si el tema asignado es claro o no para activar el modo oscuro si es necesario.
        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //asignacion de usuario actual a User
        user = mAuth.getCurrentUser();

        //Llamada al boton de cambiar contraseña
        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);

        //Oir el click y en base a eso llevar a cabo varias acciones
        buttonChangePassword.setOnClickListener(v -> {
            EditText currentPasswordEditText = findViewById(R.id.editTextCurrentPassword);
            EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);

            String currentPassword = currentPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            reauthenticateAndChangePassword(currentPassword, newPassword);
        });

        //Importacion y funcionalidad de imageView de Toolbar
        ImageView iconoHome = findViewById(R.id.iconoHome);
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Contra.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());

    }

    //Funcionalidad de cerrar sesion.
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Contra.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    //Reautenticar para luego cambiar contraseña
    private void reauthenticateAndChangePassword(String currentPassword, String newPassword) {
        if (user == null || user.getEmail() == null) {
            // Manejar el caso en el que user o su correo electrónico sean nulos
            Log.e(TAG, "User or user email is null");
            return;
        }

        // Crear credencial para la reautenticación
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        // Reautenticar al usuario con la credencial
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Reautenticación exitosa, cambiar la contraseña
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Contraseña cambiada exitosamente
                                        Log.d(TAG, "Password updated successfully");
                                        // Cerrar sesión del usuario actual
                                        FirebaseAuth.getInstance().signOut();
                                        // Mostrar un mensaje de éxito
                                        Toast.makeText(Contra.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                                        // Redirigir a la pantalla de inicio de sesión
                                        Intent intent = new Intent(Contra.this, Login.class);
                                        startActivity(intent);
                                        finish(); // Cierra la actividad actual para que el usuario no pueda volver atrás
                                    } else {
                                        // Error al cambiar la contraseña
                                        if (task1.getException() != null) {
                                            Log.d(TAG, "Failed to update password: " + task1.getException().getMessage());
                                            handlePasswordChangeError(task1.getException());
                                        } else {
                                            // Aquí manejas el caso en el que getException() devuelve null
                                            Log.d(TAG, "Failed to update password: Exception is null");
                                            // Realiza alguna acción de manejo de errores o notifica al usuario
                                        }
                                    }
                                });
                    } else {
                        // Error en la reautenticación
                        if (task.getException() != null) {
                            Log.d(TAG, "Failed to reauthenticate user: " + task.getException().getMessage());
                            handlePasswordChangeError(task.getException());
                        } else {
                            // Aquí manejas el caso en el que getException() devuelve null
                            Log.d(TAG, "Failed to reauthenticate user: Exception is null");
                            // Realiza alguna acción de manejo de errores o notifica al usuario
                        }
                    }
                });
    }


    //Metodo para manejar los errores que pueden ocurrir durante el proceso de cambio de contraseña
    private void handlePasswordChangeError(Exception exception) {
        if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
            // Se requiere una autenticación reciente, redirigir al usuario a la pantalla de inicio de sesión
            Toast.makeText(Contra.this, "Se requiere autenticación reciente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Contra.this, Login.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        } else {
            // Mostrar un mensaje de error genérico
            Toast.makeText(Contra.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
        }
    }
}
