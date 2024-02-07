package com.curso.android.app.practica.takenote;


import static android.content.ContentValues.TAG;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Gestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        Button borrarCuenta = findViewById(R.id.textViewBorrarCuenta);
        borrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    Log.e(TAG, "El usuario está autenticado.");

                    // Construir el diálogo de confirmación
                    AlertDialog.Builder builder = new AlertDialog.Builder(Gestion.this);
                    builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta?")
                            .setTitle("Confirmar eliminación de cuenta");


                    // Agregar botón de confirmación
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            eliminarCuentaConUID();
                        }
                    });

                    // Agregar botón de cancelación
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Usuario cancela la eliminación de la cuenta
                            dialog.dismiss(); // Cerrar el diálogo
                        }
                    });

                    // Mostrar el diálogo
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // El usuario no está autenticado
                    Log.e(TAG, "El usuario no está autenticado.");
                    // Aquí puedes mostrar un mensaje al usuario o redirigirlo al inicio de sesión
                }


            }
        });


    }

    public void cambiarEmail(View view) {
        Intent intent = new Intent(this, Email.class);
        startActivity(intent);
    }

    public void cambiarContrasena(View view) {
        Intent intent = new Intent(this, Contra.class);
        startActivity(intent);
    }

    private void eliminarCuentaConUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Eliminar la cuenta del usuario
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Gestion.this, Login.class);
                                startActivity(intent);
                                finish(); // Cierra la actividad actual
                            } else {
                                // Error al eliminar la cuenta
                                Log.e(TAG, "Error al eliminar la cuenta: " + task.getException());
                                // Aquí puedes mostrar un mensaje al usuario informando sobre el error
                            }
                        }
                    });
        } else {
            Log.e(TAG, "No se pudo obtener el usuario actual para eliminar la cuenta.");
        }
    }

}


