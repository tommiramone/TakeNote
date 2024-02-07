package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText currentPasswordEditText = findViewById(R.id.editTextCurrentPassword);
                EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);

                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                reauthenticateAndChangePassword(currentPassword, newPassword);
            }
        });
    }

    private void reauthenticateAndChangePassword(String currentPassword, String newPassword) {
        // Crear credencial para la reautenticación
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        // Reautenticar al usuario con la credencial
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Reautenticación exitosa, cambiar la contraseña
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
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
                                                Log.d(TAG, "Failed to update password: " + task.getException().getMessage());
                                                handlePasswordChangeError(task.getException());
                                            }
                                        }
                                    });
                        } else {
                            // Error en la reautenticación
                            Log.d(TAG, "Failed to reauthenticate user: " + task.getException().getMessage());
                            handlePasswordChangeError(task.getException());
                        }
                    }
                });
    }

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
