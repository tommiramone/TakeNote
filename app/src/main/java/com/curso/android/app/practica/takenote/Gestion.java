package com.curso.android.app.practica.takenote;


import static android.content.ContentValues.TAG;
import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.curso.android.app.practica.takenote.utils.temaUtils;
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

        //Oir el boton borrarCuenta y ejecutar su funcionalidad.
        borrarCuenta.setOnClickListener(v -> {
            //Obtener el usuario actual.
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                Log.e(TAG, "El usuario está autenticado.");

                // Construir el diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(Gestion.this);
                builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta?")
                        .setTitle("Confirmar eliminación de cuenta");

                // Agregar botón de confirmación
                builder.setPositiveButton("Sí", (dialog, id) -> eliminarCuentaConUID());

                // Agregar botón de cancelación
                builder.setNegativeButton("Cancelar", (dialog, id) -> {
                    // Usuario cancela la eliminación de la cuenta
                    dialog.dismiss(); // Cerrar el diálogo
                });

                // Mostrar el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // El usuario no está autenticado
                Log.e(TAG, "El usuario no está autenticado.");
                // Aquí puedes mostrar un mensaje al usuario o redirigirlo al inicio de sesión
            }


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
            Intent intent = new Intent(Gestion.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());



    }

    //Funcionalidad de cerrar esion
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Gestion.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Redireccionar a la pantalla de cambiar email
    public void cambiarEmail(View view) {
        Intent intent = new Intent(this, Email.class);
        startActivity(intent);
    }

    //Redireccionar a la pantalla de cambiar contraseña
    public void cambiarContrasena(View view) {
        Intent intent = new Intent(this, Contra.class);
        startActivity(intent);
    }

    //Metodo para eliminar la cuenta del usuario actual.
    private void eliminarCuentaConUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Eliminar la cuenta del usuario
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Gestion.this, Login.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual
                        } else {
                            // Error al eliminar la cuenta
                            Log.e(TAG, "Error al eliminar la cuenta: " + task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "No se pudo obtener el usuario actual para eliminar la cuenta.");
        }
    }

}


